package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.io.StringBufferInputStream;

import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2eAuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2ePortfolioService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({ "serial", "deprecation" })
public class FetchI2eNoteAction extends BaseAction{
	private Logger log = Logger.getLogger(SubmitAction.class);
	@Autowired
	private I2ePortfolioService i2ePortfolioService;
	@Autowired
	private I2eAuditService i2eAuditService;
	
	public String fetchAuditNote(){
		String appId = (String)request.getParameter("pId");
		try{
			String note = i2eAuditService.getAuditNoteById(Long.parseLong(appId));
			if(note == null){
				note = "";
			}
			inputStream = new StringBufferInputStream(note);
		}catch(Exception e){
			log.error("fetchAuditNote failed, id=" + appId +",  " +  e.getMessage(), e);
			inputStream = new StringBufferInputStream("Unable to find the I2e Audit Account Note.");
		}
		
		return SUCCESS;
	}
	
	@SuppressWarnings("deprecation")
	public String fetchPortfolioNote(){
		Long id = null;
		String appId = (String)request.getParameter("pId");
		if(appId == null){
			return "";
		}
		id = Long.parseLong(appId);
		try{
			String note = i2ePortfolioService.getPortfolioNoteById(id);
			if(note == null){
				note = "";
			}
			inputStream = new StringBufferInputStream(note);
		}catch(Exception e){
			log.error("fetchAuditNote failed, id=" + appId +",  " +  e.getMessage(), e);
			inputStream = new StringBufferInputStream("Unable to find the I2E Portfolio Note.");
		}
		
		return SUCCESS;
	}
}
