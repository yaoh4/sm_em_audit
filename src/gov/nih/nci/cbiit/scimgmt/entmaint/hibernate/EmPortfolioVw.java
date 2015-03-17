package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

// Generated Mar 5, 2015 1:55:08 PM by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * EmPortfolioVw generated by hbm2java
 */
public class EmPortfolioVw implements java.io.Serializable {

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
	private List<EmPortfolioRolesVw> accountRoles = new ArrayList<EmPortfolioRolesVw>(0);
	private List<EmDiscrepancyTypesT> accountDiscrepancies = new ArrayList<EmDiscrepancyTypesT>(0);
	
	public EmPortfolioVw() {
	}

	public EmPortfolioVw(String impaciiUserId, String impaciiLastName, Date createdDate, String createdByUserId) {
		this.impaciiUserId = impaciiUserId;
		this.impaciiLastName = impaciiLastName;
		this.createdDate = createdDate;
		this.createdByUserId = createdByUserId;
	}

	public EmPortfolioVw(String impaciiUserId, String nihNetworkId, String impaciiLastName, String impaciiFirstName,
			String nedLastName, String nedFirstName, String nedEmailAddress, String parentNedOrgPath, String nedOrgPath, String nedIc,
			String nedActiveFlag, String nciDoc, Date createdDate, String createdByUserId, String createdByFullName,
			Date deletedDate, String deletedByUserId, String deletedByFullName, String deactivationComments,
			String secondaryOrgText, String lastLoginDate, String impaciiOnlyFlag, String notes,
			String notesSubmittedByFullName, Date notesSubmittedDate,
			List accountRoles, List accountDiscrepancies) {
		this.impaciiUserId = impaciiUserId;
		this.nihNetworkId = nihNetworkId;
		this.impaciiLastName = impaciiLastName;
		this.impaciiFirstName = impaciiFirstName;
		this.nedLastName = nedLastName;
		this.nedFirstName = nedFirstName;
		this.nedEmailAddress = nedEmailAddress;
		this.nedOrgPath = nedOrgPath;
		this.parentNedOrgPath = parentNedOrgPath;
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
		this.accountRoles = accountRoles;
		this.accountDiscrepancies = accountDiscrepancies;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof EmPortfolioVw)) {
			return false;
		}
		EmPortfolioVw castOther = (EmPortfolioVw) other;

		return (getImpaciiUserId() == castOther.getImpaciiUserId() || getImpaciiUserId() != null
				&& castOther.getImpaciiUserId() != null && getImpaciiUserId().equals(castOther.getImpaciiUserId()))
				&& (getNihNetworkId() == castOther.getNihNetworkId() || getNihNetworkId() != null
						&& castOther.getNihNetworkId() != null
						&& getNihNetworkId().equals(castOther.getNihNetworkId()))
				&& (getImpaciiLastName() == castOther.getImpaciiLastName() || getImpaciiLastName() != null
						&& castOther.getImpaciiLastName() != null
						&& getImpaciiLastName().equals(castOther.getImpaciiLastName()))
				&& (getImpaciiFirstName() == castOther.getImpaciiFirstName() || getImpaciiFirstName() != null
						&& castOther.getImpaciiFirstName() != null
						&& getImpaciiFirstName().equals(castOther.getImpaciiFirstName()))
				&& (getNedLastName() == castOther.getNedLastName() || getNedLastName() != null
						&& castOther.getNedLastName() != null && getNedLastName().equals(castOther.getNedLastName()))
				&& (getNedFirstName() == castOther.getNedFirstName() || getNedFirstName() != null
						&& castOther.getNedFirstName() != null
						&& getNedFirstName().equals(castOther.getNedFirstName()))
				&& (getNedEmailAddress() == castOther.getNedEmailAddress() || getNedEmailAddress() != null
						&& castOther.getNedEmailAddress() != null
						&& getNedEmailAddress().equals(castOther.getNedEmailAddress()))
				&& (getNedOrgPath() == castOther.getNedOrgPath() || getNedOrgPath() != null
						&& castOther.getNedOrgPath() != null && getNedOrgPath().equals(castOther.getNedOrgPath()))
				&& (getParentNedOrgPath() == castOther.getParentNedOrgPath() || getParentNedOrgPath() != null
						&& castOther.getParentNedOrgPath() != null && getParentNedOrgPath().equals(castOther.getParentNedOrgPath()))
				&& (getNedIc() == castOther.getNedIc() || getNedIc() != null && castOther.getNedIc() != null
						&& getNedIc().equals(castOther.getNedIc()))
				&& (getNedActiveFlag() == castOther.getNedActiveFlag() || getNedActiveFlag() != null
						&& castOther.getNedActiveFlag() != null
						&& getNedActiveFlag().equals(castOther.getNedActiveFlag()))
				&& (getNciDoc() == castOther.getNciDoc() || getNciDoc() != null && castOther.getNciDoc() != null
						&& getNciDoc().equals(castOther.getNciDoc()))
				&& (getCreatedDate() == castOther.getCreatedDate() || getCreatedDate() != null
						&& castOther.getCreatedDate() != null && getCreatedDate().equals(castOther.getCreatedDate()))
				&& (getCreatedByUserId() == castOther.getCreatedByUserId() || getCreatedByUserId() != null
						&& castOther.getCreatedByUserId() != null
						&& getCreatedByUserId().equals(castOther.getCreatedByUserId()))
				&& (getCreatedByFullName() == castOther.getCreatedByFullName() || getCreatedByFullName() != null
						&& castOther.getCreatedByFullName() != null
						&& getCreatedByFullName().equals(castOther.getCreatedByFullName()))
				&& (getDeletedDate() == castOther.getDeletedDate() || getDeletedDate() != null
						&& castOther.getDeletedDate() != null && getDeletedDate().equals(castOther.getDeletedDate()))
				&& (getDeletedByUserId() == castOther.getDeletedByUserId() || getDeletedByUserId() != null
						&& castOther.getDeletedByUserId() != null
						&& getDeletedByUserId().equals(castOther.getDeletedByUserId()))
				&& (getDeletedByFullName() == castOther.getDeletedByFullName() || getDeletedByFullName() != null
						&& castOther.getDeletedByFullName() != null
						&& getDeletedByFullName().equals(castOther.getDeletedByFullName()))
				&& (getDeactivationComments() == castOther.getDeactivationComments() || getDeactivationComments() != null
						&& castOther.getDeactivationComments() != null
						&& getDeactivationComments().equals(castOther.getDeactivationComments()))
				&& (getSecondaryOrgText() == castOther.getSecondaryOrgText() || getSecondaryOrgText() != null
						&& castOther.getSecondaryOrgText() != null
						&& getSecondaryOrgText().equals(castOther.getSecondaryOrgText()))
				&& (getLastLoginDate() == castOther.getLastLoginDate() || getLastLoginDate() != null
						&& castOther.getLastLoginDate() != null
						&& getLastLoginDate().equals(castOther.getLastLoginDate()))
				&& (getImpaciiOnlyFlag() == castOther.getImpaciiOnlyFlag() || getImpaciiOnlyFlag() != null
						&& castOther.getImpaciiOnlyFlag() != null
						&& getImpaciiOnlyFlag().equals(castOther.getImpaciiOnlyFlag()))
				&& (getNotes() == castOther.getNotes() || getNotes() != null && castOther.getNotes() != null
						&& getNotes().equals(castOther.getNotes()))
				&& (getNotesSubmittedByFullName() == castOther.getNotesSubmittedByFullName() || getNotesSubmittedByFullName() != null
						&& castOther.getNotesSubmittedByFullName() != null
						&& getNotesSubmittedByFullName().equals(castOther.getNotesSubmittedByFullName()))
				&& (getNotesSubmittedDate() == castOther.getNotesSubmittedDate() || getNotesSubmittedDate() != null
						&& castOther.getNotesSubmittedDate() != null
						&& getNotesSubmittedDate().equals(castOther.getNotesSubmittedDate()));
	}

	public String getCreatedByFullName() {
		return createdByFullName;
	}

	public String getCreatedByUserId() {
		return createdByUserId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getDeactivationComments() {
		return deactivationComments;
	}

	public String getDeletedByFullName() {
		return deletedByFullName;
	}

	public String getDeletedByUserId() {
		return deletedByUserId;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public String getImpaciiFirstName() {
		return impaciiFirstName;
	}

	public String getImpaciiLastName() {
		return impaciiLastName;
	}

	public String getImpaciiOnlyFlag() {
		return impaciiOnlyFlag;
	}

	public String getImpaciiUserId() {
		return impaciiUserId;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}

	public String getNciDoc() {
		return nciDoc;
	}

	public String getNedActiveFlag() {
		return nedActiveFlag;
	}

	public String getNedEmailAddress() {
		return nedEmailAddress;
	}

	public String getNedFirstName() {
		return nedFirstName;
	}

	public String getNedIc() {
		return nedIc;
	}

	public String getNedLastName() {
		return nedLastName;
	}

	public String getNedOrgPath() {
		return nedOrgPath;
	}
	
	public String getParentNedOrgPath() {
		return parentNedOrgPath;
	}

	public String getNihNetworkId() {
		return nihNetworkId;
	}

	public String getNotes() {
		return notes;
	}

	public String getNotesSubmittedByFullName() {
		return notesSubmittedByFullName;
	}

	public Date getNotesSubmittedDate() {
		return notesSubmittedDate;
	}

	public String getSecondaryOrgText() {
		return secondaryOrgText;
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getImpaciiUserId() == null ? 0 : getImpaciiUserId().hashCode());
		result = 37 * result + (getNihNetworkId() == null ? 0 : getNihNetworkId().hashCode());
		result = 37 * result + (getImpaciiLastName() == null ? 0 : getImpaciiLastName().hashCode());
		result = 37 * result + (getImpaciiFirstName() == null ? 0 : getImpaciiFirstName().hashCode());
		result = 37 * result + (getNedLastName() == null ? 0 : getNedLastName().hashCode());
		result = 37 * result + (getNedFirstName() == null ? 0 : getNedFirstName().hashCode());
		result = 37 * result + (getNedEmailAddress() == null ? 0 : getNedEmailAddress().hashCode());
		result = 37 * result + (getNedOrgPath() == null ? 0 : getNedOrgPath().hashCode());
		result = 37 * result + (getParentNedOrgPath() == null ? 0 : getParentNedOrgPath().hashCode());
		result = 37 * result + (getNedIc() == null ? 0 : getNedIc().hashCode());
		result = 37 * result + (getNedActiveFlag() == null ? 0 : getNedActiveFlag().hashCode());
		result = 37 * result + (getNciDoc() == null ? 0 : getNciDoc().hashCode());
		result = 37 * result + (getCreatedDate() == null ? 0 : getCreatedDate().hashCode());
		result = 37 * result + (getCreatedByUserId() == null ? 0 : getCreatedByUserId().hashCode());
		result = 37 * result + (getCreatedByFullName() == null ? 0 : getCreatedByFullName().hashCode());
		result = 37 * result + (getDeletedDate() == null ? 0 : getDeletedDate().hashCode());
		result = 37 * result + (getDeletedByUserId() == null ? 0 : getDeletedByUserId().hashCode());
		result = 37 * result + (getDeletedByFullName() == null ? 0 : getDeletedByFullName().hashCode());
		result = 37 * result + (getDeactivationComments() == null ? 0 : getDeactivationComments().hashCode());
		result = 37 * result + (getSecondaryOrgText() == null ? 0 : getSecondaryOrgText().hashCode());
		result = 37 * result + (getLastLoginDate() == null ? 0 : getLastLoginDate().hashCode());
		result = 37 * result + (getImpaciiOnlyFlag() == null ? 0 : getImpaciiOnlyFlag().hashCode());
		result = 37 * result + (getNotes() == null ? 0 : getNotes().hashCode());
		result = 37 * result + (getNotesSubmittedByFullName() == null ? 0 : getNotesSubmittedByFullName().hashCode());
		result = 37 * result + (getNotesSubmittedDate() == null ? 0 : getNotesSubmittedDate().hashCode());
		return result;
	}

	public void setCreatedByFullName(String createdByFullName) {
		this.createdByFullName = createdByFullName;
	}

	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setDeactivationComments(String deactivationComments) {
		this.deactivationComments = deactivationComments;
	}

	public void setDeletedByFullName(String deletedByFullName) {
		this.deletedByFullName = deletedByFullName;
	}

	public void setDeletedByUserId(String deletedByUserId) {
		this.deletedByUserId = deletedByUserId;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public void setImpaciiFirstName(String impaciiFirstName) {
		this.impaciiFirstName = impaciiFirstName;
	}

	public void setImpaciiLastName(String impaciiLastName) {
		this.impaciiLastName = impaciiLastName;
	}

	public void setImpaciiOnlyFlag(String impaciiOnlyFlag) {
		this.impaciiOnlyFlag = impaciiOnlyFlag;
	}

	public void setImpaciiUserId(String impaciiUserId) {
		this.impaciiUserId = impaciiUserId;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public void setNciDoc(String nciDoc) {
		this.nciDoc = nciDoc;
	}

	public void setNedActiveFlag(String nedActiveFlag) {
		this.nedActiveFlag = nedActiveFlag;
	}

	public void setNedEmailAddress(String nedEmailAddress) {
		this.nedEmailAddress = nedEmailAddress;
	}

	public void setNedFirstName(String nedFirstName) {
		this.nedFirstName = nedFirstName;
	}

	public void setNedIc(String nedIc) {
		this.nedIc = nedIc;
	}

	public void setNedLastName(String nedLastName) {
		this.nedLastName = nedLastName;
	}

	public void setNedOrgPath(String nedOrgPath) {
		this.nedOrgPath = nedOrgPath;
	}

	public void setParentNedOrgPath(String parentNedOrgPath) {
		this.parentNedOrgPath = parentNedOrgPath;
	}
	
	public void setNihNetworkId(String nihNetworkId) {
		this.nihNetworkId = nihNetworkId;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setNotesSubmittedByFullName(String notesSubmittedByFullName) {
		this.notesSubmittedByFullName = notesSubmittedByFullName;
	}

	public void setNotesSubmittedDate(Date notesSubmittedDate) {
		this.notesSubmittedDate = notesSubmittedDate;
	}

	public void setSecondaryOrgText(String secondaryOrgText) {
		this.secondaryOrgText = secondaryOrgText;
	}
	
	public List<EmPortfolioRolesVw> getAccountRoles() {
		return accountRoles;
	}

	public void setAccountRoles(List<EmPortfolioRolesVw> accountRoles) {
		this.accountRoles = accountRoles;
	}
	
	public List<EmDiscrepancyTypesT> getAccountDiscrepancies() {
		return accountDiscrepancies;
	}

	public void setAccountDiscrepancies(List<EmDiscrepancyTypesT> accountDiscrepancies) {
		this.accountDiscrepancies = accountDiscrepancies;
	}
}
