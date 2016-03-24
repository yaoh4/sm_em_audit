package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

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
	@Autowired
	private LookupService lookupService;
	
    /**
     * Data retrieval for active accounts
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditAccountVO>
     */
	@Override
	public PaginatedListImpl<AuditAccountVO> searchActiveAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) {
		paginatedList = impac2AuditDAO.searchActiveAccounts(paginatedList, searchVO, all);
		List<EmAuditAccountsVw> auditAccountsList = paginatedList.getList();
		final List<AuditAccountVO> list = new ArrayList<AuditAccountVO>();
		for (final EmAuditAccountsVw account : auditAccountsList) {
			account.setAccountActivity(getAccountActivity(account, ApplicationConstants.CATEGORY_ACTIVE));
			account.setAccountDiscrepancies(populateAccountDiscrepancy(account));
			AuditAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		paginatedList.setList(list);
		return paginatedList;
	}

    /**
     * Data retrieval for new accounts
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditAccountVO>
     */
	@Override
	public PaginatedListImpl<AuditAccountVO> searchNewAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) {
		paginatedList = impac2AuditDAO.searchNewAccounts(paginatedList, searchVO, all);
		List<EmAuditAccountsVw> auditAccountsList = paginatedList.getList();
		final List<AuditAccountVO> list = new ArrayList<AuditAccountVO>();
		for (final EmAuditAccountsVw account : auditAccountsList) {
			account.setAccountActivity(getAccountActivity(account, ApplicationConstants.CATEGORY_NEW));
			AuditAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		paginatedList.setList(list);
		return paginatedList;
	}

    /**
     * Data retrieval for deleted accounts
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditAccountVO>
     */
	@Override
	public PaginatedListImpl<AuditAccountVO> searchDeletedAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) {
		paginatedList = impac2AuditDAO.searchDeletedAccounts(paginatedList, searchVO, all);
		List<EmAuditAccountsVw> auditAccountsList = paginatedList.getList();
		final List<AuditAccountVO> list = new ArrayList<AuditAccountVO>();
		for (final EmAuditAccountsVw account : auditAccountsList) {
			account.setAccountActivity(getAccountActivity(account, ApplicationConstants.CATEGORY_DELETED));
			AuditAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		paginatedList.setList(list);
		return paginatedList;
	}

    /**
     * Data retrieval for inactive > 120 days
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditAccountVO>
     */
	@Override
	public PaginatedListImpl<AuditAccountVO> searchInactiveAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) {
		paginatedList = impac2AuditDAO.searchInactiveAccounts(paginatedList, searchVO, all);
		List<EmAuditAccountsVw> auditAccountsList = paginatedList.getList();
		final List<AuditAccountVO> list = new ArrayList<AuditAccountVO>();
		for (final EmAuditAccountsVw account : auditAccountsList) {
			account.setAccountActivity(getAccountActivity(account, ApplicationConstants.CATEGORY_INACTIVE));
			AuditAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		paginatedList.setList(list);
		return paginatedList;
	}

    /**
     * Data retrieval for Exclude from Audit accounts
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditAccountVO>
     */
	@Override
	public PaginatedListImpl<AuditAccountVO> searchExcludedAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) {
		paginatedList = impac2AuditDAO.searchExcludedAccounts(paginatedList, searchVO, all);
		List<EmAuditAccountsVw> auditAccountsList = paginatedList.getList();
		final List<AuditAccountVO> list = new ArrayList<AuditAccountVO>();
		for (final EmAuditAccountsVw account : auditAccountsList) {
			account.setAccountActivity(getAccountActivity(account));
			AuditAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		paginatedList.setList(list);
		return paginatedList;
	}


    /**
     * Update actions taken on account for submit.
     * @param category
     * @param eaaId
     * @param actionId
     * @param actionComments
     * @return DBResult
     */
	@Override
	public DBResult submit(String category, Long eaaId, Long actionId, String actionComments, Date date) {
		AppLookupT cat = lookupService.getAppLookupByCode(ApplicationConstants.APP_LOOKUP_CATEGORY_LIST, category);
		return impac2AuditDAO.submit(cat, eaaId, actionId, actionComments, date);
	}


    /**
     * Update actions taken on account for unsubmit.
     * @param category
     * @param eaaId
     * @return DBResult
     */
	@Override
	public DBResult unsubmit(String category, Long eaaId) {
		return impac2AuditDAO.unsubmit(category, eaaId);
	}
	
    /**
     * Get EmAuditAccountsVw record using id (EAA_ID)
     * @param id
     * @return
     */
	public EmAuditAccountsVw getAuditAccountById(Long id) {
		return impac2AuditDAO.getAuditAccountById(id);
	}
	
    /**
     * Get Audit Note using ID (EAA_ID)
     * @param id
     * @return String (Note)
     */
	public String getAuditNoteById(Long id, String category){
		
		return impac2AuditDAO.getAuditNoteById(id, category);
	}
	
    /**
     * Retrieve all accounts for a specific audit id
     * @param auditId
     * @return
     */
	@Override
	public List<AuditAccountVO> getAllAccountsByAuditId(Long auditId) {
		List<EmAuditAccountsVw> auditAccountsList = impac2AuditDAO.getAllAccountsByAuditId(auditId);
		final List<AuditAccountVO> list = new ArrayList<AuditAccountVO>();
		for (final EmAuditAccountsVw account : auditAccountsList) {
			AuditAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		return list;
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
			log.error("Error occured creating audit account object",
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
	private EmAuditAccountActivityVw getAccountActivity(EmAuditAccountsVw account, String category) {
		EmAuditAccountActivityVw activity = null;
		if(category.equalsIgnoreCase(ApplicationConstants.CATEGORY_ACTIVE)) {
			if (account.getActiveAction() != null) {
				activity = new EmAuditAccountActivityVw();
				activity.setEaaId(account.getId());
				activity.setAction(account.getActiveAction());
				activity.setNotes(account.getActiveNotes());
				activity.setSubmittedByFullName(account.getActiveSubmittedBy());
				activity.setSubmittedDate(account.getActiveSubmittedDate());
				activity.setUnsubmittedFlag(account.getActiveUnsubmittedFlag());
			}
		} else if (category.equalsIgnoreCase(ApplicationConstants.CATEGORY_NEW)) {
			if (account.getNewAction() != null) {
				activity = new EmAuditAccountActivityVw();
				activity.setEaaId(account.getId());
				activity.setAction(account.getNewAction());
				activity.setNotes(account.getNewNotes());
				activity.setSubmittedByFullName(account.getNewSubmittedBy());
				activity.setSubmittedDate(account.getNewSubmittedDate());
				activity.setUnsubmittedFlag(account.getNewUnsubmittedFlag());
			}
		} else if (category.equalsIgnoreCase(ApplicationConstants.CATEGORY_DELETED)) {
			if (account.getDeletedAction() != null) {
				activity = new EmAuditAccountActivityVw();
				activity.setEaaId(account.getId());
				activity.setAction(account.getDeletedAction());
				activity.setNotes(account.getDeletedNotes());
				activity.setSubmittedByFullName(account.getDeletedSubmittedBy());
				activity.setSubmittedDate(account.getDeletedSubmittedDate());
				activity.setUnsubmittedFlag(account.getDeletedUnsubmittedFlag());
			}
		} else if (category.equalsIgnoreCase(ApplicationConstants.CATEGORY_INACTIVE)) {
			if (account.getInactiveAction() != null) {
				activity = new EmAuditAccountActivityVw();
				activity.setEaaId(account.getId());
				activity.setAction(account.getInactiveAction());
				activity.setNotes(account.getInactiveNotes());
				activity.setSubmittedByFullName(account.getInactiveSubmittedBy());
				activity.setSubmittedDate(account.getInactiveSubmittedDate());
				activity.setUnsubmittedFlag(account.getInactiveUnsubmittedFlag());
			}
		}
		return activity;
	}
	
	/**
	 * Get account activity for any category
	 * 
	 * @param accountActivities
	 * @return
	 */
	private EmAuditAccountActivityVw getAccountActivity(EmAuditAccountsVw account) {
		EmAuditAccountActivityVw activity = null;
		if (account.getActiveAction() != null) {
			activity = new EmAuditAccountActivityVw();
			activity.setEaaId(account.getId());
			activity.setAction(account.getActiveAction());
			activity.setNotes(account.getActiveNotes());
			activity.setSubmittedByFullName(account.getActiveSubmittedBy());
			activity.setSubmittedDate(account.getActiveSubmittedDate());
			activity.setUnsubmittedFlag(account.getActiveUnsubmittedFlag());
		}
		if (account.getNewAction() != null) {
			activity = new EmAuditAccountActivityVw();
			activity.setEaaId(account.getId());
			activity.setAction(account.getNewAction());
			activity.setNotes(account.getNewNotes());
			activity.setSubmittedByFullName(account.getNewSubmittedBy());
			activity.setSubmittedDate(account.getNewSubmittedDate());
			activity.setUnsubmittedFlag(account.getNewUnsubmittedFlag());

		}
		if (account.getDeletedAction() != null) {
			activity = new EmAuditAccountActivityVw();
			activity.setEaaId(account.getId());
			activity.setAction(account.getDeletedAction());
			activity.setNotes(account.getDeletedNotes());
			activity.setSubmittedByFullName(account.getDeletedSubmittedBy());
			activity.setSubmittedDate(account.getDeletedSubmittedDate());
			activity.setUnsubmittedFlag(account.getDeletedUnsubmittedFlag());
		}
		if (account.getInactiveAction() != null) {
			activity = new EmAuditAccountActivityVw();
			activity.setEaaId(account.getId());
			activity.setAction(account.getInactiveAction());
			activity.setNotes(account.getInactiveNotes());
			activity.setSubmittedByFullName(account.getInactiveSubmittedBy());
			activity.setSubmittedDate(account.getInactiveSubmittedDate());
			activity.setUnsubmittedFlag(account.getInactiveUnsubmittedFlag());
		}
		return activity;
	}
	
	/**
	 * Compute account discrepancies on the fly and populate a list of discrepancies
	 * 
	 * @param account
	 * @param category
	 * @return
	 */
	private List<String> populateAccountDiscrepancy(EmAuditAccountsVw account) {
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
     * Retrieve a set of impaciiUserId/nihNetworkId from audit which were marked Exclude from Audit
     * @param auditId
     * @return
     */
	public HashSet<String> retrieveExcludedFromAuditAccounts(Long auditId) {
		// Retrieve list of excluded from audit accounts for IMPAC II
		PaginatedListImpl<AuditAccountVO> auditAccounts = new PaginatedListImpl<AuditAccountVO>();
		AuditSearchVO searchVO = new AuditSearchVO();
		searchVO.setAuditId(auditId);
		searchVO.setAct(ApplicationConstants.ACTIVE_EXCLUDE_FROM_AUDIT.toString());
		auditAccounts = searchActiveAccounts(auditAccounts, searchVO, true);
		HashSet<String> nihNetworkIdList = new HashSet<String>();
		for (AuditAccountVO account: auditAccounts.getList()) {
			if(!StringUtils.isEmpty(account.getImpaciiUserId()) || !StringUtils.isEmpty(account.getNihNetworkId()))
				nihNetworkIdList.add((!StringUtils.isEmpty(account.getImpaciiUserId()) ? account.getImpaciiUserId() : account.getNihNetworkId()));
		}
		return nihNetworkIdList;
	}
	
	/**
	 * Transfers Impac2 account to different organization.
	 * @param accountId, nihNetworkId, auditId, parentNedOrgPath, actionId, actionComments, transferOrg, category, isImpac2Transfer
     * @return DBResult
	 */
	@Override
	public DBResult transfer(Long accountId, String nihNetworkId, Long auditId, String parentNedOrgPath, Long actionId, String actionComments, String transferOrg, String category, boolean isImpac2Transfer) {
		return impac2AuditDAO.transfer(accountId, nihNetworkId, auditId, parentNedOrgPath, actionId, actionComments, transferOrg, category, isImpac2Transfer);
	}

}
