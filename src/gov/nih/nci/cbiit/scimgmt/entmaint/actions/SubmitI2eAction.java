package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.exceptions.ServiceDeniedException;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2eAuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;

import java.io.StringBufferInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class handles submit or unsubmit action (Complete or undo action).
 * @author 
 *
 */
@SuppressWarnings({ "serial", "deprecation" })
public class SubmitI2eAction extends BaseAction {
	private Logger log = Logger.getLogger(SubmitI2eAction.class);
	@Autowired
	private I2eAuditService i2eService;
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
		String transferOrg = (String)request.getParameter("transferOrg");
		AuditI2eAccountVO account = i2eService.getAuditAccountById(Long.parseLong(appId));
		try{
			if(ApplicationConstants.ACTIVE_ACTION_TRANSFER == Long.parseLong(actId)){
				i2eService.transfer(Long.parseLong(appId), account.getNihNetworkId(), account.getAuditId(), account.getParentNedOrgPath(), note, transferOrg , true);
				//Category is not available at this stage. Pass null now and compute later in the DAO.
				impac2Service.transfer(Long.parseLong(appId), account.getNihNetworkId(), account.getAuditId(), account.getParentNedOrgPath(), Long.parseLong(actId), note, transferOrg, null, false);
			}
			else{
				Date date = new Date();
				i2eService.submit(Long.parseLong(appId), Long.parseLong(actId), note, date);
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy h:mm a");
				String timeStr = sdf.format(date);
				String fullName = nciUser.getLastName() + ", " + nciUser.getFirstName();

				inputStream = new StringBufferInputStream(timeStr + ";" + fullName);
			}
		}catch(ServiceDeniedException e){
			log.error(e.getMessage());
			inputStream = new StringBufferInputStream("permission");
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
		
		i2eService.unsubmit(Long.parseLong(appId));
		
		try{
			i2eService.unsubmit(Long.parseLong(appId));

			inputStream = new StringBufferInputStream("success");
		}catch(ServiceDeniedException e){
			log.error(e.getMessage());
			inputStream = new StringBufferInputStream("permission");
		}catch(Exception e){
			log.error(e.getMessage());
			inputStream = new StringBufferInputStream("fail");
		}
		
		return SUCCESS;
	}
}
