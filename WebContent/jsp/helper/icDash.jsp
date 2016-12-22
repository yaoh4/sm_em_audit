<%@ taglib prefix="s" uri="/struts-tags"%>
<script language="JavaScript" src="../../scripts/entMaint_JQuery.js" type="text/javascript"></script>

<script>
	$(function() {
		$("table.icDash tbody tr td.percent").each(function(e) {
			if ($(this).text().indexOf("(0%)") != -1) {
				if($(this).prev().children("a").text().indexOf("0/0") == -1) {
					$(this).addClass('red');
				} else {
					$(this).addClass('grey');
				}
			}
			else if ($(this).text().indexOf("(100%)") != -1) {
				$(this).addClass('green');
			}
			else {
				$(this).addClass('black');
			}
		});
	});
	
</script>
<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isAuditEnabled()}">
<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_DASHBOARD != #request.selectedSubNav}">
<form id="dashboardFormId" action="" method="post">
</s:if>
<s:hidden id="orgPath" value="%{icDashboardCountReload.orgName}"/>
<table class="table icDash">
	<tbody>
		<tr>
			<th>Category</th>
			<th>Accounts</th>
			<th>% Complete</th>
		</tr>
		<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_ACTIVE)}">
		<tr class="org">
			<td>IMPAC II Active</td>
			<td>
			<s:if test="%{icDashboard.activeAccountCount == 0}">
				<a>0/0</a>
			</s:if>
			<s:else>
				<s:a
					href="javascript:searchAuditByCategory('%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_ACTIVE}','%{icDashboard.orgName}');">
					<s:property value="%{icDashboard.activeCompleteCount}"/>/<s:property value="%{icDashboard.activeAccountCount}"/>
				</s:a>
			</s:else>
			</td>
			<td class="percent">(<s:property value="icDashboard.activePercent"/>%)</td>
		</tr>
		</s:if>
		<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_NEW)}">
		<tr class="org">
			<td>IMPAC II New</td>
			<td>
			<s:if test="%{icDashboard.newAccountCount == 0}">
				<a>0/0</a>
			</s:if>
			<s:else>
				<s:a
					href="javascript:searchAuditByCategory('%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_NEW}','%{icDashboard.orgName}');">
					<s:property value="%{icDashboard.newCompleteCount}"/>/<s:property value="%{icDashboard.newAccountCount}"/>
				</s:a>
			</s:else>
			</td>
			<td class="percent">(<s:property value="icDashboard.newPercent"/>%)</td>
		</tr>
		</s:if>
		<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED)}">
		<tr class="org">
			<td>IMPACI II Deleted</td>
			<td>
			<s:if test="%{icDashboard.deletedAccountCount == 0}">
				<a>0/0</a>
			</s:if>
			<s:else>
				<s:a
					href="javascript:searchAuditByCategory('%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED}','%{icDashboard.orgName}');">
					<s:property value="%{icDashboard.deletedCompleteCount}"/>/<s:property value="%{icDashboard.deletedAccountCount}"/>
				</s:a>
			</s:else>
			</td>
			<td class="percent">(<s:property value="icDashboard.deletedPercent"/>%)</td>
		</tr>
		</s:if>
		<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_INACTIVE)}">
		<tr class="org">
			<td>IMPACI II Inactive</td>
			<td>
			<s:if test="%{icDashboard.inactiveAccountCount == 0}">
				<a>0/0</a>
			</s:if>
			<s:else>
				<s:a
					href="javascript:searchAuditByCategory('%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_INACTIVE}','%{icDashboard.orgName}');">
					<s:property value="%{icDashboard.inactiveCompleteCount}"/>/<s:property value="%{icDashboard.inactiveAccountCount}"/>
				</s:a>
			</s:else>
			</td>
			<td class="percent">(<s:property value="icDashboard.inactivePercent"/>%)</td>
		</tr>
		</s:if>
		<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_I2E)}">
		<tr class="org">
			<td>I2E Active</td>
			<td>
			<s:if test="%{icDashboard.i2eAccountCount == 0}">
				<a>0/0</a>
			</s:if>
			<s:else>
				<s:a
					href="javascript:searchAuditByCategory('%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_I2E}','%{icDashboard.orgName}');">
					<s:property value="%{icDashboard.i2eCompleteCount}"/>/<s:property value="%{icDashboard.i2eAccountCount}"/>
				</s:a>
			</s:else>
			</td>
			<td class="percent">(<s:property value="icDashboard.i2ePercent"/>%)</td>
		</tr>
		</s:if>
	</tbody>
</table>
<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_DASHBOARD != #request.selectedSubNav}">
</form>
</s:if>
</s:if>