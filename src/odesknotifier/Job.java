/*
 *
 *
 */
package odesknotifier;

/**
 *
 * @author parkee
 */
public class Job {
    private String title;
    private String description;
    private String jobType;
    private String amount; // Let it be string... anyway no one cares, yeah?

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
}
