package commands_processing;

import static grammar.LanguageEnums.*;

public class Command {

    private ActionTypes action;
    private DirectionTypes direction;
    private Integer amount;
    private UnitTypes unit;

    public Command(ActionTypes action, DirectionTypes direction, Integer amount, UnitTypes unit){
        this.action = action;
        this.direction = direction;
        this.amount = amount;
        this.unit = unit;
    }

    public String toJSON(){
        String json =
                "{" +
                    "\"com_type\" : \""+ action +"\","+
                    "\"direction\" : \""+direction+"\","+
                    "\"amount\" : \""+standardizeAmount()+"\""+
                "}";
        return json;
    }

    public String toCSV(){
        String csv = action +","+direction+","+standardizeAmount()+"\n";
        return csv;
    }


    public String toString(){
        return "Command type:"+ action +" Direction:"+direction+" Amount:"+amount+" Unit:"+unit;
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

}
