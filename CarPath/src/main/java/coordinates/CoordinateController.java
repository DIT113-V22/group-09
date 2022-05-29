package coordinates;

import api.CarAPI;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class CoordinateController {
    private CarAPI carAPI;
    private ArrayList<Coordinate> coordinates = new ArrayList<>();
    private boolean firstCoordinate = false;
    private Label labelX,labelY;


    public CoordinateController (CarAPI carAPI, Label labelX,Label labelY){
        this.carAPI = carAPI;
        this.labelX = labelX;
        this.labelY = labelY;
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

//            Platform.runLater(() ->{
//                try {
//                    labelX.setText(Double.toString(coordinate.getX()));
//                    labelY.setText(Double.toString(coordinate.getY()));
//                }
//                catch (Exception ignored){
//
//                }
//            });
        });
    }

    public void goToCoordinate(double x, double y){

        double startingX = coordinates.get(coordinates.size()-1).getX();
        double startingY = coordinates.get(coordinates.size()-1).getY();

        double xDifference = Math.abs(startingX-x);
        double yDifference = Math.abs(startingY-y);

        //pythagoras
        double travelDistance = Math.sqrt(xDifference * xDifference + yDifference * yDifference);

        double turnDegrees = Math.acos(Math.toRadians(yDifference/travelDistance));
        int turnDegreesDifference = (int) Math.abs(coordinates.get(coordinates.size()-1).getGyroscopeDegrees()-turnDegrees);


        String rightOrLeft;
        if (turnDegreesDifference < 0) {
            rightOrLeft = "-40,40,";
        } else {
            rightOrLeft = "40,-40,";
        }
        String turningCSVCommand = rightOrLeft + Math.abs(turnDegreesDifference) + ",ANGULAR;";

        String drivingCSVCommand = "40," + "40," + travelDistance + ",FORWARD";

        String CSVCommand = turningCSVCommand + drivingCSVCommand;

        try {
            carAPI.sendCSVCommand(CSVCommand);
        }catch (Exception ignored){

        }
    }



    public void returnToStart(){
       for (int i = coordinates.size()-1; i > 0; i--){
           goToCoordinate(coordinates.get(i).getX(), coordinates.get(i).getY());
       }
    }
}