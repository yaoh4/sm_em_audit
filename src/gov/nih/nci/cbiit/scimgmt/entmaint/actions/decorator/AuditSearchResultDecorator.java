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
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;

import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Configurable
public class AuditSearchResultDecorator extends TableDecorator{
	
	@Autowired
	private LookupService lookupService;
	
	public String getAction(){
		NciUser nciUser = (NciUser)this.getPageContext().getSession().getAttribute(ApplicationConstants.SESSION_USER);
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String name = accountVO.getNedFirstName() + " " + accountVO.getNedLastName();
		String id = ""+accountVO.getId();
		String actionStr = "";
		String actionId ="";
		String note = "";
		EmAuditAccountActivityVw eaaVw = accountVO.getAccountActivity();
		
		if(eaaVw != null && eaaVw.getAction() != null){
			if(accountVO.getAccountActivity().getAction().getDescription() != null){
				actionStr = accountVO.getAccountActivity().getAction().getDescription();
				actionId = ""+accountVO.getAccountActivity().getAction().getId();
				note = accountVO.getAccountActivity().getNotes();
			}
		}
		
		if(eaaVw != null && (eaaVw.getUnsubmittedFlag() == null || eaaVw.getUnsubmittedFlag().equalsIgnoreCase("N"))){
			if(nciUser.getCurrentUserRole().equalsIgnoreCase(ApplicationConstants.USER_ROLE_SUPER_USER)){
				actionStr = "<div id='"+ id +"'>" + actionStr + "<input type=\"button\" onclick=\"unsubmitAct('" + name +"'," + id + ");\" value=\"Unsubmit\"/>" + 
						    "<input type='hidden' id='hiddenAction"+ id + "' value='" + actionId +"' /> <input type='hidden' id='hiddennote" + id +"' value='" + note +"'/>" +
						    "</div>";
			}
		}else{
			//check if the auditing is end or not. If yes, do not show buttons
			actionStr = "<input type='hidden' id='hiddenAction"+ id +"' value='" + getActionId(actionStr) + "'/>";
			actionStr = "<div id='"+ id +"'>" + actionStr;
			if(EmAppUtil.isAuditEnabled()){
				actionStr = actionStr + "\n<input type=\"button\" onclick=\"submitAct('" + name +"'," + id + ");\" value=\"Submit\"/>" + "</div>";
			}
		}
		return actionStr;
	}
	
	public String getDiscrepancy(){
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getPageContext()
				.getServletContext());
		AutowireCapableBeanFactory acbf = wac.getAutowireCapableBeanFactory();
		acbf.autowireBean(this);
		String path = this.getPageContext().getRequest().getServletContext().getContextPath();
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String id = ""+accountVO.getId();
		List<String> discrepancies = accountVO.getAccountDiscrepancies();
		StringBuffer sbu = new StringBuffer();
		for(String dis : discrepancies){
			EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,
					dis);
			if(disVw.getShortDescrip() != null){
				String longDesc = disVw.getLongDescrip().replace("'", "&#39;");
				String resolution = disVw.getResolutionText().replace("'", "&#39;");
				sbu.append(disVw.getShortDescrip() + "&nbsp;<img src='"+path +"/images/info.png' alt='info' onclick=\"openHelp('help" + id + "');\"/>" + 
						"<input type='hidden' id='help" + id + "' value='" + longDesc + resolution + "'/>" +
						"<br/>");
			}
		}
		return sbu.toString();
	}
	
	public String getAccountCreatedDate(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		Date createDate = accountVO.getCreatedDate();
		if(createDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(createDate);
	}
	
	public String getFullName(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String fullName = accountVO.getFullName();
		String email = accountVO.getNedEmailAddress();
		return "<a href='mailto:" + email + "'>" + fullName + "</a>";
	}
	
	public String getAccountSubmittedDate(){
		Date submittedDate = null;
		
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		if(accountVO.getAccountActivity() == null){
			return "";
		}else{
			submittedDate = accountVO.getAccountActivity().getSubmittedDate();
		}
		
		return new SimpleDateFormat("MM/dd/yyyy").format(submittedDate);
		
	}
	public String getAccountDeletedDate(){
		Date deletedDate = null;
		
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		deletedDate = accountVO.getDeletedDate();
		if(deletedDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(deletedDate);
		
	}
	public String getApplicationRole(){
		String path = this.getPageContext().getRequest().getServletContext().getContextPath();
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> roles = accountVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String role = "<table width='100%' border='0'>";
		for(EmAuditAccountRolesVw roleVw : roles){
			String roleName = roleVw.getRoleName();
			role = role + "<tr><td>" + roleName + "&nbsp;<img src='"+path +"/images/info.png' alt='info' onclick=\"getRoleDescription('" + roleName + "');\"/></td></tr>";
		}
		role = role + "</table>";
		return role;
	}
	
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
	
	public String getActionNote(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String id = "note"+accountVO.getId();
		String note = "";
		if(accountVO.getAccountActivity() != null){
			note = accountVO.getAccountActivity().getNotes();
		}
		EmAuditAccountActivityVw eaaVw = accountVO.getAccountActivity();
		if(eaaVw != null && (eaaVw.getUnsubmittedFlag() == null || eaaVw.getUnsubmittedFlag().equalsIgnoreCase("Y"))){
			note = "<input type='hidden' id='hidden" + id +"' value='" + note+ "'/>";
		}
		String completedNote = "<div id='" + id +"'>" + note + "</div>";
		
		return completedNote;
	}
	
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

}
