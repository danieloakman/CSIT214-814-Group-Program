/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;


public class ResultCompare implements Comparable<ResultCompare>{
    String title;
    String content;
    double percent;
    
    ResultCompare(String title, String content, String compContent){
        this.content = content;
        this.title = title;
        
        this.percent =  calcPercent(content, compContent);
        
    }
    
    /*
    The algorithm below was translated from a C# algorithm that can be found at
    https://stackoverflow.com/questions/16840503/how-to-compare-2-strings-and-find-the-difference-in-percentage
    */
    
    static double calcPercent(String content, String comparativeContent){
        double percentContent;

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
        return title + "\n" + content;
    
    }
    
    @Override
    public int compareTo(ResultCompare C){
        if(this.percent > C.percent){
            return 1;
        } else if(this.percent < C.percent){
            return -1;
        } else {
            return 0;
        }
    }
}
