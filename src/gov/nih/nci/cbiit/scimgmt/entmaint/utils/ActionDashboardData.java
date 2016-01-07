package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;

public class ActionDashboardData {
	
	private String orgName;
	private int activeUnknownCount;
	private int newUnknownCount;
	private int deletedUnknownCount;
	private int i2eUnknownCount;

	public ActionDashboardData() {
	}
	
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the unknownAccountDataStr
	 */
	public String getActiveUnknownDataStr(String key) {
		if(this.activeUnknownCount != 0){
			return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_ACTIVE + "\",\"" + key + "\",\"" + ApplicationConstants.ACTIVE_ACTION_UNKNOWN + "\");'>" + this.activeUnknownCount + " </a>";
		}else{
			return getActiveUnknownDataStr();
		}
	}

	public String getActiveUnknownDataStr() {
		return "<a>" + this.activeUnknownCount + " </a>";
	}
	
	/**
	 * @return the newUnknownDataStr
	 */
	public String getNewUnknownDataStr(String key) {
		if(this.newUnknownCount != 0){
			return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_NEW + "\",\"" + key + "\",\"" + ApplicationConstants.NEW_ACTION_UNKNOWN + "\");'>" + this.newUnknownCount + " </a>";
		}else{
			return getNewUnknownDataStr();
		}
	}
	
	public String getNewUnknownDataStr() {
		return "<a>" + this.newUnknownCount + " </a>";
	}
	/**
	 * @return the deletedUnknownDataStr
	 */
	public String getDeletedUnknownDataStr(String key) {
		if(this.deletedUnknownCount != 0){
			return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_DELETED + "\",\"" + key + "\",\"" + ApplicationConstants.DELETED_ACTION_UNKNOWN + "\");'>" + this.deletedUnknownCount + " </a>";
		}else{
			return getDeletedUnknownDataStr();
		}
	}
	public String getDeletedUnknownDataStr() {
		return "<a>" + this.deletedUnknownCount + " </a>";
	}
	/**
	 * @return the i2eUnknownDataStr
	 */
	public String getI2eUnknownDataStr(String key) {
		if(this.i2eUnknownCount != 0){
			return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_I2E + "\",\"" + key + "\",\"" + ApplicationConstants.ACTIVE_ACTION_UNKNOWN + "\");'>" + this.i2eUnknownCount + " </a>";
		}else{
			return getI2eUnknownDataStr();
		}
	}

	public String getI2eUnknownDataStr() {
		return "<a>" + this.i2eUnknownCount + " </a>";
	}
	/**
	 * @return the activeUnknownCount
	 */
	public int getActiveUnknownCount() {
		return activeUnknownCount;
	}
	/**
	 * @param activeUnknownCount the activeUnknownCount to set
	 */
	public void setActiveUnknownCount(int activeUnknownCount) {
		this.activeUnknownCount = activeUnknownCount;
	}
	/**
	 * @return the newUnknownCount
	 */
	public int getNewUnknownCount() {
		return newUnknownCount;
	}
	/**
	 * @param newUnknownCount the newUnknownCount to set
	 */
	public void setNewUnknownCount(int newUnknownCount) {
		this.newUnknownCount = newUnknownCount;
	}
	/**
	 * @return the deletedUnknownCount
	 */
	public int getDeletedUnknownCount() {
		return deletedUnknownCount;
	}
	/**
	 * @param deletedUnknownCount the deletedUnknownCount to set
	 */
	public void setDeletedUnknownCount(int deletedUnknownCount) {
		this.deletedUnknownCount = deletedUnknownCount;
	}
	/**
	 * @return the i2eUnknownCount
	 */
	public int getI2eUnknownCount() {
		return i2eUnknownCount;
	}
	/**
	 * @param i2eUnknownCount the i2eUnknownCount to set
	 */
	public void setI2eUnknownCount(int i2eUnknownCount) {
		this.i2eUnknownCount = i2eUnknownCount;
	}

}
