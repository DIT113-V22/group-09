package app;

import api.CarAPI;
import api.sensor.Infrared;
import api.sensor.Odometer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.glass.ui.Pixels;
import commands_processing.InputProcessor;
import exceptions.UnclearInputException;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import org.eclipse.paho.client.mqttv3.MqttException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
    private Button drawSendBtn;
    @FXML
    private Button drawClearBtn;
    @FXML
    private Canvas drawCanvas;
    @FXML
    private Tab drawTab;
    //end

    public Controller(){
        initialiseListeners();
        runLater(() -> App.getKeyboardHandler().setEnabled(keyBoardCheckBox.isSelected()));
        runLater(() -> manualTab.setOnSelectionChanged(event -> {
            if (manualTab.isSelected())checkSelectedTab();
            App.getKeyboardHandler().setEnabled(keyBoardCheckBox.isSelected());
        }));
        runLater(() -> autonomousTab.setOnSelectionChanged(event -> {

            if (autonomousTab.isSelected())checkSelectedTab();
            App.getKeyboardHandler().setEnabled(false);
        }));
        runLater(this::checkSelectedTab);
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
                else if (drawTab.equals(selectedTab)){
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

    public HashMap drawCanvas(){

        HashMap<Integer, Integer> points = new HashMap<>();
        GraphicsContext gc = drawCanvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        drawCanvas.setOnMousePressed(mouseEvent -> {
            gc.beginPath();
            gc.lineTo(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            gc.stroke();
            final double startingX = mouseEvent.getSceneX();
            final double startingY = mouseEvent.getSceneY();
            points.clear();
            points.put(0, 0);
            drawCanvas.setOnMouseDragged(mouseEvent2 -> {
                gc.lineTo(mouseEvent2.getSceneX(), mouseEvent2.getSceneY());
                gc.stroke();
                points.put((((int) (mouseEvent2.getSceneX()-startingX))), ((int) (mouseEvent2.getSceneY()-startingY)));
            });
        });
    return points;
    }

    public void clearCanvas(){
            drawCanvas.getGraphicsContext2D().clearRect(0,0, drawCanvas.getWidth(), drawCanvas.getHeight());
    }

    public void sendCanvas(){

        drawCanvas().forEach((key, value) -> {

        });

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