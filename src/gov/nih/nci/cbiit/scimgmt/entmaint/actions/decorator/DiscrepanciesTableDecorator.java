package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.I2eActiveUserRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class is responsible for decorating the rows of portfolio accounts search results table.
 * @author tembharend
 *
 */
@Configurable
public class DiscrepanciesTableDecorator extends TableDecorator{
	
	@Autowired
	private LookupService lookupService;
	@Autowired
	protected EntMaintProperties entMaintProperties;
	
	/**
	 *  Get the date on which this account was created
	 * @return accountCreatedDate in mm/dd/yyyy format.
	 */
	public String getAccountCreatedDate(){
		String dateString = "";
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		Date createDate = portfolioVO.getCreatedDate();
		if(createDate != null) {
			dateString = new SimpleDateFormat("MM/dd/yyyy").format(createDate);
		}		
		return dateString;
	}
	
	/**
	 * Get application role for this row.
	 * @return applicationRole string with info icon.
	 */
	public String getApplicationRole(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> roles = portfolioVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String role = "<table width='100%' border='0'>";
		for(EmPortfolioRolesVw roleVw : roles){
			String createdBy = roleVw.getCreatedByFullName();
			String roleName = roleVw.getRoleName();
			role = role + "<tr><td><span title='" + createdBy + "'>" + roleName + "</span>&nbsp;<img src='../images/info.png' alt='info' onclick=\"getRoleDescription('" + roleName + "');\"/></td></tr>";
		}
		role = role + "</table>";
		return role;
	}
	
	/**
	 * Get the organization ID for this row.
	 * @return orgId
	 */
	public String getOrgId(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> roles = portfolioVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String orgId = "<table width='100%' border='0'>";
		for(EmPortfolioRolesVw roleVw : roles){
			orgId = orgId + "<tr><td>" + roleVw.getOrgId()+"</td></tr>";
		}
		orgId = orgId + "</table>";
		return orgId;
	}
	
	/**
	 * Get the date on which this role was created.
	 * @return role created date in mm/dd/yyyy format.
	 */
	public String getRoleCreateOn(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> roles = portfolioVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String createDate = "<table width='100%' border='0'>";
		for(EmPortfolioRolesVw roleVw : roles){
			createDate = createDate + "<tr><td>" + new SimpleDateFormat("MM/dd/yyyy").format(roleVw.getCreatedDate()) + "</td></tr>";
		}
		createDate = createDate + "</table>";
		return createDate;
	}
	
	/**
	 * Get the full name.
	 * @return fullName
	 */
	public String getFullName() {
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
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
	
	/**
	 * Get discrepancy for this account.
	 * @return discrepancy text with info icon.
	 */
	public String getDiscrepancy(){
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getPageContext().getServletContext());
		AutowireCapableBeanFactory acbf = wac.getAutowireCapableBeanFactory();
		acbf.autowireBean(this);
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		List<String> discrepancies = portfolioVO.getAccountDiscrepancies();
		String id = portfolioVO.getImpaciiUserId();
		StringBuffer sbu = new StringBuffer();
		sbu.append("<ul style='padding-bottom: 0px; margin-bottom: 0px;'>");
		for(String dis : discrepancies){
			EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,dis);
			if(disVw.getShortDescrip() != null){
				//replace all single quote to HTML code
				String secondId = disVw.getCode();
				String longDesc = disVw.getLongDescrip().replace("'", "&#39;");
				//replace all single quote to HTML code
				String resolution = disVw.getResolutionText().replace("'", "&#39;");
				sbu.append("<li>" + disVw.getShortDescrip() + "&nbsp;<img src='../images/info.png' alt='info' onclick=\"openHelp('help" + id + secondId + "');\"/>" + 
						"<input type='hidden' id='help" + id + secondId + "' value='" + longDesc + resolution + "'/>" +
						"</li>");
			}
		}
		sbu.append("</ul>");
		
		String impaciiId = portfolioVO.getImpaciiUserId();
		if(impaciiId == null){
			impaciiId = "";
		}
		
		String era_ua_url = entMaintProperties.getPropertyValue(ApplicationConstants.ERA_US_LINK);
		String era_ua_link =  (StringUtils.isBlank(impaciiId) ? era_ua_url : era_ua_url + "accounts/manage.era?accountType=NIH&userId=" + impaciiId);
		String era_ua_link_text =  entMaintProperties.getPropertyValue(ApplicationConstants.ERA_US_LINK_TEXT);
		if(era_ua_url.equalsIgnoreCase(ApplicationConstants.ERAUA_NA)){
			era_ua_link = "<a href='javascript:openEraua();'>" + era_ua_link_text + "</a>";
		}else{
			era_ua_link = "<a href='" + era_ua_link + "' target='_BLANK'>" + era_ua_link_text + "</a>";
		}
		
