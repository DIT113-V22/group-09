package grammar;

import exceptions.UnclearInputException;
import commands_processing.CommandBuilder;
import commands_processing.CommandList;

public class TextTranslator {

    public CommandList translateText(String text) {
        String[] split_text = TextUnifier.unify(text);
        CommandBuilder cmBuilder = new CommandBuilder();

        if (text.isBlank()){
            throw new UnclearInputException("No input provided");
        }

        CommandList commandList = cmBuilder.processText(split_text);

        if (commandList.getList().isEmpty()){
            throw new UnclearInputException("The provided input is either too vague, grammatically incorrect or not supported.");
        }

        OmittanceInferer omInfer = new OmittanceInferer();
        omInfer.inferOmitted(commandList);

        return commandList;
    }

}