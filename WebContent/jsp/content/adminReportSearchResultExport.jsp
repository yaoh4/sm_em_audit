<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>

<%
	String action = "reportSearch";
%>
<display:table name="auditAccounts" id="auditAccountsId" export="true" requestURI="<%=action %>">
<display:setProperty name="export.excel.decorator" value="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.AdminReportSearchResultExportDecorator"/>
<s:iterator var="t" value="displayColumn">
<s:if test="#t.display == 'true'">
	<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
</s:if>
</s:iterator>
</display:table>