		return sbu.toString() + era_ua_link;
	}
	
	/**
	 * This method displays the impaciiUserId and network id
	 * @return
	 */
	public String getImpaciiUserIdNetworkId(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		String impaciiId = portfolioVO.getImpaciiUserId();
		String networkId = portfolioVO.getNihNetworkId();
		
		if(impaciiId == null){
			impaciiId = "";
		}
		if(networkId == null){
			networkId = "";
		}
		
		return "<span title='IMPAC II ID'>" + impaciiId + "</span><br/>" + "<span title='NIH (Network) ID'>" + networkId + "</span>";
	}
	
	public String getCreatedBy(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		String displayStr = "<span title='" + portfolioVO.getCreatedByFullName() + "'>" + portfolioVO.getCreatedByUserId() + "</span>";
		return displayStr;
	}
	
	/**
	 * Get Discrepancy and help icon
	 * @return descrepancy
	 */
	public String getI2eDiscrepancy(){
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getPageContext().getServletContext());
		AutowireCapableBeanFactory acbf = wac.getAutowireCapableBeanFactory();
		acbf.autowireBean(this);
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		List<String> discrepancies = portfolioVO.getAccountDiscrepancies();

		String id = ""+portfolioVO.getNpnId();
		StringBuffer sbu = new StringBuffer();
		sbu.append("<ul style='padding-bottom: 0px; margin-bottom: 0px;'>");
		for(String s : discrepancies){
			EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,s);
			if(disVw.getShortDescrip() != null){
				//replace all single quote to HTML code
				String secondId = disVw.getCode();
				String longDesc = disVw.getLongDescrip().replace("'", "&#39;");
				//replace all single quote to HTML code
				String resolution = disVw.getResolutionText().replace("'", "&#39;");
				sbu.append("<li>" + disVw.getShortDescrip() + "&nbsp;<img src='../images/info.png' alt='info' onclick=\"openHelp('help" + id + secondId + "');\"/>" + 
						"<input type='hidden' id='help" + id + secondId + "' value='" + longDesc + resolution + "'/>" +
						"</li>");
			}
		}
		sbu.append("</ul>");
		String networkId = portfolioVO.getNihNetworkId();
		
		if(networkId == null){
			networkId = "";
		}
		
		String i2e_em_url = entMaintProperties.getPropertyValue(ApplicationConstants.I2E_EM_LINK);
		String i2e_em_link = (StringUtils.isBlank(networkId) ? i2e_em_url : i2e_em_url + "?personPageAction=Find&SEARCH_AGENCY_ID=" + networkId);
		String i2e_em_link_text = entMaintProperties.getPropertyValue(ApplicationConstants.I2E_EM_LINK_TEXT);
		
		return sbu.toString() + "<a href='" + i2e_em_link + "' target='_BLANK'>" + i2e_em_link_text + "</a>";
	}

	/**
	 * Get the full name.
	 * @return fullName
	 */
	public String getI2eFullName() {
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
	
	/**
	 * Get I2E created date
	 * @return created Date
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
	 * This method is for displaying Org path for application roles. It could be multiple.
	 * @return String
	 */
	public String getOrgPath(){
		String orgPath = "";
		I2eActiveUserRolesVw roleVw = (I2eActiveUserRolesVw)getCurrentRowObject();
		if(StringUtils.isNotBlank(roleVw.getFullOrgPathAbbrev())){
			int beginIndex = roleVw.getFullOrgPathAbbrev().lastIndexOf("/");
			String lastOrg = (beginIndex > 0 ?  roleVw.getFullOrgPathAbbrev().substring(beginIndex + 1) : roleVw.getFullOrgPathAbbrev());
			orgPath = "<span title='" + roleVw.getFullOrgPathAbbrev() + "'>" + lastOrg + "</span>";
		}
		return orgPath;
	}
	
	/**
	 * Get application role for this row.
	 * @return applicationRole string with info icon.
	 */
	public String getActiveI2eRole(){
		String role = "";
		I2eActiveUserRolesVw roleVw = (I2eActiveUserRolesVw)getCurrentRowObject();		
		if(StringUtils.isNotBlank(roleVw.getRoleCreatedByFullName()) && StringUtils.isNotBlank(roleVw.getRoleName())){
			role = "<span title='" + roleVw.getRoleCreatedByFullName() + "'>" +  roleVw.getRoleName() + "</span>&nbsp;";
		} else if (StringUtils.isNotBlank(roleVw.getRoleName())) {
			role = roleVw.getRoleName();
		}
		return role;
	}
	
	/**
	 * This method displays the impaciiUserId and network id
	 * @return
	 */
	public String getNihNetworkId(){
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		String networkId = portfolioVO.getNihNetworkId();
		
		if(networkId == null){
			networkId = "";
		}
		return "<span title='NIH (Network) ID'>" + networkId + "</span>";
	}
	
	/**
	 * Get the date on which this role was created.
	 * @return role created date in mm/dd/yyyy format.
	 */
	public String getI2eRoleCreateOn(){
		String createDate = "";
		I2eActiveUserRolesVw roleVw = (I2eActiveUserRolesVw)getCurrentRowObject();
		if(roleVw.getRoleCreatedDate() != null){
			createDate = new SimpleDateFormat("MM/dd/yyyy").format(roleVw.getRoleCreatedDate());
		}		
		return createDate;	
	}
}
