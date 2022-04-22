package grammar;

public class LanguageEnums {
    //IntelliJ might show the enums as not being used, this is due to the fact that the maps/lists that use them are loaded from separate files.
    public enum actionTypes {
        GO,
        TURN,
        STOP,
        CIRCLE,
        RETURN,
        NULL,
        NOT_APPLICABLE
    }

    public enum unitTypes {
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

    public enum directionTypes {
        LEFT,
        RIGHT,
        FORWARD,
        BACK,
        NULL,
        NOT_APPLICABLE
    }

}