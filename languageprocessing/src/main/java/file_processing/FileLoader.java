package file_processing;

import grammar.PhrasalVerb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import static settings.FilePaths.*;
import static grammar.LanguageEnums.*;

public class FileLoader {
    public static ArrayList<String> loadTxtFile(String filePath){
        BufferedReader reader;
        ArrayList<String> lines = new ArrayList<>();

        try{
            File file = new File(Path.of(filePath).normalize().toString().replace("\\","/"));
            reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();

            while(line != null){
                lines.add(line);
                line = reader.readLine();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return lines;
    }

    public static String listToString(ArrayList<String> list){
        StringBuilder builder = new StringBuilder();
        for (String entry :list){
            builder.append(entry+"\n");
        }
        return builder.toString();
    }

    public static HashMap<String, actionTypes> loadActionMap(){
        ArrayList<String> keyValPairs = loadTxtFile(ACTIONS_MAP_PATH);
        HashMap<String, actionTypes> actionMap = new HashMap<>();
        String[] keyAndVal;

        for (String keyValPair : keyValPairs){
            keyAndVal =keyValPair.replace(" ","").split(":");

            try {
                actionMap.put(keyAndVal[0],actionTypes.valueOf(keyAndVal[1]));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return actionMap;
    }

    public static HashMap<String, directionTypes> loadDirectionMap(){
        ArrayList<String> keyValPairs = loadTxtFile(DIRECTIONS_MAP_PATH);
        HashMap<String, directionTypes> directionMap = new HashMap<>();
        String[] keyAndVal;

        for (String keyValPair : keyValPairs){
            keyAndVal =keyValPair.replace(" ","").split(":");
            try {
                directionMap.put(keyAndVal[0],directionTypes.valueOf(keyAndVal[1]));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return directionMap;
    }

    public static HashMap<String, unitTypes> loadUnitMap(){
        ArrayList<String> keyValPairs = loadTxtFile(UNITS_MAP_PATH);
        HashMap<String, unitTypes> unitsMap = new HashMap<>();
        String[] keyAndVal;

        for (String keyValPair : keyValPairs){
            keyAndVal =keyValPair.replace(" ","").split(":");
            try {
                unitsMap.put(keyAndVal[0],unitTypes.valueOf(keyAndVal[1]));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return unitsMap;
    }

    public static HashMap<actionTypes,Integer> loadAmountFromActMap(){
        ArrayList<String> keyValPairs = loadTxtFile(AMOUNT_FROM_ACT_MAP_PATH);
        HashMap<actionTypes,Integer> amountFromActMap = new HashMap<>();
        String[] keyAndVal;

        for (String keyValPair : keyValPairs){
            keyAndVal =keyValPair.replace(" ","").split(":");
            try {
                amountFromActMap.put(actionTypes.valueOf(keyAndVal[0]),Integer.valueOf(keyAndVal[1]));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return amountFromActMap;
    }

    public static HashMap<actionTypes,directionTypes> loadDirFromActMap(){
        ArrayList<String> keyValPairs = loadTxtFile(DIR_FROM_ACT_MAP_PATH);
        HashMap<actionTypes,directionTypes> dirFromActMap = new HashMap<>();
        String[] keyAndVal;

        for (String keyValPair : keyValPairs){
            keyAndVal =keyValPair.replace(" ","").split(":");
            try {
                dirFromActMap.put(actionTypes.valueOf(keyAndVal[0]),directionTypes.valueOf(keyAndVal[1]));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return dirFromActMap;
    }

    public static HashMap<directionTypes,actionTypes> loadActFromDirMap(){
        ArrayList<String> keyValPairs = loadTxtFile(ACT_FROM_DIR_MAP_PATH);
        HashMap<directionTypes,actionTypes> actFromDirMap = new HashMap<>();
        String[] keyAndVal;

        for (String keyValPair : keyValPairs){
            keyAndVal =keyValPair.replace(" ","").split(":");
            try {
                actFromDirMap.put(directionTypes.valueOf(keyAndVal[0]),actionTypes.valueOf(keyAndVal[1]));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return actFromDirMap;
    }

    public static HashMap<actionTypes,unitTypes> loadUnitFromActMap(){
        ArrayList<String> keyValPairs = loadTxtFile(UNIT_FROM_ACT_MAP_PATH);
        HashMap<actionTypes,unitTypes> dirFromActMap = new HashMap<>();
        String[] keyAndVal;

        for (String keyValPair : keyValPairs){
            keyAndVal =keyValPair.replace(" ","").split(":");
            try {
                dirFromActMap.put(actionTypes.valueOf(keyAndVal[0]),unitTypes.valueOf(keyAndVal[1]));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return dirFromActMap;
    }


    public static ArrayList<PhrasalVerb> loadPhrasalVerbsList(){
        ArrayList<String> values = loadTxtFile(PHRASAL_VERBS_LIST_PATH);
        ArrayList<PhrasalVerb> phrasalVerbs = new ArrayList<>();

        for (String value : values){
            String[] componentWords = value.split(" ");
            phrasalVerbs.add(new PhrasalVerb(componentWords));
        }
        return phrasalVerbs;
    }
}