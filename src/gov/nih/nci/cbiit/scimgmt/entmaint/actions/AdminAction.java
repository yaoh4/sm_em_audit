package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

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
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

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
	protected Impac2AuditService impac2AuditService;	
	
	//------------------------------
	//Attributes for Dashboard
	//-----------------------------------
	private List<Object> orgKeys;
	private HashMap<String, HashMap<String, DashboardData>> orgsData;
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
    	
    	//Comments cannot be longer than 1000 characters.
    	if(!StringUtils.isEmpty(emAuditsVO.getComments())) {
    		if(emAuditsVO.getComments().length() > COMMENTS_FIELD_SIZE) {
    			this.addActionError(getText("error.comments.size.exceeded"));
    		}
    	}
    	
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
    
    
    /**
     * Invoked when the the Reset button is clicked. Closes the Audit.
     * If success, takes the user to the Start Audit screen.
     * @return
     */
    public String resetAudit() {
    	
    	logger.info("Closing current audit");
    	//Close the current Audit
    	adminService.closeCurrentAudit(emAuditsVO.getComments());
    	logger.info("Closed audit with auditId " + emAuditsVO.getId());
    	
    	
    	emAuditsVO.setAuditState(ApplicationConstants.AUDIT_STATE_CODE_RESET);
    	emAuditsVO.setImpaciiToDate(new Date());
    	
    	setDisableInput(false);
    	
    	//Remove attribute from session
    	removeAttributeFromSession(ApplicationConstants.CURRENT_AUDIT);
    	
    	return ApplicationConstants.SUCCESS;
    }
	
    /**
     * Set up all necessary component for dashboard. If success, takes the user to dashboard page. 
     * @param auditState
     * @return
     */
    public String gotoDashboard(){
    	orgsData = new HashMap<String, HashMap<String,DashboardData>>();
    	//set up all environment for displaying dashboard page.
    	emAuditsVO = (EmAuditsVO)getAttributeFromSession(ApplicationConstants.CURRENT_AUDIT);
    	Long auditId = emAuditsVO.getId();
    
    	List<AuditAccountVO> auditAccountVOs = impac2AuditService.getAllAccountsByAuditId(auditId);
    	if(auditAccountVOs != null && auditAccountVOs.size() > 0){
    		for(AuditAccountVO audit : auditAccountVOs){
//    			System.out.println("id : " + audit.getId());
//    			System.out.println("nedIC : " + audit.getNedIc());
//    			System.out.println("parentNedOrgPath: " + audit.getParentNedOrgPath());
    			System.out.println("nciDoc: " + audit.getNciDoc());
//    			System.out.println("create date: " + audit.getCreatedDate());
//    			System.out.println("deleted Date: " + audit.getDeletedDate());
//    			System.out.println("inactive user flag: " + audit.getInactiveUserFlag());
//    			System.out.println("active Submitted by: " + audit.getActiveSubmittedBy());
//    			System.out.println("new submitted by: " + audit.getNewSubmittedBy());
//    			System.out.println("deleted submitted by: " + audit.getDeletedSubmittedBy());
//    			System.out.println("Inactive Submitted by: " + audit.getInactiveSubmittedBy());
//    			System.out.println("=========================================");
    			String org = audit.getParentNedOrgPath();
    			if(org != null){
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

    	Set<String> keySet = orgsData.keySet();
    	Object[] keys = keySet.toArray();
    	Arrays.sort(keys);
    	//set arrays for displaying
    	orgKeys = Arrays.asList(keys);
//    	for( Object s : keys){
//    		System.out.println("====" + s);
//    		HashMap<String, DashboardData> dData = orgsData.get(s);
//    		System.out.println("***"+dData.get(ACTIVE).getActiveAccountCount());
//    		System.out.println("***"+dData.get(NEW).getNewAccountCount());
//    		System.out.println("***"+dData.get(DELETED).getDeletedAccountCount());
//    		System.out.println("***"+dData.get(INACTIVE).getInactiveAccountCount());
//    		System.out.println("---------------------");
//    	}
    
    	return ApplicationConstants.SUCCESS;
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
	
}
