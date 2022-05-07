package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CarGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        /*
        Make sure to have those vm arguments when launching the app:

        --module-path "JAVA FX LIB FOLDER" --add-modules javafx.controls,javafx.fxml,javafx.media
         */


        Parent root = FXMLLoader.load(getClass().getResource("/views/start-view.fxml"));

        Scene scene = new Scene(root, 720, 540);

        stage.setTitle("AutoXCar");
        stage.setScene(scene);
        stage.setMinWidth(740);
        stage.setMinHeight(540);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}