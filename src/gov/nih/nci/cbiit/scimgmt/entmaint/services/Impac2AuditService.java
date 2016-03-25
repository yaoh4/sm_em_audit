package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import java.util.Date;
import java.util.HashSet;
import java.util.List;


/**
 * interface that provides all the APIs related to Impac2Audit
 */
public interface Impac2AuditService {

    /**
     * Data retrieval for active accounts
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditAccountVO>
     */
    public PaginatedListImpl<AuditAccountVO> searchActiveAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all);
    
    /**
     * Data retrieval for new accounts
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditAccountVO>
     */
    public PaginatedListImpl<AuditAccountVO> searchNewAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all);
    
    /**
     * Data retrieval for deleted accounts
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditAccountVO>
     */
    public PaginatedListImpl<AuditAccountVO> searchDeletedAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all);
    
    /**
     * Data retrieval for inactive > 120 days
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditAccountVO>
     */
    public PaginatedListImpl<AuditAccountVO> searchInactiveAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all);
    
    /**
     * Data retrieval for Exclude from Audit accounts
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditAccountVO>
     */
    public PaginatedListImpl<AuditAccountVO> searchExcludedAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all);
    
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
    
    /**
     * Get EmAuditAccountsVw record using id (EAA_ID)
     * @param id
     * @return
     */
    public EmAuditAccountsVw getAuditAccountById(Long id);
    /**
     * Get Audit Note using ID (EAA_ID)
     * @param id
     * @return String (Note)
     */
    public String getAuditNoteById(Long id, String category);

    /**
     * Retrieve all accounts for a specific audit id
     * @param auditId
     * @return
     */
    public List<AuditAccountVO> getAllAccountsByAuditId(Long auditId);
    
    /**
     * Retrieve a set of impaciiUserId/nihNetworkId from audit which were marked Exclude from Audit
     * @param auditId
     * @return
     */
    public HashSet<String> retrieveExcludedFromAuditAccounts(Long auditId);
    
    /**
	 * Transfers account to different organization.
	 * @param accountId, nihNetworkId, auditId, parentNedOrgPath, actionId, actionComments, transferOrg, category, isImpac2Transfer
     * @return DBResult
     * @throws Exception 
	 */
    public DBResult transfer(Long accountId, String nihNetworkId, Long auditId, String parentNedOrgPath, Long actionId, String actionComments, String transferOrg, String category, boolean isImpac2Transfer) throws Exception;
}
