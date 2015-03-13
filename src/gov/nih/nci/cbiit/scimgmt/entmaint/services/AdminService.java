/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.services;

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
	 * @return EmAuditsVO
	 */
	public EmAuditsVO setupNewAudit(EmAuditsVO emAuditsVO);
	
	
	/**
	 * Close the current Audit
	 * 
	 * @param id
	 */
	public void closeCurrentAudit(Long id);
	
	
	/**
	 * Update the state of the current Audit
	 * 
	 * @param actionCode
	 * @param comments
	 */
	public EmAuditsVO updateCurrentAudit(String actionCode, String comments);
	
	
	
	/**
	 * Retrieves the current audit info from EM_AUDITS_VW
	 * 
	 * @return EmAuditsVO current Audit
	 */
	public EmAuditsVO retrieveCurrentAudit();
	
}
