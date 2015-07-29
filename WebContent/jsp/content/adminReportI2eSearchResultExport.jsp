<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>

<%
	String action = "reportSearch";
%>
<s:set var="type" value="searchType"/>
<display:table name="auditI2eAccounts" id="auditAccountsId" export="true" requestURI="<%=action %>">
<display:setProperty name="export.excel.decorator" value="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.AdminReportI2eSearchResultExportDecorator"/>
<s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_I2E}">
	<display:setProperty name="export.excel.filename" value="I2E.xls"/>
</s:if>
<s:iterator var="t" value="displayColumn">
	<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
</s:iterator>
</display:table>
