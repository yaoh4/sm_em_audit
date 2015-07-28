package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

import java.util.Date;


/**
 * interface that provides all the APIs related to I2ePortfolio
 */
public interface I2ePortfolioService {

    /**
     * Call stored procedure for saving notes
     * 
     * @param npnId
     * @param notes
     * @param date
     * @return DBResult
     */
    public DBResult saveNotes(Long npnId, String notes, Date date);
    
    /**
     * Data retrieval from view, EmI2ePortfolioVw
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<PortfolioAccountVO>
     */
    public PaginatedListImpl<PortfolioI2eAccountVO> searchI2eAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all);
    
    /**
     * Get portfolio notes by npnId
     * 
     * @param npnId
     * @return notes retrieved
     */
    public String getPortfolioNoteById(Long id);
}
