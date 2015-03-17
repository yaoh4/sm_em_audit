/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditHistoryVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;

import org.apache.log4j.Logger;
import org.displaytag.decorator.TableDecorator;


/**
 * @author menons2
 *
 */
public class AuditHistoryTableDecorator extends TableDecorator {

    
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
		LookupService lookupService  = (LookupService)this.getPageContext().getServletContext().getAttribute("lookupService");
		AppLookupT appLookupT = 
			lookupService.getAppLookupByCode(
				ApplicationConstants.AUDIT_STATES_LOOKUP_KEY, currentRowObject.getActionCode());
		String description = appLookupT.getDescription();
		String actionStr = description + " by " + currentRowObject.getCreateUserFullName();
		
		return actionStr;
	}


}
