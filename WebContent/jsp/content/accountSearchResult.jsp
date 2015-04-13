<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl" %>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditAccountVO" %>
<%
	String action = null;
	PaginatedListImpl<AuditAccountVO> activeAuditAccounts = null;
	if(request.getAttribute("activeAuditAccounts") != null) {
		activeAuditAccounts  =(PaginatedListImpl)request.getAttribute("activeAuditAccounts");
		action = (String)request.getParameter("act") + "?size=" + activeAuditAccounts.getFullListSize();
	} else {
		action = (String)request.getParameter("act");
	}
%>
<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery-ui-1.11.3.css"/>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
<input type="hidden" id="i2eemlinkId" value="<s:text name='i2e_em_link' />"/>
<input type="hidden" id="eraualinkId" value="<s:text name='era_ua_link' />"/>

<s:if test="%{activeAuditAccounts.list.size > 0}"> 
<s:include value="/jsp/helper/changePageSizeHelper.jsp"/>
</s:if>

<display:table name="activeAuditAccounts" id="auditAccountsId" pagesize="${pageSize}" export="true" requestURI="<%=action%>" excludedParams="act size" decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.AuditSearchResultDecorator">
<s:iterator var="t" value="displayColumn">
<s:if test="#t.display == 'true'">
	<s:if test="#t.columnName == 'IMPAC II Application Role(s)'">
		<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}" style="white-space:nowrap;"/>
	</s:if>
	<s:else>
		<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
	</s:else>
</s:if>
</s:iterator>
</display:table>
<div id="help" style="display: none; overflow:auto;" title="IMPAC Discrepancy Report">
	<br/>
	<div align="left" id="helpDiv"></div>
</div>
<div id="role" style="display: none; overflow:auto;" title="Role Description">
	<br>
	<div align="left" id="roleHelpId"></div>
</div>	