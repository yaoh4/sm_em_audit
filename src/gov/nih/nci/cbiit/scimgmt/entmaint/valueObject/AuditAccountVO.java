package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;

public class AuditAccountVO extends EmAuditAccountsVw{
	
	public String getFullName() {
		final StringBuffer sb = new StringBuffer(70);

		sb.append(getNedLastName());
		sb.append(", ");
		sb.append(getNedFirstName());

		return sb.toString();
	}

}
