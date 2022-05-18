package file_processing;

import grammar.PhrasalVerb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;



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

    public static ArrayList<String> loadTxtFile(File file, String delimiter) throws Exception{
        BufferedReader reader;
        ArrayList<String> lines = new ArrayList<>();

        reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();

        while(line != null){
            lines.add(line);
            line = reader.readLine();
        }

        return lines;
    }

    public static <K, V> HashMap<K, V> genericMapLoader(java.lang.reflect.Type kClass, java.lang.reflect.Type vClass, String delimiter, String keyValSeparator, URL path) throws Exception{
        Class c1 = (Class) kClass;
        Class c2 = (Class) vClass;

        if (!c1.isEnum() && !(c1.getSimpleName().equals("String")) && !(c1.getSimpleName().equals("Integer")) && !(c1.getSimpleName().equals("Double"))){
            throw new IllegalArgumentException("Unsupported key type provided.");
        }
        if (!c2.isEnum() && !(c2.getSimpleName().equals("String")) && !(c2.getSimpleName().equals("Integer")) && !(c2.getSimpleName().equals("Double"))){
            throw new IllegalArgumentException("Unsupported value type provided.");
        }

        File file = new File(correctPath(path));
        ArrayList<String> kvPairs = loadTxtFile(file,delimiter);

        K key;
        V value;
        HashMap<K, V> genericMap = new HashMap<>();

        for (String pair : kvPairs){
            String[] keyVal = pair.split(keyValSeparator);

            if (c1.isEnum()){
                key = (K) Enum.valueOf(c1,keyVal[0]);
            }
            else if (c1.getSimpleName().equals("String")) {
                key = (K) keyVal[0];
            }
            else if (c1.getSimpleName().equals("Integer")){
                key = (K) Integer.valueOf(keyVal[0]);
            }
            else {
                key = (K) Double.valueOf(keyVal[0]);
            }

            if (c2.isEnum()){
                value = (V) Enum.valueOf(c2,keyVal[1]);
            }
            else if (c2.getSimpleName().equals("String")) {
                value = (V) keyVal[1];
            }
            else if (c2.getSimpleName().equals("Integer")){
                value = (V) Integer.valueOf(keyVal[1]);
            }
            else {
                value = (V) Double.valueOf(keyVal[1]);
            }

            genericMap.put(key,value);
        }
        return genericMap;
    }

    private static URI correctPath(URL path) throws URISyntaxException {
        String correctedPath = path.toString();
        correctedPath = correctedPath.replace("/target/classes","/src/main/resources");
        return new URI(correctedPath);
    }

    public static ArrayList<String> loadTxtFile(URL filePath){
        BufferedReader reader;
        ArrayList<String> lines = new ArrayList<>();

        try{
            reader = new BufferedReader(new InputStreamReader(filePath.openStream()));

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

    public static ArrayList<PhrasalVerb> loadPhrasalVerbsList(URL url){
        ArrayList<String> values = loadTxtFile(url);
        ArrayList<PhrasalVerb> phrasalVerbs = new ArrayList<>();

        for (String value : values){
            String[] componentWords = value.split(" ");
            phrasalVerbs.add(new PhrasalVerb(componentWords));
        }
        return phrasalVerbs;
    }
}