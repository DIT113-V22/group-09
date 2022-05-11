package controllers;

import api.CarAPI;
import file_processing.FileLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;
import util.TxtWriter;

import java.net.URL;
import java.util.*;

import static util.ValidityChecker.notNullOrBlank;
import static util.VarargFunctions.blankReplace;
import static util.VarargFunctions.concatenate;

public class StartController implements Initializable {

    private Map<String,User> users;

    @FXML private Label info_label;

    private List<Node> infoNodes;

    private WindowModes mode;

    @FXML private HBox root_box;

    @FXML private AnchorPane side_anchor;

    @FXML private AnchorPane root_anchor;

    @FXML private Button log_btn;
    @FXML private Button anon_btn;
    @FXML private Button prof_btn;
    @FXML private Button set_btn;


    @FXML private TextField usr_nm;
    @FXML private TextField usr_pass;

    @FXML private TextField car_nm;
    @FXML private TextField host_inf;
    @FXML private TextField conn_inf;

    @FXML private TextField mqt_usr;
    @FXML private TextField mqt_pass;
    @FXML private TextField mqt_cli;



    private boolean sideShown;

    public StartController(){
        users = new HashMap<>();
        reloadUsers();
        sideShown = true;
        mode = WindowModes.USR_CREATE;


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showLogScr();
    }

    @FXML
    public void handleKeyPress(KeyEvent event){

        if (event.getCode() == KeyCode.ESCAPE){
            if (sideShown){
                sideShown=false;
                root_box.getChildren().remove(side_anchor);
            }
            else{
                sideShown=true;
                root_box.getChildren().add(0,side_anchor);
            }
        }
        else if(event.getCode() == KeyCode.ENTER){
            confirm();
        }
    }

    private void handleButtonTags(){
        switch (mode){
            case USR_CREATE -> {
                prof_btn.getStyleClass().remove("chosen");
            }
            case LOG_IN -> {
                log_btn.getStyleClass().remove("chosen");
            }
            case ANON -> {
                anon_btn.getStyleClass().remove("chosen");
            }
            case SETTINGS -> {
                set_btn.getStyleClass().remove("chosen");
            }
        }
    }


    @FXML
    protected void showLogScr(){
        info_label.setText("{ LOG IN }");
        handleButtonTags();
        log_btn.getStyleClass().add("chosen");
        mode= WindowModes.LOG_IN;
        hideInfoNodesByCSS(".car-field");
    }

    @FXML
    protected void showUsrCreScr(){
        info_label.setText("{ CREATE PROFILE }");
        handleButtonTags();
        prof_btn.getStyleClass().add("chosen");
        mode= WindowModes.USR_CREATE;
        restoreInfoNodes();
    }

    @FXML
    protected void showAnonScr(){
        info_label.setText("{ SIMPLE SESSION }");
        handleButtonTags();
        anon_btn.getStyleClass().add("chosen");
        mode= WindowModes.ANON;
        hideInfoNodesByCSS(".user-field");
    }

    @FXML
    protected void showSetScr(){
        info_label.setText("{ SETTINGS }");
        handleButtonTags();
        set_btn.getStyleClass().add("chosen");
        mode= WindowModes.SETTINGS;
        //TODO: Implement settings
        hideInfoNodesByCSS(".info");
    }

