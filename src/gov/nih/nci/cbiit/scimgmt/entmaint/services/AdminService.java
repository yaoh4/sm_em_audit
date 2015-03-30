/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import java.util.Date;

import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

/**
 * @author menons2
 *
 */
public interface AdminService {

	
	/**
	 * Setup a new Audit
	 * 
	 * @param emAuditsVO
	 * @return Long the auditId of the new Audit
	 */
	public Long setupNewAudit(EmAuditsVO emAuditsVO);
	
	
	/**
	 * Close the current Audit
	 * 
	 * @return
	 */
	public void closeCurrentAudit();
	
	
	/**
	 * Update the state of the current Audit
	 * 
	 * @param actionCode
	 * @param comments
	 * 
	 * @return Long the auditId of the updated Audit
	 */
	public Long updateCurrentAudit(String actionCode, String comments);
	
	
	
	/**
	 * Retrieves the audit info for the current Audit 
	 * 
	 * @return EmAuditsVO 
	 */
	public EmAuditsVO retrieveCurrentAuditVO();
	
	
	/**
	 * Retrieves the start date of the current Audit.
	 * 
	 * @return Date the audit start date.
	 */
	public Date retrieveAuditStartDate();
	
	
	/**
	 * Retrieves the audit info for the Audit with the given auditId 
	 * 
	 * @return EmAuditsVO t
	 */
	public EmAuditsVO retrieveAuditVO(Long auditId);
	
}
