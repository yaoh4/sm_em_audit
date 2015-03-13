package gov.nih.nci.cbiit.scimgmt.entmaint.helper.action;

import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmOrganizationVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;

@SuppressWarnings("unchecked")
public class AuditSearchActionHelper {
	

	public void createActiveDropDownList(List<DropDownOption> organizationList, List<DropDownOption> actionList, LookupService lookupService){
		
		List<AppLookupT> actList = lookupService.getList(ApplicationConstants.APP_LOOKUP_ACTIVE_ACTION_LIST);
		List<EmOrganizationVw> orglist = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		createOrgList(orglist, organizationList);
		createActionList(actList, actionList);
	}
	
	public void createNewDropDownList(List<DropDownOption> organizationList, List<DropDownOption> actionList, LookupService lookupService){
	
		List<AppLookupT> actList = lookupService.getList(ApplicationConstants.APP_LOOKUP_NEW_ACTION_LIST);
		List<EmOrganizationVw> orglist = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		createOrgList(orglist, organizationList);
		createActionList(actList, actionList);
	}
	
	public void createDeletedDropDownList(List<DropDownOption> organizationList, List<DropDownOption> actionList, LookupService lookupService){
		List<AppLookupT> actList = lookupService.getList(ApplicationConstants.APP_LOOKUP_DELETED_ACTION_LIST);
		List<EmOrganizationVw> orglist = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		createOrgList(orglist, organizationList);
		createActionList(actList, actionList); 
	}
	
	public void createInactiveDropDownList(List<DropDownOption> organizationList, List<DropDownOption> actionList, LookupService lookupService){
		List<AppLookupT> actList = lookupService.getList(ApplicationConstants.APP_LOOKUP_INACTIVE_ACTION_LIST);
		List<EmOrganizationVw> orglist = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		createOrgList(orglist, organizationList);
		createActionList(actList, actionList);
	}
	
	public void createActionList(List<AppLookupT> actList, List<DropDownOption> actionList){
		
		for(AppLookupT act : actList){
			DropDownOption ddp1 = new DropDownOption(""+act.getId(), act.getDescription());	
			actionList.add(ddp1);
		}
	}
	
	public void createOrgList(List<EmOrganizationVw> orgList, List<DropDownOption> organizationList){
		for(EmOrganizationVw org : orgList){
			DropDownOption ddp = new DropDownOption(org.getNihorgpath(), org.getNihorgpath());
			organizationList.add(ddp);
		}
	}
	
	/**
	 * This method is responsible for preparing drop down lists for organizationList and categoriesList for portfolio accounts search.
	 * @return 
	 */
	public void  createPortFolioDropDownLists(List<DropDownOption> organizationList, List<DropDownOption> categoriesList,  LookupService lookupService){

		List<EmOrganizationVw> orgs = lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		if(orgs != null && orgs.size() >0){
			for(EmOrganizationVw org : orgs){
				DropDownOption orgOption = new DropDownOption(org.getNihorgpath(), org.getNihorgpath());	
				organizationList.add(orgOption);	
			}
		}

		List<AppLookupT> categories = (List<AppLookupT>)lookupService.getList(ApplicationConstants.APP_LOOKUP_PORTFOLIO_CATEGORY_LIST);		
		if(categories != null && categories.size() >0){
			for(AppLookupT category : categories){
				DropDownOption categoryOption = new DropDownOption(""+category.getId(), category.getDescription());	
				categoriesList.add(categoryOption);
			}
		}		
	}
}
