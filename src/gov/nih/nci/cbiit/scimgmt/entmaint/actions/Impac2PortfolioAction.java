package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.helper.DisplayTagHelper;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This action class is responsible for PortFolio Analysis search and other processing.  
 * @author tembharend 
 */
@SuppressWarnings({ "unchecked", "serial" })
public class Impac2PortfolioAction extends BaseAction{
	
	private static final Logger log = Logger.getLogger(Impac2PortfolioAction.class);		
	private PaginatedListImpl<PortfolioAccountVO> portfolioAccounts;	
	private List<DropDownOption> categoriesList = new ArrayList<DropDownOption>();	
	private boolean isDefaultSearch = false;
	@Autowired
	protected Impac2PortfolioService impac2PortfolioService;
	
	/**
	 * This method is responsible for portfolio accounts search. 
	 * @return String
	 */    
	public String searchPortfolioAccounts() {
		log.debug("Begin : searchPortfolioAccounts");
		String forward = SUCCESS;
		
		//Set default page size.
		setDefaultPageSize();
		//Initialize PaginatedList.
		portfolioAccounts = new PaginatedListImpl<PortfolioAccountVO>(request,changePageSize);	
		//Get search VO from session if its null and set dates if they are null.
		setupSearchVO();
		
		//If export is requested then retrieve all accounts else retrieve only limited accounts as per pagination.
		if(DisplayTagHelper.isExportRequest(request, "portfolioAccountsId")) {
			portfolioAccounts = impac2PortfolioService.searchImpac2Accounts(portfolioAccounts, searchVO, true);
			portfolioAccounts.setList(getExportAccountVOList(portfolioAccounts.getList()));
			forward =  ApplicationConstants.EXPORT;
		}
		else {
			portfolioAccounts = impac2PortfolioService.searchImpac2Accounts(portfolioAccounts, searchVO, false);			
		}
		//Put searchVO in session.
		session.put(ApplicationConstants.PORTFOLIO_SEARCHVO, searchVO);	
		
		//Get displayColumn as per entered category.
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.COLUMNSATTRIBUTE);
		displayColumn = auditSearchActionHelper.getPortfolioDisplayColumn(colMap,searchVO.getCategory().intValue());	
		processList(displayColumn);
		//Set form action.
		this.setFormAction("searchPortfolioAccounts");
		showResult = true;
		isDefaultSearch = false;
		log.debug("End : searchPortfolioAccounts");
		
