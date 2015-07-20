package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import gov.nih.nci.cbiit.scimgmt.entmaint.actions.BaseAction;
import gov.nih.nci.iscs.infra.ctxdispenser.DirCtxDispenser;
import gov.nih.nci.iscs.infra.ctxdispenser.DirCtxDispenserConfigurator;
import gov.nih.nci.iscs.infra.ctxdispenser.DirCtxDispenserException;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;

import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

public class LdapUtil {
  
    private DirContext ctx;
    private String stBaseSearchDN;
    private String stSearchAttribute;
    private String configEnvironment;
    
    static Logger logger = Logger.getLogger(BaseAction.class);

    public LdapUtil() {
    }

    public LdapUtil(String stBaseSearchDN, 
                    String stSearchAttribute) throws DirCtxDispenserException {
        ctx = null;
        this.stBaseSearchDN = null;
        this.stSearchAttribute = null;
        configEnvironment = null;
        ctx = getDirContext();
        this.stBaseSearchDN = stBaseSearchDN;
        this.stSearchAttribute = stSearchAttribute;
    }

    public LdapUtil(String stBaseSearchDN, String stSearchAttribute, 
                    String configEnvironment) throws DirCtxDispenserException {
        ctx = null;
        this.stBaseSearchDN = null;
        this.stSearchAttribute = null;
        this.configEnvironment = null;
        this.stBaseSearchDN = stBaseSearchDN;
        this.stSearchAttribute = stSearchAttribute;
        this.configEnvironment = configEnvironment;
        ctx = getDirContext();
    }

    private DirContext getDirContext() throws DirCtxDispenserException {
        logger.debug("Loading the context");
        DirCtxDispenser disp = new DirCtxDispenser();
        java.util.List configList = null;
        if (configEnvironment == null) {
            configList = DirCtxDispenserConfigurator.getConfig();
        } else {
            try {
                configList = 
                        DirCtxDispenserConfigurator.getConfig(configEnvironment, 
                                                              null);
            } catch (DirCtxDispenserException de) {
                logger.error(de);
                logger.info("Trying the default configuration.");
                configList = DirCtxDispenserConfigurator.getConfig();
            }
        }
        disp.init(configList);
        ctx  = disp.getContext();
        logger.debug("Context loaded.");
        return ctx;
    }

    

    public void finalize() {
        try {
            ctx.close();
        } catch (Exception e) {
        }
    }

    
    public synchronized String getUserFDN(String stUserID) throws Exception {
        try {
            return (getFullDN(ctx, stBaseSearchDN, stSearchAttribute, stUserID, 
                              false));
        } catch (Exception e) {
            ctx = getDirContext();
            return (getFullDN(ctx, stBaseSearchDN, stSearchAttribute, stUserID, 
                              false));
        }
    }
    

    public synchronized Attributes getAttributes(String stFDN, 
                                                 String[] stAttrDirIDs) throws Exception {
        try {
            return (ctx.getAttributes(stFDN, stAttrDirIDs));
        } catch (Exception e) {
            ctx = getDirContext();
            return (ctx.getAttributes(stFDN, stAttrDirIDs));
        }
    }


    private String getFullDN(DirContext ctx, String stBaseSearchDN, 
                             String stSearchAttrib, String stAttribVal, 
                             boolean bExactMatch) throws Exception {
        String searchFilter = "(" + stSearchAttrib + "=" + stAttribVal;
        if (bExactMatch) {
            searchFilter = searchFilter + ")";
        } else {
            searchFilter = searchFilter + "*)";
        }
        SearchControls ctls = new SearchControls();
        ctls.setSearchScope(2);
        String stReturnVal = null;
        NamingEnumeration searchEnum = 
            ctx.search(stBaseSearchDN, searchFilter, ctls);
        if (searchEnum.hasMore()) {
            SearchResult sr = (SearchResult)searchEnum.next();
            stReturnVal = sr.getName() + "," + stBaseSearchDN;
        }
        if (stReturnVal == null) {
            throw new Exception("Could not find entry in LDAP server.");
        } else {
            return stReturnVal;
        }
    }


    public void setStBaseSearchDN(String stBaseSearchDN) {
        this.stBaseSearchDN = stBaseSearchDN;
    }

    public String getStBaseSearchDN() {
        return stBaseSearchDN;
    }
    
    public void setStSearchAttribute(String stSearchAttribute) {
        this.stSearchAttribute = stSearchAttribute;
    }

    public String getStSearchAttribute() {
        return stSearchAttribute;
    }

    public void setConfigEnvironment(String configEnvironment) {
        this.configEnvironment = configEnvironment;
    }

    public String getConfigEnvironment() {
        return configEnvironment;
    }


}
