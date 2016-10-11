package gov.nih.nci.cbiit.scimgmt.entmaint.helper.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;

import org.apache.commons.lang.StringUtils;
import com.opensymphony.xwork2.ActionContext;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmOrganizationVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

@SuppressWarnings("unchecked")
public class AuditSearchActionHelper {
	
	public static final String ROLE_ORG_PATH = "Role Org";
	
	static Logger logger = Logger.getLogger(AuditSearchActionHelper.class);

	public void createActiveDropDownList(List<DropDownOption> organizationList, List<DropDownOption> actionList, LookupService lookupService, boolean isSuperUser){
		
		List<AppLookupT> actList = lookupService.getList(ApplicationConstants.APP_LOOKUP_ACTIVE_ACTION_LIST);
		List<EmOrganizationVw> orglist = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		createOrgList(orglist, organizationList);
		createActionList(actList, actionList, isSuperUser);
	}
	
	public void createNewDropDownList(List<DropDownOption> organizationList, List<DropDownOption> actionList, LookupService lookupService, boolean isSuperUser){
	
		List<AppLookupT> actList = lookupService.getList(ApplicationConstants.APP_LOOKUP_NEW_ACTION_LIST);
		List<EmOrganizationVw> orglist = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		createOrgList(orglist, organizationList);
		createActionList(actList, actionList, isSuperUser);
	}
	
	public void createDeletedDropDownList(List<DropDownOption> organizationList, List<DropDownOption> actionList, LookupService lookupService, boolean isSuperUser){
		List<AppLookupT> actList = lookupService.getList(ApplicationConstants.APP_LOOKUP_DELETED_ACTION_LIST);
		//List<EmOrganizationVw> orglist = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		//createOrgList(orglist, organizationList);
		Map<String, Object> session = ActionContext.getContext().getSession();
		NciUser nciUser = (NciUser)session.get(ApplicationConstants.SESSION_USER);
		String orgPath = nciUser.getOrgPath();
		organizationList.add(new DropDownOption(orgPath, orgPath));
		organizationList.add(new DropDownOption(ApplicationConstants.ORG_PATH_NO_NED_ORG, ApplicationConstants.ORG_PATH_NO_NED_ORG));
		createActionList(actList, actionList, isSuperUser); 
	}
	
	public void createInactiveDropDownList(List<DropDownOption> organizationList, List<DropDownOption> actionList, LookupService lookupService, boolean isSuperUser){
		List<AppLookupT> actList = lookupService.getList(ApplicationConstants.APP_LOOKUP_INACTIVE_ACTION_LIST);
		List<EmOrganizationVw> orglist = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		createOrgList(orglist, organizationList);
		createActionList(actList, actionList, isSuperUser);
	}
	
	public void createActionList(List<AppLookupT> actList, List<DropDownOption> actionList, boolean isSuperUser){
		
		for(AppLookupT act : actList){
			if(!isSuperUser && (act.getId() == ApplicationConstants.ACTIVE_EXCLUDE_FROM_AUDIT || 
							    act.getId() == ApplicationConstants.NEW_EXCLUDE_FROM_AUDIT ||
							    act.getId() == ApplicationConstants.DELETED_EXCLUDE_FROM_AUDIT ||
							    act.getId() == ApplicationConstants.INACTIVE_EXCLUDE_FROM_AUDIT)){
					continue;
			}else{
				if(ApplicationConstants.ACTION_TRANSFER.equalsIgnoreCase(act.getCode())){
					act.setDescription(ApplicationConstants.SEARCH_TRANSFERED);
				}
				DropDownOption ddp1 = new DropDownOption(""+act.getId(), act.getDescription());	
				actionList.add(ddp1);
			}
		}
	}
	
	public void createOrgList(List<EmOrganizationVw> orgList, List<DropDownOption> organizationList){
		for(EmOrganizationVw org : orgList){
			DropDownOption ddp = new DropDownOption(org.getNihorgpath(), org.getNihorgpath());
			organizationList.add(ddp);
		}
	}
	