		return forward;
	}
	
	/**
	 * This method does pre processing of the searchVO before sending it to service layer. 
	 * @return 
	 */   
	public void setupSearchVO(){
		//Retrieves searchVO from session for changePageSize functionality. 
		if(searchVO == null){
			searchVO = (AuditSearchVO) session.get(ApplicationConstants.PORTFOLIO_SEARCHVO);
		}
		//If user doesn't enter Start date or End date, populate it.
		if(searchVO.getDateRangeStartDate() == null && 
				!isDefaultSearch && (searchVO.getCategory() == ApplicationConstants.PORTFOLIO_CATEGORY_DELETED ||
				searchVO.getCategory() == ApplicationConstants.PORTFOLIO_CATEGORY_NEW)){
			searchVO.setDateRangeStartDate(new Date());
		}	
		if(searchVO.getDateRangeEndDate() == null){
			searchVO.setDateRangeEndDate(new Date());
		}
	}
	
	/**
	 * This method returns Export List for portfolio accounts. 
	 * @return List
	 */  
	private List<PortfolioAccountVO> getExportAccountVOList(List<PortfolioAccountVO> auditAccounts) {
		List<PortfolioAccountVO> exportAccountVOList = new ArrayList<PortfolioAccountVO>();
		
		for(PortfolioAccountVO portfolioAccountVO: auditAccounts) {
			exportAccountVOList.add(portfolioAccountVO);
			List<EmPortfolioRolesVw> accountRoles = portfolioAccountVO.getAccountRoles();
			if(!accountRoles.isEmpty() && accountRoles.size() > 0) {
				//Exclude the first role, because it is already present in auditAccountVO
				for(int index = 1; index < accountRoles.size(); index++) {
					//For the remaining ones, create a new AuditAccountVO, and 
					//add the role to it, so that each role shows up in a separate
					//row in excel.
					PortfolioAccountVO portfolioAccountVOItem = new PortfolioAccountVO();
					
					//Blank out the name field in additional role export rows
					portfolioAccountVOItem.setNedLastName("");
					portfolioAccountVOItem.setNedFirstName("");
					
					//Prevent false discrepancies
					portfolioAccountVOItem.setImpaciiLastName("");
					portfolioAccountVOItem.setNedIc("NCI");
					
					portfolioAccountVOItem.addAccountRole(accountRoles.get(index));
					exportAccountVOList.add(portfolioAccountVOItem);				
				}
			}
		}		
		return exportAccountVOList;
	}
	
	/**
	 * This method is responsible for portfolio accounts default search. 
	 * @return String
	 */    
	public String execute() {
		log.debug("Begin : execute");
    	String forward = SUCCESS; 
    	//Set up initial values of searchVO
    	initialComponent();
    	
    	//No default search for Super User
    	if(isSuperUser()){
    		showResult = false;
    		this.setFormAction("searchPortfolioAccounts");     		     		
    		return forward;
    	}
    	//Default search for IC coordinators. 
    	isDefaultSearch=true;
    	forward = searchPortfolioAccounts();     	
		log.debug("End : execute");
		return forward;
	}
	
	public void getOptionAction(){
		PrintWriter pw = null;
		String defaultOrg = nciUser.getOrgPath();
		String category = request.getParameter("cate");
		String scriptCall = "";
		if(isSuperUser()){
			scriptCall  = "onchange=\"onOrgChange(this.value);\"";
		}
		try{
			pw = response.getWriter();
			if(Long.parseLong(category) == ApplicationConstants.PORTFOLIO_CATEGORY_DELETED){
				pw.print("<select name=\"searchVO.organization\" id=\"portfolioOrg\" class=\"form-control\" style=\"width:590px;\">");
				pw.println("<option value=\"all\">All</option>");
				pw.println("</select>");
			}else{
				//
				auditSearchActionHelper.createPortFolioDropDownLists(organizationList, categoriesList, lookupService, session); 
				pw.print("<select name=\"searchVO.organization\" id=\"portfolioOrg\" " + scriptCall + "class=\"form-control\" style=\"width:590px;\">");
				if(isSuperUser()){
					pw.print("<option value=\"all\" selected=\"selected\">All</option>");
				}else{
					pw.print("<option value=\"all\">All</option>");
				}
				for(DropDownOption ddo : organizationList){
					if(ddo.getOptionKey().equalsIgnoreCase(defaultOrg) && !isSuperUser()){
						pw.print("<option value='" + ddo.getOptionKey() +"' selected=\"selected\">" + ddo.getOptionValue() +"</option>");
					}else{
						pw.print("<option value='" + ddo.getOptionKey() +"' >" + ddo.getOptionValue() +"</option>");
					}
				}
				pw.println("</select>");
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}finally{
			pw.close();
		}
	}
	
	/**
	 * This method sets up default values of searchVO for default search. 
	 * @return String
	 */ 
	private void initialComponent(){
		//Set default values
		searchVO = new AuditSearchVO();
    	searchVO.setDateRangeEndDate(new Date());
		searchVO.setCategory(ApplicationConstants.PORTFOLIO_CATEGORY_ACTIVE);
		if(isSuperUser()){  
			searchVO.setOrganization(ApplicationConstants.NCI_DOC_ALL);	    		
    	} 
		else if(nciUser != null && !StringUtils.isBlank(nciUser.getOrgPath())){      				
    		searchVO.setOrganization(nciUser.getOrgPath());				
		}
		
    	session.put(ApplicationConstants.PORTFOLIO_SEARCHVO, searchVO);    	
  		auditSearchActionHelper.createPortFolioDropDownLists(organizationList, categoriesList, lookupService, session); 
  		auditSearchActionHelper.setUpChangePageSizeDropDownList( getPropertyValue(ApplicationConstants.PAGE_SIZE_LIST),session);
	}
	
	/**
	 * This method is used for AJAX call when the user wants to save notes.
	 * @return String
	 */
	public String saveNotes(){
		log.debug("Begin : saveNotes");
		String impac2Id = (String)request.getParameter("impac2Id");
		String notes = (String)request.getParameter("notes");
		PrintWriter pw = null;
		try{
			impac2PortfolioService.saveNotes(impac2Id,notes,new Date());
			pw = response.getWriter();
			pw.print(decorateResponse(notes));
		}catch(Exception e){
			log.error("Exception occurred while saving portfolio notes.",e);
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm a");
		String lastUpdateDate = dateFormat.format(new Date());
		String lastUpdateBy = nciUser.getLastName() +", " + nciUser.getFirstName();
		return "Updated on "+ lastUpdateDate + " by " +lastUpdateBy;
	}
	
	/**
	 * This method is responsible for validation.
	 * @return 
	 */
	public void validateSearchPortfolioAccounts() {
		if(searchVO != null){							
			if(searchVO.getDateRangeStartDate() != null && searchVO.getDateRangeEndDate() != null && searchVO.getDateRangeStartDate().after(searchVO.getDateRangeEndDate())){
				addFieldError("searchVO.dateRangeStartDate", getText("error.daterange.outofsequence"));
			}	
			if(searchVO.getDateRangeStartDate() != null && searchVO.getDateRangeStartDate().after(new Date())){
				addFieldError("searchVO.dateRangeStartDate", getText("error.daterange.startdate.future"));
			}
			if(searchVO.getDateRangeEndDate() != null && searchVO.getDateRangeEndDate().after(new Date())){
				addFieldError("searchVO.dateRangeEndDate", getText("error.daterange.enddate.future"));
			}
			session.put(ApplicationConstants.PORTFOLIO_SEARCHVO, searchVO);			
		}
	}	
	
	/**
	 * This method is responsible for clearing search accounts screen.
	 * @return String
	 */
	public String clearSearchPortfolioAccounts() {
		log.info("clearSearchPortfolioAccounts()");
		session.remove(ApplicationConstants.PORTFOLIO_SEARCHVO);
		//Setting default searchVO
		initialComponent();				
		return SUCCESS;
	}	
	
	/**
	 * Get the selected category	
	 * @return category
	 * 
	 */
	public String getSearchResultsCat(){
		String category = "";
		category = lookupService.getAppLookupById(ApplicationConstants.APP_LOOKUP_PORTFOLIO_CATEGORY_LIST, searchVO.getCategory()).getDescription();
		return category;
	}	
	
	/**
	 * Get the Last Refresh date	
	 * @return lastRefreshDate
	 * 
	 */
	public String getLastRefreshDate(){
		return new SimpleDateFormat ("MM/dd/yyyy 'at' hh:mm a").format(impac2PortfolioService.getLastRefreshDate());		
	}	
	
	/**
	 * @return the portfolioAccounts
	 */
	public PaginatedListImpl<PortfolioAccountVO> getPortfolioAccounts() {
		return portfolioAccounts;
	}

	/**
	 * @param portfolioAccounts the portfolioAccounts to set
	 */
	public void setPortfolioAccounts(PaginatedListImpl<PortfolioAccountVO> portfolioAccounts) {
		this.portfolioAccounts = portfolioAccounts;
	}
	
	/**
	 * @return the categoriesList
	 */
	public List<DropDownOption> getCategoriesList() {
		return categoriesList;
	}

	/**
	 * @param categoriesList the categoriesList to set
	 */
	public void setCategoriesList(List<DropDownOption> categoriesList) {
		this.categoriesList = categoriesList;
	}
	/**
	 * Process display tag table header to remove #R to <br/>
	 * @param disColumn
	 */
	private void processList(List<Tab> disColumn){
		for(Tab tab : disColumn){
			String colName = tab.getColumnName().replace("#R", "<br/>");
			tab.setColumnName(colName);
		}
	}

}
