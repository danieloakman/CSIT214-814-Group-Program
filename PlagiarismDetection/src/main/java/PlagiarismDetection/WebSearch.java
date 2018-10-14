/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import java.net.*;
import java.util.*;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WebSearch {
    static String subscriptionKey = "cd19e4797ef14c9984899ea5e09bf2b6";
    static String host = "https://api.cognitive.microsoft.com";
    static String path = "/bing/v7.0/search";
    static String searchTerm;
    static String results = "";
    
    public static WebResults Search(String term) throws Exception {
        URL url = new URL(host + path + "?q=" +  URLEncoder.encode(term, "UTF-8"));
        
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
        
        InputStream stream = connection.getInputStream();
        String response = new Scanner(stream).useDelimiter("\\A").next();
        WebResults results = new WebResults(new HashMap<String, String>(), response);
        
        Map<String, List<String>> headers = connection.getHeaderFields();
        
        for (String header : headers.keySet()) {
            if (header == null) continue;
            if (header.startsWith("BingAPIs-") || header.startsWith("X-MSEdge-")){
                results.relavantHeaders.put(header, headers.get(header).get(0));
            }
        }
        
        stream.close();
        return results;
    }
    
    public static String parse(String json_txt){
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_txt).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }
    
    public static void start(String searchTxt){
        if (subscriptionKey.length() != 32) {
            System.out.println("Invalid Bing Search API subscription key!");
            System.out.println("Please paste yours into the source code.");
            System.exit(1);
        }
        
        try{
            WebResults result = Search(searchTxt);
            
            for(String header : result.relavantHeaders.keySet()){
                results = results.concat(header + ": " + result.relavantHeaders.get(header));
                results = results.concat(parse(result.jsonResponse));
            }
        }
        catch(Exception e){
            e.printStackTrace(System.out);
            System.exit(1);
        }
    }
}
