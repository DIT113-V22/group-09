package grammar;

import file_processing.FileLoader;

import java.util.Map;


public class LanguageMaps {
    private final Map<String, LanguageEnums.UnitTypes> unitsMap;
    private final Map<String, LanguageEnums.ActionTypes> actionMap;
    private final Map<String, LanguageEnums.DirectionTypes> directionMap;

    public LanguageMaps(){
        actionMap = FileLoader.loadActionMap();
        unitsMap = FileLoader.loadUnitMap();
        directionMap = FileLoader.loadDirectionMap();
    }

    public LanguageEnums.ActionTypes getAction(String key){
        LanguageEnums.ActionTypes action = actionMap.get(key);

        if (action != null){
            return action;
        }
        else{
            return LanguageEnums.ActionTypes.NULL;
        }
    }


    public LanguageEnums.UnitTypes getUnit(String key){
        LanguageEnums.UnitTypes unit = unitsMap.get(key);

        if (unit != null){
            return unit;
        }
        else{
            return LanguageEnums.UnitTypes.NULL;
        }
    }

    public LanguageEnums.DirectionTypes getDirection(String key){
        LanguageEnums.DirectionTypes direction = directionMap.get(key);

        if (direction != null){
            return direction;
        }
        else{
            return LanguageEnums.DirectionTypes.NULL;
        }
    }
}