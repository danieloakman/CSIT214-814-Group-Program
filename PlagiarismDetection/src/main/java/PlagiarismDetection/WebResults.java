/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import java.util.*;

public class WebResults {
    HashMap<String, String> relavantHeaders;
    String jsonResponse;
    WebResults(HashMap<String, String> headers, String json){
        relavantHeaders = headers;
        jsonResponse = json;
    }
}
