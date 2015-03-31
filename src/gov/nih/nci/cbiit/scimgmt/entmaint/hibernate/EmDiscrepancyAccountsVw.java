package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

// Generated Mar 30, 2015 12:44:18 PM by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * EmDiscrepancyAccountsVw generated by hbm2java
 */
public class EmDiscrepancyAccountsVw implements java.io.Serializable {

	private String impaciiUserId;
	private String nihNetworkId;
	private String impaciiLastName;
	private String impaciiFirstName;
	private String nedLastName;
	private String nedFirstName;
	private String nedEmailAddress;
	private String parentNedOrgPath;
	private String nedOrgPath;
	private String nedIc;
	private String nedActiveFlag;
	private String nciDoc;
	private Date createdDate;
	private String createdByUserId;
	private String createdByFullName;
	private Date deletedDate;
	private String deletedByUserId;
	private String deletedByFullName;
	private String deactivationComments;
	private String secondaryOrgText;
	private String lastLoginDate;
	private String impaciiOnlyFlag;
	private String notes;
	private String notesSubmittedByFullName;
	private Date notesSubmittedDate;
	private Boolean sodFlag;
	private Boolean icDiffFlag;
	private Boolean nedInactiveFlag;
	private Boolean lastNameDiffFlag;
	private List<EmPortfolioRolesVw> accountRoles = new ArrayList<EmPortfolioRolesVw>(0);
	private List<String> accountDiscrepancies = new ArrayList<String>(0);
	
	public EmDiscrepancyAccountsVw() {
	}

	public EmDiscrepancyAccountsVw(String impaciiUserId, Date createdDate, String createdByUserId) {
		this.impaciiUserId = impaciiUserId;
		this.createdDate = createdDate;
		this.createdByUserId = createdByUserId;
	}

	public EmDiscrepancyAccountsVw(String impaciiUserId, String nihNetworkId, String impaciiLastName,
			String impaciiFirstName, String nedLastName, String nedFirstName, String nedEmailAddress,
			String parentNedOrgPath, String nedOrgPath, String nedIc, String nedActiveFlag, String nciDoc,
			Date createdDate, String createdByUserId, String createdByFullName, Date deletedDate,
			String deletedByUserId, String deletedByFullName, String deactivationComments, String secondaryOrgText,
			String lastLoginDate, String impaciiOnlyFlag, String notes, String notesSubmittedByFullName,
			Date notesSubmittedDate, Boolean sodFlag, Boolean icDiffFlag, Boolean nedInactiveFlag,
			Boolean lastNameDiffFlag, List accountRoles, List accountDiscrepancies) {
		this.impaciiUserId = impaciiUserId;
		this.nihNetworkId = nihNetworkId;
		this.impaciiLastName = impaciiLastName;
		this.impaciiFirstName = impaciiFirstName;
		this.nedLastName = nedLastName;
		this.nedFirstName = nedFirstName;
		this.nedEmailAddress = nedEmailAddress;
		this.parentNedOrgPath = parentNedOrgPath;
		this.nedOrgPath = nedOrgPath;
		this.nedIc = nedIc;
		this.nedActiveFlag = nedActiveFlag;
		this.nciDoc = nciDoc;
		this.createdDate = createdDate;
		this.createdByUserId = createdByUserId;
		this.createdByFullName = createdByFullName;
		this.deletedDate = deletedDate;
		this.deletedByUserId = deletedByUserId;
		this.deletedByFullName = deletedByFullName;
		this.deactivationComments = deactivationComments;
		this.secondaryOrgText = secondaryOrgText;
		this.lastLoginDate = lastLoginDate;
		this.impaciiOnlyFlag = impaciiOnlyFlag;
		this.notes = notes;
		this.notesSubmittedByFullName = notesSubmittedByFullName;
		this.notesSubmittedDate = notesSubmittedDate;
		this.sodFlag = sodFlag;
		this.icDiffFlag = icDiffFlag;
		this.nedInactiveFlag = nedInactiveFlag;
		this.lastNameDiffFlag = lastNameDiffFlag;
		this.accountRoles = accountRoles;
		this.accountDiscrepancies = accountDiscrepancies;
	}

