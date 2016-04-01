package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import org.apache.commons.lang3.StringUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class is responsible for decorating the rows of I2E audit accounts search results table.
 * @author 
 *
 */
@Configurable
public class I2eAuditSearchResultDecorator extends TableDecorator{
	
	@Autowired
	private LookupService lookupService;
	@Autowired
	protected EntMaintProperties entMaintProperties;
	
	public static final String VERIFIEDACTION = "3";
	public static final String NONEED = "13";
	
	/**
	 * Get the full name.
	 * @return fullName
	 */
	public String getFullName() {
		AuditI2eAccountVO auditVO = (AuditI2eAccountVO)getCurrentRowObject();
		String fullName = "";
		if(!StringUtils.isBlank(auditVO.getFullName())){
			if(!StringUtils.isBlank(auditVO.getNedEmailAddress())){
				fullName =  "<a href=\"mailto:" + auditVO.getNedEmailAddress() +  "\">" + auditVO.getFullName() + "</a>";
			}
			else{
				fullName = auditVO.getFullName();
			}
		}
		return fullName;
	}
	
	/**
	 * Get I2E created date
	 * @return created Date
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
	 * Get Discrepancy and help icon
	 * @return discrepancy
	 */
	public String getDiscrepancy(){
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getPageContext().getServletContext());
		AutowireCapableBeanFactory acbf = wac.getAutowireCapableBeanFactory();
		acbf.autowireBean(this);
		AuditI2eAccountVO auditVO = (AuditI2eAccountVO)getCurrentRowObject();
		List<String> discrepancies = auditVO.getAccountDiscrepancies();

		String id = ""+auditVO.getNpnId();
		StringBuffer sbu = new StringBuffer();
		sbu.append("<ul>");
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
		
