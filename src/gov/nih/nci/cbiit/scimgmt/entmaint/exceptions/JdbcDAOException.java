package gov.nih.nci.cbiit.scimgmt.entmaint.exceptions;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class JdbcDAOException extends RuntimeException {

    protected final Logger logger = Logger.getLogger(JdbcDAOException.class);
	
    public JdbcDAOException() {}

	public JdbcDAOException(String message) {
		super(message);
		logger.error(" An application exception has ocurred during JDBC Data Access!!! " + message);
	}
}
