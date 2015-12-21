package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppPropertiesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;

/**
 * interface  that provides all the generic APIs for the EM audit application
 */
public interface UserRoleService {

	/**
	 * Get NCI User information such as Roles and Organization
	 * 
	 * @return 
	 */
	public void loadPersonInfo(NciUser nciUser);
	
	/**
	 * Retrieve Application properties from DB
	 * @return
	 */
	public List<AppPropertiesT> getAppPropertiesList();
	
	 /**
     * This method checks if logged in user is Valid.
     * @param oracleId
     * @return boolean
     */
    public boolean isI2eAccountValid(String oracleId);
}