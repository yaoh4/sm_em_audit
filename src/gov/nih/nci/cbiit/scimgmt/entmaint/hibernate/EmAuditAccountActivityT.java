package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

// Generated Mar 5, 2015 3:44:04 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * EmAuditAccountActivityT generated by hbm2java
 */
public class EmAuditAccountActivityT implements java.io.Serializable {

	private Long eaaId;
	private AppLookupT category;
	private Long actionId;
	private String createUserId;
	private Date createDate;
	private String lastChangeUserId;
	private Date lastChangeDate;
	private String unsubmittedFlag;
	private String notes;

	public EmAuditAccountActivityT() {
	}

	public EmAuditAccountActivityT(Long eaaId, AppLookupT category, Long actionId, String createUserId,
			Date createDate, String lastChangeUserId, Date lastChangeDate, String unsubmittedFlag, String notes) {
		this.eaaId = eaaId;
		this.category = category;
		this.actionId = actionId;
		this.createUserId = createUserId;
		this.createDate = createDate;
		this.lastChangeUserId = lastChangeUserId;
		this.lastChangeDate = lastChangeDate;
		this.unsubmittedFlag = unsubmittedFlag;
		this.notes = notes;
	}

	public EmAuditAccountActivityT(Long eaaId, AppLookupT category, String createUserId, Date createDate) {
		this.eaaId = eaaId;
		this.category = category;
		this.createUserId = createUserId;
		this.createDate = createDate;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof EmAuditAccountActivityT)) {
			return false;
		}
		EmAuditAccountActivityT castOther = (EmAuditAccountActivityT) other;

		return (getEaaId() == castOther.getEaaId() || getEaaId() != null && castOther.getEaaId() != null
				&& getEaaId().equals(castOther.getEaaId()))
				&& (getCategory() == castOther.getCategory() || getCategory() != null
						&& castOther.getCategory() != null && getCategory().equals(castOther.getCategory()))
				&& (getActionId() == castOther.getActionId() || getActionId() != null
						&& castOther.getActionId() != null && getActionId().equals(castOther.getActionId()))
				&& (getCreateUserId() == castOther.getCreateUserId() || getCreateUserId() != null
						&& castOther.getCreateUserId() != null
						&& getCreateUserId().equals(castOther.getCreateUserId()))
				&& (getCreateDate() == castOther.getCreateDate() || getCreateDate() != null
						&& castOther.getCreateDate() != null && getCreateDate().equals(castOther.getCreateDate()))
				&& (getLastChangeUserId() == castOther.getLastChangeUserId() || getLastChangeUserId() != null
						&& castOther.getLastChangeUserId() != null
						&& getLastChangeUserId().equals(castOther.getLastChangeUserId()))
				&& (getLastChangeDate() == castOther.getLastChangeDate() || getLastChangeDate() != null
						&& castOther.getLastChangeDate() != null
						&& getLastChangeDate().equals(castOther.getLastChangeDate()))
				&& (getUnsubmittedFlag() == castOther.getUnsubmittedFlag() || getUnsubmittedFlag() != null
						&& castOther.getUnsubmittedFlag() != null
						&& getUnsubmittedFlag().equals(castOther.getUnsubmittedFlag()))
				&& (getNotes() == castOther.getNotes() || getNotes() != null && castOther.getNotes() != null
						&& getNotes().equals(castOther.getNotes()));
	}

	public Long getActionId() {
		return actionId;
	}

	public AppLookupT getCategory() {
		return category;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public Long getEaaId() {
		return eaaId;
	}

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public String getLastChangeUserId() {
		return lastChangeUserId;
	}

	public String getNotes() {
		return notes;
	}

	public String getUnsubmittedFlag() {
		return unsubmittedFlag;
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getEaaId() == null ? 0 : getEaaId().hashCode());
		result = 37 * result + (getCategory() == null ? 0 : getCategory().hashCode());
		result = 37 * result + (getActionId() == null ? 0 : getActionId().hashCode());
		result = 37 * result + (getCreateUserId() == null ? 0 : getCreateUserId().hashCode());
		result = 37 * result + (getCreateDate() == null ? 0 : getCreateDate().hashCode());
		result = 37 * result + (getLastChangeUserId() == null ? 0 : getLastChangeUserId().hashCode());
		result = 37 * result + (getLastChangeDate() == null ? 0 : getLastChangeDate().hashCode());
		result = 37 * result + (getUnsubmittedFlag() == null ? 0 : getUnsubmittedFlag().hashCode());
		result = 37 * result + (getNotes() == null ? 0 : getNotes().hashCode());
		return result;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	public void setCategory(AppLookupT category) {
		this.category = category;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public void setEaaId(Long eaaId) {
		this.eaaId = eaaId;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	public void setLastChangeUserId(String lastChangeUserId) {
		this.lastChangeUserId = lastChangeUserId;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setUnsubmittedFlag(String unsubmittedFlag) {
		this.unsubmittedFlag = unsubmittedFlag;
	}

}
