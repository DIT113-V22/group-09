package grammar;

import exceptions.UnclearInputException;
import commands_processing.CommandBuilder;
import commands_processing.CommandList;

public class TextTranslator {

    TextUnifier textUnifier;
    CommandBuilder cmBuilder;
    OmittanceInferer omInfer;

    public TextTranslator(){
       textUnifier = new TextUnifier();
        cmBuilder = new CommandBuilder();
        try {
            omInfer = new OmittanceInferer();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public TextTranslator(String pathToLangResource){
        textUnifier = new TextUnifier(pathToLangResource);
        cmBuilder = new CommandBuilder(pathToLangResource);

        try {
            omInfer = new OmittanceInferer(pathToLangResource);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public CommandList translateText(String text) {
        if (text.isBlank()){
            throw new UnclearInputException("No input provided");
        }

        String[] split_text = textUnifier.unify(text);



        CommandList commandList = cmBuilder.processText(split_text);

        if (commandList.getList().isEmpty()){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        try {

            omInfer.inferOmitted(commandList);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return commandList;
    }

}