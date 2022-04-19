package app;

import api.CarAPI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;


public class App extends Application {

    private static CarAPI carAPI;
    private static KeyboardHandler keyboardHandler;

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
        primaryStage.setTitle("Control Panel");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) throws Exception {
        initClient();
        launch(args);
    }

    private static void initClient() throws MqttException{
        carAPI = new CarAPI("127.0.0.1","2cf6307a-5bd4-4af1-b90f-db66b849078f","smartcar");
        keyboardHandler  = new KeyboardHandler();
    }

    public static CarAPI getCarAPI() {
        return carAPI;
    }

    public static KeyboardHandler getKeyboardHandler(){
        return keyboardHandler;
    }
}
