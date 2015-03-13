package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import gov.nih.nci.cbiit.scimgmt.entmaint.exceptions.UserLoginException;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;

import java.util.List;

public interface LdapServices {

    public NciUser verifyNciUserWithRole(String remoteUser) throws UserLoginException;
}
