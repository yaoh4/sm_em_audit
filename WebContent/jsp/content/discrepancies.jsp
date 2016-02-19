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

<s:if test="%{portfolioAccounts.list.size > 0}">
	<div class="table-responsive">
	<display:table class="table table-bordered" name="portfolioAccounts" id="portfolioAccountsId" export="false" decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.PortfolioSearchResultDecorator">
	<display:setProperty name="paging.banner.one_item_found" value="" />
	<display:setProperty name="paging.banner.all_items_found" value="" />
	<s:iterator var="t" value="displayColumn">
	<s:if test="#t.display == 'true'">
		<s:if test="#t.columnName == 'IMPAC II Application Role'">
			<display:column property="${t.property}" title="${t.columnName}" sortable="false" style="white-space:nowrap;"/>
		</s:if>
		<s:else>
			<display:column property="${t.property}" title="${t.columnName}" sortable="false"/>
		</s:else>
	</s:if>
	</s:iterator>
	</display:table>
	</div>
</s:if>
<s:else>
	<span class="bannerAlign"><s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@NOTHING_DISPLAY)}"/></span>
</s:else>
<br/><br/>
<h4>I2E Discrepancies <span style="font-weight: normal;"></span></h4>
<s:if test="%{i2ePortfolioAccounts.list.size > 0}">
	<div class="table-responsive">
	<display:table class="table table-bordered" name="i2ePortfolioAccounts" id="i2ePortfolioAccountsId" export="false" decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.I2ePortfolioSearchResultDecorator">
	<display:setProperty name="paging.banner.one_item_found" value="" />
	<display:setProperty name="paging.banner.all_items_found" value="" />

	<s:set var="i2ePortfolioAccountsRoles" value="%{#attr['i2ePortfolioAccountsId'].accountRoles}" />
	<s:set var="i2ePortfolioAccountsRolesId" value="%{#attr['i2ePortfolioAccountsId'].npnId}" />
	<s:set var="i2ePortfolioAccountsRolesColumns" value="%{getI2ePortfolioAccountsRolesColumns()}" />
	<s:set var="i2ePortfolioAccountsRolesColumnsNames" value="%{getI2ePortfolioAccountsRolesColumnsNames()}" />
		
	<s:iterator var="t" value="displayColumnI2e">
 		<s:if test="#t.display == 'true'">
 			<s:if test="#t.isNestedColumn == 'false'">
				<display:column property="${t.property}" title="${t.columnName}" sortable="false"/>
			</s:if>
 			<s:if test="#t.columnName == 'Created/Last Updated By'">
 				<display:column title="${i2ePortfolioAccountsRolesColumnsNames}" style="width:30%;">
 		
				<display:table class="table table-bordered nestedTableStyle" name="${i2ePortfolioAccountsRoles}" id="i2ePortfolioAccountsRoles_${i2ePortfolioAccountsRolesId}" 
						   decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.I2ePortfolioSearchResultDecorator">
						   
					<display:setProperty name="basic.show.header" value="false" />
			
					<s:iterator var="t" value="i2ePortfolioAccountsRolesColumns">
						<display:column property="${t.property}" title="${t.columnName}" style="width:10%;"/>
					</s:iterator>
				
				</display:table>
				</display:column>
 			</s:if> 	
 		</s:if>
	</s:iterator>
	</display:table>
	</div>
	<br/>
</s:if>
<s:else>
	<span class="bannerAlign"><s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@NOTHING_DISPLAY)}"/></span>
</s:else>
<div id="help" style="display: none; overflow:auto;" title="Discrepancy Report">
	<br/>
	<div align="left" id="helpDiv"></div>
</div>
<div id="role" style="display: none; overflow:auto;" title="Role Description">
	<br>
	<div align="left" id="roleHelpId"></div>
</div>