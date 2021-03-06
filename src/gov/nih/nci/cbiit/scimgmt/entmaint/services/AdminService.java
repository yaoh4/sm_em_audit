package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DashboardData;
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
	public Long closeCurrentAudit(String comments);
	
	
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
	 * Retrieve the attributes of the newest audit
	 * @return
	 */
	public EmAuditsVO retrieveCurrentOrLastAuditVO();
	
	
	/**
	 * Retrieves the audit info for the current Audit 
	 * 
	 * @return EmAuditsVO 
	 */
	public EmAuditsVO retrieveCurrentAuditVO();
		
	
	/**
	 * Retrieves the audit info for the Audit with the given auditId 
	 * 
	 * @return EmAuditsVO t
	 */
	public EmAuditsVO retrieveAuditVO(Long auditId);
	
	
	/**
	 * Retrieves the audit info for all Audits 
	 * 
	 * @return List<EmAuditsVO> t
	 */
	public List<EmAuditsVO> retrieveAuditVOList();
	
	
	/**
	 * Retrieves the audit info for all Audits for the given category
	 * @param category 
	 * 
	 * @return List<EmAuditsVO> t
	 */
	public List<EmAuditsVO> retrieveAuditVOList(String category);
	
	/**
	 * Retrieves the audit info for all Audits for reports
	 * 
	 * @return List<EmAuditsVO> t
	 */
	public List<EmAuditsVO> retrieveReportAuditVOList();

	/**
	 * Retrieves the i2e audit info for all Audits 
	 * 
	 * @return List<EmAuditsVO> t
	 */
	public List<EmAuditsVO> retrieveI2eAuditVOList();
	
	/**
	 * Checks if there is at least one audit present in the system.
	 * 
	 * @return true if an audit is present, false otherwise.
	 */
	public boolean isAuditPresent();
	/**
	 * Checks if there is at least one I2E audit present in the system.
	 * 
	 * @return true if an I2E audit is present, false otherwise.
	 */
	public boolean isI2eAuditPresent();
	
	/**
	 * Retrieves the ic dashboard data based on org
	 * 
	 * @return DashboardData
	 */
	public DashboardData retrieveIcDashboardData(Long auditId, String orgPath);
}
