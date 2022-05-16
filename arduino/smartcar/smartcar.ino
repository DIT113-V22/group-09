#include <vector>
#include <deque>
#include <MQTT.h>
#include <WiFi.h>
#ifdef __SMCE__
#include <OV767X.h>
#endif

#include <Smartcar.h>

MQTTClient mqtt;
WiFiClient net;

const char ssid[] = "***";
const char pass[] = "****";

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

const int GYROSCOPE_OFFSET = 37;
GY50 gyro(arduinoRuntime, GYROSCOPE_OFFSET);

const unsigned long PULSES_PER_METER = 600;
DirectionalOdometer leftOdometer{ arduinoRuntime,
                                  smartcarlib::pins::v2::leftOdometerPins,
                                  []() { leftOdometer.update(); },
                                  PULSES_PER_METER };

DirectionalOdometer rightOdometer{ arduinoRuntime,
                                   smartcarlib::pins::v2::rightOdometerPins,
                                   []() { rightOdometer.update(); },
                                   PULSES_PER_METER };

GP2Y0A21 frontIR(arduinoRuntime, 0);
GP2Y0A21 leftIR(arduinoRuntime, 1);
GP2Y0A21 rightIR(arduinoRuntime, 2);
GP2Y0A21 backIR(arduinoRuntime, 3);

SmartCar smartCar(arduinoRuntime,control,gyro,leftOdometer,rightOdometer);

const auto oneSecond = 1000UL;
#ifdef __SMCE__
const auto triggerPin = 6;
const auto echoPin = 7;
const auto mqttBrokerUrl = "127.0.0.1";
#else
const auto triggerPin = 33;
const auto echoPin = 32;
const auto mqttBrokerUrl = "192.168.0.40";
#endif

std::vector<char> frameBuffer;

const auto maxDistance = 400;
SR04 front(arduinoRuntime, triggerPin, echoPin, maxDistance);

String CAR_NAME = "/smartcar";

const String CSV_COMMAND_TOPIC = CAR_NAME + "/csvcmd";
const String THROTTLE_CONTROL_TOPIC = CAR_NAME + "/control/throttle";
const String STEERING_CONTROL_TOPIC = CAR_NAME + "/control/steering";
const String HEARTBEAT_TOPIC = CAR_NAME + "/heartbeat";
const String HEARTBEAT_RESPONSE_TOPIC = CAR_NAME + "/heartbeat/response";
const String ULTRASONIC_FRONT_TOPIC = CAR_NAME + "/ultrasound/front";
const String INFRARED_TOPIC_BASE = CAR_NAME + "/infrared/";
const String INFRARED_FRONT_TOPIC = INFRARED_TOPIC_BASE + "/front";
const String INFRARED_BACK_TOPIC = INFRARED_TOPIC_BASE + "/back";
const String INFRARED_LEFT_TOPIC = INFRARED_TOPIC_BASE + "/left";
const String INFRARED_RIGHT_TOPIC = INFRARED_TOPIC_BASE + "/right";
const String ODOMETER_SPEED_TOPIC_BASE = CAR_NAME + "/odometer/speed";
const String ODOMETER_SPEED_RIGHT_TOPIC = ODOMETER_SPEED_TOPIC_BASE + "/right";
const String ODOMETER_SPEED_LEFT_TOPIC = ODOMETER_SPEED_TOPIC_BASE + "/left";
const String ODOMETER_TOTAL_DISTANCE_TOPIC_BASE = CAR_NAME + "/odometer/totalDistance";
const String ODOMETER_TOTAL_DISTANCE_RIGHT_TOPIC = ODOMETER_TOTAL_DISTANCE_TOPIC_BASE + "/right";
const String ODOMETER_TOTAL_DISTANCE_LEFT_TOPIC = ODOMETER_TOTAL_DISTANCE_TOPIC_BASE + "/left";
const String CAMERA_FEED_TOPIC = CAR_NAME + "/camera";
const String CONTROL_MODE_TOPIC = CAR_NAME + "/controlMode";
const String GYROSCOPE_TOPIC = CAR_NAME + "/gyroscope";
const String COMMAND_STATE_TOPIC = CAR_NAME + "/commandState";
const String EMERGENCY_DETECTION_TOPIC = CAR_NAME + "/emergencyDetection";

bool manualControl = true;
bool emergencyCheck = false;

 struct VehicleState {
    unsigned long lOdometerDistance;
    unsigned long rOdometerDistance;
    unsigned long time;
    int heading;
    long distance;
} ;

