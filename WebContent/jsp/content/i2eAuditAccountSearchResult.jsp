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

<s:if test="%{activeAuditAccounts.list.size > 0}"> 
<s:include value="/jsp/helper/changePageSizeHelper.jsp"/>
</s:if>

<body onload="moveToAnchor();"></body>
<div class="table-responsive">
<display:table class="table table-bordered" name="activeAuditAccounts" id="i2eAuditAccountsId" pagesize="${pageSize}" export="true" requestURI="<%=sortAction%>" excludedParams="sortAction size" decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.I2eAuditSearchResultDecorator">

<s:set var="i2eAuditAccountsRoles" value="%{#attr['i2eAuditAccountsId'].accountRoles}" />
<s:set var="i2eAuditAccountsRolesId" value="%{#attr['i2eAuditAccountsId'].id}" />
<s:set var="i2eAuditAccountsRolesColumns" value="%{getI2eAuditAccountsRolesColumns()}" />
<s:set var="i2eAuditAccountsRolesColumnsNames" value="%{getI2eAuditAccountsRolesColumnsNames()}" />

<s:iterator var="t" value="displayColumn">
<s:if test="#t.display == 'true'">
	<s:if test="#t.isNestedColumn == 'false'">
		<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
	</s:if>
 	<s:if test="#t.columnName == 'Created/Last Updated By'">
 		<display:column title="${i2eAuditAccountsRolesColumnsNames}" style="width:30%;">
 		
			<display:table class="table table-bordered nestedTableStyle" name="${i2eAuditAccountsRoles}" id="i2eAuditAccountsRoles_${i2eAuditAccountsRolesId}" 
						   decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.I2eAuditSearchResultDecorator">
						   
			<display:setProperty name="basic.show.header" value="false" />
			
				<s:iterator var="t" value="i2eAuditAccountsRolesColumns">
					<display:column property="${t.property}" title="${t.columnName}" style="width:10%;"/>
				</s:iterator>
				
			</display:table>
		</display:column>
 	</s:if> 	
</s:if>
</s:iterator>
</display:table>
</div>
<div id="help" style="display: none; overflow:auto;" title="I2E Discrepancy Report">
	<br/>
	<div align="left" id="helpDiv"></div>
</div>
<div id="note" style="display: none; overflow:auto;" title="Note">
	<br>
	<div align="left" id="noteId"></div>
</div>	