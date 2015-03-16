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
		//TODO Remove me !!
		if(nciUser.getCurrentUserRole() == null) {
			nciUser.setCurrentUserRole(ApplicationConstants.USER_ROLE_IC_COORDINATOR); // For testing only
		}
		//TODO end remove
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
			crit.add(Restrictions.eq("appName", "EM"));
			final List<AppPropertiesT> result = crit.list();
			//Add IC coordinator emails
			List<String> emails = getIcEmails();
			if(emails != null) {
				String value = StringUtils.join(emails.toArray(), ";");
				AppPropertiesT prop = new AppPropertiesT();
				prop.setPropKey(ApplicationConstants.IC_COORDINATOR_EMAIL);
				prop.setPropValue(value);
				result.add(prop);
			}
			return result;
		} catch (RuntimeException re) {
			logger.error("get app properties failed", re);
			throw re;
		}
	}
	
	/**
	 * Get the list of IC Coordinator email addresses
	 * @return
	 */
	public List<String> getIcEmails() {

		String emailQuery="select email_address from nci_people_t people, nci_person_org_roles_t org_roles " + 
					"where people.id=org_roles.epn_id and ere_code='EMREP'and org_roles.end_date is null " +
					"and people.inactive_date is null";
		final Session session = sessionFactory.getCurrentSession();
		try {
			final List<String> emails = sessionFactory.getCurrentSession().createSQLQuery(emailQuery).list();
			return emails;
		} catch (RuntimeException re) {
			logger.error("get app properties failed", re);
			throw re;
		}
	}
}
