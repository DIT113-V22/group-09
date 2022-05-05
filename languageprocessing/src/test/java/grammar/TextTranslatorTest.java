package grammar;

import commands_processing.CommandList;
import file_processing.FileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import settings.FilePaths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TextTranslatorTest {
    TextTranslator translator;
    String text;
    CommandList cmList;
    String json;
    String csv;
    String expectedText;
    String[] texts;
    String[] expectedTexts;

    @BeforeEach
    void setUp() {
        translator = new TextTranslator();
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
                "stop"
        };
        expectedTexts = new String[] {
                "Type:GO Dir:FORWARD Amount:MAX Unit:CENTIMETER",
                "Type:GO Dir:BACK Amount:MAX Unit:CENTIMETER",
                "Type:TURN Dir:LEFT Amount:90 Unit:DEGREE",
                "Type:TURN Dir:RIGHT Amount:90 Unit:DEGREE",
                "Type:STOP Dir:FORWARD Amount:0 Unit:CENTIMETER"
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
        };
        expectedTexts = new String[] {
                "Type:GO Dir:FORWARD Amount:12 Unit:SECOND\n" +
                        "Type:TURN Dir:LEFT Amount:43 Unit:DEGREE",
                "Type:GO Dir:BACK Amount:11 Unit:METER\n" +
                        "Type:TURN Dir:LEFT Amount:32 Unit:DEGREE\n" +
                            "Type:STOP Dir:FORWARD Amount:0 Unit:CENTIMETER"
        };
        for (int i=0; i<texts.length; i++){
            testText(texts[i], expectedTexts[i]);
        }
    }

    @Test
    void loadFromTxt(){
        expectedText = "Type:GO Dir:FORWARD Amount:10 Unit:METER\n" +
                            "Type:TURN Dir:LEFT Amount:90 Unit:DEGREE";

        ArrayList<String> textList = FileLoader.loadTxtFile(FilePaths.TEST_TEXT_PATH);
        testText(FileLoader.listToString(textList),expectedText);
    }
}