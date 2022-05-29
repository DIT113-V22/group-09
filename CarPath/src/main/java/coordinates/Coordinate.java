package coordinates;

public class Coordinate {

    private double x;
    private double y;
    private double odometerDistance;
    private int gyroscopeDegrees;


    public Coordinate(long odometerDistance, int gyroscopeTurnDegrees){
        this.odometerDistance = odometerDistance;
        this.gyroscopeDegrees = gyroscopeTurnDegrees;
        x = 0;
        y = 0;
        System.out.println(odometerDistance);
        System.out.println(gyroscopeTurnDegrees);
    }

    public Coordinate(long odometerDistance, int gyroscopeTurnDegrees, Coordinate lastCoordinate) {


      //  adjacentCoordinates = new ArrayList<>();
        this.odometerDistance = odometerDistance;
        int gyroscopeDegrees = gyroscopeTurnDegrees;

        double distanceFromLast = odometerDistance - lastCoordinate.odometerDistance;
        double xDifference;
        int refactoredTurnDegrees = gyroscopeDegrees;
        if (gyroscopeDegrees > 0){
            if (gyroscopeDegrees > 90){
                refactoredTurnDegrees = 180 - gyroscopeDegrees;
            }
            xDifference = (Math.sin(Math.toRadians(refactoredTurnDegrees)) * distanceFromLast);
        }else{
            if (gyroscopeDegrees < -90){
                refactoredTurnDegrees = 180 + gyroscopeDegrees;
            }
            //Math.abs should not be needed
            xDifference = -(Math.sin(Math.abs(Math.toRadians(refactoredTurnDegrees))) * distanceFromLast);
        }
        x = lastCoordinate.x + xDifference;

        double yDifference;
        if (gyroscopeDegrees > 90){
            yDifference = (Math.cos(Math.toRadians(180 - gyroscopeDegrees)) * distanceFromLast);
        }else if (gyroscopeDegrees < -90){
            yDifference = (Math.cos(Math.toRadians(180 + gyroscopeDegrees)) * distanceFromLast);
        }else{
            //Math.abs should not be needed
            yDifference = -(Math.cos(Math.abs(Math.toRadians(gyroscopeDegrees))) * distanceFromLast);
        }
        y = lastCoordinate.y + yDifference;
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
    public int getGyroscopeDegrees(){
        return gyroscopeDegrees;
    }
    public double getOdometerDistance(){
        return odometerDistance;
    }
}
