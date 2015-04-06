package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import org.apache.commons.lang.StringUtils;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioVw;

@SuppressWarnings("serial")
public class PortfolioAccountVO extends EmPortfolioVw{	
	
	/**
	 * Returns full name.
	 * @return String
	 */
	public String getFullName() {
		final StringBuffer sb = new StringBuffer("");
		String lastName = getNedLastName();
		String firstName = getNedFirstName();
		
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
