package grammar;

public class LanguageEnums {
    //IntelliJ might show the enums as not being used, this is due to the fact that the maps/lists that use them are loaded from separate files.
    public enum ActionTypes {
        GO,
        TURN,
        STOP,
        CIRCLE,
        RETURN,
        NULL,
        NOT_APPLICABLE
    }

    public enum UnitTypes {
        MILLIMETER,
        CENTIMETER,
        DECIMETER,
        METER,
        KILOMETER,
        DEGREE,
        RADIAN,
        INCH,
        FOOT,
        MILE,
        YARD,
        MILLISECOND,
        SECOND,
        MINUTE,
        HOUR,
        NULL,
        NOT_APPLICABLE
    }

    public enum DirectionTypes {
        LEFT,
        RIGHT,
        FORWARD,
        BACK,
        NULL,
        NOT_APPLICABLE
    }

}