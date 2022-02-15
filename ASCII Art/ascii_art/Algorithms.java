package ascii_art;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * The Algorithms class.
 * @author Adam Shtrasner
 */
public class Algorithms {

    /**
     * This function returns the only repeated number in an n+1 array
     * of values in the range [1, n].
     */
    public static int findDuplicate(int[] numList) {
        int oneStep = numList[0], twoStep = numList[numList[0]];

        while(oneStep != twoStep)
        {
            oneStep = numList[oneStep];
            twoStep = numList[numList[twoStep]];
        }

        twoStep = 0;

        while(oneStep != twoStep)
        {
            oneStep = numList[oneStep];
            twoStep = numList[twoStep];
        }

        return oneStep;
    }

    /**
     * Given a list of words, the function returns the number of unique morse
     * representations, that is - the number of words which have different morse
     * representation.
     */
    public static int uniqueMorseRepresentations(String[] words) {
        String[] morseCodes = new String[] {
                ".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.- ",
                ".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...- ",
                ".--","-..-","-.--","--.."
        };
        HashMap<Character, String> hashMap = new HashMap<>();
        int j = 0;
        for (char i = 'a'; i <= 'z'; i++) {
            hashMap.put(i, morseCodes[j]);
            j++;
        }
        Set<String> seen = new HashSet<>();
        for (String word: words) {
            seen.add(transform(word, hashMap));
        }
        return seen.size();
    }

    /*
     This function transforms a string to its morse representation.
     */
    private static String transform(String word, HashMap<Character, String> hashmap) {
        String morse = "";
        for (int i = 0; i < word.length(); i++) {
           morse = morse + hashmap.get(word.charAt(i));
        }
        return morse;
    }

}
