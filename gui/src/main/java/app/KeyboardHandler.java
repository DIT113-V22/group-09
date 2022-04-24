package app;

import api.CarAPI;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.paho.client.mqttv3.MqttException;

public class KeyboardHandler {
    private final CarAPI api = App.getCarAPI();
    private int direction = 0;
    private boolean enabled = true;

    public void handle(KeyEvent event) throws MqttException {
        if (!isEnabled())return;
        KeyCode keyCode = event.getCode();
        switch (keyCode) {
            case W -> {
                if (direction < 0) {
                    api.stop();
                    direction = 0;
                } else {
                    api.moveForward();
                    direction = 1;
                }
            }
            case A -> {
                direction = 0;
                api.moveForwardLeft();
            }
            case D -> {
                direction = 0;
                api.moveForwardRight();
            }
            case S -> {
                if (direction > 0) {
                    api.stop();
                    direction = 0;
                } else {
                    api.moveBackward();
                    direction = -1;
                }
            }
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
