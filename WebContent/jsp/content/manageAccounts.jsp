<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>


<s:if
	test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_IMPAC2 eq #request.selectedTab
	    || @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_I2E eq #request.selectedTab}">
  <ol class="breadcrumb">
  <s:if
	test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_PORTFOLIO eq #request.selectedSubNav}">
	<li class="active">Portfolio Analysis</li><li><a href="prepareActiveAuditAccounts.action">Audit</a></li>
  </s:if>
  <s:else>
  	<li><a href="defaultPortfolioSearch.action">Portfolio Analysis</a></li><li  class="active">Audit</li>
  </s:else>
  </ol>
</s:if>

<!-- Disabled - Not in scope for EM Audit Module Version 1 --!>
<!-- <s:else>
 
 	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_ADMINISTER eq #request.selectedSubNav}">
		<li class="active">Administer Audit</li>
	</s:if>
	<s:else>
		<li><a href="adminHome.action">Administer Audit</a></li>
	</s:else> 
	
	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_DASHBOARD eq #request.selectedSubNav}">
		<li class="active">Dashboard</li>
	</s:if>
	<s:else>
		<li><a href="adminHome.action">Dashboard</a></li>
	</s:else>
	
	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_REPORTS eq #request.selectedSubNav}">
		<li class="active">Reports</li>
	</s:if>
	<s:else>
		<li><a href="adminHome.action">Reports</a></li>
	</s:else> 
	
 </s:else> -->


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
  <h4>IMPAC II Accounts Audit <span style="font-weight: normal;"> (<s:property value="%{session.currentAudit.impaciiFromDate}"/> to <s:property value="%{session.currentAudit.impaciiToDate}"/>)</span></h4>
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

 <s:include value="/jsp/error/errorMessages.jsp" />

<!-- Do not delete for now -->
<!--  
<div class="tab-content">
	
 	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_ACTIVE_ACCOUNTS eq #request.selectedSubTab}">
		<div class="tab-pane fade in active" id="activeAccounts">...</div>
	</s:if>
	<s:else>
		<div class="tab-pane fade" id="activeAccounts">...</div>
	</s:else>
	
	
	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_NEW_ACCOUNTS eq #request.selectedSubTab}">
		<div class="tab-pane fade in active" id="newAccounts">...</div>
	</s:if>
	<s:else>
		<div class="tab-pane fade" id="newAccounts">...</div>
	</s:else>
	
	
	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_DELETED_ACCOUNTS eq #request.selectedSubTab}">
		<div class="tab-pane fade in active" id="deletedAccounts">...</div>
	</s:if>
	<s:else>
		<div class="tab-pane fade" id="deletedAccounts">...</div>
	</s:else>

</div>
-->
 