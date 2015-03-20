<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<ul class="nav nav-tabs">
	<!-- Landing Tab: begin -->
	<s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_IMPAC2 eq #request.selectedTab}">
		<li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
			IMPAC II
		</s:a></li>
	</s:if>
	<s:else>
		<li><s:a href="/entmaint/impac2/impac2Home.action" cssStyle="text-decoration:none;">
			IMPAC II
		</s:a></li>
	</s:else>
	<!-- My Task: End -->
	
	<!-- Initiated Task Tab: Begin -->
	<!--  Temporarily disabled - Not in scope for Audit Module Version 1 -->
  <!--   <s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_I2E eq #request.selectedTab}">
		<li><li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
		   I2E
		</s:a></li>
	</s:if>
	<s:else>
		<li><s:a href="/entmaint/i2e/i2eHome.action" cssStyle="text-decoration:none;">
			I2E
		</s:a></li>
	</s:else> -->
	<!-- Initiated Task Tab: End -->

	<!-- Initiated Task Tab: Begin -->
	<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isAdminUser(#session)}">

    	<s:if
			test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_ADMIN eq #request.selectedTab}">
			<li><li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
		    	Admin
			</s:a></li>
		</s:if>
		<s:else>
			<li><s:a href="/entmaint/admin/adminHome.action" cssStyle="text-decoration:none;">
				Admin
			</s:a></li>
		</s:else>
	
	</s:if>
	<!-- Initiated Task Tab: End -->

</ul>


