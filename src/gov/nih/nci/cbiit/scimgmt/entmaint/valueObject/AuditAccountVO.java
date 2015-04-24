package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import org.apache.commons.lang.StringUtils;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;

@SuppressWarnings("serial")
public class AuditAccountVO extends EmAuditAccountsVw{
	
	public String getFullName() {
		String lastName = "";
		String firstName = "";
		final StringBuffer sb = new StringBuffer("");
		if(this.getDeletedDate() != null){
			lastName = getImpaciiLastName();
			firstName = getImpaciiFirstName();
		}else{
			lastName = getNedLastName();
			firstName = getNedFirstName();
		}
		
		if(!StringUtils.isBlank(lastName)){
			sb.append(lastName);
		}
		if(!StringUtils.isBlank(lastName) && !StringUtils.isBlank(firstName)){
			sb.append(", ");
		}
		if(!StringUtils.isBlank(firstName)){
			sb.append(firstName);
		}
		
		return sb.toString();
	}
	
	

}
