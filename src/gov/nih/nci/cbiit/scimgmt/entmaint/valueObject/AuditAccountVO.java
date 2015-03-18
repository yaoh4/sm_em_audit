package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;

public class AuditAccountVO extends EmAuditAccountsVw{
	
	public static final String FLAG_NO = "N";
	public static final String ORG_ID_CA = "CA";
	public static final String ROLE_GM_MANAGER = "GM_MANAGER_ROLE";
	public static final String ROLE_ICO_PROGRAM_OFFICIAL = "ICO_PROGRAM_OFFICIAL_ROLE";
	public static final String ROLE_RR_CHIEF = "RR_CHIEF_ROLE";
	public static final String ROLE_UADM_ADMIN = "UADM_ADMIN_ROLE";
	
	public String getFullName() {
		final StringBuffer sb = new StringBuffer(70);

		sb.append(getNedLastName());
		sb.append(", ");
		sb.append(getNedFirstName());

		return sb.toString();
	}
	
	public List<String> getAccountDiscrepancies() {

		List<String> discrepancyList = new ArrayList<String>();
		// Check if there is a violation in roles given to the user
		int role1Count = 0;
		for (EmAuditAccountRolesVw role : getAccountRoles()) {
			if (role.getOrgId().equalsIgnoreCase(ORG_ID_CA)
					&& (role.getRoleName().equalsIgnoreCase(ROLE_GM_MANAGER)
							|| role.getRoleName().equalsIgnoreCase(ROLE_ICO_PROGRAM_OFFICIAL) || role.getRoleName()
							.equalsIgnoreCase(ROLE_RR_CHIEF))) {
				role1Count++;
			}
		}
		int role2Count = 0;
		for (EmAuditAccountRolesVw role : getAccountRoles()) {
			if (role.getOrgId().equalsIgnoreCase(ORG_ID_CA)
					&& (role.getRoleName().equalsIgnoreCase(ROLE_GM_MANAGER)
							|| role.getRoleName().equalsIgnoreCase(ROLE_UADM_ADMIN) || role.getRoleName()
							.equalsIgnoreCase(ROLE_RR_CHIEF))) {
				role2Count++;
			}
		}
		if (role1Count > 1 || role2Count > 1) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_SOD);
		}

		// Check if NED_IC is not NCI
		if (!StringUtils.equalsIgnoreCase(getNedIc(), ApplicationConstants.NED_IC_NCI)) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_IC);
		}

		// Check if NED_ACTIVE_FLAG is N
		if (StringUtils.equalsIgnoreCase(getNedActiveFlag(), FLAG_NO)) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_NED_INACTIVE);
		}

		// Check if last name is different between IMPACII and NED
		if (!StringUtils.equalsIgnoreCase(getNedLastName(), getImpaciiLastName())) {
			discrepancyList.add(ApplicationConstants.DISCREPANCY_CODE_LAST_NAME);
		}
		return discrepancyList;
	}

}
