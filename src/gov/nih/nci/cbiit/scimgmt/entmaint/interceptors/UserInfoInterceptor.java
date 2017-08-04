package gov.nih.nci.cbiit.scimgmt.entmaint.interceptors;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.exceptions.UserLoginException;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.UserRoleService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.I2eActiveUserRolesVw;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
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
        String changeUser = request.getParameter("changeUser");
        String actionName = action.getClass().getName();
        if(StringUtils.isEmpty(changeUser) && actionName.contains("SysAdminAction")) {
        	changeUser = request.getParameter("user");
        }
        
       //Check if this is a sys admin action
        String sysAdminAction = request.getParameter("task");
        
        if ((nciUser == null || nciUser.getUserId() == null || !StringUtils.isEmpty(changeUser)) && StringUtils.isEmpty(sysAdminAction)) {               
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
            	if(isUserValid(request, remoteUser)) {
            		populateNCIUserRoles(newNciUser);
            	
            		//If user doesn't have required role to access this application then navigate the user to Login Error page.
            		if(!verifyAuthorization(newNciUser)){
            			return "notauthorized";
            		}
            		newNciUser.setAppRoles(userRoleService.getUserAppRoles(newNciUser.getUserId()));                
	            	BeanUtils.copyProperties(newNciUser, nciUser);
	            	session.put(ApplicationConstants.REMOTE_USER, newNciUser);
	            	
            		//If logged on user is I2E developer
	            	if(isI2eDeveloper(remoteUser)) {
    	            	session.put(ApplicationConstants.DEVELOPER_ROLE, ApplicationConstants.FLAG_YES);
	            		//If changeUser is set, validate and replace new user in session
	            		  if(!StringUtils.isEmpty(changeUser)) {
	            			    NciUser newChangeUser = userRoleService.getNCIUser(changeUser);
	            			  	if(isUserValid(request, changeUser)) {
	            				populateNCIUserRoles(newChangeUser);
	                        	
	                    		//If user doesn't have required role to access this application then navigate the user to Login Error page.
	                    		if(!verifyAuthorization(newChangeUser)){
	                    			return "notauthorized";
	                    		}
	                    		//If this is a prod env, then I2E users will have only read only privileges
	    	            		if(isProdEnv()) {
	    	            			newChangeUser.setReadOnly(true);
	     	            		}
	    	            		newChangeUser.setAppRoles(userRoleService.getUserAppRoles(newChangeUser.getUserId()));                
	        	            	BeanUtils.copyProperties(newChangeUser, nciUser);
								logger.info("Change User - Original Default/Logged in user: " + newNciUser.getUserId()
										+ "(" + newNciUser.getFullName() + ") Changed User: "
										+ newChangeUser.getUserId() + "(" + newChangeUser.getFullName() + ")");
	            			} else {
	            				return "notauthorized";
	            			}
	            		}
	            	} else if (!StringUtils.isEmpty(changeUser)) {
	            		return "notauthorized";
	            	}
	            	
            		return invocation.invoke();
            	} else {
            		return "notauthorized";
            	}
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
    
    /**
     * to verify if the user is valid
     * 
     * @param request
     * @param remoteUser
     * @return true if the user is valid
     * @throws Exception
     */
    protected boolean isUserValid(HttpServletRequest request, String remoteUser)
        	throws Exception {
        	
        	String errorReason = "";
        	String accessError = "User "+ remoteUser +" is not authorized to access EM Audit application. ";
        
        	NciUser nciUser = userRoleService.getNCIUser(remoteUser);           

        	if(nciUser == null){
        		 logger.error(accessError);
        		 return false;
        	 }
    		
    		//If OracleId is Null
    		if(StringUtils.isBlank(nciUser.getOracleId())){
    			errorReason = "OracleId is Null.";
    		}
    		//If User is Inactive
    		else if("N".equalsIgnoreCase(nciUser.getActiveFlag())){
    			errorReason = "I2E Account is not Active.";			
    		}
    		
    		//If Email is Null
            if(StringUtils.isBlank((String)nciUser.getEmail())){
    			errorReason = "Email is Null.";
    		}
            
            // if the user is i2e restricted user
             if(userRoleService.isRestrictedUser(nciUser.getOracleId())) {
            	 errorReason = "User has Restricted access.";
             }
    		
            //Then navigate the user to Login Error page.
       	    if(StringUtils.isNotBlank(errorReason)){               
       	    	logger.error(accessError + errorReason);
       	    	return false;
       	    }
       	    
       	    return true;  
    	
        }
    
    /**
     * to check if the Environment is Prod 
     * 
     * @return true if the environment is prod
     */
    public boolean isProdEnv() {    	
        String environment  = entMaintProperties.getProperty("ENVIRONMENT");
    	/*if(!"Development".equalsIgnoreCase(environment) &&
    			!"Test".equalsIgnoreCase(environment) &&
    			!"Stage".equalsIgnoreCase(environment)) {*/
        
        if("Development".equalsIgnoreCase(environment)) {
    		//This is prod environment, so restrict access
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * to check if the logged in user has an I2E Developer Role
     * 
     * @param userId
     * @return true if the use has an i2e developer role
     */
    private boolean isI2eDeveloper(String userId) {
		
		List<I2eActiveUserRolesVw> appRoles = userRoleService.getUserAppRoles(userId);
		for(I2eActiveUserRolesVw role: appRoles) {
    		if(ApplicationConstants.I2E_DEV_ROLE.equals(role.getRoleCode())) {
    			return true;
    		}
    	}
    	return false;
    }
 
}
