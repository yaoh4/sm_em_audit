package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
/**
 * This class is for reading column display of search result.
 * @author zhoujim
 *
 */
public class DisplayReader {
	/**
	 * This method initializes the Xstram for xml parser and parse the XML into tab format.
	 * @param fread
	 * @return DisplayObject
	 */
	public DisplayObject readXML(InputStream iread){
		 XStream xstream = new XStream();
		 xstream.processAnnotations(DisplayObject.class);
		 xstream.processAnnotations(Tab.class);
		 DisplayObject dObj = (DisplayObject)xstream.fromXML(iread);
		return dObj;
	}

	/**
	 * This method is for construct hashmap for all columns that will display in search result for all types of account.
	 * Map contains the tab name, and column names and column related properties
	 *  
	 * @param reader
	 * @return Map
	 */
	public Map<String, List<Tab>> getAllTabsAndColumns(InputStream ireader){
		Map<String, List<Tab>> colMap = new HashMap<String, List<Tab>>();
		List<Tab> activeTab = new ArrayList<Tab>();
		List<Tab> newTab = new ArrayList<Tab>();
		List<Tab> deleteTab = new ArrayList<Tab>();
		List<Tab> inactiveTab = new ArrayList<Tab>();
		List<Tab> portfolioTab = new ArrayList<Tab>();
		List<Tab> portfolioDeletedAccounts = new ArrayList<Tab>();
		DisplayObject dObj = readXML(ireader);
		for(Tab t : dObj.getTabs()){
			if(ApplicationConstants.CATEGORY_ACTIVE.equalsIgnoreCase(t.getType())){
				activeTab.add(t);
			}
			if(ApplicationConstants.CATEGORY_NEW.equalsIgnoreCase(t.getType())){
				newTab.add(t);
			}
			if(ApplicationConstants.CATEGORY_DELETED.equalsIgnoreCase(t.getType())){
				deleteTab.add(t);
			}
			if(ApplicationConstants.PORTFOLIOTAB.equalsIgnoreCase(t.getType())){
				portfolioTab.add(t);
			}
			if(ApplicationConstants.CATEGORY_INACTIVE.equalsIgnoreCase(t.getType())){
				inactiveTab.add(t);
			}
			if(ApplicationConstants.PORTFOLIO_DELETED_ACCOUNTS.equalsIgnoreCase(t.getType())){
				portfolioDeletedAccounts.add(t);
			}
		}
		colMap.put(ApplicationConstants.CATEGORY_ACTIVE, activeTab);
		colMap.put(ApplicationConstants.CATEGORY_NEW, newTab);
		colMap.put(ApplicationConstants.CATEGORY_DELETED, deleteTab);
		colMap.put(ApplicationConstants.PORTFOLIOTAB, portfolioTab);
		colMap.put(ApplicationConstants.CATEGORY_INACTIVE, inactiveTab);
		colMap.put(ApplicationConstants.PORTFOLIO_DELETED_ACCOUNTS, portfolioDeletedAccounts);
		
		return colMap;
	}
	
	/** 
	 * For test only
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		FileInputStream file = new FileInputStream("C:/work/eclipse/i2e_ent_maint_temp/properties/displayColumn.xml");
		Map<String, List<Tab>> colMap = new DisplayReader().getAllTabsAndColumns(file);
		Set<String> kset = colMap.keySet();
		for(String s : kset){
			System.out.println("Key : " + s);
		}
	}
}
