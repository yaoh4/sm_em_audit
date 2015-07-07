package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.helper.DisplayTagHelper;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.I2eActiveUserRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2ePortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
public class I2ePortfolioAction extends BaseAction {
	private static final Logger log = Logger.getLogger(I2ePortfolioAction.class);	
	private List<DropDownOption> categoriesList = new ArrayList<DropDownOption>();	
	private PaginatedListImpl<PortfolioI2eAccountVO> i2ePortfolioAccounts = null;
	private String title = null;
	
	@Autowired
	protected I2ePortfolioService i2ePortfolioService;
	
	public String execute() {
		String forward = SUCCESS;
		initialComponent();
	  	if(isSuperUser()){
    		showResult = false;
    		this.setFormAction("searchI2ePortfolioAccounts");     		     		
    		return forward;
    	}
	  	
	  	forward = searchI2ePortfolioAccounts();
	  	
        return forward;
	}
	
	@SuppressWarnings("unchecked")
	public String searchI2ePortfolioAccounts() {
		String forward = SUCCESS;
		if(searchVO == null){
			searchVO = (AuditSearchVO) session.get(ApplicationConstants.I2E_PORTFOLIO_SEARCHVO);
		}
		setDefaultPageSize();
		i2ePortfolioAccounts = new PaginatedListImpl<PortfolioI2eAccountVO>(request,changePageSize);
		
		
		//If export is requested then retrieve all accounts else retrieve only limited accounts as per pagination.
		if(DisplayTagHelper.isExportRequest(request, "i2ePortfolioAccountsId")) {
			i2ePortfolioAccounts = i2ePortfolioService.searchI2eAccounts(i2ePortfolioAccounts, searchVO, true);
			i2ePortfolioAccounts.setList(getExportAccountVOList(i2ePortfolioAccounts.getList()));
			forward =  ApplicationConstants.EXPORT;
		}
		else {
			i2ePortfolioAccounts = i2ePortfolioService.searchI2eAccounts(i2ePortfolioAccounts, searchVO, false);	
		}
		
		//Put searchVO in session.
		session.put(ApplicationConstants.I2E_PORTFOLIO_SEARCHVO, searchVO);	
				
		//Get displayColumn as per entered category.
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.I2ECOLATTRIBUTE);
		
		displayColumn = auditSearchActionHelper.getI2ePortfolioDisplayColumn(colMap,searchVO.getCategory().intValue());	
		
		//Set form action.
		this.setFormAction("searchI2ePortfolioAccounts");
		showResult = true;
		setUpEnvironmentAfterSearch();
		
