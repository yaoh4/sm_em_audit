package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import java.util.List;

public interface LookupService {
    public List getList (String listName);
    public void flushListForSession();

}
