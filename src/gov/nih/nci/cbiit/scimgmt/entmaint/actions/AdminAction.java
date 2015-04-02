package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.util.Date;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

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

   
	protected EmAuditsVO emAuditsVO = new EmAuditsVO();
	
	protected boolean sendAuditNotice = false;	
	protected boolean disableInput = true;
	
	
	static Logger logger = Logger.getLogger(AdminAction.class);
	
	@Autowired
	protected AdminService adminService;
	
	
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
    	//If both the audits are false, then error
    	if(emAuditsVO.getImpac2AuditFlag().equals("false")) {
    		this.addActionError(getText("error.audittypes.empty"));
    	}
    
    	//If either of the dates are not present, then error
    	if(emAuditsVO.getImpaciiFromDate() == null) {
    		this.addActionError(getText("error.daterange.startdate.empty"));
    	}  	  	
    	if(emAuditsVO.getImpaciiToDate() == null) {
    		this.addActionError(getText("error.daterange.enddate.empty"));
    	}
    	
    	//If either of the dates are in the future, then error
    	if(emAuditsVO.getImpaciiFromDate().after(new Date())) {
    		this.addActionError(getText("error.daterange.startdate.future"));
    	} 
    	if(emAuditsVO.getImpaciiToDate().after(new Date())) {
    		this.addActionError(getText("error.daterange.enddate.future"));
    	}
    	   	
    	//Audit End Date should be after Audit Start Date
    	if(emAuditsVO.getImpaciiFromDate() != null && emAuditsVO.getImpaciiToDate() != null
    			&& emAuditsVO.getImpaciiFromDate().after(emAuditsVO.getImpaciiToDate())) {
    		this.addActionError(getText("error.daterange.outofsequence"));
    	}
    	
    	if(this.hasActionErrors()) {
    		setDisableInput(false);
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
    	
    	//Store the audit info into the DB
    	Long id = adminService.setupNewAudit(emAuditsVO);
    	
    	//Retrieve the newly created audit from the DB
    	emAuditsVO = adminService.retrieveAuditVO(id);
    	
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
    	
    	//Close the current Audit
    	adminService.closeCurrentAudit();
    	
    	emAuditsVO.setAuditState(ApplicationConstants.AUDIT_STATE_CODE_RESET);
    	
    	setDisableInput(false);
    	
    	//Remove attribute from session
    	removeAttributeFromSession(ApplicationConstants.CURRENT_AUDIT);
    	
    	return ApplicationConstants.SUCCESS;
    }
	
    
    private String updateAudit(String auditState) {
    	
    	//Store the auditState into the DB
    	Long auditId = adminService.updateCurrentAudit(
    		auditState, emAuditsVO.getComments());
    	
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

}
