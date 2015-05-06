package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Map;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
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
	
	
	static Logger logger = Logger.getLogger(AdminAction.class);
	
	@Autowired
	protected AdminService adminService;
	@Autowired
	private EntMaintProperties properties;
	
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

}
