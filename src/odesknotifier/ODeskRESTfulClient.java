/*
 * Search Jobs oDesk docs: http://developers.odesk.com/w/page/12364012/search%20jobs
 * List of admissible categories and subcategories: http://developers.odesk.com/w/page/12363984/all%20categories
 * 
 */
package odesknotifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author parkee
 */
public class ODeskRESTfulClient {
    private final String baseURL = "https://www.odesk.com/api/profiles/v1/search/jobs.";
    private String category;
    private HashSet<String> subcategories;
    private ResponseFormat responseFormat;
    
    public ODeskRESTfulClient() {
        this(ResponseFormat.JSON);
    }
    
    public ODeskRESTfulClient(ResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setSubcategory(Collection<String> subcategory) {
        this.subcategories = new HashSet<>(subcategory);
    }
    
    private URL buildURL() throws MalformedURLException {
        StringBuilder url = new StringBuilder(baseURL);
        url.append(this.responseFormat.toString());
        url.append("?");
        if(this.category != null) {
            url.append("c1=");
            url.append(category);
            
            if(this.subcategories != null) {
                url.append("&");
                
                for (String subcategory : this.subcategories) {
                    url.append("c2[]=" + subcategory + "&");
                }
            }
        }
        return new URL(url.toString());
    }
    
    public String getContent() throws MalformedURLException, IOException, Exception {
        URL url;
        url = buildURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        if (conn.getResponseCode() != 200) {
            throw new Exception("HTTP Error. Code: " + 
                    ((Integer) conn.getResponseCode()).toString());
        }
        
        BufferedReader responseReader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
                );
        StringBuilder responseBody = new StringBuilder();
        String responseLine;
        while ((responseLine = responseReader.readLine()) != null ) {
            responseBody.append(responseLine);
        }
        conn.disconnect();
        return responseBody.toString();
    }
    
    public enum ResponseFormat {
        XML,
        JSON
    }
}
