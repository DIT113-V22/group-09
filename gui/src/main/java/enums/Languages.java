package enums;

public enum Languages {
    ENGLISH,
    POLISH,
    SWEDISH,
    KOREAN,
    PERSIAN;

    public String getHPath() {
            return "/languages/"+this.toString().toLowerCase()+"/head_s1.txt/";
    }

    public String getS1Path() {
        return "/languages/"+this.toString().toLowerCase()+"/s1.txt/";
    }

    public String getS2Path() {
        return "/languages/"+this.toString().toLowerCase()+"/s2.txt/";
    }

    public String getPTxtPath() {
        return "/languages/"+this.toString().toLowerCase()+"/pTxt_s1.txt/";
    }
}