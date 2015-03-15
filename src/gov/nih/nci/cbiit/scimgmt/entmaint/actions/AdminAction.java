package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


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
	 * the state of the Audit, user will be redirected to the appropriate sub-screen.
	 * If the Audit has started, user will be taken to the End Audit sub-screen
	 * If the Audit has ended, user will be taken to the Enable Audit/Reset sub-screen
	 * If the Audit has closed, user will be taken to the Start Audit sub-screen
	 *
     * @see also - com.opensymphony.xwork2.ActionSupport#execute()
     */
    public String execute() throws Exception {
        
    	//Retrieves audit info from the DB 
    	emAuditsVO = adminService.retrieveCurrentAuditVO();
    			
    	//Store it into the session
    	setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO);
        
    	//Enable/disable the UI elements based on the audit state.
    	setupAuditDisplay(emAuditsVO);
        
        return ApplicationConstants.SUCCESS;
    }
    
    
    /**
     * Invoked when the Start Audit button is clicked. 
     * Setups up the data in the backend, enables display of the audit tab, and pops up
     * an email screen with to field, subject field and body populated.
     * If success, takes the user to the End Audit sub-screen.
     * @return
     */
    public String startAudit() {
    	
    	//If both the audits are false, then error
    	if(emAuditsVO.getImpac2AuditFlag().equals("false")) {
    		this.addActionError(getText("error.audit.type"));
    	}
    
    	//If either of the dates are not present, then error
    	if(emAuditsVO.getImpaciiFromDate() == null) {
    		this.addActionError(getText("error.audit.start"));
    	}
    	
    	//If either of the dates are not present, then error
    	if(emAuditsVO.getImpaciiToDate() == null) {
    		this.addActionError(getText("error.audit.end"));
    	}
    	
    	//Audit End Date should be after Audit Start Date
    	if(emAuditsVO.getImpaciiFromDate() != null && emAuditsVO.getImpaciiToDate() != null
    			&& emAuditsVO.getImpaciiFromDate().after(emAuditsVO.getImpaciiToDate())) {
    		this.addActionError(getText("error.audit.dates"));
    	}
    	
    	if(this.hasActionErrors()) {
    		setDisableInput(false);
    		emAuditsVO.setAuditState(ApplicationConstants.AUDIT_STATE_CODE_RESET);
    		return ApplicationConstants.INPUT;
    	}
    	
    	//Store the audit info into the DB
    	Long id = adminService.setupNewAudit(emAuditsVO);
    	
    	//Retrieve the newly created audit from the DB
    	emAuditsVO = adminService.retrieveAuditVO(id);
    	
    	//Store it into the session
    	setAttributeInSession(ApplicationConstants.CURRENT_AUDIT, emAuditsVO);
    	
    	//Audit has started, so disable input
    	setDisableInput(true);
    	
    	setSendAuditNotice(true);
    	
    	//Forward to the End Audit screen
    	return ApplicationConstants.SUCCESS;
    }
    
    
    /**
     * Invoked when the End Audit button is clicked.
     * Disables the Audit tab.
     * If success, takes the user to the Enable/Reset Audit screen.
     * @return
     */
    public String endAudit() {
    	
    	return updateAudit(ApplicationConstants.AUDIT_STATE_CODE_DISABLED);
    }
    
    
    /**
     * Invoked when the Enable button is clicked.
     * Enables the Audit tab.
     * If success, takes the user to the End Audit screen.
     * @return
     */
    public String enableAudit() {
    	
    	return updateAudit(ApplicationConstants.AUDIT_STATE_CODE_ENABLED);
    }
    
    
    /**
     * Invoked when the the Reset button is clicked.
     * Removes the data setup for Audit.
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
    	
    	//Forward to Start Audit Screen
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
    	
    	//Forward to the End Audit screen.
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
	 * @return the sendAuditNotice
	 */
	public boolean isSendAuditNotice() {
		return sendAuditNotice;
	}

	
	/**
	 * @param sendAuditNotice the sendAuditNotice to set
	 */
	public void setSendAuditNotice(boolean sendAuditNotice) {
		this.sendAuditNotice = sendAuditNotice;
	}


	/**
	 * @return the disableInput
	 */
	public boolean getDisableInput() {
		return disableInput;
	}

	
	/**
	 * @param disableInput the disableInput to set
	 */
	public void setDisableInput(boolean disableInput) {
		this.disableInput = disableInput;
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


}
