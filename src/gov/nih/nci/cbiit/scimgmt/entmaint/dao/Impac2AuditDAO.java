package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

// Generated Feb 13, 2015 3:58:29 PM by Hibernate Tools 3.4.0.CR1

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountActivityT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountsT;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
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
import org.hibernate.criterion.Conjunction;
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
	@Autowired
	private LookupService lookupService;
	@Autowired
	private AdminService adminService;
	
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
		PaginatedListImpl<EmAuditAccountsVw> pListImpl = null;
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
			criteria.add(Restrictions.sqlRestriction("trunc(created_date) <= impacii_to_date"));

			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.ACTIVE_ACTION_NOACTION) ) {
				Disjunction dc = Restrictions.disjunction();
	            dc.add(Restrictions.isNull("activeAction.id"));
	            dc.add(Restrictions.eq("activeUnsubmittedFlag", ApplicationConstants.FLAG_YES));
	            criteria.add(dc);
			}
			else if(!StringUtils.isBlank(searchVO.getAct()) && (Long.parseLong(searchVO.getAct()) ==  ApplicationConstants.ACTIVE_ACTION_TRANSFER) ) {
				criteria.add(Restrictions.isNotNull("transferToNedOrgPath"));
			}

			else if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.ACTIVE_ACTION_ALL) ) {
				criteria.add(Restrictions.eq("activeAction.id", new Long(searchVO.getAct())));
				criteria.add(Restrictions.eq("activeUnsubmittedFlag", ApplicationConstants.FLAG_NO));
			}

			pListImpl =  getPaginatedListResult(paginatedList, criteria, all);
			return pListImpl;

		} catch (Throwable e) {
			log.error("Error while searching for IMPAC II active accounts in audit view", e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in parameters: paginatedList - " + paginatedList + ", searchVO - " + searchVO + ", all - " + all);
			log.error("Outgoing parameters: PaginatedList - " + pListImpl);
			
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
		PaginatedListImpl<EmAuditAccountsVw> pListImpl = null;
		try {
	
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			criteria = addSortOrder(criteria, paginatedList);
			
			// Criteria specific to new accounts
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.geProperty("createdDate", "audit.impaciiFromDate"));
			criteria.add(Restrictions.sqlRestriction("trunc(created_date) <= impacii_to_date"));

			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.NEW_ACTION_NOACTION) ) {
				Disjunction dc = Restrictions.disjunction();
				dc.add(Restrictions.isNull("newAction.id"));
				dc.add(Restrictions.eq("newUnsubmittedFlag", ApplicationConstants.FLAG_YES));
	            criteria.add(dc);
			}
			else if(!StringUtils.isBlank(searchVO.getAct()) && (Long.parseLong(searchVO.getAct()) ==  ApplicationConstants.NEW_ACTION_TRANSFER) ) {
				criteria.add(Restrictions.isNotNull("transferToNedOrgPath"));
			}
			else if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.NEW_ACTION_ALL)) {
				criteria.add(Restrictions.eq("newAction.id", new Long(searchVO.getAct())));
				criteria.add(Restrictions.eq("newUnsubmittedFlag", ApplicationConstants.FLAG_NO));
			}			

			pListImpl = getPaginatedListResult(paginatedList, criteria, all);
			return pListImpl;
			
		} catch (Throwable e) {
			log.error("searching for IMPAC II new accounts in audit view", e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in Parameters: paginatedList - " + paginatedList + ", searchVO - " + searchVO + ", all - " + all );
			log.error("Outgoing parameters: PaginatedList - " + pListImpl);
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
		PaginatedListImpl<EmAuditAccountsVw> pListImpl = null;
		
		try {

			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			criteria = addSortOrder(criteria, paginatedList);
			
			// Criteria specific to deleted accounts
			criteria.createAlias("audit", "audit");
			criteria.add(Restrictions.geProperty("deletedDate", "audit.impaciiFromDate"));
			criteria.add(Restrictions.sqlRestriction("trunc(deleted_date) <= impacii_to_date"));

			// Add user specific search criteria
			addDeletedSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.DELETED_ACTION_NOACTION) ) {
				Disjunction dc = Restrictions.disjunction();
				dc.add(Restrictions.isNull("deletedAction.id"));
				dc.add(Restrictions.eq("deletedUnsubmittedFlag", ApplicationConstants.FLAG_YES));
	            criteria.add(dc);
			}
			else if(!StringUtils.isBlank(searchVO.getAct()) && (Long.parseLong(searchVO.getAct()) ==  ApplicationConstants.DELETED_ACTION_TRANSFER) ) {
				criteria.add(Restrictions.isNotNull("transferToNedOrgPath"));
			}
			else if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.DELETED_ACTION_ALL)) {
				criteria.add(Restrictions.eq("deletedAction.id", new Long(searchVO.getAct())));
				criteria.add(Restrictions.eq("deletedUnsubmittedFlag", ApplicationConstants.FLAG_NO));
			}

			pListImpl = getPaginatedListResult(paginatedList, criteria, all);
			return pListImpl;
			
		} catch (Throwable e) {
			log.error("searching for IMPAC II deleted accounts in audit view", e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in Parameters: PaginatedListImpl - " + paginatedList + ", seachVO - " + searchVO +", all - " + all);
			log.error("Outgoing parameters: PaginatedListImpl - " + pListImpl);
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
		PaginatedListImpl<EmAuditAccountsVw> pListImpl = null;
		
		try {

			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			criteria = addSortOrder(criteria, paginatedList);
			
			// Criteria specific to inactive accounts
			criteria.add(Restrictions.eq("inactiveUserFlag", ApplicationConstants.FLAG_YES));
			criteria.createAlias("audit", "audit");
			
			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.INACTIVE_ACTION_NOACTION) ) {
				Disjunction dc = Restrictions.disjunction();
				dc.add(Restrictions.isNull("inactiveAction.id"));
				dc.add(Restrictions.eq("inactiveUnsubmittedFlag", ApplicationConstants.FLAG_YES));
	            criteria.add(dc);
			}
			else if(!StringUtils.isBlank(searchVO.getAct()) && (Long.parseLong(searchVO.getAct()) ==  ApplicationConstants.INACTIVE_ACTION_TRANSFER) ) {
				criteria.add(Restrictions.isNotNull("transferToNedOrgPath"));
			}
			else if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.INACTIVE_ACTION_ALL)) {
				criteria.add(Restrictions.eq("inactiveAction.id", new Long(searchVO.getAct())));
				criteria.add(Restrictions.eq("inactiveUnsubmittedFlag", ApplicationConstants.FLAG_NO));
			}				

			pListImpl = getPaginatedListResult(paginatedList, criteria, all);
			return pListImpl;
			
		} catch (Throwable e) {
			log.error("searching for IMPAC II inactive accounts in audit view", e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in Parameters: PaginatedListImpl - " + paginatedList + ", searchVO - " + searchVO +", all - " + all);
			log.error("Outgoing parameter: PaginatedListImpl - " + pListImpl);
			throw e;
		}
	}
	
	/**
	 * Search EmAuditAccountsVw for Exclude from Audit accounts
	 * 
	 * @param paginatedList
	 * @param searchVO
	 * @param all
	 * @return
	 */
	public PaginatedListImpl<EmAuditAccountsVw> searchExcludedAccounts(PaginatedListImpl paginatedList, final AuditSearchVO searchVO, Boolean all) {
		log.debug("searching for IMPAC II Exclude from Audit accounts in audit view: " + searchVO);
		PaginatedListImpl<EmAuditAccountsVw> pListImpl = null;
		
		try {

			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);

			// Sort order
			criteria = addSortOrder(criteria, paginatedList);
			
			// Criteria for excluded accounts
			criteria.createAlias("audit", "audit");
			
			// Add audit id to search criteria
			addSearchCriteria(criteria, searchVO);
			
			// Look for Exclude from Audit action
			Conjunction c1 = Restrictions.conjunction();
			c1.add(Restrictions.eq("activeAction.id", ApplicationConstants.ACTIVE_EXCLUDE_FROM_AUDIT));
			c1.add(Restrictions.eq("activeUnsubmittedFlag", ApplicationConstants.FLAG_NO));
			
			Conjunction c2 = Restrictions.conjunction();
			c2.add(Restrictions.eq("newAction.id", ApplicationConstants.NEW_EXCLUDE_FROM_AUDIT));
			c2.add(Restrictions.eq("newUnsubmittedFlag", ApplicationConstants.FLAG_NO));
			
			Conjunction c3 = Restrictions.conjunction();
			c3.add(Restrictions.eq("deletedAction.id", ApplicationConstants.DELETED_EXCLUDE_FROM_AUDIT));
			c3.add(Restrictions.eq("deletedUnsubmittedFlag", ApplicationConstants.FLAG_NO));
			
			Conjunction c4 = Restrictions.conjunction();
			c4.add(Restrictions.eq("inactiveAction.id", ApplicationConstants.INACTIVE_EXCLUDE_FROM_AUDIT));
			c4.add(Restrictions.eq("inactiveUnsubmittedFlag", ApplicationConstants.FLAG_NO));
			
			Disjunction dc = Restrictions.disjunction();
			dc.add(c1);
			dc.add(c2);
			dc.add(c3);
			dc.add(c4);
	        criteria.add(dc);

			pListImpl = getPaginatedListResult(paginatedList, criteria, all);
			return pListImpl;
			
		} catch (Throwable e) {
			log.error("searching for IMPAC II Exclude from Audit accounts in audit view", e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in Parameters: PaginatedListImpl - " + paginatedList + ", searchVO - " + searchVO +", all - " + all);
			log.error("Outgoing parameter: PaginatedListImpl - " + pListImpl);
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
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in parameters: category - " + category +", eaaId - " + eaaId + ", actionId - " + actionId + ", actionComments - " + actionComments + ", date - " + date);
			log.error("Outgoing Parameter: DBResult - " + result.getStatus());
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
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in parameters: category - " + category +", eaaId - " + eaaId);
			log.error("Outgoing Parameter: DBResult - " + result.getStatus());
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
		EmAuditAccountsVw result = null;
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsVw.class);
			crit.add(Restrictions.eq("id", id));
			result = (EmAuditAccountsVw) crit.uniqueResult();
			return result;
		} catch (Throwable e) {
			log.error("getAuditAccountById failed for id=" + id + e.getMessage(), e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in parameters: id - " + id);
			log.error("Outgoing Parameter: EmAuditAccountsVw - " + result);
			throw e;
		}
	}
	
	/**
	 * get Audit Note by id
	 */
	public String getAuditNoteById(Long id, String category){
		String note = "";
		Criteria crit = null;
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
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in parameters: id - " + id +", category - " + category);
			log.error("Outgoing Parameter: crit - " + (crit == null ? "NULL" : crit.uniqueResult()));
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
		List<EmAuditAccountsVw> auditList = null;
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
					.add(Projections.property("inactiveSubmittedBy"), "inactiveSubmittedBy")
					.add(Projections.property("deletedByParentOrgPath"), "deletedByParentOrgPath")
					.add(Projections.property("deletedByNciDoc"), "deletedByNciDoc")
					.add(Projections.property("activeAction"), "activeAction")
					.add(Projections.property("activeUnsubmittedFlag"), "activeUnsubmittedFlag")
					.add(Projections.property("newAction"), "newAction")
					.add(Projections.property("newUnsubmittedFlag"), "newUnsubmittedFlag")
					.add(Projections.property("deletedAction"), "deletedAction")
					.add(Projections.property("deletedUnsubmittedFlag"), "deletedUnsubmittedFlag"));

			
			auditList = criteria.setResultTransformer(new AliasToBeanResultTransformer(EmAuditAccountsVw.class))
					.list();

			return auditList;
			
		} catch (Throwable e) {
			log.error("getAllAccountsByAuditId failed for auditId=" + auditId + e.getMessage(), e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in parameters: auditId - " + auditId);
			log.error("Outgoing Parameter: size of EmAuditAccountsVw - " + (auditList == null ? "NULL" : auditList.size()));
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
			criteria.add(Restrictions.ilike("firstName", searchVO.getUserFirstname().trim(), MatchMode.START));
		}
		// lastName partial search
		if (!StringUtils.isBlank(searchVO.getUserLastname())) {
			criteria.add(Restrictions.ilike("lastName", searchVO.getUserLastname().trim(), MatchMode.START));
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
			criteria.add(Restrictions.ilike("firstName", searchVO.getUserFirstname().trim(), MatchMode.START));
		}
		// lastName partial search
		if (!StringUtils.isBlank(searchVO.getUserLastname())) {
			criteria.add(Restrictions.ilike("lastName", searchVO.getUserLastname().trim(), MatchMode.START));
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
					criteria.addOrder(Order.asc("lastName"));
					criteria.addOrder(Order.asc("firstName"));
				}
				else {
					criteria.addOrder(Order.desc("sodFlag"));
					criteria.addOrder(Order.desc("icDiffFlag"));
					criteria.addOrder(Order.desc("nedInactiveFlag"));
					criteria.addOrder(Order.desc("lastNameDiffFlag"));
					criteria.addOrder(Order.desc("lastName"));
					criteria.addOrder(Order.desc("firstName"));
				}
			} else {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
					criteria.addOrder(Order.asc(sortOrderCriterion));
				else
					criteria.addOrder(Order.desc(sortOrderCriterion));
			}
		}
		else {
			// Default sort, add lastname, firstname asc
			criteria.addOrder(Order.asc("lastName"));
			criteria.addOrder(Order.asc("firstName"));
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
	
	/**
	 * Transfers account to different organization.
	 * @param accountId, nihNetworkId, auditId, parentNedOrgPath, actionId, actionComments, transferOrg, category, isImpac2Transfer
     * @return DBResult
	 * @throws Exception 
	 */
	public DBResult transfer(Long accountId, String nihNetworkId, Long auditId, String parentNedOrgPath, Long actionId, String actionComments, String transferOrg, String category, boolean isImpac2Transfer) throws Exception {
		DBResult result = new DBResult();
		try {			
			EmAuditAccountsT account = null;					
			if(isImpac2Transfer){
				//For Impac2 Transfer retrieve EmAuditAccountsT based on id
				account = getAccountsT(accountId);					
			}
			else{
				//If this Impac2 Transfer is triggered after I2E Transfer, then retrieve EmAuditAccountsT based on nihNetworkId and auditId.
				account = getAccountsT(nihNetworkId, auditId);			
			}			
			if(account != null){
				account.setTransferFromNedOrgPath(parentNedOrgPath);
				account.setTransferToNedOrgPath(transferOrg);
				account.setTransferredDate(new Date());
				saveOrUpdateEmAuditAccountsT(account);
				
				AppLookupT cat = null;				
				if(category != null){
					//Category is not null because this method is being called from SubmitAction to transfer the Impac2 account.
					//Category is available for Impac2 accounts.
					cat = lookupService.getAppLookupByCode(ApplicationConstants.APP_LOOKUP_CATEGORY_LIST, category);
				}
				else{
					//If category is null, that means this method is being called by SubmitI2eAction after transferring the I2e account.
					//In this situation, we don't have category. It needs to be computed on the fly for Impac2Account.
					cat = getCategory(account);
				}	
								
				EmAuditAccountActivityT activity = getAccountActivityT(account.getId(), cat.getCode());
				if(activity == null) {
					activity = new EmAuditAccountActivityT();
					activity.setEaaId(account.getId());
					activity.setCategory(cat);
					activity.setCreateUserId(nciUser.getUserId().toUpperCase());
					activity.setCreateDate(new Date());
				} else {
					activity.setLastChangeUserId(nciUser.getUserId().toUpperCase());
					activity.setLastChangeDate(new Date());
				}
				activity.setActionId(actionId);
				activity.setNotes(actionComments);
				activity.setUnsubmittedFlag(ApplicationConstants.FLAG_NO);

				saveOrUpdateActivity(activity);			
				result.setStatus(DBResult.SUCCESS);
			}
			else{
				log.error("EmAuditAccountsT doesn't exist for ID: "+accountId);
			}
			
		} catch (Throwable e) {
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in Parameters: id - " + accountId +", nihNetworkId - " + nihNetworkId + ", auditId - " + auditId + ", parentNedOrgPath - " + parentNedOrgPath + ",actionId" + actionId +", actionComments - " + actionComments  + ", transferOrg" + transferOrg + ", category"+ category + ", isImpac2Transfer"+ isImpac2Transfer);			
			String errorString = "Transfer Failed for Account with Id= " + accountId + " " + e.getMessage();
			log.error(errorString, e);
			throw new Exception(errorString,e);
		}
		return result;
	}
	
	/**
	 * Get account using id.
	 * @param id
	 * @return EmAuditAccountsT
	 * @throws Exception 
	 */
	private EmAuditAccountsT getAccountsT(Long id) throws Exception {
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsT.class);
			crit.add(Restrictions.eq("id", id));
			EmAuditAccountsT result = (EmAuditAccountsT) crit.uniqueResult();
			return result;
		} catch (Throwable e) {
			String errorString = "get EmAuditAccountsT failed for Id=" + id + e.getMessage();
			log.error(errorString, e);
			throw new Exception(errorString,e);
		}
	}
	
	/**
	 * Get account using nihNetworkId , auditId.
	 * @param nihNetworkId , auditId
	 * @return EmAuditAccountsT
	 * @throws Exception 
	 */
	private EmAuditAccountsT getAccountsT(String nihNetworkId, Long auditId) throws Exception {
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmAuditAccountsT.class);
			crit.add(Restrictions.eq("nihNetworkId", nihNetworkId));
			crit.add(Restrictions.eq("eauId", auditId));
			EmAuditAccountsT result = (EmAuditAccountsT) crit.uniqueResult();
			return result;
		} catch (Throwable e) {
			String errorString = "get EmAuditAccountsT failed for nihNetworkId=" + nihNetworkId + " and Auddit Id "+ auditId + e.getMessage();
			log.error(errorString, e);
			throw new Exception(errorString,e);
		}
	}
	
	/**
	 * Save or update Action
	 * 
	 * @param transientInstance
	 *            the transient instance
	 */
	private void saveOrUpdateEmAuditAccountsT(final EmAuditAccountsT transientInstance) {
		log.debug("saveOrUpdate EmI2eAuditAccountsT instance");
		try {
			Session session = sessionFactory.getCurrentSession();
			session.saveOrUpdate(transientInstance);
			log.debug("saveOrUpdateAction successful");
		} catch (Throwable e) {
			log.error("saveOrUpdateAction failed", e);
			throw e;
		}
	}
	
	/**
	 * This method computes category for the Impac2 account.
	 * @param account
	 * @return AppLookupT
	 */
	public AppLookupT getCategory(EmAuditAccountsT account){
		String category = "";
		EmAuditsVw emAudit = adminService.retrieveAuditVO(account.getEauId());	
		
		if((account.getDeletedDate() == null || account.getDeletedDate().after(emAudit.getImpaciiToDate())) && (account.getCreatedDate().before(emAudit.getImpaciiToDate()) || account.getCreatedDate().equals(emAudit.getImpaciiToDate()))){
			category = ApplicationConstants.CATEGORY_ACTIVE;
		}
		else if((account.getCreatedDate().after(emAudit.getImpaciiFromDate()) || account.getCreatedDate().equals(emAudit.getImpaciiFromDate())) && (account.getCreatedDate().before(emAudit.getImpaciiToDate()) || account.getCreatedDate().equals(emAudit.getImpaciiToDate()))){
			category = ApplicationConstants.CATEGORY_NEW;
		}
		else if((account.getDeletedDate().after(emAudit.getImpaciiFromDate()) || account.getDeletedDate().equals(emAudit.getImpaciiFromDate())) && (account.getDeletedDate().before(emAudit.getImpaciiToDate()) || account.getDeletedDate().equals(emAudit.getImpaciiToDate()))){
			category = ApplicationConstants.CATEGORY_DELETED;
		}
		else if(ApplicationConstants.FLAG_YES.equalsIgnoreCase(account.getInactiveUserFlag())){
			category = ApplicationConstants.CATEGORY_INACTIVE;
		}		
		return lookupService.getAppLookupByCode(ApplicationConstants.APP_LOOKUP_CATEGORY_LIST, category);
	}
}
