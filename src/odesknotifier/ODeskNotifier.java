/*
 * 
 * 
 */
package odesknotifier;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 *
 * @author parkee
 */
public class ODeskNotifier extends TimerTask {
    private ODeskRESTfulClient client;
    private NotifyOSD notification;
    private JobExtractor jobExtractor;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ODeskNotifier oDeskNotifier = new ODeskNotifier();
        try {
            oDeskNotifier.setup();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Timer().scheduleAtFixedRate(oDeskNotifier, 0, 60000);
    }
    
    @Override
    public void run() {
        try {
            LinkedList<Job> nJobs;
            this.jobExtractor.prepareDocument(client.getContent());
            nJobs = this.jobExtractor.getNewJobs();

            Iterator i = nJobs.listIterator();

            while (i.hasNext()) {
                Job currentJob = (Job) i.next();
                this.notification.runNotification(currentJob.getTitle() + " " 
                        + currentJob.getAmount());
            }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ODeskNotifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setup() throws ParserConfigurationException, 
                                SAXException,
                                IOException,
                                XPathExpressionException,
                                MalformedURLException,
                                ParseException,
                                Exception {
        //Setup restful client
        this.client = new ODeskRESTfulClient(ODeskRESTfulClient.ResponseFormat.XML);
        this.client.setCategory("Software Development");
        HashSet<String> subcategories = new HashSet<>();
        subcategories.add("Desktop Applications");
        subcategories.add("Scripts & Utilities");
        subcategories.add("Other - Software Development");
        this.client.setSubcategory(subcategories);
        
        //Setup notifications
        this.notification = new NotifyOSD("/usr/share/pixmaps/odeskteam.png");
        
        //Setup jobExtractor
        this.jobExtractor = new JobExtractor(this.client.getContent());
    }
}
