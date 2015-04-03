package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Decorator to display data in Excel spreadsheet from Portfolio tab.
 * 
 * @author menons2
 *
 */
public class PortfolioSearchResultExportDecorator extends
		PortfolioSearchResultDecorator {

	
	/**
	 * Checks if SOD discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancySod() {
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_SOD);
	}
	
	
	/**
	 * Checks if there NED IC discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyIc() {
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_IC);
	}
	
	
	/**
	 * Checks if NED Inactive discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyNedInactive() {
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_NED_INACTIVE);
	}
	
	
	/**
	 * Checks if NED Last Name discrepancy exists for the current row.
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyLastName() {
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_LAST_NAME);
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
	private String isDiscrepancy(PortfolioAccountVO accountVO, String type) {
		String isDiscrepancy = "";
		
		List<String> discrepancies = accountVO.getAccountDiscrepancies();
		if(discrepancies != null && !discrepancies.isEmpty()) {
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
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		String fullName = accountVO.getFullName();
		if(!fullName.replace(", ", "").isEmpty()) {
			return fullName;
		} 
		
		return "";
    }
	
	
	/**
	 * Get the last updated date and the full name of the IC cordinator who made
	 * the update
	 * 
	 * @return String last name and update date
	 */
	public String getLastUpdated() {
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy hh:mm a");
		StringBuffer lastUpdated = new StringBuffer();
		
		String id = portfolioVO.getImpaciiUserId();
		if(portfolioVO.getNotesSubmittedDate() != null) {
			lastUpdated.append("Submitted on ").append(dateFormat.format(portfolioVO.getNotesSubmittedDate()));
			if(!StringUtils.isBlank(portfolioVO.getNotesSubmittedByFullName())) {
				lastUpdated.append(" by ").append(portfolioVO.getNotesSubmittedByFullName());
			} else {
				lastUpdated.append(" by ").append(id);
			}
		}			
		return lastUpdated.toString();
	}
	
	
	/**
	 * get the organization id of the account holder represented by this row
	 * 
	 * @return String the organization id
	 */
	public String getOrgId() {
		
		String orgId = "";
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> accountRoles = accountVO.getAccountRoles();
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
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> accountRoles = accountVO.getAccountRoles();
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
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			createdOn =  DateFormat.getDateInstance().format(accountRoles.get(0).getCreatedDate());
		}
		
		return createdOn;
	}
	
	
	/**
	 * Get the notes information entered for this account.
	 * 
	 * @return the notes info.
	 */
	public String getNotes(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		
		return portfolioVO.getNotes();
	}

}
