package gov.nih.nci.cbiit.scimgmt.entmaint.common;

import java.util.Properties;

public class PropertiesManager {
	static Properties prop = null;
	
	static {
		prop = new Properties();
		try{
			prop.load(PropertiesManager.class.getClassLoader().getResourceAsStream("test.properties"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getUrl(){
		return prop.getProperty("url");
	}
	
	public static String getUserId(){
		return prop.getProperty("userId");
	}
	
	public static String getPassword(){
		return prop.getProperty("password");
	}
	
	public static String getAuiditId(){
		return prop.getProperty("auditId");
	}
	
	public static String getConfDir(){
		return prop.getProperty("conf.dir");
	}
	
	public static String getOrgName(){
		return prop.getProperty("orgName");
	}
	
	public static String getI2eAccountId(){
		return prop.getProperty("i2eAccountID");
	}
	
	public static String getI2eDiscrepancyId(){
		return prop.getProperty("i2eDiscrepancyID");
	}
	
	public static String getActionId(){
		return prop.getProperty("actionID");
	}

}
