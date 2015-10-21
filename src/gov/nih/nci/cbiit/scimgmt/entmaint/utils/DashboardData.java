package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;

public class DashboardData {
	
	private String orgName;
	private int activeAccountCount;
	private int newAccountCount;
	private int deletedAccountCount;
	private int inactiveAccountCount;
	private int i2eAccountCount;
	private int activeCompleteCount;
	private int newCompleteCount;
	private int deletedCompleteCount;
	private int inactiveCompleteCount;
	private int i2eCompleteCount;

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
	 * @return the activeAccountDataStr
	 */
	public String getActiveAccountDataStr(String key) {
		if(this.activeAccountCount != 0){
			return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_ACTIVE + "\",\"" + key + "\");'>" + this.activeCompleteCount + "/" + this.activeAccountCount + " </a><span class='percent'>(" + this.getActivePercent() + "%)</span>";
		}else{
			return getActiveAccountDataStr();
		}
	}

	public String getActiveAccountDataStr() {
		return "<a>" + this.activeCompleteCount + "/" + this.activeAccountCount + " </a><span class='percent'>(" + this.getActivePercent() + "%)</span>";
	}
	
	/**
	 * @return the newAccountDataStr
	 */
	public String getNewAccountDataStr(String key) {
		if(this.newAccountCount != 0){
			return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_NEW + "\",\"" + key + "\");'>" + this.newCompleteCount + "/" + this.newAccountCount + " </a><span class='percent'>(" + this.getNewPercent() + "%)</span>";
		}else{
			return getNewAccountDataStr();
		}
	}
	
	public String getNewAccountDataStr() {
		return "<a>" + this.newCompleteCount + "/" + this.newAccountCount + " </a><span class='percent'>(" + this.getNewPercent() + "%)</span>";
	}
	/**
	 * @return the deletedAccountDataStr
	 */
	public String getDeletedAccountDataStr(String key) {
		if(this.deletedAccountCount != 0){
			return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_DELETED + "\",\"" + key + "\");'>" + this.deletedCompleteCount + "/" + this.deletedAccountCount + " </a><span class='percent'>(" + this.getDeletedPercent() + "%)</span>";
		}else{
			return getDeletedAccountDataStr();
		}
	}
	public String getDeletedAccountDataStr() {
		return "<a>" + this.deletedCompleteCount + "/" + this.deletedAccountCount + " </a><span class='percent'>(" + this.getDeletedPercent() + "%)</span>";
	}
	/**
	 * @return the inactiveAccountDataStr
	 */
	public String getInactiveAccountDataStr(String key) {
		if(this.inactiveAccountCount != 0){
			return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_INACTIVE + "\",\"" + key + "\");'>" + this.inactiveCompleteCount + "/" + this.inactiveAccountCount + " </a><span class='percent'>(" + this.getInactivePercent() + "%)</span>";
		}else{
			return getInactiveAccountDataStr();
		}
	}
	public String getInactiveAccountDataStr() {
		return "<a>" + this.inactiveCompleteCount + "/" + this.inactiveAccountCount + " </a><span class='percent'>(" + this.getInactivePercent() + "%)</span>";
	}
	/**
	 * @return the i2eAccountDataStr
	 */
	public String getI2eAccountDataStr(String key) {
		if(this.i2eAccountCount != 0){
			return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_I2E + "\",\"" + key + "\");'>" + this.i2eCompleteCount + "/" + this.i2eAccountCount + " </a><span class='percent'>(" + this.getI2ePercent() + "%)</span>";
		}else{
			return getI2eAccountDataStr();
		}
	}

