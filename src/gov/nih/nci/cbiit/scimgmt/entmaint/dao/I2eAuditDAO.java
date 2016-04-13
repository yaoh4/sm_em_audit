package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountsT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
 * DAO for domain model class EmAuditI2eAccountsVw.
 * @see gov.nih.nci.cbiit.scimgmt.EmAuditI2eAccountsVw
 * @author Hibernate Tools
 */
@Component
public class I2eAuditDAO {

	public static Logger log = Logger.getLogger(I2eAuditDAO.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private NciUser nciUser;
	
	/**
	 * Search EmAuditI2eAccountsVw for active accounts
	 * 
	 * @param paginatedList
	 * @param searchVO
	 * @param all
	 * @return
	 */
	public PaginatedListImpl<EmI2eAuditAccountsVw> searchActiveAccounts(PaginatedListImpl paginatedList, final AuditSearchVO searchVO, Boolean all) {
		log.debug("searching for I2E active accounts in audit view: " + searchVO);
		PaginatedListImpl<EmI2eAuditAccountsVw> paginatedListResult = null;
		try {
	  
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmI2eAuditAccountsVw.class);
			
			// Sort order
			criteria = addSortOrder(criteria, paginatedList);
			
			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);
			
			// action
			if (!StringUtils.isBlank(searchVO.getAct()) && StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.ACTIVE_ACTION_NOACTION) ) {
				Disjunction dc = Restrictions.disjunction();
	            dc.add(Restrictions.isNull("action.id"));
	            dc.add(Restrictions.eq("unsubmittedFlag", ApplicationConstants.FLAG_YES));
	            criteria.add(dc);
			}
			else if(!StringUtils.isBlank(searchVO.getAct()) && (Long.parseLong(searchVO.getAct()) ==  ApplicationConstants.ACTIVE_ACTION_TRANSFER) ) {
				criteria.add(Restrictions.isNotNull("transferToNedOrgPath"));
			}
			else if (!StringUtils.isBlank(searchVO.getAct()) && !StringUtils.equalsIgnoreCase(searchVO.getAct(), ApplicationConstants.ACTIVE_ACTION_ALL) ) {
				criteria.add(Restrictions.eq("action.id", new Long(searchVO.getAct())));
				criteria.add(Restrictions.eq("unsubmittedFlag", ApplicationConstants.FLAG_NO));
			}			

			paginatedListResult = getPaginatedListResult(paginatedList, criteria, all);
			
