package gov.nih.nci.cbiit.scimgmt.entmaint.actions;
      
import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;



@SuppressWarnings("serial")
public class HomeAction extends BaseAction {

    /*
     * (non-Javadoc)
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
	
	static Logger logger = Logger.getLogger(HomeAction.class);
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	LookupService lookupService;
	
	/**
	 * Home action.
	 * 
	 * @return forward string
	 */
	public String execute() throws Exception {
        String forward = SUCCESS;
        
        //Retrieve current audit info
        
        EmAuditsVO emAuditsVO = adminService.retrieveCurrentAuditVO();
        
        //Store this in the session
        setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO);             

        // Temporary putting here so roles will load into cache.
        lookupService.getList(ApplicationConstants.ERA_ROLES_LIST);
        
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
        	return ApplicationConstants.AUDIT_ENABLED;
        	
        } else {
        	return ApplicationConstants.AUDIT_DISABLED;
        }

    }
}