struct Command{
    int lWheel;
    int rWheel;
    int amount;
    String type;
    bool isExecuted;
    VehicleState initialState;
    String textCommand;
};

std::deque<Command> commandsDqe;
Command currentCommand;



void parseCSV(String message){
    std::vector<String> stringCommands;
    if(message.indexOf(";") == -1){ //single command
        stringCommands.push_back(message);
    }
    else { //command batch
        stringCommands = split(message,";");
    }

    for(String strCmd : stringCommands){ //this always assume we send with the right formatting. give it something random and it'll explode :)
        std::vector<String> parsedCmd = split(strCmd,",");
        Command command;
        command.lWheel = parsedCmd.at(0).toInt();
        command.rWheel = parsedCmd.at(1).toInt();
        command.amount = parsedCmd.at(2).toInt();
        command.type = parsedCmd.at(3);
        command.isExecuted = false;
        command.textCommand = strCmd;
        commandsDqe.push_back(command);
    }
}

void stopCommands(){
    car.setSpeed(0);
    while(!commandsDqe.empty())commandsDqe.pop_front();
    currentCommand.isExecuted = true;
}

void handleMqttMessage(String topic, String message) {

    if (topic == HEARTBEAT_TOPIC){
        mqtt.publish(HEARTBEAT_RESPONSE_TOPIC,message);
    }
    else if (topic == THROTTLE_CONTROL_TOPIC) {
        if (manualControl){
            car.setSpeed(message.toInt());
        }
    }
    else if (topic == STEERING_CONTROL_TOPIC) {
        if (manualControl){
            car.setAngle(message.toInt());
        }
    }
    else if(topic == CONTROL_MODE_TOPIC){
        if (message == "manual") {
            manualControl = true;
            stopCommands();
        }
        else if (message == "auto"){
            manualControl = false;
            car.setSpeed(0);
        }
    }
    else if (topic == CSV_COMMAND_TOPIC) {
        parseCSV(message); //this always assume we're giving an input with right formatting. atm no one knows what happens if the format is messed up
        manualControl = false;
    }
    else if(topic == EMERGENCY_DETECTION_TOPIC){
        if(message == "true")emergencyCheck= true;
        else if(message == "false")emergencyCheck = false;
    }
    else {
        Serial.println(topic + " " + message);
    }
}


struct VehicleState getCurrentState(){
    VehicleState state;
    state.rOdometerDistance = rightOdometer.getDistance();
    state.lOdometerDistance = leftOdometer.getDistance();
    gyro.update();
    state.heading = gyro.getHeading();
    state.distance = smartCar.getDistance();
    state.time = millis();
    return state;
}


void setup() {
    Serial.begin(9600);
#ifdef __SMCE__
    Camera.begin(QVGA, RGB888, 15);
  frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
#endif

    currentCommand.isExecuted = true; //remove this line and everything will go to hell.

    WiFi.begin(ssid, pass);
    mqtt.begin(mqttBrokerUrl, 1883, net);

    Serial.println("Connecting to WiFi...");
    auto wifiStatus = WiFi.status();
    while (wifiStatus != WL_CONNECTED && wifiStatus != WL_NO_SHIELD) {
        Serial.println(wifiStatus);
        Serial.print(".");
        delay(1000);
        wifiStatus = WiFi.status();
    }

    Serial.println(wifiStatus);

    Serial.println("Connecting to MQTT broker");
    while (!mqtt.connect("arduino", "public", "public")) {
        Serial.print(".");
        delay(1000);
    }

    mqtt.subscribe("/smartcar/control/#", 1);
    mqtt.subscribe(CSV_COMMAND_TOPIC,1);
    mqtt.subscribe(CONTROL_MODE_TOPIC,1);
    mqtt.subscribe(HEARTBEAT_TOPIC,0);
    mqtt.onMessage([](String topic, String message) {
        handleMqttMessage(topic, message);
    });
}

unsigned long pauseTime = 0;
void _pause(){
    pauseTime = millis() + 500; //half a second pause
}

void emergencyDetection(){
    auto distance = front.getDistance();
    int stopThreshold = 50;
    rightOdometer.update();

    if(rightOdometer.getDirection() > 0 && rightOdometer.getSpeed() > 0 && distance > 0 && distance < stopThreshold){
        stopCommands();
    }
}

