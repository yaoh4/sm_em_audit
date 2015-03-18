package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.PropertyListDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmOrganizationVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LookupServiceImpl implements LookupService {

	@Autowired
	private PropertyListDAO propertyListDAO;
	
	@Autowired
	private EntMaintProperties entMaintProperties;
	
    private GeneralCacheAdministrator cacheAdministrator;
    static Logger logger = Logger.getLogger(LookupServiceImpl.class);

	/**
	 * Looks up the listname for the cache-type and obtains the time interval for retrieving the
	 * values from cache/database
	 * 
	 * @param listname
	 * @return refresh period as an int value
	 */
	private int getRefreshperiod(String listname) {
		
		String cacheType = entMaintProperties.getProperty(listname);
		
		if (cacheType.equalsIgnoreCase("NoCache")) {
			return -1;
		} else if (cacheType.equalsIgnoreCase("NoRefresh")) {
			return -2;
		} else {
			String cacheDuration = entMaintProperties.getProperty(cacheType);
			return Integer.valueOf(cacheDuration).intValue();
		}
	}

	/**
	 * Retrieved the list values from Cache ,databaseTable or derive within the application Code.
	 * 
	 * @param listName
	 * @return List
	 */
	public List< ? extends Object> getList(String listName) {
	  	List result = null;
		boolean updated = false; // int myRefreshPeriod = 86400; //set refresh period to one day
		int refreshPeriod = 0;
		if(StringUtils.startsWithIgnoreCase(listName, ApplicationConstants.GLOBAL_LOOKUP_LIST)) {
			refreshPeriod = getRefreshperiod(ApplicationConstants.GLOBAL_LOOKUP_LIST);
		} else {
			refreshPeriod = getRefreshperiod(listName);
		}
                
        try {
			// Get from the cache
			if (refreshPeriod >= 0) { // LongDuartion,ShortDuration and runtime Derived values(Form
									// ArrayLists)
				result = (List) cacheAdministrator.getFromCache(listName, refreshPeriod);
			} else if (refreshPeriod == -1) { // NeverCache-always hit DB
			     	result = search(listName);
			} else if (refreshPeriod == -2) { // NeverRefresh-load once pr app start!
			        result = (List) cacheAdministrator.getFromCache(listName);
			}
		} catch (NeedsRefreshException nre) { // Get the value (probably from the database)
			try {
				// define the value as 'NoRefresh' in the cache.properties file
				// for the lists generated dynamically in the application code.								
				
				result = search(listName);
				
				if(listName.equalsIgnoreCase(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST)) {
					EmOrganizationVw org = new EmOrganizationVw(ApplicationConstants.ORG_PATH_NON_NCI);
					result.add(org);
				}
				
				// Store in the cache..
				// on refreshperiod=-1 dont put into cache.Hit DB always
				if (refreshPeriod != -1) {
					cacheAdministrator.putInCache(listName, result);
					Map<String, Object> resultMap = new HashMap<String, Object>();
					for (Object element: result) {
						try {
							String c = BeanUtils.getProperty(element,"code");
							if(!StringUtils.isBlank(c)) {
								resultMap.put(c, element);
							}
						} catch (IllegalAccessException e) {
							break;
						} catch (InvocationTargetException e) {
							break;
						} catch (NoSuchMethodException e) {
							break;
						}
					}
					if(!resultMap.isEmpty()) {
						cacheAdministrator.putInCache(listName+"Map", resultMap);
					}
					updated = true;
				}
			} finally {
				if (!updated) {
					// It is essential that cancelUpdate is called if the
					// cached content could not be rebuilt
					cacheAdministrator.cancelUpdate(listName);
				}
			}
		}
		return result;
	}

	/**
	 * Retrieved the list values from Cache ,databaseTable or derive within the application Code.
	 * 
	 * @param listName
	 * @return List
	 */
	public Map<String, ? extends Object> getListMap(String listName) {
	  	List result = null;
	  	Map resultMap = null;
		boolean updated = false; // int myRefreshPeriod = 86400; //set refresh period to one day
		int refreshPeriod = 0;
		if(StringUtils.startsWithIgnoreCase(listName, ApplicationConstants.GLOBAL_LOOKUP_LIST)) {
			refreshPeriod = getRefreshperiod(ApplicationConstants.GLOBAL_LOOKUP_LIST);
		} else {
			refreshPeriod = getRefreshperiod(listName);
		}
                
        try {
			// Get from the cache
			if (refreshPeriod >= 0) { // LongDuartion,ShortDuration and runtime Derived values(Form
									// ArrayLists)
				resultMap = (Map) cacheAdministrator.getFromCache(listName+"Map", refreshPeriod);
			} else if (refreshPeriod == -1) { // NeverCache-always hit DB
			     	result = search(listName);
			} else if (refreshPeriod == -2) { // NeverRefresh-load once pr app start!
				resultMap = (Map) cacheAdministrator.getFromCache(listName+"Map");
			}
		} catch (NeedsRefreshException nre) { // Get the value (probably from the database)
			try {
				// define the value as 'NoRefresh' in the cache.properties file
				// for the lists generated dynamically in the application code.								
				
				result = search(listName);
				
				if(listName.equalsIgnoreCase(ApplicationConstants.ORGANIZATION_DROPDOWN_LIST)) {
					EmOrganizationVw org = new EmOrganizationVw(ApplicationConstants.ORG_PATH_NON_NCI);
					result.add(org);
				}
				
				// Store in the cache..
				// on refreshperiod=-1 dont put into cache.Hit DB always
				if (refreshPeriod != -1) {
					cacheAdministrator.putInCache(listName, result);
					Map<String, Object> map = new HashMap<String, Object>();
					for (Object element: result) {
						try {
							String c = BeanUtils.getProperty(element,"code");
							if(!StringUtils.isBlank(c)) {
								map.put(c, element);
							}
						} catch (IllegalAccessException e) {
							break;
						} catch (InvocationTargetException e) {
							break;
						} catch (NoSuchMethodException e) {
							break;
						}
					}
					if(!map.isEmpty()) {
						cacheAdministrator.putInCache(listName+"Map", map);
						resultMap = map;
					}
					updated = true;
				}
			} finally {
				if (!updated) {
					// It is essential that cancelUpdate is called if the
					// cached content could not be rebuilt
					cacheAdministrator.cancelUpdate(listName);
				}
			}
		}
		return resultMap;
	}
	
	public void flushListForSession() {
		// These two lists are refreshed every session
	}

	private List search(String listName) {
//		logger.debug("Searching for list by name: " + listName);
		return propertyListDAO.retrieve(listName);
	}
	
	public void setPropertyListDAO(PropertyListDAO acrPropertyListDAO) {
		this.propertyListDAO = acrPropertyListDAO;
	}

	public PropertyListDAO getPropertyListDAO() {
		return propertyListDAO;
	}

	public void setCacheAdministrator(GeneralCacheAdministrator cacheAdministrator) {
		this.cacheAdministrator = cacheAdministrator;
	}
	
	public GeneralCacheAdministrator getCacheAdministrator() {
		return cacheAdministrator;
	}
   
	/**
	 * Get AppLookupT by list name and code
	 * 
	 * @param listName
	 * @param code
	 * @return
	 */
	public AppLookupT getAppLookupByCode(String listName, String code) {
		Map<String, AppLookupT> map = (Map<String, AppLookupT>) getListMap(listName);
		if(map != null) {
			return map.get(code);
		}
		return null;
	}
	
	/**
	 * Get object in a list by list name and code
	 * 
	 * @param listName
	 * @param code
	 * @return
	 */
	public Object getListObjectByCode(String listName, String code) {
		Map<String, ? extends Object> map = (Map<String, ? extends Object>) getListMap(listName);
		if(map != null) {
			return map.get(code);
		}
		return null;
	}
	
	/**
	 * Get AppLookupT by id
	 * 
	 * @param discriminator
	 * @param code
	 * @return
	 */
	public AppLookupT getAppLookupById(String listName, Long id) {
		List<AppLookupT> list = (List<AppLookupT>) getList(listName);
		for (AppLookupT element : list) {
			if (element.getId().longValue() == id.longValue())
				return element;
		}
		return null;
	}

	/**
	 * Get Role description for the help pop-up
	 * 
	 * @param roleName
	 * @return description text
	 */
	@Override
	public String getRoleDescription(String roleName) {
		// TODO Auto-generated method stub
		return "Dummy Role Description";
	}
}
