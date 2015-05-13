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
<br/>
</div>
<s:if test="showResult">
<div class="panel panel-default">
  <div class="panel-heading">
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_ACTIVE}">
  <h3  class="panel-title">Results - All Active Accounts</h3>
  </s:if>
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_NEW}">
  <h3  class="panel-title">Results - All New Accounts</h3>
  </s:if>
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED}">
  <h3  class="panel-title">Results - All Deleted Accounts</h3>
  </s:if>
  <s:if test="%{#type == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_INACTIVE}">
  <h3  class="panel-title">Results - All Inactive Accounts</h3>
  </s:if>
  </div>
 <s:if test="%{auditAccounts.list.size > 0}">
<div align="center" style="overflow:auto; width: 100%;">
	<s:include value="/jsp/content/adminReportSearchResult.jsp"/>
</div>
</s:if>
<s:else>
	<div style="text-align:left; width: 100%; padding-left: 10px; padding-top: 10px; padding-bottom:10px;"><s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@NOTHING_DISPLAY)}"/></div>
</s:else> 
</div>
</s:if>
<div id="loading" align="center" style="display:none;"><img src="../images/loading.gif" alt="Loading" /></div>