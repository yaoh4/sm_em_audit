<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery-ui-1.11.3.css"/>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl" %>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO" %>

<s:include value="/jsp/content/manageAccounts.jsp" />
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<s:set name="eraualink" value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@ERA_US_LINK)}"/>

<script>
	$(function() {
		$("#portfolioAccountsId").children().children("tr").each(
				function(e) {
					if($(this).next().children("td:first").text() != "")
						$(this).children("td").css("border-bottom", "1px solid black");
				});
	});
</script>

<s:if test="%{portfolioAccounts.list.size > 0}">
	<div class="table-responsive">
	<display:table class="table table-bordered" name="portfolioAccounts" id="portfolioAccountsId" export="false" decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.DiscrepanciesTableDecorator">
	<display:setProperty name="paging.banner.one_item_found" value="" />
	<display:setProperty name="paging.banner.all_items_found" value="" />
	<s:set var="portfolioAccountsRoles" value="%{#attr['portfolioAccountsId'].accountRoles}" />
	<s:set var="portfolioAccountsRolesId" value="%{#attr['portfolioAccountsId'].nihNetworkId}" />
	<s:set var="portfolioAccountsRolesColumns" value="%{getPortfolioAccountsRolesColumns()}" />
	<s:set var="portfolioAccountsRolesColumnsNames" value="%{getPortfolioAccountsRolesColumnsNames()}" />
		
	<s:iterator var="t" value="displayColumn">
 		<s:if test="#t.display == 'true'">
 			<s:if test="#t.isNestedColumn == 'false'">
				<display:column property="${t.property}" title="${t.columnName}" sortable="false"/>
			</s:if>
 			<s:if test="#t.columnName == 'Created/Last Updated By'">
 				<display:column title="${portfolioAccountsRolesColumnsNames}" style="width:30%;">
 		
				<display:table class="table table-bordered nestedTableStyle" name="${portfolioAccountsRoles}" id="portfolioAccountsRoles_${portfolioAccountsRolesId}" 
						   decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.DiscrepanciesTableDecorator">
						   
					<display:setProperty name="basic.show.header" value="false" />
			
					<s:iterator var="t" value="portfolioAccountsRolesColumns">
						<display:column property="${t.property}" title="${t.columnName}" style="width:10%;word-break: break-all;"/>
					</s:iterator>
				
				</display:table>
				</display:column>
 			</s:if> 	
 		</s:if>
	</s:iterator>
	</display:table>
	</div>
</s:if>
<s:else>
	<span class="bannerAlign"><s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@NOTHING_DISPLAY)}"/></span>
</s:else>
<br/><br/>
<div id="help" style="display: none; overflow:auto;" title="Discrepancy Report">
	<br/>
	<div align="left" id="helpDiv"></div>
</div>
<div id="role" style="display: none; overflow:auto;" title="Role Description">
	<br>
	<div align="left" id="roleHelpId"></div>
</div>
<div id="eraua_na" align="center" style="display:none;" title="Information">
<br/>
	<div align="center">
	<s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@ERAUA_INFO)}"/>
	</div>
</div>