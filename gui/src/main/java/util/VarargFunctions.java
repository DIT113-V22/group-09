package util;

import java.util.ArrayList;
import java.util.List;

public class VarargFunctions {

    public static ArrayList<String> massReplace(String toReplace, String replacement, String... strings){
        ArrayList<String> stringList = new ArrayList<>();
        for (String string : strings){
            stringList.add(string.replace(toReplace,replacement));
        }
        return stringList;
    }

    public static ArrayList<String> blankReplace(String replacement, String... strings){
        ArrayList<String> stringList = new ArrayList<>();
        for (String string : strings){
            if (string == null || string.isBlank()){
                stringList.add(replacement);
            }
            else {
                stringList.add(string);
            }
        }
        return stringList;
    }

    public static String concatenate(String separator, String... strings){
        StringBuilder builder =new StringBuilder();

        for (String string : strings){
            builder.append(string).append(separator);
        }
        return builder.substring(0,builder.length()-separator.length());
    }

    public static  <T> void addMany(List<T> originalList, T... additions){
            for (T addition : additions){
                originalList.add(addition);
            }
    }


}