package gov.nih.nci.cbiit.scimgmt.entmaint.exceptions;

import org.apache.log4j.Logger;

/**
 * This exception is used to mark business rule violations.
 *
 * @author menons2
 * 
 */
public class ServiceDeniedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(ServiceDeniedException.class);
	
	public ServiceDeniedException() {
		super("You do not have permission to perform the requested operation.");
		
	}

	public ServiceDeniedException(String message) {
		super(message);		
	    //session.setAttribute(ApplicationConstants.ERROR_MESSAGE, message);
		//logger.error(" An exception has occurred \n" + message);
	}
	
}