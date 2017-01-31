<%@ taglib prefix="s" uri="/struts-tags"%>
<script language="JavaScript" src="../../scripts/entMaint_JQuery.js" type="text/javascript"></script>

<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isAuditEnabled()}">
<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_DASHBOARD != #request.selectedSubNav}">
<form id="dashboardFormId" action="" method="post">
</s:if>
<s:hidden id="orgPath" value="%{icDashboardCountReload.orgName}"/>
<table class="table table-condensed icDash">
	<tbody>
		<tr>
			<th>Category</th>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_ACTIVE)}">
				<th>IMPAC II Active</th>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_NEW)}">
				<th>IMPAC II New</th>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED)}">
				<th>IMPAC II Deleted</th>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_INACTIVE)}">
				<th>IMPAC II Inactive</th>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_I2E)}">
				<th>I2E Active</th>
			</s:if>
		</tr>
		<tr class="org">
			<td><strong>Accounts</strong></td>
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
				</td>
			</s:if>
		</tr>
		<tr>
			<td><strong>% Complete</strong></td>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_ACTIVE)}">
				<s:if test="%{icDashboard.activeAccountCount == 0}">
					<td class="percent grey"><s:property value="icDashboard.activePercent"/>%</td>
				</s:if>
				<s:elseif test="%{icDashboard.activePercent == 0}">
					<td class="percent red"><s:property value="icDashboard.activePercent"/>%</td>
				</s:elseif>
				<s:elseif test="%{icDashboard.activePercent == 100}">
					<td class="percent green"><s:property value="icDashboard.activePercent"/>%</td>
				</s:elseif>
				<s:else>
					<td class="percent black"><s:property value="icDashboard.activePercent"/>%</td>
				</s:else>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_NEW)}">
					<s:if test="%{icDashboard.newAccountCount == 0}">
					<td class="percent grey"><s:property value="icDashboard.newPercent"/>%</td>
				</s:if>
				<s:elseif test="%{icDashboard.newPercent == 0}">
					<td class="percent red"><s:property value="icDashboard.newPercent"/>%</td>
				</s:elseif>
				<s:elseif test="%{icDashboard.newPercent == 100}">
					<td class="percent green"><s:property value="icDashboard.newPercent"/>%</td>
				</s:elseif>
				<s:else>
					<td class="percent black"><s:property value="icDashboard.newPercent"/>%</td>
				</s:else>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED)}">
				<s:if test="%{icDashboard.deletedAccountCount == 0}">
					<td class="percent grey"><s:property value="icDashboard.deletedPercent"/>%</td>
				</s:if>
				<s:elseif test="%{icDashboard.deletedPercent == 0}">
					<td class="percent red"><s:property value="icDashboard.deletedPercent"/>%</td>
				</s:elseif>
				<s:elseif test="%{icDashboard.deletedPercent == 100}">
					<td class="percent green"><s:property value="icDashboard.deletedPercent"/>%</td>
				</s:elseif>
				<s:else>
					<td class="percent black"><s:property value="icDashboard.deletedPercent"/>%</td>
				</s:else>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_INACTIVE)}">
				<s:if test="%{icDashboard.inactiveAccountCount == 0}">
					<td class="percent grey"><s:property value="icDashboard.inactivePercent"/>%</td>
				</s:if>
				<s:elseif test="%{icDashboard.inactivePercent == 0}">
					<td class="percent red"><s:property value="icDashboard.inactivePercent"/>%</td>
				</s:elseif>
				<s:elseif test="%{icDashboard.inactivePercent == 100}">
					<td class="percent green"><s:property value="icDashboard.inactivePercent"/>%</td>
				</s:elseif>
				<s:else>
					<td class="percent black"><s:property value="icDashboard.inactivePercent"/>%</td>
				</s:else>
			</s:if>
			<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil@isCategoryAvailable(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_I2E)}">
				<s:if test="%{icDashboard.i2eAccountCount == 0}">
					<td class="percent grey"><s:property value="icDashboard.i2ePercent"/>%</td>
				</s:if>
				<s:elseif test="%{icDashboard.i2ePercent == 0}">
					<td class="percent red"><s:property value="icDashboard.i2ePercent"/>%</td>
				</s:elseif>
				<s:elseif test="%{icDashboard.i2ePercent == 100}">
					<td class="percent green"><s:property value="icDashboard.i2ePercent"/>%</td>
				</s:elseif>
				<s:else>
					<td class="percent black"><s:property value="icDashboard.i2ePercent"/>%</td>
				</s:else>
			</s:if>
		</tr>
	</tbody>
</table>
<s:if test="%{@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@SUB_NAV_DASHBOARD != #request.selectedSubNav}">
</form>
</s:if>
</s:if>