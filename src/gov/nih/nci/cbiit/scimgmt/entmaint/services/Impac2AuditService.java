package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import java.util.Date;
import java.util.List;


/**
 * interface that provides all the APIs related to Impac2Audit
 */
public interface Impac2AuditService {

    /**
     * Data retrieval for active accounts
     * @return list of EmAuditAccountsVw
     */
    public PaginatedListImpl<AuditAccountVO> searchActiveAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO);
    
    /**
     * Data retrieval for new accounts
     * @return list of EmAuditAccountsVw
     */
    public PaginatedListImpl<AuditAccountVO> searchNewAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO);
    
    /**
     * Data retrieval for deleted accounts
     * @return list of EmAuditAccountsVw
     */
    public PaginatedListImpl<AuditAccountVO> searchDeletedAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO);
    
    /**
     * Data retrieval for inactive > 130 days
     * @return list of EmAuditAccountsVw
     */
    public PaginatedListImpl<AuditAccountVO> searchInactiveAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO);

    /**
     * Update actions taken on account for submit.
     * @param category
     * @param eaaId
     * @param actionId
     * @param actionComments
     * @return DBResult
     */
    public DBResult submit(String category, Long eaaId, Long actionId, String actionComments, Date date);
    
    /**
     * Update actions taken on account for unsubmit.
     * @param category
     * @param eaaId
     * @return DBResult
     */
    public DBResult unsubmit(String category, Long eaaId);
    
}
