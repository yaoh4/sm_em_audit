package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.helper.DisplayTagHelper;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2eAuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
public class I2eAuditAction extends BaseAction {
	
	private static final Logger log = Logger.getLogger(Impac2AuditAction.class);
	private List<DropDownOption> actionList = new ArrayList<DropDownOption>();	
	private List<DropDownOption> actionWithoutAllList = new ArrayList<DropDownOption>();
	private String role;
	
	@Autowired
	protected I2eAuditService i2eAuditService;	
	@Autowired
	protected AdminService adminService;
	private PaginatedListImpl<AuditI2eAccountVO> activeAuditAccounts = null;
	
	/**
	 * The method is for handling the clear button action.
	 * @return
	 */
	public String clearAll() {
		String forward = SUCCESS;
		auditSearchActionHelper.createActiveDropDownList(organizationList, actionList, lookupService,this.isSuperUser());
		this.setFormAction("searchI2eAuditAccounts");
		session.put(ApplicationConstants.ACTIONLIST, actionList);
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.I2ECOLATTRIBUTE);
		displayColumn = colMap.get(ApplicationConstants.I2E_AUDIT_ACTIVE);
		actionWithoutAllList = getActionListWithAll();
		this.setRole(getRole(nciUser));
		searchVO.setAct("");
		searchVO.setUserFirstname("");
		searchVO.setUserLastname("");
		searchVO.setOrganization("");
		setUpDefaultSearch();
		setAuditPeriodList(auditSearchActionHelper.createI2eAuditPeriodDropDownList(adminService));
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		showResult = false;
		
