package grammar;

import commands_processing.Command;
import commands_processing.CommandList;
import exceptions.UnclearInputException;
import file_processing.FileLoader;
import grammar.LanguageEnums.*;

import java.util.HashMap;

public class OmittanceInferer {
    private  final HashMap<DirectionTypes, ActionTypes> actFromDirMap;
    private  final HashMap<ActionTypes, DirectionTypes> dirFromActMap;
    private  final HashMap<ActionTypes, UnitTypes> unitFromActMap;
    private final HashMap<ActionTypes,Integer> amountFromActMap;

    public OmittanceInferer() throws Exception {
        actFromDirMap = FileLoader.genericMapLoader(DirectionTypes.class,ActionTypes.class,":",getClass().getResource("/files/inferActFromDir_map.txt"));
        dirFromActMap = FileLoader.genericMapLoader(ActionTypes.class,DirectionTypes.class,":",getClass().getResource("/files/inferDirFromAct_map.txt"));
        unitFromActMap = FileLoader.genericMapLoader(ActionTypes.class,UnitTypes.class,":",getClass().getResource("/files/inferUnitFromAct_map.txt"));
        amountFromActMap = FileLoader.genericMapLoader(ActionTypes.class,Integer.class,":",getClass().getResource("/files/inferAmountFromAct_map.txt"));
    }

    private ActionTypes inferActFromDir(DirectionTypes directionVal){
        if (directionVal == DirectionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        ActionTypes inferred_action = actFromDirMap.get(directionVal);

        if (inferred_action == null){
            throw new UnsupportedOperationException("Unsupported inference of action.");
        }
        return inferred_action;
    }
    private DirectionTypes inferDirFromAct(ActionTypes actionVal){

        if (actionVal == ActionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        DirectionTypes inferred_direction = dirFromActMap.get(actionVal);

        if (inferred_direction == null){
            throw new UnsupportedOperationException("Unsupported inference of direction.");
        }
        return inferred_direction;
    }

    private UnitTypes inferUnitFromAct(ActionTypes actionVal){

        if (actionVal == ActionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        UnitTypes inferred_unit = unitFromActMap.get(actionVal);

        if (inferred_unit == null){
            throw new UnsupportedOperationException("Unsupported inference of unit.");
        }
        return inferred_unit;
    }

    private int inferAmountFromAct(ActionTypes actionVal){

        if (actionVal == ActionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        Integer inferred_amount = amountFromActMap.get(actionVal);


        if (inferred_amount == null){
            throw new UnsupportedOperationException("Unsupported inference of amount.");
        }

        return inferred_amount;
    }

    //TODO: make consider making this more sophisticated.
    private Shapes inferShapeFromRotation(Rotations rotation){
        if (rotation==null){
            return Shapes.NULL;
        }

        return Shapes.CIRCLE;
    }

    private Rotations inferRotationFromShape(Shapes shape){
        if (shape==null){
            return Rotations.NULL;
        }
        return Rotations.CLOCKWISE;
    }


    private ActionTypes inferActionFromShape(Shapes shape){
        if (shape==null){
            throw new UnclearInputException("Provided input is incorrect, too vague or unsupported.");
        }
        return ActionTypes.GO;
    }





    public void inferOmitted(CommandList cmList){
        for (Command command : cmList.getList()){
            if (!command.hasAction()){
                ActionTypes inferredAction;
                if (command.hasDirection()){
                    inferredAction = inferActFromDir(command.getDirection());
                }
                else {
                    inferredAction = inferActionFromShape(command.getShape());
                }
                command.setAction(inferredAction);
            }

            if (!command.hasDirection()){
                DirectionTypes inferredDirection = inferDirFromAct(command.getAction());
                command.setDirection(inferredDirection);
            }

            if (!command.hasUnit()){
                UnitTypes inferredUnit = inferUnitFromAct(command.getAction());
                command.setUnit(inferredUnit);
            }

            if (!command.hasAmount()){
                Integer inferredAmount = inferAmountFromAct(command.getAction());
                command.setAmount(inferredAmount);
            }

            if (!command.hasShape()){
                Shapes inferredShape = inferShapeFromRotation(command.getRotation());
                command.setShape(inferredShape);
            }

            if (!command.hasRotation()){
                Rotations inferredRotation = inferRotationFromShape(command.getShape());
                command.setRotation(inferredRotation);
            }
        }
    }
}