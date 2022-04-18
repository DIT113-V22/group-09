package apitest;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import api.sensor.Infrared;
import api.sensor.Odometer;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MockCar {

    private final MqttClient client;
    private final String carName;

    private String ultrasonicFrontTopic;
    private String gyroscopeTopic;
    private String infraredTopic;
    private String odometerSpeedTopic;
    private String odometerTotalDistanceTopic;
    private String heartbeatTopic;

    public MockCar(String server,int port,String carName) throws MqttException {

        this.carName = carName;
        initMQTTVariables();
        String clientId = UUID.randomUUID().toString();
        String serverAddress = "tcp://"+server+":"+port;
        client = new MqttClient(serverAddress,clientId,new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir")));
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        client.connect(options);
    }

    public void sendGyroscopeData(double value) throws MqttException{
        client.publish(gyroscopeTopic,convertDouble(value));
    }

    public void sendUltraSonicData(double value) throws MqttException {
        client.publish(ultrasonicFrontTopic,convertDouble(value));
    }

    public void sendOdometerSpeedData(Odometer odometer, double value) throws MqttException {
        client.publish(odometerSpeedTopic+odometer.toString(),convertDouble(value));
    }
    public void sendOdometerTotalDistanceData(Odometer odometer, double value) throws MqttException{
        client.publish(odometerTotalDistanceTopic+odometer.toString(),convertDouble(value));
    }

    public void sendInfraredData(Infrared infrared, double value) throws MqttException{
        client.publish(this.infraredTopic+infrared.toString(),convertDouble(value));
    }

    public void sendHeartBeat(long value) throws MqttException{
        client.publish(heartbeatTopic,new MqttMessage(Long.toString(value).getBytes(StandardCharsets.UTF_8)));
    }

    private void initMQTTVariables() {
        ultrasonicFrontTopic = "/" + carName +"/ultrasound/front";
        infraredTopic = "/" + carName + "/infrared/";
        odometerSpeedTopic = "/" + carName + "/odometer/speed/";
        odometerTotalDistanceTopic = "/" + carName + "/odometer/totalDistance/";
        gyroscopeTopic =  "/" + carName + "/gyroscope";
        heartbeatTopic = "/" + carName + "/heartbeat";
    }

    private MqttMessage convertDouble(double value){
        return new MqttMessage(Double.toString(value).getBytes(StandardCharsets.UTF_8));
    }


}
