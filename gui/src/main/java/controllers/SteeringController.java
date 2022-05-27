package controllers;

import api.CarAPI;
import api.sensor.Infrared;
import api.sensor.Odometer;
import app.MovementHandler;
import commands_processing.InputProcessor;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import exceptions.UnclearInputException;
import file_processing.FileLoader;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import app.CarGUI;

public class SteeringController {

    private List<Node> infoNodes;
    private WindowModes mode;
    private WindowModes previousMode;
    private boolean sideShown;


    private List<String> languageList;

    @FXML private Label info_label;
    @FXML private Label a_info_label;

    @FXML private ScrollPane txt_scrl;
    @FXML private TextFlow txt_out;

    @FXML private Tab manual_tab;
    @FXML private Tab autonomous_tab;
    @FXML private Tab settings_tab;


    @FXML private TabPane main_pane;

    @FXML private HBox root_box;
    @FXML private HBox a_root_box;
    @FXML private HBox s_root_box;

    @FXML private AnchorPane side_anchor;
    @FXML private AnchorPane a_side_anchor;
    @FXML private AnchorPane s_side_anchor;

    @FXML private AnchorPane root_anchor;

    @FXML private Label m_gyro;
    
    @FXML private Label m_f_inf;
    @FXML private Label m_b_inf;
    @FXML private Label m_l_inf;
    @FXML private Label m_r_inf;

    @FXML private Label m_f_ult;
    
    @FXML private Label m_l_odo;
    @FXML private Label m_r_odo;
    @FXML private Label m_tot_l_odo;
    @FXML private Label m_tot_r_odo;
    
    @FXML private Label m_x_coo;
    @FXML private Label m_y_coo;
    @FXML private Label m_ping;

    @FXML private ImageView m_vid;
    @FXML private ImageView a_vid;


    @FXML private Label a_gyro;
    
    @FXML private Label a_f_inf;
    @FXML private Label a_b_inf;
    @FXML private Label a_l_inf;
    @FXML private Label a_r_inf;

    @FXML private Label a_f_ult;

    @FXML private Label a_l_odo;
    @FXML private Label a_r_odo;
    @FXML private Label a_tot_l_odo;
    @FXML private Label a_tot_r_odo;

    @FXML private Label a_x_coo;
    @FXML private Label a_y_coo;
    @FXML private Label a_ping;

    private boolean voiceIsOn;

    @FXML private FontAwesomeIconView microphone_glyph;

   @FXML private TextArea txt_inpt;

    private CarAPI carAPI;
    private MovementHandler keyboardHandler;

    //Takes the role of a constructor, as the javafx constructor cannot be easily implemented with parameters.
    public void initialize(CarAPI carAPI){
        if (this.carAPI == null){
            languageList = new ArrayList<>();
            voiceIsOn = false;
            txt_scrl.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            txt_scrl.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            this.carAPI = carAPI;
            this.keyboardHandler = new MovementHandler(carAPI);
            sideShown = true;
            mode = WindowModes.MANUAL;
            previousMode = mode;

            loadLanguage();
            initialiseListeners();
        }
    }

