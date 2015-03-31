package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

// Generated Feb 13, 2015 3:58:29 PM by Hibernate Tools 3.4.0.CR1

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountActivityT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
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

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private NciUser nciUser;
	
	/**
	 * Search EmAuditAccountsVw for active accounts
	 * 
	 * @param paginatedList
	 * @param searchVO
	 * @param all
	 * @return
	 */
	public PaginatedListImpl<EmAuditAccountsVw> searchActiveAccounts(PaginatedListImpl paginatedList, final AuditSearchVO searchVO, Boolean all) {
		log.debug("searching for IMPAC II active accounts in audit view: " + searchVO);
		try {
			final int objectsPerPage = paginatedList.getObjectsPerPage();
			final int firstResult = objectsPerPage * paginatedList.getIndex();
			String sortOrderCriterion = paginatedList.getSortCriterion();
			String sortOrder = paginatedList.getSqlSortDirection();
					  
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			if (!StringUtils.isBlank(sortOrderCriterion)) {
				if (sortOrderCriterion.equalsIgnoreCase("fullName")) {
					if (StringUtils.equalsIgnoreCase(sortOrder, "asc")) {
						criteria.addOrder(Order.asc("nedLastName"));
						criteria.addOrder(Order.asc("nedFirstName"));
					} else {
						criteria.addOrder(Order.desc("nedLastName"));
						criteria.addOrder(Order.desc("nedFirstName"));
					}
				} else if (sortOrderCriterion.equalsIgnoreCase("accountCreatedDate")) {
					if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
						criteria.addOrder(Order.asc("createdDate"));
					else
						criteria.addOrder(Order.desc("createdDate"));
				} else {
					if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
						criteria.addOrder(Order.asc(sortOrderCriterion));
					else
						criteria.addOrder(Order.desc(sortOrderCriterion));
				}
			}
			
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
				criteria.add(Restrictions.eq("activeAction.id", new Long(searchVO.getAct())));
			}
	
			List<EmAuditAccountsVw> auditList = null;
			if (all) {
				auditList = criteria.list();
				paginatedList.setTotal(auditList.size());
			} else {
				auditList = criteria.setFirstResult(firstResult)
					.setMaxResults(objectsPerPage)
					.list();
			}
			
			paginatedList.setList(auditList);
			if(!all && paginatedList.getFullListSize() == 0) {
				paginatedList.setTotal(getTotalResultCount(criteria));
			}

			return paginatedList;

		} catch (final RuntimeException re) {
			log.error("Error while searching", re);
			throw re;
		}
	}
	
	/**
	 * Search EmAuditAccountsVw for new accounts
	 * 
	 * @param paginatedList
	 * @param searchVO
	 * @param all
	 * @return
	 */
	public PaginatedListImpl<EmAuditAccountsVw> searchNewAccounts(PaginatedListImpl paginatedList, final AuditSearchVO searchVO, Boolean all) {
		log.debug("searching for IMPAC II new accounts in audit view: " + searchVO);
		try {
			final int objectsPerPage = paginatedList.getObjectsPerPage();
			final int firstResult = objectsPerPage * paginatedList.getIndex();
			String sortOrderCriterion = paginatedList.getSortCriterion();
			String sortOrder = paginatedList.getSqlSortDirection();
				
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			if (!StringUtils.isBlank(sortOrderCriterion)) {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
					criteria.addOrder(Order.asc(sortOrderCriterion));
				else
					criteria.addOrder(Order.desc(sortOrderCriterion));
			}
			
			// Criteria specific to new accounts
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.geProperty("createdDate", "audit.impaciiFromDate"));
			criteria.add(Restrictions.leProperty("createdDate", "audit.impaciiToDate"));
			criteria.add(Restrictions.isNull("audit.endDate"));

			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.NEW_ACTION_ALL)) {
				criteria.createAlias("newAction", "action");
				criteria.add(Restrictions.eq("action.id", new Long(searchVO.getAct())));
			}
			
			List<EmAuditAccountsVw> auditList = null;
			if (all) {
				auditList = criteria.list();
				paginatedList.setTotal(auditList.size());
			} else {
				auditList = criteria.setFirstResult(firstResult)
					.setMaxResults(objectsPerPage)
					.list();
			}
			
			paginatedList.setList(auditList);
			if(!all && paginatedList.getFullListSize() == 0) {
				paginatedList.setTotal(getTotalResultCount(criteria));
			}

			return paginatedList;
		} catch (final RuntimeException re) {
			log.error("Error while searching", re);
			throw re;
		}
	}
	
	/**
	 * Search EmAuditAccountsVw for deleted accounts
	 * 
	 * @param paginatedList
	 * @param searchVO
	 * @param all
	 * @return
	 */
	public PaginatedListImpl<EmAuditAccountsVw> searchDeletedAccounts(PaginatedListImpl paginatedList, final AuditSearchVO searchVO, Boolean all) {
		log.debug("searching for IMPAC II deleted accounts in audit view: " + searchVO);
		try {
			final int objectsPerPage = paginatedList.getObjectsPerPage();
			final int firstResult = objectsPerPage * paginatedList.getIndex();
			String sortOrderCriterion = paginatedList.getSortCriterion();
			String sortOrder = paginatedList.getSqlSortDirection();
				
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			if (!StringUtils.isBlank(sortOrderCriterion)) {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
					criteria.addOrder(Order.asc(sortOrderCriterion));
				else
					criteria.addOrder(Order.desc(sortOrderCriterion));
			}
			
			// Criteria specific to deleted accounts
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.geProperty("deletedDate", "audit.impaciiFromDate"));
			criteria.add(Restrictions.leProperty("deletedDate", "audit.impaciiToDate"));
			criteria.add(Restrictions.isNull("audit.endDate"));

			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.DELETED_ACTION_ALL)) {
				criteria.createAlias("deletedAction", "action");
				criteria.add(Restrictions.eq("action.id", new Long(searchVO.getAct())));
			}
						
			List<EmAuditAccountsVw> auditList = null;
			
			if (all) {
				auditList = criteria.list();
				paginatedList.setTotal(auditList.size());
			} else {
				auditList = criteria.setFirstResult(firstResult)
					.setMaxResults(objectsPerPage)
					.list();
			}
			
			paginatedList.setList(auditList);
			if(!all && paginatedList.getFullListSize() == 0) {
				paginatedList.setTotal(getTotalResultCount(criteria));
			}

			return paginatedList;
			
		} catch (final RuntimeException re) {
			log.error("Error while searching", re);
			throw re;
		}
	}
	
	/**
	 * Search EmAuditAccountsVw for inactive accounts
	 * 
	 * @param paginatedList
	 * @param searchVO
	 * @param all
	 * @return
	 */
	public PaginatedListImpl<EmAuditAccountsVw> searchInactiveAccounts(PaginatedListImpl paginatedList, final AuditSearchVO searchVO, Boolean all) {
		log.debug("searching for IMPAC II inactive accounts in audit view: " + searchVO);
		try {
			final int objectsPerPage = paginatedList.getObjectsPerPage();
			final int firstResult = objectsPerPage * paginatedList.getIndex();
			String sortOrderCriterion = paginatedList.getSortCriterion();
			String sortOrder = paginatedList.getSqlSortDirection();
				
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			if (!StringUtils.isBlank(sortOrderCriterion)) {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
					criteria.addOrder(Order.asc(sortOrderCriterion));
				else
					criteria.addOrder(Order.desc(sortOrderCriterion));
			}
			
			// Criteria specific to inactive accounts
			criteria.add(Restrictions.eq("inactiveUserFlag", "Y"));
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.isNull("audit.endDate"));
			
			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.INACTIVE_ACTION_ALL)) {
				criteria.createAlias("inactiveAction", "action");
				criteria.add(Restrictions.eq("action.id", new Long(searchVO.getAct())));
			}				
			
			List<EmAuditAccountsVw> auditList = null;
			if (all) {
				auditList = criteria.list();
				paginatedList.setTotal(auditList.size());
			} else {
				auditList = criteria.setFirstResult(firstResult)
					.setMaxResults(objectsPerPage)
					.list();
			}
			
			paginatedList.setList(auditList);
			if(!all && paginatedList.getFullListSize() == 0) {
				paginatedList.setTotal(getTotalResultCount(criteria));
			}

			return paginatedList;
			
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
	 * @param date
	 * @return
	 */
	public DBResult submit(AppLookupT category, Long eaaId, Long actionId, String actionComments, Date date) {
		DBResult result = new DBResult();
		try {
			EmAuditAccountActivityT activity = getAccountActivityT(eaaId, category.getCode());
			if(activity == null) {
				activity = new EmAuditAccountActivityT();
				activity.setEaaId(eaaId);
				activity.setCategory(category);
				activity.setCreateUserId(nciUser.getUserId().toUpperCase());
				activity.setCreateDate(date);
				activity.setLastSubmittedByUserId(nciUser.getUserId().toUpperCase());
				activity.setLastSubmittedDate(date);
			}
			activity.setActionId(actionId);
			activity.setNotes(actionComments);
			activity.setLastChangeUserId(nciUser.getUserId().toUpperCase());
			activity.setLastChangeDate(date);
			activity.setUnsubmittedFlag(ApplicationConstants.FLAG_NO);
			activity.setLastSubmittedByUserId(nciUser.getUserId().toUpperCase());
			activity.setLastSubmittedDate(date);
			saveOrUpdateActivity(activity);
		} catch (RuntimeException re) {
			log.error("Submit failed, " + re.getMessage());
			throw re;
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
				activity.setLastChangeUserId(nciUser.getUserId().toUpperCase());
				activity.setLastChangeDate(new Date());
				activity.setUnsubmittedFlag(ApplicationConstants.FLAG_YES);
				activity.setLastSubmittedByUserId(null);
				activity.setLastSubmittedDate(null);
				saveOrUpdateActivity(activity);
			}
		} catch (RuntimeException re) {
			log.error("Unsubmit failed, " + re.getMessage());
			throw re;
		}
		result.setStatus(DBResult.SUCCESS);
		return result;
	}
	
	/**
	 * Get audit account vw using id
	 * 
	 * @param id
	 * @return
	 */
	public EmAuditAccountsVw getAuditAccountById(Long id) {
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);
			crit.add(Restrictions.eq("id", id));
			EmAuditAccountsVw result = (EmAuditAccountsVw) crit.uniqueResult();
			return result;
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
	
	/**
	 * Gets the total result count.
	 * 
	 * @param criteria
	 *            the criteria
	 * @return the total result count
	 */
	private int getTotalResultCount(Criteria criteria) {

		criteria.setProjection(Projections.rowCount());
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount.intValue();

	}

}
