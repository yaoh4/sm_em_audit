package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

public class DiscrepancyEmailAccountVO {

	private String discrepancyText;
	private String fullName;
	private String impaciiUserIdNetworkId;
	private String nedOrgPath;
	private String secondaryOrgText;
	private String createdDate;
	private String createdByFullName;

	public DiscrepancyEmailAccountVO() {
		
	}
	
	/**
	 * @return the discrepancyText
	 */
	public String getDiscrepancyText() {
		return discrepancyText;
	}

	/**
	 * @param discrepancyText
	 *            the discrepancyText to set
	 */
	public void setDiscrepancyText(String discrepancyText) {
		this.discrepancyText = discrepancyText;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the impaciiUserIdNetworkId
	 */
	public String getImpaciiUserIdNetworkId() {
		return impaciiUserIdNetworkId;
	}

	/**
	 * @param impaciiUserIdNetworkId
	 *            the impaciiUserIdNetworkId to set
	 */
	public void setImpaciiUserIdNetworkId(String impaciiUserIdNetworkId) {
		this.impaciiUserIdNetworkId = impaciiUserIdNetworkId;
	}

	/**
	 * @return the nedOrgPath
	 */
	public String getNedOrgPath() {
		return nedOrgPath;
	}

	/**
	 * @param nedOrgPath
	 *            the nedOrgPath to set
	 */
	public void setNedOrgPath(String nedOrgPath) {
		this.nedOrgPath = nedOrgPath;
	}

	/**
	 * @return the secondaryOrgText
	 */
	public String getSecondaryOrgText() {
		return secondaryOrgText;
	}

	/**
	 * @param secondaryOrgText
	 *            the secondaryOrgText to set
	 */
	public void setSecondaryOrgText(String secondaryOrgText) {
		this.secondaryOrgText = secondaryOrgText;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdByFullName
	 */
	public String getCreatedByFullName() {
		return createdByFullName;
	}

	/**
	 * @param createdByFullName
	 *            the createdByFullName to set
	 */
	public void setCreatedByFullName(String createdByFullName) {
		this.createdByFullName = createdByFullName;
	}
	
}
