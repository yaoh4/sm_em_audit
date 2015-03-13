package gov.nih.nci.cbiit.scimgmt.entmaint.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class GwbLinksT implements Serializable {

    /** identifier field */
    private java.lang.Long id;

    /** persistent field */
    private java.lang.String linkType;

    /** nullable persistent field */
    private java.lang.String linkServer;

    /** persistent field */
    private java.lang.String linkPath;

    /** nullable persistent field */
    private java.lang.String name;

    /** persistent field */
    private java.lang.String displayName;

    /** persistent field */
    private java.lang.String activeFlag;

    /** nullable persistent field */
    private java.lang.String protocol;

    /** persistent field */
    private java.lang.String createUserId;

    /** persistent field */
    private java.util.Date createDate;

    /** nullable persistent field */
    private java.lang.String lastChangeUserId;

    /** nullable persistent field */
    private java.util.Date lastChangeDate;

    /** nullable persistent field */
    private java.lang.Integer updateStamp;

    /** full constructor */
    public GwbLinksT(java.lang.Long id, java.lang.String linkType, java.lang.String linkServer, java.lang.String linkPath, java.lang.String name, java.lang.String displayName, java.lang.String activeFlag, java.lang.String protocol, java.lang.String createUserId, java.util.Date createDate, java.lang.String lastChangeUserId, java.util.Date lastChangeDate, java.lang.Integer updateStamp) {
        this.id = id;
        this.linkType = linkType;
        this.linkServer = linkServer;
        this.linkPath = linkPath;
        this.name = name;
        this.displayName = displayName;
        this.activeFlag = activeFlag;
        this.protocol = protocol;
        this.createUserId = createUserId;
        this.createDate = createDate;
        this.lastChangeUserId = lastChangeUserId;
        this.lastChangeDate = lastChangeDate;
        this.updateStamp = updateStamp;
    }

    /** default constructor */
    public GwbLinksT() {
    }

    /** minimal constructor */
    public GwbLinksT(java.lang.Long id, java.lang.String linkType, java.lang.String linkPath, java.lang.String displayName, java.lang.String activeFlag, java.lang.String createUserId, java.util.Date createDate) {
        this.id = id;
        this.linkType = linkType;
        this.linkPath = linkPath;
        this.displayName = displayName;
        this.activeFlag = activeFlag;
        this.createUserId = createUserId;
        this.createDate = createDate;
    }

    /**
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.Long"
     *             column="ID"
     *
     */
    public java.lang.Long getId() {
        return this.id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    /**
     *            @hibernate.property
     *             column="LINK_TYPE"
     *             length="1"
     *             not-null="true"
     *
     */
    public java.lang.String getLinkType() {
        return this.linkType;
    }

    public void setLinkType(java.lang.String linkType) {
        this.linkType = linkType;
    }

    /**
     *            @hibernate.property
     *             column="LINK_SERVER"
     *             length="100"
     *
     */
    public java.lang.String getLinkServer() {
        return this.linkServer;
    }

    public void setLinkServer(java.lang.String linkServer) {
        this.linkServer = linkServer;
    }

    /**
     *            @hibernate.property
     *             column="LINK_PATH"
     *             length="500"
     *             not-null="true"
     *
     */
    public java.lang.String getLinkPath() {
        return this.linkPath;
    }

    public void setLinkPath(java.lang.String linkPath) {
        this.linkPath = linkPath;
    }

    /**
     *            @hibernate.property
     *             column="NAME"
     *             length="100"
     *
     */
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     *            @hibernate.property
     *             column="DISPLAY_NAME"
     *             length="100"
     *             not-null="true"
     *
     */
    public java.lang.String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    /**
     *            @hibernate.property
     *             column="ACTIVE_FLAG"
     *             length="1"
     *             not-null="true"
     *
     */
    public java.lang.String getActiveFlag() {
        return this.activeFlag;
    }

    public void setActiveFlag(java.lang.String activeFlag) {
        this.activeFlag = activeFlag;
    }

    /**
     *            @hibernate.property
     *             column="PROTOCOL"
     *             length="30"
     *
     */
    public java.lang.String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(java.lang.String protocol) {
        this.protocol = protocol;
    }

    /**
     *            @hibernate.property
     *             column="CREATE_USER_ID"
     *             length="30"
     *             not-null="true"
     *
     */
    public java.lang.String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(java.lang.String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     *            @hibernate.property
     *             column="CREATE_DATE"
     *             length="7"
     *             not-null="true"
     *
     */
    public java.util.Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    /**
     *            @hibernate.property
     *             column="LAST_CHANGE_USER_ID"
     *             length="30"
     *
     */
    public java.lang.String getLastChangeUserId() {
        return this.lastChangeUserId;
    }

    public void setLastChangeUserId(java.lang.String lastChangeUserId) {
        this.lastChangeUserId = lastChangeUserId;
    }

    /**
     *            @hibernate.property
     *             column="LAST_CHANGE_DATE"
     *             length="7"
     *
     */
    public java.util.Date getLastChangeDate() {
        return this.lastChangeDate;
    }

    public void setLastChangeDate(java.util.Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    /**
     *            @hibernate.property
     *             column="UPDATE_STAMP"
     *             length="5"
     *
     */
    public java.lang.Integer getUpdateStamp() {
        return this.updateStamp;
    }

    public void setUpdateStamp(java.lang.Integer updateStamp) {
        this.updateStamp = updateStamp;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof GwbLinksT) ) return false;
        GwbLinksT castOther = (GwbLinksT) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }
}
