package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import java.util.Date;
import java.util.HashSet;
import java.util.List;


/**
 * interface that provides all the APIs related to I2eAudit
 */
public interface I2eAuditService {

    /**
     * Data retrieval for active accounts
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditI2eAccountVO>
     */
    public PaginatedListImpl<AuditI2eAccountVO> searchActiveAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all);
    
    /**
     * Update actions taken on I2E account for submit.
     * @param id
     * @param actionId
     * @param actionComments
     * @return DBResult
     */
    public DBResult submit(Long id, Long actionId, String actionComments, Date date);
    
    /**
     * Update actions taken on I2E account for unsubmit.
     * @param id
     * @return DBResult
     */
    public DBResult unsubmit(Long id);
    
    /**
     * Get AuditI2eAccountVO record using id
     * @param id
     * @return
     */
    public AuditI2eAccountVO getAuditAccountById(Long id);
    /**
     * Get Audit Note using ID
     * @param id
     * @return String (Note)
     */
    public String getAuditNoteById(Long id);

    /**
     * Retrieve all I2E accounts for a specific audit id
     * @param auditId
     * @return
     */
    public List<AuditI2eAccountVO> getAllAccountsByAuditId(Long auditId);
    
    /**
     * Retrieve a set of nihNetworkId from audit which were marked Exclude from Audit
     * @param auditId
     * @return
     */
    public HashSet<String> retrieveExcludedFromAuditAccounts(Long auditId);
}
