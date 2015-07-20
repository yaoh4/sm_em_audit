package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import org.apache.commons.lang.StringUtils;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;

@SuppressWarnings("serial")
public class AuditAccountVO extends EmAuditAccountsVw{
	
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
	
	

}
