package util;

public class ValidityChecker {

    public static boolean notNull(Object... objects){
        for (Object object: objects){
            if (object == null){
                return false;
            }
        }
        return true;
    }

    public static boolean notBlank(String... strings){
        for (String string: strings){
            if (string.isBlank()){
                return false;
            }
        }
        return true;
    }

    public static boolean notEmpty(String... strings){
        for (String string: strings){
            if (string.isEmpty()){
                return false;
            }
        }
        return true;
    }

    public static boolean notNullOrBlank(String... strings){
        //Do not cast to Object. This break the notNull checking.
        boolean isNull = !notNull(strings);

        if (isNull){
            return false;
        }
        else {
            return notBlank(strings);
        }

    }

    public static boolean anyEquals(String searched,String... strings){
        for (String string : strings){
            if (string.equals(searched)){
                return true;
            }
        }
        return false;
    }

}
