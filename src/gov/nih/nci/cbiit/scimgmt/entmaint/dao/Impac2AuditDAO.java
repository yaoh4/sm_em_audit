package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

// Generated Feb 13, 2015 3:58:29 PM by Hibernate Tools 3.4.0.CR1

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountActivityT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DAO for domain model class EmAuditAccountsVw.
 * @see gov.nih.nci.cbiit.scimgmt.EmAuditAccountsVw
 * @author Hibernate Tools
 */
@Component
public class Impac2AuditDAO {

	public static Logger log = Logger.getLogger(Impac2AuditDAO.class);

	public static final String FLAG_YES = "Y";
	public static final String FLAG_NO = "N";

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private NciUser nciUser;
	
	/**
	 * Search EmAuditAccountsVw for active accounts
	 * 
	 * @param searchVO
	 * @return
	 */
	public List<EmAuditAccountsVw> searchActiveAccounts(final AuditSearchVO searchVO) {
		log.debug("searching for IMPAC II active accounts in audit view: " + searchVO);
		try {
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Criteria specific to active accounts
			criteria.createAlias("audit", "audit");
			Disjunction disc = Restrictions.disjunction();
            disc.add(Restrictions.isNull("deletedDate"));
            disc.add(Restrictions.gtProperty("deletedDate", "audit.impaciiToDate"));
            criteria.add(disc);
			criteria.add(Restrictions.leProperty("createdDate", "audit.impaciiToDate"));
			criteria.add(Restrictions.isNull("audit.endDate"));

			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.ACTIVE_ACTION_ALL) ) {
				criteria.createAlias("accountActivities", "accountActivities");
				criteria.createAlias("accountActivities.action", "action");
				criteria.add(Restrictions.eq("action.id", new Integer(searchVO.getAct())));
			}
						
			List<EmAuditAccountsVw> auditList = criteria.list();
			
