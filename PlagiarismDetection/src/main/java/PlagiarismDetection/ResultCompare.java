/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import java.util.regex.Pattern;


public class ResultCompare implements Comparable<ResultCompare>{
    public String title;
    public String snippet;
    public double percent;
    public String url;
    public String language;
    public String webpage; // the full webpage
    public boolean lookedThroughWebpage;
    int[] trueComparedIndices;
    
    ResultCompare(String title, String snippet, String url, String language){
        this.snippet = snippet;
        this.title = title;
        this.url = url;
        this.language = language;
        this.percent = 0;
        this.lookedThroughWebpage = false;
    }
    
    public double calcPercent(String content, String comparativeContent){
        double percentContent = 0;
        System.out.println("called calcPercent()...");
        // If content or comparativeContent can be found inside the other.
        if (content.contains(comparativeContent) || comparativeContent.contains(content)) {
            percent = 100;
            return 100;
        }
        Pattern punctuation = Pattern.compile("(,|\\.|\\!|:|;|\\?|\\\\|\\/|\\(|\\)|\\[|\\]|'|\"|@|\\+|\\-|_|=|\\*|©|\\{|\\})"); // matches any punctuation
        String contentLC; // content in lower case and without punctuation
        String compContentLC; // comparative content in lower case and without punctuation
//        if (language.matches("English")) { // only take away punctuation if the language is English
            contentLC = content.replaceAll(punctuation.toString(), "").toLowerCase();
            compContentLC = comparativeContent.replaceAll(punctuation.toString(), "").toLowerCase();
//        } else { // any other language but English, just convert all characters to lowerCase.
//            contentLC = content;//.toLowerCase();
//            compContentLC = comparativeContent;//.toLowerCase();
//        }
        String[] contentWords = contentLC.trim().split(" ");
        String[] compContentWords = compContentLC.trim().split(" ");
        // Now with both contents having been filtered for punctuation and converted to lowercase,
        // Check again if one contains the other:
        if (contentLC.contains(compContentLC) || compContentLC.contains(contentLC)) {
            percent = 100;
            return 100;
        }
        int numberOfMatches = 0;
        // Look for compContentWords in contentWords
        trueComparedIndices = new int[compContentWords.length];
        try {
            for (int i = 0; i < compContentWords.length; i++) {
                for (int j = 0; j < contentWords.length; j++) {
                    int count = 0; // number of words matched in a row
                    int i2 = i, j2 = j;
//                    System.out.println(i2 + ": " + compContentWords[i2] + " == " + contentWords[j2] + " :" + j2);
                    while (compContentWords[i2].matches(contentWords[j2])) {
//                        System.out.println(i2 + ": " + compContentWords[i2] + " == " + contentWords[j2] + " :" + j2);
                        // Keep looping in here while next words match each other.
                        count++; // count the number of matched words
                        i2++;
                        j2++;
                        if (i2 >= compContentWords.length || j2 >= contentWords.length) // if either index went past array bounds
                            break;
                    }
                    // if the number of matched words in a row was 3 or more, then mark those words as matched in trueComparedIndices.
                    if (count >= 3) {
//                        System.out.println(count + " words found in a row.\nThese words were found: ");
                        for (int k = 0; k < count; k++) {
                            trueComparedIndices[i+k] = 1;
//                            System.out.print(compContentWords[i+k] + " ");
                        }
//                        System.out.println();
                        i += count-1; // increment i by the number of matched words, because there is no longer any need to compare them again
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } 
        for (int n : trueComparedIndices) {
            if (n == 1) {
                numberOfMatches++;
            }
        }
        percentContent = Math.round(((double)numberOfMatches / (double)trueComparedIndices.length) * 100);
        System.out.println("Percentage of content found: " + percentContent);
        
        percent = percentContent;
        return percentContent;
    }
    
    @Override
    public String toString(){
        String temp = title + "\n" + snippet + "\n" + url + "\n";
        if (lookedThroughWebpage) {
            temp += "Looked through whole webpage.";
        } else {
            temp += "Only looked through snippet.";
        }
        return temp;
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
