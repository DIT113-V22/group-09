import api.CarAPI;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.util.ArrayList;

public class Coordinate {
    // private static ArrayList<Coordinate> coordinates = new ArrayList<>();


    private double x;
    private double y;
    private double odometerDistance;
    private double gyroscopeDegrees;
   // private ArrayList<Coordinate> adjacentCoordinates;


    public Coordinate(long odometerDistance, int gyroscopeTurnDegrees, CarAPI carAPI){
        this.odometerDistance = odometerDistance;
        this.gyroscopeDegrees = gyroscopeTurnDegrees;
        x = 0;
        y = 0;
    }

    public Coordinate(long odometerDistance, int gyroscopeTurnDegrees, Coordinate lastCoordinate) {


      //  adjacentCoordinates = new ArrayList<>();
        this.odometerDistance = odometerDistance;
        this.gyroscopeDegrees = gyroscopeTurnDegrees;
        double distanceFromLast = odometerDistance - lastCoordinate.odometerDistance;
        double xDifference;
        int refactoredTurnDegrees = gyroscopeTurnDegrees;
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
    public double getGyroscopeDegrees(){
        return gyroscopeDegrees;
    }
    public double getOdometerDistance(){
        return odometerDistance;
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

}
