package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.AdminDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditHistoryVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;


/**
 * Service layer for Admin functionality.
 * 
 * @author menons2
 *
 */
@Component
public class AdminServiceImpl implements AdminService {

	
	public static Logger logger = Logger.getLogger(AdminServiceImpl.class);

	@Autowired
	private AdminDAO adminDAO;
	
	
	/**
	 * Setup a new Audit
	 * 
	 * @param emAuditsVO VO contains audit params
	 * 
	 * @return Long the new audit Id
	 */
	public Long setupNewAudit(EmAuditsVO emAuditsVO) {
		
		//Check if audit already exists
		EmAuditsVO currentAuditsVO = retrieveCurrentAuditVO();
		if(currentAuditsVO != null && currentAuditsVO.getId() != null) {
			logger.warn("Audit already exists, auditId: " + currentAuditsVO.getId());
			return currentAuditsVO.getId();
		}
		
		//Setup a new audit
		Long auditId = adminDAO.setupNewAudit(
			emAuditsVO.getImpaciiFromDate(), emAuditsVO.getImpaciiToDate(), emAuditsVO.getComments());
		
		return auditId;
	}
	
	
	/**
	 * Close the current Audit
	 * 
	 * @return
	 */
	public void closeCurrentAudit(String comments) {
		
		//Get current Audit from DB
    	EmAuditsVw emAuditsVw = adminDAO.retrieveCurrentAudit();
    	Long auditId = emAuditsVw.getId();
    	   	
    	//Close the audit
		adminDAO.closeAudit(auditId, comments);
	}
	
	
	/**
	 * Update the state of the current Audit
	 * 
	 * @param actionCode the action code of the new state
	 * @param comments the comments associated with the new state
	 * 
	 * @return Long the audit id
	 */
	public Long updateCurrentAudit(String actionCode, String comments) {
		
		//Get current Audit from DB
    	EmAuditsVw emAuditsVw = adminDAO.retrieveCurrentAudit();
    	Long auditId = emAuditsVw.getId();
    	
    	//Update the Audit History for the Audit
		adminDAO.updateAuditHistory(auditId, actionCode, comments);
		
		return auditId;
		
	}
	
	/**
	 * Retrieve the audit info associated with the given audit id
	 * 
	 * @param id the audit id of the audit to retrieve
	 */
	public EmAuditsVO retrieveAuditVO(Long id) {
		EmAuditsVw emAuditsVw = adminDAO.retrieveAudit(id);
		
		return setupAuditVO(emAuditsVw);
	}
	
	
	/**
	 * Retrieves the attributes of the current Audit.
	 * 
	 * @return EmAuditsVO
	 */
	public EmAuditsVO retrieveCurrentAuditVO() {
		
		
		EmAuditsVw emAuditsVw = adminDAO.retrieveCurrentAudit();
		
		return setupAuditVO(emAuditsVw);
	}
		
	
	
	/**
	 * Retrieves the attributes of all Audit.
	 * 
	 * @return EmAuditsVO
	 */
	public List<EmAuditsVO> retrieveAuditVOList() {
		
		List<EmAuditsVO>emAuditVOList = new ArrayList<EmAuditsVO>();
		
		List<EmAuditsVw> emAuditsList = adminDAO.retrieveAuditList();
		for(EmAuditsVw audit: emAuditsList) {	
			emAuditVOList.add(setupAuditVO(audit));
		}
		
		return emAuditVOList;
	}
	
	
	
	private EmAuditsVO setupAuditVO(EmAuditsVw emAuditsVw) {
		EmAuditsVO emAuditsVO = null;
		
		if(emAuditsVw != null) {
			emAuditsVO = populateEmAuditsVO(emAuditsVw);
		
			//Set the Audit flag
			if(emAuditsVO.getImpaciiFromDate() != null && 
				emAuditsVO.getImpaciiToDate() != null) {
				emAuditsVO.setImpac2AuditFlag(ApplicationConstants.TRUE);
			}
		
		
			//Set current action
			List<EmAuditHistoryVw> statusHistories = emAuditsVO.getStatusHistories();	
			if(statusHistories != null && statusHistories.size() > 0) {
				String currentAuditState = statusHistories.get(0).getActionCode();
				emAuditsVO.setAuditState(currentAuditState);
			}
		} else {
			emAuditsVO = new EmAuditsVO();
			emAuditsVO.setAuditState(ApplicationConstants.AUDIT_STATE_CODE_RESET);
		}
		
		return emAuditsVO;
	}
	
	
	private EmAuditsVO populateEmAuditsVO(EmAuditsVw emAuditsVw) {
		final EmAuditsVO emAuditsVO = new EmAuditsVO();
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
			BeanUtils.copyProperties(emAuditsVO, emAuditsVw);

		} catch (final Exception e) {
			logger.error("Error occured creating EmAuditsVO from EmAuditsVw",
							e);
		}
		return emAuditsVO;
	}

}
