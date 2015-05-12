/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;


@SuppressWarnings("serial")
public class AdminReportsAction extends BaseAction {
	@Autowired
	protected AdminService adminService;

	
	List<DropDownOption> categoryList = new ArrayList<DropDownOption>();
	
	public String execute() throws Exception {
    	//prepare the report search
		if(searchVO == null){
			searchVO = new AuditSearchVO();
		}
		setUpEnvironment();
		
		
        return ApplicationConstants.SUCCESS;
    }
	
	public String searchReports() throws Exception{
		//perform the report search
		if(searchVO == null){
			searchVO = (AuditSearchVO) session.get(ApplicationConstants.SEARCHVO);
		}
		return ApplicationConstants.SUCCESS;	
	}
	
	private void setUpEnvironment(){
		auditSearchActionHelper.createAuditPeriodDropDownList(auditPeriodList, adminService);
		categoryList = auditSearchActionHelper.getReportCatrgories(lookupService);
		if(searchVO.getAuditId() == null && searchVO.getCategory() == null){
			searchVO.setAuditId(Long.parseLong(auditPeriodList.get(0).getOptionKey()));
			searchVO.setCategory(Long.parseLong(categoryList.get(0).getOptionKey()));
		}
		session.put(ApplicationConstants.SEARCHVO, searchVO);
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
