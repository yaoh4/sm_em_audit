/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.I2eActiveUserRolesVw;
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
	
	private PaginatedListImpl<PortfolioAccountVO> portfolioInactiveAccounts;
	
	protected List<Tab> displayInactiveColumn;
	
	private String beginDt;
	private String endDt;
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	Calendar cal = Calendar.getInstance();

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
		
		forward = searchInactiveAccounts();
		
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
		HashSet<String> excludedAccounts = null;
		HashSet<String> excludedI2eAccounts = null;
		if(emAuditsVO != null) {
			Long auditId = emAuditsVO.getId();
			excludedAccounts = impac2AuditService.retrieveExcludedFromAuditAccounts(auditId);
			excludedI2eAccounts = i2eAuditService.retrieveExcludedFromAuditAccounts(auditId);
		} else {
			excludedAccounts = new HashSet<String>();
			excludedI2eAccounts = new HashSet<String>();
		}
						
		//perform the IMPAC II discrepancy search
		searchVO.setCategory(ApplicationConstants.PORTFOLIO_CATEGORY_DISCREPANCY);
		portfolioAccounts = impac2PortfolioService.searchImpac2Accounts(portfolioAccounts, searchVO, true);
		portfolioAccounts.setList(filterDiscrepancyAccounts(portfolioAccounts.getList(), excludedAccounts));
		
		//Get displayColumn as per entered category.
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.DISCREPANCYCOLATTRIBUTE);
		displayColumn = auditSearchActionHelper.getPortfolioDisplayColumn(colMap,searchVO.getCategory().intValue());	
		processList(displayColumn);
				
		//perform I2E discrepancy search
		searchVO.setCategory(ApplicationConstants.I2E_PORTFOLIO_CATEGORY_DISCREPANCY);
		i2ePortfolioAccounts = i2ePortfolioService.searchI2eAccounts(i2ePortfolioAccounts, searchVO, true);
		//Convert to IMPAC II and add it to the list to display consolidated table
		List<PortfolioAccountVO> i2eAccounts = filterI2eDiscrepancyAccounts(i2ePortfolioAccounts.getList(), excludedI2eAccounts);
		portfolioAccounts.getList().addAll(i2eAccounts);
		
		//Sort the list based on Name
		if (portfolioAccounts.getList().size() > 0) {
			Collections.sort(portfolioAccounts.getList(), new Comparator<PortfolioAccountVO>() {
				@Override
				public int compare(final PortfolioAccountVO object1, final PortfolioAccountVO object2) {
					return object1.getFullName().compareTo(object2.getFullName());
				}
			});
		}
		
		//Clear the columns for display if more than one rows exists.
		//Also, fetch and append the roles if "PD or GM Spec without appropriate IMPACII Roles" discrepancy exists and only I2E row is shown
		PortfolioAccountVO prev = null;
		for(PortfolioAccountVO account :portfolioAccounts.getList()) {
			boolean addImpacIIRoles = false;
			for(String dis : account.getAccountDiscrepancies()){
				if(dis.equalsIgnoreCase("I2EONLY")) {
					addImpacIIRoles = true;
					break;
				}
			}
			if (prev != null && account.getNihNetworkId() != null && StringUtils.equalsIgnoreCase(prev.getNihNetworkId(), account.getNihNetworkId())) {
				account.setFirstName(null);
				account.setLastName(null);
				account.setImpaciiLastName(null);
				account.setImpaciiFirstName(null);
				account.setNedEmailAddress(null);
				account.setNedOrgPath(null);
			} else if (addImpacIIRoles) {
				account = combineI2eAccountWithImpacIIAccount(account);
			}
			prev = account;
		}
		return forward;	
	}
	
	private PortfolioAccountVO combineI2eAccountWithImpacIIAccount(PortfolioAccountVO account) throws Exception {
		EmPortfolioVw impacIIaccount = impac2PortfolioService.getAccountbyNihNetworkId(account.getNihNetworkId());
		if(impacIIaccount != null) {
			//save current account roles since we need to append these later
			List<EmPortfolioRolesVw> i2eRoles = new ArrayList<EmPortfolioRolesVw> (account.getAccountRoles());
			account.getAccountRoles().clear();
			for(EmPortfolioRolesVw roles : impacIIaccount.getAccountRoles()) {
				account.addAccountRole(roles);
			}
			account.getAccountRoles().addAll(i2eRoles);
			String i2eCreatedBy = "<span title='" + account.getCreatedByFullName() + "'>I2E: " + account.getCreatedByUserId() + "</span>";
			i2eCreatedBy = (account.getCreatedByUserId() == null? "I2E: " + account.getCreatedByFullName() : i2eCreatedBy);
			String impac2CreatedBy = "<span title='" + impacIIaccount.getCreatedByFullName() + "'>IMPAC II: " + impacIIaccount.getCreatedByUserId() + "</span>";
			impac2CreatedBy = (impacIIaccount.getCreatedByUserId() == null? "IMPAC II: " + impacIIaccount.getCreatedByFullName() : impac2CreatedBy);
			account.setCreatedByUserId(impac2CreatedBy + "<br/>" + i2eCreatedBy);
			account.setStatusDescription(impacIIaccount.getStatusDescription());
		} else {
			account.setStatusDescription("No IMPAC II account");
		}
		return account;
	}

	/**
	 * ENTMAINT-274 - retrieves all inactive accounts.
	 * @return
	 * @throws Exception
	 */
	public String searchInactiveAccounts() throws Exception{
		String forward = SUCCESS;
		
		changePageSize = 2000;
		portfolioInactiveAccounts = new PaginatedListImpl<PortfolioAccountVO>(request,changePageSize);
		
		if(nciUser.getCurrentUserRole().equalsIgnoreCase("EMADMIN")) {
			searchVO.setExcludeNCIOrgs(true);
		}
		else {
			searchVO.setOrganization(nciUser.getOrgPath());
		}
		
		// Get accounts that are excluded from last audit
		EmAuditsVO emAuditsVO = adminService.retrieveCurrentOrLastAuditVO();
		HashSet<String> excludedAccounts = null;
		if(emAuditsVO != null) {
			Long auditId = emAuditsVO.getId();
			excludedAccounts = impac2AuditService.retrieveExcludedFromAuditAccounts(auditId);
		} else {
			excludedAccounts = new HashSet<String>();
		}
		//perform the IMPAC II discrepancy search
		searchVO.setCategory(ApplicationConstants.PORTFOLIO_CATEGORY_INACTIVE);
		portfolioInactiveAccounts = impac2PortfolioService.searchImpac2Accounts(portfolioInactiveAccounts, searchVO, true);
		
		//Get displayColumn as per entered category.
		Map<String, List<Tab>> colMap = (Map<String, List<Tab>>)servletContext.getAttribute(ApplicationConstants.DISCREPANCYCOLATTRIBUTE);
		displayInactiveColumn = auditSearchActionHelper.getPortfolioDisplayColumn(colMap,searchVO.getCategory().intValue());	
		processList(displayInactiveColumn);
		
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
	private List<PortfolioAccountVO> filterI2eDiscrepancyAccounts(List<PortfolioI2eAccountVO> list, HashSet<String> excluded) {
		
		List<PortfolioAccountVO> accounts = new ArrayList<PortfolioAccountVO>();
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
			if(!account.getAccountDiscrepancies().isEmpty() && !excluded.contains(account.getNihNetworkId())) {
				PortfolioAccountVO convertedAccount = new PortfolioAccountVO();
				convertedAccount.setFirstName(account.getFirstName());
				convertedAccount.setLastName(account.getLastName());
				convertedAccount.setImpaciiLastName(account.getI2eLastName());
				convertedAccount.setImpaciiFirstName(account.getI2eFirstName());
				convertedAccount.setNedEmailAddress(account.getNedEmailAddress());
				convertedAccount.setNihNetworkId(account.getNihNetworkId());
				convertedAccount.setNedOrgPath(account.getNedOrgPath());
				convertedAccount.setNotes("I2E");
				convertedAccount.setAccountDiscrepancies(new ArrayList<String>());
				convertedAccount.getAccountDiscrepancies().addAll(account.getAccountDiscrepancies());
				convertedAccount.setCreatedDate(account.getCreatedDate());
				convertedAccount.setCreatedByFullName(account.getLastUpdByFullName());
				convertedAccount.setAccountRoles(new ArrayList<EmPortfolioRolesVw>());
				for(I2eActiveUserRolesVw roleVw : account.getAccountRoles()) {
					if(StringUtils.isNotBlank(roleVw.getFullOrgPathAbbrev())){
						int beginIndex = roleVw.getFullOrgPathAbbrev().lastIndexOf("/");
						String lastOrg = (beginIndex > 0 ?  roleVw.getFullOrgPathAbbrev().substring(beginIndex + 1) : roleVw.getFullOrgPathAbbrev());
						String orgPath = "<span title='" + roleVw.getFullOrgPathAbbrev() + "'>" + lastOrg + "</span>";
						EmPortfolioRolesVw convertedRole = new EmPortfolioRolesVw();
						convertedRole.setOrgId(orgPath);
						convertedRole.setCreatedByFullName(roleVw.getRoleCreatedByFullName());
						convertedRole.setRoleName(roleVw.getRoleName());
						convertedRole.setCreatedDate(roleVw.getRoleCreatedDate());
						convertedRole.setImpaciiUserId("I2E");
						convertedAccount.getAccountRoles().add(convertedRole);
					}
				}
				accounts.add(convertedAccount);
			}
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
	public List<Tab> getPortfolioAccountsRolesColumns(){		
		return auditSearchActionHelper.getNestedTableColumns(displayColumn, ApplicationConstants.PORTFOLIO_DISCREPANCY);
	}
	
	/**
	 * This method returns roles Columns titles.
	 * @return String
	 */
	public String getPortfolioAccountsRolesColumnsNames(){			
		return auditSearchActionHelper.getNestedTableColumnsNames(displayColumn, ApplicationConstants.PORTFOLIO_DISCREPANCY);
	}
	
	
	/**
	 * This method returns rolesColumns.
	 * @return List<Tab>
	 */
	public List<Tab> getPortfolioInactiveAccountsRolesColumns(){		
		return auditSearchActionHelper.getNestedTableColumns(displayColumn, ApplicationConstants.PORTFOLIO_INACTIVE);
	}
	
	/**
	 * This method returns roles Columns titles.
	 * @return String
	 */
	public String getPortfolioInactiveAccountsRolesColumnsNames(){			
		return auditSearchActionHelper.getNestedTableColumnsNames(displayColumn, ApplicationConstants.PORTFOLIO_INACTIVE);
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

	public List<Tab> getDisplayInactiveColumn() {
		return displayInactiveColumn;
	}

	public void setDisplayInactiveColumn(List<Tab> displayInactiveColumn) {
		this.displayInactiveColumn = displayInactiveColumn;
	}

	public PaginatedListImpl<PortfolioAccountVO> getPortfolioInactiveAccounts() {
		return portfolioInactiveAccounts;
	}

	public void setPortfolioInactiveAccounts(PaginatedListImpl<PortfolioAccountVO> portfolioInactiveAccounts) {
		this.portfolioInactiveAccounts = portfolioInactiveAccounts;
	}
	
	public String getEndDt() {
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return dateFormat.format(cal.getTime());
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public String getBeginDt() {
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, -1);
		return dateFormat.format(cal.getTime());
	}

	public void setBeginDt(String beginDt) {
		this.beginDt = beginDt;
	}
	
}
