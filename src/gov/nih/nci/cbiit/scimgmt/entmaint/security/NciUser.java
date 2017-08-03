package gov.nih.nci.cbiit.scimgmt.entmaint.security;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.I2eActiveUserRolesVw;

public class NciUser extends NciPerson {

	/** current user role of the  the User */
    private String currentUserRole;

    /** org path of the User */
    private String orgPath;
    
    private String userId;

    /** identifier field */
   // private Long personId; changed to String for UserRoleDAO
    private String personId;
   
    private String activeFlag;

    private List<I2eActiveUserRolesVw> appRoles = new ArrayList<I2eActiveUserRolesVw>();

	private boolean readOnly = false;
	
    public NciUser() {
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String newUserId) {
        userId = newUserId;
    }

    public void setCurrentUserRole(String userRole) {
        this.currentUserRole = userRole;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }


    public String toString() {
        return new StringBuilder().append(this.getUserId()).append("\n").append(this.getFirstName()).append("\n").append(this.getLastName()).append("\n").append(this.getFullName()).append("\n").append(this.getEmail()).append("\n").toString();
    }


    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonId() {
        return personId;
    }

	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	/**
	 * @return the activeFlag
	 */
	public String getActiveFlag() {
		return activeFlag;
	}

	/**
	 * @param activeFlag the activeFlag to set
	 */
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public List<I2eActiveUserRolesVw> getAppRoles() {
		return appRoles;
	}

	public void setAppRoles(List<I2eActiveUserRolesVw> appRoles) {
		this.appRoles = appRoles;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}
