import api.CarAPI;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.MqttException;

public class CoordinateController {
    private CarAPI carAPI;
    public ArrayList<Coordinate> coordinates = new ArrayList<>();
    private static boolean firstCoordinate = false;

    public CoordinateController (CarAPI carAPI){
        this.carAPI = carAPI;
    }

    public void createCoordinateAtStop(){
        carAPI.addCommandStateListener(commandState -> {
            if (firstCoordinate = false){
                Coordinate coordinate = new Coordinate(commandState.startSate().distance(), commandState.startSate().heading());
                coordinates.add(coordinate);
                firstCoordinate = true;
            }
            Coordinate coordinate = new Coordinate(commandState.endState().distance(), commandState.endState().heading(), coordinates.get(coordinates.size()-1));
            coordinates.add(coordinate);
        });
    }

    public void goToCoordinate(double x, double y){

        double startingX = coordinates.get(coordinates.size()-1).getX();
        double startingY = coordinates.get(coordinates.size()-1).getY();

        double xDifference = Math.abs(startingX-x);
        double yDifference = Math.abs(startingY-y);

        //pythagoras
        double travelDistance = Math.sqrt(xDifference * xDifference + yDifference * yDifference);

        double turnDegrees = Math.acos(yDifference/travelDistance);
        int turnDegreesDifference = (int) Math.abs(coordinates.get(coordinates.size()-1).getGyroscopeDegrees()-turnDegrees);






    }

    public void returnToStart(){
        String turnCommand;
        String distanceCommand;


    }



}



