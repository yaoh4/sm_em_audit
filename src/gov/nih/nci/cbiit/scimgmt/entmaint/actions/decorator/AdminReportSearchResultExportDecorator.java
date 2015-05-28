package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountActivityVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;

import org.apache.commons.lang.StringUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
/**
 * 
 * @author zhoujim
 * This class is used for Audit search result. It modifies results with certain format like time stamp and handles creating a div 
 * for certian actions, such as after complete action, allows JQeury to change contents of table items 
 */
@Configurable
public class AdminReportSearchResultExportDecorator extends TableDecorator{
	
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
		//String email = accountVO.getNedEmailAddress();
		if(StringUtils.isBlank(fullName)){
			return "";
		}else{
			return fullName;
		}
//		else{
//			return "<a href='mailto:" + email + "'>" + fullName + "</a>";
//		}
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
	public String getResult(){
		String result = "";
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		EmAuditAccountActivityVw eaaVw = accountVO.getAccountActivity();
		if(eaaVw != null && eaaVw.getAction() != null){
			if(accountVO.getAccountActivity().getAction() != null){
				result = accountVO.getAccountActivity().getAction().getDescription();
			}
		}
		return result;
	}
	
	public String getMaxLastLoginDate(){
		Date loginDate = null;
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		loginDate = accountVO.getLastLoginDate();
		if(loginDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(loginDate);
	}
	/**
	 * This method is for display application roles. The roles can be multiple, each role will be displayed in seperated row.
	 * @return
	 */
	public String getApplicationRole(){
		//String path = this.getPageContext().getRequest().getServletContext().getContextPath();
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		List<EmAuditAccountRolesVw> roles = accountVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String role = roles.get(0).getRoleName();

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
		String orgId = roles.get(0).getOrgId();
	
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
		String createDate = new SimpleDateFormat("MM/dd/yyyy").format(roles.get(0).getCreatedDate());
	
		return createDate;
	}

	public String getCreatedBy(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String displayStr = accountVO.getCreatedByFullName();
		return displayStr;
	}

	public String getDeletedBy(){
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		String displayStr =  accountVO.getDeletedByFullName();
		return displayStr;
	}
	
	public String getAccountType(){
		return "APP";
	}
	
	public String getExtSysName(){
		return "NIH";
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
