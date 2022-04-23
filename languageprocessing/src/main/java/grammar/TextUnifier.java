package grammar;

import file_processing.FileLoader;
import settings.FilePaths;

import java.util.ArrayList;

public class TextUnifier {
    public static String[] unify(String sentence){
        ArrayList<String> clauseDividers = FileLoader.loadTxtFile(FilePaths.CLAUSE_DIVIDERS_LIST_PATH);

        // "\n" should be replaced with the system default line separator (or something like that, I don't remember the details, but it was used in the OOP course).
        //Also, most likely, some other special symbols should be replaced by " " - to be added.
        sentence = sentence.toLowerCase().replace("\n"," ");
        sentence = lastCharUnify(sentence);

        for (String clauseDivider : clauseDividers){
            sentence=sentence.replace(clauseDivider," ,");
        }

        return sentence.split(" ");
    }

    private static String lastCharUnify(String sentence){
        if (sentence.charAt(sentence.length()-1) != '.'){
            sentence += ",";
        }
        return sentence;
    }
}
