<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="icDashDiv">
	<s:include value="/jsp/helper/icDash.jsp"/>
</div>

<ul class="nav nav-tabs">
	<!-- Landing Tab: -->
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
	
  <s:if
		test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_I2E eq #request.selectedTab}">
		<li><li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
		   I2E
		</s:a></li>
	</s:if>
	<s:else>
		<li><s:a href="/entmaint/i2e/i2eHome.action" cssStyle="text-decoration:none;">
			I2E
		</s:a></li>
	</s:else> 
	<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TAB_DISCREPANCY eq #request.selectedTab}">
		<li><li class="active"><s:a href="javascript: void(0)" cssStyle="text-decoration:none;">
		   	My DOC Discrepancies
		</s:a></li>
	</s:if>
	<s:else>
		<li><s:a href="/entmaint/discrepancy/Discrepancies.action" cssStyle="text-decoration:none;">
			My DOC Discrepancies
		</s:a></li>
	</s:else>
	
	<s:if test="isAdminUser()">

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

</ul>


