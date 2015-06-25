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
import org.hibernate.HibernateException;
import org.hibernate.NullPrecedence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
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
	  
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			criteria = addSortOrder(criteria, paginatedList);
			
			// Criteria specific to active accounts
			criteria.createAlias("audit", "audit");
			Disjunction disc = Restrictions.disjunction();
            disc.add(Restrictions.isNull("deletedDate"));
            disc.add(Restrictions.gtProperty("deletedDate", "audit.impaciiToDate"));
            criteria.add(disc);
			criteria.add(Restrictions.leProperty("createdDate", "audit.impaciiToDate"));

			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.ACTIVE_ACTION_NOACTION) ) {
				criteria.add(Restrictions.isNull("activeAction.id"));
			}
			else if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.ACTIVE_ACTION_ALL) ) {
				criteria.add(Restrictions.eq("activeAction.id", new Long(searchVO.getAct())));
			}

			return getPaginatedListResult(paginatedList, criteria, all);

		} catch (Throwable e) {
			log.error("Error while searching for IMPAC II active accounts in audit view", e);
			throw e;
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
	
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			criteria = addSortOrder(criteria, paginatedList);
			
			// Criteria specific to new accounts
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.geProperty("createdDate", "audit.impaciiFromDate"));
			criteria.add(Restrictions.leProperty("createdDate", "audit.impaciiToDate"));

			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.NEW_ACTION_NOACTION) ) {
				criteria.add(Restrictions.isNull("newAction.id"));
			}
			else if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.NEW_ACTION_ALL)) {
				criteria.add(Restrictions.eq("newAction.id", new Long(searchVO.getAct())));
			}

			return getPaginatedListResult(paginatedList, criteria, all);
		} catch (Throwable e) {
			log.error("searching for IMPAC II new accounts in audit view", e);
			throw e;
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

			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			criteria = addSortOrder(criteria, paginatedList);
			
			// Criteria specific to deleted accounts
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.geProperty("deletedDate", "audit.impaciiFromDate"));
			criteria.add(Restrictions.leProperty("deletedDate", "audit.impaciiToDate"));

			// Add user specific search criteria
			addDeletedSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.DELETED_ACTION_NOACTION) ) {
				criteria.add(Restrictions.isNull("deletedAction.id"));
			}
			else if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.DELETED_ACTION_ALL)) {
				criteria.add(Restrictions.eq("deletedAction.id", new Long(searchVO.getAct())));
			}

			return getPaginatedListResult(paginatedList, criteria, all);
			
		} catch (Throwable e) {
			log.error("searching for IMPAC II deleted accounts in audit view", e);
			throw e;
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

			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			criteria = addSortOrder(criteria, paginatedList);
			
			// Criteria specific to inactive accounts
			criteria.add(Restrictions.eq("inactiveUserFlag", "Y"));
			criteria.createAlias("audit", "audit");
			
			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.INACTIVE_ACTION_NOACTION) ) {
				criteria.add(Restrictions.isNull("inactiveAction.id"));
			}
			else if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.INACTIVE_ACTION_ALL)) {
				criteria.add(Restrictions.eq("inactiveAction.id", new Long(searchVO.getAct())));
			}				

			return getPaginatedListResult(paginatedList, criteria, all);
			
		} catch (Throwable e) {
			log.error("searching for IMPAC II inactive accounts in audit view", e);
			throw e;
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
			} else {
				activity.setLastChangeUserId(nciUser.getUserId().toUpperCase());
				activity.setLastChangeDate(date);
			}
			activity.setActionId(actionId);
			activity.setNotes(actionComments);
			activity.setUnsubmittedFlag(ApplicationConstants.FLAG_NO);
			activity.setLastSubmittedByUserId(nciUser.getUserId().toUpperCase());
			activity.setLastSubmittedDate(date);
			saveOrUpdateActivity(activity);
		} catch (Throwable e) {
			log.error("Submit Action in Audit failed for eaaId=" + eaaId + " category=" + category.getCode()
					+ " actionId=" + actionId + " actionComments=" + actionComments
					+ e.getMessage(), e);
			throw e;
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
			} else {
				log.error("Account activity doesn't exist for eaaId=" + eaaId + " and category=" + category);
			}
		} catch (Throwable e) {
			log.error("Unsubmit Action in Audit failed for eaaId=" + eaaId + " category=" + category
					+ e.getMessage(), e);
			throw e;
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
		} catch (Throwable e) {
			log.error("getAuditAccountById failed for id=" + id + e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * get Audit Note by id
	 */
	public String getAuditNoteById(Long id, String category){
		String note = "";
		Criteria crit;
		try {
			crit = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);
			if(ApplicationConstants.CATEGORY_ACTIVE.equalsIgnoreCase(category)){
				note = "activeNotes";
			}else if(ApplicationConstants.CATEGORY_NEW.equalsIgnoreCase(category)){
				note = "newNotes";
			}else if(ApplicationConstants.CATEGORY_DELETED.equalsIgnoreCase(category)){
				note = "deletedNotes";
			}else if(ApplicationConstants.CATEGORY_INACTIVE.equalsIgnoreCase(category)){
				note="inactiveNotes";
			}
			crit.setProjection(Projections.property(note));
			crit.add(Restrictions.eq("id", id));
			return (String)crit.uniqueResult();
		} catch (HibernateException e) {
			log.error("getAuditNoteById failed for id=" + id + e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * Get all audit accounts for a specific audit
	 * 
	 * @param auditId
	 * @return
	 */
	public List<EmAuditAccountsVw> getAllAccountsByAuditId(Long auditId) {
		log.debug("retrieving all accounts from audit view for auditId: " + auditId);
		try {

			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.eq("audit.id", auditId));
			
			// Only retrieve necessary columns	
			criteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id")
					.add(Projections.property("deletedDate"), "deletedDate")
					.add(Projections.property("createdDate"), "createdDate")
					.add(Projections.property("inactiveUserFlag"), "inactiveUserFlag")
					.add(Projections.property("nedIc"), "nedIc")
					.add(Projections.property("parentNedOrgPath"), "parentNedOrgPath")
					.add(Projections.property("nciDoc"), "nciDoc")
					.add(Projections.property("activeSubmittedBy"), "activeSubmittedBy")
					.add(Projections.property("newSubmittedBy"), "newSubmittedBy")
					.add(Projections.property("deletedSubmittedBy"), "deletedSubmittedBy")
					.add(Projections.property("inactiveSubmittedBy"), "inactiveSubmittedBy"));

			List<EmAuditAccountsVw> auditList = null;
			auditList = criteria.setResultTransformer(new AliasToBeanResultTransformer(EmAuditAccountsVw.class))
					.list();

			return auditList;
		} catch (Throwable e) {
			log.error("getAllAccountsByAuditId failed for auditId=" + auditId + e.getMessage(), e);
			throw e;
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

		// audit id
		criteria.add(Restrictions.eq("audit.id", searchVO.getAuditId()));
		
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
	 * Adding user specific search criteria for deleted accounts
	 * 
	 * @param criteria
	 * @param searchVO
	 * @return
	 */
	private Criteria addDeletedSearchCriteria(final Criteria criteria, final AuditSearchVO searchVO) {
		log.debug("adding search criteria for IMPAC II account query in audit view - deleted accounts: " + searchVO);

		// audit id
		criteria.add(Restrictions.eq("audit.id", searchVO.getAuditId()));
		
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
				criteria.add(Restrictions.eq("deletedByParentOrgPath", searchVO.getOrganization().trim()));
			}
		}
		
		// excludeNCIOrgs
		if (searchVO.isExcludeNCIOrgs()) {
			criteria.add(Restrictions.eq("deletedByNciDoc", ApplicationConstants.NCI_DOC_OTHER));
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
		} catch (Throwable e) {
			log.error("getAccountActivityT failed for eaaId=" + eaaId + " category=" + category + e.getMessage(), e);
			throw e;
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
			log.debug("saveOrUpdateActivity successful");
		} catch (Throwable e) {
			log.error("saveOrUpdateActivity failed", e);
			throw e;
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
	
	/**
	 * Add Sort Order
	 * 
	 * @param criteria
	 * @param paginatedList
	 * @return
	 */
	private Criteria addSortOrder(Criteria criteria, PaginatedListImpl paginatedList) {
		String sortOrderCriterion = paginatedList.getSortCriterion();
		String sortOrder = paginatedList.getSqlSortDirection();
		
		if (!StringUtils.isBlank(sortOrderCriterion)) {
			if (sortOrderCriterion.equalsIgnoreCase("fullName")) {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc")) {
					criteria.addOrder(Order.asc("lastName"));
					criteria.addOrder(Order.asc("firstName"));
				} else {
					criteria.addOrder(Order.desc("lastName"));
					criteria.addOrder(Order.desc("firstName"));
				}
			}else if(sortOrderCriterion.equalsIgnoreCase("createdBy")){
				if(StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					criteria.addOrder(Order.asc("createdByUserId"));
				}else{
					criteria.addOrder(Order.desc("createdByUserId"));
				}
			}else if(sortOrderCriterion.equalsIgnoreCase("deletedBy")){
				if(StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					criteria.addOrder(Order.asc("deletedByUserId"));
				}else{
					criteria.addOrder(Order.desc("deletedByUserId"));
				}
			}else if(sortOrderCriterion.equalsIgnoreCase("maxLastLoginDate")){
				if(StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					criteria.addOrder(Order.asc("lastLoginDate"));
				}else{
					criteria.addOrder(Order.desc("lastLoginDate"));
				}
			}else if(sortOrderCriterion.equalsIgnoreCase("impaciiUserIdNetworkId")){
				if(StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					criteria.addOrder(Order.asc("impaciiUserId"));
					criteria.addOrder(Order.asc("nihNetworkId"));
				}else{
					criteria.addOrder(Order.desc("impaciiUserId"));
					criteria.addOrder(Order.desc("nihNetworkId"));
				}
				
			} else if (sortOrderCriterion.equalsIgnoreCase("accountCreatedDate")) {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
					criteria.addOrder(Order.asc("createdDate"));
				else
					criteria.addOrder(Order.desc("createdDate"));
			} else if (sortOrderCriterion.equalsIgnoreCase("accountDeletedDate")) {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
					criteria.addOrder(Order.asc("deletedDate"));
				else
					criteria.addOrder(Order.desc("deletedDate"));
			} else if (sortOrderCriterion.equalsIgnoreCase("discrepancy")) {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc")) {
					criteria.addOrder(Order.asc("sodFlag"));
					criteria.addOrder(Order.asc("icDiffFlag"));
					criteria.addOrder(Order.asc("nedInactiveFlag"));
					criteria.addOrder(Order.asc("lastNameDiffFlag"));
				}
				else {
					criteria.addOrder(Order.desc("sodFlag"));
					criteria.addOrder(Order.desc("icDiffFlag"));
					criteria.addOrder(Order.desc("nedInactiveFlag"));
					criteria.addOrder(Order.desc("lastNameDiffFlag"));
				}
			} else {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
					criteria.addOrder(Order.asc(sortOrderCriterion));
				else
					criteria.addOrder(Order.desc(sortOrderCriterion));
			}
		}
		return criteria;
	}

	private PaginatedListImpl getPaginatedListResult(PaginatedListImpl paginatedList, Criteria criteria, boolean all) {
		final int objectsPerPage = paginatedList.getObjectsPerPage();
		final int firstResult = objectsPerPage * paginatedList.getIndex();
			
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
	}
}
