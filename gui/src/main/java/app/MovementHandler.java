package app;

import api.CarAPI;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MovementHandler {
    private final CarAPI api;

    private boolean wIsPressed,sIsPressed,aIsPressed,dIsPressed;

    public MovementHandler(CarAPI api) {
        wIsPressed = false;
        sIsPressed = false;
        aIsPressed= false;
        dIsPressed = false;
        this.api = api;
    }


    public void handlePress(KeyEvent event) throws MqttException {
        KeyCode keyCode = event.getCode();
        switch (keyCode) {
            case W -> {
                wIsPressed = true;
                updateCarMovement();
            }
            case A -> {
                aIsPressed =true;
                updateCarMovement();
            }
            case D -> {
                dIsPressed = true;
                updateCarMovement();
            }
            case S -> {
                sIsPressed = true;
                updateCarMovement();
            }
        }
    }
    
    public void handleRelease(KeyEvent event){
        KeyCode keyCode = event.getCode();
        switch (keyCode) {
            case W -> {
                wIsPressed = false;
                updateCarMovement();
            }
            case A -> {
                aIsPressed =false;
                updateCarMovement();
            }
            case D -> {
                dIsPressed = false;
                updateCarMovement();
            }
            case S -> {
                sIsPressed = false;
                updateCarMovement();
            }
        }
    }
    
    private void updateCarMovement(){
        try {
            if (wIsPressed){
                if (sIsPressed){
                    api.stop();
                }
                else if (aIsPressed || dIsPressed){
                    if (aIsPressed && dIsPressed){
                        api.moveForward();
                    }
                    if (aIsPressed){
                        api.moveForwardLeft();
                    }
                    else {
                        api.moveForwardRight();
                    }
                }
                else {
                    api.moveForward();
                }
            }
            else if (sIsPressed){
                if (aIsPressed || dIsPressed) {
                    if (aIsPressed && dIsPressed) {
                        api.moveBackward();
                    }
                    if (aIsPressed) {
                        api.moveBackwardLeft();
                    } else {
                        api.moveBackwardRight();
                    }
                }
                else {
                    api.moveBackward();
                }
            }
            else {
                api.stop();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
