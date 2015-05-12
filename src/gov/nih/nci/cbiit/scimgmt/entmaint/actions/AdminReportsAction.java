/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
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
	
	private PaginatedListImpl<AuditAccountVO> auditAccounts = null;
	private List<DropDownOption> categoryList = new ArrayList<DropDownOption>();
	
	public String execute() throws Exception {
    	//prepare the report search
		if(searchVO == null){
			searchVO = new AuditSearchVO();
		}
		setUpEnvironment();
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		
        return ApplicationConstants.SUCCESS;
    }
	
	public String searchReports() throws Exception{
		//perform the report search
		if(searchVO == null){
			searchVO = (AuditSearchVO) session.get(ApplicationConstants.SEARCHVO);
		}
		this.setDefaultPageSize();
		Long categoryId = searchVO.getCategory();
		
		auditAccounts = new PaginatedListImpl<AuditAccountVO>(request,changePageSize);
		auditAccounts = impac2AuditService.searchActiveAccounts(auditAccounts, searchVO, false);
	    for(AuditAccountVO account : auditAccounts.getList()){
	    	System.out.println("====" + account.getImpaciiUserId());
	    }
	    setResultColumn(categoryId); 
		showResult = true;
		setUpEnvironment();
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		
		return ApplicationConstants.SUCCESS;	
	}
	
	private void setUpEnvironment(){
		auditSearchActionHelper.createAuditPeriodDropDownList(auditPeriodList, adminService);
		categoryList = auditSearchActionHelper.getReportCatrgories(lookupService);
		if(searchVO.getAuditId() == null && searchVO.getCategory() == null){
			searchVO.setAuditId(Long.parseLong(auditPeriodList.get(0).getOptionKey()));
			searchVO.setCategory(Long.parseLong(categoryList.get(0).getOptionKey()));
		}
	}

	private void setResultColumn(Long categoryId){
	    String searchType = EmAppUtil.getOptionLabelByValue(categoryId, categoryList);
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.REPORTCOLATTRIBUTE);
		displayColumn = colMap.get(searchType);
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
	
}
