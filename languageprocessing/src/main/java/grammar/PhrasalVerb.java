package grammar;

public class PhrasalVerb {
    private final String[] componentWords;

    public PhrasalVerb(String[] componentWords) {
        this.componentWords = componentWords;
    }

    public String wordAt(int n){
        if (n>componentWords.length-1){
            return "";
        }
        else {
            return componentWords[n];
        }
    }

    public int length(){
        return componentWords.length;
    }

    public String toString(){
        StringBuilder output = new StringBuilder();
        for (String word : componentWords){
            output.append(word).append(" ");
        }

        return output.toString();
    }

    public String toSingleWord(){
        StringBuilder output = new StringBuilder();
        for (String word : componentWords){
            output.append(word).append("-");
        }
        String outStr = output.toString();

        return outStr.substring(0,output.length()-1);
    }
}