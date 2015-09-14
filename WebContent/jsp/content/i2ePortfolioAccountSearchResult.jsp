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

<s:if test="%{i2ePortfolioAccounts.list.size > 0}"> 
<s:include value="/jsp/helper/changePageSizeHelper.jsp"/>
</s:if>

<body onload="moveToAnchor();"></body>
<div class="table-responsive">
<display:table class="table table-bordered" name="i2ePortfolioAccounts" id="i2ePortfolioAccountsId" pagesize="${pageSize}" export="true" requestURI="<%=sortAction%>" excludedParams="sortAction size" decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.I2ePortfolioSearchResultDecorator">

<s:set var="i2ePortfolioAccountsRoles" value="%{#attr['i2ePortfolioAccountsId'].accountRoles}" />
<s:set var="i2ePortfolioAccountsRolesId" value="%{#attr['i2ePortfolioAccountsId'].npnId}" />
<s:set var="i2ePortfolioAccountsRolesColumns" value="%{getI2ePortfolioAccountsRolesColumns()}" />
<s:set var="i2ePortfolioAccountsRolesColumnsNames" value="%{getI2ePortfolioAccountsRolesColumnsNames()}" />
		
<s:iterator var="t" value="displayColumn">
 <s:if test="#t.display == 'true'">
 	<s:if test="#t.isNestedColumn == 'false'">
		<display:column property="${t.property}" title="${t.columnName}" sortable="${t.sort}"/>
	</s:if>
 	<s:if test="#t.columnName == 'Created/Last Updated By'">
 		<display:column title="${i2ePortfolioAccountsRolesColumnsNames}" style="width:30%;">
 		
			<display:table class="table table-bordered nestedTableStyle" name="${i2ePortfolioAccountsRoles}" id="i2ePortfolioAccountsRoles_${i2ePortfolioAccountsRolesId}" 
						   decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.I2ePortfolioSearchResultDecorator">
						   
			<display:setProperty name="basic.show.header" value="false" />
			
				<s:iterator var="t" value="i2ePortfolioAccountsRolesColumns">
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
