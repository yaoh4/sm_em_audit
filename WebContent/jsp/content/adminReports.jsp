<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>

 
<s:include value="/jsp/content/manageAccounts.jsp" />
<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery-ui-1.11.3.css"/>
<script type="text/javascript">
function clearFields(){
	$('#reportForm').attr("action", "resetFields");
	$('#reportForm').submit();
}
</script>
<div class="tab-content">
<s:set name="type" value="searchType"/>
<div class="tab-pane fade active in" id="par1">
  <s:form id="reportForm" action="reportSearch" cssClass="form-horizontal">
  <s:set name="type" value="searchType"/>
  <fieldset style="padding: 15px 0;">
  <legend style="background-color: #fff; margin: 0 15px; padding: 0 10px; color: #333; border-radius:4px;">Report Criteria</legend>
 <div class="form-group">
   <!--  Audit Period selection -->
    <label class="control-label col-sm-3" >Audit Period:</label>
      <div class="col-sm-9"> 
      <s:select name="searchVO.auditId" cssClass="form-control" value="%{#session.searchVO.auditId}" list ="auditPeriodList" listKey="optionKey" listValue="optionValue" style="width:590px;" />
     </div>
 </div>   
  <div class="form-group">
      <label class="control-label col-sm-3" >Category:</label>
      <div class="col-sm-9">  
      <s:select name="searchVO.category" cssClass="form-control" value="%{#session.searchVO.category}" list="categoryList"  listKey="optionKey" listValue="optionValue" style="width:590px;"/>
      </div>																						         
  </div>
    <div class="form-group">        
      <div class="col-sm-offset-3 col-sm-9">
       <s:submit value="Search" cssClass="btn btn-primary" onclick="openLoading();"/>
	   <input type="button" value="Clear"  class="btn btn-default" onclick="clearFields();"/>
      </div>
    </div>
    </fieldset>
  </s:form>
</div>
<div id="anchor"></div>
<br/>
</div>
<s:if test="showResult">
<div class="panel panel-default">
  <div class="panel-heading">
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_ACTIVE}">
  <label  class="panel-title"><b>Results - All Active Accounts</b></label>
  </s:if>
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_NEW}">
  <label  class="panel-title"><b>Results - All New Accounts</b></label>
  </s:if>
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED}">
  <label  class="panel-title"><b>Results - All Deleted Accounts</b></label>
  </s:if>
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_INACTIVE}">
  <label  class="panel-title"><b>Results - All Inactive > 130 Days Accounts</b></label>
  </s:if>
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_EXCLUDED}">
  <label  class="panel-title"><b>Results - All Not NCI Purview</b></label>
  </s:if>
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_I2E}">
  <label  class="panel-title"><b>Results - All I2E Accounts</b></label>
  </s:if>
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_TRANSFER}">
  <label  class="panel-title"><b>Results - All Transferred Accounts</b></label>
  </s:if>
  <span style="float:right;"><a href='javascript:openHistory();'>Audit Info</a></span>
  </div>
  
  
  	<s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_TRANSFER}">
  		<s:if test="%{transferredAccounts.list.size > 0}">
			<div align="center" style="overflow:auto; width: 100%;">
				<s:include value="/jsp/content/adminReportTransferredAccountsSearchResult.jsp"/>
			</div>
		</s:if>
		<s:else>
			<div style="text-align:left; width: 100%; padding-left: 10px; padding-top: 10px; padding-bottom:10px;"><s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@NOTHING_DISPLAY)}"/></div>
			<body onload="moveToAnchor();"></body>
		</s:else> 
	</s:if>
	<s:elseif test="%{#type != @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_I2E}">
		<s:if test="%{auditAccounts.list.size > 0}">
			<div align="center" style="overflow:auto; width: 100%;">
				<s:include value="/jsp/content/adminReportSearchResult.jsp"/>
			</div>
		</s:if>
		<s:else>
			<div style="text-align:left; width: 100%; padding-left: 10px; padding-top: 10px; padding-bottom:10px;"><s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@NOTHING_DISPLAY)}"/></div>
			<body onload="moveToAnchor();"></body>
		</s:else> 
	</s:elseif>
	<s:else>
		<s:if test="%{auditI2eAccounts.list.size > 0}">
			<div align="center" style="overflow:auto; width: 100%;">
				<s:include value="/jsp/content/adminReportI2eSearchResult.jsp"/>
			</div>
		</s:if>
		<s:else>
			<div style="text-align:left; width: 100%; padding-left: 10px; padding-top: 10px; padding-bottom:10px;"><s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@NOTHING_DISPLAY)}"/></div>
			<body onload="moveToAnchor();"></body>
		</s:else>
	</s:else>
</div>
</s:if>
<div id="loading" align="center" style="display:none;"><img src="../images/loading.gif" alt="Loading" /></div>
<div id="auditHistory" align="center" style="display:none;" title="">
	<h4>Audit History for <s:property value="selectedAuditDescription"></s:property></h4>	
	<s:include value="auditHistory.jsp"/>
</div>