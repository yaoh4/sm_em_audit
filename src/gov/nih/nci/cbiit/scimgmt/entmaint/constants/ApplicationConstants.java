package gov.nih.nci.cbiit.scimgmt.entmaint.constants;



public interface ApplicationConstants {
    
	/* Application name in lookup and property table */
	public static final String APP_NAME = "EM";
	
	/* Application tabs */
    public static final String TAB_IMPAC2 = "impac2";
    public static final String TAB_I2E = "i2e";
    public static final String TAB_ADMIN = "admin";
	
    /* Tab Navigation links for IMPAC II and I2E*/
	public static final String SUB_NAV_AUDIT = "audit";
    public static final String SUB_NAV_PORTFOLIO = "portfolio";
    
    /*Tab Navigation links for Admin*/
    public static final String SUB_NAV_ADMINISTER = "administer";
    public static final String SUB_NAV_DASHBOARD = "dashboard";
    public static final String SUB_NAV_REPORTS = "reports";
    
    /*Sub tabs within the navigation links for IMPAC II and I2E*/
    public static final String SUB_TAB_ACTIVE_ACCOUNTS = "activeAccounts";
    public static final String SUB_TAB_NEW_ACCOUNTS = "newAccounts";
    public static final String SUB_TAB_DELETED_ACCOUNTS = "deletedAccounts";
    public static final String SUB_TAB_INACTIVE_ACCOUNTS = "inactiveAccounts";
    
    /*Audit states*/
    public static final String AUDIT_STATE_CODE_ENABLED = "ENABLED";
    public static final String AUDIT_STATE_CODE_DISABLED = "DISABLED";
    public static final String AUDIT_STATE_CODE_RESET = "RESET";
    
    /* AppLookupT discriminator key for audit states*/
    public static final String AUDIT_STATES_LOOKUP_KEY = "AppLookupAuditState";
    
    /*User roles*/
    public static final String USER_ROLE_IC_COORDINATOR = "EMREP";
    public static final String USER_ROLE_SUPER_USER = "EMADMIN";
    
    /*NCI Doc */
    public static final String NCI_DOC_OTHER = "OTHER";
    public static final String NCI_DOC_ALL = "All";
    
    /* Session key for current audit*/
    public static final String CURRENT_AUDIT = "currentAudit";
    
    
    /*Lists */
    public static final String APP_LINK_LIST = "applinklist";
    public static final String APP_LOOKUP_CATEGORY_LIST = "appLookupCategory";
    public static final String APP_LOOKUP_DISCREPANCY_TYPE_LIST = "appLookupDiscrepancyType";
    public static final String APP_LOOKUP_ACTIVE_ACTION_LIST = "appLookupActiveAction";
    public static final String APP_LOOKUP_NEW_ACTION_LIST = "appLookupNewAction";
    public static final String APP_LOOKUP_DELETED_ACTION_LIST = "appLookupDeletedAction";
    public static final String APP_LOOKUP_INACTIVE_ACTION_LIST = "appLookupInactiveAction";
    public static final String YOUR_GRANT_DETAILS_APP_LINK_LIST = "yourGrantDetailsapplinklist";
    public static final String APP_LOOKUP_PORTFOLIO_CATEGORY_LIST = "appLookupPortfolioCategory";
    public static final String ORGANIZATION_DROPDOWN_LIST = "orgList";
    public static final String CATEGORY_DROPDOWN_LIST = "categoryList";
    public static final String DISCREPANCY_TYPES_LIST = "discrepancyTypesList";
    public static final String ERA_ROLES_LIST = "eraBusinessRolesList";
    public static final String GLOBAL_LOOKUP_LIST = "appLookup";
    public static final String HOST_URL = "hostUrl";
    
    /* Property */
    public static final String IC_COORDINATOR_EMAIL = "IC_EMAIL";
    public static final String KEY_ROLES_DOC_LINK = "ROLES_DOC_LINK";

