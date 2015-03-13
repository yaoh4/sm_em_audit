package gov.nih.nci.cbiit.scimgmt.entmaint.actions;


import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.helper.action.AuditSearchActionHelper;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;


/**
 * Base action class for all Struts action classes in the application All Struts action classes
 * should extend this base class at a minimum.
 * 
 * @author menons2
 *
 */
@SuppressWarnings("serial")
public class BaseAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware, SessionAware {

	static Logger logger = Logger.getLogger(BaseAction.class);

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext servletContext;
	protected Map<String, Object> session;

	protected AuditSearchVO searchVO;		
	protected boolean showResult;
	protected String formAction;
	protected List<Tab> displayColumn;
	protected AuditSearchActionHelper auditSearchActionHelper = new AuditSearchActionHelper();
	protected List<DropDownOption> organizationList = new ArrayList<DropDownOption>();
	
	protected InputStream inputStream;
	
	@Autowired
	protected LookupService lookupService;	
	@Autowired
	protected NciUser nciUser;
	@Autowired
	protected EntMaintProperties entMaintProperties;
	
    public InputStream getInputStream() {
        return inputStream;
    }

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.
	 * HttpServletRequest)
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}


	
	/**
	 * Sets the servlet context.
	 * 
	 * @param context
	 *            the new servlet context
	 */
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	/**
	 * Gets the servlet context.
	 * 
	 * @return the servletContext
	 */
	protected ServletContext getServletContext() {
		return servletContext;
	}

	
	/**
	 * removes the attribute from the session.
	 * 
	 * @param sessionKey
	 *            the session key
	 */
	public void removeAttributeFromSession(String sessionKey) {
		// Map<String, Object> session = ActionContext.getContext().getSession();
		session.remove(sessionKey);
	}

	/**
	 * Sets attribute in session.
	 * 
	 * Note that this method should be used sparingly, hence the deprecated annotation.
	 * 
	 * @param sessionKey
	 *            the session key
	 * @param sessionAttribute
	 *            the session attribute
	 */
	public void setAttributeInSession(String sessionKey,
										Object sessionAttribute) {
		// Map<String, Object> session = ActionContext.getContext().getSession();
		session.put(sessionKey, sessionAttribute);
	}

  
    /**
	 * Gets the attribute from session.
	 * 
	 * @param sessionKey
	 *            the session key
	 * @return the attribute from session
	 */
	public Object getAttributeFromSession(String sessionKey) {
		// Map<String, Object> session = ActionContext.getContext().getSession();
		Object sessioAttribute = null;

		if (session.containsKey(sessionKey)) {
			sessioAttribute = session.get(sessionKey);

		}
		return sessioAttribute;
	}

	/**
	 * Sets attribute in session.
	 * 
	 * @param requestKey
	 *            the request key
	 * @param requestAttribute
	 *            the request attribute
	 */
	public void setAttributeInRequest(String requestKey,
										Object requestAttribute) {

		request.setAttribute(requestKey, requestAttribute);
	}

	/**
	 * Gets the attribute from request.
	 * 
	 * @param requestKey
	 *            the request key
	 * @return the attribute from request
	 */
	public Object getAttributeFromRequest(String requestKey) {
		return request.getAttribute(requestKey);
	}

	
	/**
	 * Gets the user information.
	 * 
	 * @return the user information
	 */
	public NciUser getUserInformation() {
		return (NciUser) session.get(ApplicationConstants.SESSION_USER);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
	 */
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	
	/**
	 * Gets the session.
	 * 
	 * @return the session map.
	 */
	public Map<String, Object> getSession() {
		return session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.struts2.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http
	 * .HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	/**
	 * @return the searchVO
	 */
	public AuditSearchVO getSearchVO() {
		return searchVO;
	}

	/**
	 * @param searchVO the searchVO to set
	 */
	public void setSearchVO(AuditSearchVO searchVO) {
		this.searchVO = searchVO;
	}
		
	/**
	 * @return the organizationList
	 */
	public List<DropDownOption> getOrganizationList() {
		return organizationList;
	}

	/**
	 * @param organizationList the organizationList to set
	 */
	public void setOrganizationList(List<DropDownOption> organizationList) {
		this.organizationList = organizationList;
	}
	
	/**
	 * @return the showResult
	 */
	public boolean isShowResult() {
		return showResult;
	}

	/**
	 * @param showResult the showResult to set
	 */
	public void setShowResult(boolean showResult) {
		this.showResult = showResult;
	}

	/**
	 * @return the formAction
	 */
	public String getFormAction() {
		return formAction;
	}

	/**
	 * @param formAction the formAction to set
	 */
	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}

	/**
	 * @return the displayColumn
	 */
	public List<Tab> getDisplayColumn() {
		return displayColumn;
	}

	/**
	 * @param displayColumn the displayColumn to set
	 */
	public void setDisplayColumn(List<Tab> displayColumn) {
		this.displayColumn = displayColumn;
	}

	/**
	 * Gets value from app properties
	 * 
	 * @return
	 */
	public String getPropertyValue(String key) {
		return entMaintProperties.getProperty(key);
	}
	
}
