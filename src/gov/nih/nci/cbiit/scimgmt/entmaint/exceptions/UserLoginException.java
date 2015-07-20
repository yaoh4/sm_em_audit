package gov.nih.nci.cbiit.scimgmt.entmaint.exceptions;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class UserLoginException extends RuntimeException {

	  protected final Logger logger = Logger.getLogger(UserLoginException.class);

    public UserLoginException() {
    }

    public UserLoginException(String className, String methodName, 
                              String message) {
        super(message);
        String errorMessage = formatMessage(className, methodName, message);
        logger.error(errorMessage);
    }

    private String formatMessage(String className, String methodName, String message) {
        String errorMessage = 
            new StringBuffer(" An exception has occurred during authetication/authorization process  ")
                .append(className)
                .append("."+methodName)
                .append("<BR>")
                .append("Message: ")
                .append(message)
                .toString();
        return errorMessage;

    }

}
