<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%
	String sortAction = (String)request.getParameter("sortAction");
%>
<display:table name="portfolioAccounts" id="portfolioAccountsId" export="true" requestURI="<%=sortAction%>" decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.PortfolioSearchResultDecorator">
<s:iterator var="t" value="displayColumn">
<s:if test="#t.display == 'true'">
	<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
</s:if>
</s:iterator>
</display:table>