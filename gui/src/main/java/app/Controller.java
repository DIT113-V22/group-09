package app;

import api.CarAPI;
import api.sensor.*;
import api.sensor.Infrared;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Controller {

    private final CarAPI carAPI = App.getCarAPI();

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

    public Controller(){
        initialiseListeners();
        Platform.runLater(() -> App.getKeyboardHandler().setEnabled(keyBoardCheckBox.isSelected()));
    }

    private void initialiseListeners(){

        carAPI.addVideoFeedListener(image -> runLater(() -> videoFeed.setImage(image)));

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

    public void checkBoxToggle(){
        App.getKeyboardHandler().setEnabled(keyBoardCheckBox.isSelected());
    }

    private void runLater(Runnable runnable){
        Platform.runLater(runnable);
    }

    private String doubleToString(Double value){
        return Double.toString(value);
    }

}