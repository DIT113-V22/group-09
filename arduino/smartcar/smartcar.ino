#include <vector>

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
const auto maxDistance = 400;
SR04 front(arduinoRuntime, triggerPin, echoPin, maxDistance);

std::vector<char> frameBuffer;

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

bool manualControl = true;

void setup() {
    Serial.begin(9600);
#ifdef __SMCE__
    Camera.begin(QVGA, RGB888, 15);
  frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
#endif

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
            if (message == "manual") manualControl = true;
            else if (message == "auto") manualControl = false;
        }
        else {
            Serial.println(topic + " " + message);
        }
    });
}

void loop() {
    if (mqtt.connected()) {
        mqtt.loop();
        const auto currentTime = millis();
#ifdef __SMCE__
        static auto previousFrame = 0UL;
    if (currentTime - previousFrame >= 65) {
      previousFrame = currentTime;
      Camera.readFrame(frameBuffer.data());
      mqtt.publish(CAMERA_FEED_TOPIC, frameBuffer.data(), frameBuffer.size(),false, 0);
    }
#endif
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
    }
#ifdef __SMCE__
    // Avoid over-using the CPU if we are running in the emulator
  delay(1);
#endif
}