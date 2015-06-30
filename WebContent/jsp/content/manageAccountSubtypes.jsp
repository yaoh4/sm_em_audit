<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>


<!--  Display sub-tabs of the selected tab -->

<div class="panel"> 

<s:if
	test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_IMPAC2 eq #request.selectedTab}">
  	<ul class="nav nav-tabs">
  
  		<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_PORTFOLIO eq #request.selectedSubNav}">
			<h4>IMPAC II Accounts Portfolio Analysis <span style="font-weight: normal;"></span></h4>
			<li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
				Search Criteria
			</s:a></li>
  		</s:if>
  
  		<s:else>	
  			<h4>IMPAC II Accounts Audit <span style="font-weight: normal;"></span></h4>
			
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_ACTIVE_ACCOUNTS eq #request.selectedSubTab}">
				<li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
					Active Accounts
				</s:a></li>
			</s:if>
			<s:else>
				<li><s:a href="prepareActiveAuditAccounts.action" cssStyle="text-decoration:none;">
					Active Accounts
				</s:a></li>
			</s:else>
	
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_NEW_ACCOUNTS eq #request.selectedSubTab}">
				<li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
					New Accounts
				</s:a></li>
			</s:if>
			<s:else>
				<li><s:a href="prepareNewAuditAccounts.action" cssStyle="text-decoration:none;">
					New Accounts
				</s:a></li>
			</s:else>
	
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_DELETED_ACCOUNTS eq #request.selectedSubTab}">
				<li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
					Deleted Accounts
				</s:a></li>
			</s:if>
			<s:else>
				<li><s:a href="prepareDeletedAuditAccounts.action" cssStyle="text-decoration:none;">
					Deleted Accounts
				</s:a></li>
			</s:else>
	
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_TAB_INACTIVE_ACCOUNTS eq #request.selectedSubTab}">
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
  
  
 <s:elseif
	test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_I2E eq #request.selectedTab}">
  	<ul class="nav nav-tabs">
  		<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_PORTFOLIO eq #request.selectedSubNav}">
			<h4>I2E Accounts Portfolio Analysis <span style="font-weight: normal;"></span></h4>
			<li class="active">
				<s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
					Search Criteria
				</s:a>
			</li>
 	 	</s:if>
  
  		<s:else>	
  			<h4>I2E Accounts Audit <span style="font-weight: normal;"></span></h4>
			<li class="active">
				<s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
					Active Accounts
				</s:a>
			</li>
  		</s:else>
	</ul>
 </s:elseif>
  	
  	
  
  <!-- We are in admin tab -->
  <s:else>
  
  	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_ADMINISTER eq #request.selectedSubNav}">
  		<h4>Administer Audit <span style="font-weight: normal;"></span></h4>
  	</s:if>
  	<s:elseif
  		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_DASHBOARD eq #request.selectedSubNav}">
  		<h4>Audit Dashboard <span style="font-weight: normal;"></span></h4>
  	</s:elseif>
  	<s:else>
  		<h4>eRA Audit Reports <span style="font-weight: normal;"></span></h4>
  	</s:else>
  
  </s:else>
 