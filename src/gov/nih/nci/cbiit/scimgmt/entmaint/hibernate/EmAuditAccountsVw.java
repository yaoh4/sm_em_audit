package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

// Generated Mar 5, 2015 1:53:49 PM by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * EmAuditAccountsVw generated by hbm2java
 */
public class EmAuditAccountsVw implements java.io.Serializable {

	private Long id;
	private EmAuditsT audit;
	private String impaciiUserId;
	private String nihNetworkId;
	private String lastName;
	private String firstName;
	private String impaciiLastName;
	private String impaciiFirstName;
	private String nedLastName;
	private String nedFirstName;
	private String nedEmailAddress;
	private String parentNedOrgPath;
	private String nedOrgPath;
	private String nedIc;
	private String nciDoc;
	private Date createdDate;
	private String createdByUserId;
	private String createdByFullName;
	private Date deletedDate;
	private String deletedByUserId;
	private String deletedByFullName;
	private String deactivationComments;
	private String secondaryOrgText;
	private Date lastLoginDate;
	private String inactiveUserFlag;
	private List<EmAuditAccountRolesVw> accountRoles = new ArrayList<EmAuditAccountRolesVw>(0);
	private EmAuditAccountActivityVw accountActivity;
	private AppLookupT activeAction;
	private String activeNotes;
	private String activeUnsubmittedFlag;
	private String activeSubmittedBy;
	private Date activeSubmittedDate;
	private AppLookupT newAction;
	private String newNotes;
	private String newUnsubmittedFlag;
	private String newSubmittedBy;
	private Date newSubmittedDate;
	private AppLookupT deletedAction;
	private String deletedNotes;
	private String deletedUnsubmittedFlag;
	private String deletedSubmittedBy;
	private Date deletedSubmittedDate;
	private AppLookupT inactiveAction;
	private String inactiveNotes;
	private String inactiveUnsubmittedFlag;
	private String inactiveSubmittedBy;
	private Date inactiveSubmittedDate;
	private String deletedByParentOrgPath;
	private String deletedByNciDoc;
	private Boolean sodFlag;
	private Boolean icDiffFlag;
	private Boolean nedInactiveFlag;
	private Boolean lastNameDiffFlag;
	private List<String> accountDiscrepancies = new ArrayList<String>(0);
	
	public EmAuditAccountsVw() {
	}

	public EmAuditAccountsVw(Long id, EmAuditsT audit, Date createdDate, String createdByUserId) {
		this.id = id;
		this.audit = audit;
		this.createdDate = createdDate;
		this.createdByUserId = createdByUserId;
	}

