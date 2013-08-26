/*
 * 
 * 
 */
package odesknotifier;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author parkee
 */
public class JobExtractor {

    private Document jobs;
    private XPathExpression xFirstJobDate;
    private XPathExpression xFirstJobTime;
    private Date latestJobTime;

    public JobExtractor(String jobs) throws ParserConfigurationException,
            SAXException, IOException,
            XPathExpressionException, ParseException {
        this.jobs = prepareDocument(jobs);
        prepareXPath();
        getLatestJobTime();
    }

    public Document prepareDocument(String jobs) throws ParserConfigurationException,
            SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(jobs));
        return this.jobs = builder.parse(is);
    }

    private void prepareXPath() throws XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        this.xFirstJobDate = xpath.compile("//jobs/job[1]/op_date_created/text()");
        this.xFirstJobTime = xpath.compile("//jobs/job[1]/op_time_created/text()");
    }

    private void getLatestJobTime() throws XPathExpressionException, ParseException {
        String firstJobDate = (String) this.xFirstJobDate.evaluate(this.jobs, 
                                                                XPathConstants.STRING);
        String firstJobTime = (String) this.xFirstJobTime.evaluate(this.jobs, 
                                                                XPathConstants.STRING);
        
        this.latestJobTime = evaluateDate(firstJobTime, firstJobDate);
    }
    
    private Date evaluateDate(String time, String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy HH:mm:ss", Locale.US);
        return sdf.parse(date + " " + time);
    }
    
    public LinkedList<Job> getNewJobs() throws XPathExpressionException, ParseException {
        NodeList jobs = this.jobs.getElementsByTagName("job");
        Date newDate = new Date(0L);
        LinkedList<Job> newJobs = new LinkedList<>();
        
        for (int i = 0; i < jobs.getLength(); i++) {
            Element theJob = null;
            
            if (jobs.item(i).getNodeType() == Node.ELEMENT_NODE) {
                theJob = (Element) jobs.item(i);
                String title = theJob.getElementsByTagName("op_title").item(0).getTextContent();
                String description = theJob.getElementsByTagName("op_description").item(0).getTextContent();
                String timeCreated = theJob.getElementsByTagName("op_time_created").item(0).getTextContent();
                String jobType = theJob.getElementsByTagName("job_type").item(0).getTextContent();
                String dateCreated = theJob.getElementsByTagName("op_date_created").item(0).getTextContent();
                String amount = theJob.getElementsByTagName("amount").item(0).getTextContent();
                
                if (i == 0) {
                    newDate = evaluateDate(timeCreated, dateCreated);
                    //Date of latest job in the xml. The variable's supposed to help me update latest job time later
                }
                
                if (evaluateDate(timeCreated, dateCreated).after(this.latestJobTime)) {
                    Job curJob = new Job();
                    curJob.setAmount(amount);
                    curJob.setDescription(description);
                    curJob.setJobType(jobType);
                    curJob.setTitle(title);
                    newJobs.add(curJob);
                } else {
                    break; // !BREAK! away from everybody\n !BREAK! away from everything ⓒ Three Days Grace ☺
                }
            
            }
        }
        this.latestJobTime = newDate;
        return newJobs;
    }
}
