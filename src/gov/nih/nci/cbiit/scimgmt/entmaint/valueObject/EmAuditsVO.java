/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditsVw;

/**
 * @author menons2
 *
 */
public class EmAuditsVO extends EmAuditsVw {
	/**
	 * Flags to indicate the type of audit data to set
	 */
	private String impac2AuditFlag = "false";
	private String i2eAuditFlag = "false";
	
	/**
	 * Audit State
	 */
	private String auditState = ApplicationConstants.AUDIT_STATE_CODE_RESET;
	
	/**
	 * Audit comments
	 */
	private String comments;


	/**
	 * @return the impac2AuditFlag
	 */
	public String getImpac2AuditFlag() {
		return impac2AuditFlag;
	}


	/**
	 * @param impac2AuditFlag the impac2AuditFlag to set
	 */
	public void setImpac2AuditFlag(String impac2AuditFlag) {
		this.impac2AuditFlag = impac2AuditFlag;
	}


	/**
	 * @return the i2eAuditFlag
	 */
	public String getI2eAuditFlag() {
		return i2eAuditFlag;
	}


	/**
	 * @param i2eAuditFlag the i2eAuditFlag to set
	 */
	public void setI2eAuditFlag(String i2eAuditFlag) {
		this.i2eAuditFlag = i2eAuditFlag;
	}


	/**
	 * @return the auditState
	 */
	public String getAuditState() {
		return auditState;
	}


	/**
	 * @param auditState the auditState to set
	 */
	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}


	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}


	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	
}
