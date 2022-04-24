package commands_processing;

import file_processing.FileLoader;
import grammar.LanguageEnums;
import grammar.LanguageEnums.*;

import java.util.Arrays;
import java.util.HashMap;

public class CommandParser {

    private static final int setsOfWheels = 2;

    //TODO:Make it adjustable in settings.
    private static final String defaultTurnSpeed = "40";
    private static final String defaultMoveSpeed = "50";
    private static final String defaultStopSpeed = "0";

    private final HashMap<LanguageEnums.UnitTypes,Double> conversionsMap;

    private final String[] wheelSpeeds;




    public CommandParser() {
        conversionsMap = FileLoader.loadConversionMap();
        wheelSpeeds = new String[setsOfWheels];
    }

//    private String parseCommand(Command command){
//
//        return ";";
//    }


    public String parseCommands(CommandList list) {
        Integer unconvertedAmount;
        Double conversionRate;
        String amount;
        LanguageEnums.CategoriesOfUnits unitCat;
        DirectionTypes direction;
        Command inBetweenCommand;
        String inBetweenCSVEntry;

        StringBuilder builder = new StringBuilder();
        for (Command command : list.getList()) {

            unitCat = command.getUnit().getUnitCategory();
            unconvertedAmount = command.getAmount();
            conversionRate = conversionsMap.get(command.getUnit());
            direction = command.getDirection();

            if (unconvertedAmount<0){
                unconvertedAmount *= -1;
                direction = direction.getInverseForCategory(unitCat);
            }

            amount = Integer.toString((int) (unconvertedAmount*conversionRate));


           switch (command.getAction()){
               case GO: {
                    if (direction == DirectionTypes.BACK){
                        Arrays.fill(wheelSpeeds,"-"+defaultMoveSpeed);
                    }
                    //TODO: Make more methods to offload the in-between commands somewhere else.
                    else if (direction == DirectionTypes.LEFT){

                        inBetweenCSVEntry = "-"+defaultTurnSpeed+","+defaultTurnSpeed+","+"90,ANGULAR;";
                        builder.append(inBetweenCSVEntry);

                        Arrays.fill(wheelSpeeds,defaultMoveSpeed);
                    }
                    else if (direction == DirectionTypes.RIGHT){
                        inBetweenCSVEntry = defaultTurnSpeed+",-"+defaultTurnSpeed+","+"90,ANGULAR;";
                        builder.append(inBetweenCSVEntry);

                        Arrays.fill(wheelSpeeds,defaultMoveSpeed);
                    }
                    else {
                        Arrays.fill(wheelSpeeds,defaultMoveSpeed);
                    }
                    break;
               }
               case STOP:{
                   Arrays.fill(wheelSpeeds, defaultStopSpeed);
                   break;
               }
               case TURN:{
                   if (direction == DirectionTypes.LEFT){
                       wheelSpeeds[0] = "-"+defaultTurnSpeed;
                       wheelSpeeds[1] = defaultTurnSpeed;
                   }
                   else {
                       wheelSpeeds[1] = "-"+defaultTurnSpeed;
                       wheelSpeeds[0] = defaultTurnSpeed;
                   }
                   break;
               }
               default:{
                    continue;
               }
           }

           for (int i = 0; i< setsOfWheels; i++){
               builder.append(wheelSpeeds[i]+",");
           }
           builder.append(amount+","+unitCat+";");
        }
        //TODO: Determine if the last ';' symbol should be there.
        return builder.substring(0,builder.length()-1);
    }
}