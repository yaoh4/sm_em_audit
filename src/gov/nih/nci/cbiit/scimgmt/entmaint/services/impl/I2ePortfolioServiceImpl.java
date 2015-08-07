package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.I2ePortfolioDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2ePortfolioVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2ePortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

/**
 * @author dinhys
 *
 */
@Component
public class I2ePortfolioServiceImpl implements I2ePortfolioService {

	static Logger log = Logger.getLogger(I2ePortfolioServiceImpl.class);

	@Autowired
	private I2ePortfolioDAO i2ePortfolioDAO;
	@Autowired
	private LookupService lookupService;

    /**
     * Data retrieval from view, EmI2ePortfolioVw
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<PortfolioAccountVO>
     */
	@Override
	public PaginatedListImpl<PortfolioI2eAccountVO> searchI2eAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) {
		paginatedList = i2ePortfolioDAO.searchI2eAccounts(paginatedList, searchVO, all);
		List<EmI2ePortfolioVw> portfolioList = paginatedList.getList();

		List<PortfolioI2eAccountVO> list = new ArrayList<PortfolioI2eAccountVO>();
		for (final EmI2ePortfolioVw account : portfolioList) {
			account.setAccountDiscrepancies(populateAccountDiscrepancy(account));
			list.add(populatePortfolioAccountVO(account));
		}
		paginatedList.setList(list);

		return paginatedList;
	}
	
    /**
     * Call stored procedure for saving notes
     * 
     * @param npnId
     * @param notes
     * @param date
     * @return DBResult
     */
	@Override
	public DBResult saveNotes(Long npnId, String notes, Date date) {
		return i2ePortfolioDAO.saveNotes(npnId, notes, date);
	}
	
    /**
     * Get portfolio notes by npnId
     * 
     * @param npnId
     * @return notes retrieved
     */
	public String getPortfolioNoteById(Long id){
		
		return i2ePortfolioDAO.getPortfolioNoteById(id);
	}
	
	/**
	 * Convert to PortfolioAccountVO object
	 * 
	 * @param emPortfolioVw
	 * @return
	 */
	private PortfolioI2eAccountVO populatePortfolioAccountVO(EmI2ePortfolioVw emPortfolioVw) {
		final PortfolioI2eAccountVO portfolioAccountVO = new PortfolioI2eAccountVO();
		try {
			ConvertUtils.register(new BigDecimalConverter(null),
									java.math.BigDecimal.class);
			ConvertUtils.register(new LongConverter(null),
									java.lang.Long.class);
			ConvertUtils.register(new IntegerConverter(null),
									java.lang.Integer.class);
			ConvertUtils.register(new ShortConverter(null),
									java.lang.Short.class);
			ConvertUtils.register(new DateConverter(null),java.util.Date.class);
			BeanUtils.copyProperties(portfolioAccountVO, emPortfolioVw);

		} catch (final Exception e) {
			log.error("Error occured creating portfolio account object",
							e);
		}
		return portfolioAccountVO;
	}
	
	/**
	 * Add account discrepancies if DB flag is set to the list of discrepancies
	 * 
	 * @param account
	 * @param category
	 * @return
	 */
	private List<String> populateAccountDiscrepancy(EmI2ePortfolioVw account) {
		List<String> discrepancyList = new ArrayList<String>();
		// Check if there is a violation in roles given to the user
		if(account.getSodFlag() != null) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_I2E_SOD);
		}

		// Check if NED_ACTIVE_FLAG is N
		if (account.getNedInactiveFlag() != null) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_I2E_NED_INACTIVE);
		}
		
		// Check for Active I2E Account/Inactive I2E Role(s)
		if (account.getNoActiveRoleFlag() != null) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_I2E_NO_ACTIVE_ROLE);
		}

		// Check for Active I2E Account/Inactive IMPAC II Account
		if (account.getI2eOnlyFlag() != null) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_I2E_ONLY);
		}
		
		// Check for Inactive I2E Account/Active I2E Role(s)
		if (account.getActiveRoleRemainderFlag() != null) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_I2E_ACTIVE_ROLE_REMAINDER);
		}
				
		return discrepancyList;
	}
	
}
