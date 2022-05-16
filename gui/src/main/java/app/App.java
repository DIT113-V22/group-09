package app;

import api.CarAPI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.awt.*;
import java.util.UUID;


public class App extends Application {

    private static CarAPI carAPI;
    private static KeyboardHandler keyboardHandler;

    private final static String HOST = "127.0.0.1";
    private final static String CAR_NAME = "smartcar";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui.fxml"));
        root.setOnKeyReleased(keyEvent -> {
            try {
                keyboardHandler.handle(keyEvent);
            }
            catch (MqttException ignore){}
        });
        primaryStage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setTitle("PROTOTYPE Control Panel");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) throws Exception {
        initClient();
        launch(args);

    }

    private static void initClient() throws MqttException{
        carAPI = new CarAPI(HOST, UUID.randomUUID().toString(),CAR_NAME);
        keyboardHandler  = new KeyboardHandler();
    }

    public static CarAPI getCarAPI() {
        return carAPI;
    }

    public static KeyboardHandler getKeyboardHandler(){
        return keyboardHandler;
    }
}
