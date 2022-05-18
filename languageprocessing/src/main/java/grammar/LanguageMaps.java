package grammar;

import file_processing.FileLoader;

import java.util.Map;

public class LanguageMaps {
    private final Map<String, LanguageEnums.UnitTypes> unitsMap;
    private final Map<String, LanguageEnums.ActionTypes> actionMap;
    private final Map<String, LanguageEnums.DirectionTypes> directionMap;
    private final Map<String, LanguageEnums.Shapes> shapesMap;
    private final Map<String, LanguageEnums.Rotations> rotationsMap;

    public LanguageMaps() throws Exception {
        actionMap =  FileLoader.genericMapLoader(String.class, LanguageEnums.ActionTypes.class, "/n", ":",getClass().getResource("/files/actions_map.txt"));
        unitsMap = FileLoader.genericMapLoader(String.class, LanguageEnums.UnitTypes.class, "/n", ":",getClass().getResource("/files/units_map.txt"));
        directionMap =  FileLoader.genericMapLoader(String.class, LanguageEnums.DirectionTypes.class, "/n", ":",getClass().getResource("/files/directions_map.txt"));
        shapesMap = FileLoader.genericMapLoader(String.class, LanguageEnums.Shapes.class, "/n",":",getClass().getResource("/files/shapes_map.txt"));
        rotationsMap = FileLoader.genericMapLoader(String.class, LanguageEnums.Rotations.class, "/n",":",getClass().getResource("/files/rotations_map.txt"));
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