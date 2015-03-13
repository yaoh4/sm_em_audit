/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditHistoryVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;

import org.apache.log4j.Logger;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author menons2
 *
 */

public class AuditHistoryTableDecorator extends TableDecorator {

	private NciUser nciUser;
	
	 private static Logger logger = 
		        Logger.getLogger(AuditHistoryTableDecorator.class);

	 /**
	  * Default Constructor
	  */
	 public AuditHistoryTableDecorator() {
		        super();
	 }
	
	 
	 public String getActionCode() {
		
		EmAuditHistoryVw currentRowObject = 
	            (EmAuditHistoryVw)getCurrentRowObject();
		
		String actionStr = currentRowObject.getActionCode() + " by "
				+ currentRowObject.getCreateUserFullName();
		
		return actionStr;
	}


	/**
	 * @return the nciUser
	 */
	public NciUser getNciUser() {
		return nciUser;
	}


	/**
	 * @param nciUser the nciUser to set
	 */
	public void setNciUser(NciUser nciUser) {
		this.nciUser = nciUser;
	}
}
