package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DashboardData;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Action class for commands executed on the Admin tab.
 * 
 * @author menons2
 *
 */
@SuppressWarnings("serial")
public class AdminAction extends BaseAction {

  private static final int COMMENTS_FIELD_SIZE = 1000;

	protected EmAuditsVO emAuditsVO = new EmAuditsVO();
	
	protected boolean sendAuditNotice = false;	
	protected boolean disableInput = true;
	
	public static final String ACTIVE = "active";
	public static final String NEW = "new";
	public static final String DELETED = "deleted";
	public static final String INACTIVE = "inactive";
	
	static Logger logger = Logger.getLogger(AdminAction.class);
	
	@Autowired
	protected AdminService adminService;
	@Autowired
	private EntMaintProperties properties;
	@Autowired
	protected Impac2AuditService impac2AuditService;	
	
	//------------------------------
	//Attributes for Dashboard
	//-----------------------------------
	private List<Object> orgKeys;
	private List<Object> otherOrgKeys;
	private HashMap<String, HashMap<String, DashboardData>> orgsData;
	private HashMap<String, HashMap<String, DashboardData>> otherOrgsData;
	private DashboardData others;
	/**
	 * Invoked when the user clicks the Audit tab. Depending on
	 * the state of the Audit, the appropriate screen elements will be displayed.
	 *
     * @return String success if no errors.
     */
    public String execute() throws Exception {
        
    	//Retrieve current audit info from the DB 
    	emAuditsVO = adminService.retrieveCurrentAuditVO();
    			
    	//Store it into the session
    	setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO);
        
    	//Enable/disable the UI elements based on the audit state.
    	setupAuditDisplay(emAuditsVO);
        
