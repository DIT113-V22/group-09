#include <Smartcar.h>

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);
SR04 sensor (arduinoRuntime, 6, 7, 70);
SimpleCar car(control);

void setup() {
    // put your setup code here, to run once:
    Serial.begin(9600);
    car.setSpeed(70);
}

void loop() {
    // put your main code here, to run repeatedly:
    int distance = sensor.getDistance();
    if(distance != 0 && distance < 70)
    {
        car.setSpeed(0);
    }
}
