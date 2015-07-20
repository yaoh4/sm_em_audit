package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;

import java.util.List;

public interface LookupService {
    /**
     * Gets the specified list 
     * 
     * @param listName
     * @return
     */
    public List getList (String listName);
    
    /**
     * Lists that are cleared every session (Unused)
     */
    public void flushListForSession();
    
    /**
     * Get list element (AppLookup) by code
     * 
     * @param listName
     * @param code
     * @return
     */
    public AppLookupT getAppLookupByCode(String listName, String code);
    
    /**
     * Get list element (AppLookup) by Id
     * 
     * @param listName
     * @param id
     * @return
     */
    public AppLookupT getAppLookupById(String listName, Long id);
    
    /**
     * Generic get list element by code
     * 
     * @param listName
     * @param code
     * @return
     */
    public Object getListObjectByCode(String listName, String code);
    
    /**
     * Get role description given a role name
     * 
     * @param roleName
     * @return
     */
    public String getRoleDescription(String roleName);
    
    /**
     * Flush the cache immediately
     */
    public void refreshLists();
}
