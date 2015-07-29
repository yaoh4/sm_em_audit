package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;

import org.apache.commons.collections.CollectionUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * This class is responsible for decorating the rows of I2E admin reports search results table.
 * @author 
 *
 */
@Configurable
public class AdminReportI2eSearchResultExportDecorator extends TableDecorator{
	
	/**
	 * Get the full name.
	 * @return fullName
	 */
	public String getFullName() {
		AuditI2eAccountVO auditVO = (AuditI2eAccountVO)getCurrentRowObject();
		return auditVO.getFullName();
	}
	
	/**
	 * get created date	
	 * @return created date in string
	 */
	public String getI2eCreatedDate(){
		AuditI2eAccountVO auditVO = (AuditI2eAccountVO)getCurrentRowObject();
		Date createDate = auditVO.getCreatedDate();
		if(createDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(createDate);
	}
	
	/**
	 * Checks if SOD discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancySod() {
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_I2E_SOD);
	}
	
	/**
	 * Checks if NED Inactive discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyNedInactive() {
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_I2E_NED_INACTIVE);
	}
	
	/**
	 * Checks if Inactive I2E Role(s) discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyInactiveI2eRole() {
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_I2E_NO_ACTIVE_ROLE);
	}
	
	/**
	 * Checks if Inactive IMPAC II discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyI2eOnly() {
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_I2E_ONLY);
	}
	
	/**
	 * Checks if I2e Inactive discrepancy exists
	 * 
	 * @return String 'Y' if discrepancy exists, else empty string.
	 */
	public String getDiscrepancyI2eInactive() {
		AuditI2eAccountVO accountVO = (AuditI2eAccountVO)getCurrentRowObject();
		return isDiscrepancy(accountVO, ApplicationConstants.DISCREPANCY_CODE_I2E_ACTIVE_ROLE_REMAINDER);
	}
	
	/**
	 * Checks if a discrepancy of the given type exists in the given account.
	 * Helper method used by the other discrepancy specific methods.
	 * 
	 * @param accountVO the account in which to look for discrepancy
	 * @param type the discrepancy type to check for.
	 * 
	 * @return String 'Y' if discrepancy exists, else return empty String.
	 */
	private String isDiscrepancy(AuditI2eAccountVO accountVO, String type) {
		String isDiscrepancy = "";
		
		List<String> discrepancies = accountVO.getAccountDiscrepancies();
		if(!CollectionUtils.isEmpty(discrepancies)) {
			for(String discrepancy: discrepancies) {
				if(discrepancy.equals(type)) {
					return "Y";
				}
			}
		}
		return isDiscrepancy;
	}
}
