package commands_processing;

import static grammar.LanguageEnums.*;

public class Command {

    private actionTypes action;
    private directionTypes direction;
    private Integer amount;
    private unitTypes unit;

    public Command(actionTypes action, directionTypes direction, Integer amount, unitTypes unit){
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
        if (action == actionTypes.NULL && direction == directionTypes.NULL){
            return true;
        }
        return false;
    }

    public void setDirection(directionTypes direction) {
        this.direction = direction;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setAction(actionTypes action) {
        if (this.action == actionTypes.NULL){
            this.action = action;
        }
    }

    public void setUnit(unitTypes unit) {
        this.unit = unit;
    }

    private int standardizeAmount(){
        //IMPLEMENT UNIT CONVERSION, DECIDE ON UNIFIED UNIT FOR C++ SIDE

        if (amount == null){
            return 0;
        }

        return amount;
    }

    public actionTypes getAction() {
        return action;
    }

    public directionTypes getDirection() {
        return direction;
    }

    public Integer getAmount() {
        return amount;
    }

    public unitTypes getUnit() {
        return unit;
    }

    public boolean hasAction(){
        return action != actionTypes.NULL;
    }

    public boolean hasDirection(){
        return direction != directionTypes.NULL;
    }

    public boolean hasAmount(){
        return amount != null;
    }

    public boolean hasUnit(){
        return unit != unitTypes.NULL;
    }

}
