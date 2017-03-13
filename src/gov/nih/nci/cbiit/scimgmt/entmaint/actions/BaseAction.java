package gov.nih.nci.cbiit.scimgmt.entmaint.actions;


import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.helper.action.AuditSearchActionHelper;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DashboardData;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.Tab;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
	protected boolean anchorDash = false;
	protected String formAction;
	protected List<Tab> displayColumn;
	protected AuditSearchActionHelper auditSearchActionHelper = new AuditSearchActionHelper();
	protected List<DropDownOption> organizationList = new ArrayList<DropDownOption>();
	protected List<DropDownOption> transferOrgList = new ArrayList<DropDownOption>();
	protected List<DropDownOption> auditPeriodList = new ArrayList<DropDownOption>();
	protected InputStream inputStream;
	protected int changePageSize;
	private DashboardData icDashboard;
	
	@Autowired
	protected LookupService lookupService;	
	@Autowired
	protected AdminService adminService;
	@Autowired
	protected NciUser nciUser;
	@Autowired
	protected EntMaintProperties entMaintProperties;
	
    public InputStream getInputStream() {
        return inputStream;
    }

	
	/* 
	 * (non-Javadoc)
	 *  @see 
	 *  org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.
	 * HttpServletRequest)
	 * 
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}


	
	/**
	 * Sets the servlet context.
	 * 
	 * @param context the new servlet context
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
	 * @param sessionKey the session key
	 */
	public void removeAttributeFromSession(String sessionKey) {
		session.remove(sessionKey);
	}

	/**
	 * Sets attribute in session.
	 * 
	 * @param sessionKey the session key
	 * @param sessionAttribute the session attribute
	 */
	public void setAttributeInSession(String sessionKey,
										Object sessionAttribute) {
		if(session != null){
			session.put(sessionKey, sessionAttribute);
		}
	}

  
    /**
	 * Gets the attribute from session.
	 * 
	 * @param sessionKey the session key
	 * 
	 * @return the attribute from session
	 */
	public Object getAttributeFromSession(String sessionKey) {
		
		Object sessioAttribute = null;

		if (session.containsKey(sessionKey)) {
			sessioAttribute = session.get(sessionKey);

		}
		return sessioAttribute;
	}

	/**
	 * Sets attribute in session.
	 * 
	 * @param requestKey the request key
	 * @param requestAttribute the request attribute
	 */
	public void setAttributeInRequest(String requestKey,
										Object requestAttribute) {

		request.setAttribute(requestKey, requestAttribute);
	}

	/**
	 * Gets the attribute from request.
	 * 
	 * @param requestKey the request key
	 * 
	 * @return the attribute from request
	 */
	public Object getAttributeFromRequest(String requestKey) {
		return request.getAttribute(requestKey);
	}

	
	/**
	 * Set the session
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
	 * Get the searchVO
	 * 
	 * @return the searchVO
	 */
	public AuditSearchVO getSearchVO() {
		return searchVO;
	}

	/**
	 * Set searchVO
	 * 
	 * @param searchVO the searchVO to set
	 */
	public void setSearchVO(AuditSearchVO searchVO) {
		this.searchVO = searchVO;
	}
		
	/**
	 * Get organization list.
	 * 
	 * @return the organizationList
	 */
	public List<DropDownOption> getOrganizationList() {
		return organizationList;
	}

	/**
	 * Set the organization list.
	 * 
	 * @param organizationList the organizationList to set
	 */
	public void setOrganizationList(List<DropDownOption> organizationList) {
		this.organizationList = organizationList;
	}
	
	/**
	 * Get showResult
	 * 
	 * @return the showResult
	 */
	public boolean isShowResult() {
		return showResult;
	}

	/**
	 * Set showResult.
	 * 
	 * @param showResult the showResult to set
	 */
	public void setShowResult(boolean showResult) {
		this.showResult = showResult;
	}

	/**
	 * Get the formAction
	 * 
	 * @return the formAction
	 */
	public String getFormAction() {
		return formAction;
	}

	/**
	 * Set the formAction
	 * 
	 * @param formAction the formAction to set
	 */
	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}

	/**
	 * Get displayColumn.
	 * 
	 * @return the displayColumn
	 */
	public List<Tab> getDisplayColumn() {
		return displayColumn;
	}

	/**
	 * Set displayColumn.
	 * 
	 * @param displayColumn the displayColumn to set
	 */
	public void setDisplayColumn(List<Tab> displayColumn) {
		this.displayColumn = displayColumn;
	}

	/**
	 * Gets value from app properties
	 * 
	 * @return the value of the given key.
	 */
	public String getPropertyValue(String key) {
		return entMaintProperties.getProperty(key);
	}
	
	/**
	 * Get changePageSize value
	 * 
	 * @return the changePageSize
	 */
	public int getChangePageSize() {
		return changePageSize;
	}


	/**
	 * Set changePageSize value
	 * 
	 * @param changePageSize the changePageSize to set
	 */
	public void setChangePageSize(int changePageSize) {
		this.changePageSize = changePageSize;
	}


	/**
	 * Check if logged in user is a superuser.
	 * 
	 * @return true if logged in user is a superuser, false otherwise.
	 * 
	 */
	public boolean isSuperUser(){
		boolean isSuperUser = false;
		if(ApplicationConstants.USER_ROLE_SUPER_USER.equalsIgnoreCase(nciUser.getCurrentUserRole())){
			isSuperUser = true;
		}
		return isSuperUser;
	}
	
	/**
	 * This method sets defaultPageSize
	 * 
	 * @return the default page size.
	 */
	public void setDefaultPageSize(){
		if(changePageSize == 0){
			changePageSize = Integer.parseInt(getPropertyValue(ApplicationConstants.DEFAULT_PAGE_SIZE));
		}
	}


	/**
	 * @return the auditPeriodList
	 */
	public List<DropDownOption> getAuditPeriodList() {
		return auditPeriodList;
	}


	/**
	 * @param auditPeriodList the auditPeriodList to set
	 */
	public void setAuditPeriodList(List<DropDownOption> auditPeriodList) {
		this.auditPeriodList = auditPeriodList;
	}
	
	
	/**
	 * Checks if there is at least one audit present in the system.
	 * 
	 * @return true if an audit is present, false otherwise.
	 */
	public boolean isAuditPresent() {
		return adminService.isAuditPresent();
	}
	
	/**
	 * Checks if there is at least one audit present in the system.
	 * 
	 * @return true if an audit is present, false otherwise.
	 */
	public boolean isI2eAuditPresent() {
		return adminService.isI2eAuditPresent();
	}
	
	/**
	 * Gets the contact us email
	 * 
	 * @return
	 */
	public String getContactEmail() {
		String[] emails = null;
		if(StringUtils.containsAny(entMaintProperties.getProperty("CONTACT_EMAIL"), ",")) {
			emails=StringUtils.split(entMaintProperties.getProperty("CONTACT_EMAIL"), ",");
		} else {
			emails=StringUtils.split(entMaintProperties.getProperty("CONTACT_EMAIL"));
		}
		return StringUtils.join(emails, ";");
	}
	
	/**
	 * This method returns Transfer Org list which doesn't contain user's
	 * ParentNedOrgPath and Non-NCI.
	 */
	public String getAccountTransferOrgList() {			
		String parentNedOrgPath = request.getParameter("transferOrg");
		auditSearchActionHelper.createTransferOrgDropDownList(transferOrgList,lookupService,parentNedOrgPath);
		return SUCCESS;
	}
	
	/**
	 * Gets the contact us text
	 * 
	 * @return
	 */
	public String getContactText() {
		return entMaintProperties.getProperty("CONTACT_TEXT");
	}


	public List<DropDownOption> getTransferOrgList() {
		return transferOrgList;
	}


	public void setTransferOrgList(List<DropDownOption> transferOrgList) {
		this.transferOrgList = transferOrgList;
	}
	
	/**
	 * Get AppLookupT description by list name and code
	 * 
	 * @param listName
	 * @param code
	 * @return
	 */
	public String getDescriptionByCode(String listName, String code) {
		AppLookupT entry = lookupService.getAppLookupByCode(listName, code);
		return (entry == null ? null: entry.getDescription());
	}
	
	public boolean isAdminUser() {
		return EmAppUtil.isAdminUser(nciUser);
	}


	public NciUser getNciUser() {
		return nciUser;
	}

	public String refreshIcDashboardCount() {
		return SUCCESS;
	}
	
	public DashboardData getIcDashboardCountReload() {
		EmAuditsVO emAuditsVO = (EmAuditsVO)session.get(ApplicationConstants.CURRENT_AUDIT);
		icDashboard = adminService.retrieveIcDashboardData(emAuditsVO.getId(), nciUser.getOrgPath());
		return icDashboard;
	}

	public DashboardData getIcDashboard() {
		return icDashboard;
	}


	public String getTooltipText(String key) {
		
		return getText(key);
	}
	
	public void setIcDashboard(DashboardData icDashboard) {
		this.icDashboard = icDashboard;
	}


	public boolean isAnchorDash() {
		return anchorDash;
	}


	public void setAnchorDash(boolean anchorDash) {
		this.anchorDash = anchorDash;
	}
}
