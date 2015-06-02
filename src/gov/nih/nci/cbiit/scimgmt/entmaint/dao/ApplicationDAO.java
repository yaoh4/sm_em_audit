package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppPropertiesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;

import javax.persistence.ParameterMode;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.procedure.ProcedureOutputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationDAO {
	
	static Logger logger = Logger.getLogger(ApplicationDAO.class);
	
	@Autowired
	private NciUser nciUser;
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Gets user role and org path for a given ldap id.
	 * 
	 * @param nciUser
	 */
	public void loadPersonInfo(final NciUser nciUser) {
		
		final Session session = sessionFactory.getCurrentSession();
		
		ProcedureCall pc = session.createStoredProcedureCall("USER_INFO_PKG.RETRIEVE_EM_PERSON_INFO");
		pc.registerParameter("p_ldap_id", String.class, ParameterMode.IN).bindValue(nciUser.getUserId().toUpperCase());
		pc.registerParameter("p_role_code", String.class, ParameterMode.OUT);
		pc.registerParameter("p_org_path", String.class, ParameterMode.OUT);

		ProcedureOutputs outputs = pc.getOutputs();
		String roleCode = (String) outputs.getOutputParameterValue("p_role_code");
		String orgPath = (String) outputs.getOutputParameterValue("p_org_path");
		
		nciUser.setCurrentUserRole(roleCode);
		nciUser.setOrgPath(orgPath);
		logger.debug("Setting user role;  " + nciUser.getCurrentUserRole() + " org path: "
				+ nciUser.getOrgPath() + " for ldapuser " + nciUser.getUserId());
		
	}
	
	/**
	 * Get Application Properties from DB
	 * @return
	 */
	public List<AppPropertiesT> getAppPropertiesList() {
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(AppPropertiesT.class);
			crit.add(Restrictions.eq("appName", ApplicationConstants.APP_NAME));
			final List<AppPropertiesT> result = crit.list();
			return result;
		} catch (RuntimeException re) {
			logger.error("get app properties failed", re);
			throw re;
		}
	}
	
}
