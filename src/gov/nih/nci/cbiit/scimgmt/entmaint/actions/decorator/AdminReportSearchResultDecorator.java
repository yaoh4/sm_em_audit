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
public class AdminReportSearchResultDecorator extends TableDecorator{
	
	@Autowired
	private LookupService lookupService;
	@Autowired
	protected EntMaintProperties entMaintProperties;
	
	
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
				dateStr = new SimpleDateFormat("MM/dd/yyyy HH:mm a").format(submittedDate);
			} 
			if(eaaVw != null && (eaaVw.getUnsubmittedFlag() == null || eaaVw.getUnsubmittedFlag().equalsIgnoreCase("Y"))){
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
			String roleName = roleVw.getRoleName();
			role = role + "<tr><td>" + roleName + "&nbsp;<img src='"+path +"/images/info.png' alt='info' onclick=\"getRoleDescription('" + roleName + "');\"/></td></tr>";
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
	 * start autowired
	 */
//	private void startAutowired(){
//		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getPageContext()
//				.getServletContext());
//		AutowireCapableBeanFactory acbf = wac.getAutowireCapableBeanFactory();
//		acbf.autowireBean(this);
//	}
}
