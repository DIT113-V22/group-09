package grammar;

import file_processing.FileLoader;

import java.net.URL;
import java.util.ArrayList;

public class TextUnifier {

    URL divPath;

    public TextUnifier(){
        divPath = getClass().getResource("/files/clause_dividers_list.txt");
    }

    public TextUnifier(String pathToLangResource){
        try {
            this.divPath = new URL(pathToLangResource+"/files/clause_dividers_list.txt");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String[] unify(String sentence){
        ArrayList<String> clauseDividers = null;
        try {
            clauseDividers = FileLoader.loadTxtFile(divPath);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // "\n" should be replaced with the system default line separator (or something like that, I don't remember the details, but it was used in the OOP course).
        //Also, most likely, some other special symbols should be replaced by " " - to be added.
        sentence = sentence.toLowerCase().replace("\n"," ");
        sentence = lastCharUnify(sentence);

        for (String clauseDivider : clauseDividers){
            sentence=sentence.replace(clauseDivider," ,");
        }

        return sentence.split(" ");
    }

    private String lastCharUnify(String sentence){
        if (sentence.charAt(sentence.length()-1) != '.'){
            sentence += ",";
        }
        return sentence;
    }
}
