/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
