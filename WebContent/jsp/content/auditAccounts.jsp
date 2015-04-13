<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>

 
<s:include value="/jsp/content/manageAccounts.jsp" />
<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery-ui-1.11.3.css"/>

<script>
	$(function() {
		$("#submitAction").dialog({
			 autoOpen: false,
			 resizable: false,
			 width: 600,
			 height:400,
			 modal: true,
			 show: { effect: "slide", duration: 250 },
			 hide: { effect: "slide", duration: 250 },
			 buttons: {
			 		OK: function() {
			 			var result = "";
			 			var role = $('#roleId').val();
			 			var cId = $('#cellId').val();
			 			var nId = $('#nameId').val();
			 			var aId = $('#selectActId').val();
			 			var actionLabel = jQuery('#selectActId option:selected').text();
			 			var comments = $('#noteText').val();
			 			var category = $('#categoryId').val();
			 			if((aId == "3" || aId == "4" || aId == "7" || aId =="10") && $.trim(comments).length < 1){
			 				$('#errorMessage').html("<font color='red'>Please enter Notes for the submission.</font>");
			 			}else{
				 			$.ajax({
				 				url: "submitAction.action",
				 				type: "get",
				 				data: {pId: cId, aId: aId, note: comments, cate: category},
				 				async:   false,
				 				success: function(msg){
				 					result = $.trim(msg);
				 				}, 
				 				error: function(){}		
				 			});
				 			var items = result.split(";");
				 			
				 			if(items[0] == 'validationError'){
				 				$('#errorMessage').html("<font color='red'>" + items[1] + "</font>");
				 			}else if(items[0] == "fail"){
				 				$( this ).dialog( "close" );
				 				openErrorDialog();
				 				
				 			}else{
				 				$('#'+cId).html("");
				 				var actStr = "";
				 				if(role == "EMADMIN"){
				 					actStr = actionLabel + "<br/><input type='button' Value='Undo' onclick='unsubmitAct(&#39;"+ nId + "&#39;," + cId +");'/> " + 
				 					"<input type='hidden' id='hiddenAction"+ cId + "' value='" + aId +"' /> <input type='hidden' id='hiddennote" + cId +"' value='" + comments +"'/>";
				 				}else{
				 					actStr = actionLabel;
				 				}
				 				if(aId == "3"){
				 					actStr = actStr + "<br/><a href='https://apps.era.nih.gov/useradmin/SearchAccount.action' target='_BLANK'>eRA UA</a><br/><a href='https://i2e.nci.nih.gov/common/PersonController' target='_BLANK'>I2E EM</a>";
				 				}
				 				$('#'+cId).html(actStr);
				 				$('#note' + cId).html(comments);
				 				var elements = result.split(";");
				 				var submitted = "Submitted on " + elements[0] + " by " + elements[1];
				 				$('#submittedby'+cId).html(submitted);
				 				$( this ).dialog( "close" );
				 			}
			 			}
			 		},
			 		Cancel: function() {$( this ).dialog( "close" );}
			 }
		});
		$("#unsubmitAction").dialog({
			 autoOpen: false,
			 resizable: false,
			 width: 400,
			 height:200,
			 modal: true,
			 show: { effect: "slide", duration: 250 },
			 hide: { effect: "slide", duration: 250 },
			 buttons: {
			 		OK: function() {
			 			var result = "";
			 			var cId = $('#unsubmitCellId').val();
			 			var nId = $('#unsubmitName').val();
			 			var category = $('#categoryId').val();
			 			$.ajax({
			 				url: "unsubmitAction.action",
			 				type: "get",
			 				data: {pId: cId, cate: category},
			 				async:   false,
			 				success: function(msg){
			 					result = $.trim(msg);
			 				}, 
			 				error: function(){}		
			 			});
			 			if(result == "fail"){
			 				$( this ).dialog( "close" );
			 				openErrorDialog();
			 			}else{
			 				$('#'+cId).html("<input type='button' Value='Complete' onclick='submitAct(&#39;"+ nId + "&#39;," + cId +");'/>" + 
			 				"<input type='hidden' id='hiddenAction"+ cId + "' value='" + $('#hiddenAction' +cId).val() +"' /> <input type='hidden' id='hiddennote" + cId +"' value='" + $('#hiddennote' + cId).val() +"'/>");
			 				$('#note' + cId).html("");
			 				$('#submittedby'+cId).html("");
				 			$( this ).dialog( "close" ); 	
			 			}
			 		},
			 		Cancel: function() {$( this ).dialog( "close" );}
			 }
		});
	});
	function submitAct(name, cellId){
		$('#errorMessage').html("");
		$('#nameId').val(name);
		$('#nameValue').html("<label style=padding-left:13px>" + name + "</label>");
		$('#cellId').val(cellId);
		$('#selectActId').val($('#hiddenAction'+cellId).val());
		$('#noteText').val($('#hiddennote'+cellId).val());
		$("#submitAction").dialog( "open" );
	}	
	function unsubmitAct(name, cellId){
		$('#unsubmitName').val(name);
		$('#unsubmitCellId').val(cellId);
		$("#unsubmitAction").dialog( "open" );
	}
	function clearFields(){
		$('#auditForm').attr("action", "clearAllFields");
		$('#auditForm').submit();
	}

