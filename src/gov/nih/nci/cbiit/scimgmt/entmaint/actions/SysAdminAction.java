package gov.nih.nci.cbiit.scimgmt.entmaint.actions;



import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.ApplicationService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LdapServices;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppInitializer;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import org.apache.log4j.Logger;
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
    private LdapServices ldapServices; 
	@Autowired
    private ApplicationService applicationService;
	@Autowired
	private NciUser nciUser;
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
   // http://localhost/entmaint/ChangeUser.action?user=CHANGD3
   
   
    /**
     * Change user functionality support only for NON production environment
     * 
     * @return
     * @throws Exception
     */
    public String changeUser() throws Exception {
        
        String forward = SUCCESS;
        
        if (nciUser == null ||
        	(!entMaintProperties.getPropertyValue("ENVIRONMENT").contains("Development") &&
        	!entMaintProperties.getPropertyValue("ENVIRONMENT").equalsIgnoreCase("Test") &&
        	!entMaintProperties.getPropertyValue("ENVIRONMENT").equalsIgnoreCase("Stage"))) {
        	return forward;
        }
        
        nciUser = ldapServices.verifyNciUserWithRole(user);
        session.put(ApplicationConstants.SESSION_USER, nciUser);
        return forward;
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

}
