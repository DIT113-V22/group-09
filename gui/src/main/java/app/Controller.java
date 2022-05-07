package app;

import api.CarAPI;
import api.sensor.Infrared;
import api.sensor.Odometer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import commands_processing.InputProcessor;
import exceptions.UnclearInputException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Controller {

    private final CarAPI carAPI = App.getCarAPI();

    @FXML
    private TabPane tabPane;

    //manual tab stuff
    @FXML
    private Tab manualTab;

    @FXML
    private ImageView videoFeed;

    @FXML
    private CheckBox keyBoardCheckBox;

    @FXML
    private Label ultraSonic;

    @FXML
    private Label frontInfrared;
    @FXML
    private Label backInfrared;
    @FXML
    private Label leftInfrared;
    @FXML
    private Label rightInfrared;

    @FXML
    private Label rightOdometerSpeed;
    @FXML
    private Label leftOdometerSpeed;

    @FXML
    private Label rightOdometerTotalDistance;
    @FXML
    private Label leftOdometerTotalDistance;

    @FXML
    private Label gyroscope;
    //end


    //autonomous tab stuff
    @FXML
    private Tab autonomousTab;

    @FXML
    private ImageView videoFeed2;

    @FXML
    private Label pingVal;

    @FXML
    private TextArea commandInfo;
    @FXML
    private TextArea commandBox;

    @FXML
    private Button sendBtn;
    @FXML
    private Button clearBtn;
    //end

    public Controller(){
        initialiseListeners();
    }

    private void checkSelectedTab(){
        runLater(() -> {
            try {
                Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                if (manualTab.equals(selectedTab)) {
                    carAPI.setManualMode(true);
                } else if (autonomousTab.equals(selectedTab)) {

                    carAPI.setManualMode(false);
                }
            }
            catch (MqttException e){
                e.printStackTrace();
            }
        });
    }

    private void initialiseListeners(){

        carAPI.addVideoFeedListener(image -> runLater(() -> videoFeed.setImage(image)));
        carAPI.addVideoFeedListener(image -> runLater(() ->videoFeed2.setImage(image)));

        carAPI.addUltraSonicListener(distance -> runLater(() -> ultraSonic.setText(doubleToString(distance))));

        carAPI.addGyroscopeListener(degrees -> runLater(() -> gyroscope.setText(doubleToString(degrees))));

        carAPI.addInfraredListener(Infrared.FRONT, distance -> runLater(() -> frontInfrared.setText(doubleToString(distance))));
        carAPI.addInfraredListener(Infrared.BACK, distance -> runLater(() -> backInfrared.setText(doubleToString(distance))));
        carAPI.addInfraredListener(Infrared.LEFT, distance -> runLater(() -> leftInfrared.setText(doubleToString(distance))));
        carAPI.addInfraredListener(Infrared.RIGHT, distance -> runLater(() -> rightInfrared.setText(doubleToString(distance))));

        carAPI.addOdometerSpeedListener(Odometer.LEFT, speed -> runLater(() -> leftOdometerSpeed.setText(doubleToString(speed))));
        carAPI.addOdometerSpeedListener(Odometer.RIGHT, speed -> runLater(() -> rightOdometerSpeed.setText(doubleToString(speed))));

        carAPI.addOdometerTotalDistanceListener(Odometer.LEFT, totalDistance -> runLater(() -> leftOdometerTotalDistance.setText(doubleToString(totalDistance))));
        carAPI.addOdometerTotalDistanceListener(Odometer.RIGHT, totalDistance -> runLater(() -> rightOdometerTotalDistance.setText(doubleToString(totalDistance))));

        startPingUpdateThread();
    }

    private void startPingUpdateThread(){
        Thread updater = new Thread(() ->{
            while (true) {
                int ping = carAPI.getPing();
                runLater(() -> pingVal.setText(Integer.toString(ping)));
                try {
                    Thread.sleep(1111);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updater.start();
    }

    public void forward() {
        try {
            carAPI.moveForward();
        }
        catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void stop() {
        try {
            carAPI.stop();
        }
        catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void right() {

        try {
            carAPI.moveForwardRight();
        }
        catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void left() {
        try {
            carAPI.moveForwardLeft();
        }
        catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void backward() {
        try {
            carAPI.moveBackward();
        }
        catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void sendCommand(){
        String phrase = commandBox.getText();
        System.out.println(phrase);
        InputProcessor inputProcessor = new InputProcessor();
        try {
            String csv = inputProcessor.processInput(phrase);
            String json = inputProcessor.getLatestCommands();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(json);
            String prettyJsonString = gson.toJson(je);
            commandInfo.appendText(phrase + " \n->\n " + prettyJsonString+ " \n->\n ");
            StringBuilder csvDisplay = new StringBuilder();
            if (csv.contains(";")){
                String [] split = csv.split(";");
                for (String s : split) {
                    csvDisplay.append(s).append("\n");
                }
            }
            else csvDisplay.append(csv);
            commandInfo.appendText(csvDisplay.toString());
            commandInfo.appendText("\n-------------\n\n");
            System.out.println(inputProcessor.getLatestCommands());
            System.out.println("CSV:"+csv);
            carAPI.sendCSVCommand(csv);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        catch (UnclearInputException e){
            commandInfo.appendText(phrase + " \n->\n " + "INVALID COMMAND");
            commandInfo.appendText("\n-------------\n\n");
        }
    }

    public void clearCommandBox(){
        commandBox.setText("");
        commandInfo.setText("");
    }

    public void checkBoxToggle(){

    }

    private void runLater(Runnable runnable){
        Platform.runLater(runnable);
    }

    private String doubleToString(Double value){
        return Double.toString(value);
    }

}