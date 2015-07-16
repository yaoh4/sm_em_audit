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
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DashboardData;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Action class for commands executed on the Admin tab.
 * 
 * @author menons2
 *
 */
@SuppressWarnings("serial")
public class AdminDashboardAction extends BaseAction {
	protected EmAuditsVO emAuditsVO = new EmAuditsVO();
	
	private static final String ACTIVE = "active";
	private static final String NEW = "new";
	private static final String DELETED = "deleted";
	private static final String INACTIVE = "inactive";
	
	//------------------------------
	//Attributes for Dashboard
	//-----------------------------------
	private List<Object> orgKeys;
	private List<Object> otherOrgKeys;
	private HashMap<String, HashMap<String, DashboardData>> orgsData;
	private HashMap<String, HashMap<String, DashboardData>> otherOrgsData;
	private DashboardData others;
	private String auditYear;
	
	@Autowired
	protected Impac2AuditService impac2AuditService;	
	
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
    	orgsData = new HashMap<String, HashMap<String,DashboardData>>();
    	otherOrgsData = new HashMap<String, HashMap<String, DashboardData>>();
    	
    	//set up all environment for displaying dashboard page.
    	emAuditsVO = (EmAuditsVO)getAttributeFromSession(ApplicationConstants.CURRENT_AUDIT);
    	Long auditId = emAuditsVO.getId();
    
    	List<AuditAccountVO> auditAccountVOs = impac2AuditService.getAllAccountsByAuditId(auditId);
    	int i = 0;
    	if(auditAccountVOs != null && auditAccountVOs.size() > 0){
    		for(AuditAccountVO audit : auditAccountVOs){
    			String org = audit.getParentNedOrgPath();
    			String nciDoc = audit.getNciDoc();
    			String nedIc = audit.getNedIc();
    			if(org != null){
    				if(nciDoc != null && nciDoc.equalsIgnoreCase(ApplicationConstants.NCI_DOC_OTHER)){
    					if(nedIc != null && ApplicationConstants.NED_IC_NCI.equalsIgnoreCase(nedIc) == false){
							org = ApplicationConstants.ORG_PATH_NON_NCI;
						}
    					if(containsKey(otherOrgsData, org)){
    						HashMap<String,DashboardData> dashData = otherOrgsData.get(org);
	    					//calculate Count
	    					setCountForEachCategory(audit, dashData);
	    					otherOrgsData.put(org, dashData);
	    				}else{
	    					HashMap<String, DashboardData> dashData = new HashMap<String, DashboardData>();
	    					//calculate count
	    					setFirstElementCountForEachCategory(audit, dashData);
	    					otherOrgsData.put(org, dashData);
	    				}
	    			}else{
	    				if(containsKey(orgsData, org)){
	    					HashMap<String,DashboardData> dashData = orgsData.get(org);
	    					//calculate Count
	    					setCountForEachCategory(audit, dashData);
	    					orgsData.put(org, dashData);
	    				}else{
	    					HashMap<String, DashboardData> dashData = new HashMap<String, DashboardData>();
	    					//calculate count
	    					setFirstElementCountForEachCategory(audit, dashData);
	    					orgsData.put(org, dashData);
	    				}
    				}
    			}
    		}
    	}
    	//calculate the total other orgs
    	others = incrementCountOther(otherOrgsData);
    	
    	Set<String> keySet = orgsData.keySet();
    	Object[] keys = keySet.toArray();
    	Arrays.sort(keys);
    	
    	Set<String> otherKeySet = otherOrgsData.keySet();
    	Object[] otherKeys = otherKeySet.toArray();
    	Arrays.sort(otherKeys);
    	
