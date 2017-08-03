package gov.nih.nci.cbiit.scimgmt.entmaint.actions;



import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.exceptions.UserLoginException;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.UserRoleService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppInitializer;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * EM System Administration Utility
 */
public class SysAdminAction extends BaseAction {

   
    /**
     * SYS Admin Action 
     * See Constants below
     */
    private String task;
    private String user;
    
   	@Autowired
    private UserRoleService userRoleService;
	@Autowired
	private EntMaintProperties entMaintProperties;
	@Autowired
	private EmAppInitializer emAppInitializer;
	
    static Logger logger = Logger.getLogger(SysAdminAction.class);
    
    private static final String ADMIN_TASK_RELOAD_PROPERTIES="RELOAD_PROPERTIES";
    private static final String ADMIN_TASK_REFRESH_LISTS="REFRESH_LISTS";
 
   // URL examples
   // http://localhost/entmaint/SysAdmin.action?task=RELOAD_PROPERTIES
   // http://localhost/entmaint/SysAdmin.action?task=REFRESH_LISTS
   // http://localhost/entmaint/changeUser.action?user=CHANGD3
   
   
    /**
     * Change user functionality support for all tiers
     * 
     * @return
     * @throws Exception
     */
    public String changeUser() throws Exception {
        
        String forward = SUCCESS;
        
    	// By the time it invokes this method, user is validated in the interceptor.
        return forward;
    }
    
    
    /**
     * This method restores to the logged in users profile
     * and navigates to the home page
     * 
     * @return String
     */
    public String restoreUser() {
    	
    	NciUser remote_user = (NciUser) session.get(ApplicationConstants.REMOTE_USER);

    	//Set the Remote User back and populate the roles
    	BeanUtils.copyProperties(remote_user, nciUser);
        
    	return SUCCESS;
    }
    
    /* (non-Javadoc)
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    public String execute() throws Exception {
        
        String  log="";
        log="System Administration Task  "+getTask()+" Initiated by "+nciUser.getUserId();
        logger.info(log);
                
        if (nciUser== null){
        	String error = "Invalid Access";
        	throw new Exception(error);
        }
        
		verifyUser();
        
        if (ADMIN_TASK_RELOAD_PROPERTIES.equals(task)){
            logger.info("Task: Reload EM properties Initiated by :"+nciUser.getUserId());
            reloadProperties();
            logger.info("Task: Reload EM properties Completed");

        }
        
        if (ADMIN_TASK_REFRESH_LISTS.equals(task)){
            logger.info("Task: Refresh Lists Initiated by :"+nciUser.getUserId());
            refreshLists();
            logger.info("Task: Refresh Lists Completed");

        }
        
        return SUCCESS;       
    }
    
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
  
    public void setTask(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }
    
    /**
     * Calls property init
     */
    private void reloadProperties(){
    	logger.info("Initiating Reload properties...");
        entMaintProperties.init();
        logger.info("Reload properties completed");
    }
    
    /**
     * Calls initializer bean reinit
     */
    private void refreshLists(){
    	logger.info("Initiating Refresh lists...");
    	emAppInitializer.reinit();
        logger.info("Refresh lists completed");
    }
    
    /**
     * Only Sys Admin users can execute SysAdmin Actions.
     * @throws Exception
     */
    private void  verifyUser()throws Exception{
        logger.info("Authenticating the User "+nciUser.getUserId()+" for Sys Administration Task");
        String  adminUsers= entMaintProperties.getPropertyValue("SYS_ADMIN");
        String[] sysAdminUsers  = adminUsers.split(",");
        boolean validAdminUser =  false;
        for (int i=0;i <sysAdminUsers.length;i++){
            if (nciUser.getUserId().equalsIgnoreCase(sysAdminUsers[i])){
                validAdminUser =  true;
                nciUser.setCurrentUserRole("SYS_ADMIN");
            }
        }
        if (!validAdminUser){
            String error =nciUser.getUserId() + " is NOT a valid user to perform System Administration Task";
            logger.error(error);
            throw new Exception(error);
        }
    } 
    
    /**
     * Populate User Roles
     * @param nciUser
     */
    public void populateNCIUserRoles( NciUser nciUser){
    	// Load user EM roles
    	userRoleService.loadPersonInfo(nciUser);

    	// Give IC coordinator role to application developers
    	// Changed to allow production environment APP_DEVELOPER role for validation
    	if (entMaintProperties.getPropertyValue("ENVIRONMENT").contains("Development") ||
    			entMaintProperties.getPropertyValue("ENVIRONMENT").equalsIgnoreCase("Test") ||
    			entMaintProperties.getPropertyValue("ENVIRONMENT").equalsIgnoreCase("Stage") ||
    			entMaintProperties.getPropertyValue("ENVIRONMENT").equalsIgnoreCase("Production")) {
    		String  devUsers= entMaintProperties.getPropertyValue("APP_DEVELOPER");
    		if(devUsers != null) {
    			String[] appDevUsers = devUsers.split(",");
    			for (int i = 0; i < appDevUsers.length; i++) {
    				if (nciUser.getUserId().equalsIgnoreCase(appDevUsers[i])) {
    					nciUser.setCurrentUserRole("EMREP");
    				}
    			}
    		}
    	}
    }

    /**
     * Defines the user authorization for this action.
     * 
     * @param nu NciUser to be verified
     * 
     * @return boolean true if the user has access, else false
     * @throws gov.nih.nci.cbiit.oracle.entmaint.exceptions.UserLoginException
     */
    private boolean verifyAuthorization(NciUser nciUser) throws UserLoginException {
        logger.debug("In the verifyAuthorization method with user: " + 
                     nciUser);
		if (nciUser.getCurrentUserRole() == null
				|| (!nciUser.getCurrentUserRole().equalsIgnoreCase(
						ApplicationConstants.USER_ROLE_IC_COORDINATOR) && !nciUser.getCurrentUserRole()
						.equalsIgnoreCase(ApplicationConstants.USER_ROLE_SUPER_USER))) {
			return false;
		}
        return true;
    }

}
