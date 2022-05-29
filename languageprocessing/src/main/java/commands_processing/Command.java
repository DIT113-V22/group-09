package commands_processing;

import static grammar.LanguageEnums.*;

public class Command {

    private ActionTypes action;
    private DirectionTypes direction;
    private Integer amount;
    private UnitTypes unit;
    private static final int MAX_THRESHOLD = 10000000;
    private Shapes shape;
    private Rotations rotation;
    private Integer x,y;



    public Command(ActionTypes action, DirectionTypes direction, Integer amount, UnitTypes unit, Shapes shape, Rotations rotation){
        this.action = action;
        this.direction = direction;
        this.amount = amount;
        this.unit = unit;
        this.shape = shape;
        this.rotation = rotation;
        x=null;
        y=null;
    }

    public Command(ActionTypes action, int x, int y){
        if (action != ActionTypes.GO_TO_COORD){
            throw new IllegalArgumentException("Command accepts only DRIVE_TO action type for this constructor");
        }
        this.action = action;
        this.direction = DirectionTypes.NOT_APPLICABLE;
        this.amount = 0;
        this.unit = UnitTypes.NOT_APPLICABLE;
        this.shape = Shapes.NULL;
        this.rotation = Rotations.NULL;
        this.x=x;
        this.y=y;
    }

    public String toJSON(){
        String json;
        if (action!=ActionTypes.GO_TO_COORD){
            json =
                "{" +
                    "\"com_type\" : \""+ action +"\","+
                    "\"direction\" : \""+direction+"\","+
                    "\"amount\" : \""+standardizeAmount()+"\""+
                "}";
        }
        else {
            json =
                "{" +
                    "\"com_type\" : \""+ action +"\","+
                    "\"x\" : \""+x+"\","+
                    "\"y\" : \""+y+"\""+
                "}";
        }
        return json;
    }

    public String toCSV(){
        String csv;
        if (action==ActionTypes.GO_TO_COORD){
           csv = action+","+x+","+y;
        }
        else {
        csv= action +","+direction+","+standardizeAmount()+"\n";

        }

        return csv;
    }


    public String toString(){

        if (action==ActionTypes.GO_TO_COORD){
            return "Type:"+action+" X:"+x+" Y:"+y;
        }
        String str_amount;

        if (amount> MAX_THRESHOLD){
            str_amount="MAX";
        }
        else {
            str_amount=Integer.toString(amount);
        }

        if (shape != Shapes.NULL){
            return "Type:"+ action+"_"+shape+"_"+rotation+" Dir:"+direction+" Amount:"+str_amount+" Unit:"+unit;
        }

        return "Type:"+ action +" Dir:"+direction+" Amount:"+str_amount+" Unit:"+unit;
    }

    public boolean isEmpty(){
        //Consider if the inferring should be done inside the command or outside when building.
        if (action == ActionTypes.NULL && direction == DirectionTypes.NULL){
            return true;
        }
        return false;
    }

    public void setDirection(DirectionTypes direction) {
        this.direction = direction;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setAction(ActionTypes action) {
        if (this.action == ActionTypes.NULL){
            this.action = action;
        }
    }

    public void setUnit(UnitTypes unit) {
        this.unit = unit;
    }

    private int standardizeAmount(){
        //IMPLEMENT UNIT CONVERSION, DECIDE ON UNIFIED UNIT FOR C++ SIDE

        if (amount == null){
            return 0;
        }

        return amount;
    }

    public ActionTypes getAction() {
        return action;
    }

    public DirectionTypes getDirection() {
        return direction;
    }

    public Integer getAmount() {
        return amount;
    }

    public UnitTypes getUnit() {
        return unit;
    }

    public boolean hasAction(){
        return action != ActionTypes.NULL;
    }

    public boolean hasDirection(){
        return direction != DirectionTypes.NULL;
    }

    public boolean hasAmount(){
        return amount != null;
    }

    public boolean hasUnit(){
        return unit != UnitTypes.NULL;
    }

    public boolean hasShape(){
        return shape != null;
    }

    public boolean hasRotation(){
        return rotation != null;
    }

    public int getXCoord(){return x;}

    public int getYCoord(){return y;}

    public Shapes getShape() {
        return shape;
    }

    public Rotations getRotation() {
        return rotation;
    }

    public void setShape(Shapes shape) {
        this.shape = shape;
    }

    public void setRotation(Rotations rotation) {
        this.rotation = rotation;
    }
}
