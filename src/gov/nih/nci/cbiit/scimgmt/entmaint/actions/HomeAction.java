package gov.nih.nci.cbiit.scimgmt.entmaint.actions;
      
import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Action class for initial setup.
 * 
 * @author menons2
 *
 */
@SuppressWarnings("serial")
public class HomeAction extends BaseAction {

   
	static Logger logger = Logger.getLogger(HomeAction.class);
	
	@Autowired
	AdminService adminService;
	
	/**
	 * Home action. Invoked when user logs into the application..
	 * 
	 * @return forward string
	 */
	public String execute() throws Exception {
        String forward = SUCCESS;
        
        //Retrieve current audit info
        
        EmAuditsVO emAuditsVO = adminService.retrieveCurrentOrLastAuditVO();
        if(emAuditsVO != null) {
        	logger.debug("Retrieved auditInfo for audit " + emAuditsVO.getId());
        } else {
        	logger.debug("No audit info available for current audit");
        }
        
        //Store this in the session
        setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO);             
  
        return forward;
	}
	
	
	/**
	 * Forwards to the Audit or Portfolio tab depending
	 * on whether the audit is enabled or not.
	 * 
	 * @return forward string indicating whether audit is enabled or not.
	 * @throws Exception
	 */
    public String search() throws Exception {
        
        if(EmAppUtil.isAuditEnabled()) {
        	if(nciUser.getCurrentUserRole().equalsIgnoreCase("EMADMIN")) {
        		return ApplicationConstants.AUDIT_ENABLED;
        	}
        	if (EmAppUtil.isCategoryAvailable(ApplicationConstants.CATEGORY_ACTIVE))
        		return ApplicationConstants.SUB_TAB_ACTIVE_ACCOUNTS;
        	if (EmAppUtil.isCategoryAvailable(ApplicationConstants.CATEGORY_NEW))
        		return ApplicationConstants.SUB_TAB_NEW_ACCOUNTS;
        	if (EmAppUtil.isCategoryAvailable(ApplicationConstants.CATEGORY_DELETED))
        		return ApplicationConstants.SUB_TAB_DELETED_ACCOUNTS;
        	if (EmAppUtil.isCategoryAvailable(ApplicationConstants.CATEGORY_INACTIVE))
        		return ApplicationConstants.SUB_TAB_INACTIVE_ACCOUNTS;
        }
        return ApplicationConstants.AUDIT_DISABLED;

    }
    
	/**
	 * Forwards to the Audit or Portfolio tab depending
	 * on whether the audit is enabled or not.
	 * 
	 * @return forward string indicating whether audit is enabled or not.
	 * @throws Exception
	 */
    public String searchI2e() throws Exception {
        
        if(EmAppUtil.isI2eAuditEnabled()) {
        	return ApplicationConstants.AUDIT_ENABLED;
        	
        } else {
        	return ApplicationConstants.AUDIT_DISABLED;
        }

    }
}
