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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.Impac2AuditDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountActivityVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

@Component
public class Impac2AuditServiceImpl implements Impac2AuditService {

	public static Logger log = Logger.getLogger(Impac2AuditServiceImpl.class);
	public static final String FLAG_NO = "N";
	public static final String ORG_ID_CA = "CA";
	public static final String ROLE_GM_MANAGER = "GM_MANAGER_ROLE";
	public static final String ROLE_ICO_PROGRAM_OFFICIAL = "ICO_PROGRAM_OFFICIAL_ROLE";
	public static final String ROLE_RR_CHIEF = "RR_CHIEF_ROLE";
	public static final String ROLE_UADM_ADMIN = "UADM_ADMIN_ROLE";
	
	@Autowired
	private Impac2AuditDAO impac2AuditDAO;
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService#searchActiveAccounts(gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO)
	 */
	@Override
	public List<AuditAccountVO> searchActiveAccounts(AuditSearchVO searchVO) {
		List<EmAuditAccountsVw> auditAccountsList;
		auditAccountsList = impac2AuditDAO.searchActiveAccounts(searchVO);
		final List<AuditAccountVO> list = new ArrayList<AuditAccountVO>();
		for (final EmAuditAccountsVw account : auditAccountsList) {
			account.setAccountDiscrepancies(populateAccountDiscrepancy(account));
			account.setAccountActivity(getAccountActivity(account.getAccountActivities(), ApplicationConstants.CATEGORY_ACTIVE));
			AuditAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService#searchNewAccounts(gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO)
	 */
	@Override
	public List<AuditAccountVO> searchNewAccounts(AuditSearchVO searchVO) {
		List<EmAuditAccountsVw> auditAccountsList;
		auditAccountsList = impac2AuditDAO.searchNewAccounts(searchVO);
		final List<AuditAccountVO> list = new ArrayList<AuditAccountVO>();
		for (final EmAuditAccountsVw account : auditAccountsList) {
			account.setAccountActivity(getAccountActivity(account.getAccountActivities(), ApplicationConstants.CATEGORY_NEW));
			AuditAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService#searchDeletedAccounts(gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO)
	 */
	@Override
	public List<AuditAccountVO> searchDeletedAccounts(AuditSearchVO searchVO) {
		List<EmAuditAccountsVw> auditAccountsList;
		auditAccountsList = impac2AuditDAO.searchDeletedAccounts(searchVO);
		final List<AuditAccountVO> list = new ArrayList<AuditAccountVO>();
		for (final EmAuditAccountsVw account : auditAccountsList) {
			account.setAccountActivity(getAccountActivity(account.getAccountActivities(), ApplicationConstants.CATEGORY_DELETED));
			AuditAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService#searchInactiveAccounts(gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO)
	 */
	@Override
	public List<AuditAccountVO> searchInactiveAccounts(AuditSearchVO searchVO) {
		List<EmAuditAccountsVw> auditAccountsList;
		auditAccountsList = impac2AuditDAO.searchInactiveAccounts(searchVO);
		final List<AuditAccountVO> list = new ArrayList<AuditAccountVO>();
		for (final EmAuditAccountsVw account : auditAccountsList) {
			account.setAccountActivity(getAccountActivity(account.getAccountActivities(), ApplicationConstants.CATEGORY_INACTIVE));
			AuditAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		return list;
	}


	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService#submit(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public DBResult submit(String category, Long eaaId, Long actionId, String actionComments) {
		return impac2AuditDAO.submit(category, eaaId, actionId, actionComments);
	}


	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService#unsubmit(java.lang.String, java.lang.Integer)
	 */
	@Override
	public DBResult unsubmit(String category, Long eaaId) {
		return impac2AuditDAO.unsubmit(category, eaaId);
	}
	
	/**
	 * Convert to AuditAccountVO object
	 * 
	 * @param emAuditAccountsVw
	 * @return
	 */
	private AuditAccountVO populateAuditAccountVO(EmAuditAccountsVw emAuditAccountsVw) {
		final AuditAccountVO auditAccountVO = new AuditAccountVO();
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
			BeanUtils.copyProperties(auditAccountVO, emAuditAccountsVw);

		} catch (final Exception e) {
			log.error("Error occured creating notification transfer object",
							e);
		}
		return auditAccountVO;
	}

	/**
	 * Get account activity for specified category
	 * 
	 * @param accountActivities
	 * @param category
	 * @return
	 */
	private EmAuditAccountActivityVw getAccountActivity(List<EmAuditAccountActivityVw> accountActivities, String category) {
		for(EmAuditAccountActivityVw account: accountActivities) {
			if(account.getCategory().getCode().equalsIgnoreCase(category)) {
				return account;
			}
		}
		return null;
	}
	
	/**
	 * Compute account discrepancies on the fly and populate a list of discrepancies
	 * 
	 * @param account
	 * @param category
	 * @return
	 */
	private List<AppLookupT> populateAccountDiscrepancy(EmAuditAccountsVw account) {
		List<AppLookupT> discrepancyList = new ArrayList<AppLookupT>();
		// Check if there is a violation in roles given to the user
		int role1Count = 0;
		for (EmAuditAccountRolesVw role: account.getAccountRoles()) {
			if (role.getOrgId().equalsIgnoreCase(ORG_ID_CA) && 
					(role.getRoleName().equalsIgnoreCase(ROLE_GM_MANAGER) ||
					role.getRoleName().equalsIgnoreCase(ROLE_ICO_PROGRAM_OFFICIAL) ||
					role.getRoleName().equalsIgnoreCase(ROLE_RR_CHIEF))) {
				role1Count++;
			}
		}
		int role2Count = 0;
		for (EmAuditAccountRolesVw role: account.getAccountRoles()) {
			if (role.getOrgId().equalsIgnoreCase(ORG_ID_CA) && 
					(role.getRoleName().equalsIgnoreCase(ROLE_GM_MANAGER) ||
					role.getRoleName().equalsIgnoreCase(ROLE_UADM_ADMIN) ||
					role.getRoleName().equalsIgnoreCase(ROLE_RR_CHIEF))) {
				role2Count++;
			}
		}
		if (role1Count > 1 || role2Count > 1) {
			discrepancyList.add(impac2AuditDAO
					.getDiscrepancyByCode(ApplicationConstants.DISCREPANCY_CODE_SOD));
		}
		
		// Check if NED_IC is not NCI
		if (!StringUtils.equalsIgnoreCase(account.getNedIc(), ApplicationConstants.NED_IC_NCI)) {
			discrepancyList.add(impac2AuditDAO
					.getDiscrepancyByCode(ApplicationConstants.DISCREPANCY_CODE_IC));
		}
		
		// Check if NED_ACTIVE_FLAG is N
		if (StringUtils.equalsIgnoreCase(account.getNedActiveFlag(), FLAG_NO)) {
			discrepancyList.add(impac2AuditDAO
					.getDiscrepancyByCode(ApplicationConstants.DISCREPANCY_CODE_NED_INACTIVE));
		}
		
		// Check if last name is different between IMPACII and NED
		/*if (!StringUtils.equalsIgnoreCase(account.getNedLastName(), account.getImpaciiLastName())) {
			discrepancyList.add(impac2AuditDAO
					.getDiscrepancyByCode(ApplicationConstants.DISCREPANCY_CODE_LAST_NAME));
		}*/
		return discrepancyList;
	}

}
