<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>

<%
	String action = "reportSearch";
%>
<s:set var="type" value="searchType"/>
<div class="table-responsive">
<display:table class="table table-bordered" name="transferredAccounts" id="auditAccountsId" export="true" requestURI="<%=action %>">
<display:setProperty name="export.excel.decorator" value="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.AdminReportTransferredAccountsSearchResultExportDecorator"/>
<s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_TRANSFER}">
	<display:setProperty name="export.excel.filename" value="TransferredAccounts.xls"/>
</s:if>
<s:iterator var="t" value="displayColumn">
 <s:if test="#t.export == 'true'">
 <display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
 </s:if></s:iterator>
</display:table>
</div>