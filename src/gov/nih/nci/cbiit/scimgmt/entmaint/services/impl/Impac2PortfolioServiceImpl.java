package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.Impac2PortfolioDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;

@Component
public class Impac2PortfolioServiceImpl implements Impac2PortfolioService {

	private static final Log log = LogFactory.getLog(Impac2PortfolioServiceImpl.class);
	public static final String FLAG_NO = "N";
	public static final String ORG_ID_CA = "CA";
	public static final String ROLE_GM_MANAGER = "GM_MANAGER_ROLE";
	public static final String ROLE_ICO_PROGRAM_OFFICIAL = "ICO_PROGRAM_OFFICIAL_ROLE";
	public static final String ROLE_RR_CHIEF = "RR_CHIEF_ROLE";
	public static final String ROLE_UADM_ADMIN = "UADM_ADMIN_ROLE";
	public static final long PORTFOLIO_CATEGORY_DISCREPANCY = 25;
	
	@Autowired
	private Impac2PortfolioDAO impac2PortfolioDAO;
	@Autowired
	private LookupService lookupService;

	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService#searchImpac2Accounts(gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.SearchObject)
	 */
	@Override
	public List<PortfolioAccountVO> searchImpac2Accounts(AuditSearchVO searchVO) {
		List<EmPortfolioVw> portfolioList;
		portfolioList = impac2PortfolioDAO.searchImpac2Accounts(searchVO);
		final List<PortfolioAccountVO> list = new ArrayList<PortfolioAccountVO>();
		for (final EmPortfolioVw account : portfolioList) {
			if (searchVO.getCategory() == PORTFOLIO_CATEGORY_DISCREPANCY) {
				account.setAccountDiscrepancies(populateAccountDiscrepancy(account));
				if(account.getAccountDiscrepancies() == null ||
						account.getAccountDiscrepancies().isEmpty()) {
					continue;
				}
			}
			list.add(populatePortfolioAccountVO(account));
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService#saveNotes(java.lang.String)
	 */
	@Override
	public DBResult saveNotes(String impaciiUserId, String notes) {
		return impac2PortfolioDAO.saveNotes(impaciiUserId, notes);
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
			log.error("Error occured creating notification transfer object",
							e);
		}
		return portfolioAccountVO;
	}

	/**
	 * Compute account discrepancies on the fly and populate a list of discrepancies
	 * 
	 * @param account
	 * @param category
	 * @return
	 */
	private List<String> populateAccountDiscrepancy(EmPortfolioVw account) {
		List<String> discrepancyList = new ArrayList<String>();
		// Check if there is a violation in roles given to the user
		int role1Count = 0;
		for (EmPortfolioRolesVw role: account.getAccountRoles()) {
			if (role.getOrgId().equalsIgnoreCase(ORG_ID_CA) && 
					(role.getRoleName().equalsIgnoreCase(ROLE_GM_MANAGER) ||
					role.getRoleName().equalsIgnoreCase(ROLE_ICO_PROGRAM_OFFICIAL) ||
					role.getRoleName().equalsIgnoreCase(ROLE_RR_CHIEF))) {
				role1Count++;
			}
		}
		int role2Count = 0;
		for (EmPortfolioRolesVw role: account.getAccountRoles()) {
			if (role.getOrgId().equalsIgnoreCase(ORG_ID_CA) && 
					(role.getRoleName().equalsIgnoreCase(ROLE_GM_MANAGER) ||
					role.getRoleName().equalsIgnoreCase(ROLE_UADM_ADMIN) ||
					role.getRoleName().equalsIgnoreCase(ROLE_RR_CHIEF))) {
				role2Count++;
			}
		}
		if (role1Count > 1 || role2Count > 1) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_SOD);
		}

		// Check if NED_IC is not NCI
		if (!StringUtils.equalsIgnoreCase(account.getNedIc(), ApplicationConstants.NED_IC_NCI)) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_IC);
		}

		// Check if NED_ACTIVE_FLAG is N
		if (StringUtils.equalsIgnoreCase(account.getNedActiveFlag(), FLAG_NO)) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_NED_INACTIVE);
		}

		// Check if last name is different between IMPACII and NED
		if (!StringUtils.equalsIgnoreCase(account.getNedLastName(), account.getImpaciiLastName())) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_LAST_NAME);
		}
		return discrepancyList;
	}
}
