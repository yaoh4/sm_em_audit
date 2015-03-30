package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;

import java.util.Date;


/**
 * interface that provides all the APIs related to Impac2Portfolio
 */
public interface Impac2PortfolioService {

    /**
     * Call stored procedure for saving notes
     * @param impaciiUserId
     * @return DBResult
     */
    public DBResult saveNotes(String impaciiUserId, String notes, Date date);
    
    /**
     * Data retrieval from view, EmPortfolioVw
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<PortfolioAccountVO>
     */
    public PaginatedListImpl<PortfolioAccountVO> searchImpac2Accounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all);
    
}