String stateToString(struct VehicleState state){
    //startRWheel,startLWheel,startDistance,startHeading,startTime;endRWheel,endLWheel,endDistance,endHeading,endTime;%command%

    /*struct VehicleState {
    unsigned long lOdometerDistance;
    unsigned long rOdometerDistance;
    unsigned long time;
    int heading;
    long distance;
} ;*/

    String stateString =
            String(state.rOdometerDistance) +
            "," + String(state.lOdometerDistance) +
            "," + String(state.distance) +
            "," + String(state.heading) +
            "," + String(state.time);

    return stateString;
}

void commandExecuted() {
    smartCar.setSpeed(0);
    currentCommand.isExecuted = true;
    VehicleState startState = currentCommand.initialState;
    VehicleState endState = getCurrentState();
    String textCommand = currentCommand.textCommand;
    String commandState = stateToString(startState) + ";" +stateToString(endState) + ";" + textCommand;
    mqtt.publish(COMMAND_STATE_TOPIC,commandState);
    _pause();
}

void executeCurrentCommand(){
    String taskType = currentCommand.type;
    int amount = currentCommand.amount;
    VehicleState initState = currentCommand.initialState;
    if (taskType == "DISTANCE"){
        long currentDistance = smartCar.getDistance();
        if (amount>0){
            int offset = 10;
            if (abs(currentDistance-initState.distance)>=amount-offset){
                commandExecuted();
            }
            else {
                smartCar.overrideMotorSpeed(currentCommand.lWheel,currentCommand.rWheel);
            }
        }
        else {
            commandExecuted();
        }
    }
    else if (taskType == "TIME"){
        int currentTime = millis();
        if (currentTime>initState.time+amount*1000){
            commandExecuted();
        }
        else {
            smartCar.overrideMotorSpeed(currentCommand.lWheel,currentCommand.rWheel);
        }
    }
    else if (taskType == "ANGULAR"){ //todo improve
        gyro.update();
        int currentHeading = gyro.getHeading();
        int targetDegree = amount % 360;
        if (abs(initState.heading - currentHeading) >= targetDegree){
            commandExecuted();
        }
        else {
            smartCar.overrideMotorSpeed(currentCommand.lWheel,currentCommand.rWheel);
        }
    }
}

void loop() {
    if (mqtt.connected()) {
        mqtt.loop();
        const auto currentTime = millis();

        static auto previousTransmission = 0UL;
        if (currentTime - previousTransmission >= oneSecond) {
            previousTransmission = currentTime;
            const auto distance = String(front.getDistance());
            mqtt.publish(ULTRASONIC_FRONT_TOPIC, distance);
            gyro.update();
            mqtt.publish(GYROSCOPE_TOPIC,String(gyro.getHeading()));

            mqtt.publish(INFRARED_FRONT_TOPIC,String(frontIR.getDistance()));
            mqtt.publish(INFRARED_LEFT_TOPIC,String(leftIR.getDistance()));
            mqtt.publish(INFRARED_RIGHT_TOPIC,String(rightIR.getDistance()));
            mqtt.publish(INFRARED_BACK_TOPIC,String(backIR.getDistance()));

            mqtt.publish(ODOMETER_SPEED_LEFT_TOPIC,String(leftOdometer.getSpeed()));
            mqtt.publish(ODOMETER_SPEED_RIGHT_TOPIC,String(rightOdometer.getSpeed()));

            mqtt.publish(ODOMETER_TOTAL_DISTANCE_LEFT_TOPIC,String(leftOdometer.getDistance()));
            mqtt.publish(ODOMETER_TOTAL_DISTANCE_RIGHT_TOPIC,String(rightOdometer.getDistance()));
        }

#ifdef __SMCE__
        static auto previousFrame = 0UL;
        if (currentTime - previousFrame >= 65) {
            previousFrame = currentTime;
            Camera.readFrame(frameBuffer.data());
            mqtt.publish("/smartcar/camera", frameBuffer.data(), frameBuffer.size(),false, 0); //todo smartcar name should be fixed
        }
#endif

        if(emergencyCheck){
            emergencyDetection();
        }

        if(!manualControl) {
            if(currentCommand.isExecuted && !commandsDqe.empty()) {
                currentCommand = commandsDqe.front();
                commandsDqe.pop_front();
                currentCommand.initialState = getCurrentState();
            }

            if(!currentCommand.isExecuted && millis() > pauseTime){
                executeCurrentCommand();
            }
        }


    }
#ifdef __SMCE__
    // Avoid over-using the CPU if we are running in the emulator
  delay(1);
#endif
}
