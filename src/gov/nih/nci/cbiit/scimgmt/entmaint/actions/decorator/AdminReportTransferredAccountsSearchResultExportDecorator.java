package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import org.apache.commons.lang.StringUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.TransferredAuditAccountsVO;

/**
 * This class is responsible for decorating the rows of Transferred accounts admin reports search results table.
 * @author tembharend
 *
 */

public class AdminReportTransferredAccountsSearchResultExportDecorator extends TableDecorator{
	
	@Autowired
	private LookupService lookupService;
	
	/**
	 * Get the full name.
	 * @return fullName
	 */
	public String getFullName() {
		TransferredAuditAccountsVO accountVO = (TransferredAuditAccountsVO)getCurrentRowObject();
		final StringBuffer sb = new StringBuffer("");
		if(!StringUtils.isBlank(accountVO.getLastName())){
			sb.append(accountVO.getLastName());
		}
		if(!StringUtils.isBlank(accountVO.getLastName()) && !StringUtils.isBlank(accountVO.getFirstName())){
			sb.append(", ");
		}
		if(!StringUtils.isBlank(accountVO.getFirstName())){
			sb.append(accountVO.getFirstName());
		}
		return StringUtils.remove(sb.toString(), "'");
	}	
	
	/**
	 *  Get ImpaciiActiveStatusFlag
	 * @return status
	 */
	public String getImpaciiActiveStatusFlag(){
		String status = "";
		TransferredAuditAccountsVO accountVO = (TransferredAuditAccountsVO)getCurrentRowObject();
		
		if(accountVO.getImpaciiActiveStatusFlag() == null){
			status = "NA";
			
		}else if(accountVO.getImpaciiActiveStatusFlag()){
			status = "Yes";
			
		}else{
			status="No";
		}

		return status;
	}
	
	/**
	 * Get I2eActiveStatusFlag
	 * @return status
	 */
	public String getI2eActiveStatusFlag(){
		String status = "";
		TransferredAuditAccountsVO accountVO = (TransferredAuditAccountsVO)getCurrentRowObject();
		
		if(accountVO.getI2eActiveStatusFlag() == null){
			status = "NA";
			
		}else if( accountVO.getI2eActiveStatusFlag()){
			status = "Yes";
			
		}else{
			status="No";
		}

		return status;
	}	
	
	/**
	 * Get TransferToOrgPath
	 * return orgPath
	 */
	public String getTransferToNedOrgPath(){
		String orgPath = "";
		TransferredAuditAccountsVO accountVO = (TransferredAuditAccountsVO)getCurrentRowObject();
		
		if("No".equalsIgnoreCase(getImpaciiActiveStatusFlag()) && StringUtils.isNotBlank(accountVO.getDeletedTransferToNedOrgPath())){
			orgPath = accountVO.getDeletedTransferToNedOrgPath();
		}
		else{
			orgPath = accountVO.getTransferToNedOrgPath();
		}
		return orgPath;
	}
		
	/**
	 * Get TransferFromNedOrgPath
	 * return orgPath
	 */
	public String getTransferFromNedOrgPath(){
		String orgPath = "";
		TransferredAuditAccountsVO accountVO = (TransferredAuditAccountsVO)getCurrentRowObject();
		
		if("No".equalsIgnoreCase(getImpaciiActiveStatusFlag()) && StringUtils.isNotBlank(accountVO.getDeletedTransferFromNedOrgPath())){
			orgPath = accountVO.getDeletedTransferFromNedOrgPath();
		}
		else{
			orgPath = accountVO.getTransferFromNedOrgPath();
		}
		return orgPath;
	}
	
}
