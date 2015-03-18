/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;


import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;

/**
 * @author menons2
 *
 */
public class AuditSearchResultExportDecorator extends AuditSearchResultDecorator {

	/**
	 * Default Constructor
	 */
	public AuditSearchResultExportDecorator() {
		
	}
	
	/**
	 * Checks if SOD discrepancy exists
	 * 
	 * @return true is discrepancy exists, else false.
	 */
	public String getDiscrepancySod() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_SOD);
	}
	
	
	/**
	 * Checks if there NED IC discrepancy exists
	 * 
	 * @return true if discrepancy exists, else false
	 */
	public String getDiscrepancyIc() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_IC);
	}
	
	
	/**
	 * Checks if NED Inactive discrepancy exists
	 * 
	 * @return true if discrepancy exists, else false
	 */
	public String getDiscrepancyNedInactive() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_NED_INACTIVE);
	}
	
	
	/**
	 * Checks if NED Last Name discrepancy exists
	 * 
	 * @return true if discrepancy exists, else false.
	 */
	public String getDiscrepancyLastName() {
		AuditAccountVO accountVO = (AuditAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_LAST_NAME);
	}
	
	
	private String isDiscrepancy(AuditAccountVO accountVO, String type) {
		String isDiscrepancy = "N";
		
		List<String> discrepancies = accountVO.getAccountDiscrepancies();
		if(discrepancies != null && !discrepancies.isEmpty()) {
			for(String discrepancy: discrepancies) {
				if(discrepancy.equals(type)) {
					return "Y";
				}
			}
		}
		return isDiscrepancy;
	}

}