	public String getImpaciiUserId() {
		return this.impaciiUserId;
	}

	public void setImpaciiUserId(String impaciiUserId) {
		this.impaciiUserId = impaciiUserId;
	}

	public String getNihNetworkId() {
		return this.nihNetworkId;
	}

	public void setNihNetworkId(String nihNetworkId) {
		this.nihNetworkId = nihNetworkId;
	}

	public String getImpaciiLastName() {
		return this.impaciiLastName;
	}

	public void setImpaciiLastName(String impaciiLastName) {
		this.impaciiLastName = impaciiLastName;
	}

	public String getImpaciiFirstName() {
		return this.impaciiFirstName;
	}

	public void setImpaciiFirstName(String impaciiFirstName) {
		this.impaciiFirstName = impaciiFirstName;
	}

	public String getNedLastName() {
		return this.nedLastName;
	}

	public void setNedLastName(String nedLastName) {
		this.nedLastName = nedLastName;
	}

	public String getNedFirstName() {
		return this.nedFirstName;
	}

	public void setNedFirstName(String nedFirstName) {
		this.nedFirstName = nedFirstName;
	}

	public String getNedEmailAddress() {
		return this.nedEmailAddress;
	}

	public void setNedEmailAddress(String nedEmailAddress) {
		this.nedEmailAddress = nedEmailAddress;
	}

	public String getParentNedOrgPath() {
		return this.parentNedOrgPath;
	}

	public void setParentNedOrgPath(String parentNedOrgPath) {
		this.parentNedOrgPath = parentNedOrgPath;
	}

	public String getNedOrgPath() {
		return this.nedOrgPath;
	}

	public void setNedOrgPath(String nedOrgPath) {
		this.nedOrgPath = nedOrgPath;
	}

	public String getNedIc() {
		return this.nedIc;
	}

	public void setNedIc(String nedIc) {
		this.nedIc = nedIc;
	}

	public String getNedActiveFlag() {
		return this.nedActiveFlag;
	}

	public void setNedActiveFlag(String nedActiveFlag) {
		this.nedActiveFlag = nedActiveFlag;
	}

	public String getNciDoc() {
		return this.nciDoc;
	}

