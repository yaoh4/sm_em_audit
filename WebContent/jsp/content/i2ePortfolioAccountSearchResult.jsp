<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery-ui-1.11.3.css"/>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl" %>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO" %>
<%
	String sortAction = null;
	PaginatedListImpl<PortfolioAccountVO> i2ePortfolioAccounts = null;
	if(request.getAttribute("i2ePortfolioAccounts") != null) {
		i2ePortfolioAccounts  =(PaginatedListImpl)request.getAttribute("i2ePortfolioAccounts");
		sortAction = (String)request.getParameter("sortAction") + "?size=" + i2ePortfolioAccounts.getFullListSize();
	} else {
		sortAction = (String)request.getParameter("sortAction");
	}
%>

<s:if test="%{activeAuditAccounts.list.size > 0}"> 
<s:include value="/jsp/helper/changePageSizeHelper.jsp"/>
</s:if>

<body onload="moveToAnchor();"></body>
<div class="table-responsive">
<display:table class="table table-bordered" name="i2ePortfolioAccounts" id="i2ePortfolioAccountsId" pagesize="${pageSize}" export="true" requestURI="<%=sortAction%>" excludedParams="sortAction size" decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.I2ePortfolioSearchResultDecorator">
<s:iterator var="t" value="displayColumn">
 <s:if test="#t.display == 'true'">
	<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
 </s:if>
</s:iterator>
</display:table>
</div>
<div id="help" style="display: none; overflow:auto;" title="I2E Discrepancy Report">
	<br/>
	<div align="left" id="helpDiv"></div>
</div>
