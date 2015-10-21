<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery-ui-1.11.3.css"/>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl" %>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditI2eAccountVO" %>
<%
	String sortAction = null;
	PaginatedListImpl<AuditI2eAccountVO> i2eAuditAccounts = null;
	if(request.getAttribute("activeAuditAccounts") != null) {
		i2eAuditAccounts  =(PaginatedListImpl)request.getAttribute("activeAuditAccounts");
		sortAction = (String)request.getParameter("sortAction") + "?size=" + i2eAuditAccounts.getFullListSize();
	} else {
		sortAction = (String)request.getParameter("sortAction");
	}
%>
<body onload="moveToAnchor();"></body>
<div class="table-responsive">
<display:table class="table table-bordered" name="activeAuditAccounts" id="i2eAuditAccountsId" pagesize="${pageSize}" export="true" requestURI="<%=sortAction%>" excludedParams="sortAction size">
<display:setProperty name="export.excel.decorator" value="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.I2eAuditSearchResultExportDecorator"/>
<display:setProperty name="export.excel.filename" value="i2eAudit.xls"/>
<s:iterator var="t" value="displayColumn">
<s:if test="#t.export == 'true'">
 <display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
 </s:if>
</s:iterator>
</display:table>
</div>
