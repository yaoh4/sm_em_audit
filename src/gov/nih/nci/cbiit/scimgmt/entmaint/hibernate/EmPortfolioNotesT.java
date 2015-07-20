package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

// Generated Mar 5, 2015 1:58:31 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * EmPortfolioNotesT generated by hbm2java
 */
public class EmPortfolioNotesT implements java.io.Serializable {

	private String impaciiUserId;
	private String notes;
	private String createUserId;
	private Date createDate;
	private String lastChangeUserId;
	private Date lastChangeDate;

	public EmPortfolioNotesT() {
	}

	public EmPortfolioNotesT(String impaciiUserId, String createUserId, Date createDate) {
		this.impaciiUserId = impaciiUserId;
		this.createUserId = createUserId;
		this.createDate = createDate;
	}

	public EmPortfolioNotesT(String impaciiUserId, String notes, String createUserId, Date createDate,
			String lastChangeUserId, Date lastChangeDate) {
		this.impaciiUserId = impaciiUserId;
		this.notes = notes;
		this.createUserId = createUserId;
		this.createDate = createDate;
		this.lastChangeUserId = lastChangeUserId;
		this.lastChangeDate = lastChangeDate;
	}

	public String getImpaciiUserId() {
		return this.impaciiUserId;
	}

	public void setImpaciiUserId(String impaciiUserId) {
		this.impaciiUserId = impaciiUserId;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getLastChangeUserId() {
		return this.lastChangeUserId;
	}

	public void setLastChangeUserId(String lastChangeUserId) {
		this.lastChangeUserId = lastChangeUserId;
	}

	public Date getLastChangeDate() {
		return this.lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

}
