package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountActivityVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;

import org.apache.commons.collections.CollectionUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * This class is responsible for decorating the rows of I2E portfolio accounts search results table.
 * @author Jim Zhou
 *
 */
@Configurable
public class I2eAuditSearchResultExportDecorator extends TableDecorator{
	
	/**
	 * Get the full name.
	 * @return fullName
	 */
	public String getFullName() {
		AuditI2eAccountVO auditVO = (AuditI2eAccountVO)getCurrentRowObject();
		return auditVO.getFullName();
	}
	
	/**
	 * get created date	
	 * @return created date in string
	 */
	public String getI2eCreatedDate(){
		AuditI2eAccountVO auditVO = (AuditI2eAccountVO)getCurrentRowObject();
		Date createDate = auditVO.getCreatedDate();
		if(createDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(createDate);
	}
	
	/**
	 * Get the action performed on this account
	 * 
	 * String action info
	 */
	public String getAction() {
		String actionStr = "";
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		
		if (accountVO.getAction() != null) {
			AppLookupT action = accountVO.getAction();
			if (action != null && action.getDescription() != null) {
				if(accountVO.getUnsubmittedFlag() == null || accountVO.getUnsubmittedFlag().equalsIgnoreCase("N")){
					actionStr = action.getDescription();
				}
			}
		}
		return actionStr;
	}
	
	/**
	 * Get Submitted by user and Submitted on date.
	 * @return string representing submitted by + submitted on date.
	 */
	public String getLastUpdated(){
		AuditI2eAccountVO auditVO = (AuditI2eAccountVO)getCurrentRowObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy 'at' h:mm a");
		String lastUpdated = "";
		if( auditVO.getNpnId() != null && auditVO.getLastUpdByFullName() !=null){
			lastUpdated =  "Updated on " +dateFormat.format(auditVO.getSubmittedDate()) + " by "  +auditVO.getLastUpdByFullName();
		}
		else{
			lastUpdated = "";
		}
		return lastUpdated;
	}
	
	/**
	 * This method is for account submitted on and submitted by.
	 * @return
	 */
	public String getAccountSubmitted(){
		String submittedBy = "";
		Date submittedDate = null;
		
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		String id = ""+accountVO.getId();
		
		if(accountVO.getAction() == null){
			return "<div id='submittedby"+ id +"'></div>";
		}else{
			submittedBy = accountVO.getSubmittedBy();
			submittedDate = accountVO.getSubmittedDate();
		}
		if(submittedBy == null){
			submittedBy = "<div id='submittedby"+ id +"'></div>";
		}else{
			String dateStr = "";
			if(submittedDate != null){
				dateStr = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a").format(submittedDate);
			} 
			if(accountVO != null && (accountVO.getUnsubmittedFlag() == null || accountVO.getUnsubmittedFlag().equalsIgnoreCase("Y"))){
				submittedBy = "<div id='submittedby" + id + "'></div>" + "<input type='hidden' id='hiddenSubmittedby" + id +"' value='Submitted on " + dateStr + " by " + submittedBy + "'/>";
			}else{
				submittedBy = "<div id='submittedby"+ id +"'>" + "Submitted on " + dateStr + " by " + submittedBy + "</div>";
			}
		}
		return  submittedBy;
	}
	
	/**
	 * Get application role for this row.
	 * @return applicationRole string with info icon.
	 */
	public String getActiveI2eRole(){
		AuditI2eAccountVO auditVO = (AuditI2eAccountVO)getCurrentRowObject();
		List<EmI2eAuditAccountRolesVw> roles = auditVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String role = "";
		EmI2eAuditAccountRolesVw roleVw = roles.get(0);
		role = roleVw.getRoleDescription();

		return role;
	}
	
	/**
	 * Get the date on which this role was created.
	 * @return role created date in mm/dd/yyyy format.
	 */
	public String getRoleCreateOn(){
		AuditI2eAccountVO auditVO = (AuditI2eAccountVO)getCurrentRowObject();
		List<EmI2eAuditAccountRolesVw> roles = auditVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String createDate = "";
		EmI2eAuditAccountRolesVw roleVw = roles.get(0);
		createDate = new SimpleDateFormat("MM/dd/yyyy").format(roleVw.getCreatedDate());

		return createDate;
	}
	
	/**
	 * Get the notes information entered for the action performed on this account.
	 * 
	 * @return the notes info.
	 */
	public String getActionNote(){
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		String note = "";
		if(accountVO.getAction() != null) {
			if(accountVO.getUnsubmittedFlag() == null || accountVO.getUnsubmittedFlag().equalsIgnoreCase("N")){
				note = accountVO.getNotes();
			}
		}
		
		return note;
	}
	
	/**
	 * Checks if SOD discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancySod() {
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_I2E_SOD);
	}
	
	/**
	 * Checks if NED Inactive discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyNedInactive() {
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_I2E_NED_INACTIVE);
	}
	
	/**
	 * Checks if Inactive I2E Role(s) discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyInactiveI2eRole() {
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_I2E_NO_ACTIVE_ROLE);
	}
	
	/**
	 * Checks if Inactive IMPAC II discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyI2eOnly() {
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_I2E_ONLY);
	}
	
	/**
	 * Checks if I2e Inactive discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyI2eInactive() {
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_I2E_ACTIVE_ROLE_REMAINDER);
	}
	
	/**
	 * This method is for displaying Org path for application roles. It could be multiple.
	 * @return String
	 */
	public String getOrgPath(){
		String orgPath = "";
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		List<EmI2eAuditAccountRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			orgPath = accountRoles.get(0).getFullOrgPathAbbrev();
		}
		
		return orgPath;
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
	private String isDiscrepancy(AuditI2eAccountVO accountVO, String type) {
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
	 * Get the last submitted date.
	 * 
	 * @return String the update date
	 */
	public String getSubmittedDate(){
		
		String dateStr = "";
		
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		
		if(accountVO != null){		
			Date submittedDate = accountVO.getSubmittedDate();
			if(submittedDate != null){
				dateStr = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a").format(submittedDate);		
			}
		}
							
		return dateStr;
	}
}
