package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.helper.DisplayTagHelper;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This action class is for handling search function including getting input, set up search page and perform searches.
 * 
 * @author zhoujim
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class Impac2AuditAction extends BaseAction {
	private static final Logger log = Logger.getLogger(Impac2AuditAction.class);
	private List<DropDownOption> actionList = new ArrayList<DropDownOption>();	
	private List<DropDownOption> actionWithoutAllList = new ArrayList<DropDownOption>();
	private String category;
	private String role;
	
	@Autowired
	protected Impac2AuditService impac2AuditService;	
	private String type = "active";
	private List<AuditAccountVO> activeAuditAccounts = new ArrayList<AuditAccountVO>();
	
	/**
	 * this method is performing search for deleted audit account
	 * @return String
	 */
	public String searchDeletedAccounts() {
		String forward = SUCCESS;

		setUpDefaultSearch(); //check if default search is needed
		activeAuditAccounts = impac2AuditService.searchDeletedAccounts(searchVO);
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		auditSearchActionHelper.createDeletedDropDownList(organizationList, actionList, lookupService);
		session.put(ApplicationConstants.ACTIONLIST, actionList);
		showResult = true;
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.COLUMNSATTRIBUTE);
		displayColumn = colMap.get(ApplicationConstants.CATEGORY_DELETED);
		this.setFormAction("searchDeletedAuditAccounts");
		this.setTableAction("searchDeletedAuditAccounts");
		actionWithoutAllList = getActionListWithAll();
		this.setCategory(ApplicationConstants.CATEGORY_DELETED);		
		this.setRole(getRole(nciUser));

		if(DisplayTagHelper.isExportRequest(request, "auditAccountsId")) {
			activeAuditAccounts = getExportAccountVOList(activeAuditAccounts);
			return ApplicationConstants.EXPORT;
		}
		
        return forward;
	}
	
	/**
	 * This method is pagination and sorting of display table. 
	 * @return String
	 */
	public String getActiveAccounts() {
		String forward = SUCCESS;
		activeAuditAccounts = (List<AuditAccountVO>) session.get(ApplicationConstants.SEARCHLIST);
		showResult = true;
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.COLUMNSATTRIBUTE);
		displayColumn = colMap.get(ApplicationConstants.CATEGORY_ACTIVE);
		this.setFormAction("searchActiveAuditAccounts");
		this.setTableAction("getActiveAuditAccounts");
		actionWithoutAllList = getActionListWithAll();
		this.setCategory(ApplicationConstants.CATEGORY_ACTIVE);
		this.setRole(getRole(nciUser));
		if(DisplayTagHelper.isExportRequest(request, "auditAccountsId")) {
			activeAuditAccounts = getExportAccountVOList(activeAuditAccounts);
			return ApplicationConstants.EXPORT;
		}

        return forward;
	}
	
	/**
	 * This method is performing search for active audit account
	 * @return String
	 */
	public String searchActiveAccounts() {
		String forward = SUCCESS;
		setUpDefaultSearch(); //check if default search is needed
		activeAuditAccounts = impac2AuditService.searchActiveAccounts(searchVO);
		session.put(ApplicationConstants.SEARCHLIST, activeAuditAccounts);
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		auditSearchActionHelper.createActiveDropDownList(organizationList, actionList, lookupService);
		session.put(ApplicationConstants.ACTIONLIST, actionList);
		showResult = true;
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.COLUMNSATTRIBUTE);
		displayColumn = colMap.get(ApplicationConstants.CATEGORY_ACTIVE);
		this.setFormAction("searchActiveAuditAccounts");
		this.setTableAction("getActiveAuditAccounts");
		actionWithoutAllList = getActionListWithAll();
		this.setCategory(ApplicationConstants.CATEGORY_ACTIVE);
		this.setRole(getRole(nciUser));
		if(DisplayTagHelper.isExportRequest(request, "auditAccountsId")) {
			activeAuditAccounts = getExportAccountVOList(activeAuditAccounts);
			return ApplicationConstants.EXPORT;
		}

        return forward;
	}
	
	/**
	 * This method is performing search for new audit account
	 * @return String
	 */
	public String searchNewAccounts() {
		String forward = SUCCESS;
		setUpDefaultSearch(); //check if default search is needed
		activeAuditAccounts = impac2AuditService.searchNewAccounts(searchVO);
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		auditSearchActionHelper.createNewDropDownList(organizationList, actionList, lookupService);
		session.put(ApplicationConstants.ACTIONLIST, actionList);
		showResult = true;
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.COLUMNSATTRIBUTE);
		displayColumn = colMap.get(ApplicationConstants.CATEGORY_NEW);
		this.setFormAction("searchNewAuditAccounts");
		this.setTableAction("searchNewAuditAccounts");
		actionWithoutAllList = getActionListWithAll();
		this.setCategory(ApplicationConstants.CATEGORY_NEW);
		this.setRole(getRole(nciUser));
		
		if(DisplayTagHelper.isExportRequest(request, "auditAccountsId")) {
			activeAuditAccounts = getExportAccountVOList(activeAuditAccounts);
			return ApplicationConstants.EXPORT;
		}

        return forward;
	}
	
	
	/**
	 * Search for accounts that have been inactive for greater than
	 * 130 days
	 * 
	 * @return String
	 */
	public String searchInactiveAccounts() {
		String forward = SUCCESS;
		setUpDefaultSearch(); //check if default search is needed
		activeAuditAccounts = impac2AuditService.searchInactiveAccounts(searchVO);
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		auditSearchActionHelper.createInactiveDropDownList(organizationList, actionList, lookupService);
		session.put(ApplicationConstants.ACTIONLIST, actionList);
		showResult = true;
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.COLUMNSATTRIBUTE);
		displayColumn = colMap.get(ApplicationConstants.CATEGORY_INACTIVE);
		this.setFormAction("searchInactiveAuditAccounts");
		this.setTableAction("searchInactiveAuditAccounts");
		actionWithoutAllList = getActionListWithAll();
		this.setCategory(ApplicationConstants.CATEGORY_INACTIVE);
		this.setRole(getRole(nciUser));

		if(DisplayTagHelper.isExportRequest(request, "auditAccountsId")) {
			activeAuditAccounts = getExportAccountVOList(activeAuditAccounts);
			return ApplicationConstants.EXPORT;
		}

		return forward;
	}
	
	
	private List<AuditAccountVO> getExportAccountVOList(List<AuditAccountVO> auditAccounts) {
		List<AuditAccountVO> exportAccountVOList = new ArrayList<AuditAccountVO>();
		
		for(AuditAccountVO auditAccountVO: auditAccounts) {
			exportAccountVOList.add(auditAccountVO);
			List<EmAuditAccountRolesVw> accountRoles = auditAccountVO.getAccountRoles();
			if(!accountRoles.isEmpty() && accountRoles.size() > 0) {
				//Exclude the first role, because it is already present in auditAccountVO
				for(int index = 1; index < accountRoles.size(); index++) {
					//For the remaining ones, create a new AuditAccountVO, and 
					//add the role to it, so that each role shows up in a separate
					//row in excel.
					AuditAccountVO auditAccountVOItem = new AuditAccountVO();
					auditAccountVOItem.setNedLastName("");
					auditAccountVOItem.setNedFirstName("");
					auditAccountVOItem.addAccountRole(accountRoles.get(index));
					exportAccountVOList.add(auditAccountVOItem);				
				}
			}
		}
		
		return exportAccountVOList;
	}
	
	/**
	 * This method is retrieving Action List
	 * @return List<DropDownOption>
	 */
	public List<DropDownOption> getActionList() {
		return actionList;
	}

	/**
	 * This method is setting action list
	 * @param actionList
	 */
	public void setActionList(List<DropDownOption> actionList) {
		this.actionList = actionList;
	}	
	
	/**
	 * @return the activeduitAccounts
	 */
	public List<AuditAccountVO> getActiveAuditAccounts() {
		return activeAuditAccounts;
	}

	/**
	 * @param activeduitAccounts the activeduitAccounts to set
	 */
	public void setActiveAuditAccounts(List<AuditAccountVO> activeAuditAccounts) {
		this.activeAuditAccounts = activeAuditAccounts;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the actionWithoutAllList
	 */
	public List<DropDownOption> getActionWithoutAllList() {
		return actionWithoutAllList;
	}

	/**
	 * @param actionWithoutAllList the actionWithoutAllList to set
	 */
	public void setActionWithoutAllList(List<DropDownOption> actionWithoutAllList) {
		this.actionWithoutAllList = actionWithoutAllList;
	}
	
	private List<DropDownOption> getActionListWithAll(){
		List<DropDownOption> tempList = new ArrayList<DropDownOption>();
		for(DropDownOption opt : actionList){
			if(ApplicationConstants.ACTIVE_ACTION_ALL.equalsIgnoreCase(opt.getOptionKey()) || 
					ApplicationConstants.DELETED_ACTION_ALL.equalsIgnoreCase(opt.getOptionKey()) || 
					ApplicationConstants.INACTIVE_ACTION_ALL.equalsIgnoreCase(opt.getOptionKey()) || 
					ApplicationConstants.NEW_ACTION_ALL.equalsIgnoreCase(opt.getOptionKey())){
				continue;
			}else{
				tempList.add(opt);
			}
		}
		return tempList;
	}

	private String getRole(NciUser nciUser){
		return nciUser.getCurrentUserRole();
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	private void setUpDefaultSearch(){
		//Default search
		if(searchVO == null){
			searchVO = new AuditSearchVO();
		}
    	if(nciUser != null && !StringUtils.isBlank(nciUser.getOrgPath()) && StringUtils.isBlank(searchVO.getOrganization())){    		
    		searchVO.setOrganization(nciUser.getOrgPath());
    	}
	}
}
