package gov.nih.nci.cbiit.scimgmt.entmaint.actions;
      
import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
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
	
    public String execute() throws Exception {
        String forward = SUCCESS;
        
        //Retrieve current audit info
        
        EmAuditsVO emAuditsVO = adminService.retrieveCurrentAudit();
        
        //Store this in the session
        setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO);             

        return forward;
    }
}
