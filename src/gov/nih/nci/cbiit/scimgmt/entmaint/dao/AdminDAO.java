package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.ParameterMode;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditsT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditHistoryT;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DAO for retrieving and saving Admin information.
 * 
 * @author menons2
 *
 */
@Component
public class AdminDAO  {

	static Logger logger = Logger.getLogger(AdminDAO.class);
	
	@Autowired
	private NciUser nciUser;	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	 * Sets up a new Audit by inserting a record in EM_AUDITS_T
	 * 
	 * @param impaciiFromDate
	 * @param impaciiToDate
	 * @param comments
	 * 
	 * @return Long audit id of the new Audit record
	 */
	public Long setupNewAudit(EmAuditsVO emAuditsVO) {
		
		Session session = sessionFactory.getCurrentSession();
		Long auditId = null;
		Date impaciiFromDate = emAuditsVO.getImpaciiFromDate();
		Date impaciiToDate = emAuditsVO.getImpaciiToDate();
		try {
			
			//Setup audit control
			EmAuditsT emAuditsT = setupAudit(emAuditsVO);
			auditId = (Long)session.save(emAuditsT);	
			
			//The freeze_audit_records procedure needs the above record to be present
			session.flush();
			
			logger.info("Calling freezeAuditRecords, start date: " + impaciiFromDate + ", end date: " + impaciiToDate);
			//Setup audit data
			freezeAuditRecords(impaciiFromDate, impaciiToDate, session);
			logger.info("freeze audit records invovation completed with start date: " + impaciiFromDate + ", end date: " + impaciiToDate);
		
			//Setup audit status
			EmAuditHistoryT history = setupHistory(auditId, ApplicationConstants.AUDIT_STATE_CODE_ENABLED, emAuditsVO.getComments());   
			session.save(history);
        				
		} catch (Throwable e) {		
			logger.error("Error setting up audit data, new auditId: " + auditId, e);
			EmAppUtil.logUserID(nciUser, logger);
			logger.error("Parameters: impaciiFromDate - " + impaciiFromDate + ", " + "impaciiToDate - " + impaciiToDate + ", " + 
			              "comments - " + emAuditsVO.getComments() + ", " + "i2eAuditFlag - " + emAuditsVO.getI2eAuditFlag());
			throw e;		
		}
		return auditId;
	}
	
	/**
	 * Closes the current Audit by end dating the current audit record in
	 * EM_AUDITS_T
	 * 
	 * @param id the audit id of the audit to close.
	 */
	public void closeAudit(Long auditId, String comments) {
		
		Session session = sessionFactory.getCurrentSession();
		
		try {
		
			//Set the Audit end date for the current audit in the EM_AUDIT_T table
		
			EmAuditsT emAuditsT = (EmAuditsT)session.load(EmAuditsT.class, new Long(auditId));
			emAuditsT.setEndDate(new Date());
			session.update(emAuditsT);
			
			//Update the Audit History for the Audit
			updateAuditHistory(auditId, ApplicationConstants.AUDIT_STATE_CODE_RESET, comments);
			
		} catch (Throwable e) {		
			logger.error("Error while updating data in EM_AUDITS_T for auditId " + auditId, e);		
			EmAppUtil.logUserID(nciUser, logger);
			logger.error("Parameters: " + "auditId - " + auditId +", " + "comments - " + comments);
			throw e;			 
		}
		
	}
	
	
	
