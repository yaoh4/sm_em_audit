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
	<jsp:useBean id="orgData" type="java.util.HashMap"/>
	<jsp:useBean id="keys" type="java.util.List"/>
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
<!--     <tr class="org">
      <td><strong>OGA</strong></td>
      <td><a href="#">23/40</a></td>
      <td><a href="#">18/21</a></td>
      <td><a href="#">0/4</a>                    <span> (0%)</span></td>
      <td><a href="#">21/21</a>                                 <span>(100%)</span></td>
    </tr>
   <tr class="org">
      <td class="other">DCEG</td>
      <td><a href="#">xx</a></td>
      <td><a href="#">xx</a></td>
      <td><a href="#">xx</a></td>
      <td><a href="#">xx</a></td>
    </tr> -->
    
  </table>
  </div>
