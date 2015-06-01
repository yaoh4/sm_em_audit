package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountActivityVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Decorator to display data in Excel spreadsheet from Audit tab.
 * 
 * @author menons2
 *
 */
public class AuditSearchResultExportDecorator extends AuditSearchResultDecorator {

	/**
	 * Checks if SOD discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancySod() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_SOD);
	}
	
	
	/**
	 * Checks if there NED IC discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyIc() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_IC);
	}
	
	
	/**
	 * Checks if NED Inactive discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyNedInactive() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_NED_INACTIVE);
	}
	
	
	/**
	 * Checks if NED Last Name discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyLastName() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		if(!isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_LAST_NAME).isEmpty()) {
			return accountVO.getImpaciiLastName();
		}
		
		return "";
	}
	
	
	/**
	 * Checks if a discrepancy of the given type exists in the given account.
	 * Helper method used by the other discrepancy specific methods.
	 * 
	 * @param accountVO the account in which to look for discrepancy
	 * @param type the discrepancy type to check for.
	 * 
	 * @return String 'Y' if discrepancy exists, else return empty String.
	 */
	private String isDiscrepancy(AuditAccountVO accountVO, String type) {
		String isDiscrepancy = "";
		
		List<String> discrepancies = accountVO.getAccountDiscrepancies();
		if(!CollectionUtils.isEmpty(discrepancies)) {
			for(String discrepancy: discrepancies) {
				if(discrepancy.equals(type)) {
					return "Y";
				}
			}
		}
		return isDiscrepancy;
	}
	
	
	/**
	 * Get the full name of the account holder represented by this row.
	 * 
	 * @return String the full name of the account holder
	 */
    public String getFullName() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		
		return accountVO.getFullName();
    }
	
    
    /**
	 * get the organization id of the account holder represented by this row
	 * 
	 * @return String the organization id
	 */
    public String getOrgId() {
		
		String orgId = "";
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			orgId = accountRoles.get(0).getOrgId();
		}
		
		return orgId;
    }
	
    
    /**
	 * get the application role of the account role for display on the current row.
	 * In the excel spreadsheet, each role is displayed in a separate row, hence 
	 * the accountRoles array is populated such that it has only one element with
	 * the value of the role for that row.
	 * 
	 * String the role to be displayed in this row
	 */
	public String getApplicationRole() {
		
		String role = "";
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			role = accountRoles.get(0).getRoleName();
		}
		
		return role;
	}
	
	public String getApplicationRoleCreatedBy(){
		String createdBy = "";
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			createdBy = accountRoles.get(0).getCreatedByUserId();
		}
		
		return createdBy;
	}
	
	/**
	 * Get the date on which the role being displayed in this row was created.
	 * 
	 * @return String the date on which the role was created
	 */
	public String getRoleCreateOn() {
		SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy");
		
		String createdOn = "";
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			createdOn =  dateFormat.format(accountRoles.get(0).getCreatedDate());
		}
		
		return createdOn;
	}
	
	
	public String getCreatedBy(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String createdBy =  accountVO.getCreatedByFullName();
		if(StringUtils.isEmpty(createdBy)) {
			createdBy = accountVO.getCreatedByUserId();
		}
		return createdBy;
	}

	public String getDeletedBy(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String deletedBy = accountVO.getDeletedByFullName();
		if(StringUtils.isEmpty(deletedBy)) {
			deletedBy = accountVO.getDeletedByUserId();
		}
		return deletedBy;
	}
	
	
	/**
	 * Get the action performed on this account
	 * 
	 * String action info
	 */
	public String getAction() {
		String actionStr = "";
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		
		EmAuditAccountActivityVw eaaVw = accountVO.getAccountActivity();
		if (eaaVw != null) {
			AppLookupT action = eaaVw.getAction();
			if (action != null && action.getDescription() != null) {
				actionStr = action.getDescription();			
			}
		}
		return actionStr;
	}
	
	
	/**
	 * Get the notes information entered for the action performed on this account.
	 * 
	 * @return the notes info.
	 */
	public String getActionNote(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String note = "";
		EmAuditAccountActivityVw accountActivityVw = accountVO.getAccountActivity();
		if(accountVO.getAccountActivity() != null) {
			if(accountActivityVw != null && (accountActivityVw.getUnsubmittedFlag() == null || accountActivityVw.getUnsubmittedFlag().equalsIgnoreCase("N"))){
				note = accountActivityVw.getNotes();
			}
		}
		
		return note;
	}
	
	
	/**
	 * Get the last submitted date.
	 * 
	 * @return String the update date
	 */
	public String getSubmittedOn(){
		
		String dateStr = "";
		
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		
		EmAuditAccountActivityVw eaaVw = accountVO.getAccountActivity();
		if(eaaVw != null){		
			Date submittedDate = eaaVw.getSubmittedDate();
			if(submittedDate != null){
				dateStr = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a").format(submittedDate);		
			}
		}
							
		return dateStr;
	}
	
	
	/**
	 * Get the full name of the person who made the submission
	 * 
	 * @return the name of the person who submitted the action.
	 */
	public String getSubmittedBy() {
	String submittedBy = "";
		
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		
		EmAuditAccountActivityVw eaaVw = accountVO.getAccountActivity();
		if(eaaVw != null){		
			Date submittedDate = eaaVw.getSubmittedDate();
			if(submittedDate != null){
				submittedBy = eaaVw.getSubmittedByFullName();	
				if(submittedBy == null) {
					submittedBy = accountVO.getId().toString();
				}			
			}
		}
							
		return submittedBy;
	}

}
