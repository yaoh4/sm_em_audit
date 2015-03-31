package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

// Generated Feb 13, 2015 3:58:29 PM by Hibernate Tools 3.4.0.CR1

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioNotesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	 * 
	 * @param paginatedList
	 * @param searchVO
	 * @param all
	 * @return
	 */
	public PaginatedListImpl<EmPortfolioVw> searchImpac2Accounts(PaginatedListImpl paginatedList, final AuditSearchVO searchVO, Boolean all) {
		log.debug("searching for IMPAC II accounts in portfolio view: " + searchVO);

		try {
			final int objectsPerPage = paginatedList.getObjectsPerPage();
			final int firstResult = objectsPerPage * paginatedList.getIndex();
			String sortOrderCriterion = paginatedList.getSortCriterion();
			String sortOrder = paginatedList.getSqlSortDirection();
			
			Criteria criteria = null;
			if (searchVO.getCategory() == PORTFOLIO_CATEGORY_DISCREPANCY) {
				criteria = sessionFactory.getCurrentSession().createCriteria(EmDiscrepancyAccountsVw.class);
			}
			else {
				criteria = sessionFactory.getCurrentSession().createCriteria(EmPortfolioVw.class);
			}

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
						
			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);

			List<EmPortfolioVw> portfolioList = null;
			List<EmDiscrepancyAccountsVw> discrepancyList = null;
			
			if (searchVO.getCategory() == PORTFOLIO_CATEGORY_DISCREPANCY) {
				if(all) {
					discrepancyList = criteria.list();
					paginatedList.setTotal(discrepancyList.size());
				}
				else {
					discrepancyList = criteria.setFirstResult(firstResult)
							.setMaxResults(objectsPerPage)
							.list();
				}			
				paginatedList.setList(discrepancyList);			}
			else {
				if(all) {
					portfolioList = criteria.list();
					paginatedList.setTotal(portfolioList.size());
				}
				else {
					portfolioList = criteria.setFirstResult(firstResult)
							.setMaxResults(objectsPerPage)
							.list();
				}			
				paginatedList.setList(portfolioList);
			}

			if (!all && paginatedList.getFullListSize() == 0) {
				paginatedList.setTotal(getTotalResultCount(criteria));
			}

			return paginatedList;

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
	 * @param date
	 * @return
	 */
	public DBResult saveNotes(String id, String text, Date date) {
		log.debug("saveNotes: saving notes for user," + id);
		DBResult result = new DBResult();
		try {
			EmPortfolioNotesT notes = findNotesById(id);
			if(notes == null) {
				notes = new EmPortfolioNotesT();
				notes.setImpaciiUserId(id);
				notes.setCreateUserId(nciUser.getUserId().toUpperCase());
				notes.setCreateDate(date);
			}
			// save notes
			notes.setLastChangeUserId(nciUser.getUserId().toUpperCase());
			notes.setLastChangeDate(date);
			notes.setNotes(text);
			saveOrUpdateNotes(notes);
		} catch (RuntimeException re) {
			log.error("Save notes failed, " + re.getMessage());
			throw re;
		}
		result.setStatus(DBResult.SUCCESS);
		return result;
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
			addDiscrepancyCriteria(criteria, searchVO);
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
	 * Add criteria specific to accounts with discrepancy
	 * 
	 * @param criteria
	 * @param searchVO
	 * @return
	 */
	private Criteria addDiscrepancyCriteria(final Criteria criteria, final AuditSearchVO searchVO) {
		Disjunction disc = Restrictions.disjunction();
        disc.add(Restrictions.eq("sodFlag", true));
        disc.add(Restrictions.eq("icDiffFlag", true));
        disc.add(Restrictions.eq("nedInactiveFlag", true));
        disc.add(Restrictions.eq("lastNameDiffFlag", true));
        criteria.add(disc);
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
