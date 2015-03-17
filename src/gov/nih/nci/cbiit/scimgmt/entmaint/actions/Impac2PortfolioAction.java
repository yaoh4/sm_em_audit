package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.helper.DisplayTagHelper;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;

import java.io.PrintWriter;
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
	private List<PortfolioAccountVO> portfolioAccounts;	
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
    	if(searchVO == null){
    		searchVO = new AuditSearchVO();
    	}
    	searchVO.setDateRangeStartDate(new Date());
    	searchVO.setDateRangeEndDate(new Date());
    	
		//Default search
    	if(nciUser != null && !StringUtils.isBlank(nciUser.getOrgPath()) && StringUtils.isBlank(searchVO.getOrganization())){    		
    		searchVO.setOrganization(nciUser.getOrgPath());
    	}    	
		portfolioAccounts = impac2PortfolioService.searchImpac2Accounts(searchVO);
		session.put(ApplicationConstants.SEARCHVO, searchVO);
		
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.COLUMNSATTRIBUTE);
		displayColumn = colMap.get(ApplicationConstants.PORTFOLIOTAB);
		this.setFormAction("searchPortfolioAccounts");
		
		showResult = true;
		log.debug("End : searchPortfolioAccounts");
		
		if(DisplayTagHelper.isExportRequest(request, "portfolioAccountsId")) {
			return ApplicationConstants.EXPORT;
		}
		
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
			DBResult dbResult = impac2PortfolioService.saveNotes(ipac2Id,notes);
			if(dbResult.getStatus().equalsIgnoreCase(DBResult.FAILURE)){
				log.error("Exception occurred while saving portfolio notes.");
				throw new Exception("Exception occurred while saving portfolio notes.");
			}
			pw = response.getWriter();
			pw.print(notes);
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
	 * This method is responsible for validation.
	 * @return 
	 */
	public void validateSearchPortfolioAccounts() {
		if(searchVO == null){
    		searchVO = new AuditSearchVO();
    	}
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
		auditSearchActionHelper.createPortFolioDropDownLists(organizationList, categoriesList, lookupService);	
	}
	
	/**
	 * This method is responsible for clearing search accounts screen.
	 * @return String
	 */
	public String clearSearchPortfolioAccounts() {
		log.info("clearSearchPortfolioAccounts()");
		searchVO = null;
		session.remove(ApplicationConstants.SEARCHVO);
		auditSearchActionHelper.createPortFolioDropDownLists(organizationList, categoriesList, lookupService);	
		return SUCCESS;
	}
	
	/**
	 * @return the portfolioAccounts
	 */
	public List<PortfolioAccountVO> getPortfolioAccounts() {
		return portfolioAccounts;
	}

	/**
	 * @param portfolioAccounts the portfolioAccounts to set
	 */
	public void setPortfolioAccounts(List<PortfolioAccountVO> portfolioAccounts) {
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
}