</script>

<div class="tab-content">

<div class="tab-pane fade active in" id="par1">
  <s:form id="auditForm" action="%{formAction}" cssClass="form-horizontal">
  <fieldset style="padding: 15px 0;">
  <legend style="background-color: #fff; margin: 0 15px; padding: 0 10px; color: #333; border-radius:4px;">Search Criteria</legend>
    <div class="form-group">
      <label class="control-label col-sm-3" for="f-name">IMPAC II User First Name:</label>
      <div class="col-sm-9">
        <s:textfield name="searchVO.userFirstname" maxlength="192" cssClass="form-control" value="%{#session.searchVO.userFirstname}" id="f-name"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-3" for="l-name">IMPAC II User Last Name:</label>
      <div class="col-sm-9">          
        <s:textfield name="searchVO.userLastname" maxlength="192" cssClass="form-control" value="%{#session.searchVO.userLastname}" id="l-name"/>
      </div>
    </div>
 <div class="form-group">
      <label class="control-label col-sm-3" >NCI Organization:</label>
      <div class="col-sm-9"> 
       <s:if test="role == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@USER_ROLE_SUPER_USER">    
      <s:select name="searchVO.organization" cssClass="form-control" value="%{#session.searchVO.organization}" onchange="onOrgChange(this.value);" list ="organizationList" listKey="optionKey" listValue="optionValue" headerKey="all" headerValue="All" style="width:590px;"/>
      </s:if>
      <s:else>
      <s:select name="searchVO.organization" cssClass="form-control" value="%{#session.searchVO.organization}" list ="organizationList" listKey="optionKey" listValue="optionValue" headerKey="all" headerValue="All" style="width:590px;" />
      </s:else>
      </div>
      </div>
       <div class="form-group" style="margin-top: -10px;">
         <label class="control-label col-sm-3" > </label>
      <s:if test="role == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@USER_ROLE_SUPER_USER">   
      <div class="col-sm-9">  
      	<s:checkbox name="searchVO.excludeNCIOrgs" id="excludeNciCheck"/><label style="font-weight: normal; font-size: 0.9em;">Exclude NCI Orgs with IC Coordinators</label>
      </div>
      </s:if>
    </div>
<div class="form-group">
      <label class="control-label col-sm-3" >Action:</label>
      <div class="col-sm-9">  
      <s:select name="searchVO.act" cssClass="form-control" value="%{#session.searchVO.act}" list="actionList"  listKey="optionKey" listValue="optionValue" style="width:590px;"/>
      </div>
      </div>

    <div class="form-group">        
      <div class="col-sm-offset-3 col-sm-9">
       <s:submit value="Search" cssClass="btn btn-primary" onclick="openLoading();"/>
	   <input type="button" value="Clear"  class="btn btn-default" onclick="clearFields();"/>
      </div>
    </div>
    </fieldset>
    <s:hidden id="categoryId" name="category"/> 
	<s:hidden id="roleId" name="role"/>
  </s:form>
</div>
<br/>
</div>

<div style="text-align:right; width: 100%; padding-right: 10px; padding-bottom: 20px;">
    <span style="font-size: 0.9em;"><a href="#" onclick="window.open('<s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@KEY_ROLES_DOC_LINK)}"/>')">IMPAC II User Roles (.pdf) </a></span>
</div> 

<s:if test="showResult">
<div class="panel panel-default">
  <div class="panel-heading">
 <h3  class="panel-title">Search Results</h3>
  </div>
 <s:if test="%{activeAuditAccounts.list.size > 0}">
<div align="center" style="overflow:auto;">
	<s:include value="/jsp/content/accountSearchResult.jsp?act=%{formAction}"/>
</div>
</s:if>
<s:else>
	<div style="text-align:left; width: 100%; padding-left: 10px; padding-top: 10px; padding-bottom:10px;">Nothing found to display.</div>
</s:else>
</div>
</s:if> 

<div id="submitAction" style="display: none;" title="Complete Review">
	<s:include value="/jsp/helper/submitActionContent.jsp" />
</div>
<div id="errorDialog" style="display: none;" title="Submit Action">
	<br/>
	<div align="center">
	<font color='red'>Failed to save data into database, please contact system administrator.</font>
	</div>
</div>
<div id="loading" align="center" style="display:none;"><img src="../images/loading.gif" alt="Loading" /></div>
<div id="unsubmitAction" style="display: none;" title="Undo Action">
	<br/>
	<div align="center">
	Are you sure to undo the review?
	</div>
	<input type="hidden" id="unsubmitName"/>
	<input type="hidden" id="unsubmitCellId"/>
</div>  

