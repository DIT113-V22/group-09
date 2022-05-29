package coordinates;

import api.CarAPI;

import java.util.ArrayList;

public class CoordinateController {

    private CarAPI carAPI;
    public ArrayList<Coordinate> coordinates = new ArrayList<>();
    private boolean firstCoordinate = false;

    public CoordinateController (CarAPI carAPI){
        this.carAPI = carAPI;
    }

    public void createCoordinateAtStop(){
        carAPI.addCommandStateListener(commandState -> {
            if (firstCoordinate == false){
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

        double triangleDegree = Math.toDegrees(Math.acos(yDifference/travelDistance));

        if (y > startingY){
            triangleDegree = 180 - triangleDegree;
        }
        if (x > startingX){
            triangleDegree = -triangleDegree;
        }
        int lastCoordinateGyroscope = coordinates.get(coordinates.size()-1).getGyroscopeDegrees();

        int turnDegreesDifference = (int) Math.abs(lastCoordinateGyroscope-triangleDegree);

        String rightOrLeft;
        if (lastCoordinateGyroscope > triangleDegree ) {
            //left
            rightOrLeft = "-40,40,";
        } else {
            //right
            rightOrLeft = "40,-40,";
        }
        String turningCSVCommand = rightOrLeft + Math.abs(turnDegreesDifference) + ",ANGULAR;";

        int intTravelDistance = (int)travelDistance;
        String drivingCSVCommand = "40," + "40," + intTravelDistance + ",DISTANCE";

        String CSVCommand = turningCSVCommand + drivingCSVCommand;

        try {
            carAPI.sendCSVCommand(CSVCommand);
        }catch (Exception ignored){

        }
    }

    public void returnToStart(){
       //This method is too fast, the car can not perform all commands in time
        for (int i = coordinates.size()-1; i >= 0; i--){
           goToCoordinate(coordinates.get(i).getX(), coordinates.get(i).getY());
       }
    }
}