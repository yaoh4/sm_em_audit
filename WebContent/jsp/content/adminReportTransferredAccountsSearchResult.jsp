<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl" %>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.TransferredAuditAccountsVO" %>

<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery-ui-1.11.3.css"/>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
<%
	String action = null;
	PaginatedListImpl<TransferredAuditAccountsVO> transferredAccounts = null;
	if(request.getAttribute("transferredAccounts") != null) {
		transferredAccounts  =(PaginatedListImpl)request.getAttribute("transferredAccounts");
		action = "reportSearch?size=" + transferredAccounts.getFullListSize();
	} else {
		action = "reportSearch";
	}
%>

<s:if test="%{transferredAccounts.list.size > 0}"> 
<s:include value="/jsp/helper/changePageSizeHelper.jsp"/>
</s:if>

<body onload="moveToAnchor();"></body>
<div class="table-responsive">
	<display:table class="table table-bordered" style="width: 100%;" name="transferredAccounts" id="auditAccountsId" pagesize="${pageSize}" export="true" requestURI="<%=action %>" excludedParams="size"
		decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.AdminReportTransferredAccountsSearchResultExportDecorator">

		<s:iterator var="t" value="displayColumn">
			<s:if test="#t.display == 'true'">
				<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}" />
			</s:if>
		</s:iterator>
	</display:table>
</div>