	public EmAuditAccountsVw(Long id, EmAuditsT audit, String impaciiUserId, String nihNetworkId, String lastName, String firstName,
			String impaciiLastName, String impaciiFirstName, String nedLastName, String nedFirstName,
			String nedEmailAddress, String parentNedOrgPath, String nedOrgPath, String nedIc,
			String nciDoc, Date createdDate, String createdByUserId, String createdByFullName, Date deletedDate,
			String deletedByUserId, String deletedByFullName, String deactivationComments, String secondaryOrgText,
			Date lastLoginDate, String inactiveUserFlag, AppLookupT activeAction, String activeNotes,
			String activeUnsubmittedFlag, String activeSubmittedBy, Date activeSubmittedDate, AppLookupT newAction,
			String newNotes, String newUnsubmittedFlag, String newSubmittedBy, Date newSubmittedDate,
			AppLookupT deletedAction, String deletedNotes, String deletedUnsubmittedFlag, String deletedSubmittedBy,
			Date deletedSubmittedDate, AppLookupT inactiveAction, String inactiveNotes, String inactiveUnsubmittedFlag,
			String inactiveSubmittedBy, Date inactiveSubmittedDate, String deletedByParentOrgPath, String deletedByNciDoc,
			Boolean sodFlag, Boolean icDiffFlag, Boolean nedInactiveFlag,
			Boolean lastNameDiffFlag, List accountRoles, List accountActivities,
			EmAuditAccountActivityVw accountActivity, List accountDiscrepancies) {
		this.id = id;
		this.audit = audit;
		this.impaciiUserId = impaciiUserId;
		this.nihNetworkId = nihNetworkId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.impaciiLastName = impaciiLastName;
		this.impaciiFirstName = impaciiFirstName;
		this.nedLastName = nedLastName;
		this.nedFirstName = nedFirstName;
		this.nedEmailAddress = nedEmailAddress;
		this.nedOrgPath = nedOrgPath;
		this.parentNedOrgPath = parentNedOrgPath;
		this.nedIc = nedIc;
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
		this.inactiveUserFlag = inactiveUserFlag;
		this.accountRoles = accountRoles;
		this.accountActivity = accountActivity;
		this.activeAction = activeAction;
		this.activeNotes = activeNotes;
		this.activeUnsubmittedFlag = activeUnsubmittedFlag;
		this.activeSubmittedBy = activeSubmittedBy;
		this.activeSubmittedDate = activeSubmittedDate;
		this.newAction = newAction;
		this.newNotes = newNotes;
		this.newUnsubmittedFlag = newUnsubmittedFlag;
		this.newSubmittedBy = newSubmittedBy;
		this.newSubmittedDate = newSubmittedDate;
		this.deletedAction = deletedAction;
		this.deletedNotes = deletedNotes;
		this.deletedUnsubmittedFlag = deletedUnsubmittedFlag;
		this.deletedSubmittedBy = deletedSubmittedBy;
		this.deletedSubmittedDate = deletedSubmittedDate;
		this.inactiveAction = inactiveAction;
		this.inactiveNotes = inactiveNotes;
		this.inactiveUnsubmittedFlag = inactiveUnsubmittedFlag;
		this.inactiveSubmittedBy = inactiveSubmittedBy;
		this.inactiveSubmittedDate = inactiveSubmittedDate;
		this.deletedByParentOrgPath = deletedByParentOrgPath;
		this.deletedByNciDoc = deletedByNciDoc;
		this.sodFlag = sodFlag;
		this.icDiffFlag = icDiffFlag;
		this.nedInactiveFlag = nedInactiveFlag;
		this.lastNameDiffFlag = lastNameDiffFlag;
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
		if (!(other instanceof EmAuditAccountsVw)) {
			return false;
		}
		EmAuditAccountsVw castOther = (EmAuditAccountsVw) other;

		return (getId() == castOther.getId() || getId() != null && castOther.getId() != null
				&& getId().equals(castOther.getId()))
				&& (getAudit() == castOther.getAudit() || getAudit() != null && castOther.getAudit() != null
						&& getAudit().equals(castOther.getAudit()))
				&& (getImpaciiUserId() == castOther.getImpaciiUserId() || getImpaciiUserId() != null
						&& castOther.getImpaciiUserId() != null
						&& getImpaciiUserId().equals(castOther.getImpaciiUserId()))
				&& (getNihNetworkId() == castOther.getNihNetworkId() || getNihNetworkId() != null
						&& castOther.getNihNetworkId() != null
						&& getNihNetworkId().equals(castOther.getNihNetworkId()))
				&& (getLastName() == castOther.getLastName() || getLastName() != null
						&& castOther.getLastName() != null && getLastName().equals(castOther.getLastName()))
				&& (getFirstName() == castOther.getFirstName() || getFirstName() != null
						&& castOther.getFirstName() != null && getFirstName().equals(castOther.getFirstName()))
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
						&& castOther.getParentNedOrgPath() != null
						&& getParentNedOrgPath().equals(castOther.getParentNedOrgPath()))
				&& (getNedIc() == castOther.getNedIc() || getNedIc() != null && castOther.getNedIc() != null
						&& getNedIc().equals(castOther.getNedIc()))
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
				&& (getInactiveUserFlag() == castOther.getInactiveUserFlag() || getInactiveUserFlag() != null
						&& castOther.getInactiveUserFlag() != null
						&& getInactiveUserFlag().equals(castOther.getInactiveUserFlag()))
				&& ((getActiveAction() == castOther.getActiveAction()) || (getActiveAction() != null
						&& castOther.getActiveAction() != null && getActiveAction().equals(
						castOther.getActiveAction())))
				&& ((getActiveNotes() == castOther.getActiveNotes()) || (getActiveNotes() != null
						&& castOther.getActiveNotes() != null && getActiveNotes().equals(
						castOther.getActiveNotes())))
				&& ((getActiveUnsubmittedFlag() == castOther.getActiveUnsubmittedFlag()) || (this
						.getActiveUnsubmittedFlag() != null && castOther.getActiveUnsubmittedFlag() != null && this
						.getActiveUnsubmittedFlag().equals(castOther.getActiveUnsubmittedFlag())))
				&& ((getActiveSubmittedBy() == castOther.getActiveSubmittedBy()) || (getActiveSubmittedBy() != null
						&& castOther.getActiveSubmittedBy() != null && getActiveSubmittedBy().equals(
						castOther.getActiveSubmittedBy())))
				&& ((getActiveSubmittedDate() == castOther.getActiveSubmittedDate()) || (this
						.getActiveSubmittedDate() != null && castOther.getActiveSubmittedDate() != null && this
						.getActiveSubmittedDate().equals(castOther.getActiveSubmittedDate())))
				&& ((getNewAction() == castOther.getNewAction()) || (getNewAction() != null
						&& castOther.getNewAction() != null && getNewAction().equals(
						castOther.getNewAction())))
				&& ((getNewNotes() == castOther.getNewNotes()) || (getNewNotes() != null
						&& castOther.getNewNotes() != null && getNewNotes().equals(castOther.getNewNotes())))
				&& ((getNewUnsubmittedFlag() == castOther.getNewUnsubmittedFlag()) || (this
						.getNewUnsubmittedFlag() != null && castOther.getNewUnsubmittedFlag() != null && this
						.getNewUnsubmittedFlag().equals(castOther.getNewUnsubmittedFlag())))
				&& ((getNewSubmittedBy() == castOther.getNewSubmittedBy()) || (getNewSubmittedBy() != null
						&& castOther.getNewSubmittedBy() != null && getNewSubmittedBy().equals(
						castOther.getNewSubmittedBy())))
				&& ((getNewSubmittedDate() == castOther.getNewSubmittedDate()) || (getNewSubmittedDate() != null
						&& castOther.getNewSubmittedDate() != null && getNewSubmittedDate().equals(
						castOther.getNewSubmittedDate())))
				&& ((getDeletedAction() == castOther.getDeletedAction()) || (getDeletedAction() != null
						&& castOther.getDeletedAction() != null && getDeletedAction().equals(
						castOther.getDeletedAction())))
				&& ((getDeletedNotes() == castOther.getDeletedNotes()) || (getDeletedNotes() != null
						&& castOther.getDeletedNotes() != null && getDeletedNotes().equals(
						castOther.getDeletedNotes())))
				&& ((getDeletedUnsubmittedFlag() == castOther.getDeletedUnsubmittedFlag()) || (this
						.getDeletedUnsubmittedFlag() != null && castOther.getDeletedUnsubmittedFlag() != null && this
						.getDeletedUnsubmittedFlag().equals(castOther.getDeletedUnsubmittedFlag())))
				&& ((getDeletedSubmittedBy() == castOther.getDeletedSubmittedBy()) || (this
						.getDeletedSubmittedBy() != null && castOther.getDeletedSubmittedBy() != null && this
						.getDeletedSubmittedBy().equals(castOther.getDeletedSubmittedBy())))
				&& ((getDeletedSubmittedDate() == castOther.getDeletedSubmittedDate()) || (this
						.getDeletedSubmittedDate() != null && castOther.getDeletedSubmittedDate() != null && this
						.getDeletedSubmittedDate().equals(castOther.getDeletedSubmittedDate())))
				&& ((getInactiveAction() == castOther.getInactiveAction()) || (getInactiveAction() != null
						&& castOther.getInactiveAction() != null && getInactiveAction().equals(
						castOther.getInactiveAction())))
				&& ((getInactiveNotes() == castOther.getInactiveNotes()) || (getInactiveNotes() != null
						&& castOther.getInactiveNotes() != null && getInactiveNotes().equals(
						castOther.getInactiveNotes())))
				&& ((getInactiveUnsubmittedFlag() == castOther.getInactiveUnsubmittedFlag()) || (this
						.getInactiveUnsubmittedFlag() != null && castOther.getInactiveUnsubmittedFlag() != null && this
						.getInactiveUnsubmittedFlag().equals(castOther.getInactiveUnsubmittedFlag())))
				&& ((getInactiveSubmittedBy() == castOther.getInactiveSubmittedBy()) || (this
						.getInactiveSubmittedBy() != null && castOther.getInactiveSubmittedBy() != null && this
						.getInactiveSubmittedBy().equals(castOther.getInactiveSubmittedBy())))
				&& ((getInactiveSubmittedDate() == castOther.getInactiveSubmittedDate()) || (this
						.getInactiveSubmittedDate() != null && castOther.getInactiveSubmittedDate() != null && this
						.getInactiveSubmittedDate().equals(castOther.getInactiveSubmittedDate())))
				&& ((getDeletedByParentOrgPath() == castOther.getDeletedByParentOrgPath()) || (this
						.getDeletedByParentOrgPath() != null && castOther.getDeletedByParentOrgPath() != null && this
						.getDeletedByParentOrgPath().equals(castOther.getDeletedByParentOrgPath())))
				&& ((getDeletedByNciDoc() == castOther.getDeletedByNciDoc()) || (this
						.getDeletedByNciDoc() != null && castOther.getDeletedByNciDoc() != null && this
						.getDeletedByNciDoc().equals(castOther.getDeletedByNciDoc())))
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

