package settings;

public class FilePaths {
    //The .replace operations are to resolve conflicts when the path is created via maven tests and/or on linux.
    public static final String FILES_PATH = System.getProperty("user.dir").replace("/","\\").replace("\\languageprocessing\\src\\test","\\languageprocessing").replace("\\languageprocessing\\languageprocessing","\\languageprocessing")+"\\src\\main\\resources\\files\\";
    public static final String ACTIONS_MAP_PATH = FILES_PATH+"actions_map.txt";
    public static final String DIRECTIONS_MAP_PATH = FILES_PATH+"directions_map.txt";
    public static final String UNITS_MAP_PATH = FILES_PATH+"units_map.txt";
    public static final String PHRASAL_VERBS_LIST_PATH = FILES_PATH+"phrasal_verbs_list.txt";
    public static final String CLAUSE_DIVIDERS_LIST_PATH = FILES_PATH+"clause_dividers_list.txt";
    public static final String PROVIDED_TEXT_PATH = FILES_PATH+"provided_text.txt";
    public static final String ACT_FROM_DIR_MAP_PATH = FILES_PATH+"inferActFromDir_map.txt";
    public static final String DIR_FROM_ACT_MAP_PATH = FILES_PATH+"inferDirFromAct_map.txt";
    public static final String AMOUNT_FROM_ACT_MAP_PATH = FILES_PATH+"inferAmountFromAct_map.txt";
    public static final String UNIT_FROM_ACT_MAP_PATH = FILES_PATH+"inferUnitFromAct_map.txt";
    public static final String TEST_TEXT_PATH = FILES_PATH+"txtTest.txt";
}