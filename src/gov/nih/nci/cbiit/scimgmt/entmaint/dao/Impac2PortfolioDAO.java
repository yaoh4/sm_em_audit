package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

// Generated Feb 13, 2015 3:58:29 PM by Hibernate Tools 3.4.0.CR1

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioNotesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DAO for domain model class EmPortfolioVw.
 * 
 * @see gov.nih.nci.cbiit.scimgmt.EmPortfolioVw
 * @author Hibernate Tools
 */
@Component
public class Impac2PortfolioDAO {

	private static final Log log = LogFactory.getLog(Impac2PortfolioDAO.class);
	public static final long PORTFOLIO_CATEGORY_ACTIVE = 22;
	public static final long PORTFOLIO_CATEGORY_NEW = 23;
	public static final long PORTFOLIO_CATEGORY_DELETED = 24;
	public static final long PORTFOLIO_CATEGORY_DISCREPANCY = 25;

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private NciUser nciUser;
	
	/**
	 * Search IMPACII portfolio accounts
	 * @param searchVO
	 * @return
	 */
	public List<EmPortfolioVw> searchImpac2Accounts(final AuditSearchVO searchVO) {
		log.debug("searching for IMPAC II accounts in portfolio view: " + searchVO);

		try {
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmPortfolioVw.class);

			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);

			List<EmPortfolioVw> portfolioList = criteria.list();
			return portfolioList;

		} catch (final RuntimeException re) {
			log.error("Error while searching", re);
			throw re;
		}

	}
	
	/**
	 * Saving user entered notes for an account
	 * 
	 * @param id
	 * @param text
	 * @return
	 */
	public DBResult saveNotes(String id, String text) {
		log.debug("saveNotes: saving notes for user," + id);
		DBResult result = new DBResult();
		try {
			EmPortfolioNotesT notes = findNotesById(id);
			if(notes == null) {
				notes = new EmPortfolioNotesT();
				notes.setImpaciiUserId(id);
				notes.setCreateUserId(nciUser.getOracleId());
				notes.setCreateDate(new Date());
			}
			// save notes
			notes.setLastChangeUserId(nciUser.getOracleId());
			notes.setLastChangeDate(new Date());
			notes.setNotes(text);
			saveOrUpdateNotes(notes);
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
	 * Add user entered search criteria
	 * 
	 * @param criteria
	 * @param searchVO
	 * @return
	 */
	private Criteria addSearchCriteria(final Criteria criteria, final AuditSearchVO searchVO) {
		// firstName partial search
		if (!StringUtils.isBlank(searchVO.getUserFirstname())) {
			criteria.add(Restrictions.ilike("nedFirstName", searchVO.getUserFirstname().trim(), MatchMode.START));
		}
		// lastName partial search
		if (!StringUtils.isBlank(searchVO.getUserLastname())) {
			criteria.add(Restrictions.ilike("nedLastName", searchVO.getUserLastname().trim(), MatchMode.START));
		}

		// org
		if (!StringUtils.isBlank(searchVO.getOrganization())
				&& !StringUtils.equalsIgnoreCase(searchVO.getOrganization(), ApplicationConstants.NCI_DOC_ALL)) {
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

		if (searchVO.getCategory() == PORTFOLIO_CATEGORY_ACTIVE) {
			addActiveCriteria(criteria, searchVO);
		} else if (searchVO.getCategory() == PORTFOLIO_CATEGORY_NEW) {
			addNewCriteria(criteria, searchVO);
		} else if (searchVO.getCategory() == PORTFOLIO_CATEGORY_DELETED) {
			addDeletedCriteria(criteria, searchVO);
		} else if (searchVO.getCategory() == PORTFOLIO_CATEGORY_DISCREPANCY) {
			addActiveCriteria(criteria, searchVO);
		}

		return criteria;
	}

	/**
	 * Add criteria specific to active accounts
	 * 
	 * @param criteria
	 * @param searchVO
	 * @return
	 */
	private Criteria addActiveCriteria(final Criteria criteria, final AuditSearchVO searchVO) {
		// Criteria specific to active accounts
		criteria.add(Restrictions.isNull("deletedDate"));

		return criteria;
	}

	/**
	 * Add criteria specific to new accounts
	 * 
	 * @param criteria
	 * @param searchVO
	 * @return
	 */
	private Criteria addNewCriteria(final Criteria criteria, final AuditSearchVO searchVO) {
		// Criteria specific to new accounts
		if(searchVO.getDateRangeStartDate() != null) {
			criteria.add(Restrictions.ge("createdDate", new java.sql.Date(searchVO.getDateRangeStartDate().getTime())));
		}
		if(searchVO.getDateRangeEndDate() != null) {
			criteria.add(Restrictions.le("createdDate", new java.sql.Date(searchVO.getDateRangeEndDate().getTime())));
		}

		return criteria;
	}

	/**
	 * Add criteria specific to deleted accounts
	 * 
	 * @param criteria
	 * @param searchVO
	 * @return
	 */
	private Criteria addDeletedCriteria(final Criteria criteria, final AuditSearchVO searchVO) {
		// Criteria specific to deleted accounts
		criteria.add(Restrictions.isNotNull("deletedDate"));
		if(searchVO.getDateRangeStartDate() != null) {
			criteria.add(Restrictions.ge("deletedDate", new java.sql.Date(searchVO.getDateRangeStartDate().getTime())));
		}
		if(searchVO.getDateRangeEndDate() != null) {
			criteria.add(Restrictions.le("deletedDate", new java.sql.Date(searchVO.getDateRangeEndDate().getTime())));
		}

		return criteria;
	}
	
	/**
	 * Retrieve notes using id
	 * @param id
	 * @return
	 */
	private EmPortfolioNotesT findNotesById(String id) {
		log.debug("getting EmPortfolioNotesT instance with id: " + id);
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmPortfolioNotesT.class);
			crit.add(Restrictions.eq("impaciiUserId", id));
			EmPortfolioNotesT instance = (EmPortfolioNotesT) crit.uniqueResult();
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
	 * Save or update notes
	 * 
	 * @param transientInstance
	 *            the transient instance
	 */
	private void saveOrUpdateNotes(final EmPortfolioNotesT transientInstance) {
		log.debug("saveOrUpdate EmPortfolioNotesT instance");
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
