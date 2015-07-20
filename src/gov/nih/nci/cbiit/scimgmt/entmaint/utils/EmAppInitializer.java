package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * The Class EmAppInitializer.
 */
@Service
@Scope(value = "singleton")
public class EmAppInitializer {
	@Autowired
	private LookupService lookupService;


	public EmAppInitializer() {

	}

	/**
	 * Initialize.
	 */
	@PostConstruct
	public void initialize() {
		lookupService.getList(ApplicationConstants.APP_LINK_LIST);
		lookupService.getList(ApplicationConstants.ERA_ROLES_LIST);
		lookupService.getList(ApplicationConstants.APP_LOOKUP_ACTIVE_ACTION_LIST);
		lookupService.getList(ApplicationConstants.APP_LOOKUP_NEW_ACTION_LIST);
		lookupService.getList(ApplicationConstants.APP_LOOKUP_DELETED_ACTION_LIST);
		lookupService.getList(ApplicationConstants.APP_LOOKUP_INACTIVE_ACTION_LIST);
		lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		lookupService.getList(ApplicationConstants.APP_LOOKUP_PORTFOLIO_CATEGORY_LIST);
	}

	/**
	 * Re-initialize.
	 */
	public void reinit() {
		lookupService.refreshLists();
		lookupService.getList(ApplicationConstants.APP_LINK_LIST);
		lookupService.getList(ApplicationConstants.ERA_ROLES_LIST);
		lookupService.getList(ApplicationConstants.APP_LOOKUP_ACTIVE_ACTION_LIST);
		lookupService.getList(ApplicationConstants.APP_LOOKUP_NEW_ACTION_LIST);
		lookupService.getList(ApplicationConstants.APP_LOOKUP_DELETED_ACTION_LIST);
		lookupService.getList(ApplicationConstants.APP_LOOKUP_INACTIVE_ACTION_LIST);
		lookupService.getList(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST);
		lookupService.getList(ApplicationConstants.APP_LOOKUP_PORTFOLIO_CATEGORY_LIST);
	}

}
