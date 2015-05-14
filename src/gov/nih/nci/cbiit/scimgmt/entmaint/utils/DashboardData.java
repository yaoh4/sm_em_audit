package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;

public class DashboardData {
	
	private String orgName;
	private int activeAccountCount;
	private int newAccountCount;
	private int deletedAccountCount;
	private int inactiveAccountCount;
	private int activeCompleteCount;
	private int newCompleteCount;
	private int deletedCompleteCount;
	private int inactiveCompleteCount;

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
		return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_ACTIVE + "\",\"" + key + "\");'>" + this.activeCompleteCount + "/" + this.activeAccountCount + " </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='percent'>(" + this.getActivePercent() + "%)</span>";
	}

	public String getActiveAccountDataStr() {
		return this.activeCompleteCount + "/" + this.activeAccountCount + " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='percent'>(" + this.getActivePercent() + "%)</span>";
	}
	
	/**
	 * @return the newAccountDataStr
	 */
	public String getNewAccountDataStr(String key) {
		return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_NEW + "\",\"" + key + "\");'>" + this.newCompleteCount + "/" + this.newAccountCount + "</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='percent'>(" + this.getNewPercent() + "%)</span>";
	}
	
	public String getNewAccountDataStr() {
		return this.newCompleteCount + "/" + this.newAccountCount + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='percent'>(" + this.getNewPercent() + "%)</span>";
	}
	/**
	 * @return the deletedAccountDataStr
	 */
	public String getDeletedAccountDataStr(String key) {
		return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_DELETED + "\",\"" + key + "\");'>" + this.deletedCompleteCount + "/" + this.deletedAccountCount + "</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='percent'>(" + this.getDeletedPercent() + "%)</span>";
	}
	public String getDeletedAccountDataStr() {
		return this.deletedCompleteCount + "/" + this.deletedAccountCount + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='percent'>(" + this.getDeletedPercent() + "%)</span>";
	}
	/**
	 * @return the inactiveAccountDataStr
	 */
	public String getInactiveAccountDataStr(String key) {
		return "<a href='javascript:searchAuditByCategory(\"" + ApplicationConstants.CATEGORY_INACTIVE + "\",\"" + key + "\");'>" + this.inactiveCompleteCount + "/" + this.inactiveAccountCount + "</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='percent'>(" + this.getInactivePercent() + "%)</span>";
	}
	public String getInactiveAccountDataStr() {
		return this.inactiveCompleteCount + "/" + this.inactiveAccountCount + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='percent'>(" + this.getInactivePercent() + "%)</span>";
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
}
