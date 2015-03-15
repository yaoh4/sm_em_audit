package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import java.util.List;
import java.util.Map;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditHistoryVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;


import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

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
	public static boolean isAdminUser(Map<String, Object> session) {
		
		NciUser nciUser = (NciUser)session.get(ApplicationConstants.SESSION_USER);
		
		
		if (nciUser != null) {
			String currentUserRole = nciUser.getCurrentUserRole();
			if(currentUserRole != null && currentUserRole.equalsIgnoreCase(ApplicationConstants.USER_ROLE_SUPER_USER)) {
				return true;
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
			
			//If emAuditsVO is empty, we do not have an active audit. 
			currentAuditState = ApplicationConstants.AUDIT_STATE_CODE_RESET;
		}
		
		return currentAuditState;
	}
	
	
	
	
	/**
	 * Checks if the current Audit is enabled.
	 * 
	 * @param session The session object from the ActionContext or jsp.
	 * 
	 * @return true if audit is enabled, else false.
	 */
	public static boolean isAuditEnabled(EmAuditsVO emAuditsVO) {		
		
			String auditState = getCurrentAuditState(emAuditsVO);
		
			if(ApplicationConstants.AUDIT_STATE_CODE_ENABLED.equals(auditState)) {
				return true;
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
		
				return isAuditEnabled(emAuditsVO);
			}
		
		return false;	
	}

}
