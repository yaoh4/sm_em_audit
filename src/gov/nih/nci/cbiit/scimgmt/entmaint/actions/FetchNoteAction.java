package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.io.StringBufferInputStream;

import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
public class FetchNoteAction extends BaseAction{
	private Logger log = Logger.getLogger(SubmitAction.class);
	@Autowired
	private Impac2AuditService impac2Service;
	
	@SuppressWarnings("deprecation")
	public String fetchAuditNote(){
		String appId = (String)request.getParameter("pId");
		String cate = (String)request.getParameter("cate");
		String note = impac2Service.getAuditNoteById(Long.parseLong(appId), cate);
		inputStream = new StringBufferInputStream(note);
		
		return SUCCESS;
	}
}
