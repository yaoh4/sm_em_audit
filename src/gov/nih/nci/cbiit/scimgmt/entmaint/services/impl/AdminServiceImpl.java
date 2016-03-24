package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.commons.collections.CollectionUtils;

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
		Long auditId = adminDAO.setupNewAudit(emAuditsVO);
		
		return auditId;
	}
	
	
	/**
	 * Close the current Audit
	 * 
	 * @return
	 */
	public Long closeCurrentAudit(String comments) {
		
		//Get current Audit from DB
    	EmAuditsVw emAuditsVw = adminDAO.retrieveCurrentAudit();
    	Long auditId = emAuditsVw.getId();
    	   	
    	//Close the audit
		adminDAO.closeAudit(auditId, comments);
		
		return auditId;
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
		
		return setupAuditVO(emAuditsVw, false, false);
	}
	
	
	/**
	 * Retrieve the attributes of the newest audit
	 * 
	 * @return EmAuditsVO
	 */
	public EmAuditsVO retrieveCurrentOrLastAuditVO() {
		List<EmAuditsVw> auditList = adminDAO.retrieveAuditList(null);
		if(!CollectionUtils.isEmpty(auditList)) {
			return setupAuditVO(auditList.get(0), true, false);
		}
		
		return null;
	}
	
	
	/**
	 * Retrieves the attributes of the current Audit.
	 * 
	 * @return EmAuditsVO
	 */
	public EmAuditsVO retrieveCurrentAuditVO() {
		
		
		EmAuditsVw emAuditsVw = adminDAO.retrieveCurrentAudit();
		
		return setupAuditVO(emAuditsVw, true, false);
	}
		
	
	public List<EmAuditsVO> retrieveAuditVOList() {
		return retrieveAuditVOList(null);
	}
	
	
	/**
	 * Retrieves the attributes of all Audit.
	 * 
	 * @return EmAuditsVO
	 */
	public List<EmAuditsVO> retrieveAuditVOList(String category) {
		
		List<EmAuditsVO>emAuditVOList = new ArrayList<EmAuditsVO>();
		
		List<EmAuditsVw> emAuditsList = adminDAO.retrieveAuditList(category);
		boolean latest = true;
		if(CollectionUtils.isNotEmpty(emAuditsList)) {
			for(EmAuditsVw audit: emAuditsList) {	
				emAuditVOList.add(setupAuditVO(audit,latest,false));
				latest = false;
			}
		}
		
		return emAuditVOList;
	}
	
	/**
	 * Retrieves the attributes of all Audit for Reports.
	 * 
	 * @return EmAuditsVO
	 */
	public List<EmAuditsVO> retrieveReportAuditVOList() {
		
		List<EmAuditsVO>emAuditVOList = new ArrayList<EmAuditsVO>();
		
		List<EmAuditsVw> emAuditsList = adminDAO.retrieveAuditList(null);
		boolean latest = true;
		if(CollectionUtils.isNotEmpty(emAuditsList)) {
			for(EmAuditsVw audit: emAuditsList) {	
				emAuditVOList.add(setupAuditVO(audit,latest,true));
				latest = false;
			}
		}
		
		return emAuditVOList;
	}
	
	/**
	 * Retrieves the attributes of I2E Audit.
	 * 
	 * @return EmAuditsVO
	 */
	public List<EmAuditsVO> retrieveI2eAuditVOList() {
		
		List<EmAuditsVO>emAuditVOList = new ArrayList<EmAuditsVO>();
		
		List<EmAuditsVw> emAuditsList = adminDAO.retrieveI2eAuditList();
		boolean latest = false;
		if(CollectionUtils.isNotEmpty(emAuditsList)) {
			for(EmAuditsVw audit: emAuditsList) {	
				if(audit.getEndDate() == null)
					latest = true;
				else
					latest = false;
				emAuditVOList.add(setupAuditVO(audit,latest,false));
				
			}
		}
		
		return emAuditVOList;
	}
	
	/**
	 * Checks if there is at least one audit present in the system.
	 * 
	 * @return true if an audit is present, false otherwise.
	 */
	public boolean isAuditPresent() {
		
		return(CollectionUtils.isNotEmpty(adminDAO.retrieveAuditList(null)));
		
	}
	
	/**
	 * Checks if there is at least one I2E audit present in the system.
	 * 
	 * @return true if an I2E audit is present, false otherwise.
	 */
	public boolean isI2eAuditPresent() {
		
		return(CollectionUtils.isNotEmpty(adminDAO.retrieveI2eAuditList()));
		
	}
	
	
	private EmAuditsVO setupAuditVO(EmAuditsVw emAuditsVw, boolean latest, boolean report) {
		EmAuditsVO emAuditsVO = null;
		
		if(emAuditsVw != null) {
			emAuditsVO = populateEmAuditsVO(emAuditsVw);
		
			//Set the Audit flag
			if(emAuditsVO.getImpaciiFromDate() != null && 
				emAuditsVO.getImpaciiToDate() != null) {
				emAuditsVO.setImpac2AuditFlag(ApplicationConstants.TRUE);
			}
			if(emAuditsVO.getI2eFromDate() != null && 
					emAuditsVO.getI2eToDate() != null) {
					emAuditsVO.setI2eAuditFlag(ApplicationConstants.TRUE);
			}
		
		
			//Set current action
			List<EmAuditHistoryVw> statusHistories = emAuditsVO.getStatusHistories();	
			if(statusHistories != null && statusHistories.size() > 0) {
				String currentAuditState = statusHistories.get(0).getActionCode();
				emAuditsVO.setAuditState(currentAuditState);
			}
			
			//Set the description for impac II audit
			if(emAuditsVO.getI2eAuditFlag() != null && emAuditsVO.getI2eAuditFlag().equalsIgnoreCase(ApplicationConstants.TRUE)){
				final StringBuffer sb = new StringBuffer();
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				sb.append(df.format(emAuditsVO.getImpaciiFromDate()) + " to " + df.format(emAuditsVO.getImpaciiToDate()));
				if(latest) {
					// Add (Current) to description
					if(report)
						sb.append(" (Current - IMPAC II and I2E)");
					else
						sb.append(" (Current)");
				}
				else if (report){
					sb.append(" (IMPAC II and I2E)");
				}
				emAuditsVO.setDescription(sb.toString());
			}
			else if(emAuditsVO.getImpac2AuditFlag() != null && emAuditsVO.getImpac2AuditFlag().equalsIgnoreCase(ApplicationConstants.TRUE)){
				final StringBuffer sb = new StringBuffer();
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				sb.append(df.format(emAuditsVO.getImpaciiFromDate()) + " to " + df.format(emAuditsVO.getImpaciiToDate()));
				if(latest) {
					// Add (Current) to description
					if(report)
						sb.append(" (Current - IMPAC II)");
					else
						sb.append(" (Current)");
				}
				else if (report){
					sb.append(" (IMPAC II)");
				}
				emAuditsVO.setDescription(sb.toString());
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