	public void setNciDoc(String nciDoc) {
		this.nciDoc = nciDoc;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedByUserId() {
		return this.createdByUserId;
	}

	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public String getCreatedByFullName() {
		return this.createdByFullName;
	}

	public void setCreatedByFullName(String createdByFullName) {
		this.createdByFullName = createdByFullName;
	}

	public Date getDeletedDate() {
		return this.deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public String getDeletedByUserId() {
		return this.deletedByUserId;
	}

	public void setDeletedByUserId(String deletedByUserId) {
		this.deletedByUserId = deletedByUserId;
	}

	public String getDeletedByFullName() {
		return this.deletedByFullName;
	}

	public void setDeletedByFullName(String deletedByFullName) {
		this.deletedByFullName = deletedByFullName;
	}

	public String getDeactivationComments() {
		return this.deactivationComments;
	}

	public void setDeactivationComments(String deactivationComments) {
		this.deactivationComments = deactivationComments;
	}

	public String getSecondaryOrgText() {
		return this.secondaryOrgText;
	}

	public void setSecondaryOrgText(String secondaryOrgText) {
		this.secondaryOrgText = secondaryOrgText;
	}

	public String getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getImpaciiOnlyFlag() {
		return this.impaciiOnlyFlag;
	}

	public void setImpaciiOnlyFlag(String impaciiOnlyFlag) {
		this.impaciiOnlyFlag = impaciiOnlyFlag;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotesSubmittedByFullName() {
		return this.notesSubmittedByFullName;
	}

	public void setNotesSubmittedByFullName(String notesSubmittedByFullName) {
		this.notesSubmittedByFullName = notesSubmittedByFullName;
	}

	public Date getNotesSubmittedDate() {
		return this.notesSubmittedDate;
	}

	public void setNotesSubmittedDate(Date notesSubmittedDate) {
		this.notesSubmittedDate = notesSubmittedDate;
	}

	public Boolean getSodFlag() {
		return this.sodFlag;
	}

	public void setSodFlag(Boolean sodFlag) {
		this.sodFlag = sodFlag;
	}

	public Boolean getIcDiffFlag() {
		return this.icDiffFlag;
	}

	public void setIcDiffFlag(Boolean icDiffFlag) {
		this.icDiffFlag = icDiffFlag;
	}

	public Boolean getNedInactiveFlag() {
		return this.nedInactiveFlag;
	}

	public void setNedInactiveFlag(Boolean nedInactiveFlag) {
		this.nedInactiveFlag = nedInactiveFlag;
	}

	public Boolean getLastNameDiffFlag() {
		return this.lastNameDiffFlag;
	}

	public void setLastNameDiffFlag(Boolean lastNameDiffFlag) {
		this.lastNameDiffFlag = lastNameDiffFlag;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EmDiscrepancyAccountsVw))
			return false;
		EmDiscrepancyAccountsVw castOther = (EmDiscrepancyAccountsVw) other;

		return ((this.getImpaciiUserId() == castOther.getImpaciiUserId()) || (this.getImpaciiUserId() != null
				&& castOther.getImpaciiUserId() != null && this.getImpaciiUserId()
				.equals(castOther.getImpaciiUserId())))
				&& ((this.getNihNetworkId() == castOther.getNihNetworkId()) || (this.getNihNetworkId() != null
						&& castOther.getNihNetworkId() != null && this.getNihNetworkId().equals(
						castOther.getNihNetworkId())))
				&& ((this.getImpaciiLastName() == castOther.getImpaciiLastName()) || (this.getImpaciiLastName() != null
						&& castOther.getImpaciiLastName() != null && this.getImpaciiLastName().equals(
						castOther.getImpaciiLastName())))
				&& ((this.getImpaciiFirstName() == castOther.getImpaciiFirstName()) || (this.getImpaciiFirstName() != null
						&& castOther.getImpaciiFirstName() != null && this.getImpaciiFirstName().equals(
						castOther.getImpaciiFirstName())))
				&& ((this.getNedLastName() == castOther.getNedLastName()) || (this.getNedLastName() != null
						&& castOther.getNedLastName() != null && this.getNedLastName().equals(
						castOther.getNedLastName())))
				&& ((this.getNedFirstName() == castOther.getNedFirstName()) || (this.getNedFirstName() != null
						&& castOther.getNedFirstName() != null && this.getNedFirstName().equals(
						castOther.getNedFirstName())))
				&& ((this.getNedEmailAddress() == castOther.getNedEmailAddress()) || (this.getNedEmailAddress() != null
						&& castOther.getNedEmailAddress() != null && this.getNedEmailAddress().equals(
						castOther.getNedEmailAddress())))
				&& ((this.getParentNedOrgPath() == castOther.getParentNedOrgPath()) || (this.getParentNedOrgPath() != null
						&& castOther.getParentNedOrgPath() != null && this.getParentNedOrgPath().equals(
						castOther.getParentNedOrgPath())))
				&& ((this.getNedOrgPath() == castOther.getNedOrgPath()) || (this.getNedOrgPath() != null
						&& castOther.getNedOrgPath() != null && this.getNedOrgPath().equals(castOther.getNedOrgPath())))
				&& ((this.getNedIc() == castOther.getNedIc()) || (this.getNedIc() != null
						&& castOther.getNedIc() != null && this.getNedIc().equals(castOther.getNedIc())))
				&& ((this.getNedActiveFlag() == castOther.getNedActiveFlag()) || (this.getNedActiveFlag() != null
						&& castOther.getNedActiveFlag() != null && this.getNedActiveFlag().equals(
						castOther.getNedActiveFlag())))
				&& ((this.getNciDoc() == castOther.getNciDoc()) || (this.getNciDoc() != null
						&& castOther.getNciDoc() != null && this.getNciDoc().equals(castOther.getNciDoc())))
				&& ((this.getCreatedDate() == castOther.getCreatedDate()) || (this.getCreatedDate() != null
						&& castOther.getCreatedDate() != null && this.getCreatedDate().equals(
						castOther.getCreatedDate())))
				&& ((this.getCreatedByUserId() == castOther.getCreatedByUserId()) || (this.getCreatedByUserId() != null
						&& castOther.getCreatedByUserId() != null && this.getCreatedByUserId().equals(
						castOther.getCreatedByUserId())))
				&& ((this.getCreatedByFullName() == castOther.getCreatedByFullName()) || (this.getCreatedByFullName() != null
						&& castOther.getCreatedByFullName() != null && this.getCreatedByFullName().equals(
						castOther.getCreatedByFullName())))
				&& ((this.getDeletedDate() == castOther.getDeletedDate()) || (this.getDeletedDate() != null
						&& castOther.getDeletedDate() != null && this.getDeletedDate().equals(
						castOther.getDeletedDate())))
				&& ((this.getDeletedByUserId() == castOther.getDeletedByUserId()) || (this.getDeletedByUserId() != null
						&& castOther.getDeletedByUserId() != null && this.getDeletedByUserId().equals(
						castOther.getDeletedByUserId())))
				&& ((this.getDeletedByFullName() == castOther.getDeletedByFullName()) || (this.getDeletedByFullName() != null
						&& castOther.getDeletedByFullName() != null && this.getDeletedByFullName().equals(
						castOther.getDeletedByFullName())))
				&& ((this.getDeactivationComments() == castOther.getDeactivationComments()) || (this
						.getDeactivationComments() != null && castOther.getDeactivationComments() != null && this
						.getDeactivationComments().equals(castOther.getDeactivationComments())))
				&& ((this.getSecondaryOrgText() == castOther.getSecondaryOrgText()) || (this.getSecondaryOrgText() != null
						&& castOther.getSecondaryOrgText() != null && this.getSecondaryOrgText().equals(
						castOther.getSecondaryOrgText())))
				&& ((this.getLastLoginDate() == castOther.getLastLoginDate()) || (this.getLastLoginDate() != null
						&& castOther.getLastLoginDate() != null && this.getLastLoginDate().equals(
						castOther.getLastLoginDate())))
				&& ((this.getImpaciiOnlyFlag() == castOther.getImpaciiOnlyFlag()) || (this.getImpaciiOnlyFlag() != null
						&& castOther.getImpaciiOnlyFlag() != null && this.getImpaciiOnlyFlag().equals(
						castOther.getImpaciiOnlyFlag())))
				&& ((this.getNotes() == castOther.getNotes()) || (this.getNotes() != null
						&& castOther.getNotes() != null && this.getNotes().equals(castOther.getNotes())))
				&& ((this.getNotesSubmittedByFullName() == castOther.getNotesSubmittedByFullName()) || (this
						.getNotesSubmittedByFullName() != null && castOther.getNotesSubmittedByFullName() != null && this
						.getNotesSubmittedByFullName().equals(castOther.getNotesSubmittedByFullName())))
				&& ((this.getNotesSubmittedDate() == castOther.getNotesSubmittedDate()) || (this
						.getNotesSubmittedDate() != null && castOther.getNotesSubmittedDate() != null && this
						.getNotesSubmittedDate().equals(castOther.getNotesSubmittedDate())))
				&& ((this.getSodFlag() == castOther.getSodFlag()) || (this.getSodFlag() != null
						&& castOther.getSodFlag() != null && this.getSodFlag().equals(castOther.getSodFlag())))
				&& ((this.getIcDiffFlag() == castOther.getIcDiffFlag()) || (this.getIcDiffFlag() != null
						&& castOther.getIcDiffFlag() != null && this.getIcDiffFlag().equals(castOther.getIcDiffFlag())))
				&& ((this.getNedInactiveFlag() == castOther.getNedInactiveFlag()) || (this.getNedInactiveFlag() != null
						&& castOther.getNedInactiveFlag() != null && this.getNedInactiveFlag().equals(
						castOther.getNedInactiveFlag())))
				&& ((this.getLastNameDiffFlag() == castOther.getLastNameDiffFlag()) || (this.getLastNameDiffFlag() != null
						&& castOther.getLastNameDiffFlag() != null && this.getLastNameDiffFlag().equals(
						castOther.getLastNameDiffFlag())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getImpaciiUserId() == null ? 0 : this.getImpaciiUserId().hashCode());
		result = 37 * result + (getNihNetworkId() == null ? 0 : this.getNihNetworkId().hashCode());
		result = 37 * result + (getImpaciiLastName() == null ? 0 : this.getImpaciiLastName().hashCode());
		result = 37 * result + (getImpaciiFirstName() == null ? 0 : this.getImpaciiFirstName().hashCode());
		result = 37 * result + (getNedLastName() == null ? 0 : this.getNedLastName().hashCode());
		result = 37 * result + (getNedFirstName() == null ? 0 : this.getNedFirstName().hashCode());
		result = 37 * result + (getNedEmailAddress() == null ? 0 : this.getNedEmailAddress().hashCode());
		result = 37 * result + (getParentNedOrgPath() == null ? 0 : this.getParentNedOrgPath().hashCode());
		result = 37 * result + (getNedOrgPath() == null ? 0 : this.getNedOrgPath().hashCode());
		result = 37 * result + (getNedIc() == null ? 0 : this.getNedIc().hashCode());
		result = 37 * result + (getNedActiveFlag() == null ? 0 : this.getNedActiveFlag().hashCode());
		result = 37 * result + (getNciDoc() == null ? 0 : this.getNciDoc().hashCode());
		result = 37 * result + (getCreatedDate() == null ? 0 : this.getCreatedDate().hashCode());
		result = 37 * result + (getCreatedByUserId() == null ? 0 : this.getCreatedByUserId().hashCode());
		result = 37 * result + (getCreatedByFullName() == null ? 0 : this.getCreatedByFullName().hashCode());
		result = 37 * result + (getDeletedDate() == null ? 0 : this.getDeletedDate().hashCode());
		result = 37 * result + (getDeletedByUserId() == null ? 0 : this.getDeletedByUserId().hashCode());
		result = 37 * result + (getDeletedByFullName() == null ? 0 : this.getDeletedByFullName().hashCode());
		result = 37 * result + (getDeactivationComments() == null ? 0 : this.getDeactivationComments().hashCode());
		result = 37 * result + (getSecondaryOrgText() == null ? 0 : this.getSecondaryOrgText().hashCode());
		result = 37 * result + (getLastLoginDate() == null ? 0 : this.getLastLoginDate().hashCode());
		result = 37 * result + (getImpaciiOnlyFlag() == null ? 0 : this.getImpaciiOnlyFlag().hashCode());
		result = 37 * result + (getNotes() == null ? 0 : this.getNotes().hashCode());
		result = 37 * result
				+ (getNotesSubmittedByFullName() == null ? 0 : this.getNotesSubmittedByFullName().hashCode());
		result = 37 * result + (getNotesSubmittedDate() == null ? 0 : this.getNotesSubmittedDate().hashCode());
		result = 37 * result + (getSodFlag() == null ? 0 : this.getSodFlag().hashCode());
		result = 37 * result + (getIcDiffFlag() == null ? 0 : this.getIcDiffFlag().hashCode());
		result = 37 * result + (getNedInactiveFlag() == null ? 0 : this.getNedInactiveFlag().hashCode());
		result = 37 * result + (getLastNameDiffFlag() == null ? 0 : this.getLastNameDiffFlag().hashCode());
		return result;
	}

	public List<EmPortfolioRolesVw> getAccountRoles() {
		return accountRoles;
	}

	public List<String> getAccountDiscrepancies() {
		return accountDiscrepancies;
	}

	public void setAccountRoles(List<EmPortfolioRolesVw> accountRoles) {
		this.accountRoles = accountRoles;
	}

	public void setAccountDiscrepancies(List<String> accountDiscrepancies) {
		this.accountDiscrepancies = accountDiscrepancies;
	}

}