    @FXML
    protected void exit() {
        Stage stage = (Stage) root_anchor.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void cancel(){
        if (mode== WindowModes.SETTINGS){
            //TODO: SETTINGS restoration to default state.
        }
        else {
            clear();
        }
    }

    @FXML
    protected void confirm(){
        switch(mode){
            case LOG_IN -> {
                try{
                    User user = logIn(usr_nm.getText(),usr_pass.getText());
                    startCarSession(user);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            case ANON -> {
                try{
                    User user = createUsr();
                    startCarSession(user);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            case USR_CREATE -> {
                try{
                    storeUser();
                    clear();
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            case SETTINGS -> {
                //TODO: Settings + updates.
            }
        }
    }

    private User logIn(String username,String password) throws Exception{
        User user = users.get(username);

        if (user.checkPassword(password)){
            return new User(user.getCarName(),user.getHost(),user.getAddress(),user.getMQTTusername(),user.getMQTTpassword(),user.getMQTTclientID());
        }
        else {
            throw new Exception("Invalid Password of Username");
        }
    }

    private void clear(){
        Set<Node> textNodes = getNodesByCSS(".txt");

        for (Node textNode : textNodes){
            ((TextField) textNode).clear();
        }
        PasswordField passwordField = (PasswordField) getNodeByCSS(".pass");
        passwordField.clear();
    }



    private User createUsr() throws Exception{

        String[] ids = {"#usr-txt","#usr-pass","#car-txt","#hos-txt","#conn-txt","#mq-usr-txt","#mq-pass-txt","#mq-uid-txt"};
        String[] usrInfo = new String[8];

        Node node;

        for (int i = 0; i<ids.length; i++){
            node = getNodeByCSS(ids[i]);
            usrInfo[i] = getTextInfo(node);
            System.out.println(i+": "+ usrInfo[i]);
        }

        if (mode== WindowModes.ANON){
            return new User(usrInfo[2],usrInfo[3],usrInfo[4],usrInfo[5],usrInfo[6],usrInfo[7]);
        }

        return new User(usrInfo[0],usrInfo[1],usrInfo[2],usrInfo[3],usrInfo[4],usrInfo[5],usrInfo[6],usrInfo[7]);
    }

    private String getTextInfo(Node node){
        if (node != null){
            //PasswordField is also an instanceof Textfield.
            if (node instanceof TextField){
                TextField textField = (TextField) node;
                return textField.getText();
            }
            else {
                return "";
            }
        }
        else {
            return "";
        }
    }


    private void startCarSession(User user) throws Exception{
        CarAPI api;

        if (user.getMQTTpassword().isBlank() || user.getMQTTusername().isBlank()){
            if (user.getMQTTclientID().isBlank()){
                api =new CarAPI(user.getHost(), Integer.parseInt(user.getAddress()),user.getCarName());
            }
            else {
                api =new CarAPI(user.getHost(), Integer.parseInt(user.getAddress()), user.getMQTTclientID(),user.getCarName());
            }
        }
        else {
            if (user.getMQTTclientID().isBlank()){
                api= new CarAPI(user.getHost(), Integer.parseInt(user.getAddress()), user.getCarName(), user.getMQTTusername(), user.getMQTTpassword().toCharArray());
            }
            else {
                api= new CarAPI(user.getHost(), Integer.parseInt(user.getAddress()), user.getMQTTclientID(), user.getCarName(), user.getMQTTusername(), user.getMQTTpassword().toCharArray());
            }
        }


        Stage stage = (Stage) root_anchor.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/steering-view.fxml"));


        Scene scene = new Scene(loader.load(), 1040, 620);
        stage.setMinWidth(1040);
        stage.setMinHeight(620);

        SteeringController controller = loader.getController();
        stage.show();
        controller.initialize(api);
        stage.setScene(scene);

    }


    private void backupInfoNodes(){
        if (infoNodes == null){
            VBox box = (VBox) getNodeByCSS("#info-box");
            ObservableList<Node> nodes = box.getChildren();

            infoNodes = new ArrayList<>();

            for (Node node : nodes){
                infoNodes.add(node);
            }
        }
    }


    private void reloadUsers(){
        try {
            users.clear();
            ArrayList<String> userProfiles = FileLoader.loadTxtFile(getClass().getResource("/profiles/profiles.txt"));
            String[] userDetails;
            User mapEntry;
            for (String user: userProfiles){
                userDetails = user.split(",");



                for(int i = 0; i<userDetails.length; i++){
                     userDetails[i]= userDetails[i].replace(";;;","");
                }

                mapEntry = new User(userDetails[0],userDetails[1],userDetails[2],userDetails[3],userDetails[4],userDetails[5],userDetails[6],userDetails[7]);
                users.put(userDetails[0],mapEntry);
            }
        }
        catch (Exception e){
            System.out.println("Error occurred while loading user profiles.");
            System.out.println(e.getMessage());
        }
    }

    private void storeUser() throws Exception{

        String username = usr_nm.getText();
        String password = usr_pass.getText();
        String carName = car_nm.getText();
        String host = host_inf.getText();
        String port = conn_inf.getText();
        String MQTTusername = mqt_usr.getText();
        String MQTTpassword = mqt_pass.getText();
        String MQTTcliID = mqt_cli.getText();


        if (!notNullOrBlank(username,password,carName,host,port)){
            throw new Exception("Cannot create user profile with lacking information.");
        }

        if (users.containsKey(username)){
            throw new Exception("Username already taken.");
        }


        ArrayList<String> MQTTinformation = blankReplace(";;;",MQTTusername,MQTTpassword,MQTTcliID);

        String entry = concatenate(",",username,password,carName,host,port,MQTTinformation.get(0),MQTTinformation.get(1),MQTTinformation.get(2));

        User user = new User(username,password,carName,host,port,MQTTusername,MQTTpassword,MQTTcliID);
        users.put(username,user);

        TxtWriter.writeEntry(getClass().getResource("/profiles/profiles.txt"),entry);
    }

    private Node getNodeByCSS(String selector){
        return root_anchor.lookup(selector);
    }

    private Set<Node> getNodesByCSS(String selector){
        return root_anchor.lookupAll(selector);
    }

    private void hideInfoNodesByCSS(String selector){
        restoreInfoNodes();
        Set<Node> nodes = getNodesByCSS(selector);
        for (Node node : nodes){
            ((VBox)node.getParent()).getChildren().remove(node);
        }
    }

    private void restoreInfoNodes(){
        backupInfoNodes();
        VBox box = (VBox) getNodeByCSS("#info-box");
        ObservableList<Node> children = box.getChildren();
        children.clear();

        for (Node node : infoNodes){
            children.add(node);
        }
    }

    private enum WindowModes{
        SETTINGS,LOG_IN,USR_CREATE,ANON
    }
}