	public  List<DropDownOption> createAuditPeriodDropDownList(AdminService adminService, String category){
		List<DropDownOption> auditPeriodList = new ArrayList<DropDownOption>();
		List<EmAuditsVO> emAuditVOs = adminService.retrieveAuditVOList(category);
		for(EmAuditsVO emAuditVO : emAuditVOs){
			DropDownOption ddp = new DropDownOption(""+emAuditVO.getId(), emAuditVO.getDescription());
			auditPeriodList.add(ddp);
		}
		
		return auditPeriodList;
	}
	
	public  List<DropDownOption> createReportAuditPeriodDropDownList(AdminService adminService){
		List<DropDownOption> auditPeriodList = new ArrayList<DropDownOption>();
		List<EmAuditsVO> emAuditVOs = adminService.retrieveReportAuditVOList();
		for(EmAuditsVO emAuditVO : emAuditVOs){
			DropDownOption ddp = new DropDownOption(""+emAuditVO.getId(), emAuditVO.getDescription());
			auditPeriodList.add(ddp);
		}
		
		return auditPeriodList;
	}
	
	public  List<DropDownOption> createI2eAuditPeriodDropDownList(AdminService adminService){
		List<DropDownOption> auditPeriodList = new ArrayList<DropDownOption>();
		List<EmAuditsVO> emAuditVOs = adminService.retrieveI2eAuditVOList();
		for(EmAuditsVO emAuditVO : emAuditVOs){
			DropDownOption ddp = new DropDownOption(""+emAuditVO.getId(), emAuditVO.getDescription());
			auditPeriodList.add(ddp);
		}
		
		return auditPeriodList;
	}
	
	/**
	 * This method is responsible for preparing drop down lists for organizationList and categoriesList for portfolio accounts search.
	 * @return 
	 */
	public void  createPortFolioDropDownLists(List<DropDownOption> organizationList, List<DropDownOption> categoriesList,  LookupService lookupService, Map<String, Object> session){

		List<EmOrganizationVw> orgs = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		if(orgs != null && orgs.size() >0){
			for(EmOrganizationVw org : orgs){
				DropDownOption orgOption = new DropDownOption(org.getNihorgpath(), org.getNihorgpath());	
				organizationList.add(orgOption);	
			}
			session.put(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST, organizationList);	
		}

		List<AppLookupT> categories = (List<AppLookupT>)lookupService.getList(ApplicationConstants.APP_LOOKUP_PORTFOLIO_CATEGORY_LIST);		
		if(categories != null && categories.size() >0){
			for(AppLookupT category : categories){
				if (category.getCode().equalsIgnoreCase("INACTIVE")) {
					continue;
				}
				DropDownOption categoryOption = new DropDownOption(""+category.getId(), category.getDescription());	
				categoriesList.add(categoryOption);
			}
			session.put(ApplicationConstants.CATEGORY_DROPDOWN_LIST, categoriesList);	
		}		
	}
	
