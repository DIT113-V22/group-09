package grammar;

import commands_processing.CommandList;
import file_processing.FileLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextTranslatorTest {
    TextTranslator translator;
    String text;
    CommandList cmList;
    String json;
    String csv;
    String expectedText;
    String[] texts;
    String[] expectedTexts;
    URL path;

    @BeforeAll
    public static void setup(){

    }

    @BeforeEach
    void setUp() {
        path = getClass().getResource("/files/txtTest.txt");
        translator = new TextTranslator();

        nullify();
    }

//    @BeforeEach
//    void setUp(String pathToLangResource){
//        try {
//            path = new URL(pathToLangResource+"/files/txtTest.txt");
//            translator = new TextTranslator(pathToLangResource);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        nullify();
//    }


    private void nullify(){
        text = null;
        cmList = null;
        json = null;
        expectedText = null;
        csv = null;
        texts = null;
        expectedTexts = null;
    }

    void testText(String text,String expected){
        cmList = translator.translateText(text);
        json = cmList.toString();
        assertEquals(expected,json);
    }

    @Test
    void shapeTest(){
        text = "Move forward and counterclockwise following a circumference of a circle with a radius of 4 feet.";
        expectedText = "Type:GO_CIRCLE_COUNTERCLOCKWISE Dir:FORWARD Amount:4 Unit:FOOT";
        testText(text,expectedText);
    }

    @Test
    void csvTest(){
        text = "Go forward for 100 meters.";
        cmList =translator.translateText(text);
        csv= cmList.toCSV();
        expectedText = "action,direction,amount\n" +
                            "GO,FORWARD,100";

        assertEquals(expectedText,csv);
    }


    @Test
    void simpleInference() {
        texts = new String[]{
                "go",
                "forward",
                "back",
                "right",
                "left"
        };
        expectedTexts = new String[] {
                "Type:GO Dir:FORWARD Amount:MAX Unit:CENTIMETER",
                "Type:GO Dir:FORWARD Amount:MAX Unit:CENTIMETER",
                "Type:GO Dir:BACK Amount:MAX Unit:CENTIMETER",
                "Type:TURN Dir:RIGHT Amount:90 Unit:DEGREE",
                "Type:TURN Dir:LEFT Amount:90 Unit:DEGREE"
        };
        for (int i=0; i<texts.length; i++){
            if (i==3){
                System.out.println();
            }
            testText(texts[i], expectedTexts[i]);
        }
    }

    @Test
    void simpleSingleCommands() {
        texts = new String[]{
                "go forward.",
                "go back",
                "turn left.",
                "turn right.",
                "stop",
                "return"
        };
        expectedTexts = new String[] {
                "Type:GO Dir:FORWARD Amount:MAX Unit:CENTIMETER",
                "Type:GO Dir:BACK Amount:MAX Unit:CENTIMETER",
                "Type:TURN Dir:LEFT Amount:90 Unit:DEGREE",
                "Type:TURN Dir:RIGHT Amount:90 Unit:DEGREE",
                "Type:STOP Dir:FORWARD Amount:0 Unit:CENTIMETER",
                "Type:RETURN Dir:NOT_APPLICABLE Amount:0 Unit:NOT_APPLICABLE"
        };
        for (int i=0; i<texts.length; i++){
            testText(texts[i], expectedTexts[i]);
        }
    }


    @Test
    void simpleCoords() {
        texts = new String[]{
                "go to 12,13.",
                "go to the coordinates 321,12.",
                "go to coordinate 420,1"

        };
        expectedTexts = new String[] {
                "Type:GO_TO_COORD X:12 Y:13",
                "Type:GO_TO_COORD X:321 Y:12",
                "Type:GO_TO_COORD X:420 Y:1",

        };
        for (int i=0; i<texts.length; i++){
            testText(texts[i], expectedTexts[i]);
        }
    }




    @Test
    void simpleMultiCommands() {
        texts = new String[]{
                "go forward then stop.",
                "go back, after that turn left",
        };
        expectedTexts = new String[] {
                "Type:GO Dir:FORWARD Amount:MAX Unit:CENTIMETER\n" +
                        "Type:STOP Dir:FORWARD Amount:0 Unit:CENTIMETER",
                "Type:GO Dir:BACK Amount:MAX Unit:CENTIMETER\n" +
                        "Type:TURN Dir:LEFT Amount:90 Unit:DEGREE"
        };
        for (int i=0; i<texts.length; i++){
            testText(texts[i], expectedTexts[i]);
        }
    }

    @Test
    void complexSingleCommands() {
        texts = new String[]{
                "go forward for 50 m.",
                "Go back for 12 seconds",
                "turn left 50 degrees.",
                "turn 32 radians to the right.",
                "stop after 50 yards"
        };
        expectedTexts = new String[] {
                "Type:GO Dir:FORWARD Amount:50 Unit:METER",
                "Type:GO Dir:BACK Amount:12 Unit:SECOND",
                "Type:TURN Dir:LEFT Amount:50 Unit:DEGREE",
                "Type:TURN Dir:RIGHT Amount:32 Unit:RADIAN",
                "Type:STOP Dir:FORWARD Amount:50 Unit:YARD"
        };
        for (int i=0; i<texts.length; i++){
            testText(texts[i], expectedTexts[i]);
        }
    }

    @Test
    void complexMultiCommands() {
        texts = new String[]{
                "go forward for 12 seconds then turn 43 degrees left.",
                "go 11 meters back, after that turn 32 degrees to the left, then stop",
                "go to the coordinate 420,1, then go forward for 3 meters, after that return to start, then go to 2,33"
        };
        expectedTexts = new String[] {
                "Type:GO Dir:FORWARD Amount:12 Unit:SECOND\n" +
                    "Type:TURN Dir:LEFT Amount:43 Unit:DEGREE",
                "Type:GO Dir:BACK Amount:11 Unit:METER\n" +
                    "Type:TURN Dir:LEFT Amount:32 Unit:DEGREE\n" +
                        "Type:STOP Dir:FORWARD Amount:0 Unit:CENTIMETER",
                "Type:GO_TO_COORD X:420 Y:1\n" +
                    "Type:GO Dir:FORWARD Amount:3 Unit:METER\n"+
                        "Type:RETURN Dir:NOT_APPLICABLE Amount:0 Unit:NOT_APPLICABLE\n"+
                            "Type:GO_TO_COORD X:2 Y:33"
        };
        for (int i=0; i<texts.length; i++){
            testText(texts[i], expectedTexts[i]);
        }
    }

    @Test
    void loadFromTxt(){
        expectedText = "Type:GO Dir:FORWARD Amount:10 Unit:METER\n" +
                            "Type:TURN Dir:LEFT Amount:90 Unit:DEGREE";

        try {

            ArrayList<String> textList = FileLoader.loadTxtFile(path);
            testText(FileLoader.listToString(textList),expectedText);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}