/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class ResultCompare implements Comparable<ResultCompare>{
    public String title;
    public String snippet;
    public double percent;
    public String url;
    public String language;
//    public String webpage; // the full webpage
    public String extraInfo; // E.G. "Looked through whole webpage", "Couldn't load web page, just looked at snippet", etc
    ArrayList<WordMatch> wordMatches; // This is the text that we are trying to find, seperated into WordMatch objects
    public static Pattern punctuation = Pattern.compile("(,|\\.|\\!|:|;|\\?|\\\\|\\/|\\(|\\)|\\[|\\]|'|\"|@|\\+|\\-|_|=|\\*|©|\\{|\\})"); // matches any punctuation
    
    ResultCompare(String title, String snippet, String url, String language, ArrayList<WordMatch> wordMatches){
        this.snippet = snippet;
        this.title = title;
        this.url = url;
        this.language = language;
        this.percent = 0;
        this.extraInfo = "";
        this.wordMatches = new ArrayList<>();
        for (WordMatch wm: wordMatches) { // deep copy wordMatches:
            this.wordMatches.add(wm);
        }
    }
    
    public double calcPercent(String content, String comparativeContent){
        System.out.println("called calcPercent()...");
        WordMatch.setMatches(wordMatches, false); // clear all previous matches:
        double percentContent = 0;
        // If content or comparativeContent can be found inside the other.
        if (content.contains(comparativeContent) || comparativeContent.contains(content)) {
            WordMatch.setMatches(wordMatches, true);
            percent = 100;
            return 100;
        }
        String contentLC; // content in lower case and without punctuation
        String compContentLC; // comparative content in lower case and without punctuation
        contentLC = content.replaceAll(punctuation.toString(), "").toLowerCase();
        compContentLC = comparativeContent.replaceAll(punctuation.toString(), "").toLowerCase();
        String[] contentWords = contentLC.trim().split(" ");
//        String[] compContentWords = compContentLC.trim().split(" ");
        // Now with both contents having been filtered for punctuation and converted to lowercase,
        // Check again if one contains the other:
        if (contentLC.contains(compContentLC) || compContentLC.contains(contentLC)) {
            WordMatch.setMatches(wordMatches, true);
            percent = 100;
            return 100;
        }
        int numberOfMatches = 0;
        // Look for each wordMatches.wordEdited in contentWords
        try {
            for (int i = 0; i < wordMatches.size(); i++) {
                for (int j = 0; j < contentWords.length; j++) {
                    int count = 0; // number of words matched in a row
                    int i2 = i, j2 = j;
                    while (wordMatches.get(i2).wordEdited.matches(contentWords[j2])) {
                        // Keep looping in here while next words match each other.
                        count++; // count the number of matched words
                        i2++;
                        j2++;
                        if (i2 >= wordMatches.size() || j2 >= contentWords.length) // if either index went past array bounds
                            break;
                    }
                    // if the number of matched words in a row was 3 or more, then mark those words as matched in trueComparedIndices.
                    if (count >= 3) {
                        for (int k = 0; k < count; k++) {
                            wordMatches.get(i+k).matched = true;
                        }
                        i += count-1; // increment i by the number of matched words, because there is no longer any need to compare them again
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } 
        for (WordMatch n : wordMatches) {
            if (n.matched) {
                numberOfMatches++;
            }
        }
        percentContent = Math.round(((double)numberOfMatches / (double)wordMatches.size()) * 100);
        System.out.println("Percentage of content found: " + percentContent);
        
        percent = percentContent;
        return percentContent;
    }
    
    @Override
    public String toString(){
        return title + "\n" + snippet + "\n" + url + "\n" + extraInfo;
    }
    
    // sorts array list by percentage match
    @Override
    public int compareTo(ResultCompare C){
        if(this.percent < C.percent){
            return 1;
        } else if(this.percent > C.percent){
            return -1;
        } else {
            return 0;
        }
    }
    
    
}

/*
 *  Store a word
 */
class WordMatch {
    public String word; // normal word
    public String wordEdited; // word as lower case, no punctuation
    public boolean matched; // whether this was matched to a word in this web result
    public WordMatch(String word, String wordEdited) {
        this.matched = false;
        this.word = word;
        this.wordEdited = wordEdited;
    }
    
    /*
     *  Returns a WordMatch Arraylist from a string.
     */
    public static ArrayList<WordMatch> createWordMatchList (String text) {
        String[] nonEditedWords = text.split(" ");
        ArrayList<WordMatch> wordMatches = new ArrayList<>();
        for (String str : nonEditedWords) {
            String strEdited = str.replaceAll(ResultCompare.punctuation.toString(), "").toLowerCase().trim();
            wordMatches.add(new WordMatch(str, strEdited));
        }
        return wordMatches;
    }
    
    /*
     *  Sets all words in a WordMatch array list to the matched value of bool.
     */
    public static void setMatches(ArrayList<WordMatch> wordMatches, boolean bool) {
        for (int i = 0; i < wordMatches.size(); i++) {
            wordMatches.get(i).matched = bool;
        }
    }
}