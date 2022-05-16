import java.util.ArrayList;
import java.util.HashMap;

public class CarGrid {
    
    public static ArrayList<Coordinate> coords;

    public CarGrid(){
        coords = new ArrayList<Coordinate>();
    }
    public void addCoordinate(Coordinate coordinate){
        coords.add(coordinate);
    }

}
