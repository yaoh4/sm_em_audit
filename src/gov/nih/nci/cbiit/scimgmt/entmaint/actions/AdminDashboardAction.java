package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2eAuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DashboardData;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.ActionDashboardData;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Action class for Dashboard tab.
 * 
 * @author zhoujim, dinhys
 *
 */
@SuppressWarnings("serial")
public class AdminDashboardAction extends BaseAction {
	protected EmAuditsVO emAuditsVO = new EmAuditsVO();
	
	private static final String ACTIVE = "active";
	private static final String NEW = "new";
	private static final String DELETED = "deleted";
	private static final String INACTIVE = "inactive";
	private static final String I2E = "i2e";
	
	//------------------------------
	//Attributes for Dashboard
	//-----------------------------------
	private List<Object> orgKeys;
	private List<Object> otherOrgKeys;
	private List<Object> actionOrgKeys;
	private HashMap<String, HashMap<String, DashboardData>> orgsData;
	private HashMap<String, HashMap<String, DashboardData>> otherOrgsData;
	private HashMap<String, HashMap<String, ActionDashboardData>> actionOrgsData;
	private DashboardData others;
	private Long auditId;
	
	@Autowired
	protected Impac2AuditService impac2AuditService;	
	@Autowired
	protected I2eAuditService i2eAuditService;
	
	/**
     * Set up all necessary component for dashboard. If success, takes the user to dashboard page. 
     * @param auditState
     * @return
     */
    public String gotoDashboard(){
    	/**
    	 * Define two hashMaps to hold data and user maps to sort all Organizations with total counts for active, 
    	 * new, deleted and inactive accounts in one loop.
    	 */
    	//clear the audit ID
    	auditId = null;
    	orgsData = new HashMap<String, HashMap<String,DashboardData>>();
    	otherOrgsData = new HashMap<String, HashMap<String, DashboardData>>();
    	actionOrgsData = new HashMap<String, HashMap<String, ActionDashboardData>>();
    	
    	//set up all environment for displaying dashboard page.
    	emAuditsVO = adminService.retrieveCurrentOrLastAuditVO();
    	setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO);
    	auditId = emAuditsVO.getId();
    
    	List<AuditAccountVO> auditAccountVOs = impac2AuditService.getAllAccountsByAuditId(auditId);
    	List<AuditI2eAccountVO> auditI2eAccountVOs = i2eAuditService.getAllAccountsByAuditId(auditId);

    	populateImpac2DashboardData(auditAccountVOs);
    	populateI2eDashboardData(auditI2eAccountVOs);
    	
    	//calculate the total other orgs
    	others = incrementCountOther(otherOrgsData);
    	
    	Set<String> keySet = orgsData.keySet();
    	Object[] keys = keySet.toArray();
    	Arrays.sort(keys);
    	
    	Set<String> otherKeySet = otherOrgsData.keySet();
    	Object[] otherKeys = otherKeySet.toArray();
    	Arrays.sort(otherKeys);
    	
    	Set<String> actionKeySet = actionOrgsData.keySet();
    	Object[] actionKeys = actionKeySet.toArray();
    	Arrays.sort(actionKeys);
    	
