package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;
import org.apache.commons.lang3.StringUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * This class is responsible for decorating the rows of I2E admin reports search results table.
 * @author
 *
 */
@Configurable
public class AdminReportI2eSearchResultDecorator extends TableDecorator{
	
	@Autowired
	private LookupService lookupService;
	
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
}
