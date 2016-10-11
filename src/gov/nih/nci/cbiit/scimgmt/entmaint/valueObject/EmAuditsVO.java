/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;

/**
 * @author menons2
 *
 */
public class EmAuditsVO extends EmAuditsVw {
	/**
	 * Flags to indicate the type of audit data to set
	 */
	private String impac2AuditFlag = "true";
	private String i2eAuditFlag = "false";
	
	/**
	 * Audit State
	 */
	private String auditState = ApplicationConstants.AUDIT_STATE_CODE_RESET;
	
	/**
	 * Audit comments
	 */
	private String comments;
	
	
	/**
	 * Account categories in this audit
	 */
	private String categories;


	/**
	 * Description used for drop down label
	 */
	private String description;
	
	/**
	 * @return the impac2AuditFlag
	 */
	public String getImpac2AuditFlag() {
		return impac2AuditFlag;
	}


	/**
	 * @param impac2AuditFlag the impac2AuditFlag to set
	 */
	public void setImpac2AuditFlag(String impac2AuditFlag) {
		this.impac2AuditFlag = impac2AuditFlag;
	}


	/**
	 * @return the i2eAuditFlag
	 */
	public String getI2eAuditFlag() {
		return i2eAuditFlag;
	}


	/**
	 * @param i2eAuditFlag the i2eAuditFlag to set
	 */
	public void setI2eAuditFlag(String i2eAuditFlag) {
		this.i2eAuditFlag = i2eAuditFlag;
	}


	/**
	 * @return the auditState
	 */
	public String getAuditState() {
		return auditState;
	}


	/**
	 * @param auditState the auditState to set
	 */
	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}


	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}


	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	/**
	 * Translates the account categories into checkbox selections
	 * on admin UI.
	 * @return List
	 */
	public List<String> getCategoryList() {
		
		List<String> list = new ArrayList<String>();
		
		if(ApplicationConstants.FLAG_YES.equals(getActiveCategoryEnabledFlag())) {
			list.add(ApplicationConstants.CATEGORY_ACTIVE);
		}
		if(ApplicationConstants.FLAG_YES.equals(getNewCategoryEnabledFlag())) {
			list.add(ApplicationConstants.CATEGORY_NEW);
		}
		if(ApplicationConstants.FLAG_YES.equals(getDeletedCategoryEnabledFlag())) {
			list.add(ApplicationConstants.CATEGORY_DELETED);
		}
		if(ApplicationConstants.FLAG_YES.equals(getInactiveCategoryEnabledFlag())) {
			list.add(ApplicationConstants.CATEGORY_INACTIVE);
		}
		
		return EmAppUtil.formatDisplayList(list);
	}
	
	
	public String getCategories() {
		return categories;
	}
	
	/**
	 * Translates the checkbox selections on Admin UI to account categories
	 * @param categories
	 */
	public void setCategories(String categories) {
		
		clearCategories();
		
		this.categories = categories;
		
		if(StringUtils.isNotBlank(categories)) {
			StringTokenizer tokenizer = new StringTokenizer(categories, ",");
			while(tokenizer.hasMoreTokens()) {
				String category = (String)tokenizer.nextElement();
				switch(category.toUpperCase().trim()) {
				case ApplicationConstants.CATEGORY_ACTIVE:
					this.setActiveCategoryEnabledFlag(ApplicationConstants.FLAG_YES);
					break;
				case ApplicationConstants.CATEGORY_NEW:
					this.setNewCategoryEnabledFlag(ApplicationConstants.FLAG_YES);
					break;
				case ApplicationConstants.CATEGORY_DELETED:
					this.setDeletedCategoryEnabledFlag(ApplicationConstants.FLAG_YES);
					break;
				case ApplicationConstants.CATEGORY_INACTIVE:
					this.setInactiveCategoryEnabledFlag(ApplicationConstants.FLAG_YES);
					break;
				}
			}
			
		}
	}
	
	
	private void clearCategories() {
		this.setActiveCategoryEnabledFlag(ApplicationConstants.FLAG_NO);
		this.setNewCategoryEnabledFlag(ApplicationConstants.FLAG_NO);
		this.setDeletedCategoryEnabledFlag(ApplicationConstants.FLAG_NO);
		this.setInactiveCategoryEnabledFlag(ApplicationConstants.FLAG_NO);
	}
	
}
