package commands_processing;

import grammar.TextTranslator;

import java.util.ArrayList;

public class InputProcessor {
    //TODO:Move this class somewhere else where it makes more sense to have it.
    private final TextTranslator txtTranslator;
    private final CommandParser comParser;
    private CommandList cmList;

    public InputProcessor(){
        comParser = new CommandParser();
        txtTranslator = new TextTranslator();
    }

    public String processInput(String textInput){
        cmList = null;
        cmList = txtTranslator.translateText(textInput);
        return comParser.parseCommands(cmList);
    }

    public String getLatestCommands(){
        return cmList.toJSON();
    }

    public ArrayList<String> getCmList(){
        ArrayList<String> stringList = new ArrayList<>();


        for (Command command : cmList.getList()){
            stringList.add(command.toString());
        }

        return stringList;
    }
}
