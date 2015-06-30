package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import org.apache.commons.lang.StringUtils;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioI2eVw;

@SuppressWarnings("serial")
public class PortfolioI2eAccountVO extends EmPortfolioI2eVw{	
	
	/**
	 * Returns full name.
	 * @return String
	 */
	public String getFullName() {
		final StringBuffer sb = new StringBuffer("");
		String lastName = getLastName();
		String firstName = getFirstName();
		
		if(StringUtils.isBlank(lastName)){
			lastName = this.getI2eLastName();
		}
		if(StringUtils.isBlank(firstName)){
			firstName = this.getI2eFirstName();
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
