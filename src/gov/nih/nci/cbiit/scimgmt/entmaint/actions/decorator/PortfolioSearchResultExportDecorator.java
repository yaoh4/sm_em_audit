/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;

import java.text.DateFormat;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author menons2
 *
 */
public class PortfolioSearchResultExportDecorator extends
		PortfolioSearchResultDecorator {

	/**
	 * 
	 */
	public PortfolioSearchResultExportDecorator() {
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * Checks if SOD discrepancy exists
	 * 
	 * @return true is discrepancy exists, else false.
	 */
	public String getDiscrepancySod() {
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_SOD);
	}
	
	
	/**
	 * Checks if there NED IC discrepancy exists
	 * 
	 * @return true if discrepancy exists, else false
	 */
	public String getDiscrepancyIc() {
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_IC);
	}
	
	
	/**
	 * Checks if NED Inactive discrepancy exists
	 * 
	 * @return true if discrepancy exists, else false
	 */
	public String getDiscrepancyNedInactive() {
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_NED_INACTIVE);
	}
	
	
	/**
	 * Checks if NED Last Name discrepancy exists
	 * 
	 * @return true if discrepancy exists, else false.
	 */
	public String getDiscrepancyLastName() {
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_LAST_NAME);
	}
	
	
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
	
	
	public String getFullName() {
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		String fullName = accountVO.getNedLastName() + ", " + accountVO.getNedFirstName();
		if(!fullName.replace(", ", "").isEmpty()) {
			return fullName;
		} 
		
		return "";
    }
	
	
	public String getOrgId() {
		
		String orgId = "";
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			orgId = accountRoles.get(0).getOrgId();
		}
		
		return orgId;
    }
	
	public String getApplicationRole() {
		
		String role = "";
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			role = accountRoles.get(0).getRoleName();
		}
		
		return role;
	}
	
	
	public String getRoleCreateOn() {
		String createdOn = "";
		PortfolioAccountVO accountVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			createdOn =  DateFormat.getDateInstance().format(accountRoles.get(0).getCreatedDate());
		}
		
		return createdOn;
	}
	
	
	public String getNotes(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		
		return portfolioVO.getNotes();
	}

}
