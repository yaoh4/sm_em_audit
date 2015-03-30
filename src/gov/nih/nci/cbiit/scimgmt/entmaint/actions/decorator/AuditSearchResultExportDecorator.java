/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountActivityVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author menons2
 *
 */
public class AuditSearchResultExportDecorator extends AuditSearchResultDecorator {

	/**
	 * Default Constructor
	 */
	public AuditSearchResultExportDecorator() {
		
	}
	
	/**
	 * Checks if SOD discrepancy exists
	 * 
	 * @return true is discrepancy exists, else false.
	 */
	public String getDiscrepancySod() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_SOD);
	}
	
	
	/**
	 * Checks if there NED IC discrepancy exists
	 * 
	 * @return true if discrepancy exists, else false
	 */
	public String getDiscrepancyIc() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_IC);
	}
	
	
	/**
	 * Checks if NED Inactive discrepancy exists
	 * 
	 * @return true if discrepancy exists, else false
	 */
	public String getDiscrepancyNedInactive() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_NED_INACTIVE);
	}
	
	
	/**
	 * Checks if NED Last Name discrepancy exists
	 * 
	 * @return true if discrepancy exists, else false.
	 */
	public String getDiscrepancyLastName() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_LAST_NAME);
	}
	
	
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
		String fullName = accountVO.getFullName();
		if(!fullName.replace(", ", "").isEmpty()) {
			return fullName;
		} 
		
		return "";
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
	
	
	/**
	 * Get the date on which the role being displayed in this row was created.
	 * 
	 * @return String the date on which the role was created
	 */
	public String getRoleCreateOn() {
		String createdOn = "";
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			createdOn =  DateFormat.getDateInstance().format(accountRoles.get(0).getCreatedDate());
		}
		
		return createdOn;
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
			if(accountActivityVw != null && (accountActivityVw.getUnsubmittedFlag() == null || accountActivityVw.getUnsubmittedFlag().equalsIgnoreCase("Y"))){
				note = accountActivityVw.getNotes();
			}
		}
		
		return note;
	}
	
	
	/**
	 * Get the last updated date and the full name of the IC cordinator who made
	 * the update
	 * 
	 * @return String last name and update date
	 */
	public String getAccountSubmitted(){
		
		StringBuffer lastUpdated = new StringBuffer();
		
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		
		EmAuditAccountActivityVw eaaVw = accountVO.getAccountActivity();
		if(eaaVw != null){		
			Date submittedDate = eaaVw.getSubmittedDate();
			if(submittedDate != null){
				String dateStr = new SimpleDateFormat("MM/dd/yyyy HH:mm a").format(submittedDate);
				lastUpdated.append("Submitted on ").append(dateStr);
				String submittedBy = accountVO.getAccountActivity().getSubmittedByFullName();	
				if(submittedBy == null) {
					submittedBy = accountVO.getId().toString();
				}
				lastUpdated.append(" by ").append(submittedBy);				
			}
		}
							
		return lastUpdated.toString();
	}

}
