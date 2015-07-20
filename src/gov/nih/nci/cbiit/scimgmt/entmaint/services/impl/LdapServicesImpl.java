package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.exceptions.UserLoginException;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.ApplicationService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LdapServices;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.LdapUtil;

import java.util.ArrayList;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service Layer for LDAP access. This code has been reused from other
 * I2E applications.
 * 
 * @author menons2
 *
 */
@Component
public class LdapServicesImpl implements LdapServices {
    Logger logger = Logger.getLogger(LdapServicesImpl.class);

    @Autowired
    private LdapUtil ctx;
    
    @Autowired
    private NciUser nciUser;
    
    @Autowired
    private ApplicationService applicationService; 
    
    @Autowired
	private EntMaintProperties entMaintProperties;

    public static final String NCI_ORACLE_ID = "nciOracleId";

    public static final String CN = "cn";
    public static final String EMAIL = "mail";
    public static final String FIRST_NAME = "givenname";
    public static final String SUR_NAME = "sn";
    public static final String LAST_NAME = "lastname";
    public static final String FULL_NAME = "fullname";
    public static final String GROUP_MEMBERSHIP = "groupmembership";
    public static final String OU = "nciaumembership";

    private static final String WHITE_SPACE = "";
    private static final String SINGLE_SPACE = " ";

