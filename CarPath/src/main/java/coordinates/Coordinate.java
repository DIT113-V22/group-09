package coordinates;

public class Coordinate {

    private double x;
    private double y;
    private double odometerDistance;
    private double gyroscopeDegrees;


    public Coordinate(long odometerDistance, int gyroscopeTurnDegrees){
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
}
