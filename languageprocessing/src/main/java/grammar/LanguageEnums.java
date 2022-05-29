package grammar;

public class LanguageEnums {
    //IntelliJ might show the enums as not being used, this is due to the fact that the maps/lists that use them are loaded from separate files.
    public enum ActionTypes {
        GO,
        TURN,
        STOP,
        GO_TO_COORD,
        CIRCLE,
        RETURN,
        NULL
    }

    public enum UnitTypes {
        MILLIMETER(CategoriesOfUnits.DISTANCE),
        CENTIMETER(CategoriesOfUnits.DISTANCE),
        DECIMETER(CategoriesOfUnits.DISTANCE),
        METER(CategoriesOfUnits.DISTANCE),
        KILOMETER(CategoriesOfUnits.DISTANCE),
        DEGREE(CategoriesOfUnits.ANGULAR),
        RADIAN(CategoriesOfUnits.ANGULAR),
        INCH(CategoriesOfUnits.DISTANCE),
        FOOT(CategoriesOfUnits.DISTANCE),
        MILE(CategoriesOfUnits.DISTANCE),
        YARD(CategoriesOfUnits.DISTANCE),
        MILLISECOND(CategoriesOfUnits.TIME),
        SECOND(CategoriesOfUnits.TIME),
        MINUTE(CategoriesOfUnits.TIME),
        HOUR(CategoriesOfUnits.TIME),
        NULL(CategoriesOfUnits.NONE),
        NOT_APPLICABLE(CategoriesOfUnits.NONE);

        private final CategoriesOfUnits typeOfUnit;

        UnitTypes(CategoriesOfUnits typeOfUnit) {
            this.typeOfUnit = typeOfUnit;
        }

        public CategoriesOfUnits getUnitCategory(){
            return typeOfUnit;
        }
    }

    public enum CategoriesOfUnits {
        DISTANCE,
        TIME,
        ANGULAR,
        NONE
    }

    public enum Shapes{
        CIRCLE,
        SQUARE,
        RECTANGLE,
        NULL;
    }

    public enum Rotations {
        CLOCKWISE,
        COUNTERCLOCKWISE,
        NULL;
    }


    public enum DirectionTypes {
        LEFT("RIGHT"),
        RIGHT("LEFT"),
        FORWARD("BACK"),
        BACK("FORWARD"),
        NULL("NOT_APPLICABLE"),
        NOT_APPLICABLE("NOT_APPLICABLE");

        private final String inverse;

        DirectionTypes(String inverseDirection){
            this.inverse = inverseDirection;
        }

        public DirectionTypes getInverseForCategory(CategoriesOfUnits category){
            //Returns an inverted direction, when applicable. For example: go forward -20m = go back 20m, but go forward -2s makes no sense.
            switch (category){
                case NONE, TIME -> {
                    return this;
                }
                case ANGULAR, DISTANCE -> {
                    return DirectionTypes.valueOf(inverse);
                }
                default -> {
                    return null;
                }
            }
        }

        public DirectionTypes getInverse(){
            return DirectionTypes.valueOf(inverse);
        }
    }
}