    private String[] stAttrDirIDs = 
    { CN, NCI_ORACLE_ID, FIRST_NAME, SUR_NAME, FULL_NAME, EMAIL, GROUP_MEMBERSHIP, OU };

    
    /**
     * Populates the nciUser with LDAP data and checks if the user has the privileges
     * to access the application.
     * 
     * @param remoteUser
     * 
     * @return NciUser the nciUser object populated with data from LDAP.
     * @throws gov.nih.nci.cbiit.oracle.gpmats.exceptions.UserLoginException
     */
    public NciUser verifyNciUserWithRole(String remoteUser) throws UserLoginException {
        logger.debug("inside NCIInterceptor.verifyNciUserWithRole():" + 
                     remoteUser);
        nciUser = populateNCIUser(remoteUser);
        // Load user EM roles
        applicationService.loadPersonInfo(nciUser);
        
        // For non production environment, give IC coordinator role to application developers
        if (entMaintProperties.getPropertyValue("ENVIRONMENT").contains("Development") ||
            	!entMaintProperties.getPropertyValue("ENVIRONMENT").equalsIgnoreCase("Test") ||
            	!entMaintProperties.getPropertyValue("ENVIRONMENT").equalsIgnoreCase("Stage")) {
        	String  devUsers= entMaintProperties.getPropertyValue("APP_DEVELOPER");
            String[] appDevUsers  = devUsers.split(",");
            for (int i=0;i <appDevUsers.length;i++){
                if (nciUser.getUserId().equalsIgnoreCase(appDevUsers[i])){
                    nciUser.setCurrentUserRole("EMREP");
                }
            }
        }
        
        // Verify that the logged on user has the role to access the application
        if (!verifyAuthorization(nciUser)) {
            // If user does not have permission
            logger.info(nciUser + 
                        "does not have the privilege to access this application. " + 
                        "Access is denied!!! ");

            throw new UserLoginException(this.getClass().getName(), 
                                         "verifyNciUserWithRole", 
                                         "User " + remoteUser + 
                                         " does not have the necessary role to access the application");
        }
        return nciUser;
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
     * Sets the user attributes from Ldap, this operation is called after getting
     * the remote user.
     * 
     * @param remoteUser
     * @throws gov.nih.nci.cbiit.oracle.gpmats.exceptions.UserLoginException
     */
    private NciUser populateNCIUser(String remoteUser) throws UserLoginException {
        logger.debug("Retrieving NciUser from LDAP for user id " + remoteUser);
        String loginErrorMessage = " Error while verifying  the User , roles , permissions " +
                            "of user '"+ remoteUser +"' in LDAP : ";
        String stFDN = null;
        Attributes attribs = null;
        if (nciUser != null) {
            try {
                // Strip remote user for the ldap id if the remoter user string begins with "cn"
                if (remoteUser.indexOf("cn") >= 
                    0) { //In "cn=dellosoj,ou=nci,o=nih" ,dellosoj is UserID
                    StringBuffer ru = new StringBuffer(20);
                    int cnIdx = remoteUser.indexOf("cn");
                    for (int i = cnIdx + 3; 
                         i < remoteUser.length() && remoteUser.charAt(i) != 
                         ','; i++)
                        ru.append(remoteUser.charAt(i));
                    remoteUser = ru.toString();
                }
                nciUser.setUserId(remoteUser);             
                stFDN = ctx.getUserFDN(remoteUser);
                logger.debug("stFDN from ctx:" + stFDN);
                attribs = ctx.getAttributes(stFDN, stAttrDirIDs);
                logger.debug("attributes from ctx:" + attribs);
            } catch (Exception ex) {
                logger.error(ex);
                throw new UserLoginException(this.getClass().getName(), 
                                             "setUserAttributes", 
                                             loginErrorMessage + 
                                             ex.getMessage());
            }

            try {
                if (attribs.get(EMAIL).get() != null) {
                    nciUser.setEmail(attribs.get(EMAIL).get().toString());
                } else {
                    nciUser.setEmail(null);
                }
            } catch (Exception ex) {
                logger.error(ex);
                throw new UserLoginException(this.getClass().getName(), 
                                             "setUserAttributes",
                                             loginErrorMessage+ EMAIL + " "+ex.getMessage());
            }

            try {
                if (attribs.get(NCI_ORACLE_ID).get() != null) {
                    nciUser.setOracleId(attribs.get(NCI_ORACLE_ID).get().toString());
                } else {
                    nciUser.setOracleId(null);
                }               
            } catch (Exception ex) {
                logger.error(ex);
                throw new UserLoginException(this.getClass().getName(), 
                                             "setUserAttributes", 
                                             loginErrorMessage+"(NCI_ORCALE_ID) "+ 
                                             ex.getMessage());
            }
            try {
                if (attribs.get(FIRST_NAME).get() != null) {
                    nciUser.setFirstName(attribs.get(FIRST_NAME).get().toString());
                } else {
                    nciUser.setFirstName(null);
                }
            } catch (Exception ex) {
                logger.error(ex);
                throw new UserLoginException(this.getClass().getName(), 
                                             "setUserAttributes", 
                                             loginErrorMessage+"(FIRST_NAME)  "
                                             +ex.getMessage());
            }
            try {
                if (attribs.get(SUR_NAME).get() != null) {
                    nciUser.setLastName(attribs.get(SUR_NAME).get().toString());
                } else {
                    nciUser.setLastName(null);
                }
            } catch (Exception ex) {
                logger.error(ex);
                throw new UserLoginException(this.getClass().getName(), 
                                             "setUserAttributes", 
                                             loginErrorMessage+"(SUR_NAME) "+ 
                                             ex.getMessage());
            }

            try {
                if (attribs.get(FULL_NAME) != null) {
                    nciUser.setFullName(attribs.get(FULL_NAME).get().toString());
                } else {
                    String givenName = (String)nciUser.getFirstName();
                    String lastName = nciUser.getLastName();
                    if (givenName == null) {
                        givenName = WHITE_SPACE;
                    }
                    if (lastName == null) {
                        lastName = WHITE_SPACE;
                    }
                    nciUser.setFullName(givenName + SINGLE_SPACE + lastName);
                    logger.info("Attribute full name missing from LDAP for " + 
                                nciUser.getFullName());
                }                
            } catch (Exception ex) {
                logger.error(ex);
                throw new UserLoginException(this.getClass().getName(), 
                                             "setUserAttributes", 
                                             loginErrorMessage+"(FULL_NAME) "  
                                             +ex.getMessage());
            }
            try {
                if (attribs.get(CN).get() != null) {
                    nciUser.setUserId(attribs.get(CN).get().toString());
                }
            } catch (Exception ex) {
                logger.error(ex);
                throw new UserLoginException(getClass().getName(), 
                                             "setUserAttributes", 
                                             loginErrorMessage+"(CN) "+                                             
                                             ex.getMessage());
            }
            try {
                if (attribs.get(GROUP_MEMBERSHIP).get() != null) {
                    NamingEnumeration<?> groupMembership = 
                        attribs.get(GROUP_MEMBERSHIP).getAll();
                    ArrayList<String> groups = new ArrayList<String>();
                    String group = null;
                    while (groupMembership.hasMoreElements()) {
                        group = 
                                groupMembership.nextElement().toString().toUpperCase();
                        // Strip role and get only group.  Get the role by removing
                        // the "cn=" and remove every characters  after the first encounter with a comma
                        group = group.substring(3, group.indexOf(","));
                        groups.add(group);
                    }
                    nciUser.setGroupMembership(groups);
                    
                } else {
                    nciUser.setGroupMembership(null);
                }
            } catch (Exception ex) {
                logger.error(ex);
                throw new UserLoginException(this.getClass().getName(), 
                                             "setUserAttributes", 
                                             loginErrorMessage +" (GROUP_MEMBERSHIP)  " + 
                                             ex.getMessage());
            }
        } else {
            // did not find a User
            throw new UserLoginException(this.getClass().getName(), 
                                         "verifyAuthorization", 
                                         loginErrorMessage + " (User not found) ");

        }
        return nciUser;
    }

    /**
     * Sets ctx
     * 
     * @param ctx 
     */
    public void setCtx(LdapUtil ctx) {
        this.ctx = ctx;
    }

    /**
     * Gets ctx
     * 
     * @return LdapUtil
     */
    public LdapUtil getCtx() {
        return ctx;
    }

    /**
     * Sets nciUser
     * 
     * @param nciUser
     */
    public void setNciUser(NciUser nciUser) {
        this.nciUser = nciUser;
    }

    /**
     * Gets nciUser
     * 
     * @return NciUser
     */
    public NciUser getNciUser() {
        return nciUser;
    }

}
