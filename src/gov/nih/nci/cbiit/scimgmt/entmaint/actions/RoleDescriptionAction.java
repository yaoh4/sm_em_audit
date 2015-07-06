package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;

import java.io.StringBufferInputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class is for the handling role description. It will be invoked by AJax calls.
 * @author zhoujim
 *
 */
@SuppressWarnings({ "serial", "deprecation" })
public class RoleDescriptionAction extends BaseAction {
	
	private Logger log = Logger.getLogger(RoleDescriptionAction.class);

	@Autowired
	private LookupService lookupService;
	
	/**
	 * This method is handling the role description retrieval. It will throw exception if there is an error occurs in lookupSerivce. Ajax will 
	 * Handle the exception 
	 */
			
	public String execute() throws Exception { 
		String roleName = (String)request.getParameter("rId");
		String roleDesc = lookupService.getRoleDescription(roleName);
		if(roleDesc == null){
			roleDesc = "Unable to find the Role Description.";
		}
		inputStream = new StringBufferInputStream(roleDesc);
		return SUCCESS;  
	}
}
