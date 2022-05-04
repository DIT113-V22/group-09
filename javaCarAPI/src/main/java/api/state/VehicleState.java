package api.state;

public record VehicleState(int rightOdometerDistance,int leftOdometerDistance,int heading, int distance, long time) {

    @Override
    public String toString() {
        return "VehicleState{" +
                "rightOdometerDistance=" + rightOdometerDistance +
                ", leftOdometerDistance=" + leftOdometerDistance +
                ", heading=" + heading +
                ", distance=" + distance +
                ", time=" + time +
                '}';
    }
}
