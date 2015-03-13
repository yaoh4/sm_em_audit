package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppPropertiesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.ApplicationService;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The Class EntMaintProperties.
 */
@Component
@Scope("singleton")
@SuppressWarnings("serial")
public class EntMaintProperties extends Properties {
	
	static Logger logger = Logger.getLogger(EntMaintProperties.class);

	@Autowired
	private ApplicationService applicationService;
	
	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		try {
			
			logger.info("Initing EntMaintProperties");
			
			for (AppPropertiesT a : applicationService.getAppPropertiesList()) {
				setProperty(a.getPropKey(), a.getPropValue());
			}
			// Load property file from conf.dir directory
			this.load(this.getClass().getResourceAsStream("/application.properties"));
			this.load(this.getClass().getResourceAsStream("/messages.properties"));
			this.load(this.getClass().getResourceAsStream("/cache.properties"));
					
			String confDirLocation = System.getProperty("conf.dir");
			logger.debug("=====> conf.dir=" + confDirLocation);
			FileInputStream file =
					new FileInputStream(confDirLocation + "/entmaint/entmaint.properties");
			this.load(file);

			
			logger.info("Completed init of EntMaintProperties");

		} catch (IOException ie) {
			logger.error("Error loading properties file");
			throw new RuntimeException("Error reading properties file", ie);
		}
	}


	/**
	 * Gets the property value.
	 * 
	 * @param propertyName
	 *            the propertyName
	 * @return the property value
	 */
	public String getPropertyValue(String propertyName) {
		String result = getProperty(propertyName);
		
		return result;
	}
}
