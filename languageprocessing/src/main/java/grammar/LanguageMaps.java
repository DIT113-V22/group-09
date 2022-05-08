package grammar;

import file_processing.FileLoader;

import java.util.HashMap;
import java.util.Map;

public class LanguageMaps {
    private final Map<String, LanguageEnums.UnitTypes> unitsMap;
    private final Map<String, LanguageEnums.ActionTypes> actionMap;
    private final Map<String, LanguageEnums.DirectionTypes> directionMap;
    private final Map<String, LanguageEnums.Shapes> shapesMap;
    private final Map<String, LanguageEnums.Rotations> rotationsMap;

    public LanguageMaps(){
        actionMap = FileLoader.loadActionMap();
        unitsMap = FileLoader.loadUnitMap();
        directionMap = FileLoader.loadDirectionMap();

        shapesMap = new HashMap<>();
        shapesMap.put("square", LanguageEnums.Shapes.SQUARE);
        shapesMap.put("circle", LanguageEnums.Shapes.CIRCLE);
        shapesMap.put("circular", LanguageEnums.Shapes.CIRCLE);


        rotationsMap = new HashMap<>();
        rotationsMap.put("clockwise", LanguageEnums.Rotations.CLOCKWISE);
        rotationsMap.put("counterclockwise", LanguageEnums.Rotations.COUNTERCLOCKWISE);
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

    public LanguageEnums.Shapes getShape(String key){
        LanguageEnums.Shapes shape = shapesMap.get(key);

        if (shape != null){
            return shape;
        }
        else{
            return LanguageEnums.Shapes.NULL;
        }
    }

    public LanguageEnums.Rotations getRotation(String key){
        LanguageEnums.Rotations rotation = rotationsMap.get(key);

        if (rotation != null){
            return rotation;
        }
        else{
            return LanguageEnums.Rotations.NULL;
        }
    }


}