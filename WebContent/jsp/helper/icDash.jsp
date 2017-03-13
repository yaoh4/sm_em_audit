<%@ taglib prefix="s" uri="/struts-tags"%>
<script language="JavaScript" src="../../scripts/entMaint_JQuery.js" type="text/javascript"></script>

<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isAuditEnabled()}">
<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_DASHBOARD != #request.selectedSubNav}">
<form id="dashboardFormId" action="" method="post">
</s:if>
<div id="dash"></div>
<s:hidden id="orgPath" value="%{icDashboardCountReload.orgName}"/>
	<h4>
		<s:if test="%{!icDashboard.orgName.equals('all')}">
			<s:property value="%{icDashboard.orgName}" />
		</s:if>
		Audit Dashboard
			<s:a class="hoverOver" data-toggle="tooltip" data-placement="top" data-html="true" title="%{getTooltipText('ic.dash.tooltip')}"
			style="font-size: 12px;"><img style="vertical-align: initial;"
			src="/entmaint/images/info.png"/></s:a>
	</h4>
	<table class="table table-condensed icDash">
	<tbody>
		<tr>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_ACTIVE)}">
				<th>IMPAC II Active Accounts</th>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_NEW)}">
				<th>IMPAC II New Accounts</th>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED)}">
				<th>IMPAC II Deleted Accounts</th>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_INACTIVE)}">
				<th>IMPAC II Inactive Accounts</th>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_I2E)}">
				<th>I2E Active Accounts</th>
			</s:if>
		</tr>
		<tr class="org">
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_ACTIVE)}">
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
				<s:if test="%{icDashboard.activeAccountCount == 0}">
					<span class="percent grey">(<s:property value="icDashboard.activePercent"/>%)</span>
				</s:if>
				<s:elseif test="%{icDashboard.activePercent == 0}">
					<span class="percent red">(<s:property value="icDashboard.activePercent"/>%)</span>
				</s:elseif>
				<s:elseif test="%{icDashboard.activePercent == 100}">
					<span class="percent green">(<s:property value="icDashboard.activePercent"/>%)</span>
				</s:elseif>
				<s:else>
					<span class="percent black">(<s:property value="icDashboard.activePercent"/>%)</span>
				</s:else>
				</td>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_NEW)}">
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
				<s:if test="%{icDashboard.newAccountCount == 0}">
					<span class="percent grey">(<s:property value="icDashboard.newPercent"/>%)</span>
				</s:if>
				<s:elseif test="%{icDashboard.newPercent == 0}">
					<span class="percent red">(<s:property value="icDashboard.newPercent"/>%)</span>
				</s:elseif>
				<s:elseif test="%{icDashboard.newPercent == 100}">
					<span class="percent green">(<s:property value="icDashboard.newPercent"/>%)</span>
				</s:elseif>
				<s:else>
					<span class="percent black">(<s:property value="icDashboard.newPercent"/>%)</span>
				</s:else>
				</td>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED)}">
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
				<s:if test="%{icDashboard.deletedAccountCount == 0}">
					<span class="percent grey">(<s:property value="icDashboard.deletedPercent"/>%)</span>
				</s:if>
				<s:elseif test="%{icDashboard.deletedPercent == 0}">
					<span class="percent red">(<s:property value="icDashboard.deletedPercent"/>%)</span>
				</s:elseif>
				<s:elseif test="%{icDashboard.deletedPercent == 100}">
					<span class="percent green">(<s:property value="icDashboard.deletedPercent"/>%)</span>
				</s:elseif>
				<s:else>
					<span class="percent black">(<s:property value="icDashboard.deletedPercent"/>%)</span>
				</s:else>
				</td>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_INACTIVE)}">
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
				<s:if test="%{icDashboard.inactiveAccountCount == 0}">
					<span class="percent grey">(<s:property value="icDashboard.inactivePercent"/>%)</span>
				</s:if>
				<s:elseif test="%{icDashboard.inactivePercent == 0}">
					<span class="percent red">(<s:property value="icDashboard.inactivePercent"/>%)</span>
				</s:elseif>
				<s:elseif test="%{icDashboard.inactivePercent == 100}">
					<span class="percent green">(<s:property value="icDashboard.inactivePercent"/>%)</span>
				</s:elseif>
				<s:else>
					<span class="percent black">(<s:property value="icDashboard.inactivePercent"/>%)</span>
				</s:else>
				</td>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_I2E)}">
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
				<s:if test="%{icDashboard.i2eAccountCount == 0}">
					<span class="percent grey">(<s:property value="icDashboard.i2ePercent"/>%)</span>
				</s:if>
				<s:elseif test="%{icDashboard.i2ePercent == 0}">
					<span class="percent red">(<s:property value="icDashboard.i2ePercent"/>%)</span>
				</s:elseif>
				<s:elseif test="%{icDashboard.i2ePercent == 100}">
					<span class="percent green">(<s:property value="icDashboard.i2ePercent"/>%)</span>
				</s:elseif>
				<s:else>
					<span class="percent black">(<s:property value="icDashboard.i2ePercent"/>%)</span>
				</s:else>
				</td>
			</s:if>
		</tr>
	</tbody>
</table>
<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_DASHBOARD != #request.selectedSubNav}">
</form>
</s:if>
</s:if>