	/**
	 * Updates the audit state by inserting a new record in EM_AUDIT_HISTORY_T
	 * 
	 * @param auditId
	 * @param actionCode
	 * @param comments
	 * 
	 * @return Long audit id of the updated audit
	 */
	public Long updateAuditHistory(Long auditId, String actionCode, String comments) {
				
		Session session = sessionFactory.getCurrentSession();
		Long id = null;
		
		try {
			//Insert a new row into the EM_AUDIT_HISTORY_T table for the current state
			EmAuditHistoryT history = setupHistory(auditId, actionCode, comments);     
			id = (Long) session.save(history);
			logger.info("Inserted Audit History with id " + id + " for audit " + auditId);
        
		} catch (Throwable e) {		
			logger.error("Error while updating data in EM_AUDIT_HISTORY_T for auditId " + auditId, e);		
			EmAppUtil.logUserID(nciUser, logger);
			logger.error("Parameters: auditID - " + auditId +", " + "actionCode - " + actionCode +", comments - " + comments);
			throw e;			
		}
		
		return id;
		
	}
	
	
	/**
	 * Sets up data for new audit.
	 * 
	 * @param impaciiFromDate
	 * @param impaciiToDate
	 * @param session
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void freezeAuditRecords(Date impaciiFromDate, Date impaciiToDate, Session session) {
					
		ProcedureCall call = session.createStoredProcedureCall("EM_AUDIT_PKG.FREEZE_AUDIT_RECORDS");
		call.registerParameter("p_start_date", Date.class, ParameterMode.IN).bindValue(impaciiToDate);
		call.registerParameter("p_end_date", Date.class, ParameterMode.IN).bindValue(impaciiFromDate);
		call.getOutputs();	
		
	}
	
	
	private EmAuditsT setupAudit(EmAuditsVO emAuditsVO) {
		//Insert a row into the EM_AUDIT_T table
		EmAuditsT emAuditsT = new EmAuditsT();
		emAuditsT.setCreateDate(new Date());
		emAuditsT.setStartDate(new Date());
		emAuditsT.setImpaciiFromDate(emAuditsVO.getImpaciiFromDate());
		emAuditsT.setImpaciiToDate(emAuditsVO.getImpaciiToDate());
		emAuditsT.setActiveCategoryEnabledFlag(emAuditsVO.getActiveCategoryEnabledFlag());
		emAuditsT.setDeletedCategoryEnabledFlag(emAuditsVO.getDeletedCategoryEnabledFlag());
		emAuditsT.setNewCategoryEnabledFlag(emAuditsVO.getNewCategoryEnabledFlag());
		emAuditsT.setInactiveCategoryEnabledFlag(emAuditsVO.getInactiveCategoryEnabledFlag());
		if(StringUtils.equalsIgnoreCase(emAuditsVO.getI2eAuditFlag(), ApplicationConstants.TRUE)) {
			emAuditsT.setI2eFromDate(emAuditsVO.getImpaciiFromDate());
			emAuditsT.setI2eToDate(emAuditsVO.getImpaciiToDate());
		}
		
		emAuditsT.setCreateUserId(nciUser.getOracleId());
		
		return emAuditsT;
	}
	
	
	
	private EmAuditHistoryT setupHistory(Long auditId, String actionCode, String comments) {
		
		EmAuditHistoryT history = new EmAuditHistoryT();
        history.setEauId(auditId);
        history.setCreateUserId(nciUser.getOracleId());
        history.setActionCode(actionCode);
        history.setComments(comments);
        
        return history;
	}
	
	
	/**
	 * Retrieves the current audit record from the EM_AUDITs_VW
	 * 
	 * @return EmAuditsVw the data associated with the current audit
	 */
	public EmAuditsVw retrieveCurrentAudit() {
		
		Session session = sessionFactory.getCurrentSession();
		EmAuditsVw emAuditsVw = null;
		
		try {
			Criteria criteria = session.createCriteria(EmAuditsVw.class);
			criteria.add(Restrictions.isNull("endDate"));			
			Object result =  criteria.uniqueResult();
			if (result != null) {
				emAuditsVw = (EmAuditsVw)result;
				session.evict(emAuditsVw);
			}
		} catch (Throwable e) {		
			logger.error("Error retrieving data from EM_AUDITS_VW ", e);		
			EmAppUtil.logUserID(nciUser, logger);
			logger.error("Pass-in Parameters: None");
			logger.error("outgoing parameters: EmAuditsVw - " + (emAuditsVw == null ? "NULL" : "Object ID=" + emAuditsVw.getId() ));
			throw e;	
		}
		
		return emAuditsVw;
	}
	
	public EmAuditsVw retrieveSelectedAudit(Long auditId){
		Session session = sessionFactory.getCurrentSession();
		EmAuditsVw emAuditsVw = null;
		
		try {
			Criteria criteria = session.createCriteria(EmAuditsVw.class);
			criteria.add(Restrictions.eq("id", auditId));			
			Object result =  criteria.uniqueResult();
			if (result != null) {
				emAuditsVw = (EmAuditsVw)result;
				session.evict(emAuditsVw);
			}
		} catch (Throwable e) {		
			logger.error("Error retrieving data from EM_AUDITS_VW ", e);	
			EmAppUtil.logUserID(nciUser, logger);
			logger.error("Pass-in Parameters: " + " auditId - " + auditId);
			logger.error("Outgoing paramters: EmAuditsVw = " + (emAuditsVw == null ? "NULL" : "Object ID=" + emAuditsVw.getId()));
			throw e;	
		}
		
		return emAuditsVw;
	}
	
