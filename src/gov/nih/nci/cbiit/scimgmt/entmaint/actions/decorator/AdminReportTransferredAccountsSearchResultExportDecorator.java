package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import org.apache.commons.lang.StringUtils;
import org.displaytag.decorator.TableDecorator;

import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.TransferredAuditAccountsVO;

/**
 * This class is responsible for decorating the rows of Transferred accounts admin reports search results table.
 * @author tembharend
 *
 */

public class AdminReportTransferredAccountsSearchResultExportDecorator extends TableDecorator{
	
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
		if(accountVO.getImpaciiActiveStatusFlag() != null){
			if(accountVO.getImpaciiActiveStatusFlag()){
				status = "Yes";
			}
			else{
				status="No";
			}
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
		if(accountVO.getI2eActiveStatusFlag() != null){
			if( accountVO.getI2eActiveStatusFlag()){
				status = "Yes";
			}
			else{
				status="No";
			}
		}
		return status;
	}	
}