	/**
	 * This method is responsible for preparing drop down lists for organizationList and categoriesList for portfolio accounts search.
	 * @return 
	 */
	public void  createI2EPortFolioDropDownLists(List<DropDownOption> organizationList, List<DropDownOption> categoriesList,  LookupService lookupService, Map<String, Object> session){

		List<EmOrganizationVw> orgs = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		if(orgs != null && orgs.size() >0){
			for(EmOrganizationVw org : orgs){
				DropDownOption orgOption = new DropDownOption(org.getNihorgpath(), org.getNihorgpath());	
				organizationList.add(orgOption);	
			}
			session.put(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST, organizationList);	
		}

		List<AppLookupT> categories = (List<AppLookupT>)lookupService.getList(ApplicationConstants.APP_LOOKUP_I2E_PORTFOLIO_CATEGORY_LIST);		
		if(categories != null && categories.size() >0){
			for(AppLookupT category : categories){
				DropDownOption categoryOption = new DropDownOption(""+category.getId(), category.getDescription());	
				categoriesList.add(categoryOption);
			}
			session.put(ApplicationConstants.CATEGORY_DROPDOWN_LIST, categoriesList);	
		}		
	}
	/**
	 * This method is responsible for fetching displayColumns for different categories.
	 * @return List<Tab>
	 */
	public List<Tab> getPortfolioDisplayColumn(Map<String, List<Tab>> colMap, int category){
		List<Tab> displayColumn = null;
		switch(category){
		case (int)ApplicationConstants.PORTFOLIO_CATEGORY_ACTIVE:
			 displayColumn = colMap.get(ApplicationConstants.PORTFOLIO_ACTIVE);
			 break;
		case (int)ApplicationConstants.PORTFOLIO_CATEGORY_NEW:
			 displayColumn = colMap.get(ApplicationConstants.PORTFOLIO_NEW);
			 break;
		case (int)ApplicationConstants.PORTFOLIO_CATEGORY_DELETED:
			 displayColumn = colMap.get(ApplicationConstants.PORTFOLIO_DELETED);
			 break;
		case (int)ApplicationConstants.PORTFOLIO_CATEGORY_DISCREPANCY:
			 displayColumn = colMap.get(ApplicationConstants.PORTFOLIO_DISCREPANCY);
			 break;
		case (int)ApplicationConstants.PORTFOLIO_CATEGORY_INACTIVE:
			 displayColumn = colMap.get(ApplicationConstants.PORTFOLIO_INACTIVE);
			 break;
	}
		return displayColumn;
	}
	
	/**
	 * This method is responsible for fetching displayColumns for different categories.
	 * @return List<Tab>
	 */
	public List<Tab> getI2ePortfolioDisplayColumn(Map<String, List<Tab>> colMap, int category){
		List<Tab> displayColumn = null;
		switch(category){
		case (int)ApplicationConstants.I2E_PORTFOLIO_CATEGORY_ACCOUNT:
			 displayColumn = colMap.get(ApplicationConstants.I2E_PORTFOLIO_ACCOUNT);
			 break;
		case (int)ApplicationConstants.I2E_PORTFOLIO_CATEGORY_DISCREPANCY:
			 displayColumn = colMap.get(ApplicationConstants.I2E_PORTFOLIO_DISCREPANCY);
			 break;
		}
		return displayColumn;
	}
	
