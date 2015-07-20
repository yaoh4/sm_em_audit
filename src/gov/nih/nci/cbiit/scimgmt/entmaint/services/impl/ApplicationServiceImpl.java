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
	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.ApplicationService#loadPersonInfo(gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser)
	 */
	@Override
	public void loadPersonInfo(NciUser nciUser) {
		applicationDAO.loadPersonInfo(nciUser);
    }
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.ApplicationService#getAppPropertiesList()
	 */
	@Override
	public List<AppPropertiesT> getAppPropertiesList() {
		return applicationDAO.getAppPropertiesList();
	}

}