			return paginatedListResult;

		} catch (Throwable e) {
			log.error("Error while searching for I2E active accounts in audit view", e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in Parameters: PaginatedList - " + paginatedList + ", searchVO - " + searchVO + ", " + "all - " + all);
			log.error("Outgoing parameters: PaginatedListImpl - " + (paginatedListResult == null ? "NULL" : "object ID=" + paginatedListResult.toString()));
			throw e;
		}
	}
	
	/**
	 * Submit action taken for I2e audit accounts
	 * 
	 * @param id
	 * @param actionId
	 * @param actionComments
	 * @param date
	 * @return
	 */
	public DBResult submit(Long id, Long actionId, String actionComments, Date date) {
		DBResult result = new DBResult();
		try {
			EmI2eAuditAccountsT account = getAccountsT(id);
			if(account == null || account.getActionCreateDate() == null) {
				account.setActionCreateDate(date);
				account.setActionCreateUserId(nciUser.getUserId().toUpperCase());
			} else {
				account.setActionLastChangeUserId(nciUser.getUserId().toUpperCase());
				account.setActionLastChangeDate(date);
			}
			account.setActionId(actionId);
			account.setNotes(actionComments);
			account.setUnsubmittedFlag(ApplicationConstants.FLAG_NO);
			account.setLastSubmittedByUserId(nciUser.getUserId().toUpperCase());
			account.setLastSubmittedDate(date);
			saveOrUpdateAction(account);
			result.setStatus(DBResult.SUCCESS);
		} catch (Throwable e) {
			log.error("Submit Action in I2E Audit failed for Id=" + id + " actionId=" + actionId + " actionComments="
					+ actionComments + e.getMessage(), e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in Parameters: id - " + id +", actionId - " + actionId + ", date" + date);
			log.error("Outgoing parameters: DBResult - " + result.getStatus());
			throw e;
		}
		return result;
	}
	
	/**
	 * Unsubmit action previously taken on I2e audit account
	 * 
	 * @param id
	 * @return
	 */
	public DBResult unsubmit(Long id) {
		DBResult result = new DBResult();
		try {
			EmI2eAuditAccountsT account = getAccountsT(id);
			if(account != null) {
				account.setActionLastChangeUserId(nciUser.getUserId().toUpperCase());
				account.setActionLastChangeDate(new Date());
				account.setUnsubmittedFlag(ApplicationConstants.FLAG_YES);
				account.setLastSubmittedByUserId(null);
				account.setLastSubmittedDate(null);
				saveOrUpdateAction(account);
			} else {
				log.error("EmI2eAuditAccountsT row doesn't exist for Id=" + id);
			}
		} catch (Throwable e) {
			log.error("Unsubmit Action in I2E Audit failed for Id=" + id + e.getMessage(), e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("pass-in parameters: id - " + id);
			log.error("Outgoing Parameters: DBResult - " + result.getStatus());
			throw e;
		}
		result.setStatus(DBResult.SUCCESS);
		return result;
	}
	
	/**
	 * Get I2e audit account vw using id
	 * 
	 * @param id
	 * @return
	 */
	public EmI2eAuditAccountsVw getAuditAccountById(Long id) {
		EmI2eAuditAccountsVw result = null;
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmI2eAuditAccountsVw.class);
			crit.add(Restrictions.eq("id", id));
			result = (EmI2eAuditAccountsVw) crit.uniqueResult();
			return result;
		} catch (Throwable e) {
			log.error("getAuditAccountById failed for id=" + id + e.getMessage(), e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in parameters: id - " + id);
			log.error("Outgoing parameters: EmI2eAuditAccountsVw - " + (result == null? "NULL" : result.getId()));
			throw e;
		}
	}
	
	/**
	 * get Audit Note by id
	 */
	public String getAuditNoteById(Long id){
		Criteria crit = null;
		try {
			crit = sessionFactory.getCurrentSession().createCriteria(EmI2eAuditAccountsVw.class);
			crit.setProjection(Projections.property("notes"));
			crit.add(Restrictions.eq("id", id));
			return (String)crit.uniqueResult();
		} catch (HibernateException e) {
			log.error("getAuditNoteById failed for id=" + id + e.getMessage(), e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in parameters: id - " + id);
			log.error("Outgoing parameters: crit - " + (crit == null ? "NULL" : crit.uniqueResult()));
			throw e;
		}
	}
	
	/**
	 * Get all audit accounts for a specific audit
	 * 
	 * @param auditId
	 * @return
	 */
	public List<EmI2eAuditAccountsVw> getAllAccountsByAuditId(Long auditId) {
		log.debug("retrieving all I2e accounts from audit view for auditId: " + auditId);
		List<EmI2eAuditAccountsVw> auditList = null;
		try {

			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmI2eAuditAccountsVw.class);
			criteria.add(Restrictions.eq("auditId", auditId));
			
			// Only retrieve necessary columns	
			criteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id")
					.add(Projections.property("nedIc"), "nedIc")
					.add(Projections.property("parentNedOrgPath"), "parentNedOrgPath")
					.add(Projections.property("nciDoc"), "nciDoc")
					.add(Projections.property("submittedBy"), "submittedBy")
					.add(Projections.property("action"), "action")
					.add(Projections.property("unsubmittedFlag"), "unsubmittedFlag"));

			
			auditList = criteria.setResultTransformer(new AliasToBeanResultTransformer(EmI2eAuditAccountsVw.class))
					.list();

			return auditList;
		} catch (Throwable e) {
			log.error("getAllAccountsByAuditId failed for auditId=" + auditId + e.getMessage(), e);
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in Parameters: auditId - " + auditId);
			log.error("Outgoing Parameters: size od auditList - " + (auditList == null ? "NULL" : ""+auditList.size()));
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
		log.debug("adding search criteria for I2E account query in audit view: " + searchVO);

		// audit id
		criteria.add(Restrictions.eq("auditId", searchVO.getAuditId()));
		
		// firstName partial search
		if (!StringUtils.isBlank(searchVO.getUserFirstname())) {
			Disjunction dc = Restrictions.disjunction();
			dc.add(Restrictions.ilike("firstName", searchVO.getUserFirstname().trim(), MatchMode.START));
			dc.add(Restrictions.ilike("nedFirstName", searchVO.getUserFirstname().trim(), MatchMode.START));
			dc.add(Restrictions.ilike("i2eFirstName", searchVO.getUserFirstname().trim(), MatchMode.START));
			criteria.add(dc);
		}
		// lastName partial search
		if (!StringUtils.isBlank(searchVO.getUserLastname())) {
			Disjunction dc = Restrictions.disjunction();
			dc.add(Restrictions.ilike("lastName", searchVO.getUserLastname().trim(), MatchMode.START));
			dc.add(Restrictions.ilike("nedLastName", searchVO.getUserLastname().trim(), MatchMode.START));
			dc.add(Restrictions.ilike("i2eLastName", searchVO.getUserLastname().trim(), MatchMode.START));
			criteria.add(dc);
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
	private EmI2eAuditAccountsT getAccountsT(Long id) {
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmI2eAuditAccountsT.class);
			crit.add(Restrictions.eq("id", id));
			EmI2eAuditAccountsT result = (EmI2eAuditAccountsT) crit.uniqueResult();
			return result;
		} catch (Throwable e) {
			log.error("get EmI2eAuditAccountsT failed for Id=" + id + e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * Save or update Action
	 * 
	 * @param transientInstance
	 *            the transient instance
	 */
	private void saveOrUpdateAction(final EmI2eAuditAccountsT transientInstance) {
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
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					criteria.addOrder(Order.asc("createdByUserId"));
				}else{
					criteria.addOrder(Order.desc("createdByUserId"));
				}
			}else if (sortOrderCriterion.equalsIgnoreCase("i2eCreatedDate")) {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
					criteria.addOrder(Order.asc("createdDate"));
				else
					criteria.addOrder(Order.desc("createdDate"));
			}else if(sortOrderCriterion.equalsIgnoreCase("action")){
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
					criteria.addOrder(Order.asc("notes"));
				else
					criteria.addOrder(Order.desc("notes"));
			}else if (sortOrderCriterion.equalsIgnoreCase("discrepancy")) {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc")) {
					criteria.addOrder(Order.asc("sodFlag"));
					criteria.addOrder(Order.asc("nedInactiveFlag"));
					criteria.addOrder(Order.asc("noActiveRoleFlag"));
					criteria.addOrder(Order.asc("i2eOnlyFlag"));
					criteria.addOrder(Order.asc("activeRoleRemainderFlag"));
					criteria.addOrder(Order.asc("lastName"));
					criteria.addOrder(Order.asc("firstName"));
				}
				else {
					criteria.addOrder(Order.desc("sodFlag"));
					criteria.addOrder(Order.desc("nedInactiveFlag"));
					criteria.addOrder(Order.desc("noActiveRoleFlag"));
					criteria.addOrder(Order.desc("i2eOnlyFlag"));
					criteria.addOrder(Order.desc("activeRoleRemainderFlag"));
					criteria.addOrder(Order.desc("lastName"));
					criteria.addOrder(Order.desc("firstName"));
				}
			}else {
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
			
		List<EmI2eAuditAccountsVw> auditList = null;
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
	 * @param accountId, nihNetworkId, auditId, parentNedOrgPath, actionComments, transferOrg, isI2eTransfer
     * @return DBResult
	 * @throws Exception 
	 */
	public DBResult transfer(Long accountId, String nihNetworkId, Long auditId, String parentNedOrgPath, String actionComments, String transferOrg, boolean isI2eTransfer) throws Exception {
		DBResult result = new DBResult();
		try {
			EmI2eAuditAccountsT account = null;
			
			if(isI2eTransfer){
				//For I2e Transfer retrieve EmI2eAuditAccountsT based on id
				account = getAccountsT(accountId);					
			}
			else{
				//If this I2E Transfer is triggered after Impac2 Transfer, then retrieve EmI2eAuditAccountsT based on nihNetworkId and auditId.
				account = getAccountsT(nihNetworkId, auditId);			
			}			
			if(account != null){
				account.setActionLastChangeUserId(nciUser.getUserId().toUpperCase());
				account.setActionLastChangeDate(new Date());
				//If its fresh transfer OR update to transfer OR transfer after Undo
				if(account.getActionId() == null ||	account.getActionId() == ApplicationConstants.ACTIVE_ACTION_TRANSFER ||
						ApplicationConstants.FLAG_YES.equalsIgnoreCase(account.getUnsubmittedFlag())){
					account.setActionId(ApplicationConstants.ACTIVE_ACTION_TRANSFER);
					account.setNotes(actionComments);
					account.setUnsubmittedFlag(ApplicationConstants.FLAG_NO);	
				}
				account.setTransferFromNedOrgPath(parentNedOrgPath);
				account.setTransferToNedOrgPath(transferOrg);
				account.setTransferredDate(new Date());
				
				saveOrUpdateAction(account);
				result.setStatus(DBResult.SUCCESS);
			}
			else{
				log.error("EmI2eAuditAccountsT doesn't exist for ID: "+accountId);
			}
			
		} catch (Throwable e) {			
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in Parameters: id - " + accountId +", nihNetworkId - " + nihNetworkId + ", auditId - " + auditId + ", parentNedOrgPath - " + parentNedOrgPath + ", actionComments - " + actionComments + ", transferOrg" + transferOrg +", isI2eTransfer" + isI2eTransfer);
			String errorString = "Transfer Failed for Account with Id= " + accountId + " " + e.getMessage();
			log.error(errorString, e);
			throw new Exception(errorString,e);
		}
		return result;
	}
	
	/**
	 * Get account using nihNetworkId , auditId.
	 * @param nihNetworkId , auditId
	 * @return EmI2eAuditAccountsT
	 */
	private EmI2eAuditAccountsT getAccountsT(String nihNetworkId, Long auditId) throws Exception{
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmI2eAuditAccountsT.class);
			crit.add(Restrictions.eq("nihNetworkId", nihNetworkId));
			crit.add(Restrictions.eq("eauId", auditId));
			EmI2eAuditAccountsT result = (EmI2eAuditAccountsT) crit.uniqueResult();
			return result;
		} catch (Throwable e) {
			String errorString = "get EmI2eAuditAccountsT failed for nihNetworkId= " + nihNetworkId + " and Audit Id= "+ auditId + " " +e.getMessage();
			log.error(errorString, e);
			throw new Exception(errorString,e);
		}
	}
}
