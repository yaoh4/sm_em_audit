package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;
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
 * This class is responsible for decorating the rows of discrepancy accounts table in My DOC Discrepancies.
 * @author dinhys
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
		EmPortfolioRolesVw roleVw = (EmPortfolioRolesVw)getCurrentRowObject();
		if(roleVw == null){
			return "";
		}
		String createdBy = roleVw.getCreatedByFullName();
		String roleName = roleVw.getRoleName();
		String role = "";
		if(StringUtils.equalsIgnoreCase(roleVw.getImpaciiUserId(),"I2E")) {
			if(StringUtils.isNotBlank(createdBy) && StringUtils.isNotBlank(roleName)){
				role = "<span title='" + createdBy + "'>" +  roleName + "</span>&nbsp;";
			} else if (StringUtils.isNotBlank(roleVw.getRoleName())) {
				role = roleVw.getRoleName();
			}
		} else {
			role = "<span title='" + createdBy + "'>" + roleName + "</span>&nbsp;<img src='../images/info.png' alt='info' onclick=\"getRoleDescription('" + roleName + "');\"/>";
		}
		return role;
	}
	
	/**
	 * Get the organization ID for this row.
	 * @return orgId
	 */
	public String getOrgId(){
		EmPortfolioRolesVw roleVw = (EmPortfolioRolesVw)getCurrentRowObject();
		if(roleVw == null){
			return "";
		}
		return roleVw.getOrgId();
	}
	
	/**
	 * Get the date on which this role was created.
	 * @return role created date in mm/dd/yyyy format.
	 */
	public String getRoleCreateOn(){
		String createDate = "";
		EmPortfolioRolesVw roleVw = (EmPortfolioRolesVw)getCurrentRowObject();
		if(roleVw == null){
			return "";
		}
		if(roleVw.getCreatedDate() != null){
			createDate = new SimpleDateFormat("MM/dd/yyyy").format(roleVw.getCreatedDate());
		}		
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
		
		String networkId = portfolioVO.getNihNetworkId();
		
		if(networkId == null){
			networkId = "";
		}
		
		String i2e_em_url = entMaintProperties.getPropertyValue(ApplicationConstants.I2E_EM_LINK);
		String i2e_em_link = (StringUtils.isBlank(networkId) ? i2e_em_url : i2e_em_url + "?personPageAction=Find&SEARCH_AGENCY_ID=" + networkId);
		String i2e_em_link_text = entMaintProperties.getPropertyValue(ApplicationConstants.I2E_EM_LINK_TEXT);
		
		if(StringUtils.equalsIgnoreCase(portfolioVO.getNotes(), "I2E"))
			return sbu.toString() + "<a href='" + i2e_em_link + "' target='_BLANK'>" + i2e_em_link_text + "</a>";
		
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
		
		//If name is blank, this is the second row for this account.
		if(StringUtils.isBlank(portfolioVO.getFullName())){
			return "";
		}
		if(impaciiId == null){
			impaciiId = "";
		}
		if(networkId == null){
			networkId = "";
		}
		
		return (StringUtils.isBlank(impaciiId)? "<span title='NIH (Network) ID'>" + networkId + "</span>": "<span title='IMPAC II ID'>" + impaciiId + "</span><br/>" + "<span title='NIH (Network) ID'>" + networkId + "</span>");
	}
	
	/**
	 * Get the name of the person who created the account.
	 * 
	 * @return
	 */
	public String getCreatedBy(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		String displayStr = "<span title='" + portfolioVO.getCreatedByFullName() + "'>" + portfolioVO.getCreatedByUserId() + "</span>";
		return (portfolioVO.getCreatedByUserId() == null? portfolioVO.getCreatedByFullName() : displayStr);
	}
	
	
	/**
	 * Display whether it is an IMPAC II or I2E discrepancy.
	 * 
	 * @return
	 */
	public String getSystem(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		String system = (StringUtils.isBlank(portfolioVO.getNotes()) ? "IMPAC II" : portfolioVO.getNotes());
		return system;	
	}
}
