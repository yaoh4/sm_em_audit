package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class handles submit or unsubmit action (Complete or undo action).
 * @author zhoujim
 *
 */
@SuppressWarnings("serial")
public class SubmitAction extends BaseAction {
	private Logger log = Logger.getLogger(SubmitAction.class);
	@Autowired
	private Impac2AuditService impac2Service;
	@Autowired
	protected EntMaintProperties entMaintProperties;
	/**
	 * This method is used for AJAX call when the user wants to submit account information.
	 * @return String
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public String submitChange(){
		String appId = (String)request.getParameter("pId");
		String actId = (String)request.getParameter("aId");
		String note = (String)request.getParameter("note");
		String cate = (String)request.getParameter("cate");
		EmAuditAccountsVw account = impac2Service.getAuditAccountById(Long.parseLong(appId));
		try{
			//if category is inactive, need to do validation.
			//if active action ID is 2, and inactive action id is 13 error out.
			//error message: The below action conflicts with the action specified in the Active Tab
			if(account != null &&  cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_INACTIVE) && ApplicationConstants.FLAG_NO.equalsIgnoreCase(account.getActiveUnsubmittedFlag()) 
					&& account.getActiveAction() != null &&  account.getActiveAction().getId() != null && account.getActiveAction().getId() == ApplicationConstants.VERIFIEDLEAVEINT 
					&& actId.equalsIgnoreCase(ApplicationConstants.NOTNEED)){
				String errorMessage = entMaintProperties.getPropertyValue("inactive.validation.error");
				inputStream = new StringBufferInputStream("validationError;" + errorMessage);
			}else if(account != null &&  cate.equalsIgnoreCase(ApplicationConstants.CATEGORY_ACTIVE) && ApplicationConstants.FLAG_NO.equalsIgnoreCase(account.getInactiveUnsubmittedFlag())
					&& account.getInactiveAction() != null &&  account.getInactiveAction().getId() != null && account.getInactiveAction().getId() == ApplicationConstants.NOTNEEDINT
					&& actId.equalsIgnoreCase(ApplicationConstants.VERIFIEDLEAVE)){
				String errorMessage = entMaintProperties.getPropertyValue("active.validation.error");
				inputStream = new StringBufferInputStream("validationError;" + errorMessage);
			}else{
				//normal submit action 
				Date date = new Date();
				impac2Service.submit(cate, Long.parseLong(appId), Long.parseLong(actId), note, date);
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy h:mm a");
				String timeStr = sdf.format(date);
				String fullName = nciUser.getLastName() +", " + nciUser.getFirstName();
				
				inputStream = new StringBufferInputStream(timeStr+";"+fullName);
			}
		}catch(Exception e){
			log.error(e.getMessage());
			inputStream = new StringBufferInputStream("fail");
		}
	
		return SUCCESS;
	}
	
	/**
	 * This method is used for AJAX call when the user wants to submit account information.
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public String unSubmitChange(){
		String appId = (String)request.getParameter("pId");
		String cate = (String)request.getParameter("cate");
		
		impac2Service.unsubmit(cate,Long.parseLong(appId));
		
		try{
			impac2Service.unsubmit(cate,Long.parseLong(appId));
			//DBResult has not been implemented. Wait for Yuri
			
			inputStream = new StringBufferInputStream("success");
		}catch(Exception e){
			log.error(e.getMessage());
			inputStream = new StringBufferInputStream("fail");
		}
		
		return SUCCESS;
	}
}
