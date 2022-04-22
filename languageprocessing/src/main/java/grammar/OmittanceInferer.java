package grammar;

import java.util.HashMap;

import exceptions.UnclearInputException;
import commands_processing.Command;
import commands_processing.CommandList;
import file_processing.FileLoader;
import grammar.LanguageEnums.*;

public class OmittanceInferer {
    private  final HashMap<directionTypes,actionTypes> actFromDirMap;
    private  final HashMap<actionTypes,directionTypes> dirFromActMap;
    private  final HashMap<actionTypes,unitTypes> unitFromActMap;
    private final HashMap<actionTypes,Integer> amountFromActMap;

    public  actionTypes inferActFromDir(directionTypes directionVal){
        if (directionVal == directionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        actionTypes inferred_action = actFromDirMap.get(directionVal);

        if (inferred_action == null){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }
        return inferred_action;
    }
    public directionTypes inferDirFromAct(actionTypes actionVal){

        if (actionVal == actionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        directionTypes inferred_direction = dirFromActMap.get(actionVal);

        if (inferred_direction == null){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }
        return inferred_direction;
    }

    public  unitTypes inferUnitFromAct(actionTypes actionVal){

        if (actionVal == actionTypes.NULL){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        unitTypes inferred_unit = unitFromActMap.get(actionVal);

        if (inferred_unit == null){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }
        return inferred_unit;
    }

    public int inferAmountFromAct(actionTypes actionVal){

        if (actionVal == actionTypes.NULL){
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
                actionTypes inferredAction = inferActFromDir(command.getDirection());
                command.setAction(inferredAction);
            }

            if (!command.hasDirection()){
                directionTypes inferredDirection = inferDirFromAct(command.getAction());
                command.setDirection(inferredDirection);
            }

            if (!command.hasUnit()){
                unitTypes inferredUnit = inferUnitFromAct(command.getAction());
                command.setUnit(inferredUnit);
            }

            if (!command.hasAmount()){
                Integer inferredAmount = inferAmountFromAct(command.getAction());
                command.setAmount(inferredAmount);
            }
        }
    }
}