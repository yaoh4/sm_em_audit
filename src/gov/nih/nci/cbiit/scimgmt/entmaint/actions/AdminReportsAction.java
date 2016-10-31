/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.helper.DisplayTagHelper;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.TransferredAuditAccountsVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2eAuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.ReportService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;


@SuppressWarnings("serial")
public class AdminReportsAction extends BaseAction {
	
	static Logger logger = Logger.getLogger(AdminReportsAction.class);
	
	@Autowired
	protected AdminService adminService;
	@Autowired
	protected Impac2AuditService impac2AuditService;
	@Autowired
	protected I2eAuditService i2eAuditService;
	@Autowired
	protected ReportService reportService;
	
	private String searchType;
	private PaginatedListImpl<AuditAccountVO> auditAccounts = null;
	private PaginatedListImpl<AuditI2eAccountVO> auditI2eAccounts = null;
	private PaginatedListImpl<TransferredAuditAccountsVO> transferredAccounts = null;
	private List<DropDownOption> categoryList = new ArrayList<DropDownOption>();
	protected EmAuditsVO emAuditsVO = new EmAuditsVO();
	private String selectedAuditDescription;
	
	/**
     * Set up all necessary component for report. Displays the report page with default search results. 
     * @return
     */
	public String execute() throws Exception {
    	//prepare the report search
		if(searchVO == null){
			searchVO = new AuditSearchVO();
		}
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		//default Search
		String forward = searchReports();
		
        return forward;
    }
	
	/**
     * Clears the report search criteria to the default. 
     * @return
     */
	public String clearAll(){
		setAuditPeriodList(auditSearchActionHelper.createReportAuditPeriodDropDownList(adminService));
		Long auditId = Long.parseLong(auditPeriodList.get(0).getOptionKey());
		categoryList = auditSearchActionHelper.getReportCategories(lookupService, adminService, auditId);
		searchVO.setAuditId(auditId);
		searchVO.setCategory(Long.parseLong(categoryList.get(0).getOptionKey()));
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		showResult = false;
		return ApplicationConstants.SUCCESS;
	}
	
