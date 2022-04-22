package grammar;

import file_processing.FileLoader;

import java.util.HashMap;
import java.util.Map;
import static grammar.LanguageEnums.actionTypes.*;
import static grammar.LanguageEnums.directionTypes.*;
import static grammar.LanguageEnums.unitTypes.*;


public class LanguageMaps {
    private final Map<String, LanguageEnums.unitTypes> unitsMap;
    private final Map<String, LanguageEnums.actionTypes> actionMap;
    private final Map<String, LanguageEnums.directionTypes> directionMap;

    public LanguageMaps(){
        actionMap = FileLoader.loadActionMap();
        unitsMap = FileLoader.loadUnitMap();
        directionMap = FileLoader.loadDirectionMap();
    }

    public LanguageEnums.actionTypes getAction(String key){
        LanguageEnums.actionTypes action = actionMap.get(key);

        if (action != null){
            return action;
        }
        else{
            return LanguageEnums.actionTypes.NULL;
        }
    }


    public LanguageEnums.unitTypes getUnit(String key){
        LanguageEnums.unitTypes unit = unitsMap.get(key);

        if (unit != null){
            return unit;
        }
        else{
            return LanguageEnums.unitTypes.NULL;
        }
    }

    public LanguageEnums.directionTypes getDirection(String key){
        LanguageEnums.directionTypes direction = directionMap.get(key);

        if (direction != null){
            return direction;
        }
        else{
            return LanguageEnums.directionTypes.NULL;
        }
    }
}