	/**
	 * Retrieves the audit record associated with the given id from the EM_AUDITs_VW
	 * 
	 * @return EmAuditsVw the audit info. associated with the given audit id
	 */
	public EmAuditsVw retrieveAudit(Long auditId) {
		
		Session session = sessionFactory.getCurrentSession();
		EmAuditsVw emAuditsVw = null;
		
		try {
			emAuditsVw = (EmAuditsVw)session.get(EmAuditsVw.class, new Long(auditId));
			session.evict(emAuditsVw);
		} catch (Throwable e) {		
			logger.error("Error retrieving data from EM_AUDITS_VW for auditId " + auditId, e);			
			EmAppUtil.logUserID(nciUser, logger);
			logger.error("Pass-in Parameters: auditID - " + auditId);
			logger.error("Outgoing parameters: EmAuditsVw - " + (emAuditsVw == null ? "NULL" : "Object ID=" + emAuditsVw.getId()));
			throw e;
		}
		
		return emAuditsVw;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<EmAuditsVw> retrieveAuditList(String category) {
		Session session = sessionFactory.getCurrentSession();
		List<EmAuditsVw> emAudits = null;
		
		try {
			Criteria criteria = session.createCriteria(EmAuditsVw.class);	
			criteria.addOrder(Order.desc("id"));
			if(category != null) {
				criteria.add(getCategoryCriterion(criteria, category));
			}
			Object result =  criteria.list();
			if (result != null) {
				emAudits = (List<EmAuditsVw>)result;
				
				if(!CollectionUtils.isEmpty(emAudits)) {
					for(EmAuditsVw audit: emAudits) {
						session.evict(audit);
					}
				}
			}
		} catch (Throwable e) {		
			logger.error("Error retrieving full set from EM_AUDITS_VW ", e);		
			EmAppUtil.logUserID(nciUser, logger);
			throw e;	
		}
		
		return emAudits;
	}
	
	
	private Criterion getCategoryCriterion(Criteria criteria, String category) {
		Criterion criterion = null;
		
		switch (category) {
		case ApplicationConstants.CATEGORY_ACTIVE:
			criterion =  Restrictions.ilike("activeCategoryEnabledFlag",  'Y');
			break;
		case ApplicationConstants.CATEGORY_NEW:
			criterion = Restrictions.ilike("newCategoryEnabledFlag",  'Y');
			break;
		case ApplicationConstants.CATEGORY_DELETED:
			criterion = Restrictions.ilike("deletedCategoryEnabledFlag",  'Y');
			break;
		case ApplicationConstants.CATEGORY_INACTIVE:
			criterion = Restrictions.ilike("inactiveCategoryEnabledFlag",  'Y');
			break;	
		}
		return criterion;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<EmAuditsVw> retrieveI2eAuditList() {
		Session session = sessionFactory.getCurrentSession();
		List<EmAuditsVw> emAudits = null;
		
		try {
			Criteria criteria = session.createCriteria(EmAuditsVw.class);
			criteria.addOrder(Order.desc("id"));
			criteria.add(Restrictions.isNotNull("i2eFromDate"));
			Object result =  criteria.list();
			if (result != null) {
				emAudits = (List<EmAuditsVw>)result;
				
				if(!CollectionUtils.isEmpty(emAudits)) {
					for(EmAuditsVw audit: emAudits) {
						session.evict(audit);
					}
				}
			}
		} catch (Throwable e) {		
			logger.error("Error retrieving full set from EM_AUDITS_VW ", e);	
			EmAppUtil.logUserID(nciUser, logger);
			logger.error("Outgoing Parameters: List<EmAuditsVw - " + (emAudits == null ? "NULL" : "Size of List=" + emAudits.size()));
			throw e;	
		}
		
		return emAudits;
	}
	

}
