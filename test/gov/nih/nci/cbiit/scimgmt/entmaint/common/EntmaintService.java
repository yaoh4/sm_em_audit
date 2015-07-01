package gov.nih.nci.cbiit.scimgmt.entmaint.common;

import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class EntmaintService {
	@Autowired
	protected Impac2AuditService impac2AuditService;
	
	public List<AuditAccountVO> getAuditAccount(Long auditId){
		List<AuditAccountVO> auditAccountVOs = impac2AuditService.getAllAccountsByAuditId(auditId);
		return auditAccountVOs;
	}
}
