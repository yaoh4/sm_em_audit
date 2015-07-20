package gov.nih.nci.cbiit.scimgmt.entmaint.tags;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.GwbLinksT;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;





import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class WorkbenchApplicationLinkTag extends BodyTagSupport {
    private String m_Application;
    private String m_AdditionalParams = "false";
    private String m_AdditionalUrlText;
    private String m_javascript;
    private String m_windowparams;
    private String m_ApplicationKey;
    private GwbLinksT currentApplicationLink;
    
    
    static Logger logger = Logger.getLogger(WorkbenchApplicationLinkTag.class);

    public void setApplication(String application) {
        m_Application = application;
    }

    public void setAdditionalUrlText(String pAdditionalUrlText) {
        m_AdditionalUrlText = pAdditionalUrlText;
    }

    public void setAdditionalParams(String pAdditionalParams) {
        m_AdditionalParams = pAdditionalParams;
    }

    public void setPopScript(String pScript) {
        m_javascript = pScript;
    }

    public void setWindowParams(String pWindowParams) {
        m_windowparams = pWindowParams;
    }

    public void setApplicationKey(String pApplicationKey) {
        m_ApplicationKey = pApplicationKey;
    }

    public int doStartTag() throws JspTagException {
    	logger.info("Entering doStartTag() of WorkbenchApplicationLinkTag");
        StringBuffer buf = new StringBuffer();

        try {
        	
            JspWriter out = pageContext.getOut();
            ServletContext application = pageContext.getServletContext();
            LookupService lookupService = (LookupService)application.getAttribute("lookupService");
             
            List appLinkresults = lookupService.getList(ApplicationConstants.APP_LINK_LIST);
            
            Iterator mIterator = appLinkresults.iterator();
            while (mIterator.hasNext()) {
                currentApplicationLink = (GwbLinksT)mIterator.next();            
                if(currentApplicationLink.getName().equalsIgnoreCase(m_Application)){
                    break;
                }
            }

            if (currentApplicationLink != null) {
                String thisUrl =
                    currentApplicationLink.getProtocol() + "://" + currentApplicationLink.getLinkServer() +
                    currentApplicationLink.getLinkPath();
                buf.append("<a  class=newWindow href=\'" + thisUrl +
                           "\'" + "   target=_blank" + ">");
            }
            out.print(buf.toString());
        } catch (Exception e) {
            logger.error(e);
        }
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() throws JspTagException {
      try {
         JspWriter out = pageContext.getOut();
         if (currentApplicationLink != null) {
            out.print("</a>");
         }
      }
      catch (Exception e) {
          logger.error(e);
          throw new JspTagException("Error :"+e.getMessage());
      }
      return SKIP_BODY;
    }
    public int doEndTag() throws JspTagException
    {
      try
      {
        if(bodyContent != null) // Check if we even entered the body
          bodyContent.writeOut(bodyContent.getEnclosingWriter());
      }
      catch(java.io.IOException e)
      {
        logger.error(e);
        throw new JspTagException("IO Error: " + e.getMessage());
      }
      return EVAL_PAGE;
    }

}
