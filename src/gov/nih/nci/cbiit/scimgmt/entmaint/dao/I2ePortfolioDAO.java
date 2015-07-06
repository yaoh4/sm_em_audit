package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

// Generated Feb 13, 2015 3:58:29 PM by Hibernate Tools 3.4.0.CR1

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioI2eVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioNotesT;
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
import org.hibernate.HibernateException;
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
public class I2ePortfolioDAO {

	private static final Log log = LogFactory.getLog(I2ePortfolioDAO.class);
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
	public PaginatedListImpl<EmPortfolioI2eVw> searchI2eAccounts(PaginatedListImpl paginatedList, final AuditSearchVO searchVO, Boolean all) {
		log.debug("searching for I2E accounts in portfolio view: " + searchVO);

		try {
			final int objectsPerPage = paginatedList.getObjectsPerPage();
			final int firstResult = objectsPerPage * paginatedList.getIndex();
			String sortOrderCriterion = paginatedList.getSortCriterion();
			String sortOrder = paginatedList.getSqlSortDirection();
			
			Criteria criteria = null;
			criteria = sessionFactory.getCurrentSession().createCriteria(EmPortfolioI2eVw.class);

			// Sort order
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
				}else if (sortOrderCriterion.equalsIgnoreCase("accountCreatedDate")) {
					if (StringUtils.equalsIgnoreCase(sortOrder, "asc"))
						criteria.addOrder(Order.asc("createdDate"));
					else
						criteria.addOrder(Order.desc("createdDate"));
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
						
			// Add user specific search criteria
			addSearchCriteria(criteria, searchVO);

			List<EmPortfolioI2eVw> portfolioList = null;
		
			if (all) {
				portfolioList = criteria.list();
				paginatedList.setTotal(portfolioList.size());
			} else {
				portfolioList = criteria.setFirstResult(firstResult).setMaxResults(objectsPerPage).list();
			}
			paginatedList.setList(portfolioList);


			if (!all && paginatedList.getFullListSize() == 0) {
				paginatedList.setTotal(getTotalResultCount(criteria));
			}

			return paginatedList;

		} catch (Throwable e) {
			log.error("Error while searching for IMPAC II accounts in portfolio view", e);
			throw e;
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
	public DBResult saveNotes(Long id, String text, Date date) {
		log.debug("saveNotes: saving notes for user," + id);
		DBResult result = new DBResult();
		try {
			EmPortfolioNotesT notes = findNotesById(id);
			if(notes == null) {
				notes = new EmPortfolioNotesT();
				notes.setNpnId(id);
				notes.setCreateUserId(nciUser.getUserId().toUpperCase());
				notes.setCreateDate(date);
			}
			// save notes
			notes.setLastChangeUserId(nciUser.getUserId().toUpperCase());
			notes.setLastChangeDate(date);
			notes.setNotes(text);
			saveOrUpdateNotes(notes);
		} catch (Throwable e) {
			log.error("Save notes failed for id=" + id + " text=" + text + e.getMessage(), e);
			throw e;
		}
		result.setStatus(DBResult.SUCCESS);
		return result;
	}
	
	/**
	 * get Audit Note by id
	 */
	public String getPortfolioNoteById(Long id){
		String note = "";
		Criteria crit;
		try {
			crit = sessionFactory.getCurrentSession().createCriteria(EmPortfolioI2eVw.class);
			note = "notes";
			crit.setProjection(Projections.property(note));
			crit.add(Restrictions.eq("npnId", id));
			return (String)crit.uniqueResult();
		} catch (HibernateException e) {
			log.error("getPortfolioNoteById failed for id=" + id + e.getMessage(), e);
			throw e;
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
			criteria.add(Restrictions.ilike("firstName", searchVO.getUserFirstname().trim(), MatchMode.START));
		}
		// lastName partial search
		if (!StringUtils.isBlank(searchVO.getUserLastname())) {
			criteria.add(Restrictions.ilike("lastName", searchVO.getUserLastname().trim(), MatchMode.START));
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

		if (searchVO.getCategory().longValue() == ApplicationConstants.I2E_PORTFOLIO_CATEGORY_DISCREPANCY) {
			addDiscrepancyCriteria(criteria);
		}
		
		return criteria;
	}
	
	/**
	 * Retrieve notes using id
	 * @param id
	 * @return
	 */
	private EmPortfolioNotesT findNotesById(Long id) {
		log.debug("getting EmPortfolioNotesT instance with id: " + id);
		try {
			final Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmPortfolioNotesT.class);
			crit.add(Restrictions.eq("npnId", id));
			EmPortfolioNotesT instance = (EmPortfolioNotesT) crit.uniqueResult();
			if (instance == null) {
				log.debug("get EmPortfolioNotesT successful, no instance found");
			} else {
				log.debug("get EmPortfolioNotesT successful, instance found");
			}
			return instance;
		} catch (Throwable e) {
			log.error("findNotesById failed for id=" + id, e);
			throw e;
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
			log.debug("saveOrUpdateNotes successful");
		} catch (Throwable e) {
			log.error("saveOrUpdateNotes failed", e);
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
	 * Add criteria specific to accounts with discrepancy
	 * 
	 * @param criteria
	 * @param searchVO
	 * @return
	 */
	private Criteria addDiscrepancyCriteria(final Criteria criteria) {
		Disjunction disc = Restrictions.disjunction();
        disc.add(Restrictions.eq("sodFlag", true));
        disc.add(Restrictions.eq("nedInactiveFlag", true));
        disc.add(Restrictions.eq("noActiveRoleFlag", true));
        disc.add(Restrictions.eq("i2eOnlyFlag", true));
        disc.add(Restrictions.eq("activeRoleRemainderFlag", true));
        criteria.add(disc);
		return criteria;
	}
	
}
