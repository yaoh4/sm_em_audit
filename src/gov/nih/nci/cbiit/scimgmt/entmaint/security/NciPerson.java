package gov.nih.nci.cbiit.scimgmt.entmaint.security;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class NciPerson implements Serializable {

	private static final long serialVersionUID = -4666904675736580667L;

	// unique ldap ID of a person, in the form dn=VasudeSa,ou=6116,ou=nci,o=nih
    private String uid;

    private Integer id;
    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String title;
    private String email;
    private String phone;
    private String phoneExtension;
    private String fax;
    private Address address;

    //  map containing the user attributes populated from LDAP
    //    private Map userAttributesMap;

    private List<String> groupMembership;
    private String adminUnit;
    private String orgAcronym;
    private String commonName;
    private String distinguishedName;
    private String externalOrganization;
    private String fullName;

    private String functionalRole;
    private String manager;
    private String member;
    private String oracleId;
    private String organization;
    private String tfsId;

    public NciPerson() {
    }

    /**
     * @return
     */
    public Integer getId() {
        return id;
    }

    public String getOracleId() {
        return oracleId;
    }

    public String getFullName() {
        return fullName;
    }

    /**
     * @param integer
     */
    public void setId(Integer integer) {
        id = integer;
    }

    /**
     * @param string
     */
    public void setId(int id) {
        this.id = new Integer(id);
    }

    /**
     * Sets the email.
     * @param email The email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the email.
     * @return String
     */
    public String getEmail() {

        return email;
    }

    /**
     * Sets the email.
     * @param email The email to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the email.
     * @return String
     */
    public String getPhone() {

        return phone;
    }

    /**
     * Sets the firstName.
     * @param firstName The firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the firstName.
     * @return String
     */
    public String getFirstName() {

        return firstName;
    }

    /**
     * Returns the Derived fullName.
     * @return String
     */
    public String getDerFullName() {
        String space = " ";
        String fullNameStr = "";

        if (firstName != null) {
            fullNameStr = firstName + space;
        }

        if (middleName != null) {
            fullNameStr += middleName + space;
        }

        if (lastName != null) {
            fullNameStr += lastName;
        }

        if (fullNameStr.equals(""))
            fullNameStr = null;

        return fullNameStr;
    }

    /**
     * Sets the lastName.
     * @param lastName The lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the lastName.
     * @return String
     */
    public String getLastName() {

        return lastName;
    }

    /**
     * Sets the title.
     * @param title The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the title.
     * @return String
     */
    public String getTitle() {

        return title;
    }


    /**
     * @return
     */
    public String getFax() {
        return fax;
    }

    /**
     * @return
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param string
     */
    public void setFax(String string) {
        fax = string;
    }

    /**
     * @param string
     */
    public void setMiddleName(String string) {
        middleName = string;
    }


    /**
     * @return
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param string
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param string
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * @return
     */
    public String getPhoneExtension() {
        return phoneExtension;
    }

    /**
     * @param string
     */
    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof NciPerson)) {
            return false;
        }

        NciPerson bean = (NciPerson)obj;
        int nil = (this.getId() == null) ? 1 : 0;
        nil += (bean.getId() == null) ? 1 : 0;

        if (nil == 2) {
            return true;
        } else if (nil == 1) {
            return false;
        } else {
            return this.getId().equals(bean.getId());
        }

    }


    public int hashCode() {
        return (this.getId() == null) ? 17 : this.getId().hashCode();
    }

    public boolean isEmpty() {
        if (id == null && phone == null && phoneExtension == null && 
            email == null && prefix == null && firstName == null && 
            middleName == null && lastName == null && title == null && 
            fax == null && suffix == null && address == null)
            return true;

        return false;

    }


    /**
     * Sets the adminUnit.
     * @param adminUnit The adminUnit to set
     */
    public void setAdminUnit(String adminUnit) {
        this.adminUnit = adminUnit;
    }

    /**
     * Returns the adminUnit.
     * @return String
     */
    public String getAdminUnit() {
        return adminUnit;
    }

    /**
     * Sets the orgAcronym.
     * @param hncCode The orgAcronym to set
     */
    public void setOrgAcronym(String orgAcronym) {
        this.orgAcronym = orgAcronym;
    }

    /**
     * Returns the hncCode.
     * @return String
     */
    public String getOrgAcronym() {
        return orgAcronym;
    }

    /**
     * Sets the commonName.
     * @param commonName The commonName to set
     */
    public void setLdapId(String commonName) {
        this.commonName = commonName;
    }

    /**
     * Returns the commonName.
     * @return String
     */
    public String getLdapId() {
        return commonName;
    }

    /**
     * Sets the distinguishedName.
     * @param distinguishedName The distinguishedName to set
     */
    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    /**
     * Returns the distinguishedName.
     * @return String
     */
    public String getDistinguishedName() {
        return distinguishedName;
    }


    /**
     * Sets the externalOrganization.
     * @param externalOrganization The externalOrganization to set
     */
    public void setExternalOrganization(String externalOrganization) {
        this.externalOrganization = externalOrganization;
    }

    /**
     * Returns the externalOrganization.
     * @return String
     */
    public String getExternalOrganization() {
        return externalOrganization;
    }

    /**
     * Returns the fullName.
     * @return String
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    /**
     * Sets the manager.
     * @param manager The manager to set
     */
    public void setManager(String manager) {
        this.manager = manager;
    }

    /**
     * Returns the manager.
     * @return String
     */
    public String getManager() {

        return manager;
    }

    /**
     * Sets the memeber.
     * @param memeber The memeber to set
     */
    public void setMember(String member) {
        this.member = member;
    }

    /**
     * Returns the memeber.
     * @return String
     */
    public String getMember() {

        return member;
    }

    /**
     * Sets the oracleId.
     * @param oracleId The oracleId to set
     */
    public void setOracleId(String oracleId) {
        this.oracleId = oracleId;
    }


    /**
     * Sets the tfsId.
     * @param tfsId The tfsId to set
     */
    public void setTfsId(String tfsId) {
        this.tfsId = tfsId;
    }

    /**
     * Returns the tfsId.
     * @return String
     */
    public String getTfsId() {

        return tfsId;
    }

    /**
     * Sets the uid.
     * @param uid The uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Returns the uid.
     * @return String
     */
    public String getUid() {

        return uid;
    }

    /**
     * Sets the functionalRole.
     * @param functionalRole The functionalRole to set
     */
    public void setFunctionalRole(String functionalRole) {
        this.functionalRole = functionalRole;
    }

    /**
     * Returns the functionalRole.
     * @return String
     */
    public String getFunctionalRole() {

        return functionalRole;
    }

    /**
     * Sets the organization.
     * @param organization The organization to set
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * Returns the organization.
     * @return String
     */
    public String getOrganization() {

        return organization;
    }

    /**
     * Sets the groupMembership.
     * @param groupMembership The groupMembership to set
     */
    public void setGroupMembership(List<String> groupMembership) {
        this.groupMembership = groupMembership;
    }

    /**
     * Returns the groupMembership.
     * @return List
     */
    public List<String> getGroupMembership() {

        return this.groupMembership;
    }

    /**
     * Returns true if the role(rolename shd be given in format:cn=GP-ISCS_ORACLE,ou=6116,ou=NCI,o=NIH) is part of the group 
     * @return boolean
     */
    public boolean isRoleInGroupMembership(String role) {
        return getGroupMembership().contains(role);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
