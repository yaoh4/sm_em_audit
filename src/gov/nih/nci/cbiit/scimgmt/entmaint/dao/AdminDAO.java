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
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;

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
		Long auditId = null;		
		try {
			
			//Setup audit control
			EmAuditsT emAuditsT = setupAudit(impaciiFromDate, impaciiToDate);
			auditId = (Long)session.save(emAuditsT);	
			
			//The freeze_audit_records procedure needs the above record to be present
			session.flush();
			
			//Setup audit data
			freezeAuditRecords(impaciiFromDate, impaciiToDate, session);
		
			//Setup audit status
			EmAuditHistoryT history = setupHistory(auditId, ApplicationConstants.AUDIT_STATE_CODE_ENABLED, comments);   
			session.save(history);
        				
		} catch (Throwable e) {		
			logger.error("Error setting up audit data, new auditId: " + auditId, e);
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
		return auditId;
	}
	
	/**
	 * Closes the current Audit by end dating the current audit record in
	 * EM_AUDITS_T
	 * 
	 * @param id
	 */
	public DBResult closeAudit(Long id) {
		
		DBResult result = new DBResult();
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
		
		result.setStatus(DBResult.SUCCESS);
		return result;
	}
	
	
	
	/**
	 * Updates the audit state by inserting a new record in EM_AUDIT_HISTORY_T
	 * @param auditId
	 * @param actionCode
	 * @param comments
	 */
	public Long updateAuditHistory(Long auditId, String actionCode, String comments) {
				
		Session session = sessionFactory.getCurrentSession();
		Long id = null;
		
		try {
			//Insert a new row into the EM_AUDIT_HISTORY_T table for the current state
			EmAuditHistoryT history = setupHistory(auditId, actionCode, comments);     
			id = (Long) session.save(history);
        
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
				
		try {
		ProcedureCall call = session.createStoredProcedureCall("EM_AUDIT_PKG.FREEZE_AUDIT_RECORDS");
		call.registerParameter("p_start_date", Date.class, ParameterMode.IN).bindValue(impaciiToDate);
		call.registerParameter("p_end_date", Date.class, ParameterMode.IN).bindValue(impaciiFromDate);
		call.getOutputs();	
		} catch (Throwable e) {		
			logger.error("Error while calling stored procedure EM_AUDIT_PKG.FREEZE_AUDIT_RECORDS ", e);
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
		EmAuditsVw emAuditsVw = null;
		
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmAuditsVw.class);
			criteria.add(Restrictions.isNull("endDate"));			
			Object result =  criteria.uniqueResult();
			if (result != null) {
				emAuditsVw = (EmAuditsVw)result;
				session.evict(emAuditsVw);
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
		
		return emAuditsVw;
	}

	
	/**
	 * Retrieves the current audit record from the EM_AUDITs_VW
	 * @return
	 */
	public EmAuditsVw retrieveAudit(Long auditId) {
		
		Session session = sessionFactory.getCurrentSession();
		EmAuditsVw emAuditsVw = null;
		
		try {
			emAuditsVw = (EmAuditsVw)session.get(EmAuditsVw.class, new Long(auditId));
			session.evict(emAuditsVw);
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
		
		return emAuditsVw;
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
