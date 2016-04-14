package gov.nih.nci.cbiit.scimgmt.entmaint.listeners;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DisplayReader;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class CommonContextListener implements ServletContextListener {
    public CommonContextListener() {
    }

    /*This method is invoked when the Web Application has been removed
    and is no longer able to accept requests
    */

    public void contextDestroyed(ServletContextEvent event) {

    }

    //This method is invoked when the Web Application
    //is ready to service requests

    public void contextInitialized(ServletContextEvent event) {
        //put lookup service to application scope to be used by jsp
        ServletContext context = event.getServletContext();
        context.setAttribute("lookupService", 
                WebApplicationContextUtils.getWebApplicationContext(context).getBean(ApplicationConstants.LOOKUP_SERVICE));
        //set up tab result column name for search
        InputStream iStream = this.getClass().getClassLoader().getResourceAsStream("/displayColumn.xml");
        DisplayReader dread = new DisplayReader();
        Map<String, List<Tab>> tabMap = dread.getAllTabsAndColumns(iStream);
        context.setAttribute(ApplicationConstants.COLUMNSATTRIBUTE, tabMap);
        iStream = this.getClass().getClassLoader().getResourceAsStream("/reportColumn.xml");
        dread = new DisplayReader();
        tabMap = dread.getAllTabsAndColumns(iStream);
        context.setAttribute(ApplicationConstants.REPORTCOLATTRIBUTE, tabMap);
        iStream = this.getClass().getClassLoader().getResourceAsStream("/i2eColumn.xml");
        dread = new DisplayReader();
        tabMap = dread.getAllTabsAndColumns(iStream);
        context.setAttribute(ApplicationConstants.I2ECOLATTRIBUTE, tabMap);
        iStream = this.getClass().getClassLoader().getResourceAsStream("/discrepancyColumn.xml");
        dread = new DisplayReader();
        tabMap = dread.getAllTabsAndColumns(iStream);
        context.setAttribute(ApplicationConstants.DISCREPANCYCOLATTRIBUTE, tabMap);
    }
}
