package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2eAuditService;
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
		AuditI2eAccountVO account = i2eService.getAuditAccountById(Long.parseLong(appId));
		try{
			Date date = new Date();
			i2eService.submit(Long.parseLong(appId), Long.parseLong(actId), note, date);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy h:mm a");
			String timeStr = sdf.format(date);
			String fullName = nciUser.getLastName() + ", " + nciUser.getFirstName();

			inputStream = new StringBufferInputStream(timeStr + ";" + fullName);
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
		}catch(Exception e){
			log.error(e.getMessage());
			inputStream = new StringBufferInputStream("fail");
		}
		
		return SUCCESS;
	}
}
