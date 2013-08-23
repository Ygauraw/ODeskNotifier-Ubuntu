/*
 * 
 * 
 */
package odesknotifier;

import java.util.HashSet;

/**
 *
 * @author parkee
 */
public class ODeskNotifier {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        ODeskRESTfulClient client = new ODeskRESTfulClient();
        client.setCategory("Software Development");
        HashSet<String> subcategories = new HashSet<>();
        subcategories.add("Desktop Applications");
        subcategories.add("Scripts & Utilities");
        subcategories.add("Other - Software Development");
        client.setSubcategory(subcategories);
        String output = client.getContent();
        System.out.print(output);
    }
}
