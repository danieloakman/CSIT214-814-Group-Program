/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;


public class ResultCompare {
    String title;
    String content;
    double percent;
    
    ResultCompare(String title, String content){
        this.content = content;
        this.title = title;
        
        double titlePercent = calcPercent(title);
        double contentPercent = calcPercent(content);
        
        this.percent = (titlePercent + contentPercent) / 2;
    }
    
    /*
    The algorithm below was translated from a C# algorithm that can be found at
    https://stackoverflow.com/questions/16840503/how-to-compare-2-strings-and-find-the-difference-in-percentage
    */
    
    static double calcPercent(String content){
        double percentContent = 0;
        String comparativeContent = "";

        if(content.equals(comparativeContent)){
            return 100;
        }
        
        if(content.length() == 0 || comparativeContent.length() == 0){
            return 0;
        }
        
        double maxLen = content.length() > comparativeContent.length() ? content.length() : comparativeContent.length();
        int minLen = content.length() < comparativeContent.length() ? content.length() : comparativeContent.length();
        int sameCharLength = 0;
        
        for(int i = 0; i < minLen; i++){
            if(content.charAt(i) == comparativeContent.charAt(i)){
                sameCharLength++;
            }
        }
        
        percentContent = sameCharLength / maxLen * 100;
        
        return percentContent;
    }
    
    @Override
    public String toString(){
        return title + "        " + percent + "\n" + content;
    
    }
}
