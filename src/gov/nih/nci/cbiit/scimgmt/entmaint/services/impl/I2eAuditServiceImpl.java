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
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.I2eAuditDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2eAuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

@Component
public class I2eAuditServiceImpl implements I2eAuditService {

	public static Logger log = Logger.getLogger(I2eAuditServiceImpl.class);

	@Autowired
	private I2eAuditDAO i2eAuditDAO;
	@Autowired
	private LookupService lookupService;
	

    /**
     * Data retrieval for active accounts
     * @param paginatedList
     * @param searchVO
     * @param all
     * @return PaginatedListImpl<AuditI2eAccountVO>
     */
	@Override
	public PaginatedListImpl<AuditI2eAccountVO> searchActiveAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) {
		paginatedList = i2eAuditDAO.searchActiveAccounts(paginatedList, searchVO, all);
		List<EmI2eAuditAccountsVw> auditAccountsList = paginatedList.getList();
		final List<AuditI2eAccountVO> list = new ArrayList<AuditI2eAccountVO>();
		for (final EmI2eAuditAccountsVw account : auditAccountsList) {
			account.setAccountDiscrepancies(populateAccountDiscrepancy(account));
			AuditI2eAccountVO acct = populateAuditAccountVO(account);
			list.add(acct);
		}
		paginatedList.setList(list);
		return paginatedList;
	}

    /**
     * Update actions taken on I2E account for submit.
     * @param id
     * @param actionId
     * @param actionComments
     * @return DBResult
     */
	@Override
	public DBResult submit(Long id, Long actionId, String actionComments, Date date) {
		return i2eAuditDAO.submit(id, actionId, actionComments, date);
	}

    /**
     * Update actions taken on I2E account for unsubmit.
     * @param id
     * @return DBResult
     */
	@Override
	public DBResult unsubmit(Long id) {
		return i2eAuditDAO.unsubmit(id);
	}
	
    /**
     * Get AuditI2eAccountVO record using id
     * @param id
     * @return
     */
	public AuditI2eAccountVO getAuditAccountById(Long id) {
		EmI2eAuditAccountsVw account = i2eAuditDAO.getAuditAccountById(id);
		AuditI2eAccountVO acct = null;
		if(account != null) {
			acct = populateAuditAccountVO(account);
		}
		return acct;
	}
	
    /**
     * Get Audit Note using ID
     * @param id
     * @return String (Note)
     */
	public String getAuditNoteById(Long id){
		return i2eAuditDAO.getAuditNoteById(id);
	}
	
    /**
     * Retrieve all I2E accounts for a specific audit id
     * @param auditId
     * @return
     */
	@Override
	public List<AuditI2eAccountVO> getAllAccountsByAuditId(Long auditId) {
		List<EmI2eAuditAccountsVw> auditAccountsList = i2eAuditDAO.getAllAccountsByAuditId(auditId);
		final List<AuditI2eAccountVO> list = new ArrayList<AuditI2eAccountVO>();
		for (final EmI2eAuditAccountsVw account : auditAccountsList) {
			AuditI2eAccountVO acct = populateAuditAccountVO(account);
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
	private AuditI2eAccountVO populateAuditAccountVO(EmI2eAuditAccountsVw emAuditAccountsVw) {
		final AuditI2eAccountVO auditAccountVO = new AuditI2eAccountVO();
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
			log.error("Error occured creating I2e audit account object",
							e);
		}
		return auditAccountVO;
	}
	
	/**
	 * Compute account discrepancies on the fly and populate a list of discrepancies
	 * 
	 * @param account
	 * @return
	 */
	private List<String> populateAccountDiscrepancy(EmI2eAuditAccountsVw account) {
		List<String> discrepancyList = new ArrayList<String>();
		// Check if there is a violation in roles given to the user
		if(account.getSodFlag() != null && account.getSodFlag().booleanValue()) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_I2E_SOD);
		}

		// Check if NED_ACTIVE_FLAG is N
		if (account.getNedInactiveFlag() != null && account.getNedInactiveFlag().booleanValue()) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_I2E_NED_INACTIVE);
		}
		
		// Check for Active I2E Account/Inactive I2E Role(s)
		if (account.getNoActiveRoleFlag() != null && account.getNoActiveRoleFlag().booleanValue()) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_I2E_NO_ACTIVE_ROLE);
		}

		// Check for Active I2E Account/Inactive IMPAC II Account
		if (account.getI2eOnlyFlag() != null && account.getI2eOnlyFlag().booleanValue()) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_I2E_ONLY);
		}
		
		// Check for Inactive I2E Account/Active I2E Role(s)
		if (account.getActiveRoleRemainderFlag() != null && account.getActiveRoleRemainderFlag().booleanValue()) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_I2E_ACTIVE_ROLE_REMAINDER);
		}
		return discrepancyList;
	}


    /**
     * Retrieve a set of nihNetworkId from audit which were marked Exclude from Audit
     * @param auditId
     * @return
     */
	public HashSet<String> retrieveExcludedFromAuditAccounts(Long auditId) {
		// Retrieve list of excluded from audit accounts for IMPAC II
		PaginatedListImpl<AuditI2eAccountVO> auditAccounts = new PaginatedListImpl<AuditI2eAccountVO>();
		AuditSearchVO searchVO = new AuditSearchVO();
		searchVO.setAuditId(auditId);
		searchVO.setAct(ApplicationConstants.ACTIVE_EXCLUDE_FROM_AUDIT.toString());
		auditAccounts = searchActiveAccounts(auditAccounts, searchVO, true);
		HashSet<String> nihNetworkIdList = new HashSet<String>();
		for (AuditI2eAccountVO account: auditAccounts.getList()) {
			if(!StringUtils.isEmpty(account.getNihNetworkId()))
				nihNetworkIdList.add(account.getNihNetworkId());
		}
		return nihNetworkIdList;
	}
	
	/**
	 * Transfers account to different organization.
	 * @param accountId, nihNetworkId, auditId, parentNedOrgPath, actionComments, transferOrg, isI2eTransfer
     * @return DBResult
	 * @throws Exception 
	 */
	@Override
	public DBResult transfer(Long accountId, String nihNetworkId, Long auditId, String parentNedOrgPath, String actionComments, String transferOrg, boolean isI2eTransfer) throws Exception {
		return i2eAuditDAO.transfer(accountId, nihNetworkId, auditId, parentNedOrgPath, actionComments, transferOrg, isI2eTransfer);
	}
}
