<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="gov.nih.nci.cbiit.scimgmt.entmaint.utils.ActionDashboardData" %>

<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>

<s:if test="%{actionOrgKeys.size > 0}">
  <table class="table table-condensed table-striped">
      <caption>Unknown</caption>
	<tr>
		<s:if test="emAuditsVO.i2eFromDate != null">
			<th width="20%">NCI Organizations</th>
			<s:if test="emAuditsVO.activeCategoryEnabledFlag != @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@FLAG_NO">
				<th width="16%">Active Accounts</th>
			</s:if>
			<s:if test="emAuditsVO.newCategoryEnabledFlag != @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@FLAG_NO">
				<th width="16%">New Accounts</th>
			</s:if>
			<s:if test="emAuditsVO.deletedCategoryEnabledFlag != @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@FLAG_NO">
				<th width="16%">Deleted Accounts</th>
			</s:if>
			<th width="16%">I2E Accounts</th>
		</s:if>
		<s:else>
			<th width="40%">NCI Organizations</th>
			<s:if test="emAuditsVO.activeCategoryEnabledFlag != @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@FLAG_NO">
				<th width="15%">Active Accounts</th>
			</s:if>
			<s:if test="emAuditsVO.newCategoryEnabledFlag != @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@FLAG_NO">
				<th width="15%">New Accounts</th>
			</s:if>
			<s:if test="emAuditsVO.deletedCategoryEnabledFlag != @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@FLAG_NO">
				<th width="15%">Deleted Accounts</th>
			</s:if>
		</s:else>
	</tr>
	<s:set var="actionKeys" value="actionOrgKeys"/>
    <s:set var="actionOrgData" value="actionOrgsData"/>
	<jsp:useBean id="actionKeys" type="java.util.List"/>
	<jsp:useBean id="actionOrgData" type="java.util.HashMap"/>
	<% 
		for (int i = 0; i< actionKeys.size(); i++) {
			String key = (String)actionKeys.get(i);
	%>
    <tr class="org">
    	<td><strong><%= key %></strong></td>
    	<% 
    		HashMap<String, ActionDashboardData> dData = (HashMap<String, ActionDashboardData>)actionOrgData.get(key); 
    	%>
    	<s:if test="emAuditsVO.activeCategoryEnabledFlag != @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@FLAG_NO">
    		<td><%= dData.get("active").getActiveUnknownDataStr(key) %></td>
    	</s:if>
    	<s:if test="emAuditsVO.newCategoryEnabledFlag != @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@FLAG_NO">
    		<td><%= dData.get("new").getNewUnknownDataStr(key) %></td>
    	</s:if>
    	<s:if test="emAuditsVO.deletedCategoryEnabledFlag != @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@FLAG_NO">
    		<td><%= dData.get("deleted").getDeletedUnknownDataStr(key) %></td>
    	</s:if>
    	<s:if test="emAuditsVO.i2eFromDate != null">
      		<td><%= dData.get("i2e").getI2eUnknownDataStr(key) %></td>
      	</s:if>
    </tr>
    <% } %>

  </table>
</s:if>