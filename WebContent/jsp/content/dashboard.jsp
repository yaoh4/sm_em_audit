<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.utils.DashboardData" %>

<s:include value="/jsp/content/manageAccounts.jsp" />
<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>

<script>
	$(function() {
		$("table td.colorPercent").each(function(e) {
			if ($(this).text().indexOf("(0%)") != -1) {
				$(this).addClass('red');
			}
			if ($(this).text().indexOf("(100%)") != -1) {
				$(this).addClass('green');
			}
		});
	});
	
</script>

<div class="table-responsive">
  <form id="dashboardFormId" action="gotoDashboard" method="post">
  <table class="table table-condensed table-striped">
      <caption>IMPAC II Account Audit Status</caption><span style="float:right;"><a href="javascript:refresh();">Refresh Page</a></span>
    <tr>
       <s:if test="emAuditsVO.i2eFromDate != null">
      <th width="20%">NCI Organizations</th>
      <th width="16%">Active Accounts</th>
      <th width="16%">New Accounts</th>
      <th width="16%">Deleted Accounts</th>
      <th width="16%">Inactive &gt;130 Days Accounts</th>
      <th width="16%">I2E Accounts</th>
      </s:if>
      <s:else>
       <th width="40%">NCI Organizations</th>
      <th width="15%">Active Accounts</th>
      <th width="15%">New Accounts</th>
      <th width="15%">Deleted Accounts</th>
      <th width="15%">Inactive &gt;130 Days Accounts</th>
      </s:else>
    </tr>
    <s:set var="orgData" value="orgsData"/>
    <s:set var="keys" value="orgKeys"/>
    <s:set var="otherTotal" value="others"/>
    <s:set var="otherKeys" value="otherOrgKeys"/>
    <s:set var="otherOrgData" value="otherOrgsData"/>
	<jsp:useBean id="orgData" type="java.util.HashMap"/>
	<jsp:useBean id="keys" type="java.util.List"/>
	<jsp:useBean id="otherTotal" type="gov.nih.nci.cbiit.scimgmt.entmaint.utils.DashboardData"/>
	<jsp:useBean id="otherKeys" type="java.util.List"/>
	<jsp:useBean id="otherOrgData" type="java.util.HashMap"/>
	<% 
		for (int i = 0; i< keys.size(); i++) {
			String key = (String)keys.get(i);
	%>
    <tr class="org">
    	<td><strong><%= key %></strong></td>
    	<% 
    		HashMap<String, DashboardData> dData = (HashMap<String, DashboardData>)orgData.get(key); 
    	%>
    	<td class="colorPercent"><%= dData.get("active").getActiveAccountDataStr(key) %></td>
    	<td class="colorPercent"><%= dData.get("new").getNewAccountDataStr(key) %></td>
    	<td class="colorPercent"><%= dData.get("deleted").getDeletedAccountDataStr(key) %></td>
    	<td class="colorPercent"><%= dData.get("inactive").getInactiveAccountDataStr(key) %></td>
    	<s:if test="emAuditsVO.i2eFromDate != null">
      		<td class="colorPercent"><%= dData.get("i2e").getI2eAccountDataStr(key) %></td>
      	</s:if>
    </tr>
    <% } %>
    <tr class="org">
      <td id="otherAnchor"><strong><a href="javascript:toggleOther('nameit');"><img src="../images/CriteriaClosed.gif" alt="Plus"></a>OTHER</strong></td>
      <td class="colorPercent"><b><%= otherTotal.getActiveAccountDataStr() %></b></td>
      <td class="colorPercent"><b><%= otherTotal.getNewAccountDataStr() %></b></td>
      <td class="colorPercent"><b><%= otherTotal.getDeletedAccountDataStr() %></b></td>
      <td class="colorPercent"><b><%= otherTotal.getInactiveAccountDataStr() %></b></td>
      <s:if test="emAuditsVO.i2eFromDate != null">
      	<td class="colorPercent"><b><%= otherTotal.getI2eAccountDataStr() %></b></td>
      </s:if>
    </tr>
   
    <% 
		for (int i = 0; i< otherKeys.size(); i++) {
			String key = (String)otherKeys.get(i);
	%>
    <tr class="org" nameit=other style="display:none;">
      <td class="other"><%= key %></td>
    	<% 
    		HashMap<String, DashboardData> oData = (HashMap<String, DashboardData>)otherOrgData.get(key); 
    	%>    
     	<td class="colorPercent"><%= oData.get("active").getActiveAccountDataStr(key) %></td>
    	<td class="colorPercent"><%= oData.get("new").getNewAccountDataStr(key) %></td>
    	<td class="colorPercent"><%= oData.get("deleted").getDeletedAccountDataStr(key) %></td>
    	<td class="colorPercent"><%= oData.get("inactive").getInactiveAccountDataStr(key) %></td>
    	<s:if test="emAuditsVO.i2eFromDate != null">
    		<td class="colorPercent"><%= oData.get("i2e").getI2eAccountDataStr(key) %></td>
    	</s:if>
    </tr>
    <% } %>

  </table>
  <s:include value="dashboardUnknown.jsp"/>
  </form>
  </div>
