package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

/**
 * VO for Transferred accounts admin reports.
 * @author tembharend
 */

@SuppressWarnings("serial")
public class TransferredAuditAccountsVO implements java.io.Serializable {
	
	private String lastName;
	private String firstName;
	private String nihNetworkId;
	private String nedOrgPath;
	private Boolean impaciiActiveStatusFlag;
	private Boolean i2eActiveStatusFlag;
	private String transferToNedOrgPath;
	private String transferFromNedOrgPath;
	private String deletedTransferToNedOrgPath;
	private String deletedTransferFromNedOrgPath;
	private Long statusCode; 
	
	public TransferredAuditAccountsVO() {
		super();
	}
	public TransferredAuditAccountsVO(String lastName, String firstName, String nihNetworkId) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.nihNetworkId = nihNetworkId;
	}
	public TransferredAuditAccountsVO(String lastName, String firstName, String nihNetworkId, String nedOrgPath,
			Boolean impaciiActiveStatusFlag, Boolean i2eActiveStatusFlag, String transferToNedOrgPath,
			String transferFromNedOrgPath, String deletedTransferToNedOrgPath, String deletedTransferFromNedOrgPath) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.nihNetworkId = nihNetworkId;
		this.nedOrgPath = nedOrgPath;
		this.impaciiActiveStatusFlag = impaciiActiveStatusFlag;
		this.i2eActiveStatusFlag = i2eActiveStatusFlag;
		this.transferToNedOrgPath = transferToNedOrgPath;
		this.transferFromNedOrgPath = transferFromNedOrgPath;
		this.deletedTransferToNedOrgPath=deletedTransferToNedOrgPath;
		this.deletedTransferFromNedOrgPath= deletedTransferFromNedOrgPath;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getNihNetworkId() {
		return nihNetworkId;
	}
	public void setNihNetworkId(String nihNetworkId) {
		this.nihNetworkId = nihNetworkId;
	}
	public String getNedOrgPath() {
		return nedOrgPath;
	}
	public void setNedOrgPath(String nedOrgPath) {
		this.nedOrgPath = nedOrgPath;
	}
	public Boolean getImpaciiActiveStatusFlag() {
		return impaciiActiveStatusFlag;
	}
	public void setImpaciiActiveStatusFlag(Boolean impaciiActiveStatusFlag) {
		this.impaciiActiveStatusFlag = impaciiActiveStatusFlag;
	}
	public Boolean getI2eActiveStatusFlag() {
		return i2eActiveStatusFlag;
	}
	public void setI2eActiveStatusFlag(Boolean i2eActiveStatusFlag) {
		this.i2eActiveStatusFlag = i2eActiveStatusFlag;
	}
	public String getTransferToNedOrgPath() {
		return transferToNedOrgPath;
	}
	public void setTransferToNedOrgPath(String transferToNedOrgPath) {
		this.transferToNedOrgPath = transferToNedOrgPath;
	}
	public String getTransferFromNedOrgPath() {
		return transferFromNedOrgPath;
	}
	public void setTransferFromNedOrgPath(String transferFromNedOrgPath) {
		this.transferFromNedOrgPath = transferFromNedOrgPath;
	}
	public String getDeletedTransferToNedOrgPath() {
		return deletedTransferToNedOrgPath;
	}
	public void setDeletedTransferToNedOrgPath(String deletedTransferToNedOrgPath) {
		this.deletedTransferToNedOrgPath = deletedTransferToNedOrgPath;
	}
	public String getDeletedTransferFromNedOrgPath() {
		return deletedTransferFromNedOrgPath;
	}
	public void setDeletedTransferFromNedOrgPath(String deletedTransferFromNedOrgPath) {
		this.deletedTransferFromNedOrgPath = deletedTransferFromNedOrgPath;
	}
	public Long getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}		
	
}
