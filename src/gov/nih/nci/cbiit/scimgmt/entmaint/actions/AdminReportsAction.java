/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.helper.DisplayTagHelper;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;


@SuppressWarnings("serial")
public class AdminReportsAction extends BaseAction {
	@Autowired
	protected AdminService adminService;
	@Autowired
	protected Impac2AuditService impac2AuditService;	
	
	private String searchType;
	private PaginatedListImpl<AuditAccountVO> auditAccounts = null;
	private List<DropDownOption> categoryList = new ArrayList<DropDownOption>();
	
	public String execute() throws Exception {
    	//prepare the report search
		if(searchVO == null){
			searchVO = new AuditSearchVO();
		}
		setUpEnvironment();
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		//default Search
		String forward = searchReports();
		
        return forward;
    }
	
	public String clearAll(){
		auditSearchActionHelper.createAuditPeriodDropDownList(auditPeriodList, adminService);
		categoryList = auditSearchActionHelper.getReportCatrgories(lookupService);
		searchVO.setAuditId(Long.parseLong(auditPeriodList.get(0).getOptionKey()));
		searchVO.setCategory(Long.parseLong(categoryList.get(0).getOptionKey()));
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		showResult = false;
		return ApplicationConstants.SUCCESS;
	}
	
	public String searchReports() throws Exception{
		String forward = "";
		//perform the report search
		if(searchVO == null){
			searchVO = (AuditSearchVO) session.get(ApplicationConstants.SEARCHVO);
		}
		this.setDefaultPageSize();
		setUpEnvironment();
	    searchType = EmAppUtil.getOptionLabelByValue(searchVO.getCategory(), categoryList).toUpperCase();
		auditAccounts = new PaginatedListImpl<AuditAccountVO>(request,changePageSize);
		if(!DisplayTagHelper.isExportRequest(request, "auditAccountsId")) {
			if(ApplicationConstants.CATEGORY_ACTIVE.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchActiveAccounts(auditAccounts, searchVO, false);
			}else if(ApplicationConstants.CATEGORY_NEW.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchNewAccounts(auditAccounts, searchVO, false);
			}else if(ApplicationConstants.CATEGORY_DELETED.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchDeletedAccounts(auditAccounts, searchVO, false);
			}else if(ApplicationConstants.CATEGORY_INACTIVE.equalsIgnoreCase(searchType) == true){
				auditAccounts = impac2AuditService.searchInactiveAccounts(auditAccounts, searchVO, false);
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
			}
			auditAccounts.setList(getExportAccountVOList(auditAccounts.getList()));
			forward = ApplicationConstants.EXPORT;
		}
	    setResultColumn(searchType); 
		showResult = true;

		session.put(ApplicationConstants.SEARCHVO, searchVO);
		
		return forward;	
	}
	
	private void setUpEnvironment(){
		auditSearchActionHelper.createAuditPeriodDropDownList(auditPeriodList, adminService);
		categoryList = auditSearchActionHelper.getReportCatrgories(lookupService);
		if(searchVO.getAuditId() == null && searchVO.getCategory() == null){
			searchVO.setAuditId(Long.parseLong(auditPeriodList.get(0).getOptionKey()));
			searchVO.setCategory(Long.parseLong(categoryList.get(0).getOptionKey()));
		}
	}

	private void setResultColumn(String searchType){
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.REPORTCOLATTRIBUTE);
		displayColumn = colMap.get(searchType);
	}
	
	private List<AuditAccountVO> getExportAccountVOList(List<AuditAccountVO> auditAccounts) {
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
					
					//Blank out the name field in additional role export rows
					auditAccountVOItem.setNedLastName("");
					auditAccountVOItem.setNedFirstName("");
					
					//Prevent false discrepancies
					auditAccountVOItem.setImpaciiLastName("");
					auditAccountVOItem.setNedIc(nedIc);
					
					auditAccountVOItem.addAccountRole(accountRoles.get(index));
					exportAccountVOList.add(auditAccountVOItem);				
				}
			}
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
	
}
