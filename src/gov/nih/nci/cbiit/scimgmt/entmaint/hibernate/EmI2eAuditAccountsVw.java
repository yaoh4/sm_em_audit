package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

// Generated Jul 24, 2015 3:42:00 PM by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * EmI2eAuditAccountsVw generated by hbm2java
 */
public class EmI2eAuditAccountsVw implements java.io.Serializable {

	private Long id;
	private Long auditId;
	private String npnId;
	private String nihNetworkId;
	private String lastName;
	private String firstName;
	private Date createdDate;
	private String nedLastName;
	private String nedFirstName;
	private String i2eLastName;
	private String i2eFirstName;
	private String nedEmailAddress;
	private String parentNedOrgPath;
	private String nedOrgPath;
	private String nedIc;
	private Boolean nedActiveFlag;
	private String nciDoc;
	private String lastUpdByFullName;
	private AppLookupT action;
	private String notes;
	private String unsubmittedFlag;
	private String submittedBy;
	private Date submittedDate;
	private Boolean i2eOnlyFlag;
	private Boolean sodFlag;
	private Boolean nedInactiveFlag;
	private Boolean noActiveRoleFlag;
	private Boolean activeRoleRemainderFlag;
	private List<EmI2eAuditAccountRolesVw> accountRoles = new ArrayList<EmI2eAuditAccountRolesVw>(0);
	private List<String> accountDiscrepancies = new ArrayList<String>(0);

	public EmI2eAuditAccountsVw() {
	}

	public EmI2eAuditAccountsVw(Long id, Long auditId) {
		this.id = id;
		this.auditId = auditId;
	}

