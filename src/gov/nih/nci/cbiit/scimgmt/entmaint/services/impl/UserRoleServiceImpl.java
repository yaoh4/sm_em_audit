package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.dao.UserRoleDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppPropertiesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.NciPeopleVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.UserRoleService;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRoleServiceImpl implements UserRoleService {

	public static Logger logger = Logger.getLogger(UserRoleServiceImpl.class);
	
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
     * This method retrieves information of logged in user from NciPeopleVw and populates NCIUser.
     * @param userId    
     * @return nciPeopleVw
     */
	public NciUser getNCIUser(String userId) {
		NciPeopleVw nciPeopleVw =  userRoleDAO.getNCIUserInformation(userId);
		if(nciPeopleVw == null){
			logger.info("I2E Account with userId : "+userId + " doesn't exist.");
			return null;
		}	
		NciUser nciUser = new NciUser();
		nciUser.setUserId(nciPeopleVw.getNihNetworkId());
		nciUser.setOracleId(nciPeopleVw.getOracleId());
		nciUser.setFirstName(nciPeopleVw.getFirstName());
		nciUser.setLastName(nciPeopleVw.getLastName());
		nciUser.setFullName(nciPeopleVw.getLastName() + " " + nciPeopleVw.getFirstName());		
		nciUser.setEmail(nciPeopleVw.getEmailAddress());
		nciUser.setActiveFlag(nciPeopleVw.getActiveFlag());			
		return nciUser;
	}
	
	 /**
     * This method checks if logged in user is Valid.
     * @param oracleId
     * @return boolean
     */
    public boolean isI2eAccountValid(String oracleId){
    	return userRoleDAO.isI2eAccountValid(oracleId);
    }

	@Override
	public List<String> retrieveIcCoordinators() {
		return userRoleDAO.retrieveIcCoordinators();
	}

}