		return forward;
	}
	
	/**
	 * Set up search active account page
	 * @return
	 */
	public String execute() {
		String forward = SUCCESS;
		
		// Retrieve and store current audit info in session again in case user navigated from 
		// Admin page and current audit is not started. (Last audit is RESET.)
        EmAuditsVO emAuditsVO = adminService.retrieveCurrentOrLastAuditVO();
        setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO); 
        
		setUpDefaultSearch(); //check if default search is needed
		//set up changePageSize dropdown and default pagesize
		auditSearchActionHelper.setUpChangePageSizeDropDownList( getPropertyValue(ApplicationConstants.PAGE_SIZE_LIST),session);
  		
		if(this.isSuperUser()){
			initialComponent(ApplicationConstants.CATEGORY_ACTIVE);
		}else{
			forward = searchActiveAccounts();
		}
		return forward;
	}
	
	/**
	 * This method is performing search for active audit account
	 * @return String
	 */
	public String searchActiveAccounts() {
		String forward = SUCCESS;
		String dashboard = request.getParameter("dashboard");
		if(StringUtils.equals(dashboard, "Y") || searchVO == null){
			searchVO = (AuditSearchVO) session.get(ApplicationConstants.SEARCHVO);
		}
		setDefaultPageSize();
		activeAuditAccounts = new PaginatedListImpl<AuditI2eAccountVO>(request,changePageSize);
		if(!DisplayTagHelper.isExportRequest(request, "i2eAuditAccountsId")) {
			activeAuditAccounts = i2eAuditService.searchActiveAccounts(activeAuditAccounts, searchVO, false);
		} else {
			activeAuditAccounts = i2eAuditService.searchActiveAccounts(activeAuditAccounts, searchVO, true);
			activeAuditAccounts.setList(getExportAccountVOList(activeAuditAccounts.getList()));
			forward = ApplicationConstants.EXPORT;
		}
		setForDashboard();
		setUpEnvironmentAfterSearch();
        return forward;
	}
	
	/**
	 * This method is for setting up the default search
	 */
	private void setUpDefaultSearch(){
		//Default search
		if(searchVO == null){
			searchVO = new AuditSearchVO();
		}
    	if(nciUser != null && !StringUtils.isBlank(nciUser.getOrgPath()) && StringUtils.isBlank(searchVO.getOrganization())){    	
    		
    		boolean superUser = this.isSuperUser();
    		if(superUser){
    			searchVO.setOrganization("All");
    		}else{
    			searchVO.setOrganization(nciUser.getOrgPath());
    		}
    	}
    	
    	EmAuditsVO emAuditsVO = (EmAuditsVO)getAttributeFromSession(ApplicationConstants.CURRENT_AUDIT);
    	searchVO.setAuditId(emAuditsVO.getId());
   }
	
	/**
	 * This method is shared method for all tabs to initial components after search
	 * @param pageName
	 */
	private void setUpEnvironmentAfterSearch(){
		session.put(ApplicationConstants.SEARCHVO, searchVO);
	
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.I2ECOLATTRIBUTE);
		displayColumn = colMap.get(ApplicationConstants.I2E_AUDIT_ACTIVE);
		this.processList(displayColumn);
		
		
		auditSearchActionHelper.createActiveDropDownList(organizationList, actionList, lookupService, this.isSuperUser());
		this.setFormAction("searchI2eAuditAccounts");
		
		setAuditPeriodList(auditSearchActionHelper.createI2eAuditPeriodDropDownList(adminService));
		
		session.put(ApplicationConstants.ACTIONLIST, actionList);
		showResult = true;
		actionWithoutAllList = getActionListWithAll();
		this.setRole(getRole(nciUser));
		
	}
	
	private void processList(List<Tab> disColumn){
		for(Tab tab : disColumn){
			String colName = tab.getColumnName().replace("#R", "<br/>");
			tab.setColumnName(colName);
		}
	}
	
	/**
	 * This method is method for initial components of loading search page
	 * @param pageName
	 */
	private void initialComponent(String pageName){		
		setAuditPeriodList(auditSearchActionHelper.createI2eAuditPeriodDropDownList(adminService));
		searchVO.setAuditId(Long.parseLong(auditPeriodList.get(0).getOptionKey()));
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		session.put(ApplicationConstants.CURRENTPAGE, pageName);
		auditSearchActionHelper.createActiveDropDownList(organizationList, actionList, lookupService,this.isSuperUser());
		this.setFormAction("searchI2eAuditAccounts");
		session.put(ApplicationConstants.ACTIONLIST, actionList);
		showResult = false;
		this.setRole(getRole(nciUser));
	}
	
	/**
	 * This method returns Export List for audit accounts. 
	 * @return List
	 */  
	private List<AuditI2eAccountVO> getExportAccountVOList(List<AuditI2eAccountVO> auditAccounts) {
		List<AuditI2eAccountVO> exportAccountVOList = new ArrayList<AuditI2eAccountVO>();
		
		for(AuditI2eAccountVO auditI2eAccountVO: auditAccounts) {
			exportAccountVOList.add(auditI2eAccountVO);
			List<EmI2eAuditAccountRolesVw> accountRoles = auditI2eAccountVO.getAccountRoles();
			if(!accountRoles.isEmpty() && accountRoles.size() > 0) {
				//Exclude the first role, because it is already present in auditAccountVO
				for(int index = 1; index < accountRoles.size(); index++) {
					//For the remaining ones, create a new AuditAccountVO, and 
					//add the role to it, so that each role shows up in a separate
					//row in excel.
					AuditI2eAccountVO auditAccountVOItem = new AuditI2eAccountVO();
					
					auditAccountVOItem.addAccountRole(accountRoles.get(index));
					exportAccountVOList.add(auditAccountVOItem);				
				}
			}
		}		
		return exportAccountVOList;
	}
	
	private void setForDashboard(){
		String dashboard = request.getParameter("dashboard");
		if(dashboard == null || dashboard.equalsIgnoreCase("Y")){
			session.put(ApplicationConstants.USEDASHBOARD, dashboard);
		}else{
			session.put(ApplicationConstants.USEDASHBOARD, "");
		}
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
	public PaginatedListImpl<AuditI2eAccountVO> getActiveAuditAccounts() {
		return activeAuditAccounts;
	}

	/**
	 * @param activeduitAccounts the activeduitAccounts to set
	 */
	public void setActiveAuditAccounts(PaginatedListImpl<AuditI2eAccountVO> activeAuditAccounts) {
		this.activeAuditAccounts = activeAuditAccounts;
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
					ApplicationConstants.NEW_ACTION_ALL.equalsIgnoreCase(opt.getOptionKey()) || 
					ApplicationConstants.ACTIVE_ACTION_NOACTION.equalsIgnoreCase(opt.getOptionKey()) ||
					ApplicationConstants.NEW_ACTION_NOACTION.equalsIgnoreCase(opt.getOptionKey()) || 
					ApplicationConstants.DELETED_ACTION_NOACTION.equalsIgnoreCase(opt.getOptionKey()) ||
					ApplicationConstants.INACTIVE_ACTION_NOACTION.equalsIgnoreCase(opt.getOptionKey())){
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
	
}