	/**
	 * This method is setting Up ChangePageSize DropDown List.
	 * @return 
	 */
	public void setUpChangePageSizeDropDownList(String changePageSizeProperty, Map<String, Object> session){
		List<DropDownOption> pageSizeList = new ArrayList<DropDownOption>();
		for(String pageSize: changePageSizeProperty.split(",")){
			DropDownOption pageSizeListOption = new DropDownOption(pageSize, pageSize);	
			pageSizeList.add(pageSizeListOption);
		}		
		session.put(ApplicationConstants.PAGE_SIZE_LIST, pageSizeList);		
	}
	
	
	/**
	 * Prepares the list of elements to be displayed in the Category dropdown
	 * when a specific Audit is choosen from the Audit dropdown in the Reports 
	 * sub-tab of Admin tab. This list consists of: non IMPAC II only categories + 
	 * IMPAC II only categories specified for this Audit. 
	 * 
	 * @param lookupService
	 * @param adminService
	 * @param auditId
	 * @return
	 */
	public List<DropDownOption> getReportCategories(
			LookupService lookupService, AdminService adminService, Long auditId) {
		List<DropDownOption> categoryDropdownList = new ArrayList<DropDownOption>();
		
		//Retrieve the full List of all supported report categories
		List<AppLookupT> reportCategoryLookupList = 
				lookupService.getList(ApplicationConstants.APP_LOOKUP_REPORTS_CATEGORY_LIST);
		
		//Retrieve the full list of IMPAC II only category codes. This is a subset
		//of the codes in the reportCategoryLookupList
		List<String> impac2CategoryLookupCodes = 
				lookupService.getCodeList(ApplicationConstants.APP_LOOKUP_CATEGORY_LIST);
		
		//Retrieve IMPAC II only category codes specific to this Audit
		EmAuditsVO auditVO = adminService.retrieveAuditVO(auditId);
		List<String> impac2CategoryCodes = auditVO.getCategoryList();
		logger.info("Categories retrieved for auditId " + auditId + ": " + impac2CategoryCodes.toString());
		
		//Iterate through the values in the reportCategoryLookupList. 
		for(AppLookupT lookup : reportCategoryLookupList){
			
			if(ApplicationConstants.CATEGORY_I2E.equalsIgnoreCase(lookup.getCode())) {
				//This is the lookup for I2E dropdown element, hence include it
				//only if I2E Audit was performed
				if(!ApplicationConstants.TRUE.equalsIgnoreCase(auditVO.getI2eAuditFlag())) {
					continue;
				}
			}
			
			//Include this value in the dropdown options if it is not present in the
			//impac2CategoryLookupCodes list (hence is not an IMPAC II only category) 
			//OR it is an IMPAC II only category specified for this Audit
			if(!impac2CategoryLookupCodes.contains(lookup.getCode()) ||
					impac2CategoryCodes.contains(WordUtils.capitalizeFully(lookup.getCode())) ) {
				DropDownOption ddo = new DropDownOption();
				ddo.setOptionKey(lookup.getId().toString());
				ddo.setOptionValue(lookup.getDescription());
				categoryDropdownList.add(ddo);
			}
		}
		
		return categoryDropdownList;
	}
	
	
	/**
	 * This method returns nested columns for requested type.
	 * @return List<Tab>
	 */
	public List<Tab> getNestedTableColumns(List<Tab> displayColumn, String type){
		ArrayList<Tab> nestedColumns = new ArrayList<Tab>();
		for(Tab tab : displayColumn){
			if(ApplicationConstants.TRUE.equalsIgnoreCase(tab.getIsNestedColumn()) && type.equalsIgnoreCase(tab.getType())){
				nestedColumns.add(tab);
			}
		}
		return nestedColumns;
	}
	
	/**
	 * This method returns Nested Columns titles for requested type.
	 * @return String
	 */
	public String getNestedTableColumnsNames(List<Tab> displayColumn, String type){
		String nestedColumnsNames = "";
		for(Tab tab : displayColumn){
			if(ApplicationConstants.TRUE.equalsIgnoreCase(tab.getIsNestedColumn()) && type.equalsIgnoreCase(tab.getType())){
				nestedColumnsNames += "<span class='rolesHeader'>" + (!StringUtils.contains(tab.getColumnName(), ROLE_ORG_PATH) ? " | " : "") + tab.getColumnName() + "</span>";
			}
		}	
		return nestedColumnsNames;
	}
	
	/**
	 * This method creates Transfer organization drop down list.
	 * @param transferOrganizationList
	 * @param lookupService
	 * @param parentNedOrgPath
	 */
	public void createTransferOrgDropDownList(List<DropDownOption> transferOrganizationList, LookupService lookupService, String parentNedOrgPath){
		DropDownOption emptyOption = new DropDownOption("", "");
		transferOrganizationList.add(emptyOption);
		for(EmOrganizationVw org : (List<EmOrganizationVw>) lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST)){
			if(!parentNedOrgPath.equalsIgnoreCase(org.getNihorgpath()) && !ApplicationConstants.ORG_PATH_NON_NCI.equalsIgnoreCase(org.getNihorgpath()) 
					&& !ApplicationConstants.ORG_PATH_NO_NED_ORG.equalsIgnoreCase(org.getNihorgpath())){
				DropDownOption transferOrgOption = new DropDownOption(org.getNihorgpath(), org.getNihorgpath());
				transferOrganizationList.add(transferOrgOption);
			}
		}
	}
}
