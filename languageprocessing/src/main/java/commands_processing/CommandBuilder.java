package commands_processing;

import grammar.LanguageEnums.*;
import grammar.LanguageMaps;
import grammar.PhrasalVerb;
import grammar.PhrasalVerbChecker;

public class CommandBuilder {
    private final LanguageMaps lMap;
    private final CommandList cmList;
    private PhrasalVerb phrasalVerb;
    private boolean afterUsed,andUsed;
    private final PhrasalVerbChecker pvChecker;

    private ActionTypes currentAction;
    private UnitTypes currentUnit;
    private Integer currentAmount;
    private DirectionTypes currentDirection;

    public CommandBuilder() {
        lMap = new LanguageMaps();
        cmList = new CommandList();
        pvChecker = new PhrasalVerbChecker();
        
        phrasalVerb = null;
        afterUsed = false;
        andUsed = false;

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
            if (handleActionType(text[i])){
                continue;
            }
            else if (handleDirectionType(text[i])){
                continue;
            }
            else if (handleUnitType(text[i])){
                continue;
            }
            else if (handleAmountType(text[i])){
                continue;
            }

            //Comma indicates that the clause has ended and a new one will begin (or that the text has ended)
            //In almost all cases every command is contained within one clause, so it can be now added to the list of commands.
            if (text[i].equals(",")){
                Command command = new Command(currentAction,currentDirection,currentAmount,currentUnit);
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
            }
        }
        return false;
    }

    private void nullCurrent(){
        currentAction = ActionTypes.NULL;
        currentUnit = UnitTypes.NULL;
        currentAmount = null;
        currentDirection = DirectionTypes.NULL;
    }
}