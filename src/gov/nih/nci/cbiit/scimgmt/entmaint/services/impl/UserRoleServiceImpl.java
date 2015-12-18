package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.dao.UserRoleDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppPropertiesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.UserRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRoleServiceImpl implements UserRoleService {

	@Autowired
	private UserRoleDAO userRoleDAO;
	/**
	 * Get NCI User information such as Roles and Organization
	 * 
	 * @return 
	 */
	@Override
	public void loadPersonInfo(NciUser nciUser) {
		userRoleDAO.loadPersonInfo(nciUser);
    }
	
	/**
	 * Retrieve Application properties from DB
	 * @return
	 */
	@Override
	public List<AppPropertiesT> getAppPropertiesList() {
		return userRoleDAO.getAppPropertiesList();
	}
	
	 /**
     * This method checks if logged in user is Valid.
     * @param oracleId
     * @return boolean
     */
    public boolean isI2eAccountValid(String oracleId){
    	return userRoleDAO.isI2eAccountValid(oracleId);
    }

}
