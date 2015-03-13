package gov.nih.nci.cbiit.scimgmt.entmaint.exceptions;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class HibernateDAOException extends RuntimeException {

    protected final Logger logger = Logger.getLogger(HibernateDAOException.class);
    
	public HibernateDAOException() {}

	public HibernateDAOException(String message) {
		super(message);
		logger.error(" An application exception has ocurred during Hibernate Data Access!!! " + message);
	}
        
    public HibernateDAOException(Exception e) {
            super(e);
            logger.error(" An application exception has ocurred during Hibernate Data Access!!! " + e);
    } 
    
    public HibernateDAOException(String message, Exception e) {
            super(message,e);
            logger.error(" An application exception has ocurred during Hibernate Data Access!!! " + e);
    }   
}
