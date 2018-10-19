/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import java.util.*;
//Data structure for WebSearch's response from Bing Web Search
public class WebResults {
    HashMap<String, String> relavantHeaders;
    String jsonResponse;
    WebResults(HashMap<String, String> headers, String json){
        relavantHeaders = headers;
        jsonResponse = json;
    }
}
