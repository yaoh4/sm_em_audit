package gov.nih.nci.cbiit.scimgmt.entmaint.helper.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
							    act.getId() == ApplicationConstants.INACTIVE_EXCLUDE_FROM_AUDIT )){
					continue;
			}else{
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
	
	public  List<DropDownOption> createAuditPeriodDropDownList(AdminService adminService){
		List<DropDownOption> auditPeriodList = new ArrayList<DropDownOption>();
		List<EmAuditsVO> emAuditVOs = adminService.retrieveAuditVOList();
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
	
	public List<DropDownOption> getReportCatrgories(LookupService lookupService){
		List<DropDownOption> categoryList = new ArrayList<DropDownOption>();
		List<AppLookupT> cateList = lookupService.getList(ApplicationConstants.APP_LOOKUP_REPORTS_CATEGORY_LIST);
		
		if(cateList != null){
			for(AppLookupT obj : cateList){
				DropDownOption ddo = new DropDownOption();
				ddo.setOptionKey(""+obj.getId());
				ddo.setOptionValue(obj.getDescription());
				categoryList.add(ddo);
			}
		}
		return categoryList;
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
}
