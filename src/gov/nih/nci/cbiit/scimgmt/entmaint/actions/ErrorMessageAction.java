package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Action class to display application error message with the capability 
 * for the user to report the error through the 'send email' button. This 
 * code has been reused from other I2E applications.
 * 
 * @author menons2
 *
 */
public class ErrorMessageAction extends BaseAction {
    
	
	private String message;
    
	private String errorDetails;
    
	@Autowired
	private EntMaintProperties properties;
	
    
	Logger logger = Logger.getLogger(getClass());

   
    public String execute() throws Exception {
       
    	logger.error(errorDetails);
    	
    	String to = properties.getProperty("ERROR_EMAIL");
        String environment  = properties.getProperty("ENVIRONMENT");
        String envString = environment.equalsIgnoreCase("Production") ? "" : "["+environment.toUpperCase()+"]";       
        String from = nciUser.getEmail();
        
        String  errorTime =  DateFormat.getInstance().format(new Date());
        
        //Set error email body
        StringBuffer sb = new StringBuffer("<PRE>");
        sb.append("Error occurred in Enterprise Maintenance ").append(envString).append(" ] Application at  ").append(errorTime).append("\n\n");
        sb.append("\n User: ").append(nciUser.getFullName()); 
        sb.append("\n\n User Typed Message: ").append(this.getMessage());
        sb.append("\n\n Browser: ").append(request.getHeader("User-Agent")).append("\n\n Host:").append(request.getRemoteHost());
        sb.append("\n <h4>ERROR DETAILS :</h4>");
        sb.append("\n-----------------------------------------------------------\n");
        sb.append(this.getErrorDetails());
        sb.append("</PRE>");
        
        // Create properties, get Session
        Properties mailProperties = new Properties();
        
        // SUBSTITUTE YOUR ISP'S MAIL SERVER HERE!!!
        String host = properties.getProperty("SMTP_SERVER");
        
        // If using static Transport.send(),
        // need to specify which host to send it to
        mailProperties.put("mail.smtp.host", host);
        
        Session mailSession = Session.getInstance(mailProperties);
        
        try {
        	
            // Instantiate a message
            Message msg = new MimeMessage(mailSession);

            //Set message attributes
            
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = null;
            if ( to != null){
                String[] addresses  =  to.split(";");
                address =  new InternetAddress[addresses.length];
                for (int i=0; i < addresses.length;i++){
                    address[i] =  new InternetAddress(addresses[i]) ;
                }
            }
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSentDate(new Date());
            msg.setSubject("Enterprise Maintenance (Audit Module)  " + envString + "Application Error  at " + errorTime);
            msg.setContent(sb.toString(),"text/html; charset=UTF-8");

            //Send the message
            Transport.send(msg);
        } catch (Throwable e) {
            logger.error("Error occurred emailing error message from " + from, e);
        }
        return SUCCESS;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    
    public String getMessage() {
        return message;
    }

    
    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    
    public String getErrorDetails() {
        return errorDetails;
    }

}