	public EmI2eAuditAccountsVw(Long id, Long auditId, String npnId, String nihNetworkId, String lastName,
			String firstName, Date createdDate, String nedLastName, String nedFirstName, String i2eLastName, String i2eFirstName,
			String nedEmailAddress, String parentNedOrgPath, String nedOrgPath, String nedIc, Boolean nedActiveFlag,
			String nciDoc, String lastUpdByFullName, AppLookupT action, String notes, String unsubmittedFlag,
			String submittedBy, Date submittedDate, Boolean i2eOnlyFlag, Boolean sodFlag, Boolean nedInactiveFlag, Boolean noActiveRoleFlag,
			Boolean activeRoleRemainderFlag, List accountRoles, List accountDiscrepancies) {
		this.id = id;
		this.auditId = auditId;
		this.npnId = npnId;
		this.nihNetworkId = nihNetworkId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.createdDate = createdDate;
		this.nedLastName = nedLastName;
		this.nedFirstName = nedFirstName;
		this.i2eLastName = i2eLastName;
		this.i2eFirstName = i2eFirstName;
		this.nedEmailAddress = nedEmailAddress;
		this.parentNedOrgPath = parentNedOrgPath;
		this.nedOrgPath = nedOrgPath;
		this.nedIc = nedIc;
		this.nedActiveFlag = nedActiveFlag;
		this.nciDoc = nciDoc;
		this.lastUpdByFullName = lastUpdByFullName;
		this.action = action;
		this.notes = notes;
		this.unsubmittedFlag = unsubmittedFlag;
		this.submittedBy = submittedBy;
		this.submittedDate = submittedDate;
		this.i2eOnlyFlag = i2eOnlyFlag;
		this.sodFlag = sodFlag;
		this.nedInactiveFlag = nedInactiveFlag;
		this.noActiveRoleFlag = noActiveRoleFlag;
		this.activeRoleRemainderFlag = activeRoleRemainderFlag;
		this.accountRoles = accountRoles;
		this.accountDiscrepancies = accountDiscrepancies;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAuditId() {
		return this.auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getNpnId() {
		return this.npnId;
	}

	public void setNpnId(String npnId) {
		this.npnId = npnId;
	}

	public String getNihNetworkId() {
		return this.nihNetworkId;
	}

	public void setNihNetworkId(String nihNetworkId) {
		this.nihNetworkId = nihNetworkId;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public String getI2eLastName() {
		return this.i2eLastName;
	}

	public void setI2eLastName(String i2eLastName) {
		this.i2eLastName = i2eLastName;
	}

	public String getI2eFirstName() {
		return this.i2eFirstName;
	}

	public void setI2eFirstName(String i2eFirstName) {
		this.i2eFirstName = i2eFirstName;
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

	public Boolean getNedActiveFlag() {
		return this.nedActiveFlag;
	}

	public void setNedActiveFlag(Boolean nedActiveFlag) {
		this.nedActiveFlag = nedActiveFlag;
	}

	public String getNciDoc() {
		return this.nciDoc;
	}

	public void setNciDoc(String nciDoc) {
		this.nciDoc = nciDoc;
	}

	public String getLastUpdByFullName() {
		return this.lastUpdByFullName;
	}

	public void setLastUpdByFullName(String lastUpdByFullName) {
		this.lastUpdByFullName = lastUpdByFullName;
	}

	public AppLookupT getAction() {
		return this.action;
	}

	public void setAction(AppLookupT action) {
		this.action = action;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getUnsubmittedFlag() {
		return this.unsubmittedFlag;
	}

	public void setUnsubmittedFlag(String unsubmittedFlag) {
		this.unsubmittedFlag = unsubmittedFlag;
	}

	public String getSubmittedBy() {
		return this.submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	public Date getSubmittedDate() {
		return this.submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	public Boolean getI2eOnlyFlag() {
		return this.i2eOnlyFlag;
	}

	public void setI2eOnlyFlag(Boolean i2eOnlyFlag) {
		this.i2eOnlyFlag = i2eOnlyFlag;
	}

	public Boolean getSodFlag() {
		return this.sodFlag;
	}

	public void setSodFlag(Boolean sodFlag) {
		this.sodFlag = sodFlag;
	}

	public Boolean getNedInactiveFlag() {
		return this.nedInactiveFlag;
	}

	public void setNedInactiveFlag(Boolean nedInactiveFlag) {
		this.nedInactiveFlag = nedInactiveFlag;
	}

	public Boolean getNoActiveRoleFlag() {
		return this.noActiveRoleFlag;
	}

	public void setNoActiveRoleFlag(Boolean noActiveRoleFlag) {
		this.noActiveRoleFlag = noActiveRoleFlag;
	}

	public Boolean getActiveRoleRemainderFlag() {
		return this.activeRoleRemainderFlag;
	}

	public void setActiveRoleRemainderFlag(Boolean activeRoleRemainderFlag) {
		this.activeRoleRemainderFlag = activeRoleRemainderFlag;
	}
	
	public List<EmI2eAuditAccountRolesVw> getAccountRoles() {
		return accountRoles;
	}

	public void setAccountRoles(List<EmI2eAuditAccountRolesVw> accountRoles) {
		this.accountRoles = accountRoles;
	}

	public List<String> getAccountDiscrepancies() {
		return accountDiscrepancies;
	}

	public void setAccountDiscrepancies(List<String> accountDiscrepancies) {
		this.accountDiscrepancies = accountDiscrepancies;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EmI2eAuditAccountsVw))
			return false;
		EmI2eAuditAccountsVw castOther = (EmI2eAuditAccountsVw) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null && castOther.getId() != null && this
				.getId().equals(castOther.getId())))
				&& ((this.getAuditId() == castOther.getAuditId()) || (this.getAuditId() != null
						&& castOther.getAuditId() != null && this.getAuditId().equals(castOther.getAuditId())))
				&& ((this.getNpnId() == castOther.getNpnId()) || (this.getNpnId() != null
						&& castOther.getNpnId() != null && this.getNpnId().equals(castOther.getNpnId())))
				&& ((this.getNihNetworkId() == castOther.getNihNetworkId()) || (this.getNihNetworkId() != null
						&& castOther.getNihNetworkId() != null && this.getNihNetworkId().equals(
						castOther.getNihNetworkId())))
				&& ((this.getLastName() == castOther.getLastName()) || (this.getLastName() != null
						&& castOther.getLastName() != null && this.getLastName().equals(castOther.getLastName())))
				&& ((this.getFirstName() == castOther.getFirstName()) || (this.getFirstName() != null
						&& castOther.getFirstName() != null && this.getFirstName().equals(castOther.getFirstName())))
				&& ((this.getCreatedDate() == castOther.getCreatedDate()) || (this.getCreatedDate() != null
						&& castOther.getCreatedDate() != null && this.getCreatedDate().equals(castOther.getCreatedDate())))
				&& ((this.getNedLastName() == castOther.getNedLastName()) || (this.getNedLastName() != null
						&& castOther.getNedLastName() != null && this.getNedLastName().equals(
						castOther.getNedLastName())))
				&& ((this.getNedFirstName() == castOther.getNedFirstName()) || (this.getNedFirstName() != null
						&& castOther.getNedFirstName() != null && this.getNedFirstName().equals(
						castOther.getNedFirstName())))
				&& ((this.getI2eLastName() == castOther.getI2eLastName()) || (this.getI2eLastName() != null
						&& castOther.getI2eLastName() != null && this.getI2eLastName().equals(
						castOther.getI2eLastName())))
				&& ((this.getI2eFirstName() == castOther.getI2eFirstName()) || (this.getI2eFirstName() != null
						&& castOther.getI2eFirstName() != null && this.getI2eFirstName().equals(
						castOther.getI2eFirstName())))
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
				&& ((this.getLastUpdByFullName() == castOther.getLastUpdByFullName()) || (this.getLastUpdByFullName() != null
						&& castOther.getLastUpdByFullName() != null && this.getLastUpdByFullName().equals(
						castOther.getLastUpdByFullName())))
				&& ((this.getAction() == castOther.getAction()) || (this.getAction() != null
						&& castOther.getAction() != null && this.getAction().equals(castOther.getAction())))
				&& ((this.getNotes() == castOther.getNotes()) || (this.getNotes() != null
						&& castOther.getNotes() != null && this.getNotes().equals(castOther.getNotes())))
				&& ((this.getUnsubmittedFlag() == castOther.getUnsubmittedFlag()) || (this.getUnsubmittedFlag() != null
						&& castOther.getUnsubmittedFlag() != null && this.getUnsubmittedFlag().equals(
						castOther.getUnsubmittedFlag())))
				&& ((this.getSubmittedBy() == castOther.getSubmittedBy()) || (this.getSubmittedBy() != null
						&& castOther.getSubmittedBy() != null && this.getSubmittedBy().equals(
						castOther.getSubmittedBy())))
				&& ((this.getSubmittedDate() == castOther.getSubmittedDate()) || (this.getSubmittedDate() != null
						&& castOther.getSubmittedDate() != null && this.getSubmittedDate().equals(
						castOther.getSubmittedDate())))
				&& ((this.getI2eOnlyFlag() == castOther.getI2eOnlyFlag()) || (this.getI2eOnlyFlag() != null
						&& castOther.getI2eOnlyFlag() != null && this.getI2eOnlyFlag().equals(
						castOther.getI2eOnlyFlag())))
				&& ((this.getSodFlag() == castOther.getSodFlag()) || (this.getSodFlag() != null
						&& castOther.getSodFlag() != null && this.getSodFlag().equals(castOther.getSodFlag())))
				&& ((this.getNedInactiveFlag() == castOther.getNedInactiveFlag()) || (this.getNedInactiveFlag() != null
						&& castOther.getNedInactiveFlag() != null && this.getNedInactiveFlag().equals(
						castOther.getNedInactiveFlag())))
				&& ((this.getNoActiveRoleFlag() == castOther.getNoActiveRoleFlag()) || (this.getNoActiveRoleFlag() != null
						&& castOther.getNoActiveRoleFlag() != null && this.getNoActiveRoleFlag().equals(
						castOther.getNoActiveRoleFlag())))
				&& ((this.getActiveRoleRemainderFlag() == castOther.getActiveRoleRemainderFlag()) || (this
						.getActiveRoleRemainderFlag() != null && castOther.getActiveRoleRemainderFlag() != null && this
						.getActiveRoleRemainderFlag().equals(castOther.getActiveRoleRemainderFlag())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result + (getAuditId() == null ? 0 : this.getAuditId().hashCode());
		result = 37 * result + (getNpnId() == null ? 0 : this.getNpnId().hashCode());
		result = 37 * result + (getNihNetworkId() == null ? 0 : this.getNihNetworkId().hashCode());
		result = 37 * result + (getLastName() == null ? 0 : this.getLastName().hashCode());
		result = 37 * result + (getFirstName() == null ? 0 : this.getFirstName().hashCode());
		result = 37 * result + (getCreatedDate() == null ? 0 : this.getCreatedDate().hashCode());
		result = 37 * result + (getNedLastName() == null ? 0 : this.getNedLastName().hashCode());
		result = 37 * result + (getNedFirstName() == null ? 0 : this.getNedFirstName().hashCode());
		result = 37 * result + (getI2eLastName() == null ? 0 : this.getI2eLastName().hashCode());
		result = 37 * result + (getI2eFirstName() == null ? 0 : this.getI2eFirstName().hashCode());
		result = 37 * result + (getNedEmailAddress() == null ? 0 : this.getNedEmailAddress().hashCode());
		result = 37 * result + (getParentNedOrgPath() == null ? 0 : this.getParentNedOrgPath().hashCode());
		result = 37 * result + (getNedOrgPath() == null ? 0 : this.getNedOrgPath().hashCode());
		result = 37 * result + (getNedIc() == null ? 0 : this.getNedIc().hashCode());
		result = 37 * result + (getNedActiveFlag() == null ? 0 : this.getNedActiveFlag().hashCode());
		result = 37 * result + (getNciDoc() == null ? 0 : this.getNciDoc().hashCode());
		result = 37 * result + (getLastUpdByFullName() == null ? 0 : this.getLastUpdByFullName().hashCode());
		result = 37 * result + (getAction() == null ? 0 : this.getAction().hashCode());
		result = 37 * result + (getNotes() == null ? 0 : this.getNotes().hashCode());
		result = 37 * result + (getUnsubmittedFlag() == null ? 0 : this.getUnsubmittedFlag().hashCode());
		result = 37 * result + (getSubmittedBy() == null ? 0 : this.getSubmittedBy().hashCode());
		result = 37 * result + (getSubmittedDate() == null ? 0 : this.getSubmittedDate().hashCode());
		result = 37 * result + (getI2eOnlyFlag() == null ? 0 : this.getI2eOnlyFlag().hashCode());
		result = 37 * result + (getSodFlag() == null ? 0 : this.getSodFlag().hashCode());
		result = 37 * result + (getNedInactiveFlag() == null ? 0 : this.getNedInactiveFlag().hashCode());
		result = 37 * result + (getNoActiveRoleFlag() == null ? 0 : this.getNoActiveRoleFlag().hashCode());
		result = 37 * result
				+ (getActiveRoleRemainderFlag() == null ? 0 : this.getActiveRoleRemainderFlag().hashCode());
		return result;
	}

}
