<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>

 
<s:include value="/jsp/content/manageAccounts.jsp" />
<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery-ui-1.11.3.css"/>

<div class="tab-content">
<s:set name="act" value="formAction"/>
<div class="tab-pane fade active in" id="par1">
  <s:form id="reportForm" action="reportSearch" cssClass="form-horizontal">
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