    private void initialiseListeners(){

        carAPI.addVideoFeedListener(image -> runLater(() -> m_vid.setImage(image)));
        carAPI.addVideoFeedListener(image -> runLater(() ->a_vid.setImage(image)));

        carAPI.addUltraSonicListener(distance -> runLater(() -> m_f_ult.setText(doubleToString(distance))));

        carAPI.addGyroscopeListener(degrees -> runLater(() -> m_gyro.setText(doubleToString(degrees))));

        carAPI.addInfraredListener(Infrared.FRONT, distance -> runLater(() -> m_f_inf.setText(doubleToString(distance))));
        carAPI.addInfraredListener(Infrared.BACK, distance -> runLater(() -> m_b_inf.setText(doubleToString(distance))));
        carAPI.addInfraredListener(Infrared.LEFT, distance -> runLater(() -> m_l_inf.setText(doubleToString(distance))));
        carAPI.addInfraredListener(Infrared.RIGHT, distance -> runLater(() -> m_r_inf.setText(doubleToString(distance))));

        carAPI.addOdometerSpeedListener(Odometer.LEFT, speed -> runLater(() -> m_l_odo.setText(doubleToString(speed))));
        carAPI.addOdometerSpeedListener(Odometer.RIGHT, speed -> runLater(() -> m_r_odo.setText(doubleToString(speed))));

        carAPI.addOdometerTotalDistanceListener(Odometer.LEFT, totalDistance -> runLater(() -> m_tot_l_odo.setText(doubleToString(totalDistance))));
        carAPI.addOdometerTotalDistanceListener(Odometer.RIGHT, totalDistance -> runLater(() -> m_tot_r_odo.setText(doubleToString(totalDistance))));

        carAPI.addUltraSonicListener(distance -> runLater(() -> a_f_ult.setText(doubleToString(distance))));

        carAPI.addGyroscopeListener(degrees -> runLater(() -> a_gyro.setText(doubleToString(degrees))));

        carAPI.addInfraredListener(Infrared.FRONT, distance -> runLater(() -> a_f_inf.setText(doubleToString(distance))));
        carAPI.addInfraredListener(Infrared.BACK, distance -> runLater(() -> a_b_inf.setText(doubleToString(distance))));
        carAPI.addInfraredListener(Infrared.LEFT, distance -> runLater(() -> a_l_inf.setText(doubleToString(distance))));
        carAPI.addInfraredListener(Infrared.RIGHT, distance -> runLater(() -> a_r_inf.setText(doubleToString(distance))));

        carAPI.addOdometerSpeedListener(Odometer.LEFT, speed -> runLater(() -> a_l_odo.setText(doubleToString(speed))));
        carAPI.addOdometerSpeedListener(Odometer.RIGHT, speed -> runLater(() -> a_r_odo.setText(doubleToString(speed))));

        carAPI.addOdometerTotalDistanceListener(Odometer.LEFT, totalDistance -> runLater(() -> a_tot_l_odo.setText(doubleToString(totalDistance))));
        carAPI.addOdometerTotalDistanceListener(Odometer.RIGHT, totalDistance -> runLater(() -> a_tot_r_odo.setText(doubleToString(totalDistance))));
        
        
        
        startPingUpdateThread();
    }

