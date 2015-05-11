<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.utils.DashboardData" %>

<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
  
  <div class="table-responsive">
  <table class="table table-condensed table-striped">
      <caption>2015 IMPAC II and I2E Account Audit Status</caption>
    <tr>
      <th>NCI Organizations</th>
      <th>Active Accounts</th>
      <th>New Accounts</th>
      <th>Deleted Accounts</th>
      <th>Inactive &gt;130 Days Accounts</th>
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
    	<td style="white-space: nowrap;"><%= dData.get("active").getActiveAccountDataStr() %></td>
    	<td style="white-space: nowrap;"><%= dData.get("new").getNewAccountDataStr() %></td>
    	<td style="white-space: nowrap;"><%= dData.get("deleted").getDeletedAccountDataStr() %></td>
    	<td style="white-space: nowrap;"><%= dData.get("inactive").getInactiveAccountDataStr() %></td>
    </tr>
    <% } %>
    <tr class="org">
      <td id="otherAnchor"><strong><a href="javascript:toggleOther('nameit');"><img src="../images/CriteriaClosed.gif" alt="Plus"></a>OTHER</strong></td>
      <td style="white-space: nowrap;"><%= otherTotal.getActiveAccountDataStr() %></td>
      <td style="white-space: nowrap;"><%= otherTotal.getNewAccountDataStr() %></td>
      <td style="white-space: nowrap;"><%= otherTotal.getDeletedAccountDataStr() %></td>
      <td style="white-space: nowrap;"><%= otherTotal.getInactiveAccountDataStr() %></td>
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
     	<td style="white-space: nowrap;"><%= oData.get("active").getActiveAccountDataStr() %></td>
    	<td style="white-space: nowrap;"><%= oData.get("new").getNewAccountDataStr() %></td>
    	<td style="white-space: nowrap;"><%= oData.get("deleted").getDeletedAccountDataStr() %></td>
    	<td style="white-space: nowrap;"><%= oData.get("inactive").getInactiveAccountDataStr() %></td>
    </tr>
    <% } %>

  </table>
  </div>
