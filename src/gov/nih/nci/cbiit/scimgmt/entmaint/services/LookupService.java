package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;

import java.util.List;

public interface LookupService {
    public List getList (String listName);
    public void flushListForSession();
    public AppLookupT getAppLookupByCode(String listName, String code);
    public AppLookupT getAppLookupById(String listName, Long id);
    public Object getListObjectByCode(String listName, String code);
    public String getRoleDescription(String roleName);
    public void refreshLists();
}