		return sbu.toString();
	}
	
	/**
	 * Get application role for this row.
	 * @return applicationRole string with info icon.
	 */
	public String getActiveI2eRole(){
		String role = "";
		EmI2eAuditAccountRolesVw roleVw = (EmI2eAuditAccountRolesVw)getCurrentRowObject();	
		if(StringUtils.isNotBlank(roleVw.getCreatedByFullName()) && StringUtils.isNotBlank(roleVw.getRoleDescription())){
			role = "<span title='" + roleVw.getCreatedByFullName() + "'>" +  roleVw.getRoleDescription() + "</span>&nbsp;";
		} else if(StringUtils.isNotBlank(roleVw.getRoleDescription())) {
			role = roleVw.getRoleDescription();
		}
		return role;
	}
	
	/**
	 * Get the date on which this role was created.
	 * @return role created date in mm/dd/yyyy format.
	 */
	public String getRoleCreateOn(){
		String createDate = "";
		EmI2eAuditAccountRolesVw roleVw = (EmI2eAuditAccountRolesVw)getCurrentRowObject();
		if(roleVw.getCreatedDate() != null){
			createDate = new SimpleDateFormat("MM/dd/yyyy").format(roleVw.getCreatedDate());
		}		
		return createDate;	
	}
	
	/**
	 * This method is for creating action contents. 
	 * @return String
	 */
	public String getAction(){
		String name = "";
	
		startAutowired();
		NciUser nciUser = (NciUser)this.getPageContext().getSession().getAttribute(ApplicationConstants.SESSION_USER);
		AuditSearchVO searchVO = (AuditSearchVO)this.getPageContext().getSession().getAttribute(ApplicationConstants.SEARCHVO);
		Long auditId = searchVO.getAuditId();
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		if(accountVO.getFullName() != null && accountVO.getFullName().length() > 0){
			name=accountVO.getCleanFullName();
		}
		String id = ""+accountVO.getId();
		String actionStr = "";
		String actionId ="";
		String note = "";
		//check the action has been performed.
		if(accountVO != null && accountVO.getAction() != null){
			//if yes, initial action text, action id and note
			if(accountVO.getAction().getDescription() != null){
				actionStr = accountVO.getAction().getDescription();
				actionId = ""+accountVO.getAction().getId();
				note = accountVO.getNotes();
			}
		}
		//prevent the GUi display null using empty string instead.
		if(note == null){
			note = "";
		}
		
		String actId = getActionId(actionStr);
		String noteImg = "<a href=\"javascript:fetchAuditNote(" + id + ");\"><img src='../images/commentchecked.gif' alt=\"NOTE\"/></a>&nbsp;&nbsp;";
		if(note != null && note.trim().length() > 0 && (accountVO != null && (accountVO.getUnsubmittedFlag() == null || accountVO.getUnsubmittedFlag().equalsIgnoreCase("N")))){
			actionStr = actionStr + "</br>" + noteImg;
		}else{
			actionStr = actionStr + "</br>";
		}
		//if the action record is new or submitted
		if(accountVO.getAction() != null && (accountVO.getUnsubmittedFlag() == null || accountVO.getUnsubmittedFlag().equalsIgnoreCase("N"))){
			//check if the user is primary coordinator
			if(nciUser.getCurrentUserRole().equalsIgnoreCase(ApplicationConstants.USER_ROLE_SUPER_USER) && EmAppUtil.isAuditActionEditable(auditId)){
				//if yes, show undo button
				actionStr = "<div id='"+ id +"'>" + actionStr + "<input type=\"button\" onclick=\"unsubmitAct('" + name +"'," + id + ");\" value=\"Undo\"/>" + 
						    "<input type='hidden' id='hiddenAction"+ id + "' value='" + actionId +"' />";
			}
			String era_ua_link =  entMaintProperties.getPropertyValue(ApplicationConstants.ERA_US_LINK);
			String era_ua_link_text =  entMaintProperties.getPropertyValue(ApplicationConstants.ERA_US_LINK_TEXT);
			if(era_ua_link.equalsIgnoreCase(ApplicationConstants.ERAUA_NA)){
				era_ua_link = "<br/><a href='javascript:openEraua();'>" + era_ua_link_text + "</a>";
			}else{
				era_ua_link = "<br/><a href='" + era_ua_link + "' target='_BLANK'>" + era_ua_link_text + "</a>";
			}
			String i2e_em_link = entMaintProperties.getPropertyValue(ApplicationConstants.I2E_EM_LINK);
			String i2e_em_link_text = entMaintProperties.getPropertyValue(ApplicationConstants.I2E_EM_LINK_TEXT);
			//if the action is verifiedaction,show two links
			if(actId.equalsIgnoreCase(VERIFIEDACTION)){
				actionStr = actionStr + era_ua_link +"<br/><a href='" + i2e_em_link + "' target='_BLANK'>" + i2e_em_link_text + "</a>";
			}
			actionStr = actionStr + "</div>";
		}else{
			//check if the auditing is end or not. If yes, do not show buttons
			actionStr = "<input type='hidden' id='hiddenAction"+ id +"' value='" + actId + "'/>";
			actionStr = "<div id='"+ id +"'>" + actionStr;
			//Calling service call to determine if we need to show button or not.
			if(EmAppUtil.isAuditActionEditable(auditId)){
				actionStr = actionStr + "\n<input type=\"button\" onclick=\"submitAct('" + name +"'," + id + ");\" value=\"Complete\"/>";
			}
			actionStr = actionStr + "</div>";
		}
		return actionStr;
	}
	
	/**
	 * This method is for displaying the action note. If it is unsubmitted, the action note must be hidden.
	 * @return
	 */
	public String getActionNote(){
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		String id = "note"+accountVO.getId();
		String note = "";
		if(accountVO.getUnsubmittedFlag() == null || StringUtils.equalsIgnoreCase(accountVO.getUnsubmittedFlag(), ApplicationConstants.FLAG_YES)){
			note = accountVO.getNotes();
		}
		if(note == null){
			note = "";
		}
		return note;
	}
	
	/**
	 * Get Submitted by user and Submitted on date.
	 * @return string representing submitted by + submitted on date.
	 */
	public String getLastUpdated(){
		AuditI2eAccountVO auditVO = (AuditI2eAccountVO)getCurrentRowObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy 'at' h:mm a");
		String lastUpdated = "";
		String id = ""+auditVO.getNpnId();
		if( auditVO.getNpnId() != null && auditVO.getSubmittedDate() !=null){
			lastUpdated =  "<div id=\"lastUpdateDiv_"+id+ "\"> Updated on " +dateFormat.format(auditVO.getSubmittedDate()) + " by "  +auditVO.getSubmittedBy() + "</div>";
		}
		else{
			lastUpdated = "<div id=\"lastUpdateDiv_"+id+ "\"> </div>";
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
			if(accountVO != null && (accountVO.getUnsubmittedFlag() == null || accountVO.getUnsubmittedFlag().equalsIgnoreCase(ApplicationConstants.FLAG_YES))){
				submittedBy = "<div id='submittedby" + id + "'></div>" + "<input type='hidden' id='hiddenSubmittedby" + id +"' value='Submitted on " + dateStr + " by " + submittedBy + "'/>";
			}else{
				submittedBy = "<div id='submittedby"+ id +"'>" + "Submitted on " + dateStr + " by " + submittedBy + "</div>";
			}
		}
		return  submittedBy;
	}
	
	/**
	 * This method is for displaying Org path for application roles. It could be multiple.
	 * @return String
	 */
	public String getOrgPath(){
		String orgPath = "";
		EmI2eAuditAccountRolesVw roleVw = (EmI2eAuditAccountRolesVw)getCurrentRowObject();
		if(StringUtils.isNotBlank(roleVw.getFullOrgPathAbbrev())){
			int beginIndex = roleVw.getFullOrgPathAbbrev().lastIndexOf("/");
			String lastOrg = (beginIndex > 0 ?  roleVw.getFullOrgPathAbbrev().substring(beginIndex + 1) : roleVw.getFullOrgPathAbbrev());
			orgPath = "<span title='" + roleVw.getFullOrgPathAbbrev() + "'>" + lastOrg + "</span>";			
		}
		return orgPath;
	}
	
	/**
	 * This is help method to convert action label to action ID
	 * @param label
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getActionId(String label){
		String id = "";
		List<DropDownOption> actions = (List<DropDownOption>)this.getPageContext().getSession().getAttribute(ApplicationConstants.ACTIONLIST);
		for(DropDownOption ddo : actions){
			if (ddo.getOptionValue() != null && ddo.getOptionValue().equalsIgnoreCase(label)){
				id = ddo.getOptionKey();
				break;
			}
		}
		return id;
	}
	
	/**
	 * start autowired
	 */
	private void startAutowired(){
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getPageContext()
				.getServletContext());
		AutowireCapableBeanFactory acbf = wac.getAutowireCapableBeanFactory();
		acbf.autowireBean(this);
	}
}
