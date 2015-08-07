<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>

<!-- - IMPAC II Tab selected -->
<s:if
	test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_IMPAC2 eq #request.selectedTab}">
	
	<!--  We want to show the breadcrumb with both Portfolio and Audit only if there is at least one Audit in the system 
 		Otherwise there is no Audit tab, so no need to have breadcrumb to select between them -->
	<s:if test="isAuditPresent()">
  		    
  		<ol class="breadcrumb">
  
  			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_PORTFOLIO eq #request.selectedSubNav}">
				<li class="active">Portfolio Analysis</li><li><a href="prepareActiveAuditAccounts.action">Audit</a></li>
  			</s:if>
  			<s:else>
  				<li><a href="execute.action">Portfolio Analysis</a></li><li  class="active">Audit</li>
  			</s:else>
  		</ol>
	</s:if>
</s:if>

<!--  I2E tab selected -->
<s:elseif
	test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_I2E eq #request.selectedTab}">
	
	<!--  We want to show the breadcrumb with both Portfolio and Audit only if there is at least one Audit in the system 
 Otherwise there is no Audit tab, so no need to have breadcrumb to select between them -->
	<s:if test="isAuditPresent()">		    
  		<ol class="breadcrumb">
  
  			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_PORTFOLIO eq #request.selectedSubNav}">
				<li class="active">Portfolio Analysis</li><li><a href="prepareAuditAccounts.action">Audit</a></li>
  			</s:if>
  			<s:else>
  				<li><a href="preparePortfolioAccounts.action">Portfolio Analysis</a></li><li  class="active">Audit</li>
  			</s:else>
  		</ol>
	</s:if>
</s:elseif>


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
	
	<s:if test="isAuditPresent()">
		<s:if
			test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_DASHBOARD eq #request.selectedSubNav}">
			<li class="active">Dashboard</li>
		</s:if>
		<s:else>
			<li><a href="gotoDashboard.action">Dashboard</a></li>
		</s:else> 
	
		<s:if
			test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_REPORTS eq #request.selectedSubNav}">
			<li class="active">Reports</li>
		</s:if>
		<s:else>
			<li><a href="adminReports.action">Reports</a></li>
		</s:else> 
	</s:if>
 </ol>
 </s:else> 


  <!-- for row class from application menu -->
 </nav>
 

  <s:include value="/jsp/content/manageAccountSubtypes.jsp" /> 

 <s:include value="/jsp/error/errorMessages.jsp" />

 