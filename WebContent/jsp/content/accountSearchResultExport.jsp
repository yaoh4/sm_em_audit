<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%
	String action = (String)request.getParameter("act");
%>
<display:table name="activeAuditAccounts" id="auditAccountsId" export="true" requestURI="<%=action%>">
<display:setProperty name="export.excel.decorator" value="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.AuditSearchResultExportDecorator"/>
<s:iterator var="t" value="displayColumn">
<s:if test="#t.export == 'true'">
	<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
</s:if>
</s:iterator>
</display:table>