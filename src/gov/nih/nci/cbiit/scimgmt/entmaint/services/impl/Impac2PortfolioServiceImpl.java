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
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.Impac2PortfolioDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;

@Component
public class Impac2PortfolioServiceImpl implements Impac2PortfolioService {

	static Logger log = Logger.getLogger(Impac2PortfolioServiceImpl.class);
	public static final String FLAG_NO = "N";
	public static final String ORG_ID_CA = "CA";
	public static final String ROLE_GM_MANAGER = "GM_MANAGER_ROLE";
	public static final String ROLE_ICO_PROGRAM_OFFICIAL = "ICO_PROGRAM_OFFICIAL_ROLE";
	public static final String ROLE_RR_CHIEF = "RR_CHIEF_ROLE";
	public static final String ROLE_UADM_ADMIN = "UADM_ADMIN_ROLE";
	
	@Autowired
	private Impac2PortfolioDAO impac2PortfolioDAO;
	@Autowired
	private LookupService lookupService;

    /**
     * Data retrieval from view, EmPortfolioVw
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<PortfolioAccountVO>
     */
	@Override
	public PaginatedListImpl<PortfolioAccountVO> searchImpac2Accounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) {
		paginatedList = impac2PortfolioDAO.searchImpac2Accounts(paginatedList, searchVO, all);
		List<EmPortfolioVw> portfolioList = paginatedList.getList();

		List<PortfolioAccountVO> list = new ArrayList<PortfolioAccountVO>();
		for (final EmPortfolioVw account : portfolioList) {
			account.setAccountDiscrepancies(populateAccountDiscrepancy(account));
			list.add(populatePortfolioAccountVO(account));
		}
		paginatedList.setList(list);

		return paginatedList;
	}
	
    /**
     * Call stored procedure for saving notes
     * @param impaciiUserId
     * @return DBResult
     */
	@Override
	public DBResult saveNotes(String impaciiUserId, String notes, Date date) {
		return impac2PortfolioDAO.saveNotes(impaciiUserId, notes, date);
	}
	/**
	 * Note retrieve from view
	 * @param id
	 * @param category
	 * @return
	 */
	public String getPortfolioNoteById(String id){
		
		return impac2PortfolioDAO.getPortfolioNoteById(id);
	}
	
	/**
	 * Convert to PortfolioAccountVO object
	 * 
	 * @param emPortfolioVw
	 * @return
	 */
	private PortfolioAccountVO populatePortfolioAccountVO(EmPortfolioVw emPortfolioVw) {
		final PortfolioAccountVO portfolioAccountVO = new PortfolioAccountVO();
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
	private List<String> populateAccountDiscrepancy(EmPortfolioVw account) {
		List<String> discrepancyList = new ArrayList<String>();
		// Check if there is a violation in roles given to the user
		if(account.getSodFlag() != null && account.getSodFlag().booleanValue()) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_SOD);
		}

		// Check if NED_IC is not NCI
		if (account.getIcDiffFlag() != null && account.getIcDiffFlag().booleanValue()) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_IC);
		}

		// Check if NED_ACTIVE_FLAG is N
		if (account.getNedInactiveFlag() != null && account.getNedInactiveFlag().booleanValue()) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_NED_INACTIVE);
		}

		// Check if last name is different between IMPACII and NED
		if (account.getLastNameDiffFlag() != null && account.getLastNameDiffFlag().booleanValue()) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_LAST_NAME);
		}
		return discrepancyList;
	}
	
	/**
	 * Get the Last Refresh date
	 * @return Date
	 */
	public Date getLastRefreshDate(){
		return impac2PortfolioDAO.getLastRefreshDate();
	}
	
	/**
	 * Get the distinct DOC with IC coordinator
	 * @return List<String>
	 */
	public List<String> getOrgsWithIcCoordinator(){
		return impac2PortfolioDAO.getOrgsWithIcCoordinator();
	}
	
}
