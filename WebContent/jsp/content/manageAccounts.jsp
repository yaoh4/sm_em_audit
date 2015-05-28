<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>


<s:if
	test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_IMPAC2 eq #request.selectedTab
	    || @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_I2E eq #request.selectedTab}">
	
<!--  We want to show the breadcrumb with both Portfolio and Audit only if there is at least one Audit in the system 
 Otherwise there is no Audit tab, so no need to have breadcrumb to select between them -->
<s:if test="isAuditPresent()">
  		    
  <ol class="breadcrumb">
  
  <s:if
	test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_PORTFOLIO eq #request.selectedSubNav}">
	<li class="active">Portfolio Analysis</li><li><a href="prepareActiveAuditAccounts.action">Audit</a></li>
  </s:if>
  <s:else>
  	<li><a href="execute.action">Portfolio Analysis</a></li><li  class="active">Audit</li>
  </s:else>
  
  </ol>
</s:if>
</s:if>

<!-- Admin tab selected -->
 <s:else>
 <ol class="breadcrumb">
 
 	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_ADMINISTER eq #request.selectedSubNav}">
		<li class="active">Administer Audit</li>
	</s:if>
	<s:else>
		<li><a href="adminHome.action">Administer Audit</a></li>
	</s:else> 
	
<!--	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_DASHBOARD eq #request.selectedSubNav}">
		<li class="active">Dashboard</li>
	</s:if>
	<s:else>
		<li><a href="adminHome.action">Dashboard</a></li>
	</s:else> -->
	
	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_REPORTS eq #request.selectedSubNav}">
		<li class="active">Reports</li>
	</s:if>
	<s:else>
		<li><a href="adminReports.action">Reports</a></li>
	</s:else> 
	
	 </ol>
 </s:else> 


  <!-- for row class from application menu -->
 </nav>
 
<div class="panel"> 
 
<s:if
	test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_IMPAC2 eq #request.selectedTab
	    || @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_I2E eq #request.selectedTab}">
  	<ul class="nav nav-tabs">
  
  <s:if
	test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_PORTFOLIO eq #request.selectedSubNav}">
	<h4>IMPAC II Accounts Portfolio Analysis <span style="font-weight: normal;"></span></h4>
	<li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
			Search Criteria
		</s:a></li>
  </s:if>
  
  <s:else>	
  <h4>IMPAC II Accounts Audit 
	<span style="font-weight: normal;"></span></h4>
	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_ACTIVE_ACCOUNTS eq #request.selectedSubTab}">
		<li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
			Active Accounts
		</s:a></li>
	</s:if>
	<s:else>
		<li><s:a href="prepareActiveAuditAccounts.action" cssStyle="text-decoration:none;">
			Active Accounts
		</s:a></li>
	</s:else>
	
	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_NEW_ACCOUNTS eq #request.selectedSubTab}">
		<li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
			New Accounts
		</s:a></li>
	</s:if>
	<s:else>
		<li><s:a href="prepareNewAuditAccounts.action" cssStyle="text-decoration:none;">
			New Accounts
		</s:a></li>
	</s:else>
	
	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_DELETED_ACCOUNTS eq #request.selectedSubTab}">
		<li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
			Deleted Accounts
		</s:a></li>
	</s:if>
	<s:else>
		<li><s:a href="prepareDeletedAuditAccounts.action" cssStyle="text-decoration:none;">
			Deleted Accounts
		</s:a></li>
	</s:else>
	
	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_INACTIVE_ACCOUNTS eq #request.selectedSubTab}">
		<li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
			Inactive > 130 Days Accounts
		</s:a></li>
	</s:if>
	<s:else>
		<li><s:a href="prepareInactiveAuditAccounts.action" cssStyle="text-decoration:none;">
			Inactive > 130 Days Accounts
		</s:a></li>
	</s:else>

  </s:else>
  
  
  </ul>
  </s:if>
  
  <!-- We are in admin tab -->
  <s:else>
  
  	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_ADMINISTER eq #request.selectedSubNav}">
  		<h4>Administer Audit <span style="font-weight: normal;"></span></h4>
  	</s:if>
  	<s:else>
  		<h4>ERA Reports <span style="font-weight: normal;"></span></h4>
  	</s:else>
  
  </s:else>
  

 <s:include value="/jsp/error/errorMessages.jsp" />

 