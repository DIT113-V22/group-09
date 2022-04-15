package api.sensor;

public enum Odometer {
    RIGHT("right"),
    LEFT("left");

    private final String value;

    Odometer(String value){
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
