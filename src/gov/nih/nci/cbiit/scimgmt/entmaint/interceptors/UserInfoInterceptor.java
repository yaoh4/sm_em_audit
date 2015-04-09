package gov.nih.nci.cbiit.scimgmt.entmaint.interceptors;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.ApplicationService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LdapServices;

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
import org.springframework.beans.factory.annotation.Autowired;



@SuppressWarnings("serial")
public class UserInfoInterceptor extends AbstractInterceptor implements StrutsStatics  {

	@Autowired
    private LdapServices ldapServices; 
	
    public static Logger logger = Logger.getLogger(UserInfoInterceptor.class);

    private static final String EXCLUDED_USER_ID="ncildap";

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
        // Is there a "user" object stored in the user's HttpSession?
        NciUser nciUser = 
            (NciUser)session.get(ApplicationConstants.SESSION_USER);
        

    	//Check if this is a change user action
        String changeUser = request.getParameter("user");
        
       //Check if this is a sys admin action
        String sysAdminAction = request.getParameter("task");
        
        if (nciUser == null && StringUtils.isEmpty(changeUser) && StringUtils.isEmpty(sysAdminAction)) {
                ServletContext sc = request.getSession().getServletContext();
                
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

                // Check for excluded user
                if (EXCLUDED_USER_ID.equalsIgnoreCase(remoteUser)) {
                    String message  = "Login attempt by user  '"+remoteUser+
                                      "' . User  has NO privileges for this application";
                    
                    //logger.info(message);
                    if (action instanceof ValidationAware) {
                        ((ValidationAware)action).addActionError("<B>"+message+"</B>");
                    }
                    return "error";
                }
                nciUser = ldapServices.verifyNciUserWithRole(remoteUser);
                
                session.put(ApplicationConstants.SESSION_USER, nciUser);
                logger.debug("NCI user retrive  fm session:" + nciUser);
                return invocation.invoke();
            } else {
                if (action instanceof ValidationAware) {
                    ((ValidationAware)action).addActionError("Site Minder did not pass the SM User");
                }
                // user has not signed on with Apache yet, this should only happen one time
                return "error";
            }
        } else {

            //user already in session
            return invocation.invoke();
        }
    }

    public void setLdapServices(LdapServices ldapServices) {
        this.ldapServices = ldapServices;
    }
    
 
}