	public EmAuditsT getAudit() {
		return audit;
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

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getImpaciiFirstName() {
		return impaciiFirstName;
	}

	public String getImpaciiLastName() {
		return impaciiLastName;
	}

	public String getImpaciiUserId() {
		return impaciiUserId;
	}

	public String getInactiveUserFlag() {
		return inactiveUserFlag;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public String getNciDoc() {
		return nciDoc;
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

	public String getSecondaryOrgText() {
		return secondaryOrgText;
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : getId().hashCode());
		result = 37 * result + (getAudit() == null ? 0 : getAudit().hashCode());
		result = 37 * result + (getImpaciiUserId() == null ? 0 : getImpaciiUserId().hashCode());
		result = 37 * result + (getNihNetworkId() == null ? 0 : getNihNetworkId().hashCode());
		result = 37 * result + (getLastName() == null ? 0 : getLastName().hashCode());
		result = 37 * result + (getFirstName() == null ? 0 : getFirstName().hashCode());
		result = 37 * result + (getImpaciiLastName() == null ? 0 : getImpaciiLastName().hashCode());
		result = 37 * result + (getImpaciiFirstName() == null ? 0 : getImpaciiFirstName().hashCode());
		result = 37 * result + (getNedLastName() == null ? 0 : getNedLastName().hashCode());
		result = 37 * result + (getNedFirstName() == null ? 0 : getNedFirstName().hashCode());
		result = 37 * result + (getNedEmailAddress() == null ? 0 : getNedEmailAddress().hashCode());
		result = 37 * result + (getNedOrgPath() == null ? 0 : getNedOrgPath().hashCode());
		result = 37 * result + (getParentNedOrgPath() == null ? 0 : getParentNedOrgPath().hashCode());
		result = 37 * result + (getNedIc() == null ? 0 : getNedIc().hashCode());
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
		result = 37 * result + (getInactiveUserFlag() == null ? 0 : getInactiveUserFlag().hashCode());
		result = 37 * result + (getActiveAction() == null ? 0 : getActiveAction().hashCode());
		result = 37 * result + (getActiveNotes() == null ? 0 : getActiveNotes().hashCode());
		result = 37 * result + (getActiveUnsubmittedFlag() == null ? 0 : getActiveUnsubmittedFlag().hashCode());
		result = 37 * result + (getActiveSubmittedBy() == null ? 0 : getActiveSubmittedBy().hashCode());
		result = 37 * result + (getActiveSubmittedDate() == null ? 0 : getActiveSubmittedDate().hashCode());
		result = 37 * result + (getNewAction() == null ? 0 : getNewAction().hashCode());
		result = 37 * result + (getNewNotes() == null ? 0 : getNewNotes().hashCode());
		result = 37 * result + (getNewUnsubmittedFlag() == null ? 0 : getNewUnsubmittedFlag().hashCode());
		result = 37 * result + (getNewSubmittedBy() == null ? 0 : getNewSubmittedBy().hashCode());
		result = 37 * result + (getNewSubmittedDate() == null ? 0 : getNewSubmittedDate().hashCode());
		result = 37 * result + (getDeletedAction() == null ? 0 : getDeletedAction().hashCode());
		result = 37 * result + (getDeletedNotes() == null ? 0 : getDeletedNotes().hashCode());
		result = 37 * result + (getDeletedUnsubmittedFlag() == null ? 0 : getDeletedUnsubmittedFlag().hashCode());
		result = 37 * result + (getDeletedSubmittedBy() == null ? 0 : getDeletedSubmittedBy().hashCode());
		result = 37 * result + (getDeletedSubmittedDate() == null ? 0 : getDeletedSubmittedDate().hashCode());
		result = 37 * result + (getInactiveAction() == null ? 0 : getInactiveAction().hashCode());
		result = 37 * result + (getInactiveNotes() == null ? 0 : getInactiveNotes().hashCode());
		result = 37 * result
				+ (getInactiveUnsubmittedFlag() == null ? 0 : getInactiveUnsubmittedFlag().hashCode());
		result = 37 * result + (getInactiveSubmittedBy() == null ? 0 : getInactiveSubmittedBy().hashCode());
		result = 37 * result + (getInactiveSubmittedDate() == null ? 0 : getInactiveSubmittedDate().hashCode());
		result = 37 * result + (getDeletedByParentOrgPath() == null ? 0 : getDeletedByParentOrgPath().hashCode());
		result = 37 * result + (getDeletedByNciDoc() == null ? 0 : getDeletedByNciDoc().hashCode());
		result = 37 * result + (getSodFlag() == null ? 0 : this.getSodFlag().hashCode());
		result = 37 * result + (getIcDiffFlag() == null ? 0 : this.getIcDiffFlag().hashCode());
		result = 37 * result + (getNedInactiveFlag() == null ? 0 : this.getNedInactiveFlag().hashCode());
		result = 37 * result + (getLastNameDiffFlag() == null ? 0 : this.getLastNameDiffFlag().hashCode());
		return result;
	}

	public void setAudit(EmAuditsT audit) {
		this.audit = audit;
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

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setImpaciiFirstName(String impaciiFirstName) {
		this.impaciiFirstName = impaciiFirstName;
	}
	
	public void setImpaciiLastName(String impaciiLastName) {
		this.impaciiLastName = impaciiLastName;
	}

	public void setImpaciiUserId(String impaciiUserId) {
		this.impaciiUserId = impaciiUserId;
	}

	public void setInactiveUserFlag(String inactiveUserFlag) {
		this.inactiveUserFlag = inactiveUserFlag;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public void setNciDoc(String nciDoc) {
		this.nciDoc = nciDoc;
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

	public void setSecondaryOrgText(String secondaryOrgText) {
		this.secondaryOrgText = secondaryOrgText;
	}

	public List<EmAuditAccountRolesVw> getAccountRoles() {
		return accountRoles;
	}

	public void setAccountRoles(List<EmAuditAccountRolesVw> accountRoles) {
		this.accountRoles = accountRoles;
	}
	
	public void addAccountRole(EmAuditAccountRolesVw accountRole) {
		accountRoles.add(accountRole);
	}

	public EmAuditAccountActivityVw getAccountActivity() {
		return accountActivity;
	}

	public void setAccountActivity(EmAuditAccountActivityVw accountActivity) {
		this.accountActivity = accountActivity;
	}

	public AppLookupT getActiveAction() {
		return activeAction;
	}

	public String getActiveNotes() {
		return activeNotes;
	}

	public String getActiveUnsubmittedFlag() {
		return activeUnsubmittedFlag;
	}

	public String getActiveSubmittedBy() {
		return activeSubmittedBy;
	}

	public Date getActiveSubmittedDate() {
		return activeSubmittedDate;
	}

	public AppLookupT getNewAction() {
		return newAction;
	}

	public String getNewNotes() {
		return newNotes;
	}

	public String getNewUnsubmittedFlag() {
		return newUnsubmittedFlag;
	}

	public String getNewSubmittedBy() {
		return newSubmittedBy;
	}

	public Date getNewSubmittedDate() {
		return newSubmittedDate;
	}

	public AppLookupT getDeletedAction() {
		return deletedAction;
	}

	public String getDeletedNotes() {
		return deletedNotes;
	}

	public String getDeletedUnsubmittedFlag() {
		return deletedUnsubmittedFlag;
	}

	public String getDeletedSubmittedBy() {
		return deletedSubmittedBy;
	}

	public Date getDeletedSubmittedDate() {
		return deletedSubmittedDate;
	}

	public AppLookupT getInactiveAction() {
		return inactiveAction;
	}

	public String getInactiveNotes() {
		return inactiveNotes;
	}

	public String getInactiveUnsubmittedFlag() {
		return inactiveUnsubmittedFlag;
	}

	public String getInactiveSubmittedBy() {
		return inactiveSubmittedBy;
	}

	public Date getInactiveSubmittedDate() {
		return inactiveSubmittedDate;
	}

	public String getDeletedByParentOrgPath() {
		return deletedByParentOrgPath;
	}

	public String getDeletedByNciDoc() {
		return deletedByNciDoc;
	}
	
	public void setActiveAction(AppLookupT activeAction) {
		this.activeAction = activeAction;
	}

	public void setActiveNotes(String activeNotes) {
		this.activeNotes = activeNotes;
	}

	public void setActiveUnsubmittedFlag(String activeUnsubmittedFlag) {
		this.activeUnsubmittedFlag = activeUnsubmittedFlag;
	}

	public void setActiveSubmittedBy(String activeSubmittedBy) {
		this.activeSubmittedBy = activeSubmittedBy;
	}

	public void setActiveSubmittedDate(Date activeSubmittedDate) {
		this.activeSubmittedDate = activeSubmittedDate;
	}

	public void setNewAction(AppLookupT newAction) {
		this.newAction = newAction;
	}

	public void setNewNotes(String newNotes) {
		this.newNotes = newNotes;
	}

	public void setNewUnsubmittedFlag(String newUnsubmittedFlag) {
		this.newUnsubmittedFlag = newUnsubmittedFlag;
	}

	public void setNewSubmittedBy(String newSubmittedBy) {
		this.newSubmittedBy = newSubmittedBy;
	}

	public void setNewSubmittedDate(Date newSubmittedDate) {
		this.newSubmittedDate = newSubmittedDate;
	}

	public void setDeletedAction(AppLookupT deletedAction) {
		this.deletedAction = deletedAction;
	}

	public void setDeletedNotes(String deletedNotes) {
		this.deletedNotes = deletedNotes;
	}

	public void setDeletedUnsubmittedFlag(String deletedUnsubmittedFlag) {
		this.deletedUnsubmittedFlag = deletedUnsubmittedFlag;
	}

	public void setDeletedSubmittedBy(String deletedSubmittedBy) {
		this.deletedSubmittedBy = deletedSubmittedBy;
	}

	public void setDeletedSubmittedDate(Date deletedSubmittedDate) {
		this.deletedSubmittedDate = deletedSubmittedDate;
	}

	public void setInactiveAction(AppLookupT inactiveAction) {
		this.inactiveAction = inactiveAction;
	}

	public void setInactiveNotes(String inactiveNotes) {
		this.inactiveNotes = inactiveNotes;
	}

	public void setInactiveUnsubmittedFlag(String inactiveUnsubmittedFlag) {
		this.inactiveUnsubmittedFlag = inactiveUnsubmittedFlag;
	}

	public void setInactiveSubmittedBy(String inactiveSubmittedBy) {
		this.inactiveSubmittedBy = inactiveSubmittedBy;
	}

	public void setInactiveSubmittedDate(Date inactiveSubmittedDate) {
		this.inactiveSubmittedDate = inactiveSubmittedDate;
	}

	public void setDeletedByParentOrgPath(String deletedByParentOrgPath) {
		this.deletedByParentOrgPath = deletedByParentOrgPath;
	}

	public void setDeletedByNciDoc(String deletedByNciDoc) {
		this.deletedByNciDoc = deletedByNciDoc;
	}

	public List<String> getAccountDiscrepancies() {
		return accountDiscrepancies;
	}

	public void setAccountDiscrepancies(List<String> accountDiscrepancies) {
		this.accountDiscrepancies = accountDiscrepancies;
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

}
