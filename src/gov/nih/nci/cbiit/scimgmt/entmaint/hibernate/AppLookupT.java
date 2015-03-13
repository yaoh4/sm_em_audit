package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

// Generated Mar 5, 2015 8:47:08 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * AppLookupT generated by hbm2java
 */
public class AppLookupT implements java.io.Serializable {

	private Long id;
	private String discriminator;
	private String code;
	private String description;
	private Boolean active;
	private Integer orderNum;
	private Date createDate;
	private String createUserId;
	private Date lastChangeDate;
	private String lastChangeUserId;
	private String applicationName;

	public AppLookupT() {
	}

	public AppLookupT(Long id, String discriminator, String code, String description, Boolean active,
			Integer orderNum, Date createDate, String createUserId) {
		this.id = id;
		this.discriminator = discriminator;
		this.code = code;
		this.description = description;
		this.active = active;
		this.orderNum = orderNum;
		this.createDate = createDate;
		this.createUserId = createUserId;
	}

	public AppLookupT(Long id, String discriminator, String code, String description, Boolean active,
			Integer orderNum, Date createDate, String createUserId, Date lastChangeDate, String lastChangeUserId,
			String applicationName) {
		this.id = id;
		this.discriminator = discriminator;
		this.code = code;
		this.description = description;
		this.active = active;
		this.orderNum = orderNum;
		this.createDate = createDate;
		this.createUserId = createUserId;
		this.lastChangeDate = lastChangeDate;
		this.lastChangeUserId = lastChangeUserId;
		this.applicationName = applicationName;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDiscriminator() {
		return this.discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
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

	public String getApplicationName() {
		return this.applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

}
