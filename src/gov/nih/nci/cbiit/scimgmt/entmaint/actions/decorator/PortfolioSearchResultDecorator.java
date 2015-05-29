package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
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
 * This class is responsible for decorating the rows of portfolio accounts search results table.
 * @author tembharend
 *
 */
@Configurable
public class PortfolioSearchResultDecorator extends TableDecorator{
	
	@Autowired
	private LookupService lookupService;
	
	/**
	 * Get the submitNotes button.
	 * @return submitNotes button UI element.
	 */
	public String getAction(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		String actionStr = "";
		StringBuffer name = new StringBuffer("&nbsp;");
		if(!StringUtils.isBlank(portfolioVO.getNedFirstName())){
			name.append(portfolioVO.getNedFirstName());
		}else{
			name.append(portfolioVO.getImpaciiFirstName());
		}
		if(!StringUtils.isBlank(portfolioVO.getNedLastName())){
			name.append(" "+portfolioVO.getNedLastName());
		}else{
			name.append(" "+portfolioVO.getImpaciiLastName());
		}
		String id = portfolioVO.getImpaciiUserId();
		String note = portfolioVO.getNotes();
		if(note != null && note.length() > 0){
			actionStr = "<div id='action_" + id + "'>" + "<a href=\"javascript:submitNotes('" + name.toString() +"','" + id + "')\" ><img src='../images/commentchecked.gif' alt=\"Add Notes\"/></a>" + "</div>";
		}else{
			actionStr = "<div id='action_" + id + "'>" + "<a href=\"javascript:submitNotes('" + name.toString() +"','" + id + "')\" ><img src='../images/commentunchecked.gif' alt=\"Add Notes\"/></a>" + "</div>";
		}
		return actionStr;
	}
	
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
	 * Get Submitted by user and Submitted on date.
	 * @return string representing submitted by + submitted on date.
	 */
	public String getLastUpdated(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy 'at' h:mm a");
		String lastUpdated = "";
		String id = portfolioVO.getImpaciiUserId();
		if(!StringUtils.isBlank(portfolioVO.getNotesSubmittedByFullName()) && portfolioVO.getNotesSubmittedDate() !=null){
			lastUpdated =  "<div id=\"lastUpdateDiv_"+id+ "\"> Updated on " +dateFormat.format(portfolioVO.getNotesSubmittedDate()) + " by "  +portfolioVO.getNotesSubmittedByFullName() + "</div>";
		}
		else{
			lastUpdated = "<div id=\"lastUpdateDiv_"+id+ "\"> </div>";
		}
		return lastUpdated;
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
		for(String dis : discrepancies){
			EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,dis);
			if(disVw.getShortDescrip() != null){
				//replace all single quote to HTML code
				String longDesc = disVw.getLongDescrip().replace("'", "&#39;");
				//replace all single quote to HTML code
				String resolution = disVw.getResolutionText().replace("'", "&#39;");
				sbu.append(disVw.getShortDescrip() + "&nbsp;<img src='../images/info.png' alt='info' onclick=\"openHelp('help" + id + "');\"/>" + 
						"<input type='hidden' id='help" + id + "' value='" + longDesc + resolution + "'/>" +
						"<br/>");
			}
		}
		return sbu.toString();
	}
	
	/**
	 * This is method displays the deleted date with the format "MM/dd/yyyy"
	 * @return String
	 */
	public String getDeletedDate(){

		String dateString = "";
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		Date deletedDate = portfolioVO.getDeletedDate();
		if(deletedDate != null) {
			dateString = new SimpleDateFormat("MM/dd/yyyy").format(deletedDate);
		}		
		return dateString;
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
	
	public String getDeletedBy(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		String displayStr = "<span title='" + portfolioVO.getDeletedByFullName() + "'>" + portfolioVO.getDeletedByUserId() + "</span>";
		return displayStr;
	}
}