	/**
     * Perform search based on the search criteria.
     * @return
     */
	public String searchReports() throws Exception{
		String forward = "";
		//perform the report search
		if(searchVO == null){
			searchVO = (AuditSearchVO) session.get(ApplicationConstants.SEARCHVO);
		}
		setUpEnvironment();
		this.setDefaultPageSize();
		
	    searchType = lookupService.getAppLookupById(ApplicationConstants.APP_LOOKUP_REPORTS_CATEGORY_LIST, searchVO.getCategory()).getCode();
	    if(searchType.indexOf("INACTIVE") >=0){
	    	searchType = ApplicationConstants.CATEGORY_INACTIVE;
	    }
	    if(searchType.equalsIgnoreCase(ApplicationConstants.CATEGORY_TRANSFER)) {
	    	transferredAccounts = new PaginatedListImpl<TransferredAuditAccountsVO>(request,changePageSize);
	    }
	    else if(searchType.equalsIgnoreCase(ApplicationConstants.CATEGORY_I2E)) {
	    	auditI2eAccounts = new PaginatedListImpl<AuditI2eAccountVO>(request,changePageSize);
	    } else {
	    	auditAccounts = new PaginatedListImpl<AuditAccountVO>(request,changePageSize);
	    }
		if(!DisplayTagHelper.isExportRequest(request, "auditAccountsId")) {
			if(ApplicationConstants.CATEGORY_ACTIVE.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchActiveAccounts(auditAccounts, searchVO, false);
			}else if(ApplicationConstants.CATEGORY_NEW.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchNewAccounts(auditAccounts, searchVO, false);
			}else if(ApplicationConstants.CATEGORY_DELETED.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchDeletedAccounts(auditAccounts, searchVO, false);
			}else if(ApplicationConstants.CATEGORY_INACTIVE.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchInactiveAccounts(auditAccounts, searchVO, false);
			}else if(ApplicationConstants.CATEGORY_EXCLUDED.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchExcludedAccounts(auditAccounts, searchVO, false);
			}else if(ApplicationConstants.CATEGORY_TRANSFER.equalsIgnoreCase(searchType) == true){
				transferredAccounts = reportService.searchTransferredAccounts(transferredAccounts, searchVO, false);
			}else {
				auditI2eAccounts = i2eAuditService.searchActiveAccounts(auditI2eAccounts, searchVO, false);
			}
			forward = ApplicationConstants.SUCCESS;
		}else{
			if(ApplicationConstants.CATEGORY_ACTIVE.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchActiveAccounts(auditAccounts, searchVO, true);
			}else if(ApplicationConstants.CATEGORY_NEW.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchNewAccounts(auditAccounts, searchVO, true);
			}else if(ApplicationConstants.CATEGORY_DELETED.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchDeletedAccounts(auditAccounts, searchVO, true);
			}else if(ApplicationConstants.CATEGORY_INACTIVE.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchInactiveAccounts(auditAccounts, searchVO, true);
			}else if(ApplicationConstants.CATEGORY_EXCLUDED.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchExcludedAccounts(auditAccounts, searchVO, true);
			}else if(ApplicationConstants.CATEGORY_TRANSFER.equalsIgnoreCase(searchType) == true){
				transferredAccounts = reportService.searchTransferredAccounts(transferredAccounts, searchVO, true);
			} else {
				auditI2eAccounts = i2eAuditService.searchActiveAccounts(auditI2eAccounts, searchVO, true);
			}
			forward = ApplicationConstants.EXPORT;
			if (ApplicationConstants.CATEGORY_INACTIVE.equalsIgnoreCase(searchType) == true){
				auditAccounts.setList(getExportAccountVOList(auditAccounts.getList(), false));
			}else if (ApplicationConstants.CATEGORY_EXCLUDED.equalsIgnoreCase(searchType) == true){
				auditAccounts.setList(getExcludedExportAccountVOList(auditAccounts.getList()));
			}else if (ApplicationConstants.CATEGORY_I2E.equalsIgnoreCase(searchType) == true) {
				auditI2eAccounts.setList(getExportAccountVOList(auditI2eAccounts.getList()));
				forward = ApplicationConstants.EXPORT_I2E;
			}else if (ApplicationConstants.CATEGORY_TRANSFER.equalsIgnoreCase(searchType) == true) {
				forward = ApplicationConstants.EXPORT_TRANSFERRED;
			}else {
				auditAccounts.setList(getExportAccountVOList(auditAccounts.getList(), true));
			}
			
		}
	    setResultColumn(searchType); 
		showResult = true;

		emAuditsVO = adminService.retrieveAuditVO(searchVO.getAuditId());
		
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		
		return forward;	
	}
	
	
	/**
	 * Invoked when user selects an audit in the audit dropdown list
	 */
	public String getReportCategories() {
		String auditId = (String)request.getParameter("auditIdParam");
		if(!StringUtils.isBlank(auditId)) {	
			categoryList = auditSearchActionHelper.getReportCategories(lookupService, adminService, Long.parseLong(auditId));			
		}
		
		return SUCCESS;
	}
	
	
	private void setUpEnvironment(){
		setAuditPeriodList(auditSearchActionHelper.createReportAuditPeriodDropDownList(adminService));
		this.setFormAction("reportSearch");
		if(searchVO.getAuditId() == null && searchVO.getCategory() == null){
			Long auditId = Long.parseLong(auditPeriodList.get(0).getOptionKey());
			searchVO.setAuditId(auditId);
			categoryList = auditSearchActionHelper.getReportCategories(lookupService, adminService, auditId);
			searchVO.setCategory(Long.parseLong(categoryList.get(0).getOptionKey()));
			setSelectedAuditDescription(auditPeriodList.get(0).getOptionValue());
		} else {
			categoryList = auditSearchActionHelper.getReportCategories(lookupService, adminService, searchVO.getAuditId());
			for (DropDownOption option:getAuditPeriodList()) {
				if(StringUtils.equals(searchVO.getAuditId().toString(), option.getOptionKey()))
					setSelectedAuditDescription(option.getOptionValue());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void setResultColumn(String searchType){
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.REPORTCOLATTRIBUTE);
		displayColumn = colMap.get(searchType);
	}
	
	private List<AuditAccountVO> getExportAccountVOList(List<AuditAccountVO> auditAccounts, boolean loopRoles) {
		
		if(!loopRoles){
			return getInactiveExportAccountVOList(auditAccounts);
		}
		List<AuditAccountVO> exportAccountVOList = new ArrayList<AuditAccountVO>();
		
		for(AuditAccountVO auditAccountVO: auditAccounts) {
			Long actionId = null;
			if(auditAccountVO != null && auditAccountVO.getAccountActivity() != null && auditAccountVO.getAccountActivity().getAction() != null && auditAccountVO.getAccountActivity().getAction().getId() != null){
				actionId = auditAccountVO.getAccountActivity().getAction().getId();
			}
			if(actionId != null && (actionId == ApplicationConstants.ACTIVE_EXCLUDE_FROM_AUDIT || 
			   actionId == ApplicationConstants.NEW_EXCLUDE_FROM_AUDIT ||
			   actionId == ApplicationConstants.DELETED_EXCLUDE_FROM_AUDIT ||
			   actionId == ApplicationConstants.INACTIVE_EXCLUDE_FROM_AUDIT)){
				continue;
			}
			String nedIc = auditAccountVO.getNedIc();
			
			exportAccountVOList.add(auditAccountVO);
			List<EmAuditAccountRolesVw> accountRoles = auditAccountVO.getAccountRoles();
			
			if(!accountRoles.isEmpty() && accountRoles.size() > 0) {
				//Exclude the first role, because it is already present in auditAccountVO
				for(int index = 1; index < accountRoles.size(); index++) {
					//For the remaining ones, create a new AuditAccountVO, and 
					//add the role to it, so that each role shows up in a separate
					//row in excel.
					AuditAccountVO auditAccountVOItem = new AuditAccountVO();
					
					auditAccountVOItem.setNedIc(nedIc);
					auditAccountVOItem.setImpaciiUserId(auditAccountVO.getImpaciiUserId());
					auditAccountVOItem.setNihNetworkId(auditAccountVO.getNihNetworkId());
					auditAccountVOItem.setNedLastName(auditAccountVO.getNedLastName());
					auditAccountVOItem.setNedFirstName(auditAccountVO.getNedFirstName());
					auditAccountVOItem.setLastName(auditAccountVO.getLastName());
					auditAccountVOItem.setFirstName(auditAccountVO.getFirstName());
					auditAccountVOItem.setSecondaryOrgText(auditAccountVO.getSecondaryOrgText());
					auditAccountVOItem.addAccountRole(accountRoles.get(index));
					auditAccountVOItem.setCreatedDate(auditAccountVO.getCreatedDate());
					auditAccountVOItem.setCreatedByUserId(auditAccountVO.getCreatedByUserId());
					auditAccountVOItem.setCreatedByFullName(auditAccountVO.getCreatedByFullName());
					
					exportAccountVOList.add(auditAccountVOItem);				
				}
			}
		}
		
		return exportAccountVOList;
	}
	
	private List<AuditI2eAccountVO> getExportAccountVOList(List<AuditI2eAccountVO> auditAccounts) {
		
		List<AuditI2eAccountVO> exportAccountVOList = new ArrayList<AuditI2eAccountVO>();
		
		for(AuditI2eAccountVO auditAccountVO: auditAccounts) {
			Long actionId = null;
			if(auditAccountVO != null && auditAccountVO.getAction() != null && auditAccountVO.getAction().getId() != null){
				actionId = auditAccountVO.getAction().getId();
			}
			if(actionId != null && (actionId == ApplicationConstants.ACTIVE_EXCLUDE_FROM_AUDIT)){
				continue;
			}
			String nedIc = auditAccountVO.getNedIc();
			
			exportAccountVOList.add(auditAccountVO);
			List<EmI2eAuditAccountRolesVw> accountRoles = auditAccountVO.getAccountRoles();
			
			if(!accountRoles.isEmpty() && accountRoles.size() > 0) {
				//Exclude the first role, because it is already present in auditAccountVO
				for(int index = 1; index < accountRoles.size(); index++) {
					//For the remaining ones, create a new AuditAccountVO, and 
					//add the role to it, so that each role shows up in a separate
					//row in excel.
					AuditI2eAccountVO auditAccountVOItem = new AuditI2eAccountVO();
					
					auditAccountVOItem.setNedIc(nedIc);
					auditAccountVOItem.setNihNetworkId(auditAccountVO.getNihNetworkId());
					auditAccountVOItem.setNedLastName(auditAccountVO.getNedLastName());
					auditAccountVOItem.setNedFirstName(auditAccountVO.getNedFirstName());
					auditAccountVOItem.addAccountRole(accountRoles.get(index));
					auditAccountVOItem.setCreatedDate(auditAccountVO.getCreatedDate());
					
					exportAccountVOList.add(auditAccountVOItem);				
				}
			}
		}
		
		return exportAccountVOList;
	}

	private List<AuditAccountVO> getExcludedExportAccountVOList(List<AuditAccountVO> auditAccounts) {
		
		List<AuditAccountVO> exportAccountVOList = new ArrayList<AuditAccountVO>();
		
		for(AuditAccountVO auditAccountVO: auditAccounts) {
			String nedIc = auditAccountVO.getNedIc();
			
			exportAccountVOList.add(auditAccountVO);
			List<EmAuditAccountRolesVw> accountRoles = auditAccountVO.getAccountRoles();
			
			if(!accountRoles.isEmpty() && accountRoles.size() > 0) {
				//Exclude the first role, because it is already present in auditAccountVO
				for(int index = 1; index < accountRoles.size(); index++) {
					//For the remaining ones, create a new AuditAccountVO, and 
					//add the role to it, so that each role shows up in a separate
					//row in excel.
					AuditAccountVO auditAccountVOItem = new AuditAccountVO();
					
					auditAccountVOItem.setNedIc(nedIc);
					auditAccountVOItem.setImpaciiUserId(auditAccountVO.getImpaciiUserId());
					auditAccountVOItem.setNihNetworkId(auditAccountVO.getNihNetworkId());
					auditAccountVOItem.setNedLastName(auditAccountVO.getNedLastName());
					auditAccountVOItem.setNedFirstName(auditAccountVO.getNedFirstName());
					auditAccountVOItem.setLastName(auditAccountVO.getLastName());
					auditAccountVOItem.setFirstName(auditAccountVO.getFirstName());
					auditAccountVOItem.setSecondaryOrgText(auditAccountVO.getSecondaryOrgText());
					auditAccountVOItem.addAccountRole(accountRoles.get(index));
					auditAccountVOItem.setCreatedDate(auditAccountVO.getCreatedDate());
					auditAccountVOItem.setCreatedByUserId(auditAccountVO.getCreatedByUserId());
					auditAccountVOItem.setCreatedByFullName(auditAccountVO.getCreatedByFullName());
					
					exportAccountVOList.add(auditAccountVOItem);				
				}
			}
		}
		
		return exportAccountVOList;
	}
	
	private List<AuditAccountVO> getInactiveExportAccountVOList(List<AuditAccountVO> auditAccounts){
		List<AuditAccountVO> exportAccountVOList = new ArrayList<AuditAccountVO>();
		for(AuditAccountVO auditAccountVO: auditAccounts) {
			Long actionId = null;
			if(auditAccountVO != null && auditAccountVO.getAccountActivity() != null && auditAccountVO.getAccountActivity().getAction() != null && auditAccountVO.getAccountActivity().getAction().getId() != null){
				actionId = auditAccountVO.getAccountActivity().getAction().getId();
			}
			if(actionId != null && (actionId == ApplicationConstants.ACTIVE_EXCLUDE_FROM_AUDIT || 
			   actionId == ApplicationConstants.NEW_EXCLUDE_FROM_AUDIT ||
			   actionId == ApplicationConstants.DELETED_EXCLUDE_FROM_AUDIT ||
			   actionId == ApplicationConstants.INACTIVE_EXCLUDE_FROM_AUDIT)){
				continue;
			}
			exportAccountVOList.add(auditAccountVO);
		}
		return exportAccountVOList;
	}
	
	/**
	 * @return the categoryList
	 */
	public List<DropDownOption> getCategoryList() {
		return categoryList;
	}

	/**
	 * @param categoryList the categoryList to set
	 */
	public void setCategoryList(List<DropDownOption> categoryList) {
		this.categoryList = categoryList;
	}

	/**
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * @return the auditAccounts
	 */
	public PaginatedListImpl<AuditAccountVO> getAuditAccounts() {
		return auditAccounts;
	}

	/**
	 * @param auditAccounts the auditAccounts to set
	 */
	public void setAuditAccounts(PaginatedListImpl<AuditAccountVO> auditAccounts) {
		this.auditAccounts = auditAccounts;
	}
	
	/**
	 * @return the emAuditsVO
	 */
	public EmAuditsVO getEmAuditsVO() {
		return emAuditsVO;
}

	/**
	 * @param emAuditsVO the emAuditsVO to set
	 */
	public void setEmAuditsVO(EmAuditsVO emAuditsVO) {
		this.emAuditsVO = emAuditsVO;
	}

	public String getSelectedAuditDescription() {
		return selectedAuditDescription;
	}

	public void setSelectedAuditDescription(String selectedAuditDescription) {
		this.selectedAuditDescription = selectedAuditDescription;
	}

	public PaginatedListImpl<AuditI2eAccountVO> getAuditI2eAccounts() {
		return auditI2eAccounts;
	}

	public void setAuditI2eAccounts(PaginatedListImpl<AuditI2eAccountVO> auditI2eAccounts) {
		this.auditI2eAccounts = auditI2eAccounts;
	}
	
	/**
	 * This method returns rolesColumns.
	 * @return List<Tab>
	 */
	public List<Tab> getI2eAuditAccountsRolesColumns(){
		return auditSearchActionHelper.getNestedTableColumns(displayColumn, ApplicationConstants.I2E_REPORT);
	}
	
	/**
	 * This method returns roles Columns titles.
	 * @return String
	 */
	public String getI2eAuditAccountsRolesColumnsNames(){		
		return auditSearchActionHelper.getNestedTableColumnsNames(displayColumn, ApplicationConstants.I2E_REPORT);
	}
	
	/**
	 * @return the TransferredAccounts
	 */
	public PaginatedListImpl<TransferredAuditAccountsVO> getTransferredAccounts() {
		return transferredAccounts;
	}

	/**
	 * @param transferredAccounts the transferredAccounts to set
	 */
	public void setTransferredAccounts(PaginatedListImpl<TransferredAuditAccountsVO> transferredAccounts) {
		this.transferredAccounts = transferredAccounts;
	}
	
}
