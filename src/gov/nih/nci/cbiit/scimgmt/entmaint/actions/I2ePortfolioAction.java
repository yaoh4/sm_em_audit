package gov.nih.nci.cbiit.scimgmt.entmaint.actions;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DropDownOption;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class I2ePortfolioAction extends BaseAction {
	private static final Logger log = Logger.getLogger(I2ePortfolioAction.class);	
	private List<DropDownOption> categoriesList = new ArrayList<DropDownOption>();	
	
	public String execute() {
		String forward = SUCCESS;
		initialComponent();
	  	if(isSuperUser()){
    		showResult = false;
    		this.setFormAction("preparePortfolioAccounts");     		     		
    		return forward;
    	}
   
        return forward;
	}
	
	
	public String clearSearchPortfolioAccounts() {
		log.info("clearSearchPortfolioAccounts()");
		session.remove(ApplicationConstants.PORTFOLIO_SEARCHVO);
		//Setting default searchVO
		initialComponent();				
		return SUCCESS;
	}
	
	/**
	 * This method sets up default values of searchVO for default search. 
	 * @return String
	 */ 
	private void initialComponent(){
		//Set default values
		searchVO = new AuditSearchVO();
		//searchVO.setCategory(ApplicationConstants.PORTFOLIO_CATEGORY_ACTIVE);
		if(isSuperUser()){  
			searchVO.setOrganization(ApplicationConstants.NCI_DOC_ALL);	    		
    	} 
		else if(nciUser != null && !StringUtils.isBlank(nciUser.getOrgPath())){      				
    		searchVO.setOrganization(nciUser.getOrgPath());				
		}	
		
    	session.put(ApplicationConstants.I2E_PORTFOLIO_SEARCHVO, searchVO);    	
  		auditSearchActionHelper.createI2EPortFolioDropDownLists(organizationList, categoriesList, lookupService, session); 
//  		auditSearchActionHelper.setUpChangePageSizeDropDownList( getPropertyValue(ApplicationConstants.PAGE_SIZE_LIST),session);
	}
}
