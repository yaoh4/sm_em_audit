/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;


import java.text.DateFormat;
import java.util.ArrayList;
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
	
	
    public String getFullName() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String fullName = accountVO.getFullName();
		if(!fullName.replace(", ", "").isEmpty()) {
			return fullName;
		} 
		
		return "";
    }
	
    public String getOrgId() {
		
		String orgId = "";
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			orgId = accountRoles.get(0).getOrgId();
		}
		
		return orgId;
    }
	
	public String getApplicationRole() {
		
		String role = "";
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			role = accountRoles.get(0).getRoleName();
		}
		
		return role;
	}
	
	
	public String getRoleCreateOn() {
		String createdOn = "";
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			createdOn =  DateFormat.getDateInstance().format(accountRoles.get(0).getCreatedDate());
		}
		
		return createdOn;
	}
	
	
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

}
