package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import java.util.Date;

public class AuditSearchVO {

	private String userFirstname;
	private String userLastname;
	private String organization;
	private boolean excludeNCIOrgs;
	private String act;
	private Date dateRangeStartDate;
	private Date dateRangeEndDate;
	private Long category;
	private Long auditId;
	
	public AuditSearchVO(String fname, String lname, String org, boolean exclude, String action){
		
		this.userFirstname = fname;
		this.userLastname = lname;
		this.organization = org;
		this.excludeNCIOrgs = exclude;
		this.act = action;
	}
	
	public AuditSearchVO(){}
	
	public String getUserFirstname() {
		return userFirstname;
	}
	public void setUserFirstname(String userFirstname) {
		this.userFirstname = userFirstname;
	}
	public String getUserLastname() {
		return userLastname;
	}
	public void setUserLastname(String userLastname) {
		this.userLastname = userLastname;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public boolean isExcludeNCIOrgs() {
		return excludeNCIOrgs;
	}
	public void setExcludeNCIOrgs(boolean excludeNCIOrgs) {
		this.excludeNCIOrgs = excludeNCIOrgs;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String action) {
		this.act = action;
	}

	/**
	 * @return the dateRangeStartDate
	 */
	public Date getDateRangeStartDate() {
		return dateRangeStartDate;
	}

	/**
	 * @param dateRangeStartDate the dateRangeStartDate to set
	 */
	public void setDateRangeStartDate(Date dateRangeStartDate) {
		this.dateRangeStartDate = dateRangeStartDate;
	}

	/**
	 * @return the dateRangeEndDate
	 */
	public Date getDateRangeEndDate() {
		return dateRangeEndDate;
	}

	/**
	 * @param dateRangeEndDate the dateRangeEndDate to set
	 */
	public void setDateRangeEndDate(Date dateRangeEndDate) {
		this.dateRangeEndDate = dateRangeEndDate;
	}

	/**
	 * @return the category
	 */
	public Long getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Long category) {
		this.category = category;
	}

	/**
	 * @return the auditId
	 */
	public Long getAuditId() {
		return auditId;
	}

	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}
	
}
