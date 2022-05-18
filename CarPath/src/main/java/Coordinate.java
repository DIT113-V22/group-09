import api.CarAPI;
import app.App;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.util.ArrayList;

public class Coordinate {

    private  static CarAPI carAPI = App.getCarAPI();
    private static ArrayList<Coordinate> coordinates = new ArrayList<>();
    private static boolean firstCoordinate = false;

    private double x;
    private double y;
    private double odometerDistance;
    private double gyroscopeDegrees;
   // private ArrayList<Coordinate> adjacentCoordinates;


    public Coordinate(long odometerDistance, double gyroscopeTurnDegrees){
        this.odometerDistance = odometerDistance;
        this.gyroscopeDegrees = gyroscopeTurnDegrees;
        x = 0;
        y = 0;
    }

    public Coordinate(long odometerDistance, double gyroscopeTurnDegrees, Coordinate lastCoordinate) {


      //  adjacentCoordinates = new ArrayList<>();
        this.odometerDistance = odometerDistance;
        this.gyroscopeDegrees = gyroscopeTurnDegrees;
        double distanceFromLast = odometerDistance - lastCoordinate.odometerDistance;
        double xDifference;
        double refactoredTurnDegrees = gyroscopeTurnDegrees;
        if (gyroscopeTurnDegrees > 0){
            if (gyroscopeTurnDegrees > 90){
                refactoredTurnDegrees = 180 - gyroscopeTurnDegrees;
            }
            xDifference = (Math.cos(refactoredTurnDegrees) * distanceFromLast);
        }else{
            if (gyroscopeTurnDegrees < -90){
                refactoredTurnDegrees = 180 + gyroscopeTurnDegrees;
            }
            xDifference = -(Math.cos(refactoredTurnDegrees) * distanceFromLast);
        }
        x = lastCoordinate.x + xDifference;

        double yDifference;
        if (gyroscopeTurnDegrees > 90){
            yDifference = -(Math.sin(gyroscopeTurnDegrees - 90) * distanceFromLast);
        }else if (gyroscopeTurnDegrees < -90){
            yDifference = -(Math.sin(gyroscopeTurnDegrees + 90) * distanceFromLast);
        }else{
            yDifference = (Math.sin(gyroscopeTurnDegrees) * distanceFromLast);
        }
        y = lastCoordinate.y + yDifference;

     //   adjacentCoordinates.add(lastCoordinate);
    }

    public Coordinate(){
        x = 0;
        y = 0;
        odometerDistance = 0;
        gyroscopeDegrees = 0;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    /*
    public ArrayList<Coordinate> getAdjacentCoordinates() {
        return adjacentCoordinates;
    }

    public void setAdjacentCoordinates(ArrayList<Coordinate> adjacentCoordinates) {
        this.adjacentCoordinates = adjacentCoordinates;
    }

    public void addAdjacentCoordinate(Coordinate coordinate) {
        if (!this.adjacentCoordinates.contains(coordinate))
            this.adjacentCoordinates.add(coordinate);
    }
    */
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


}
