package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.I2eActiveUserRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

import org.apache.commons.lang3.StringUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class is responsible for decorating the rows of I2E portfolio accounts search results table.
 * @author Jim Zhou
 *
 */
@Configurable
public class I2ePortfolioSearchResultDecorator extends TableDecorator{
	
	@Autowired
	private LookupService lookupService;
	
	/**
	 * Get the full name.
	 * @return fullName
	 */
	public String getFullName() {
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		String fullName = "";
		if(!StringUtils.isBlank(portfolioVO.getFullName())){
			if(!StringUtils.isBlank(portfolioVO.getNedEmailAddress())){
				fullName =  "<a href=\"mailto:" + portfolioVO.getNedEmailAddress() +  "\">" + portfolioVO.getFullName() + "</a>";
			}
			else{
				fullName = portfolioVO.getFullName();
			}
		}
		return fullName;
	}
	
	public String getI2eCreatedDate(){
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		Date createDate = portfolioVO.getCreatedDate();
		if(createDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(createDate);
	}
	
	public String getDiscrepancy(){
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getPageContext().getServletContext());
		AutowireCapableBeanFactory acbf = wac.getAutowireCapableBeanFactory();
		acbf.autowireBean(this);
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		List<String> discrepancies = portfolioVO.getAccountDiscrepancies();

		String id = ""+portfolioVO.getNpnId();
		StringBuffer sbu = new StringBuffer();
		for(String s : discrepancies){
			EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,s);
			if(disVw.getShortDescrip() != null){
				//replace all single quote to HTML code
				String secondId = disVw.getCode();
				String longDesc = disVw.getLongDescrip().replace("'", "&#39;");
				//replace all single quote to HTML code
				String resolution = disVw.getResolutionText().replace("'", "&#39;");
				sbu.append(disVw.getShortDescrip() + "&nbsp;<img src='../images/info.png' alt='info' onclick=\"openHelp('help" + id + secondId + "');\"/>" + 
						"<input type='hidden' id='help" + id + secondId + "' value='" + longDesc + resolution + "'/>" +
						"<br/>");
			}
		}
		
		return sbu.toString();
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
		String role = "<table width='100%' border='0'>";
		for(I2eActiveUserRolesVw roleVw : roles){
			//String createdBy = roleVw.getCreatedByFullName();
			String createdBy = "Jim Zhou";
			String roleName = roleVw.getRoleName();
			role = role + "<tr><td><span title='" + createdBy + "'>" + roleName + "</span>&nbsp;<img src='../images/info.png' alt='info' onclick=\"getI2eRoleDescription('" + roleName + "');\"/></td></tr>";
		}
		role = role + "</table>";
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
		String createDate = "<table width='100%' border='0'>";
		for(I2eActiveUserRolesVw roleVw : roles){
			createDate = createDate + "<tr><td>" + new SimpleDateFormat("MM/dd/yyyy").format(roleVw.getRoleCreatedDate()) + "</td></tr>";
		}
		createDate = createDate + "</table>";
		return createDate;
	}
	
}
