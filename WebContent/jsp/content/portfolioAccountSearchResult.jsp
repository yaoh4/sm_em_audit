<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery-ui-1.11.3.css"/>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>

<%
	String sortAction = (String)request.getParameter("sortAction");
%>
<display:table name="portfolioAccounts" id="portfolioAccountsId" pagesize="${pageSize}" export="true" requestURI="<%=sortAction%>" decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.PortfolioSearchResultDecorator">
<s:iterator var="t" value="displayColumn">
<s:if test="#t.display == 'true'">
	<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
</s:if>
</s:iterator>
</display:table>
<div id="help" style="display: none; overflow:auto;" title="IMPAC Discrepancy Report">
	<br/>
	<div align="left" id="helpDiv"></div>
</div>
<div id="role" style="display: none; overflow:auto;" title="Role Description">
	<br>
	<div align="center" id="roleHelpId"></div>
</div>	