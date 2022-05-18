package commands_processing;

import grammar.LanguageEnums.*;
import grammar.LanguageMaps;
import grammar.PhrasalVerb;
import grammar.PhrasalVerbChecker;

public class CommandBuilder {
    private LanguageMaps lMap;
    private final CommandList cmList;
    private PhrasalVerb phrasalVerb;
    private boolean afterUsed;
    private final PhrasalVerbChecker pvChecker;

    private ActionTypes currentAction;
    private UnitTypes currentUnit;
    private Integer currentAmount;
    private DirectionTypes currentDirection;
    private Shapes currentShape;
    private Rotations currentRotation;

    public CommandBuilder() {
        try {
            lMap = new LanguageMaps();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        cmList = new CommandList();
        pvChecker = new PhrasalVerbChecker();
        
        phrasalVerb = null;
        afterUsed = false;

        nullCurrent();
    }

    public CommandList processText(String[] text){
        if (text == null){
            return cmList;
        }

        for(int i = 0; i<text.length; i++){
            if (text[i].isBlank()){
                continue;
            }

            //Checks if words following the word "after" are a unit-word and an amount word. Ex: after 100 meters.
            //If that is the case then the unit and amount are describing the previous command.
            if (afterUsed){
                if (handleAmountType(text[i]) || handleUnitType(text[i])){
                    if (currentAmount != null && currentUnit != UnitTypes.NULL){
                        afterUsed = false;
                        Command previousCm = cmList.getLast();
                        if (previousCm != null){
                            previousCm.setAmount(currentAmount);
                            previousCm.setUnit(currentUnit);
                            nullCurrent();
                            continue;
                        }
                    }
                }
                else {
                    afterUsed = false;
                }
            }

            //Checks if the current word is a start of a phrasal verb.
            phrasalVerb = pvChecker.findPhrasal(text,i);

            if (phrasalVerb != null){
                //If yes, the identified phrasal verb is tested as a recognized action.
                if (handleActionType(phrasalVerb.toSingleWord())){
                    //If it is then all the words of the phrasal verb will be skipped
                    i += phrasalVerb.length();
                    continue;
                }
            }

            //continue is present to skip going through the remaining code if a type of the currently checked word was identified.
            if (handleWord(text[i])){
                continue;
            }

            //Comma indicates that the clause has ended and a new one will begin (or that the text has ended)
            //In almost all cases every command is contained within one clause, so it can be now added to the list of commands.
            if (text[i].equals(",")){
                Command command = new Command(currentAction,currentDirection,currentAmount,currentUnit,currentShape,currentRotation);
                if (!command.isEmpty()){
                    cmList.add(command);
                    nullCurrent();
                }
            }
            //ADD METHOD(S) FOR HANDLING OF SPECIAL KEYWORDS LIKE AFTER, REPEAT, CANCEL?, etc.
            else if (text[i].equals("after")){
                afterUsed = true;
            }
            else if (text[i].equals("and")){
                // Implement AND functionality?
            }
        }
        return cmList;
    }

    private boolean handleWord(String word){
        //returns true if the given word was identified as one of the command parts
        if (handleActionType(word)){
            return true;
        }
        else if (handleDirectionType(word)){
            return true;
        }
        else if (handleUnitType(word)){
            return true;
        }
        else if (handleAmountType(word)){
            return true;
        }
        else if (handleShape(word)){
            return true;
        }
        else if (handleRotation(word)){
            return true;
        }
        return false;
    }
    
    //Handles different parts of the sentence, returns true if the assignment occurred.
    private boolean handleActionType(String word){
        if (currentAction == ActionTypes.NULL){
            ActionTypes action = lMap.getAction(word);
            if(action != ActionTypes.NULL){
                currentAction = action;
                return true;
            }
        }
        return false;
    }

    private boolean handleUnitType(String word){
        if (currentUnit == UnitTypes.NULL){
            UnitTypes unit = lMap.getUnit(word);

            if (unit != UnitTypes.NULL) {
                currentUnit = unit;
                return true;
            }
        }
        return false;
    }

    private boolean handleAmountType(String word){
        if (currentAmount == null){
            try {
                currentAmount = Integer.valueOf(word);
                return true;
            }
            catch (Exception ignored){

            }
        }
        return false;
    }

    private boolean handleDirectionType(String word){
        if (currentDirection == DirectionTypes.NULL){
            DirectionTypes direction = lMap.getDirection(word);

            if (direction != DirectionTypes.NULL) {
                currentDirection = direction;
                return true;
            }
        }
        return false;
    }

    private boolean handleShape(String word){
        if (currentShape == Shapes.NULL){
            Shapes shape = lMap.getShape(word);

            if (shape != Shapes.NULL){
                currentShape = shape;
                return true;
            }
        }
        return false;
    }

    private boolean handleRotation(String word){
        if (currentRotation == Rotations.NULL){
            Rotations rotation = lMap.getRotation(word);

            if (rotation != Rotations.NULL){
                currentRotation = rotation;
                return true;
            }
        }
        return false;
    }
    

    private void nullCurrent(){
        currentAction = ActionTypes.NULL;
        currentUnit = UnitTypes.NULL;
        currentAmount = null;
        currentDirection = DirectionTypes.NULL;
        currentShape = Shapes.NULL;
        currentRotation = Rotations.NULL;
    }
}