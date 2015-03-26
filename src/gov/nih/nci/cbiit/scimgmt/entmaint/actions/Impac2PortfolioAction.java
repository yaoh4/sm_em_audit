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
	private List<PortfolioAccountVO> discrepancyAccounts;	
	private List<DropDownOption> categoriesList = new ArrayList<DropDownOption>();	
	
	@Autowired
	protected Impac2PortfolioService impac2PortfolioService;
	
	
	/**
	 * This method is responsible for portfolio accounts search. 
	 * @return String
	 */    
	public String searchPortfolioAccounts() {
		log.debug("Begin : searchPortfolioAccounts");
		String forward = SUCCESS;  

		portfolioAccounts = new PaginatedListImpl<PortfolioAccountVO>(request);
		
		if (portfolioAccounts.getFullListSize() != 0 && searchVO.getCategory() == ApplicationConstants.PORTFOLIO_CATEGORY_DISCREPANCY) {
			discrepancyAccounts = (List<PortfolioAccountVO>) session.get(ApplicationConstants.SEARCHLIST);
		} 
		else {			
			portfolioAccounts = impac2PortfolioService.searchImpac2Accounts(portfolioAccounts, searchVO);
			
			if (searchVO.getCategory() == ApplicationConstants.PORTFOLIO_CATEGORY_DISCREPANCY) {
				
				discrepancyAccounts = portfolioAccounts.getList();
				session.put(ApplicationConstants.SEARCHLIST, discrepancyAccounts);
			} 
			else {
				session.put(ApplicationConstants.SEARCHLIST, portfolioAccounts);
			}
		}

		session.put(ApplicationConstants.SEARCHVO, searchVO);		
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.COLUMNSATTRIBUTE);
		displayColumn = auditSearchActionHelper.getPortfolioDisplayColumn(colMap,(int)searchVO.getCategory());			
		this.setFormAction("searchPortfolioAccounts");
		showResult = true;
		log.debug("End : searchPortfolioAccounts");

		if(DisplayTagHelper.isExportRequest(request, "portfolioAccountsId")) {
			portfolioAccounts.setList(getExportAccountVOList(portfolioAccounts.getList()));
			return ApplicationConstants.EXPORT;
		}		
		return forward;
	}
		
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
	 * This method is responsible for  portfolio accounts default search. 
	 * @return String
	 */    
	public String defaultPortfolioSearch() {
		log.debug("Begin : defaultPortfolioSearch");
    	String forward = SUCCESS; 
    	if(searchVO ==null){
    		searchVO = new AuditSearchVO();
    	}
    	searchVO.setCategory(ApplicationConstants.PORTFOLIO_CATEGORY_ACTIVE);
    	session.put(ApplicationConstants.SEARCHVO, searchVO);	
  		auditSearchActionHelper.createPortFolioDropDownLists(organizationList, categoriesList, lookupService, session); 
  		
    	//No default search for Super User
    	if(isSuperUser()){
    		showResult = false;
    		this.setFormAction("searchPortfolioAccounts");     		     		
    		return forward;
    	}
    	//Default search for IC coordinators.    
    	if(nciUser != null && !StringUtils.isBlank(nciUser.getOrgPath()) && StringUtils.isBlank(searchVO.getOrganization())){      				
    		searchVO.setOrganization(nciUser.getOrgPath());	
    	} 
    	forward = searchPortfolioAccounts();     	
		log.debug("End : defaultPortfolioSearch");
		return forward;
	}
	
	/**
	 * This method is used for AJAX call when the user wants to save notes.
	 * @return String
	 */
	public String saveNotes(){
		log.debug("Begin : saveNotes");
		String ipac2Id = (String)request.getParameter("ipac2Id");
		String notes = (String)request.getParameter("notes");
		PrintWriter pw = null;
		try{
			DBResult dbResult = impac2PortfolioService.saveNotes(ipac2Id,notes,new Date());
			if(dbResult.getStatus().equalsIgnoreCase(DBResult.FAILURE)){
				log.error("Exception occurred while saving portfolio notes.");
				throw new Exception("Exception occurred while saving portfolio notes.");
			}
			pw = response.getWriter();
			pw.print(decorateResponse());
		}catch(Exception e){
			log.error(e.getMessage());
			pw.print("fail");
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
	public String decorateResponse(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm a");
		String lastUpdateDate = dateFormat.format(new Date());
		String lastUpdateBy = nciUser.getLastName() +", " + nciUser.getFirstName();
		return "Submitted on "+ lastUpdateDate + " by " +lastUpdateBy;
	}
	
	/**
	 * This method is responsible for validation.
	 * @return 
	 */
	public void validateSearchPortfolioAccounts() {
		if(searchVO != null){
			if(searchVO.getDateRangeEndDate() == null){
				searchVO.setDateRangeEndDate(new Date());
			}
			if(searchVO.getDateRangeStartDate() == null && (searchVO.getCategory() == ApplicationConstants.PORTFOLIO_CATEGORY_NEW || searchVO.getCategory() == ApplicationConstants.PORTFOLIO_CATEGORY_DELETED)){
				addFieldError("searchVO.dateRangeStartDate", getText("error.daterange.startdate.empty"));
			}	
			if(searchVO.getDateRangeStartDate() != null && searchVO.getDateRangeStartDate().after(searchVO.getDateRangeEndDate())){
				addFieldError("searchVO.dateRangeStartDate", getText("error.daterange.outofrange"));
			}	
			if(searchVO.getDateRangeStartDate() != null && searchVO.getDateRangeStartDate().after(new Date())){
				addFieldError("searchVO.dateRangeStartDate", getText("error.daterange.startdate"));
			}
			if(searchVO.getDateRangeEndDate().after(new Date())){
				addFieldError("searchVO.dateRangeEndDate", getText("error.daterange.enddate"));
			}
			session.put(ApplicationConstants.SEARCHVO, searchVO);		
		}
	}
	
	/**
	 * This method is responsible for clearing search accounts screen.
	 * @return String
	 */
	public String clearSearchPortfolioAccounts() {
		log.info("clearSearchPortfolioAccounts()");
		searchVO = null;
		session.remove(ApplicationConstants.SEARCHVO);
		return SUCCESS;
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

	public List<PortfolioAccountVO> getDiscrepancyAccounts() {
		return discrepancyAccounts;
	}

	public void setDiscrepancyAccounts(List<PortfolioAccountVO> discrepancyAccounts) {
		this.discrepancyAccounts = discrepancyAccounts;
	}
}
