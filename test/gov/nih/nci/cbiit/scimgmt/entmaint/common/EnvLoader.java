package gov.nih.nci.cbiit.scimgmt.entmaint.common;

import javax.annotation.PostConstruct;

public class EnvLoader {
	
	@PostConstruct
	public void init() {
		String conf_path = PropertiesManager.getConfDir();
		System.setProperty("conf.dir", conf_path);
	}

}