    /* Discrepancy type codes*/
    public static final String DISCREPANCY_CODE_SOD = "SOD";
    public static final String DISCREPANCY_CODE_IC = "ICDIFF";
    public static final String DISCREPANCY_CODE_NED_INACTIVE = "NEDINACTIVE";
    public static final String DISCREPANCY_CODE_LAST_NAME = "LNAMEDIFF";
    
    /* Organization Drop down */
    public static final String ORG_PATH_NON_NCI = "Non-NCI";
    public static final String NED_IC_NCI = "NCI";
    
    //Encoding
    public static final String ENCODING = "UTF-8";
    
    public static final String LOOKUP_SERVICE = "lookupService";
    
    public static final String SESSION_USER = "nciUser";
    
    public static final String NCI_ORACLE_ID = "nciOracleId";
  	
    public static String TRUE = "true";
    public static String FALSE = "false";
    
    /*forwards */
    public static String SUCCESS = "success";
    public static String INPUT = "input";
    public static String EXPORT = "export";
    public static String AUDIT_ENABLED = "audit_enabled";
    public static String AUDIT_DISABLED = "audit_disabled";
    
	public static final String CATEGORY_ACTIVE = "ACTIVE";
	public static final String CATEGORY_NEW = "NEW";
	public static final String CATEGORY_DELETED = "DELETED";
	public static final String CATEGORY_INACTIVE = "INACTIVE";
	
	public static final String ACTIVE_ACTION_ALL = "1";
	public static final String NEW_ACTION_ALL = "5";
	public static final String DELETED_ACTION_ALL = "8";
	public static final String INACTIVE_ACTION_ALL = "11";
	public static final String VERIFIEDLEAVE = "2";
	public static final int VERIFIEDLEAVEINT = 2;
	public static final String NOTNEED = "13";
	public static final int NOTNEEDINT = 13;
	
    public static String PORTFOLIO_ACTIVE = "portfolioActive";
    public static String PORTFOLIO_NEW = "portfolioNew";
    public static String PORTFOLIO_DELETED = "portfolioDeleted";
    public static String PORTFOLIO_DISCREPANCY = "portfolioDiscrepancy";
    
    public static String COLUMNSATTRIBUTE = "colAttribute";
    public static String REPORTCOLATTRIBUTE = "reportAttibute";
    public static String SEARCHVO = "searchVO";
    public static String PORTFOLIO_SEARCHVO = "portfolioSearchVO";

    public static String ACTIONLIST = "actionList";
    
    public static String PAGE_SIZE = "pageSize";
    public static final long PORTFOLIO_CATEGORY_ACTIVE = 22;
	public static final long PORTFOLIO_CATEGORY_NEW = 23;
	public static final long PORTFOLIO_CATEGORY_DELETED = 24;
	public static final long PORTFOLIO_CATEGORY_DISCREPANCY = 25;
	public static final String CURRENTPAGE ="cuurentPage";
	
	public static final String FLAG_YES = "Y";
	public static final String FLAG_NO = "N";
	public static String PRIMARY = "primary";
	
	public static final String PAGE_SIZE_LIST = "pageSizeList";
	public static final String DEFAULT_PAGE_SIZE = "defaultPageSize";
	
	public static final String STATUS_FAIL = "fail";
	
	public static final String ERA_US_LINK = "ERA_UA_LINK";
	public static final String I2E_EM_LINK = "I2E_EM_LINK";
	
	public static final String UNDO_COMFIRMATION="undo.confirmation.message";
	public static final String ERROR_SAVE_TO_DATABASE="error.database.save";
	public static final String NOTHING_DISPLAY="nothing.display";
	public static final String EMPTY_NOTE="error.empty.note";
	public static final String MISSING_NOTE="error.missing.note";
	public static final String ACTION_SELECTION="error.action.selection";
	
	public static final String ERAUA_NA = "NA";
	public static final String ERAUA_INFO = "eraus.info.message";
}
