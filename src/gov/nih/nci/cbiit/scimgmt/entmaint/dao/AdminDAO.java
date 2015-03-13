/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.ParameterMode;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditHistoryVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditsT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditHistoryT;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
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
	 * 
	 */
	public AdminDAO() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Sets up a new Audit by inserting a record in EM_AUDITS_T
	 * 
	 * @param impaciiFromDate
	 * @param impaciiToDate
	 * @param comments
	 * @return
	 */
	public Long setupNewAudit(Date impaciiFromDate, Date impaciiToDate, String comments) {
		
		Session session = sessionFactory.getCurrentSession();
		Long id = null;		
		try {
			
			//Setup audit data
			//freezeAuditRecords(impaciiFromDate, impaciiToDate, session);
		
			//Setup audit control
			EmAuditsT emAuditsT = setupAudit(impaciiFromDate, impaciiToDate);
			id = (Long)session.save(emAuditsT);						
		
			//Setup audit status
			EmAuditHistoryT history = setupHistory(id, ApplicationConstants.AUDIT_STATE_CODE_ENABLED, comments);   
			session.save(history);
        				
		} catch (Throwable e) {		
			logger.error("Error setting up audit data, new auditId: " + id, e);
			try {
				session.close();
				//TBD - Setup error handling at the application level
				//and remove this code
				throw e;
			} catch(Throwable b) {
				logger.error("Error while closing session ", b);
				throw b;
			}
		}
		return id;
	}
	
	/**
	 * Closes the current Audit by end dating the current audit record in
	 * EM_AUDITS_T
	 * 
	 * @param id
	 */
	public void closeCurrentAudit(Long id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		try {
		
			//Set the Audit end date for the current audit in the EM_AUDIT_T table
		
			EmAuditsT emAuditsT = (EmAuditsT)session.load(EmAuditsT.class, new Long(id));
			emAuditsT.setEndDate(new Date());
			session.update(emAuditsT);
			
		} catch (Throwable e) {		
			logger.error("Error while updating data in EM_AUDITS_T for auditId " + id, e);
			try {
				session.close();
				//TBD - Setup error handling at the application level
				//and remove this code
				throw e;
			} catch(Throwable b) {
				logger.error("Error while closing session ", b);
				throw b;
			}
		}
	}
	
	
	/**
	 * Updates the audit state by inserting a new record in EM_AUDIT_HISTORY_T
	 * @param auditId
	 * @param actionCode
	 * @param comments
	 */
	public EmAuditsVw updateAudit(Long auditId, String actionCode, String comments) {
		
		Session session = sessionFactory.getCurrentSession();
		EmAuditsVw emAuditsVw = null;
		
		try {
        
			//Insert a new row into the EM_AUDIT_HISTORY_T table for the current state
			EmAuditHistoryT history = setupHistory(auditId, actionCode, comments);     
			session.save(history);
			
			emAuditsVw = retrieveAudit(auditId);
			
			
        
		} catch (Throwable e) {		
			logger.error("Error while updating data in EM_AUDIT_HISTORY_T for auditId " + auditId, e);
			try {
				session.close();
				//TBD - Setup error handling at the application level
				//and remove this code
				throw e;
			} catch(Throwable b) {
				logger.error("Error while closing session ", b);
				throw b;
			}
		}
		
		return emAuditsVw;
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
				
		try {
		ProcedureCall call = session.createStoredProcedureCall("freeze_audit_records");
		call.registerParameter("p_start_date", Date.class, ParameterMode.IN);
		call.registerParameter("p_end_date", Date.class, ParameterMode.IN);
		call.getRegisteredParameters().get(0).bindValue(impaciiFromDate);
		call.getRegisteredParameters().get(1).bindValue(impaciiToDate);
		call.getOutputs();	
		} catch (Throwable e) {		
			logger.error("Error while calling stored procedure freeze_audit_records ", e);
			throw e;	
		}
	}
	
	
	private EmAuditsT setupAudit(Date impaciiFromDate, Date impaciiToDate) {
		//Insert a row into the EM_AUDIT_T table
		EmAuditsT emAuditsT = new EmAuditsT();
		emAuditsT.setCreateDate(new Date());
		emAuditsT.setStartDate(new Date());
		emAuditsT.setImpaciiFromDate(impaciiFromDate);
		emAuditsT.setImpaciiToDate(impaciiToDate);
		emAuditsT.setI2eFromDate(impaciiFromDate);
		emAuditsT.setI2eToDate(impaciiToDate);
		emAuditsT.setCreateUserId(getNciUser().getOracleId());
		
		return emAuditsT;
	}
	
	
	
	private EmAuditHistoryT setupHistory(Long auditId, String actionCode, String comments) {
		
		EmAuditHistoryT history = new EmAuditHistoryT();
        history.setEauId(auditId);
        history.setCreateUserId(getNciUser().getOracleId());
        history.setActionCode(actionCode);
        history.setComments(comments);
        
        return history;
	}
	
	
	/**
	 * Retrieves the current audit record from the EM_AUDITs_VW
	 * @return
	 */
	public EmAuditsVw retrieveCurrentAudit() {
		
		Session session = sessionFactory.getCurrentSession();
		
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditsVw.class);
			criteria.add(Restrictions.isNull("endDate"));
			criteria.add(Restrictions.ne("id", new Long(1)));
			Object result =  criteria.uniqueResult();
			if (result != null) {
				return (EmAuditsVw)result;
			
			} else {
				logger.info("No audit info retrieved from EM_AUDITS_VW");
				return null;
			} 
		} catch (Throwable e) {		
			logger.error("Error retrieving data from EM_AUDITS_VW ", e);
			try {
				session.close();
				//TBD - Setup error handling at the application level
				//and remove this code
				throw e;
			} catch(Throwable b) {
				logger.error("Error while closing session ", b);
				throw b;
			}
		}
	}

	
	/**
	 * Retrieves the current audit record from the EM_AUDITs_VW
	 * @return
	 */
	public EmAuditsVw retrieveAudit(Long auditId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditsVw.class);
			criteria.add(Restrictions.eq("id", auditId));
			Object result =  criteria.uniqueResult();
			if (result != null) {
				return (EmAuditsVw)result;
			
			} else {
				logger.info("No audit info retrieved from EM_AUDITS_VW for audit " + auditId);
				return null;
			} 
		} catch (Throwable e) {		
			logger.error("Error retrieving data from EM_AUDITS_VW for auditId " + auditId, e);
			try {
				session.close();
				//TBD - Setup error handling at the application level
				//and remove this code
				throw e;
			} catch(Throwable b) {
				logger.error("Error while closing session ", b);
				throw b;
			}
		}
	}
	
	
	public List<EmAuditHistoryVw> retrieveStatusHistories(Long auditId) {
		List<EmAuditHistoryVw> result = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(EmAuditHistoryVw.class);
			criteria.add(Restrictions.eq("auditId", auditId));
			result = criteria.list();
			
		} catch (Throwable e) {		
			logger.error("Error retrieving data from EM_AUDIT_HISTORY_VW for auditId " + auditId, e);
			try {
				session.close();
				//TBD - Setup error handling at the application level
				//and remove this code
				throw e;
			} catch(Throwable b) {
				logger.error("Error while closing session ", b);
				throw b;
			}
		}
		return result;
	}

	
	/**
	 * @return the nciUser
	 */
	public NciUser getNciUser() {
		return nciUser;
	}


	/**
	 * @param nciUser the nciUser to set
	 */
	public void setNciUser(NciUser nciUser) {
		this.nciUser = nciUser;
	}

}