	public String getI2eAccountDataStr() {
		return "<a>" + this.i2eCompleteCount + "/" + this.i2eAccountCount + " </a><span class='percent'>(" + this.getI2ePercent() + "%)</span>";
	}
	/**
	 * @return the activeAccountCount
	 */
	public int getActiveAccountCount() {
		return activeAccountCount;
	}
	/**
	 * @param activeAccountCount the activeAccountCount to set
	 */
	public void setActiveAccountCount(int activeAccountCount) {
		this.activeAccountCount = activeAccountCount;
	}
	/**
	 * @return the newAccountCount
	 */
	public int getNewAccountCount() {
		return newAccountCount;
	}
	/**
	 * @param newAccountCount the newAccountCount to set
	 */
	public void setNewAccountCount(int newAccountCount) {
		this.newAccountCount = newAccountCount;
	}
	/**
	 * @return the deletedAccountCount
	 */
	public int getDeletedAccountCount() {
		return deletedAccountCount;
	}
	/**
	 * @param deletedAccountCount the deletedAccountCount to set
	 */
	public void setDeletedAccountCount(int deletedAccountCount) {
		this.deletedAccountCount = deletedAccountCount;
	}
	/**
	 * @return the inactiveAccountCount
	 */
	public int getInactiveAccountCount() {
		return inactiveAccountCount;
	}
	/**
	 * @param inactiveAccountCount the inactiveAccountCount to set
	 */
	public void setInactiveAccountCount(int inactiveAccountCount) {
		this.inactiveAccountCount = inactiveAccountCount;
	}
	/**
	 * @return the i2eAccountCount
	 */
	public int getI2eAccountCount() {
		return i2eAccountCount;
	}
	/**
	 * @param i2eAccountCount the i2eAccountCount to set
	 */
	public void setI2eAccountCount(int i2eAccountCount) {
		this.i2eAccountCount = i2eAccountCount;
	}
	/**
	 * @return the activeCompleteCount
	 */
	public int getActiveCompleteCount() {
		return activeCompleteCount;
	}
	/**
	 * @param activeCompleteCount the activeCompleteCount to set
	 */
	public void setActiveCompleteCount(int activeCompleteCount) {
		this.activeCompleteCount = activeCompleteCount;
	}
	/**
	 * @return the newCompleteCount
	 */
	public int getNewCompleteCount() {
		return newCompleteCount;
	}
	/**
	 * @param newCompleteCount the newCompleteCount to set
	 */
	public void setNewCompleteCount(int newCompleteCount) {
		this.newCompleteCount = newCompleteCount;
	}
	/**
	 * @return the deletedCompleteCount
	 */
	public int getDeletedCompleteCount() {
		return deletedCompleteCount;
	}
	/**
	 * @param deletedCompleteCount the deletedCompleteCount to set
	 */
	public void setDeletedCompleteCount(int deletedCompleteCount) {
		this.deletedCompleteCount = deletedCompleteCount;
	}
	/**
	 * @return the inactiveCompleteCount
	 */
	public int getInactiveCompleteCount() {
		return inactiveCompleteCount;
	}
	/**
	 * @param inactiveCompleteCount the inactiveCompleteCount to set
	 */
	public void setInactiveCompleteCount(int inactiveCompleteCount) {
		this.inactiveCompleteCount = inactiveCompleteCount;
	}
	/**
	 * @return the i2eCompleteCount
	 */
	public int getI2eCompleteCount() {
		return i2eCompleteCount;
	}
	/**
	 * @param i2eCompleteCount the i2eCompleteCount to set
	 */
	public void setI2eCompleteCount(int i2eCompleteCount) {
		this.i2eCompleteCount = i2eCompleteCount;
	}
	/**
	 * @return the activePercent
	 */
	public int getActivePercent() {
		if(activeAccountCount == 0){
			return 0;
		}else{
			double percent = 100 * this.activeCompleteCount / this.activeAccountCount;
			return (int)percent;
		}
	}

	/**
	 * @return the newPercent
	 */
	public int getNewPercent() {
		if(newAccountCount == 0){
			return 0;
		}else{
			double percent = 100 * this.newCompleteCount / this.newAccountCount;
			return (int)percent;
		}
	}
	/**
	 * @return the deletedPercent
	 */
	public int getDeletedPercent() {
		if(deletedAccountCount == 0){
			return 0;
		}else{
			double percent = 100 * this.deletedCompleteCount / this.deletedAccountCount;
			return (int)percent;
		}
	}
	/**
	 * @return the inactivePercent
	 */
	public int getInactivePercent() {
		if(inactiveAccountCount == 0){
			return 0;
		}else{
			double percent = 100 * this.inactiveCompleteCount / this.inactiveAccountCount;
			return (int)percent;
		}
	}
	/**
	 * @return the i2ePercent
	 */
	public int getI2ePercent() {
		if(i2eAccountCount == 0){
			return 0;
		}else{
			double percent = 100 * this.i2eCompleteCount / this.i2eAccountCount;
			return (int)percent;
		}
	}

}
