package grammar;

import file_processing.FileLoader;

import java.util.ArrayList;

public class TextUnifier {
    public String[] unify(String sentence){
        ArrayList<String> clauseDividers = null;
        try {
            clauseDividers = FileLoader.loadTxtFile(getClass().getResource("/files/clause_dividers_list.txt"));
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
