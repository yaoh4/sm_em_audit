package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
public class SubmitAction extends BaseAction {
	private Logger log = Logger.getLogger(SubmitAction.class);
	@Autowired
	private Impac2AuditService impac2Service;
	
	
	/**
	 * This method is used for AJAX call when the user wants to submit account information.
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String submitChange(){
		String appId = (String)request.getParameter("pId");
		String actId = (String)request.getParameter("aId");
		String note = (String)request.getParameter("note");
		String cate = (String)request.getParameter("cate");
		PrintWriter pw = null;
		//if category is inactive, need to do validation.
		//if active action ID is 2, and inactive action id is 13 error out.
		//error message: The below action conflicts with the action specified in teh Active Tab
		try{
			Date date = new Date();
			DBResult dbResult = impac2Service.submit(cate, Long.parseLong(appId), Long.parseLong(actId), note, date);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm a");
			String timeStr = sdf.format(date);
			String fullName = nciUser.getLastName() +", " + nciUser.getFirstName();
			if(dbResult.getStatus().equalsIgnoreCase(DBResult.FAILURE)){
				log.error("Exception occurs during saving data into EmAuditAccountActivityT table.");
				throw new Exception("Exception occurs during saving data into EmAuditAccountActivityT table.");
			}
			pw = response.getWriter();
			pw.print(timeStr+";"+fullName);
		}catch(Exception e){
			log.error(e.getMessage());
			pw.print("fail");
		}finally{
			pw.close();
		}
	
		return null;
	}
	
	/**
	 * This method is used for AJAX call when the user wants to submit account information.
	 * @return String
	 */
	public String unSubmitChange(){
		String appId = (String)request.getParameter("pId");
		String cate = (String)request.getParameter("cate");
		
		impac2Service.unsubmit(cate,Long.parseLong(appId));
		
		PrintWriter pw = null;
		try{
			impac2Service.unsubmit(cate,Long.parseLong(appId));
			//DBResult has not been implemented. Wait for Yuri
			pw = response.getWriter();
			pw.print("success");
		}catch(Exception e){
			log.error(e.getMessage());
			pw.print("fail");
		}finally{
			pw.close();
		}
		
		return null;
	}

}
