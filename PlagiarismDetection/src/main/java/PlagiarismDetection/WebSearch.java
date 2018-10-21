/*
 *  CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import java.net.*;
import java.util.*;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

public class WebSearch {
    static String subscriptionKey = "cd19e4797ef14c9984899ea5e09bf2b6";
    static String host = "https://api.cognitive.microsoft.com";
    static String path = "/bing/v7.0/search";
    static String searchTerm;
    static String language = "en";
    static String results = "";
    static ArrayList<String> santitizedText;
    
    // opens a connection to Bing Web Search and gets a response
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
    
    // parses json string response into Json objects, then gets the relevant strings from them to return to start
    public static ArrayList<String> parse(String json_txt){
        System.out.println(json_txt);
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_txt).getAsJsonObject();
        ArrayList<String> returning = new ArrayList<>();
        JsonObject wtf = json.getAsJsonObject("webPages");
        JsonArray array = new JsonArray();
        try {
            array = wtf.getAsJsonArray("value");
        } catch (Exception ex) {
            System.out.println("No search results found");
//            System.out.println(ex);
        }
        for(int i = 0; i < array.size(); i++){
            returning.add(array.get(i).getAsJsonObject().get("name").getAsString());
            returning.add(array.get(i).getAsJsonObject().get("snippet").getAsString());
            returning.add(array.get(i).getAsJsonObject().get("url").getAsString());
        }
        return returning;
    }
    
    // Landing function for the WebSearch.
    public static void start(String searchTxt){
        if (subscriptionKey.length() != 32) {
            System.out.println("Invalid Bing Search API subscription key!");
            System.out.println("Please paste yours into the source code.");
            System.exit(1);
        }
        try{
            WebResults result = Search(searchTxt);
            santitizedText = parse(result.jsonResponse);
        }
        catch(Exception e){
            e.printStackTrace(System.out);
            System.exit(1);
        }
    }
}
