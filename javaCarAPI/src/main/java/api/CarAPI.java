package api;

import api.sensor.Infrared;
import api.sensor.Odometer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class CarAPI {

    private static final int MOVEMENT_SPEED = 70;
    private static final int STRAIGHT_ANGLE = 0;
    private static final int STEERING_ANGLE = 50;
    private static final int IDLE_SPEED = 0;
    private static final int QOS = 1;
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 240;
    private static final int DEFAULT_MQTT_PORT = 1883;
    private static final int PING_REQUEST_INTERVAL = 150;
    private static final int DISCONNECT_THRESHOLD = 1000;
    private static final int MQTT_TIME_OUT = 10;

    private String throttleControlTopic;
    private String steeringControlTopic;
    private String cameraFeedTopic;
    private String jsonCommandTopic;
    private String ultrasonicFrontTopic;
    private String gyroscopeTopic;
    private String infraredTopic;
    private String odometerSpeedTopic;
    private String odometerTotalDistanceTopic;
    private String heartbeatTopic;

    private final MqttClient client;
    private final String carName;
    private final String clientId;

    private long ping = 0;

    private final List<Consumer<Image>> videoFeedListeners = new ArrayList<>();
    private final List<Consumer<Double>> ultraSonicListeners = new ArrayList<>();
    private final List<Consumer<Double>> gyroscopeListeners = new ArrayList<>();
    private final Map<Infrared,List<Consumer<Double>>> infraredListeners = new HashMap<>();
    private final Map<Odometer,List<Consumer<Double>>> odometerSpeedListeners = new HashMap<>();
    private final Map<Odometer,List<Consumer<Double>>> odometerTotalDistanceListeners = new HashMap<>();

    public CarAPI(String host, String carName) throws MqttException{
        this(host, UUID.randomUUID().toString(),carName);
    }

    public CarAPI (String host,String clientId,String carName) throws MqttException{
        this(host,DEFAULT_MQTT_PORT,clientId,carName);
    }

    public CarAPI (String host,int port,String clientId,String carName) throws MqttException{
        this(host,port,clientId,carName,null,null);
    }

    public CarAPI (String host,int port,String clientId,String carName,String user, char [] password) throws MqttException{
        this.carName = carName;
        this.clientId = clientId;

        initMQTTVariables();

        String hostURL = "tcp://"+host+":"+port;
        client = new MqttClient(hostURL,clientId, new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir")));
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(MQTT_TIME_OUT);
        if (user != null && password != null) {
            options.setUserName(user);
            options.setPassword(password);
        }
        client.connect(options);

        subscribe();
        startHeartbeatThread();

    }

    private void startHeartbeatThread() {
        Thread thread = new Thread(() -> {
            while (client.isConnected()) {
                try {
                    MqttMessage currentTime = new MqttMessage(Long.toString(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
                    client.publish(heartbeatTopic,currentTime);
                    Thread.sleep(PING_REQUEST_INTERVAL);
                } catch (Exception ignore) {}
            }
        });
        thread.start();
    }

    private void subscribe() throws MqttException{

        client.subscribe(ultrasonicFrontTopic,(topic,message) -> ultraSonicListeners.forEach(callback -> callback.accept(convertPayloadToDouble(message))));

        client.subscribe(gyroscopeTopic, (topic,message) -> gyroscopeListeners.forEach(callback -> callback.accept(convertPayloadToDouble(message))));

        client.subscribe(heartbeatTopic, (topic,message) -> ping = (System.currentTimeMillis() - Long.parseLong(new String(message.getPayload()))));

        client.subscribe(cameraFeedTopic, (topic, message) -> {
            if (videoFeedListeners.size() == 0) return; //avoids converting the image if there is no listeners asking for it
            Image image = convertImage(message.getPayload());
            videoFeedListeners.forEach(callback -> callback.accept(image));
        });

        client.subscribe(infraredTopic, (topic,message) -> {
            Infrared infrared = null;
            if (topic.endsWith("front")) infrared = Infrared.FRONT;
            else if (topic.endsWith("back")) infrared = Infrared.BACK;
            else if (topic.endsWith("right")) infrared = Infrared.RIGHT;
            else if (topic.endsWith("left")) infrared = Infrared.LEFT;

            if (infrared == null) return;

            infraredListeners.computeIfAbsent(infrared, i -> new ArrayList<>()).forEach(callback -> callback.accept(convertPayloadToDouble(message)));
        });

        startOdometerListener(odometerSpeedTopic, odometerSpeedListeners);
        startOdometerListener(odometerTotalDistanceTopic, odometerTotalDistanceListeners);

    }

    private void startOdometerListener(String odometerTopic, Map<Odometer, List<Consumer<Double>>> odometerListener) throws MqttException {
        client.subscribe(odometerTopic, (topic, message) -> {
            Odometer odometer = null;
            if (topic.endsWith("right")) odometer = Odometer.RIGHT;
            else if (topic.endsWith("left")) odometer = Odometer.LEFT;
            if (odometer == null) return;

            odometerListener.computeIfAbsent(odometer , o -> new ArrayList<>()).forEach(callback -> callback.accept(convertPayloadToDouble(message)));
        });
    }

    private void initMQTTVariables() {
        throttleControlTopic = "/" + carName + "/control/throttle";
        steeringControlTopic = "/" + carName + "/control/steering";
        cameraFeedTopic = "/" + carName + "/camera";
        jsonCommandTopic = "/" + carName + "/jcmd";
        ultrasonicFrontTopic = "/" + carName +"/ultrasound/front";
        infraredTopic = "/" + carName + "/infrared/#"; //front , left , right , back
        odometerSpeedTopic = "/" + carName + "/odometer/speed/#"; //left , right
        odometerTotalDistanceTopic = "/" + carName + "/odometer/totalDistance/#"; // left , right
        gyroscopeTopic =  "/" + carName + "/gyroscope";
        heartbeatTopic = "/" + carName + "/heartbeat";
    }

    private Image convertImage(byte [] payload){
        final Color[] colors = new Color[IMAGE_WIDTH * IMAGE_HEIGHT];
        for (int ci = 0; ci < colors.length; ci++) {
            final int r =  (payload[3 * ci ] + 256 ) % 256;
            final int g =  (payload[3 * ci + 1] + 256) % 256;
            final int b =  (payload[3 * ci + 2] + 256) % 256;
            colors[ci] = Color.rgb(r,g,b);
        }
        WritableImage wi = new WritableImage(IMAGE_WIDTH,IMAGE_HEIGHT);
        PixelWriter pw = wi.getPixelWriter();

        int k = 0;
        for (int x = 0; x < IMAGE_HEIGHT; x++) {
            for (int y = 0; y < IMAGE_WIDTH; y++) {
                pw.setColor(y, x, colors[k]);
                k++;
            }
        }
        return wi;
    }


    private void drive(int throttleSpeed, int steeringAngle) throws MqttException {
        if (!client.isConnected()) {
            final String notConnected = "Not connected (yet)";
            System.out.println(notConnected); 
            return;
        }
        client.publish(throttleControlTopic, Integer.toString(throttleSpeed).getBytes(StandardCharsets.UTF_8), QOS ,false);
        client.publish(steeringControlTopic, Integer.toString(steeringAngle).getBytes(StandardCharsets.UTF_8), QOS ,false);

    }

    public void moveForward() throws MqttException {
        drive(MOVEMENT_SPEED, STRAIGHT_ANGLE);
    }

    public void moveForwardLeft() throws MqttException {
        drive(MOVEMENT_SPEED, -STEERING_ANGLE);
    }

    public void stop() throws MqttException {
        drive(IDLE_SPEED, STRAIGHT_ANGLE);
    }

    public void moveForwardRight() throws MqttException {
        drive(MOVEMENT_SPEED, STEERING_ANGLE);
    }

    public void moveBackward() throws MqttException {
        drive(-MOVEMENT_SPEED, STRAIGHT_ANGLE);
    }

    public boolean isConnected(){
        return client.isConnected();
    }

    public boolean isCarConnected(){
        return getPing() < DISCONNECT_THRESHOLD;
    }

    public int getPing(){
        return (int)ping;
    }

    public void sendJsonCommand(String json) throws MqttException{
        client.publish(jsonCommandTopic,new MqttMessage(json.getBytes(StandardCharsets.UTF_8)));
    }

    public String getClientId(){
        return clientId;
    }


    public void addUltraSonicListener(Consumer<Double> callback){
        ultraSonicListeners.add(callback);
    }

    public void addGyroscopeListener(Consumer<Double> callback){
        gyroscopeListeners.add(callback);
    }

    public void addVideoFeedListener(Consumer<Image> callback){
        this.videoFeedListeners.add(callback);
    }

    public void addInfraredListener(Infrared infrared, Consumer<Double> callback){
        List<Consumer<Double>> listeners = infraredListeners.computeIfAbsent(infrared, i -> new ArrayList<>());
        listeners.add(callback);
    } 

    public void addOdometerSpeedListener(Odometer odometer,Consumer<Double> callback){
        List<Consumer<Double>> listeners = odometerSpeedListeners.computeIfAbsent(odometer, o -> new ArrayList<>());
        listeners.add(callback);
    }

    public void addOdometerTotalDistanceListener(Odometer odometer,Consumer<Double> callback){
        List<Consumer<Double>> listeners = odometerTotalDistanceListeners.computeIfAbsent(odometer, o -> new ArrayList<>());
        listeners.add(callback);
    }

    private Double convertPayloadToDouble(MqttMessage message){
        return Double.valueOf(new String(message.getPayload()));
    }

}