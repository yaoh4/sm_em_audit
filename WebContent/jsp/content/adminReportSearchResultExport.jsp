<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>

<%
	String action = "reportSearch";
%>
<s:set var="type" value="searchType"/>
<display:table name="auditAccounts" id="auditAccountsId" export="true" requestURI="<%=action %>">
<display:setProperty name="export.excel.decorator" value="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.AdminReportSearchResultExportDecorator"/>
<s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_ACTIVE}">
	<display:setProperty name="export.excel.filename" value="Active.xls"/>
</s:if>
<s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_NEW}">
	<display:setProperty name="export.excel.filename" value="New.xls"/>
</s:if>
<s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED}">
	<display:setProperty name="export.excel.filename" value="Deleted.xls"/>
</s:if>
<s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_INACTIVE}">
	<display:setProperty name="export.excel.filename" value="Inactive.xls"/>
</s:if>
<s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_EXCLUDED}">
	<display:setProperty name="export.excel.filename" value="NotNCIPurvue.xls"/>
</s:if>
<s:iterator var="t" value="displayColumn">
<s:if test="#t.display == 'true'">
	<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
</s:if>
</s:iterator>
</display:table>
