package grammar;

import file_processing.FileLoader;

import java.util.ArrayList;
import java.util.Comparator;

public class PhrasalVerbChecker implements Comparator<PhrasalVerb> {

    private final ArrayList<PhrasalVerb> pVerbList;

    public PhrasalVerbChecker() {
        pVerbList = FileLoader.loadPhrasalVerbsList();
    }

    public PhrasalVerb findPhrasal(String[] text, int startPos){
        int n = 0;

        //Making a copy of the list so that non-matching pVerbs can be removed.
        ArrayList<PhrasalVerb> matchingPhrasals = new ArrayList<>(pVerbList);
        ArrayList<PhrasalVerb> confirmedPhrasals = new ArrayList<>();
        ArrayList<PhrasalVerb> nonMatchingPhrasals = new ArrayList<>();


        for (int i = startPos; i<text.length; i++){
            if (matchingPhrasals.isEmpty()){
                break;
            }
            for (PhrasalVerb pVerb : matchingPhrasals){
                if (n > pVerb.length()-1){
                    confirmedPhrasals.add(pVerb);
                    nonMatchingPhrasals.add(pVerb);
                }
                else if (!text[i].equals(pVerb.wordAt(i))){
                    nonMatchingPhrasals.add(pVerb);
                }
            }
            //Required to avoid ConcurrentModificationException, as it is not allowed to modify a List during iteration.
            matchingPhrasals.removeAll(nonMatchingPhrasals);
            nonMatchingPhrasals.clear();
            n++;
        }
        confirmedPhrasals.addAll(matchingPhrasals);

        //Sorts by length to choose the longest matching phrasal verb.
        confirmedPhrasals.sort(this::compare);


        if (confirmedPhrasals.isEmpty()){
            return null;
        }
        else {
            return confirmedPhrasals.get(0);
        }
    }

    public int compare(PhrasalVerb pv1, PhrasalVerb pv2){
        if (pv1.length() > pv2.length()){
            return 1;
        }
        else if (pv1.length() < pv2.length()){
            return -1;
        }
        else {
            return 0;
        }
    }
}