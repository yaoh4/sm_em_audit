package gov.nih.nci.cbiit.scimgmt.entmaint.interceptors;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.exceptions.UserLoginException;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.UserRoleService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;



@SuppressWarnings("serial")
public class UserInfoInterceptor extends AbstractInterceptor implements StrutsStatics  {
	
	@Autowired
    private UserRoleService userRoleService; 	
	@Autowired
    private EntMaintProperties entMaintProperties;	
	@Autowired
	private NciUser nciUser;
	
    public static Logger logger = Logger.getLogger(UserInfoInterceptor.class);

    public UserInfoInterceptor() {
    }

    @SuppressWarnings("unchecked")
    public String intercept(ActionInvocation invocation) throws Exception {
        // Get the action context from the invocation so we can access the
        // HttpServletRequest and HttpSession objects.
        final ActionContext context = invocation.getInvocationContext();
        Map session = context.getSession();
        HttpServletRequest request = 
            (HttpServletRequest)context.get(HTTP_REQUEST);
        
        Object action = invocation.getAction();
        logger.debug("Inside User Interceptor.intercept");   

    	//Check if this is a change user action
        String changeUser = request.getParameter("user");
        
       //Check if this is a sys admin action
        String sysAdminAction = request.getParameter("task");
        
        if ((nciUser == null || nciUser.getUserId() == null) && StringUtils.isEmpty(changeUser) && StringUtils.isEmpty(sysAdminAction)) {               
            // get the User header from Site Minder
            String remoteUser = request.getHeader("SM_USER");
            logger.info("User login from Site Minder SM_USER = "+remoteUser);
             
            // when deployed locally and authenticated from apache,
            // check the user from remote user  or apache Authorization header
            if (StringUtils.isEmpty(remoteUser)) {
                 remoteUser = request.getRemoteUser();
            }
            if (StringUtils.isEmpty(remoteUser)) {
                String authUser = request.getHeader("Authorization");
                
                if (StringUtils.isNotBlank(authUser)) {
                        Base64 decoder = new Base64();
                		authUser = new String(decoder.decode(authUser.substring(6).getBytes()));
                        remoteUser = authUser.substring(0, authUser.indexOf(":"));
               }
            }            
          
            //Throw an exception if the user is not found in the user 
            if (StringUtils.isNotEmpty(remoteUser)) {

            	NciUser newNciUser = userRoleService.getNCIUser(remoteUser);  

            	String accessError = "User "+ remoteUser +" is not authorized to access Enterprise Maintenance Audit application. ";
        		String errorReason = "";
        		
        		if(newNciUser == null){
					logger.error(accessError);
    				return "notauthorized";
				}
				
				//If OracleId is Null
				if(StringUtils.isEmpty(newNciUser.getOracleId())){
					errorReason = "OracleId is Null.";
				}
				//If Email is Null
				if(StringUtils.isEmpty(newNciUser.getEmail())){
					errorReason = "Email is Null.";
				}
				//If User is Inactive
				else if("N".equalsIgnoreCase(newNciUser.getActiveFlag()) ){
					errorReason = "I2E Account is not Active.";
				}
				
				if(StringUtils.isNotEmpty(errorReason)){
					logger.error(accessError + errorReason);
					return "notauthorized";
				} 
				
            	populateNCIUserRoles(newNciUser);
            	
            	//If user doesn't have required role to access this application then navigate the user to Login Error page.
            	if(!verifyAuthorization(newNciUser)){
            		return "notauthorized";
            	}
            	BeanUtils.copyProperties(newNciUser, nciUser);
            	logger.debug("NCI user retrive  fm session:" + nciUser);
            	return invocation.invoke();
            } else {
                if (action instanceof ValidationAware) {
                	String message  = "Site Minder did not pass the SM User";         
                	logger.info(message);
                    ((ValidationAware)action).addActionError(message);                 
                }
                // user has not signed on with Apache yet, this should only happen one time
                return "error";
            }
        } else {

            //user already in session
            return invocation.invoke();
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
