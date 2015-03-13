package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;

import java.util.List;


/**
 * interface that provides all the APIs related to Impac2Portfolio
 */
public interface Impac2PortfolioService {

    /**
     * Call stored procedure for saving notes
     * @param impaciiUserId
     * @return DBResult
     */
    public DBResult saveNotes(String impaciiUserId, String notes);
    
    /**
     * Data retrieval from view, EmPortfolioVw
     * @param searchVO
     * @return list of PortfolioAccountVO
     */
    public List<PortfolioAccountVO> searchImpac2Accounts(AuditSearchVO searchVO);
    
}
