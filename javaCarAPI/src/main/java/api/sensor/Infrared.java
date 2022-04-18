package api.sensor;



public enum Infrared {

    FRONT("front"),
    BACK("back"),
    LEFT("left"),
    RIGHT("right");

    private final String value;

    Infrared(String value){
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