    	//set arrays for displaying
    	orgKeys = Arrays.asList(keys);
    	otherOrgKeys = Arrays.asList(otherKeys);
    	actionOrgKeys = Arrays.asList(actionKeys);
    	//move NON-NCI to the last element
    	otherOrgKeys = moveNonNCIToLast(otherOrgKeys);
    	actionOrgKeys = moveNonNCIToLast(actionOrgKeys);
    	return ApplicationConstants.SUCCESS;
    }
    
    /**
	 * This method provide a bridge to search audit page based on category, active, new, deleted, inactive or i2e
	 */
    public String searchAudit(){
    	searchVO = new AuditSearchVO();
    	String forward = "";
    	String cate = request.getParameter("cate");
    	String orgName = request.getParameter("orgName");
    	String action = request.getParameter("act");
    	searchVO.setOrganization(orgName);
    	searchVO.setExcludeNCIOrgs(false);
    	if(!StringUtils.isEmpty(action) && !StringUtils.equalsIgnoreCase(action, "undefined"))
    		searchVO.setAct(action);
    	emAuditsVO = (EmAuditsVO)getAttributeFromSession(ApplicationConstants.CURRENT_AUDIT);
    	searchVO.setAuditId(emAuditsVO.getId());
    	session.put(ApplicationConstants.SEARCHVO, searchVO);
    	if(cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_ACTIVE)){
    		forward = ApplicationConstants.CATEGORY_ACTIVE;
    	}else if(cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_NEW)){
    		forward = ApplicationConstants.CATEGORY_NEW;
    	}else if(cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_DELETED)){
    		forward = ApplicationConstants.CATEGORY_DELETED;
    	}else if(cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_INACTIVE)){
    		forward = ApplicationConstants.CATEGORY_INACTIVE;
    	}else if(cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_I2E)){
    		forward = ApplicationConstants.CATEGORY_I2E;
    	}
    	
    	return forward;
    }
    
    /**
     * Move NON NCI to be last element
     */
    private List<Object> moveNonNCIToLast(List<Object> orgKeys){
    	List<Object> sortedList = new ArrayList<Object>();
    	for(Object s : orgKeys){
    		String key = (String)s;
    		if(key.equalsIgnoreCase(ApplicationConstants.ORG_PATH_NON_NCI) || key.equalsIgnoreCase(ApplicationConstants.ORG_PATH_NO_NED_ORG) ){
    			continue;
    		}else{
    			sortedList.add(s);
    		}
    	}
    	if(orgKeys.contains(ApplicationConstants.ORG_PATH_NON_NCI))
    		sortedList.add(ApplicationConstants.ORG_PATH_NON_NCI);
    	if(orgKeys.contains(ApplicationConstants.ORG_PATH_NO_NED_ORG))
    		sortedList.add(ApplicationConstants.ORG_PATH_NO_NED_ORG);
    	return sortedList;
    }
    
    /**
     * Find out if the hashmap has the organization initialized.
     * @param orgData
     * @param key
     * @return
     */
    private boolean containsKey(HashMap<String, HashMap<String, DashboardData>> orgData, String key){
    	return orgData.containsKey(key);
    }
    
    /**
     * Find out if the hashmap has the organization initialized.
     * @param orgData
     * @param key
     * @return
     */
    private boolean containsActionKey(HashMap<String, HashMap<String, ActionDashboardData>> orgData, String key){
    	return orgData.containsKey(key);
    }
    
    /**
     * Check to see if this account should be in the active category
     * @return
     */
    private boolean isActiveAccount(AuditAccountVO audit) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	
	   	Date impaciiToDate = emAuditsVO.getImpaciiToDate();
    	Date deletedDate = audit.getDeletedDate();
    	Date createdDate = audit.getCreatedDate();
    	
    	String deletedDateStr = (deletedDate == null ? null : dateFormat.format(deletedDate));
    	String createdDateStr = (createdDate == null ? null : dateFormat.format(createdDate));
    	
    	try {
			deletedDate = (deletedDateStr == null ? null : dateFormat.parse(deletedDateStr));
			createdDate = (createdDateStr == null ? null : dateFormat.parse(createdDateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	//determine active account
    	if(createdDate != null && 
    		(deletedDate == null || deletedDate.after(impaciiToDate) || deletedDate.equals(impaciiToDate)) &&  
    		(createdDate.before(impaciiToDate) || createdDate.equals(impaciiToDate))){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Check to see if this account should be in the new category
     * @return
     */
    private boolean isNewAccount(AuditAccountVO audit) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	
	   	Date impaciiToDate = emAuditsVO.getImpaciiToDate();
    	Date impaciiFromDate = emAuditsVO.getImpaciiFromDate();
    	Date createdDate = audit.getCreatedDate();
    	
    	String createdDateStr = (createdDate == null ? null : dateFormat.format(createdDate));
    	
    	try {
			createdDate = (createdDateStr == null ? null : dateFormat.parse(createdDateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	//determine new account
    	if(createdDate != null && 
    			(createdDate.after(impaciiFromDate) || createdDate.equals(impaciiFromDate)) && 
    			(createdDate.before(impaciiToDate) || createdDate.equals(impaciiToDate))){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Check to see if this account should be in the deleted category
     * @return
     */
    private boolean isDeletedAccount(AuditAccountVO audit) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	
	   	Date impaciiToDate = emAuditsVO.getImpaciiToDate();
    	Date impaciiFromDate = emAuditsVO.getImpaciiFromDate();
    	Date deletedDate = audit.getDeletedDate();
   	
    	String deletedDateStr = (deletedDate == null ? null : dateFormat.format(deletedDate));
   	
    	try {
			deletedDate = (deletedDateStr == null ? null : dateFormat.parse(deletedDateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	//determine deleted account
    	if(deletedDate != null && 
    			(deletedDate.after(impaciiFromDate) || deletedDate.equals(impaciiFromDate)) && 
    			(deletedDate.before(impaciiToDate) || deletedDate.equals(impaciiToDate))){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Check to see if this account should be in the inactive category
     * @return
     */
    private boolean isInactiveAccount(AuditAccountVO audit) {
    	//determine inactive account
    	if(audit.getInactiveUserFlag() != null && audit.getInactiveUserFlag().equalsIgnoreCase(ApplicationConstants.FLAG_YES)){
    		return true;
    	}
    	return false;
    }
    
    
    /** 
     * Calculate counts, including total, uncompleted numbers sorted by organization names. The other organization needs to computer seperately.
     * @param audit
     * @param dashData
     */
    private void setCountForEachCategory(AuditAccountVO audit, HashMap<String,DashboardData> dashData){
    	//determine active account
    	if(isActiveAccount(audit)){
    		incrementCountByCategory(audit, dashData, ACTIVE);
    		if(!StringUtils.isEmpty(audit.getActiveSubmittedBy())){
    			incrementCompletedCountByCategory(audit, dashData, ACTIVE);
    		}
    	}
    	//determine new account
    	if(isNewAccount(audit)){
    		incrementCountByCategory(audit, dashData, NEW);
    		if(!StringUtils.isEmpty(audit.getNewSubmittedBy())){
    			incrementCompletedCountByCategory(audit, dashData, NEW);
    		}
    	}
    	//determine inactive account
    	if(isInactiveAccount(audit)){
    		incrementCountByCategory(audit, dashData, INACTIVE);
    		if(!StringUtils.isEmpty(audit.getInactiveSubmittedBy())){
    			incrementCompletedCountByCategory(audit, dashData, INACTIVE);
    		}
    	}
    }
   
    /** 
     * Calculate counts, including total, uncompleted numbers sorted by organization names. The other organization needs to computer seperately.
     * @param audit
     * @param dashData
     */
    private void setCountForDeletedCategory(AuditAccountVO audit, HashMap<String,DashboardData> dashData){
    	//determine deleted account
    	if(isDeletedAccount(audit)){
    		incrementCountByCategory(audit, dashData, DELETED);
    		if(!StringUtils.isEmpty(audit.getDeletedSubmittedBy())){
    			incrementCompletedCountByCategory(audit, dashData, DELETED);
    		}
    	}
    }
    
    /** 
     * Calculate counts, including total, uncompleted numbers sorted by organization names. The other organization needs to computed separately.
     * @param audit
     * @param dashData
     */
    private void setCountForEachCategory(AuditI2eAccountVO audit, HashMap<String,DashboardData> dashData){
    	//active account
    	incrementCountByCategory(audit, dashData, I2E);
    	if(!StringUtils.isEmpty(audit.getSubmittedBy())){
    		incrementCompletedCountByCategory(audit, dashData, I2E);
    	}
    }
    
    private void incrementCountByCategory(AuditAccountVO audit, HashMap<String,DashboardData> dashData, String category){
    	if(category.equalsIgnoreCase(ACTIVE)){
    		DashboardData ddata = dashData.get(ACTIVE);
    		int count = ddata.getActiveAccountCount();
    		ddata.setActiveAccountCount(++count);
    		dashData.put(ACTIVE, ddata);
    	}else if(category.equalsIgnoreCase(NEW)){
    		DashboardData ddata = dashData.get(NEW);
    		int count = ddata.getNewAccountCount();
    		ddata.setNewAccountCount(++count);
    		dashData.put(NEW, ddata);
    	}else if(category.equalsIgnoreCase(DELETED)){
    		DashboardData ddata = dashData.get(DELETED);
    		int count = ddata.getDeletedAccountCount();
    		ddata.setDeletedAccountCount(++count);
    		dashData.put(DELETED, ddata);
    	}else if(category.equalsIgnoreCase(INACTIVE)){
    		DashboardData ddata = dashData.get(INACTIVE);
    		int count = ddata.getInactiveAccountCount();
    		ddata.setInactiveAccountCount(++count);
    		dashData.put(INACTIVE, ddata);
    	}
    }
    
    private void incrementCountByCategory(AuditI2eAccountVO audit, HashMap<String,DashboardData> dashData, String category){
    	if(category.equalsIgnoreCase(I2E)){
    		DashboardData ddata = dashData.get(I2E);
    		int count = ddata.getI2eAccountCount();
    		ddata.setI2eAccountCount(++count);
    		dashData.put(I2E, ddata);
    	}
    }
    
    private void incrementCompletedCountByCategory(AuditAccountVO audit, HashMap<String,DashboardData> dashData, String category){
    	if(category.equalsIgnoreCase(ACTIVE)){
    		DashboardData ddata = dashData.get(ACTIVE);
    		int count = ddata.getActiveCompleteCount();
    		ddata.setActiveCompleteCount(++count);
    		dashData.put(ACTIVE, ddata);
    	}else if(category.equalsIgnoreCase(NEW)){
    		DashboardData ddata = dashData.get(NEW);
    		int count = ddata.getNewCompleteCount();
    		ddata.setNewCompleteCount(++count);
    		dashData.put(NEW, ddata);
    	}else if(category.equalsIgnoreCase(DELETED)){
    		DashboardData ddata = dashData.get(DELETED);
    		int count = ddata.getDeletedCompleteCount();
    		ddata.setDeletedCompleteCount(++count);
    		dashData.put(DELETED, ddata);
    	}else if(category.equalsIgnoreCase(INACTIVE)){
    		DashboardData ddata = dashData.get(INACTIVE);
    		int count = ddata.getInactiveCompleteCount();
    		ddata.setInactiveCompleteCount(++count);
    		dashData.put(INACTIVE, ddata);
    	}
    }
    
    private void incrementCompletedCountByCategory(AuditI2eAccountVO audit, HashMap<String,DashboardData> dashData, String category){
    	if(category.equalsIgnoreCase(I2E)){
    		DashboardData ddata = dashData.get(I2E);
    		int count = ddata.getI2eCompleteCount();
    		ddata.setI2eCompleteCount(++count);
    		dashData.put(I2E, ddata);
    	}
    }
    
    /**
     * First map element, initial all categories with hashmap<String, DashboardData, make sure next computing will not get nullPoint.
     * All categories include ACTIVE, NEW, DELETED, INACTIVE, I2E.
     * @param audit
     * @param dashData
     */
    private void setFirstElementCountForEachCategory(AuditAccountVO audit, HashMap<String,DashboardData> dashData){
    	DashboardData dash = new DashboardData();
    	dashData.put(ACTIVE, dash);
    	dash = new DashboardData();
    	dashData.put(NEW, dash);
    	dash = new DashboardData();
    	dashData.put(DELETED, dash);
    	dash = new DashboardData();
    	dashData.put(INACTIVE, dash);
    	dash = new DashboardData();
    	dashData.put(I2E, dash);
    }
    
    /**
     * First map element, initial all categories with hashmap<String, DashboardData, make sure next computing will not get nullPoint.
     * All categories include ACTIVE, NEW, DELETED, INACTIVE, I2E.
     * @param audit
     * @param dashData
     */
    private void setFirstElementCountForEachCategory(AuditI2eAccountVO audit, HashMap<String,DashboardData> dashData){
    	DashboardData dash = new DashboardData();
    	dashData.put(I2E, dash);
    	dash = new DashboardData();
    	dashData.put(ACTIVE, dash);
    	dash = new DashboardData();
    	dashData.put(NEW, dash);
    	dash = new DashboardData();
    	dashData.put(DELETED, dash);
    	dash = new DashboardData();
    	dashData.put(INACTIVE, dash);
    	dash = new DashboardData();
    }
    
    
	/** 
	 * Calculate the other orgs total and other orgs completed
	 */
	private DashboardData incrementCountOther(HashMap<String, HashMap<String, DashboardData>> otherOrgsMap){
		DashboardData otherTotalData = new DashboardData();
		Set<String> keys = otherOrgsMap.keySet();
		Object[] keyArr = keys.toArray();
		for(int i = 0; i < keyArr.length; i++){
			String key = (String)keyArr[i];
			HashMap<String, DashboardData> tempMap = otherOrgsMap.get(key);
			DashboardData actData = tempMap.get(ACTIVE);
			otherTotalData.setActiveAccountCount(otherTotalData.getActiveAccountCount() + actData.getActiveAccountCount());
			otherTotalData.setActiveCompleteCount(otherTotalData.getActiveCompleteCount() + actData.getActiveCompleteCount());
			DashboardData newData = tempMap.get(NEW);
			otherTotalData.setNewAccountCount(otherTotalData.getNewAccountCount() + newData.getNewAccountCount());
			otherTotalData.setNewCompleteCount(otherTotalData.getNewCompleteCount() + newData.getNewCompleteCount());
			DashboardData deleteData = tempMap.get(DELETED);
			otherTotalData.setDeletedAccountCount(otherTotalData.getDeletedAccountCount() + deleteData.getDeletedAccountCount());
			otherTotalData.setDeletedCompleteCount(otherTotalData.getDeletedCompleteCount() + deleteData.getDeletedCompleteCount());
			DashboardData InactData = tempMap.get(INACTIVE);
			otherTotalData.setInactiveAccountCount(otherTotalData.getInactiveAccountCount() + InactData.getInactiveAccountCount());
			otherTotalData.setInactiveCompleteCount(otherTotalData.getInactiveCompleteCount() + InactData.getInactiveCompleteCount());
			DashboardData i2eData = tempMap.get(I2E);
			otherTotalData.setI2eAccountCount(otherTotalData.getI2eAccountCount() + i2eData.getI2eAccountCount());
			otherTotalData.setI2eCompleteCount(otherTotalData.getI2eCompleteCount() + i2eData.getI2eCompleteCount());
		}
		return otherTotalData;
	}
	
	/**
	 * Populate counts for Impac2 Dashboard data
	 * 
	 * @param auditAccountVOs
	 */
	private void populateImpac2DashboardData(List<AuditAccountVO> auditAccountVOs) {
		
    	if(auditAccountVOs != null && auditAccountVOs.size() > 0){
    		for(AuditAccountVO audit : auditAccountVOs){
    			//doing delete account first because the parentNedOrgPath will be null if deleted account appears.
    			Date deletedDate = audit.getDeletedDate();
    	    	if(deletedDate != null){
    	    		populateImpac2DeletedAccountData(audit);
    	    	}

    	    	String org = audit.getParentNedOrgPath();
    			String nciDoc = audit.getNciDoc();
    			String nedIc = audit.getNedIc();
    			if(StringUtils.isNotBlank(org)){
    				if(nciDoc != null && nciDoc.equalsIgnoreCase(ApplicationConstants.NCI_DOC_OTHER)){
    					if(nedIc != null && ApplicationConstants.NED_IC_NCI.equalsIgnoreCase(nedIc) == false){
							org = ApplicationConstants.ORG_PATH_NON_NCI;
						}
    					classifyOrgCategory(otherOrgsData, org, audit);
	    			}else{
	    				classifyOrgCategory(orgsData, org, audit);
    				}
    				classifyActionOrgCategory(actionOrgsData, org, audit);
    			} else {
    				org = ApplicationConstants.ORG_PATH_NO_NED_ORG;
    				classifyOrgCategory(otherOrgsData, org, audit);
    				classifyActionOrgCategory(actionOrgsData, org, audit);
    			}
    		}
    	}
    	return;
	}

	/**
	 * Since deleted account does not have the orgization name, we will based on deletedByOrgName to categorize the deleted account
	 *  confirm with Subasini??
	 */
	
	private void populateImpac2DeletedAccountData(AuditAccountVO audit){
		//since parentOrgPath is empty for deleted account, we will use deletedByParentOrgPath as org name.
    	String org = audit.getDeletedByParentOrgPath();
    	String nciDoc = audit.getDeletedByNciDoc();
    	String nedIc = audit.getNedIc();
    	
    	if(StringUtils.isNotBlank(org)){
			if(nciDoc != null && nciDoc.equalsIgnoreCase(ApplicationConstants.NCI_DOC_OTHER)){
				if(nedIc != null && ApplicationConstants.NED_IC_NCI.equalsIgnoreCase(nedIc) == false){
					org = ApplicationConstants.ORG_PATH_NON_NCI;
				}
				classifyOrgCategoryForDeletedAccounts(otherOrgsData, org, audit);
    		}else{
    			classifyOrgCategoryForDeletedAccounts(orgsData, org, audit);
			}
			classifyActionOrgCategoryForDeletedAccounts(actionOrgsData, org, audit);
		} else {
			org = ApplicationConstants.ORG_PATH_NO_NED_ORG;
			classifyOrgCategoryForDeletedAccounts(otherOrgsData, org, audit);
			classifyActionOrgCategoryForDeletedAccounts(actionOrgsData, org, audit);
    	}
	}
	
	/**
	 * Populate counts for I2e Dashboard data
	 * 
	 * @param auditAccountVOs
	 */
	private void populateI2eDashboardData(List<AuditI2eAccountVO> auditI2eAccountVOs) {
		
		if(auditI2eAccountVOs != null && auditI2eAccountVOs.size() > 0){
    		for(AuditI2eAccountVO audit : auditI2eAccountVOs){
    			String org = audit.getParentNedOrgPath();
    			String nciDoc = audit.getNciDoc();
    			String nedIc = audit.getNedIc();
    			if(StringUtils.isNotBlank(org)){
    				if(nciDoc != null && nciDoc.equalsIgnoreCase(ApplicationConstants.NCI_DOC_OTHER)){
    					if(nedIc != null && ApplicationConstants.NED_IC_NCI.equalsIgnoreCase(nedIc) == false){
							org = ApplicationConstants.ORG_PATH_NON_NCI;
						}
    					classifyOrgCategory(otherOrgsData, org, audit);
	    			}else{
	    				classifyOrgCategory(orgsData, org, audit);
    				}
    				classifyActionOrgCategory(actionOrgsData, org, audit);
    			} else {
    				org = ApplicationConstants.ORG_PATH_NO_NED_ORG;
    				classifyOrgCategory(otherOrgsData, org, audit);
    				classifyActionOrgCategory(actionOrgsData, org, audit);
    			}
    		}
    	}
	}
	
	/**
	 * Add a new Org into hashmap or increment count
	 * 
	 * @param orgHashmap
	 * @param org
	 * @param audit
	 */
	private void classifyOrgCategory(HashMap<String, HashMap<String, DashboardData>> orgHashmap, String org, AuditAccountVO audit) {
		if(containsKey(orgHashmap, org)){
			HashMap<String,DashboardData> dashData = orgHashmap.get(org);
			//calculate Count
			setCountForEachCategory(audit, dashData);
			orgHashmap.put(org, dashData);
		}else{
			HashMap<String, DashboardData> dashData = new HashMap<String, DashboardData>();
			//calculate count
			setFirstElementCountForEachCategory(audit, dashData);
			setCountForEachCategory(audit, dashData);
			orgHashmap.put(org, dashData);
		}
	}
	
	/**
	 * Add a new Org into hashmap or increment count
	 * 
	 * @param orgHashmap
	 * @param org
	 * @param audit
	 */
	private void classifyOrgCategoryForDeletedAccounts(HashMap<String, HashMap<String, DashboardData>> orgHashmap, String org, AuditAccountVO audit) {
		if(containsKey(orgHashmap, org)){
			HashMap<String,DashboardData> dashData = orgHashmap.get(org);
			//calculate Count
			setCountForDeletedCategory(audit, dashData);
			orgHashmap.put(org, dashData);
		}else{
			HashMap<String, DashboardData> dashData = new HashMap<String, DashboardData>();
			//calculate count
			setFirstElementCountForEachCategory(audit, dashData);
			setCountForDeletedCategory(audit, dashData);
			orgHashmap.put(org, dashData);
		}
	}
	
	/**
	 * Add a new Org into hashmap or increment count
	 * 
	 * @param orgHashmap
	 * @param org
	 * @param audit
	 */
	private void classifyActionOrgCategory(HashMap<String, HashMap<String, ActionDashboardData>> orgHashmap, String org, AuditAccountVO audit) {
		
    		
		if (isActiveAccount(audit) && audit.getActiveAction() != null
				&& !StringUtils.equalsIgnoreCase(audit.getActiveUnsubmittedFlag(), "Y")
				&& StringUtils.equalsIgnoreCase(audit.getActiveAction().getCode(), "Unknown")) {
			if (!containsActionKey(orgHashmap, org)) {
				HashMap<String, ActionDashboardData> dashData = new HashMap<String, ActionDashboardData>();
				ActionDashboardData ddata = new ActionDashboardData();
				dashData.put(ACTIVE, ddata);
				ddata = new ActionDashboardData();
				dashData.put(NEW, ddata);
				ddata = new ActionDashboardData();
				dashData.put(DELETED, ddata);
				ddata = new ActionDashboardData();
				dashData.put(I2E, ddata);
				orgHashmap.put(org, dashData);
			}
			HashMap<String, ActionDashboardData> dashData = orgHashmap.get(org);
			ActionDashboardData ddata = dashData.get(ACTIVE);
			int count = ddata.getActiveUnknownCount();
			ddata.setActiveUnknownCount(++count);
			dashData.put(ACTIVE, ddata);
			orgHashmap.put(org, dashData);
		}
		if (isNewAccount(audit) && audit.getNewAction() != null
				&& !StringUtils.equalsIgnoreCase(audit.getNewUnsubmittedFlag(), "Y")
				&& StringUtils.equalsIgnoreCase(audit.getNewAction().getCode(), "Unknown")) {
			if (!containsActionKey(orgHashmap, org)) {
				HashMap<String, ActionDashboardData> dashData = new HashMap<String, ActionDashboardData>();
				ActionDashboardData ddata = new ActionDashboardData();
				dashData.put(ACTIVE, ddata);
				ddata = new ActionDashboardData();
				dashData.put(NEW, ddata);
				ddata = new ActionDashboardData();
				dashData.put(DELETED, ddata);
				ddata = new ActionDashboardData();
				dashData.put(I2E, ddata);
				orgHashmap.put(org, dashData);
			}
			HashMap<String, ActionDashboardData> dashData = orgHashmap.get(org);
			ActionDashboardData ddata = dashData.get(NEW);
			int count = ddata.getNewUnknownCount();
			ddata.setNewUnknownCount(++count);
			dashData.put(NEW, ddata);
			orgHashmap.put(org, dashData);
		}
	}
	
	/**
	 * Add a new Org into hashmap or increment count
	 * 
	 * @param orgHashmap
	 * @param org
	 * @param audit
	 */
	private void classifyActionOrgCategoryForDeletedAccounts(HashMap<String, HashMap<String, ActionDashboardData>> orgHashmap, String org, AuditAccountVO audit) {
		
		if(isDeletedAccount(audit) && audit.getDeletedAction() != null &&
				!StringUtils.equalsIgnoreCase(audit.getDeletedUnsubmittedFlag(),"Y") &&
				StringUtils.equalsIgnoreCase(audit.getDeletedAction().getCode(), "Unknown")) {
			if(!containsActionKey(orgHashmap, org)){
				HashMap<String, ActionDashboardData> dashData = new HashMap<String, ActionDashboardData>();
				ActionDashboardData ddata = new ActionDashboardData();
	    		dashData.put(ACTIVE, ddata);
	    		ddata = new ActionDashboardData();
	        	dashData.put(NEW, ddata);
	        	ddata = new ActionDashboardData();
	        	dashData.put(DELETED, ddata);
	        	ddata = new ActionDashboardData();
	        	dashData.put(I2E, ddata);
	    		orgHashmap.put(org, dashData);
				
			}
			HashMap<String,ActionDashboardData> dashData = orgHashmap.get(org);
			ActionDashboardData ddata = dashData.get(DELETED);
    		int count = ddata.getDeletedUnknownCount();
    		ddata.setDeletedUnknownCount(++count);
    		dashData.put(DELETED, ddata);
    		orgHashmap.put(org, dashData);
		}
	}
	
	/**
	 * Add a new Org into hashmap or increment count
	 * 
	 * @param orgHashmap
	 * @param org
	 * @param audit
	 */
	private void classifyOrgCategory(HashMap<String, HashMap<String, DashboardData>> orgHashmap, String org, AuditI2eAccountVO audit) {
		if(containsKey(orgHashmap, org)){
			HashMap<String,DashboardData> dashData = orgHashmap.get(org);
			//calculate Count
			setCountForEachCategory(audit, dashData);
			orgHashmap.put(org, dashData);
		}else{
			HashMap<String, DashboardData> dashData = new HashMap<String, DashboardData>();
			//calculate count
			setFirstElementCountForEachCategory(audit, dashData);
			setCountForEachCategory(audit, dashData);
			orgHashmap.put(org, dashData);
		}
	}
	
	/**
	 * Add a new Org into hashmap or increment count
	 * 
	 * @param orgHashmap
	 * @param org
	 * @param audit
	 */
	private void classifyActionOrgCategory(HashMap<String, HashMap<String, ActionDashboardData>> orgHashmap, String org, AuditI2eAccountVO audit) {
		if(audit.getAction() != null &&
				!StringUtils.equalsIgnoreCase(audit.getUnsubmittedFlag(),"Y") &&
				StringUtils.equalsIgnoreCase(audit.getAction().getCode(), "Unknown")) {
			if(!containsActionKey(orgHashmap, org)){
				HashMap<String, ActionDashboardData> dashData = new HashMap<String, ActionDashboardData>();
				ActionDashboardData ddata = new ActionDashboardData();
	    		dashData.put(ACTIVE, ddata);
	    		ddata = new ActionDashboardData();
	        	dashData.put(NEW, ddata);
	        	ddata = new ActionDashboardData();
	        	dashData.put(DELETED, ddata);
	        	ddata = new ActionDashboardData();
	        	dashData.put(I2E, ddata);
	    		orgHashmap.put(org, dashData);
				
			}
			HashMap<String,ActionDashboardData> dashData = orgHashmap.get(org);
			ActionDashboardData ddata = dashData.get(I2E);
    		int count = ddata.getI2eUnknownCount();
    		ddata.setI2eUnknownCount(++count);
    		dashData.put(I2E, ddata);
    		orgHashmap.put(org, dashData);
		}
	}
	
	/**
	 * @return the orgKeys
	 */
	public List<Object> getOrgKeys() {
		return orgKeys;
	}
			


	/**
	 * @param orgKeys the orgKeys to set
	 */
	public void setOrgKeys(List<Object> orgKeys) {
		this.orgKeys = orgKeys;
	}


	/**
	 * @return the orgsData
	 */
	public HashMap<String, HashMap<String, DashboardData>> getOrgsData() {
		return orgsData;
	}


	/**
	 * @param orgsData the orgsData to set
	 */
	public void setOrgsData(HashMap<String, HashMap<String, DashboardData>> orgsData) {
		this.orgsData = orgsData;
	}


	/**
	 * @return the otherOrgsData
	 */
	public HashMap<String, HashMap<String, DashboardData>> getOtherOrgsData() {
		return otherOrgsData;
	}


	/**
	 * @param otherOrgsData the otherOrgsData to set
	 */
	public void setOtherOrgsData(
			HashMap<String, HashMap<String, DashboardData>> otherOrgsData) {
		this.otherOrgsData = otherOrgsData;
	}

	/**
	 * @return the actionOrgsData
	 */
	public HashMap<String, HashMap<String, ActionDashboardData>> getActionOrgsData() {
		return actionOrgsData;
	}


	/**
	 * @param actionOrgsData the actionOrgsData to set
	 */
	public void setActionOrgsData(
			HashMap<String, HashMap<String, ActionDashboardData>> actionOrgsData) {
		this.actionOrgsData = actionOrgsData;
	}
	
	/**
	 * @return the others
	 */
	public DashboardData getOthers() {
		return others;
	}


	/**
	 * @param others the others to set
	 */
	public void setOthers(DashboardData others) {
		this.others = others;
	}


	/**
	 * @return the otherOrgKeys
	 */
	public List<Object> getOtherOrgKeys() {
		return otherOrgKeys;
	}


	/**
	 * @param otherOrgKeys the otherOrgKeys to set
	 */
	public void setOtherOrgKeys(List<Object> otherOrgKeys) {
		this.otherOrgKeys = otherOrgKeys;
	}

	/**
	 * @return the actionOrgKeys
	 */
	public List<Object> getActionOrgKeys() {
		return actionOrgKeys;
	}


	/**
	 * @param actionOrgKeys the actionOrgKeys to set
	 */
	public void setActionOrgKeys(List<Object> actionOrgKeys) {
		this.actionOrgKeys = actionOrgKeys;
	}


	/**
	 * @return the emAuditsVO
	 */
	public EmAuditsVO getEmAuditsVO() {
		return emAuditsVO;
	}

	/**
	 * @param emAuditsVO the emAuditsVO to set
	 */
	public void setEmAuditsVO(EmAuditsVO emAuditsVO) {
		this.emAuditsVO = emAuditsVO;
	}
	
	/**
	 * @return the auditId
	 */
	public Long getAuditId() {
		return auditId;
	}

	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	/**
	 * @return the auditYear
	 */
	@SuppressWarnings("deprecation")
	public String getAuditYear() {
		emAuditsVO = adminService.retrieveCurrentOrLastAuditVO();
		Date auditDate = emAuditsVO.getStartDate();
		if(auditDate == null){
			return "";
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String audDate = sdf.format(auditDate);
			String[] yearStr = audDate.split("/");
			if(yearStr.length == 3){
				return yearStr[2];
			}else{
				return "";
			}
		}
	}

}
