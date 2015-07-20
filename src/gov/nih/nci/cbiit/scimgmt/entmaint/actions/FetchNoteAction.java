package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.io.StringBufferInputStream;

import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
public class FetchNoteAction extends BaseAction{
	private Logger log = Logger.getLogger(SubmitAction.class);
	@Autowired
	private Impac2AuditService impac2Service;
	@Autowired
	private Impac2PortfolioService impac2PortfolioService;
	
	
	@SuppressWarnings("deprecation")
	public String fetchAuditNote(){
		String appId = (String)request.getParameter("pId");
		String cate = (String)request.getParameter("cate");
		try{
			String note = impac2Service.getAuditNoteById(Long.parseLong(appId), cate);
			if(note == null){
				note = "";
			}
			inputStream = new StringBufferInputStream(note);
		}catch(Exception e){
			log.error("fetchAuditNote failed, id=" + appId +",  " +  e.getMessage(), e);
			inputStream = new StringBufferInputStream("Unable to find the Audit Account Note.");
		}
		
		return SUCCESS;
	}
	
	public String fetchPortfolioNote(){
		String appId = (String)request.getParameter("pId");
		try{
			String note = impac2PortfolioService.getPortfolioNoteById(appId);
			if(note == null){
				note = "";
			}
			inputStream = new StringBufferInputStream(note);
		}catch(Exception e){
			log.error("fetchAuditNote failed, id=" + appId +",  " +  e.getMessage(), e);
			inputStream = new StringBufferInputStream("Unable to find the Audit Account Note.");
		}
		
		return SUCCESS;
	}
}
