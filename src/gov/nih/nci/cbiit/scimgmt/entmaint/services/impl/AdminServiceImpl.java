/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

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
 * @author menons2
 *
 */
@Component
public class AdminServiceImpl implements AdminService {

	
	public static Logger log = Logger.getLogger(AdminServiceImpl.class);

	@Autowired
	private AdminDAO adminDAO;
	
	public static String TRUE = ApplicationConstants.TRUE;
	public static String FALSE = ApplicationConstants.FALSE;
	
	
	
	/**
	 * Default constructor
	 */
	public AdminServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Setup a new Audit
	 * 
	 * @param emAuditsVO
	 * @return Long
	 */
	public Long setupNewAudit(EmAuditsVO emAuditsVO) {
		
		//Setup a new audit
		Long auditId = adminDAO.setupNewAudit(
			emAuditsVO.getImpaciiFromDate(), emAuditsVO.getImpaciiToDate(), emAuditsVO.getComments());
		
		return auditId;
	}
	
	
	/**
	 * Close the current Audit
	 * 
	 * @param id
	 */
	public void closeCurrentAudit() {
		
		//Get current Audit from DB
    	EmAuditsVw emAuditsVw = adminDAO.retrieveCurrentAudit();
    	Long auditId = emAuditsVw.getId();
    	
    	//Close the current audit
		adminDAO.closeAudit(auditId);
	}
	
	
	/**
	 * Update the state of the current Audit
	 * 
	 * @param historyVO
	 */
	public Long updateCurrentAudit(String actionCode, String comments) {
		
		//Get current Audit from DB
    	EmAuditsVw emAuditsVw = adminDAO.retrieveCurrentAudit();
    	Long auditId = emAuditsVw.getId();
    	
    	//Update the Audit History in the Audit
		adminDAO.updateAuditHistory(auditId, actionCode, comments);
		
		return auditId;
		
	}
	
	
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
	
	
	private EmAuditsVO setupAuditVO(EmAuditsVw emAuditsVw) {
		EmAuditsVO emAuditsVO = null;
		
		if(emAuditsVw != null) {
			emAuditsVO = populateEmAuditsVO(emAuditsVw);
		
			//Set the Audit flag
			if(emAuditsVO.getImpaciiFromDate() != null && 
				emAuditsVO.getImpaciiToDate() != null) {
				emAuditsVO.setImpac2AuditFlag(TRUE);
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
			log.error("Error occured creating EmAuditsVO from EmAuditsVw",
							e);
		}
		return emAuditsVO;
	}

	/**
	 * @return the adminDAO
	 */
	public AdminDAO getAdminDAO() {
		return adminDAO;
	}

	/**
	 * @param adminDAO the adminDAO to set
	 */
	public void setAdminDAO(AdminDAO adminDAO) {
		this.adminDAO = adminDAO;
	}

}
