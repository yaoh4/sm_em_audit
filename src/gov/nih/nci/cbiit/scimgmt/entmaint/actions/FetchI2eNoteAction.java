package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.io.StringBufferInputStream;

import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2ePortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
public class FetchI2eNoteAction extends BaseAction{
	private Logger log = Logger.getLogger(SubmitAction.class);
	@Autowired
	private I2ePortfolioService i2ePortfolioService;
	
	
	
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
