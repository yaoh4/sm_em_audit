package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppPropertiesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.NciPeopleVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.DbaRolePrivs;
import gov.nih.nci.cbiit.scimgmt.entmaint.exceptions.HibernateDAOException;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.I2eActiveUserRolesVw;

import javax.persistence.ParameterMode;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.procedure.ProcedureOutputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRoleDAO {
	
	static Logger logger = Logger.getLogger(UserRoleDAO.class);
	
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
		List<AppPropertiesT> result = null;
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(AppPropertiesT.class);
			crit.add(Restrictions.eq("appName", ApplicationConstants.APP_NAME));
			result = crit.list();
			return result;
		} catch (RuntimeException re) {
			logger.error("get app properties failed", re);
			EmAppUtil.logUserID(nciUser, logger);
			logger.error("Outgoing Parameters: List<AppPerpertiesT> - " + (result == null ? "NULL" : "size=" + result.size()));
			throw re;
		}
	}
	
	/**
     * This method retrieves information of logged in user from NciPeopleVw.
     * @param userId    
     * @return nciPeopleVw
     */
    public NciPeopleVw getNCIUserInformation(String userId){ 
    	NciPeopleVw nciPeopleVw = null;
    	Criteria criteria = null;    	
    	try{
    		criteria = sessionFactory.getCurrentSession().createCriteria(NciPeopleVw.class);       		
    		criteria.add(Restrictions.eq("nihNetworkId", userId.toUpperCase()));    		
    		nciPeopleVw = (NciPeopleVw) criteria.uniqueResult();
        	
    	} catch (Throwable ex) {
    		logger.error("Error occurred while retrieving I2E Account of NCI User with nihNetworkId: "+userId, ex);
			throw ex;
		}
     	
    	return nciPeopleVw;
    }

	public List<String> retrieveIcCoordinators() {
	
		/*  select npn.nih_network_id
	    from I2E_ACTIVE_USER_ROLES_VW npe, nci_people_vw npn
	    where role_code = 'EMREP'
	    AND npe.npn_id = npn.npn_id;*/
		
		List<String> result = null;
    	try{
    		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NciPeopleVw.class);
    		criteria.add(Restrictions.eq("activeFlag", "Y"));
    		criteria.createAlias("accountRoles","accountRoles");
    		criteria.add(Restrictions.eq("accountRoles.roleCode", "EMREP"));
    		criteria.setProjection(Projections.property("nihNetworkId"));
    		result = criteria.list();
        	
    	} catch (Throwable ex) {
    		logger.error("Error occurred while retrieving list of ic coordinator nihNetworkIds: ", ex);
			throw ex;
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<I2eActiveUserRolesVw> retrieveRoleInfo(String userId) throws HibernateDAOException {
    
        List<I2eActiveUserRolesVw> result = null;
        try {
        	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(I2eActiveUserRolesVw.class);
            criteria.add(Restrictions.eq("nciLdapCn",userId.toUpperCase()));
            criteria.addOrder(Order.desc("npeId"));
            result = criteria.list();
        } catch (Exception ex) {
        	logger.error("Error occured while retrieving role info for user " + userId, ex);
        	throw ex;
        }
        
        return result;
    }
	
    /**
     * This method checks if logged in user is RestrictedUser.
     * @param  oracleId
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public boolean isRestrictedUser(String oracleId){ 
    	boolean isRestrictedUser = false;
    	try{
    		Criteria rolesCriteria = sessionFactory.getCurrentSession().createCriteria(DbaRolePrivs.class);
        	rolesCriteria.add(Restrictions.eq("grantee", oracleId));
        	List<DbaRolePrivs> roles = (List<DbaRolePrivs>) rolesCriteria.list();
    		for(DbaRolePrivs role : roles){
    			if(ApplicationConstants.I2E_RESTRICTED_USER.equalsIgnoreCase(role.getGrantedRole())){
    				isRestrictedUser = true;
    				logger.info("I2E Account with oracleId : "+oracleId + " is Restricted.");
    				break;
    			}
    		}

    	} catch (Throwable ex) {
    		logger.error("Error occurred while retrieving NCI User Roles with oracleId: "+oracleId, ex);
    		throw ex;
    	}
   	
    	return isRestrictedUser;
    }
    
    @SuppressWarnings("unchecked")
	public List<NciPeopleVw> findByFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCase(String firstName, String lastName) {
    	List<NciPeopleVw> result = null; 
    	try {
    		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NciPeopleVw.class);
    		Disjunction dc = Restrictions.disjunction();
    		 if (StringUtils.isNotBlank(firstName)) {
    			 dc.add(Restrictions.like("firstName", firstName + "%").ignoreCase());
    		 }
    		 if (StringUtils.isNotBlank(lastName)) {
    			 dc.add(Restrictions.like("lastName", lastName + "%").ignoreCase());
    		 }
    		 criteria.add(dc);
    		result = criteria.list();
    	}
    	catch (Exception e) {
        	logger.error("Error occured while results " + firstName +lastName, e);
        	throw new HibernateDAOException(e.getMessage());
        }
    	
    	return result;
    }
}
