package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.dao.ApplicationDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppPropertiesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.ApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private ApplicationDAO applicationDAO;
	/**
	 * Get NCI User information such as Roles and Organization
	 * 
	 * @return 
	 */
	@Override
	public void loadPersonInfo(NciUser nciUser) {
		applicationDAO.loadPersonInfo(nciUser);
    }
	
	/**
	 * Retrieve Application properties from DB
	 * @return
	 */
	@Override
	public List<AppPropertiesT> getAppPropertiesList() {
		return applicationDAO.getAppPropertiesList();
	}

}
