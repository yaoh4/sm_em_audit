package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

// Generated Mar 13, 2015 11:23:47 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * EmDiscrepancyTypesT generated by hbm2java
 */
public class EmDiscrepancyTypesT implements java.io.Serializable {

	private String code;
	private String shortDescrip;
	private String longDescrip;
	private String resolutionText;
	private String createDate;
	private String createUserId;
	private Date lastChangeDate;
	private String lastChangeUserId;

	public EmDiscrepancyTypesT() {
	}

	public EmDiscrepancyTypesT(String code, String createDate, String createUserId) {
		this.code = code;
		this.createDate = createDate;
		this.createUserId = createUserId;
	}

	public EmDiscrepancyTypesT(String code, String shortDescrip, String longDescrip, String resolutionText,
			String createDate, String createUserId, Date lastChangeDate, String lastChangeUserId) {
		this.code = code;
		this.shortDescrip = shortDescrip;
		this.longDescrip = longDescrip;
		this.resolutionText = resolutionText;
		this.createDate = createDate;
		this.createUserId = createUserId;
		this.lastChangeDate = lastChangeDate;
		this.lastChangeUserId = lastChangeUserId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getShortDescrip() {
		return this.shortDescrip;
	}

	public void setShortDescrip(String shortDescrip) {
		this.shortDescrip = shortDescrip;
	}

	public String getLongDescrip() {
		return this.longDescrip;
	}

	public void setLongDescrip(String longDescrip) {
		this.longDescrip = longDescrip;
	}

	public String getResolutionText() {
		return this.resolutionText;
	}

	public void setResolutionText(String resolutionText) {
		this.resolutionText = resolutionText;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getLastChangeDate() {
		return this.lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	public String getLastChangeUserId() {
		return this.lastChangeUserId;
	}

	public void setLastChangeUserId(String lastChangeUserId) {
		this.lastChangeUserId = lastChangeUserId;
	}

}