        return ApplicationConstants.SUCCESS;
    }
    
    
    private void validateComments() {
    	//Comments cannot be longer than 1000 characters.
    	if(!StringUtils.isEmpty(emAuditsVO.getComments())) {
    		if(emAuditsVO.getComments().length() > COMMENTS_FIELD_SIZE) {
    			this.addActionError(getText("error.comments.size.exceeded"));  			
    		}
    	}
    }
    
    
    private void restoreStateOnError(boolean disableInput) {
    	if(this.hasErrors()) {
    		setDisableInput(disableInput);
    		setSendAuditNotice(false);
    		//Temporarily save the comments
    		String comments = emAuditsVO.getComments();
    		emAuditsVO = (EmAuditsVO)getAttributeFromSession(ApplicationConstants.CURRENT_AUDIT);
    		emAuditsVO.setComments(comments);
    	}
    }
    
    
    public void validate() {
    	validateComments();
    	restoreStateOnError(true);
    }
    
    
    /**
     * Validator for start audit action.
     * 
     */
    public void validateStartAudit() {
    	Map fieldErrors = null;
    	
    	if (hasFieldErrors()) {
    		fieldErrors = getFieldErrors();
    	}
    	
    	//If both the audits are false, then error
    	if(emAuditsVO.getImpac2AuditFlag().equals("false")) {
    		this.addActionError(getText("error.audittypes.empty"));
    	}
    
    	if(fieldErrors == null || !fieldErrors.containsKey("emAuditsVO.impaciiToDate")) {
    	if(emAuditsVO.getImpaciiToDate() == null) {
    		emAuditsVO.setImpaciiToDate(new Date());
    	} else if(emAuditsVO.getImpaciiToDate().after(new Date())) {
    		//End date cannot be in future
    		this.addActionError(getText("error.daterange.enddate.future"));
    	}
    	}
    	
    	if(fieldErrors == null || !fieldErrors.containsKey("emAuditsVO.impaciiFromDate")) {
    	//Start date cannot be null
    	if(emAuditsVO.getImpaciiFromDate() == null) {
    		this.addActionError(getText("error.daterange.startdate.empty"));
    	} else if (emAuditsVO.getImpaciiFromDate().after(new Date())) {
    		//Start date cannot be in future	
			this.addActionError(getText("error.daterange.startdate.future"));
    	} else if (emAuditsVO.getImpaciiFromDate().after(emAuditsVO.getImpaciiToDate())) {
    		//Start date cannot be after end date
    		this.addActionError(getText("error.daterange.outofsequence"));
    	}
    	}
    	
    	validateComments();
    	
    	if(this.hasErrors()) {
    		setDisableInput(false);
    		setSendAuditNotice(false);
    		emAuditsVO.setAuditState(ApplicationConstants.AUDIT_STATE_CODE_RESET);
    	}
    }
    
    
    /**
     * Invoked when the Start Audit button is clicked. Sets up the data
     * in the backend and enables the Audit. If success, takes the user 
     * o the End Audit sub-screen.
     * 
     * @return String success if no error.
     */
    public String startAudit() {
    	
    	logger.info("Setting up new Audit...");
    	//Store the audit info into the DB
    	Long auditId = adminService.setupNewAudit(emAuditsVO);
    	logger.info("Setup new Audit with auditId: " + auditId);
    	
    	//Retrieve the newly created audit from the DB
    	emAuditsVO = adminService.retrieveAuditVO(auditId);
    	
    	//Store it into the session
    	setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO);
    	
    	//Audit has started, so disable input
    	setDisableInput(true);
    	
    	//Open mail client
    	setSendAuditNotice(true);
    	  	
    	return ApplicationConstants.SUCCESS;
    }
    
    
    /**
     * Invoked when the End Audit button is clicked. Disables the Audit
     * If success, takes the use to the Enable/Reset screen. 
     * 
     * @return String success if no error.
     */
    public String endAudit() {
    	
    	return updateAudit(ApplicationConstants.AUDIT_STATE_CODE_DISABLED);
    }
    
    
    /**
     * Invoked when the Enable button is clicked. Enables the Audit.
     * If success, takes the user to the End Audit screen.
     * 
     * @return success if no error
     */
    public String enableAudit() {
    	
    	return updateAudit(ApplicationConstants.AUDIT_STATE_CODE_ENABLED);
    }
    
    public String openEmail() {
    	String content = "";
    	try{
    		content = readEmailContent();
    	}catch(Exception e){
    		logger.error("Failed to read email from email file.", e);
    	}
    	//content = StringEscapeUtils.escapeHtml(content);
    	request.setAttribute("emailContent", content);
    
    	return SUCCESS;
    }
    
    /**
     * Invoked when the the Reset button is clicked. Closes the Audit.
     * If success, takes the user to the Start Audit screen.
     * @return
     */
    public String resetAudit() {
    	
    	//Close the current Audit
    	logger.info("Closing current audit");
    	
    	Long auditId = adminService.closeCurrentAudit(emAuditsVO.getComments());
    	logger.info("Closed audit with auditId " + emAuditsVO.getId());
    	
    	//Retrieve the updated emAuditsVO from the DB
    	emAuditsVO = adminService.retrieveAuditVO(auditId);
    	
    	//Store it into the session
    	setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO);
    	
    	setDisableInput(false);
    	
    	return ApplicationConstants.SUCCESS;
    }
	/**
	 * This method provide a bridge to search audit page based on category, active, new, deleted, or inactive
	 */
    public String searchAudit(){
    	searchVO = (AuditSearchVO) session.get(ApplicationConstants.SEARCHVO);
    	String forward = "";
    	String cate = request.getParameter("cate");
    	String orgName = request.getParameter("orgName");
    	searchVO.setOrganization(orgName);
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
	    					setTotalCountForEachCategory(audit, dashData);
	    					otherOrgsData.put(org, dashData);
	    				}else{
	    					HashMap<String, DashboardData> dashData = new HashMap<String, DashboardData>();
	    					//calculate count
	    					setFirstElementTotalCountForEachCategory(audit, dashData);
	    					otherOrgsData.put(org, dashData);
	    				}
	    			}else{
	    				if(containsKey(orgsData, org)){
	    					HashMap<String,DashboardData> dashData = orgsData.get(org);
	    					//calculate Count
	    					setTotalCountForEachCategory(audit, dashData);
	    					orgsData.put(org, dashData);
	    				}else{
	    					HashMap<String, DashboardData> dashData = new HashMap<String, DashboardData>();
	    					//calculate count
	    					setFirstElementTotalCountForEachCategory(audit, dashData);
	    					orgsData.put(org, dashData);
	    				}
    				}
    			}
    		}
    	}
    	//calculate the total other orgs
    	others = calculateOther(otherOrgsData);
    	
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
    private void setTotalCountForEachCategory(AuditAccountVO audit, HashMap<String,DashboardData> dashData){
		
    	Long impaciiToDate = emAuditsVO.getImpaciiToDate().getTime();
    	Long impaciiFromDate = emAuditsVO.getImpaciiFromDate().getTime();
    	
    	//determine active account
    	if(audit.getCreatedDate() != null && (audit.getDeletedDate() == null || audit.getDeletedDate().getTime() > impaciiToDate) && audit.getCreatedDate().getTime() <= impaciiToDate){
    		calculateCountByCategory(audit, dashData, ACTIVE);
    		if(!StringUtils.isEmpty(audit.getActiveSubmittedBy())){
    			calculateCompletedCountByCategory(audit, dashData, ACTIVE);
    		}
    	}
    	//determine new account
    	if(audit.getCreatedDate() != null && audit.getCreatedDate().getTime() >= impaciiFromDate && audit.getCreatedDate().getTime() <=impaciiToDate){
    		calculateCountByCategory(audit, dashData, NEW);
    		if(!StringUtils.isEmpty(audit.getActiveSubmittedBy())){
    			calculateCompletedCountByCategory(audit, dashData, NEW);
    		}
    	}
    	//determine deleted account
    	if(audit.getDeletedDate() != null && audit.getDeletedDate().getTime() >= impaciiFromDate && audit.getDeletedDate().getTime() <= impaciiToDate){
    		calculateCountByCategory(audit, dashData, DELETED);
    		if(!StringUtils.isEmpty(audit.getActiveSubmittedBy())){
    			calculateCompletedCountByCategory(audit, dashData, DELETED);
    		}
    	}
    	//determine inactive account
    	if(audit.getInactiveUnsubmittedFlag() != null && audit.getInactiveUnsubmittedFlag().equalsIgnoreCase("Y")){
    		calculateCountByCategory(audit, dashData, INACTIVE);
    		if(!StringUtils.isEmpty(audit.getActiveSubmittedBy())){
    			calculateCompletedCountByCategory(audit, dashData, INACTIVE);
    		}
    	}
    }
    
    private void calculateCountByCategory(AuditAccountVO audit, HashMap<String,DashboardData> dashData, String category){
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
    
    private void calculateCompletedCountByCategory(AuditAccountVO audit, HashMap<String,DashboardData> dashData, String category){
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
    private void setFirstElementTotalCountForEachCategory(AuditAccountVO audit, HashMap<String,DashboardData> dashData){
    	DashboardData dash = new DashboardData();
    	dashData.put(ACTIVE, dash);
    	dash = new DashboardData();
    	dashData.put(NEW, dash);
    	dash = new DashboardData();
    	dashData.put(DELETED, dash);
    	dash = new DashboardData();
    	dashData.put(INACTIVE, dash);
    	setTotalCountForEachCategory(audit, dashData);
    }
    private String updateAudit(String auditState) {
    	
    	//Store the auditState into the DB
    	logger.debug("Updating current audit to state " + auditState);
    	Long auditId = adminService.updateCurrentAudit(
    		auditState, emAuditsVO.getComments());
    	logger.debug("Updated current audit with auditId " + emAuditsVO.getId());
    	
    	//Retrieve the updated emAuditsVO from the DB
    	emAuditsVO = adminService.retrieveAuditVO(auditId);
    	
    	//Store it into the session
    	setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO);
    	
    	setSendAuditNotice(false);
    	setDisableInput(true);
    	
    	return ApplicationConstants.SUCCESS;
    }
    
    
	private void setupAuditDisplay(EmAuditsVO emAuditsVO) {
		
		String auditState = EmAppUtil.getCurrentAuditState(emAuditsVO);
		
		switch(auditState) {
			case ApplicationConstants.AUDIT_STATE_CODE_RESET:
				setDisableInput(false);
				emAuditsVO.setImpaciiToDate(new Date());
				break;
				
			case ApplicationConstants.AUDIT_STATE_CODE_ENABLED:
			case ApplicationConstants.AUDIT_STATE_CODE_DISABLED:	
				setDisableInput(true);
				break;
				
			default:
				logger.error("Invalid audit state");
				break;
		}
	}

	/** 
	 * Calculate the other orgs total
	 */
	private DashboardData calculateOther(HashMap<String, HashMap<String, DashboardData>> otherOrgsMap){
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
	 * Checks to see if the email client should be opened in preparation
	 * for sending the Audit notice.
	 * 
	 * @return boolean true if audit notice should be sent, false otherwise. 
	 */
	public boolean isSendAuditNotice() {
		return sendAuditNotice;
	}

	
	/**
	 * Set the sendAuditNotice flag.
	 * 
	 * @param sendAuditNotice the sendAuditNotice to set
	 */
	public void setSendAuditNotice(boolean sendAuditNotice) {
		this.sendAuditNotice = sendAuditNotice;
	}


	/**
	 * Get the disableInput attribute.
	 * 
	 * @return the disableInput
	 */
	public boolean getDisableInput() {
		return disableInput;
	}

	
	/**
	 * Set the disableInput attribute.
	 * 
	 * @param disableInput the disableInput to set
	 */
	public void setDisableInput(boolean disableInput) {
		this.disableInput = disableInput;
	}

	
	/**
	 * Get the Audit info.
	 * 
	 * @return the emAuditsVO containing Audit info.
	 */
	public EmAuditsVO getEmAuditsVO() {
		return emAuditsVO;
	}

	
	/**
	 * Set Audit info.
	 * 
	 * @param emAuditsVO the emAuditsVO containing Audit info.
	 */
	public void setEmAuditsVO(EmAuditsVO emAuditsVO) {
		this.emAuditsVO = emAuditsVO;
	}

	/**
	 * Get the list of IC coordinator email addresses
	 * 
	 * @return String list of emails.
	 */
	public String getIcEmails() {
		return entMaintProperties.getPropertyValue(ApplicationConstants.IC_COORDINATOR_EMAIL);
	}

	private String readEmailContent() throws Exception{
		String fileName = properties.getPropertyValue("EMAIL_FILE") + File.separator + "email.txt";
		File f = new File(fileName);
		if(!f.exists()){
			return "";
		}else{
			BufferedReader bread = new BufferedReader(new FileReader(new File(fileName)));
			String content = bread.readLine();
			if(content == null){
				content = "";
			}
			bread.close();
			return content;	
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
	
}