        return forward;
	}
	
	
	public String clearSearchPortfolioAccounts() {
		log.info("clearSearchPortfolioAccounts()");
		session.remove(ApplicationConstants.I2E_PORTFOLIO_SEARCHVO);
		//Setting default searchVO
		initialComponent();				
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	private void setUpEnvironmentAfterSearch(){
		organizationList = (List<DropDownOption>)session.get(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		if(organizationList == null){
			auditSearchActionHelper.createI2EPortFolioDropDownLists(organizationList, categoriesList, lookupService, session);
		}
		session.put(ApplicationConstants.CURRENTPAGE, searchVO.getCategory());
	}
	
	/**
	 * This method sets up default values of searchVO for default search. 
	 * @return String
	 */ 
	private void initialComponent(){
		//Set default values
		searchVO = new AuditSearchVO();
		//searchVO.setCategory(ApplicationConstants.PORTFOLIO_CATEGORY_ACTIVE);
		if(isSuperUser()){  
			searchVO.setOrganization(ApplicationConstants.NCI_DOC_ALL);	    		
    	} 
		else if(nciUser != null && !StringUtils.isBlank(nciUser.getOrgPath())){      				
    		searchVO.setOrganization(nciUser.getOrgPath());				
		}	
		
    	session.put(ApplicationConstants.I2E_PORTFOLIO_SEARCHVO, searchVO);    	
  		auditSearchActionHelper.createI2EPortFolioDropDownLists(organizationList, categoriesList, lookupService, session);
  		if(categoriesList != null && categoriesList.get(0) != null){
  			searchVO.setCategory(new Long(categoriesList.get(0).getOptionKey()));
  			session.put(ApplicationConstants.CURRENTPAGE, searchVO.getCategory());
  		}
 		auditSearchActionHelper.setUpChangePageSizeDropDownList( getPropertyValue(ApplicationConstants.PAGE_SIZE_LIST),session);
	}
	
	/**
	 * This method sets defaultPageSize
	 * 
	 * @return the default page size.
	 */
	public void setDefaultPageSize(){
		if(changePageSize == 0){
			changePageSize = Integer.parseInt(getPropertyValue(ApplicationConstants.DEFAULT_PAGE_SIZE));
		}
	}

	/**
	 * @return the i2ePortfolioAccounts
	 */
	public PaginatedListImpl<PortfolioI2eAccountVO> getI2ePortfolioAccounts() {
		return i2ePortfolioAccounts;
	}

	/**
	 * @param i2ePortfolioAccounts the i2ePortfolioAccounts to set
	 */
	public void setI2ePortfolioAccounts(PaginatedListImpl<PortfolioI2eAccountVO> i2ePortfolioAccounts) {
		this.i2ePortfolioAccounts = i2ePortfolioAccounts;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		String title = "";
		if((long)session.get(ApplicationConstants.CURRENTPAGE) == ApplicationConstants.I2E_PORTFOLIO_CATEGORY_ACCOUNT){
			title = "Active Accounts";
		}else if((long)session.get(ApplicationConstants.CURRENTPAGE) == ApplicationConstants.I2E_PORTFOLIO_CATEGORY_DISCREPANCY){
			title = "All Active Accounts";
		}
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * This method returns Export List for portfolio accounts. 
	 * @return List
	 */  
	private List<PortfolioI2eAccountVO> getExportAccountVOList(List<PortfolioI2eAccountVO> auditAccounts) {
		List<PortfolioI2eAccountVO> exportAccountVOList = new ArrayList<PortfolioI2eAccountVO>();
		
		for(PortfolioI2eAccountVO portfolioI2eAccountVO: auditAccounts) {
			exportAccountVOList.add(portfolioI2eAccountVO);
			List<I2eActiveUserRolesVw> accountRoles = portfolioI2eAccountVO.getAccountRoles();
			if(!accountRoles.isEmpty() && accountRoles.size() > 0) {
				//Exclude the first role, because it is already present in auditAccountVO
				for(int index = 1; index < accountRoles.size(); index++) {
					//For the remaining ones, create a new AuditAccountVO, and 
					//add the role to it, so that each role shows up in a separate
					//row in excel.
					PortfolioI2eAccountVO portfolioAccountVOItem = new PortfolioI2eAccountVO();
					
					portfolioAccountVOItem.addAccountRole(accountRoles.get(index));
					exportAccountVOList.add(portfolioAccountVOItem);				
				}
			}
		}		
		return exportAccountVOList;
	}
	
	/**
	 * This method is used for AJAX call when the user wants to save notes.
	 * @return String
	 */
	public String saveNotes(){
		log.debug("Begin : saveNotes");
		String i2eIdStr = (String)request.getParameter("i2eId");
		Long i2eId = Long.parseLong(i2eIdStr);
		String notes = (String)request.getParameter("notes");
		PrintWriter pw = null;
		try{
			i2ePortfolioService.saveNotes(i2eId,notes,new Date());
			pw = response.getWriter();
			pw.print(decorateResponse(notes));
		}catch(Exception e){
			log.error("Exception occurred while saving I2E portfolio notes.",e);
			pw.print(ApplicationConstants.STATUS_FAIL);
		}finally{
			pw.close();
		}
		log.debug("End : saveNotes");
		return null;
	}
	
	/**
	 * This method generates the repsonse to be returned to the ajax call.
	 * @return
	 */
	public String decorateResponse(String notes){
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a");
		String lastUpdateDate = dateFormat.format(new Date());
		String lastUpdateBy = nciUser.getLastName() +", " + nciUser.getFirstName();
		return "Updated on "+ lastUpdateDate + " by " +lastUpdateBy;
	}
}
