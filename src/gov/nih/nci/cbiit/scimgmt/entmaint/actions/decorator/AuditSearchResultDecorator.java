package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountActivityVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import org.apache.commons.lang.StringUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * 
 * @author zhoujim
 * This class is used for Audit search result. It modifies results with certain format like time stamp and handles creating a div 
 * for certian actions, such as after complete action, allows JQeury to change contents of table items 
 */
@Configurable
public class AuditSearchResultDecorator extends TableDecorator{
	
	@Autowired
	private LookupService lookupService;
	@Autowired
	protected EntMaintProperties entMaintProperties;
	
	public static final String VERIFIEDACTION = "3";
	public static final String NONEED = "13";
	
	
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
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String parentNedOrgPath = accountVO.getParentNedOrgPath();
//		if(accountVO.getDeletedDate() != null){
//			name = (accountVO.getImpaciiFirstName()==null? "" : accountVO.getImpaciiFirstName()) + " " + (accountVO.getImpaciiLastName()==null? "" : accountVO.getImpaciiLastName());		
//		}else{
//			name = (accountVO.getNedFirstName()==null? "" : accountVO.getNedFirstName()) + " " + (accountVO.getNedLastName()==null? "" : accountVO.getNedLastName());	
//		}
		if(accountVO.getFullName() != null && accountVO.getFullName().length() > 0){
			name=accountVO.getCleanFullName();
		}
		String id = ""+accountVO.getId();
		String actionStr = "";
		String actionId ="";
		String note = "";
		EmAuditAccountActivityVw eaaVw = accountVO.getAccountActivity();
		//check the action has been perfomed.
		if(eaaVw != null && eaaVw.getAction() != null){
			//if yes, intial action text, action id and note
			if(accountVO.getAccountActivity().getAction().getDescription() != null){
				actionStr = accountVO.getAccountActivity().getAction().getDescription();
				actionId = ""+accountVO.getAccountActivity().getAction().getId();
				note = accountVO.getAccountActivity().getNotes();
			}
		}
		//prevent the GUi display null using empty string instead.
		if(note == null){
			note = "";
		}
		
