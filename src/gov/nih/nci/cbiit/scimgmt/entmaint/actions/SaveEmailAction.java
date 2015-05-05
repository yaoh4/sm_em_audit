package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({ "serial", "deprecation" })
public class SaveEmailAction extends BaseAction {
	private Logger log = Logger.getLogger(SaveEmailAction.class);
	private InputStream inputStream;  
	@Autowired
	private EntMaintProperties properties;
	/**
	 * This method is used for AJAX call when the user wants to submit account information.
	 * @return String
	 */

	public String SaveEmail(){
		String content = request.getParameter("emailBody");
		content = content.replace("\"", "'");
		try{
				saveFile(content);
				inputStream = new StringBufferInputStream("success");
		}catch(Exception e){
			log.error(e.getMessage());
			inputStream = new StringBufferInputStream("fail");
		}
	
		return SUCCESS;
	}

	private void saveFile(String content) throws Exception{
		String path = properties.getPropertyValue("EMAIL_FILE");
		String fileName = path + File.separator + "email.txt";
		PrintWriter pw = new PrintWriter(new FileWriter(new File(fileName)));
		pw.print(content);
		pw.close();
	}
	
	public InputStream getInputStream() {    
		return inputStream;   
	}
}
