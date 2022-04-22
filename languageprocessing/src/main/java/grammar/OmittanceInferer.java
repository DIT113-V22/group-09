package grammar;

import java.util.HashMap;

import exceptions.UnclearInputException;
import commands_processing.Command;
import commands_processing.CommandList;
import file_processing.FileLoader;
import grammar.LanguageEnums.*;

public class OmittanceInferer {
    private  final HashMap<DirectionTypes, ActionTypes> actFromDirMap;
    private  final HashMap<ActionTypes, DirectionTypes> dirFromActMap;
    private  final HashMap<ActionTypes, UnitTypes> unitFromActMap;
    private final HashMap<ActionTypes,Integer> amountFromActMap;

    public ActionTypes inferActFromDir(DirectionTypes directionVal){
        if (directionVal == DirectionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        ActionTypes inferred_action = actFromDirMap.get(directionVal);

        if (inferred_action == null){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }
        return inferred_action;
    }
    public DirectionTypes inferDirFromAct(ActionTypes actionVal){

        if (actionVal == ActionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        DirectionTypes inferred_direction = dirFromActMap.get(actionVal);

        if (inferred_direction == null){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }
        return inferred_direction;
    }

    public UnitTypes inferUnitFromAct(ActionTypes actionVal){

        if (actionVal == ActionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        UnitTypes inferred_unit = unitFromActMap.get(actionVal);

        if (inferred_unit == null){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }
        return inferred_unit;
    }

    public int inferAmountFromAct(ActionTypes actionVal){

        if (actionVal == ActionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        Integer inferred_amount = amountFromActMap.get(actionVal);


        if (inferred_amount == null){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        return inferred_amount;
    }

    public OmittanceInferer() {
        actFromDirMap = FileLoader.loadActFromDirMap();
        dirFromActMap = FileLoader.loadDirFromActMap();
        unitFromActMap = FileLoader.loadUnitFromActMap();
        amountFromActMap = FileLoader.loadAmountFromActMap();
    }

    public void inferOmitted(CommandList cmList){
        for (Command command : cmList.getList()){
            if (!command.hasAction()){
                ActionTypes inferredAction = inferActFromDir(command.getDirection());
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
        }
    }
}