			return auditList;
		} catch (final RuntimeException re) {
			log.error("Error while searching", re);
			throw re;
		}
	}
	
	/**
	 * Search EmAuditAccountsVw for new accounts
	 * 
	 * @param searchVO
	 * @return
	 */
	public List<EmAuditAccountsVw> searchNewAccounts(final AuditSearchVO searchVO) {
		log.debug("searching for IMPAC II new accounts in audit view: " + searchVO);
		try {
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Criteria specific to new accounts
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.geProperty("createdDate", "audit.impaciiFromDate"));
			criteria.add(Restrictions.leProperty("createdDate", "audit.impaciiToDate"));
			criteria.add(Restrictions.isNull("audit.endDate"));

			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.NEW_ACTION_ALL)) {
				criteria.createAlias("accountActivities", "accountActivities");
				criteria.createAlias("accountActivities.action", "action");
				criteria.add(Restrictions.eq("action.id", new Integer(searchVO.getAct())));
			}
			
			List<EmAuditAccountsVw> auditList = criteria.list();

			return auditList;
		} catch (final RuntimeException re) {
			log.error("Error while searching", re);
			throw re;
		}
	}
	
	/**
	 * Search EmAuditAccountsVw for deleted accounts
	 * 
	 * @param searchVO
	 * @return
	 */
	public List<EmAuditAccountsVw> searchDeletedAccounts(final AuditSearchVO searchVO) {
		log.debug("searching for IMPAC II deleted accounts in audit view: " + searchVO);
		try {
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Criteria specific to deleted accounts
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.geProperty("deletedDate", "audit.impaciiFromDate"));
			criteria.add(Restrictions.leProperty("deletedDate", "audit.impaciiToDate"));
			criteria.add(Restrictions.isNull("audit.endDate"));

			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.DELETED_ACTION_ALL)) {
				criteria.createAlias("accountActivities", "accountActivities");
				criteria.createAlias("accountActivities.action", "action");
				criteria.add(Restrictions.eq("action.id", new Integer(searchVO.getAct())));
			}
						
			List<EmAuditAccountsVw> auditList = criteria.list();
			
			return auditList;
		} catch (final RuntimeException re) {
			log.error("Error while searching", re);
			throw re;
		}
	}
	
	/**
	 * Search EmAuditAccountsVw for inactive accounts
	 * 
	 * @param searchVO
	 * @return
	 */
	public List<EmAuditAccountsVw> searchInactiveAccounts(final AuditSearchVO searchVO) {
		log.debug("searching for IMPAC II inactive accounts in audit view: " + searchVO);
		try {
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Criteria specific to inactive accounts
			criteria.add(Restrictions.eq("inactiveUserFlag", "Y"));
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.isNull("audit.endDate"));
			
			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.INACTIVE_ACTION_ALL)) {
				criteria.createAlias("accountActivities", "accountActivities");
				criteria.createAlias("accountActivities.action", "action");
				criteria.add(Restrictions.eq("action.id", new Integer(searchVO.getAct())));
			}
						
			List<EmAuditAccountsVw> auditList = criteria.list();
			
			return auditList;
		} catch (final RuntimeException re) {
			log.error("Error while searching", re);
			throw re;
		}
	}
	
	/**
	 * Submit action taken for audit accounts
	 * 
	 * @param category
	 * @param eaaId
	 * @param actionId
	 * @param actionComments
	 * @return
	 */
	public DBResult submit(String category, Long eaaId, Long actionId, String actionComments) {
		DBResult result = new DBResult();
		try {
			EmAuditAccountActivityT activity = getAccountActivityT(eaaId, category);
			if(activity == null) {
				activity = new EmAuditAccountActivityT();
				activity.setEaaId(eaaId);
				activity.setCategory(getCategoryByCode(category));
				activity.setCreateUserId(nciUser.getOracleId());
				activity.setCreateDate(new Date());
			}
			activity.setActionId(actionId);
			activity.setNotes(actionComments);
			activity.setLastChangeUserId(nciUser.getOracleId());
			activity.setLastChangeDate(new Date());
			activity.setUnsubmittedFlag(FLAG_NO);
			saveOrUpdateActivity(activity);
		} catch (Exception e) {
			result.setStatus(DBResult.FAILURE);
			result.setMessage(e.getMessage());
			return result;
		}
		result.setStatus(DBResult.SUCCESS);
		return result;
	}
	
	/**
	 * Unsubmit action previously taken on audit account
	 * 
	 * @param category
	 * @param eaaId
	 * @return
	 */
	public DBResult unsubmit(String category, Long eaaId) {
		DBResult result = new DBResult();
		try {
			EmAuditAccountActivityT activity = getAccountActivityT(eaaId, category);
			if(activity != null) {
				activity.setLastChangeUserId(nciUser.getOracleId());
				activity.setLastChangeDate(new Date());
				activity.setUnsubmittedFlag(FLAG_YES);
				saveOrUpdateActivity(activity);
			}
		} catch (Exception e) {
			result.setStatus(DBResult.FAILURE);
			result.setMessage(e.getMessage());
			return result;
		}
		result.setStatus(DBResult.SUCCESS);
		return result;
	}
	
	/**
	 * Get discrepancy from appLookupT using code
	 * 
	 * @param discrepancy
	 * @return
	 */
	public AppLookupT getDiscrepancyByCode(String discrepancy) {
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(AppLookupT.class);
			crit.add(Restrictions.eq("discriminator", "DISCREPANCY_TYPE"));
			crit.add(Restrictions.eq("code", discrepancy));
			crit.add(Restrictions.eq("active", true));
			AppLookupT instance = (AppLookupT) crit.uniqueResult();
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Adding user specific search criteria
	 * 
	 * @param criteria
	 * @param searchVO
	 * @return
	 */
	private Criteria addSearchCriteria(final Criteria criteria, final AuditSearchVO searchVO) {
		log.debug("adding search criteria for IMPAC II account query in audit view: " + searchVO);

		// firstName partial search
		if (!StringUtils.isBlank(searchVO.getUserFirstname())) {
			criteria.add(Restrictions.ilike("nedFirstName", searchVO.getUserFirstname().trim(), MatchMode.START));
		}
		// lastName partial search
		if (!StringUtils.isBlank(searchVO.getUserLastname())) {
			criteria.add(Restrictions.ilike("nedLastName", searchVO.getUserLastname().trim(), MatchMode.START));
		}

		// org
		if (!StringUtils.isBlank(searchVO.getOrganization()) && !StringUtils.equalsIgnoreCase(searchVO.getOrganization(), ApplicationConstants.NCI_DOC_ALL)) {
			if(searchVO.getOrganization().equalsIgnoreCase(ApplicationConstants.ORG_PATH_NON_NCI)) {
				criteria.add(Restrictions.ne("nedIc", ApplicationConstants.NED_IC_NCI));
			}
			else {
				criteria.add(Restrictions.eq("parentNedOrgPath", searchVO.getOrganization().trim()));
			}
		}
		
		// excludeNCIOrgs
		if (searchVO.isExcludeNCIOrgs()) {
			criteria.add(Restrictions.eq("nciDoc", ApplicationConstants.NCI_DOC_OTHER));
		}

		return criteria;
	}
	
	/**
	 * Get account activity using id and category
	 * 
	 * @param eaaId
	 * @param category
	 * @return
	 */
	private EmAuditAccountActivityT getAccountActivityT(Long eaaId, String category) {
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountActivityT.class);
			crit.add(Restrictions.eq("eaaId", eaaId));
			crit.createAlias("category", "category");
			crit.add(Restrictions.eq("category.code", category));
			EmAuditAccountActivityT result = (EmAuditAccountActivityT) crit.uniqueResult();
			return result;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/**
	 * Get category from appLookupT using code
	 * 
	 * @param category
	 * @return
	 */
	private AppLookupT getCategoryByCode(String category) {
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(AppLookupT.class);
			crit.add(Restrictions.eq("discriminator", "CATEGORY"));
			crit.add(Restrictions.eq("code", category));
			crit.add(Restrictions.eq("active", true));
			AppLookupT instance = (AppLookupT) crit.uniqueResult();
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Save or update activity
	 * 
	 * @param transientInstance
	 *            the transient instance
	 */
	private void saveOrUpdateActivity(final EmAuditAccountActivityT transientInstance) {
		log.debug("saveOrUpdate EmAuditAccountActivityT instance");
		try {
			Session session = sessionFactory.getCurrentSession();
			session.saveOrUpdate(transientInstance);
			log.debug("saveOrUpdate successful");
		} catch (final RuntimeException re) {
			log.error("saveOrUpdate failed", re);
			throw re;
		}
	}

}
