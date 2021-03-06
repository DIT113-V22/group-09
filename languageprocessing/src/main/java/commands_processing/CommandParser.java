package commands_processing;

import coordinates.CoordinateController;
import file_processing.FileLoader;
import grammar.LanguageEnums;
import grammar.LanguageEnums.DirectionTypes;
import grammar.LanguageEnums.UnitTypes;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

public class CommandParser {

    CoordinateController coordinateController;

    private static final int setsOfWheels = 2;

    private static final String defaultTurnSpeed = "40";
    private static final String defaultMoveSpeed = "50";
    private static final String defaultStopSpeed = "0";

    private HashMap<LanguageEnums.UnitTypes,Double> conversionsMap;

    private final String[] wheelSpeeds;


    public CommandParser(CoordinateController coordinateController) {
        this.coordinateController = coordinateController;
        try {
            conversionsMap = FileLoader.genericMapLoader(UnitTypes.class,Double.class,":",getClass().getResource("/files/unitConversions.txt"));
            if (conversionsMap.isEmpty()){
                throw new Exception("Loading conversionsMap went wrong or the file is empty.");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        wheelSpeeds = new String[setsOfWheels];
    }

    public CommandParser(CoordinateController coordinateController, String pathToLangResource){
        this.coordinateController = coordinateController;
        try {
            conversionsMap = FileLoader.genericMapLoader(UnitTypes.class,Double.class,":",new URL(pathToLangResource+"/files/unitConversions.txt"));
            if (conversionsMap.isEmpty()){
                throw new Exception("Loading conversionsMap went wrong or the file is empty.");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        wheelSpeeds = new String[setsOfWheels];
    }


    public String parseCommands(CommandList list) {
        Integer unconvertedAmount;
        Double conversionRate;
        String amount;
        LanguageEnums.CategoriesOfUnits unitCat;
        DirectionTypes direction;
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


            switch (command.getAction()) {
                case GO -> {
                    if (direction == DirectionTypes.BACK) {
                        Arrays.fill(wheelSpeeds, "-" + defaultMoveSpeed);
                    }
                    else if (direction == DirectionTypes.LEFT) {

                        inBetweenCSVEntry = "-" + defaultTurnSpeed + "," + defaultTurnSpeed + "," + "90,ANGULAR;";
                        builder.append(inBetweenCSVEntry);

                        Arrays.fill(wheelSpeeds, defaultMoveSpeed);
                    } else if (direction == DirectionTypes.RIGHT) {
                        inBetweenCSVEntry = defaultTurnSpeed + ",-" + defaultTurnSpeed + "," + "90,ANGULAR;";
                        builder.append(inBetweenCSVEntry);

                        Arrays.fill(wheelSpeeds, defaultMoveSpeed);
                    } else {
                        Arrays.fill(wheelSpeeds, defaultMoveSpeed);
                    }
                    break;
                }
                case STOP -> {
                    Arrays.fill(wheelSpeeds, defaultStopSpeed);
                    break;
                }
                case TURN -> {
                    if (direction == DirectionTypes.LEFT) {
                        wheelSpeeds[0] = "-" + defaultTurnSpeed;
                        wheelSpeeds[1] = defaultTurnSpeed;
                    } else {
                        wheelSpeeds[1] = "-" + defaultTurnSpeed;
                        wheelSpeeds[0] = defaultTurnSpeed;
                    }
                    break;
                }
                //TODO: REMOVE SOUT();
                case GO_TO_COORD -> {
                    System.out.println(command);
                    coordinateController.goToCoordinate(command.getXCoord(), command.getYCoord());
                }
                case RETURN -> {
                    System.out.println(command);
                    coordinateController.returnToStart();
                }
                default -> {
                    continue;
                }
            }

           for (int i = 0; i< setsOfWheels; i++){
               builder.append(wheelSpeeds[i]+",");
           }
           builder.append(amount+","+unitCat+";");
        }
        return builder.substring(0,builder.length()-1);
    }
}