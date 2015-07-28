package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.I2eActiveUserRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * This class is responsible for decorating the rows of I2E portfolio accounts search results table.
 * @author Jim Zhou
 *
 */
@Configurable
public class I2ePortfolioSearchResultExportDecorator extends TableDecorator{
	
	/**
	 * Get the full name.
	 * @return fullName
	 */
	public String getFullName() {
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		return portfolioVO.getFullName();
	}
	
	/**
	 * get created date	
	 * @return created date in string
	 */
	public String getI2eCreatedDate(){
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		Date createDate = portfolioVO.getCreatedDate();
		if(createDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(createDate);
	}
	
	/**
	 * Get note contents
	 * @return note
	 */
	public String getAction(){
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		String note = portfolioVO.getNotes();
		return note;
	}
	
	/**
	 * Get Submitted by user and Submitted on date.
	 * @return string representing submitted by + submitted on date.
	 */
	public String getLastUpdated(){
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy 'at' h:mm a");
		String lastUpdated = "";
		if( portfolioVO.getNpnId() != null && portfolioVO.getNotesSubmittedDate() !=null){
			lastUpdated =  "Updated on " +dateFormat.format(portfolioVO.getNotesSubmittedDate()) + " by "  +portfolioVO.getNotesSubmittedByFullName();
		}
		else{
			lastUpdated = "";
		}
		return lastUpdated;
	}
	
	/**
	 * Get application role for this row.
	 * @return applicationRole string with info icon.
	 */
	public String getActiveI2eRole(){
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		List<I2eActiveUserRolesVw> roles = portfolioVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String role = "";
		I2eActiveUserRolesVw roleVw = roles.get(0);
		role = roleVw.getRoleName();

		return role;
	}
	
	/**
	 * Get the date on which this role was created.
	 * @return role created date in mm/dd/yyyy format.
	 */
	public String getRoleCreateOn(){
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		List<I2eActiveUserRolesVw> roles = portfolioVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String createDate = "";
		I2eActiveUserRolesVw roleVw = roles.get(0);
		createDate = new SimpleDateFormat("MM/dd/yyyy").format(roleVw.getRoleCreatedDate());

		return createDate;
	}
	
	/**
	 * This method is for displaying Org path for application roles. It could be multiple.
	 * @return String
	 */
	public String getOrgPath(){
		String orgPath = "";
		PortfolioI2eAccountVO accountVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		List<I2eActiveUserRolesVw> accountRoles = accountVO.getAccountRoles();
		if(!CollectionUtils.isEmpty(accountRoles)) {
			orgPath = accountRoles.get(0).getFullOrgPathAbbrev();
		}
		
		return orgPath;
	}
	
	/**
	 * Checks if SOD discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancySod() {
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(portfolioVO, ApplicationConstants.DISCREPANCY_CODE_I2E_SOD);
	}
	
	/**
	 * Checks if NED Inactive discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyNedInactive() {
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(portfolioVO, ApplicationConstants.DISCREPANCY_CODE_I2E_NED_INACTIVE);
	}
	
	/**
	 * Checks if Inactive I2E Role(s) discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyInactiveI2eRole() {
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(portfolioVO, ApplicationConstants.DISCREPANCY_CODE_I2E_NO_ACTIVE_ROLE);
	}
	
	/**
	 * Checks if Inactive IMPAC II discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyI2eOnly() {
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(portfolioVO, ApplicationConstants.DISCREPANCY_CODE_I2E_ONLY);
	}
	
	/**
	 * Checks if I2e Inactive discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyI2eInactive() {
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(portfolioVO, ApplicationConstants.DISCREPANCY_CODE_I2E_ACTIVE_ROLE_REMAINDER);
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
	private String isDiscrepancy(PortfolioI2eAccountVO portfolioVO, String type) {
		String isDiscrepancy = "";
		
		List<String> discrepancies = portfolioVO.getAccountDiscrepancies();
		if(!CollectionUtils.isEmpty(discrepancies)) {
			for(String discrepancy: discrepancies) {
				if(discrepancy.equals(type)) {
					return "Y";
				}
			}
		}
		return isDiscrepancy;
	}
	
}