		String actId = getActionId(actionStr);
		String cate = (String)this.getPageContext().getSession().getAttribute(ApplicationConstants.CURRENTPAGE);
		String noteImg = "<a href=\"javascript:fetchAuditNote(" + id + ", '" + cate + "');\"><img src='../images/commentchecked.gif' alt=\"NOTE\"/></a>&nbsp;&nbsp;";
		if(note != null && note.trim().length() > 0 && (eaaVw != null && (eaaVw.getUnsubmittedFlag() == null || eaaVw.getUnsubmittedFlag().equalsIgnoreCase("N")))){
			actionStr = actionStr + "</br>" + noteImg;
		}else{
			actionStr = actionStr + "</br>";
		}
		//if the action record is new or submitted
		if(eaaVw != null && (eaaVw.getUnsubmittedFlag() == null || eaaVw.getUnsubmittedFlag().equalsIgnoreCase("N")) && !ApplicationConstants.ACTION_TRANSFER.equalsIgnoreCase(eaaVw.getAction().getDescription())){
			//check if the user is primary coordinator
			if(nciUser.getCurrentUserRole().equalsIgnoreCase(ApplicationConstants.USER_ROLE_SUPER_USER) && EmAppUtil.isAuditActionEditable(auditId)){
				//if yes, show undo button
				actionStr = "<div id='"+ id +"'>" + actionStr + "<input type=\"button\" onclick=\"unsubmitAct('" + name +"'," + id + ");\" value=\"Undo\"/>" + 
						    "<input type='hidden' id='hiddenAction"+ id + "' value='" + actionId +"' />";
			}
			String era_ua_link =  entMaintProperties.getPropertyValue(ApplicationConstants.ERA_US_LINK);
			if(era_ua_link.equalsIgnoreCase(ApplicationConstants.ERAUA_NA)){
				era_ua_link = "<br/><a href='javascript:openEraua();'>eRA UA</a>";
			}else{
				era_ua_link = "<br/><a href='" + era_ua_link + "' target='_BLANK'>eRA UA</a>";
			}
			String i2e_em_link = entMaintProperties.getPropertyValue(ApplicationConstants.I2E_EM_LINK);
			String currentPage = (String)this.getPageContext().getSession().getAttribute(ApplicationConstants.CURRENTPAGE);
			//if the action is verfiedaction,show two links
			if(actId.equalsIgnoreCase(VERIFIEDACTION) || (ApplicationConstants.CATEGORY_INACTIVE.equalsIgnoreCase(currentPage) && actId.equalsIgnoreCase(NONEED))){
				actionStr = actionStr + era_ua_link +"<br/><a href='" + i2e_em_link + "' target='_BLANK'>I2E EM</a>";
			}
			if(StringUtils.isNotBlank(accountVO.getTransferToNedOrgPath())){
				actionStr = actionStr + "<br/> (Transferred)";
			}
			actionStr = actionStr + "</div>";
		}else{
			//check if the auditing is end or not. If yes, do not show buttons
			actionStr = "<input type='hidden' id='hiddenAction"+ id +"' value='" + actId + "'/>";
			if( eaaVw != null && eaaVw.getAction() != null && ApplicationConstants.ACTION_TRANSFER.equalsIgnoreCase(eaaVw.getAction().getDescription())){
				actionStr = actionStr + "<input type='hidden' id='hiddenTransferredNciOrg"+ id +"' value='" + accountVO.getTransferToNedOrgPath() + "'/>";
			}
			actionStr = "<div id='"+ id +"'>" + actionStr;
			//Calling Aunita's service call to determine if we need to show button or not.
			if(EmAppUtil.isAuditActionEditable(auditId)){
				actionStr = actionStr + "\n<input type=\"button\" onclick=\"submitAct('" + name +"'," + id + ",'" + parentNedOrgPath + "');\" value=\"Complete\"/>";
			}
			if(StringUtils.isNotBlank(accountVO.getTransferToNedOrgPath())){
				actionStr = actionStr + "<br/> (Transferred)";
			}
			actionStr = actionStr + "</div>";
		}
		return actionStr;
	}
	/**
	 * This method is for column discrepancy display.
	 * @return
	 */
	public String getDiscrepancy(){
		startAutowired();
		String path = this.getPageContext().getRequest().getServletContext().getContextPath();
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<String> discrepancies = accountVO.getAccountDiscrepancies();
		StringBuffer sbu = new StringBuffer();
		sbu.append("<ul>");
		for(String dis : discrepancies){
			EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,
					dis);
			String code ="" + disVw.getCode();
			if(disVw.getShortDescrip() != null){
				//replace all single quote to HTML code
				String longDesc = disVw.getLongDescrip().replace("'", "&#39;");
				//replace all single quote to HTML code
				String resolution = disVw.getResolutionText().replace("'", "&#39;");
				sbu.append("<li>" + disVw.getShortDescrip() + "&nbsp;<img src='"+path +"/images/info.png' alt='info' onclick=\"openHelp('help" + code + "');\"/>" + 
						"<input type='hidden' id='help" + code + "' value='" + longDesc + resolution + "'/>" +
						"</li>");
			}
		}
		sbu.append("</ul>");
		return sbu.toString();
	}
	
	/**
	 * This method is for account create date. It make date display as MM/dd/yyyy
	 * @return
	 */
	public String getAccountCreatedDate(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		Date createDate = accountVO.getCreatedDate();
		if(createDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(createDate);
	}
	
	/**
	 * This method is for displaying the user full. The name is hyper link for the email.
	 * @return
	 */
	public String getFullName(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String fullName = accountVO.getFullName();
		String email = accountVO.getNedEmailAddress();
		if(StringUtils.isBlank(fullName)){
			return "";
		}else if(email == null || fullName.trim().length() < 1 || email.trim().length() < 1 ){
			return fullName;
		}else{
			return "<a href='mailto:" + email + "'>" + fullName + "</a>";
		}
	}
	/**
	 * This method is for account submitted on and submitted by.
	 * @return
	 */
	public String getAccountSubmitted(){
		String submittedBy = "";
		Date submittedDate = null;
		
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String id = ""+accountVO.getId();
		
		if(accountVO.getAccountActivity() == null){
			return "<div id='submittedby"+ id +"'></div>";
		}else{
			submittedBy = accountVO.getAccountActivity().getSubmittedByFullName();
			submittedDate = accountVO.getAccountActivity().getSubmittedDate();
		}
		if(submittedBy == null){
			submittedBy = "<div id='submittedby"+ id +"'></div>";
		}else{
			EmAuditAccountActivityVw eaaVw = accountVO.getAccountActivity();
			String dateStr = "";
			if(submittedDate != null){
				dateStr = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a").format(submittedDate);
			} 
			if(eaaVw != null && (eaaVw.getUnsubmittedFlag() == null || eaaVw.getUnsubmittedFlag().equalsIgnoreCase(ApplicationConstants.FLAG_YES))){
				submittedBy = "<div id='submittedby" + id + "'></div>" + "<input type='hidden' id='hiddenSubmittedby" + id +"' value='Submitted on " + dateStr + " by " + submittedBy + "'/>";
			}else{
				submittedBy = "<div id='submittedby"+ id +"'>" + "Submitted on " + dateStr + " by " + submittedBy + "</div>";
			}
		}
		return  submittedBy;
	}
	/**
	 * This is method is for deleted account. It display the deleted date with the format "MM/dd/yyyy"
	 * @return String
	 */
	public String getAccountDeletedDate(){
		Date deletedDate = null;
		
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		deletedDate = accountVO.getDeletedDate();
		if(deletedDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(deletedDate);
		
	}
	/**
	 * This method is for display application roles. The roles can be multiple, each role will be displayed in seperated row.
	 * @return
	 */
	public String getApplicationRole(){
		String path = this.getPageContext().getRequest().getServletContext().getContextPath();
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> roles = accountVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String role = "<table width='100%' border='0'>";
		for(EmAuditAccountRolesVw roleVw : roles){
			String createdBy = roleVw.getCreatedByFullName();
			String roleName = roleVw.getRoleName();
			role = role + "<tr><td>" + "<span title='" + createdBy + "'>" + roleName + "</span>"  + "&nbsp;<img src='"+path +"/images/info.png' alt='info' onclick=\"getRoleDescription('" + roleName + "');\"/></td></tr>";
		}
		role = role + "</table>";
		return role;
	}
	
	/**
	 * This method is for displaying organization ID for application roles. It could be multiple.
	 * @return String
	 */
	public String getOrgId(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> roles = accountVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String orgId = "<table width='100%' border='0'>";
		for(EmAuditAccountRolesVw roleVw : roles){
			orgId = orgId + "<tr><td>" + roleVw.getOrgId()+"</td></tr>";
		}
		orgId = orgId + "</table>";
		return orgId;
	}
	/**
	 * This method id for displaying role created time. It could be multiple
	 * @return
	 */
	public String getRoleCreateOn(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> roles = accountVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String createDate = "<table width='100%' border='0'>";
		for(EmAuditAccountRolesVw roleVw : roles){
			createDate = createDate + "<tr><td>" + new SimpleDateFormat("MM/dd/yyyy").format(roleVw.getCreatedDate()) + "</td></tr>";
		}
		createDate = createDate + "</table>";
		return createDate;
	}
	/**
	 * This method is for displaying the action note. If it is unsubmitted, the action note must be hidden.
	 * @return
	 */
	private String getActionNote(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String id = "note"+accountVO.getId();
		String note = "";
		if(accountVO.getAccountActivity() != null){
			note = accountVO.getAccountActivity().getNotes();
		}
		if(note == null){
			note = "";
		}
		return note;
	}

	public String getImpaciiUserIdNetworkId(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String impaciiId = accountVO.getImpaciiUserId();
		String networkId = accountVO.getNihNetworkId();
		
		if(impaciiId == null){
			impaciiId = "";
		}
		if(networkId == null){
			networkId = "";
		}
		
		return "<span title='IMPAC II ID'>" + impaciiId + "</span><br/>" + "<span title='NIH (Network) ID'>" + networkId + "</span>";
	}
	
	public String getCreatedBy(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String displayStr = "<span title='" + accountVO.getCreatedByFullName() + "'>" + accountVO.getCreatedByUserId() + "</span>";
		return displayStr;
	}

	public String getDeletedBy(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String displayStr = "<span title='" + accountVO.getDeletedByFullName() + "'>" + accountVO.getDeletedByUserId() + "</span>";
		return displayStr;
	}
	
	
	/**
	 * This is help method to convert action label to action ID
	 * @param label
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getActionId(String label){
		if(ApplicationConstants.ACTION_TRANSFER.equalsIgnoreCase(label)){
			label = ApplicationConstants.SEARCH_TRANSFERED;
		}
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
