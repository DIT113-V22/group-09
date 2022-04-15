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

    private String ultrasonicFront;
    private String gyroscope;
    private String infrared;
    private String odometerSpeed;
    private String odometerTotalDistance;
    private String heartbeat;

    public MockCar(String server,int port,String carName) throws MqttException {

        this.carName = carName;
        initMQTTVariables();
        String clientId = UUID.randomUUID().toString();
        String serverAddress = "tcp://"+server+":"+port;
        client = new MqttClient(serverAddress,clientId,new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir")));
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
    }

    public void sendGyroscopeData(double value) throws MqttException{
        client.publish(gyroscope,convertDouble(value));
    }

    public void sendUltraSonicData(double value) throws MqttException {
        client.publish(ultrasonicFront,convertDouble(value));
    }

    public void sendOdometerSpeedData(Odometer odometer, double value) throws MqttException {
        client.publish(odometerSpeed+odometer.toString(),convertDouble(value));
    }
    public void sendOdometerTotalDistanceData(Odometer odometer, double value) throws MqttException{
        client.publish(odometerTotalDistance+odometer.toString(),convertDouble(value));
    }

    public void sendInfraredData(Infrared infrared, double value) throws MqttException{
        client.publish(this.infrared+infrared.toString(),convertDouble(value));
    }

    public void sendHeartBeat(long value) throws MqttException{
        client.publish(heartbeat,new MqttMessage(Long.toString(value).getBytes(StandardCharsets.UTF_8)));
    }

    private void initMQTTVariables() {
        ultrasonicFront = "/" + carName +"/ultrasound/front";
        infrared = "/" + carName + "/infrared/#"; //front , left , right , back
        odometerSpeed = "/" + carName + "/odometer/speed/#"; //left , right
        odometerTotalDistance = "/" + carName + "/odometer/totalDistance/#"; // left , right
        gyroscope =  "/" + carName + "/gyroscope";
        heartbeat = "/" + carName + "/heartbeat";
    }

    private MqttMessage convertDouble(double value){
        return new MqttMessage(Double.toString(value).getBytes(StandardCharsets.UTF_8));
    }


}
