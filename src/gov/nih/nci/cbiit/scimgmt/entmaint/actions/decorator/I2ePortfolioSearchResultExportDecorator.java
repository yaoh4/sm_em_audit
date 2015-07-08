package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.I2eActiveUserRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

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
	
	public String getI2eCreatedDate(){
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		Date createDate = portfolioVO.getCreatedDate();
		if(createDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(createDate);
	}
	
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
	
}
