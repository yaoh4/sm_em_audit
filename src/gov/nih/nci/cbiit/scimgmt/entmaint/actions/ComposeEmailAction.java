package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({ "serial", "deprecation" })
public class ComposeEmailAction extends BaseAction {
	private Logger log = Logger.getLogger(ComposeEmailAction.class);
	@Autowired
	private Impac2AuditService impac2Service;
	private InputStream inputStream;  
	@Autowired
	protected EntMaintProperties entMaintProperties;
	/**
	 * This method is used for AJAX call when the user wants to submit account information.
	 * @return String
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public String composeEmailSubjectAndBody(){
		String subject = entMaintProperties.getPropertyValue("email.subject");
		String cc = entMaintProperties.getPropertyValue("email.cc");
		try{
				inputStream = new StringBufferInputStream(subject + "|" + cc + "|" + getEmailBodyFromVelocityTemplate());
		}catch(Exception e){
			log.error(e.getMessage());
			inputStream = new StringBufferInputStream("fail");
		}
	
		return SUCCESS;
	}
	
	private String getEmailBodyFromVelocityTemplate(){
		StringBuilder message = null;
		BufferedReader br = null;
		String bodyText = "";
		
		VelocityContext ctx = new VelocityContext();
		InputStream sourceStream =  this.getClass().getClassLoader().getResourceAsStream("EMEmailTemplate.vm");
		
		try {
			br = new BufferedReader(new InputStreamReader(sourceStream));
			message = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				message.append(line);
				line = br.readLine();
			}
			
			Velocity.init();
//			ctx.put("userId", userId);
//			ctx.put("item", GpmatsApplicationConstants.PROGRAMDIRECTOR);
//			ctx.put("summaries", missingPDList);
			StringWriter writer = new StringWriter();
			Velocity.evaluate(ctx, writer, "log tag name", message.toString());
			bodyText = writer.toString();
			bodyText = bodyText.replace("\n", "");
		} catch (Exception e) {
			bodyText = "";
			e.printStackTrace();
			log.error("Cannot read velocity templates");
		}finally{
			try {
				br.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return bodyText;
	}

	public InputStream getInputStream() {    
		return inputStream;   
	}
}
