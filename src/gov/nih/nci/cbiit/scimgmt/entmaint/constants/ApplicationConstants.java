package gov.nih.nci.cbiit.scimgmt.entmaint.constants;



public interface ApplicationConstants {
    
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
    public static final String DISCREPANCY_TYPES_LIST = "discrepancyTypesList";
    public static final String GLOBAL_LOOKUP_LIST = "appLookup";
    public static final String HOST_URL = "hostUrl";
    
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
	
    public static String PORTFOLIOTAB = "portfolio";
    
    public static String COLUMNSATTRIBUTE = "colAttribute";
    public static String SEARCHVO = "searchVO";
    
    public static String ACTIONLIST = "actionList";
    
    public static String PAGE_SIZE = "pageSize";
}
