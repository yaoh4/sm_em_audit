/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2eAuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2ePortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;


@SuppressWarnings("serial")
public class DiscrepanciesAction extends BaseAction {
	
	private static final Logger log = Logger.getLogger(Impac2PortfolioAction.class);	
	
	private PaginatedListImpl<PortfolioAccountVO> portfolioAccounts;	
	private PaginatedListImpl<PortfolioI2eAccountVO> i2ePortfolioAccounts = null;
	private List<Tab> displayColumnI2e;
	
	@Autowired
	protected Impac2PortfolioService impac2PortfolioService;
	@Autowired
	protected I2ePortfolioService i2ePortfolioService;
	@Autowired
	protected Impac2AuditService impac2AuditService;
	@Autowired
	protected I2eAuditService i2eAuditService;
	
	
	/**
     * Set up all necessary component for report. Displays the report page with default search results. 
     * @return
     */
	public String execute() throws Exception {
    	//prepare the report search
		searchVO = new AuditSearchVO();
		
		//default Search
		String forward = searchReports();
		
        return forward;
    }
	
	/**
     * Perform search based on the search criteria.
     * @return
     */
	public String searchReports() throws Exception{
		String forward = SUCCESS;
		
		changePageSize = 2000;
		portfolioAccounts = new PaginatedListImpl<PortfolioAccountVO>(request,changePageSize);	
		i2ePortfolioAccounts = new PaginatedListImpl<PortfolioI2eAccountVO>(request,changePageSize);	
		
		
		if(nciUser.getCurrentUserRole().equalsIgnoreCase("EMADMIN")) {
			searchVO.setExcludeNCIOrgs(true);
		}
		else {
			searchVO.setOrganization(nciUser.getOrgPath());
		}
		
		// Get accounts that are excluded from last audit
		EmAuditsVO emAuditsVO = adminService.retrieveCurrentOrLastAuditVO();
		Long auditId = emAuditsVO.getId();
		HashSet<String> excludedAccounts = impac2AuditService.retrieveExcludedFromAuditAccounts(auditId);
		HashSet<String> excludedI2eAccounts = i2eAuditService.retrieveExcludedFromAuditAccounts(auditId);
						
		//perform the discrepancy search
		searchVO.setCategory(ApplicationConstants.PORTFOLIO_CATEGORY_DISCREPANCY);
		portfolioAccounts = impac2PortfolioService.searchImpac2Accounts(portfolioAccounts, searchVO, true);
		portfolioAccounts.setList(filterDiscrepancyAccounts(portfolioAccounts.getList(), excludedAccounts));
		
		//Get displayColumn as per entered category.
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.COLUMNSATTRIBUTE);
		displayColumn = auditSearchActionHelper.getPortfolioDisplayColumn(colMap,searchVO.getCategory().intValue());	
		processList(displayColumn);
				
		searchVO.setCategory(ApplicationConstants.I2E_PORTFOLIO_CATEGORY_DISCREPANCY);
		i2ePortfolioAccounts = i2ePortfolioService.searchI2eAccounts(i2ePortfolioAccounts, searchVO, true);
		i2ePortfolioAccounts.setList(filterI2eDiscrepancyAccounts(i2ePortfolioAccounts.getList(), excludedI2eAccounts));
		
		colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.I2ECOLATTRIBUTE);	
		displayColumnI2e = auditSearchActionHelper.getI2ePortfolioDisplayColumn(colMap,searchVO.getCategory().intValue());
		processList(displayColumnI2e);
		
		return forward;	
	}

	/**
	 * Process display tag table header to remove #R to <br/>
	 * @param disColumn
	 */
	private void processList(List<Tab> disColumn){
		List<Tab> deleteColumn = new ArrayList<Tab>();
		for(Tab tab : disColumn){
			String colName = tab.getColumnName().replace("#R", "<br/>");
			tab.setColumnName(colName);
			if(colName.equalsIgnoreCase("Last Updated")||colName.equalsIgnoreCase("Notes")) {
				deleteColumn.add(tab);
			}
		}
		for(Tab tab : deleteColumn){
			disColumn.remove(tab);
		}
	}
	
	/**
	 * Filter out Excluded from audit accounts and Last Name diff discrepancy
	 * 
	 * @param list
	 * @param excluded
	 * @return
	 */
	private List<PortfolioI2eAccountVO> filterI2eDiscrepancyAccounts(List<PortfolioI2eAccountVO> list, HashSet<String> excluded) {
		
		List<PortfolioI2eAccountVO> accounts = new ArrayList<PortfolioI2eAccountVO>();
		for (PortfolioI2eAccountVO account : list) {
			List<String> filteredDiscrepancies = new ArrayList<String>();
			
			for(String dis : account.getAccountDiscrepancies()){
				EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,dis);
				if(disVw.getShortDescrip() != null){
					if(!disVw.getCode().equalsIgnoreCase("LNAMEDIFF")) {
						filteredDiscrepancies.add(dis);
					}
				}
			}
			account.getAccountDiscrepancies().clear();
			account.getAccountDiscrepancies().addAll(filteredDiscrepancies);
			if(!account.getAccountDiscrepancies().isEmpty() && !excluded.contains(account.getNihNetworkId()))
				accounts.add(account);
		}
		return accounts;
	}

	/**
	 * Filter out Excluded from audit accounts and Last Name diff discrepancy
	 * 
	 * @param list
	 * @param excluded
	 * @return
	 */
	private List<PortfolioAccountVO> filterDiscrepancyAccounts(List<PortfolioAccountVO> list, HashSet<String> excluded) {
		
		List<PortfolioAccountVO> accounts = new ArrayList<PortfolioAccountVO>();
		for (PortfolioAccountVO account : list) {
			List<String> filteredDiscrepancies = new ArrayList<String>();
			
			for(String dis : account.getAccountDiscrepancies()){
				EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,dis);
				if(disVw.getShortDescrip() != null){
					if(!disVw.getCode().equalsIgnoreCase("LNAMEDIFF")) {
						filteredDiscrepancies.add(dis);
					}
				}
			}
			account.getAccountDiscrepancies().clear();
			account.getAccountDiscrepancies().addAll(filteredDiscrepancies);
			String userId = (account.getImpaciiUserId() != null ? account.getImpaciiUserId() : (account.getNihNetworkId() == null ? "" : account.getNihNetworkId()));
			if(!account.getAccountDiscrepancies().isEmpty() && !excluded.contains(userId))
				accounts.add(account);
		}
		return accounts;
	}

	/**
	 * This method returns rolesColumns.
	 * @return List<Tab>
	 */
	public List<Tab> getI2ePortfolioAccountsRolesColumns(){		
		return auditSearchActionHelper.getNestedTableColumns(displayColumnI2e, ApplicationConstants.I2E_PORTFOLIO_DISCREPANCY);
	}
	
	/**
	 * This method returns roles Columns titles.
	 * @return String
	 */
	public String getI2ePortfolioAccountsRolesColumnsNames(){			
		return auditSearchActionHelper.getNestedTableColumnsNames(displayColumnI2e, ApplicationConstants.I2E_PORTFOLIO_DISCREPANCY);
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

	public List<Tab> getDisplayColumnI2e() {
		return displayColumnI2e;
	}

	public void setDisplayColumnI2e(List<Tab> displayColumnI2e) {
		this.displayColumnI2e = displayColumnI2e;
	}
	
}
