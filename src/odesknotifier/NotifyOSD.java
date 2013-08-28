/*
 * 
 * 
 */
package odesknotifier;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parkee
 */
public class NotifyOSD {
    private String command = "notify-send";
    private Integer expire = 10000;
    private String image = "";
    
    public NotifyOSD() {}
    
    public NotifyOSD(String image) {
        this.image = image;
    }
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public void runNotification(String message) {
        String[] cmd = { this.command,
                 "-t",
                 this.expire.toString(),
                 "-i",
                 this.image,
                 message};
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            Logger.getLogger(NotifyOSD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
