package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cbiit.scimgmt.entmaint.dao.ReportsDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.TransferredAuditAccountsVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.ReportService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;


/**
 * Interface that provides all the APIs related to Admin Reports
 * @author tembharend
 */
@Component
public class ReportServiceImpl implements ReportService {

	public static Logger log = Logger.getLogger(ReportServiceImpl.class);
	
	@Autowired
	private ReportsDAO reportsDAO;
		
	/**
	 * Search Transferred accounts
	 * @param paginatedList
	 * @param searchVO
	 * @param all
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PaginatedListImpl<TransferredAuditAccountsVO> searchTransferredAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) {
		paginatedList = reportsDAO.searchTransferredAccounts(paginatedList, searchVO, all);		
		return paginatedList;
	}  

}