    	//set arrays for displaying
    	orgKeys = Arrays.asList(keys);
    	otherOrgKeys = Arrays.asList(otherKeys);
    	//move NON-NCI to the last element
    	otherOrgKeys = moveNonNCIToLast();
    	return ApplicationConstants.SUCCESS;
    }
    
    /**
	 * This method provide a bridge to search audit page based on category, active, new, deleted, or inactive
	 */
    public String searchAudit(){
    	searchVO = (AuditSearchVO) session.get(ApplicationConstants.SEARCHVO);
    	if(searchVO == null){
    		searchVO = new AuditSearchVO();
    	}
    	String forward = "";
    	String cate = request.getParameter("cate");
    	String orgName = request.getParameter("orgName");
    	searchVO.setOrganization(orgName);
    	searchVO.setExcludeNCIOrgs(false);
    	session.put(ApplicationConstants.SEARCHVO, searchVO);
    	if(cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_ACTIVE)){
    		forward = ApplicationConstants.CATEGORY_ACTIVE;
    	}else if(cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_NEW)){
    		forward = ApplicationConstants.CATEGORY_NEW;
    	}else if(cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_DELETED)){
    		forward = ApplicationConstants.CATEGORY_DELETED;
    	}else if(cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_INACTIVE)){
    		forward = ApplicationConstants.CATEGORY_INACTIVE;
    	}
    	
    	return forward;
    }
    
    /**
     * Move NON NCI to be last element
     */
    private List<Object> moveNonNCIToLast(){
    	List<Object> sortedList = new ArrayList<Object>();
    	for(Object s : otherOrgKeys){
    		String key = (String)s;
    		if(key.equalsIgnoreCase(ApplicationConstants.ORG_PATH_NON_NCI)){
    			continue;
    		}else{
    			sortedList.add(s);
    		}
    	}
    	sortedList.add(ApplicationConstants.ORG_PATH_NON_NCI);
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
     * Calculate counts, including total, uncompleted numbers sorted by organization names. The other organization needs to computer seperately.
     * @param audit
     * @param dashData
     */
    private void setCountForEachCategory(AuditAccountVO audit, HashMap<String,DashboardData> dashData){
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	
	   	Date impaciiToDate = emAuditsVO.getImpaciiToDate();
    	Date impaciiFromDate = emAuditsVO.getImpaciiFromDate();
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
    		incrementCountByCategory(audit, dashData, ACTIVE);
    		if(!StringUtils.isEmpty(audit.getActiveSubmittedBy())){
    			incrementCompletedCountByCategory(audit, dashData, ACTIVE);
    		}
    	}
    	//determine new account
    	if(createdDate != null && 
    			(createdDate.after(impaciiFromDate) || createdDate.equals(impaciiFromDate)) && 
    			(createdDate.before(impaciiToDate) || createdDate.equals(impaciiToDate))){
    		incrementCountByCategory(audit, dashData, NEW);
    		if(!StringUtils.isEmpty(audit.getActiveSubmittedBy())){
    			incrementCompletedCountByCategory(audit, dashData, NEW);
    		}
    	}
    	//determine deleted account
    	if(deletedDate != null && 
    			(deletedDate.after(impaciiFromDate) || deletedDate.equals(impaciiFromDate)) && 
    			(deletedDate.before(impaciiToDate) || deletedDate.equals(impaciiToDate))){
    		incrementCountByCategory(audit, dashData, DELETED);
    		if(!StringUtils.isEmpty(audit.getActiveSubmittedBy())){
    			incrementCompletedCountByCategory(audit, dashData, DELETED);
    		}
    	}
    	//determine inactive account
    	if(audit.getInactiveUnsubmittedFlag() != null && audit.getInactiveUnsubmittedFlag().equalsIgnoreCase("Y")){
    		incrementCountByCategory(audit, dashData, INACTIVE);
    		if(!StringUtils.isEmpty(audit.getActiveSubmittedBy())){
    			incrementCompletedCountByCategory(audit, dashData, INACTIVE);
    		}
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
    
    /**
     * First map element, initial all categories with hashmap<String, DashboardData, make sure next computing will not get nullPoint.
     * All categories include ACTIVE, NEW, DELETED, INACTIVE.
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
    	setCountForEachCategory(audit, dashData);
    }
    
    
	/** 
	 * Calculate the other orgs total
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
			DashboardData newData = tempMap.get(NEW);
			otherTotalData.setNewAccountCount(otherTotalData.getNewAccountCount() + newData.getNewAccountCount());
			DashboardData deleteData = tempMap.get(DELETED);
			otherTotalData.setDeletedAccountCount(otherTotalData.getDeletedAccountCount() + deleteData.getDeletedAccountCount());
			DashboardData InactData = tempMap.get(INACTIVE);
			otherTotalData.setInactiveAccountCount(otherTotalData.getInactiveAccountCount() + InactData.getInactiveAccountCount());

		}
		return otherTotalData;
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
	 * @return the auditYear
	 */
	@SuppressWarnings("deprecation")
	public String getAuditYear() {
		emAuditsVO = (EmAuditsVO)getAttributeFromSession(ApplicationConstants.CURRENT_AUDIT);
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
