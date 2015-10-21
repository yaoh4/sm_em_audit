package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import java.util.List;
import java.util.Map;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditHistoryVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;


import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author menons2
 *
 */
public class EmAppUtil {

	private static final Logger logger = Logger.getLogger(EmAppUtil.class);
	
	
	/**
	 * Default Constructor
	 */
	public EmAppUtil() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Checks if the user is a superuser.
	 * 
	 * @param session Session object from the ActionContext or jsp
	 * @return
	 */
	public static boolean isAdminUser() {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		NciUser nciUser = (NciUser)session.get(ApplicationConstants.SESSION_USER);
		
		
		if (nciUser != null) {
			String currentUserRole = nciUser.getCurrentUserRole();
			if(currentUserRole != null && currentUserRole.equalsIgnoreCase(ApplicationConstants.USER_ROLE_SUPER_USER)) {
				logger.debug("User " + nciUser.getLdapId() + " is primary IC Cordinator");
				return true;
			}  else {
				logger.debug("User " + nciUser.getLdapId() + " is not primary IC Cordinator");
			}
		} else {
			logger.warn("Could not retrieve nciUser from session");
		}
		
		return false;
	}
	
	
	/**
	 * Gets the current state of an Audit.
	 * 
	 * @return String current state of the audit
	 */
	public static String getCurrentAuditState(EmAuditsVO emAuditsVO) {
		String currentAuditState = null;	
		
		if(emAuditsVO != null) {
			
			List<EmAuditHistoryVw> statusHistories = emAuditsVO.getStatusHistories();
			
			//Get the newest row from history table. The state of this 
	        //row is the state of the audit.
			if(statusHistories != null && statusHistories.size() > 0) {
				currentAuditState = statusHistories.get(0).getActionCode();
			}
		} else {
			currentAuditState = ApplicationConstants.AUDIT_STATE_CODE_RESET;
		}
		logger.debug("Current state of Audit " + emAuditsVO.getId() + " is " + currentAuditState);
		return currentAuditState;
	}
	
	
	
	
	/**
	 * Checks if the current Audit is enabled.
	 * 
	 * @param session The session object from the ActionContext or jsp.
	 * 
	 * @return true if audit is enabled, else false.
	 */
	public static boolean isAuditEnabled(Long auditId) {		
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		
		if(session != null) {
			EmAuditsVO emAuditsVO = (EmAuditsVO)session.get(ApplicationConstants.CURRENT_AUDIT);
			if(emAuditsVO != null) {
				if(emAuditsVO.getId().equals(auditId)) {
					String auditState = getCurrentAuditState(emAuditsVO);
					if(ApplicationConstants.AUDIT_STATE_CODE_ENABLED.equals(auditState)) {
						logger.debug("Audit with ID " + auditId + " is current and enabled");
						return true;
					} else {
						logger.debug("Audit with ID " + auditId + " is not current or not enabled");
					}
				} else {
					logger.debug("Audit with ID " + auditId + " is not current or last active");
				}
			} else {
				logger.warn("No audit in session");
			}
		}
				
		return false;	
	}
		
	
	/**
	 * Checks if the current Audit is enabled.
	 * 
	 * @return true if audit is enabled, else false.
	 */
	public static boolean isAuditEnabled() {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		
		if(session != null) {
			EmAuditsVO emAuditsVO = (EmAuditsVO)session.get(ApplicationConstants.CURRENT_AUDIT);
			if(emAuditsVO != null) {
				String auditState = getCurrentAuditState(emAuditsVO);
				if(ApplicationConstants.AUDIT_STATE_CODE_ENABLED.equals(auditState)) {
					logger.debug("Audit in session with ID " + emAuditsVO.getId() + " is current and enabled");
					return true;
				} else {
					logger.debug("Audit in session with ID " + emAuditsVO.getId() + " is not current or not enabled");
				}
			} else {
				logger.warn("No audit in session");
			}
		}
				
		return false;	
	}
	
	/**
	 * Checks if the current Audit is enabled and contains I2E audit.
	 * 
	 * @return true if audit is enabled, else false.
	 */
	public static boolean isI2eAuditEnabled() {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		
		if(session != null) {
			EmAuditsVO emAuditsVO = (EmAuditsVO)session.get(ApplicationConstants.CURRENT_AUDIT);
			if(emAuditsVO != null) {
				String auditState = getCurrentAuditState(emAuditsVO);
				if(ApplicationConstants.AUDIT_STATE_CODE_ENABLED.equals(auditState) && emAuditsVO.getI2eFromDate() != null) {
					logger.debug("Audit in session with ID " + emAuditsVO.getId() + " is current and enabled");
					return true;
				} else {
					logger.debug("Audit in session with ID " + emAuditsVO.getId() + " is not current or not enabled or does not contain I2e");
				}
			} else {
				logger.warn("No audit in session");
			}
		}
				
		return false;	
	}
	
	/**
	 * Checks if the given Audit ID represent the current or last active audit.
	 * 
	 * @return true if audit is current or last active, else false.
	 */
	public static boolean isAuditCurrentOrLastActive(Long auditId) {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		
		if(session != null) {
			EmAuditsVO emAuditsVO = (EmAuditsVO)session.get(ApplicationConstants.CURRENT_AUDIT);
			if(emAuditsVO != null && emAuditsVO.getId().equals(auditId)) {
				logger.debug("Audit with ID " + auditId + " is current or last active");
				return true;
			} else {
				logger.debug("Audit with ID " + auditId + " is not current or last active");
			}
		}
		
		return false;	
	}
	
	
	public static boolean isAuditActionEditable(Long auditId) {
		boolean result = false;
		
		// Results should be editable if any one of the following conditions is satisfied 
		// - Selected audit is enabled,  or  
		// - Selected Audit is current/last active, and user is admin.

		if(isAuditEnabled(auditId) || (isAdminUser() && isAuditCurrentOrLastActive(auditId))) {
			result = true;
		}
		
		return result;
	}
	
	public static String getOptionLabelByValue(Long id, List<DropDownOption> categoryList){
		String label = "";
		for(DropDownOption ddo : categoryList){
			if(Long.parseLong(ddo.getOptionKey()) == id){
				label = ddo.getOptionValue();
				break;
			}
		}
		if(label.indexOf("inactive") >= 0){
			label = "inactive";
		}
		return label;
	}

	public static void logUserID(NciUser nciUser, Logger logger){
		logger.error("user ID: " + nciUser.getOracleId() + "/" + nciUser.getFullName());
	}

	public static void logUserID(NciUser nciUser, Log logger){
		logger.error("user ID: " + nciUser.getOracleId() + "/" + nciUser.getFullName());
	}

}
