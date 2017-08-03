package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class DbaRolePrivs implements Serializable {

    /** identifier field */
    private String grantee;

    /** identifier field */
    private String grantedRole;

    /** identifier field */
    private String adminOption;

    /** identifier field */
    private String defaultRole;

    /** full constructor */
    public DbaRolePrivs(String grantee, String grantedRole, String adminOption, String defaultRole) {
        this.grantee = grantee;
        this.grantedRole = grantedRole;
        this.adminOption = adminOption;
        this.defaultRole = defaultRole;
    }

    /** default constructor */
    public DbaRolePrivs() {
    }

    public String getGrantee() {
        return this.grantee;
    }

    public void setGrantee(String grantee) {
        this.grantee = grantee;
    }

    public String getGrantedRole() {
        return this.grantedRole;
    }

    public void setGrantedRole(String grantedRole) {
        this.grantedRole = grantedRole;
    }

    public String getAdminOption() {
        return this.adminOption;
    }

    public void setAdminOption(String adminOption) {
        this.adminOption = adminOption;
    }

    public String getDefaultRole() {
        return this.defaultRole;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }


    public String toString() {
        return new ToStringBuilder(this)
            .append("grantee", getGrantee())
            .append("grantedRole", getGrantedRole())
            .append("adminOption", getAdminOption())
            .append("defaultRole", getDefaultRole())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof DbaRolePrivs) ) return false;
        DbaRolePrivs castOther = (DbaRolePrivs) other;
        return new EqualsBuilder()
            .append(this.getGrantee(),     castOther.getGrantee())
            .append(this.getGrantedRole(), castOther.getGrantedRole())
            .append(this.getAdminOption(), castOther.getAdminOption())
            .append(this.getDefaultRole(), castOther.getDefaultRole())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getGrantee())
            .append(getGrantedRole())
            .append(getAdminOption())
            .append(getDefaultRole())
            .toHashCode();
    }

}
