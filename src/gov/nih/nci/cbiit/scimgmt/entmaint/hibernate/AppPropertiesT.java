package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

// Generated Mar 12, 2015 11:22:56 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * AppPropertiesT generated by hbm2java
 */
public class AppPropertiesT implements java.io.Serializable {

	private String appName;
	private String propKey;
	private String propValue;
	private Date createDate;
	private Date lastChangeDate;

	public AppPropertiesT() {
	}

	public AppPropertiesT(String appName, String propKey, String propValue, Date createDate) {
		this.appName = appName;
		this.propKey = propKey;
		this.propValue = propValue;
		this.createDate = createDate;
	}

	public AppPropertiesT(String appName, String propKey, String propValue, Date createDate, Date lastChangeDate) {
		this.appName = appName;
		this.propKey = propKey;
		this.propValue = propValue;
		this.createDate = createDate;
		this.lastChangeDate = lastChangeDate;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPropKey() {
		return this.propKey;
	}

	public void setPropKey(String propKey) {
		this.propKey = propKey;
	}

	public String getPropValue() {
		return this.propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastChangeDate() {
		return this.lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AppPropertiesT))
			return false;
		AppPropertiesT castOther = (AppPropertiesT) other;

		return ((this.getAppName() == castOther.getAppName()) || (this.getAppName() != null
				&& castOther.getAppName() != null && this.getAppName().equals(castOther.getAppName())))
				&& ((this.getPropKey() == castOther.getPropKey()) || (this.getPropKey() != null
						&& castOther.getPropKey() != null && this.getPropKey().equals(castOther.getPropKey())))
				&& ((this.getPropValue() == castOther.getPropValue()) || (this.getPropValue() != null
						&& castOther.getPropValue() != null && this.getPropValue().equals(castOther.getPropValue())))
				&& ((this.getCreateDate() == castOther.getCreateDate()) || (this.getCreateDate() != null
						&& castOther.getCreateDate() != null && this.getCreateDate().equals(castOther.getCreateDate())))
				&& ((this.getLastChangeDate() == castOther.getLastChangeDate()) || (this.getLastChangeDate() != null
						&& castOther.getLastChangeDate() != null && this.getLastChangeDate().equals(
						castOther.getLastChangeDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getAppName() == null ? 0 : this.getAppName().hashCode());
		result = 37 * result + (getPropKey() == null ? 0 : this.getPropKey().hashCode());
		result = 37 * result + (getPropValue() == null ? 0 : this.getPropValue().hashCode());
		result = 37 * result + (getCreateDate() == null ? 0 : this.getCreateDate().hashCode());
		result = 37 * result + (getLastChangeDate() == null ? 0 : this.getLastChangeDate().hashCode());
		return result;
	}

}
