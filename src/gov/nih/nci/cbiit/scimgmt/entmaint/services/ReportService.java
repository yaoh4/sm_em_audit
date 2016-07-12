package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.TransferredAuditAccountsVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;


/**
 * Interface that provides all the APIs related to Admin Reports
 * @author tembharend
 */
public interface ReportService {
    
	/**
	 * Search Transferred accounts
	 * @param paginatedList
	 * @param searchVO
	 * @param all
	 * @return
	 * @throws Exception 
	 */
    @SuppressWarnings("rawtypes")
	public PaginatedListImpl<TransferredAuditAccountsVO> searchTransferredAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) throws Exception;    
    
}
