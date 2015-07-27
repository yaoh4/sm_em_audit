package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import org.apache.commons.lang.StringUtils;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountsVw;

@SuppressWarnings("serial")
public class AuditI2eAccountVO extends EmI2eAuditAccountsVw{
	
	public String getFullName() {
		final StringBuffer sb = new StringBuffer("");
		
		if(!StringUtils.isBlank(this.getLastName())){
			sb.append(this.getLastName());
		}
		if(!StringUtils.isBlank(this.getLastName()) && !StringUtils.isBlank(this.getFirstName())){
			sb.append(", ");
		}
		if(!StringUtils.isBlank(this.getFirstName())){
			sb.append(this.getFirstName());
		}
		
		return sb.toString();
	}
	
	public void addAccountRole(EmI2eAuditAccountRolesVw role){
		getAccountRoles().add(role);
	}

}