    private void startPingUpdateThread(){
        Thread updater = new Thread(() ->{
            while (true) {
                int ping = carAPI.getPing();
                runLater(() -> m_ping.setText(Integer.toString(ping)));
                runLater(() -> a_ping.setText(Integer.toString(ping)));
                try {
                    Thread.sleep(1111);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updater.start();
    }


    private void checkSelectedTab() {
        runLater(() -> {
            try {
                Tab selectedTab = main_pane.getSelectionModel().getSelectedItem();
                if (manual_tab.equals(selectedTab)) {
                    carAPI.setManualMode(true);
                } else if (autonomous_tab.equals(selectedTab)) {

                    carAPI.setManualMode(false);
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }


    private void runLater(Runnable runnable){
        Platform.runLater(runnable);
    }

    private String doubleToString(Double value){
        return Double.toString(value);
    }


    @FXML
    protected void exit() {
        Stage stage = (Stage) root_anchor.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void quitSession() {
        Stage stage = (Stage) root_anchor.getScene().getWindow();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/start-view.fxml"));

            Scene scene = new Scene(root, 700, 550);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void switchToMTab(){
        main_pane.getSelectionModel().select(0);
        updateTabSelection();
    }

    @FXML
    protected void switchToATab(){
        main_pane.getSelectionModel().select(1);
        updateTabSelection();
    }

    @FXML
    protected void switchToSTab(){
        main_pane.getSelectionModel().select(2);
        updateTabSelection();
    }

    @FXML
    protected void handleKeyPress(KeyEvent event){
        KeyCode code =event.getCode();

        if (code.isArrowKey()){
            updateTabSelection();
        }
        else if (code == KeyCode.ESCAPE){

            if (sideShown){
                sideShown=false;
                root_box.getChildren().remove(side_anchor);
                a_root_box.getChildren().remove(a_side_anchor);
                s_root_box.getChildren().remove(s_side_anchor);
            }
            else{
                sideShown=true;
                root_box.getChildren().add(0,side_anchor);
                a_root_box.getChildren().add(0,a_side_anchor);
                s_root_box.getChildren().add(0,s_side_anchor);
            }
        }
        else if (mode == WindowModes.MANUAL && code == KeyCode.W || code == KeyCode.S || code == KeyCode.A || code == KeyCode.D){
            try {
                keyboardHandler.handlePress(event);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    }

    @FXML
    protected void handleKeyReleased(KeyEvent event){
        KeyCode code = event.getCode();
        if (mode == WindowModes.MANUAL && code == KeyCode.W || code == KeyCode.S || code == KeyCode.A || code == KeyCode.D){
            try {
                keyboardHandler.handleRelease(event);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void updateTabSelection(){
        int selected = main_pane.getSelectionModel().getSelectedIndex();
        previousMode = mode;

        switch (selected) {
            case 0 -> {
                mode = WindowModes.MANUAL;
                if (mode != previousMode) {
                    try {
                        carAPI.setManualMode(true);
                    } catch (Exception ignored) {

                    }
                }
            }
            case 1 -> {
                mode = WindowModes.AUTO;
                if (mode != previousMode) {
                    try {
                        carAPI.setManualMode(false);
                    } catch (Exception ignored) {

                    }
                }
            }
            case 2 -> {
                mode = WindowModes.SETTINGS;
                if (mode != previousMode) {
                    try {
                        carAPI.stop();
                        carAPI.setManualMode(false);
                    } catch (Exception ignored) {

                    }

                }
            }
        }
    }

    @FXML
    protected void cancel(){

        if(mode == WindowModes.AUTO) {
            txt_inpt.clear();
        }
            //TODO: SETTINGS restoration to default state.
    }

    @FXML
    protected void confirm(){

        if (mode==WindowModes.AUTO){
            processInput();
        }
        //TODO: Settings + updates.
    }



    private void processInput(){
        String input = txt_inpt.getText();
        InputProcessor processor = new InputProcessor();

        try {
            if (input == null || input.isBlank()){
                throw new UnclearInputException("No input provided.");
            }
            String csv = processor.processInput(input);
            ArrayList<String> cList = processor.getCmList();
            carAPI.sendCSVCommand(csv);

            ObservableList<Node> children = txt_out.getChildren();
            children.clear();
            Text tNode;
            for (String command : cList){
                tNode = new Text(command+"\n");
                children.add(tNode);
            }


        }
        catch (MqttException e){
            e.printStackTrace();
        }
        catch (UnclearInputException e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    protected void getFile(){
        try {

            Window window = root_anchor.getScene().getWindow();
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(window);
            String output = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            txt_inpt.setText(output);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void detectON() throws MqttException {
        carAPI.setEmergencyCheck(true);
        System.out.println("Auto detection ON");
    }


    @FXML
    protected void detectOFF() throws MqttException {
        carAPI.setEmergencyCheck(false);
        System.out.println("Auto detection OFF");
    }

    @FXML
    protected void getSoundFile(){
        try {
            Window window = root_anchor.getScene().getWindow();
            FileChooser chooser = new FileChooser();
            File soundFile = chooser.showOpenDialog(window);
            //TODO: Add .mp3 and .wav speech-to-text processing here once implemented.


            //Proof of concept while ensuring that the audio-file loading works.
            Media media = new Media(soundFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadLanguage(){
        try {
            languageList = FileLoader.loadTxtFile(getClass().getResource("/languages/current/s2.txt"));

            for (String entry : languageList){
                String[] idAndValue = entry.split(";;;");
                Node node = getNodeByCSS(idAndValue[0]);

                if (node instanceof Button){
                    ((Button) node).setText(idAndValue[1]);
                }
                else if (node instanceof Label){
                    ((Label) node).setText(idAndValue[1]);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    protected void toggleVoice(){
        //TODO: Add voice typing.
        if (voiceIsOn){
            voiceIsOn = false;
            microphone_glyph.setGlyphName("MICROPHONE_SLASH");
        }
        else {
            voiceIsOn = true;
            microphone_glyph.setGlyphName("MICROPHONE");
        }
    }

    private enum WindowModes {
        MANUAL,AUTO,SETTINGS
    }

    private Node getNodeByCSS(String selector){
        return main_pane.lookup(selector);
    }

    private Set<Node> getNodesByCSS(String selector){
        return main_pane.lookupAll(selector);
    }

}