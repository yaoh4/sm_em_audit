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
			 			var userId = $('#userId').val();
			 			var networkId = $('#networkId').val();
			 			var cId = $('#cellId').val();
			 			var nId = $('#nameId').val();
			 			var aId = $('#selectActId').val();
			 			var actionLabel = jQuery('#selectActId option:selected').text();
			 			var comments = $('#noteText').val();
			 			var category = $('#categoryId').val();
			 			var extraChars = $.trim(comments).length - 200;
			 			var transferOrg = $('#transferOrg').val();
			 			if(aId == null || $.trim(aId).length < 1){
			 				$('#errorMessage').html("<font color='red'><s:property value='%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@ACTION_SELECTION)}'/></font>");
			 			}else if((aId == "3" || aId == "4" || aId == "7" || aId =="10") && $.trim(comments).length < 1){
			 				$('#errorMessage').html("<font color='red'><s:property value='%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@EMPTY_NOTE)}'/></font>");
			 			}else if($.trim(comments).length > 200){
			 				$('#errorMessage').html("<font color='red'><s:property value='%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@MISSING_NOTE)}'/>" + " by " + extraChars + " characters.</font>");
			 			}else if((aId == "50" || aId == "51" || aId == "52" || aId =="53") && transferOrg.length < 1){
			 				$('#errorMessage').html("<font color='red'><s:property value='%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@TRANSFER_ORG_NOT_SELECTED)}'/></font>");
			 			}else{
				 			$.ajax({
				 				url: "submitAction.action",
				 				type: "post",
				 				data: {pId: cId, aId: aId, note: comments, cate: category, transferOrg:transferOrg},
				 				async:   false,
				 				success: function(msg){
				 					result = $.trim(msg);
				 				}, 
				 				error: function(){}		
				 			});
				 			var items = result.split(";");
				 			if(items[0] == 'validationError'){
				 				$('#errorMessage').html("<font color='red'>" + items[1] + "</font>");
				 			}else if(items[0] == "permission"){
				 				$('#errorMessage').html("<font color='red'><s:property value='%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@ERROR_PERMISSION)}'/></font>");
				 			}else if(items[0] == "fail"){
				 				$('#errorMessage').html("<font color='red'><s:property value='%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@ERROR_SAVE_TO_DATABASE)}'/></font>");
				 			}
				 			else if(aId == "50" || aId == "51" || aId == "52" || aId == "53"){
				 				if(!$('#'+cId).text().match('(Transferred)')){
				 					$('#'+cId).append("</br>(Transferred)");
				 				}
				 				if($('#'+cId).has("#hiddenTransferredNciOrg"+cId).length > 0){
				 					$('#hiddenTransferredNciOrg'+cId).val(transferOrg);
				 				}
				 				else{
				 					$('#'+cId).append("<input type='hidden' id='hiddenTransferredNciOrg"+ cId + "' value='" + transferOrg +"' />");
				 				}
				 				if($('#'+cId).has('#hiddenAction'+cId).length > 0){
				 					$('#hiddenAction'+cId).val(aId);
				 				}	
				 				$( this ).dialog( "close" );
				 				refreshIcDash();
				 			}else{
				 				var isTransferred = $('#'+cId).text().match('(Transferred)');
				 				$('#'+cId).html("");
				 				var actStr = "";
				 				if($.trim(comments).length > 0){
				 					actionLabel = actionLabel + "<br/><a href=\"javascript:fetchAuditNote(" + cId + ", '" + category + "');\"><img src='../images/commentchecked.gif' alt=\"NOTE\"/></a>";
				 				}
				 				if(role == "EMADMIN"){
				 					actStr = actionLabel + "<input type='button' Value='Undo' onclick='unsubmitAct(&#39;"+ nId + "&#39;," + cId + ",&#39;" + userId + "&#39;,&#39;" + networkId + "&#39;);'/> " + 
				 					"<input type='hidden' id='hiddenAction"+ cId + "' value='" + aId +"' />";
				 				}else{
				 					actStr = actionLabel;
				 				}
				 				if(aId == "3" || (category == 'INACTIVE' && aId == '13')){
				 					var emUrl  = "";
				 					if(networkId == null || networkId == "null") {
				 						emUrl = "<a href='"+ $('#i2eemlinkId').val() +"' target='_BLANK'>" + $('#i2eemlinkTextId').val() + "</a>";
				 					}else {
				 						emUrl = "<a href='"+ $('#i2eemlinkId').val() + "?personPageAction=Find&SEARCH_AGENCY_ID=" + networkId + "' target='_BLANK'>" + $('#i2eemlinkTextId').val() + "</a>";
				 					}
				 					if($('#eraualinkId').val() == "NA"){
				 						
				 						actStr = actStr + "<br/><a href='javascript:openEraua();'>" + $('#eraualinkTextId').val() + "</a><br/>" + emUrl;
				 					}else{
				 						if(userId == null || userId == "null") {
				 							actStr = actStr + "<br/><a href='" + $('#eraualinkId').val() + "' target='_BLANK'>" + $('#eraualinkTextId').val() + "</a><br/>"+ emUrl;
				 						}else {
				 							actStr = actStr + "<br/><a href='" + $('#eraualinkId').val() + "accounts/manage.era?accountType=NIH&userId=" + userId + "' target='_BLANK'>" + $('#eraualinkTextId').val() + "</a><br/>"+ emUrl;
				 					}
				 				}
				 				}
				 				$('#'+cId).html(actStr);
				 				if(isTransferred){
				 					$('#'+cId).append("</br>(Transferred)");
				 				}				 				
				 				var elements = result.split(";");
				 				var submitted = "Submitted on " + elements[0] + " by " + elements[1];
				 				$('#submittedby'+cId).html(submitted);
				 				$( this ).dialog( "close" );
				 				refreshIcDash();
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
			 			var userId = $('#userId').val();
			 			var networkId = $('#networkId').val();
			 			var parentNedOrgPath = $('#orgId').val();
			 			$.ajax({
			 				url: "unsubmitAction.action",
			 				type: "post",
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
			 				
			 				var isTransferred = $('#'+cId).text().match('(Transferred)');
			 				$('#'+cId).html("<input type='button' Value='Complete' onclick='submitAct(&#39;"+ nId + "&#39;," + cId +",&#39;" + userId + "&#39;,&#39;" + networkId + "&#39;,&#39;" + parentNedOrgPath +"&#39;);'/>" + 		
			 				"<input type='hidden' id='hiddenAction"+ cId + "' value='" + $('#hiddenAction' +cId).val() +"' /> ");
			 				if(isTransferred){
			 					$('#'+cId).append("</br>(Transferred)");
			 				}	
			 				$('#submittedby'+cId).html("");
				 			$( this ).dialog( "close" ); 	
				 			refreshIcDash();
			 			}
			 		},
			 		Cancel: function() {$( this ).dialog( "close" );}
			 }
		});
	});
	function submitAct(name, cellId, userId, networkId, parentNedOrgPath){
		$('#errorMessage').html("");
		$('#nameId').val(name);		
		$('#userId').val(userId);
		$('#networkId').val(networkId);
		if($.trim(name).length < 1){
			$('#nameValue').html("<label style=padding-left:13px>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>");
		}else{
			$('#nameValue').html("<label style=padding-left:13px>" + name + "</label>");
		}
		$('#cellId').val(cellId);
		if($('#hiddenTransferredNciOrg'+cellId).length > 0){
			$('#hiddenAction'+cellId).val("");
			$('#orgId').val($('#hiddenTransferredNciOrg'+cellId).val());
		} else {
			$('#orgId').val(parentNedOrgPath);
		}
		$('#selectActId').val($('#hiddenAction'+cellId).val());
		var note = getNote(cellId, $('#categoryId').val());
		$('#noteText').val(note);		
		
		var tranferOptionExists = false;
		$.each([ 50, 51, 52, 53 ], function( index, value ) {			 
			 if( 0 != $('#selectActId option[value='+value+']').length){
				 tranferOptionExists = true;
				 return false;
			 }
		});		
		if(tranferOptionExists){
			var actionId = $('#hiddenAction'+cellId).val();
			if(actionId == 50 || actionId == 51 || actionId == 52 || actionId == 53){
				onActionChange(actionId,'');
				$('#transferOrgDiv').css("display","inline");
				if(action == '52') {
					$('#transferOrg').val('');
				}
				else {
					$('#transferOrg').val($('#hiddenTransferredNciOrg'+cellId).val());
				}
			}
			else{
				$('#transferOrgDiv').css("display","none");
			}
		}
		$("#submitAction").dialog( "open" );
	}	
	function unsubmitAct(name, cellId, userId, networkId){
		$('#unsubmitName').val(name);
		$('#userId').val(userId);
		$('#networkId').val(networkId);
		$('#unsubmitCellId').val(cellId);
		$("#unsubmitAction").dialog( "open" );
	}
	function clearFields(){
		$('#auditForm').attr("action", "clearAllFields");
		$('#auditForm').submit();
	}

</script>

<div class="tab-content">
<s:set name="act" value="formAction"/>
<div class="tab-pane fade active in" id="par1">
  <s:form id="auditForm" action="%{formAction}" cssClass="form-horizontal">
  <fieldset style="padding: 15px 0;">
  <legend style="background-color: #fff; margin: 0 15px; padding: 0 10px; color: #333; border-radius:4px;">Search Criteria</legend>
    <div class="form-group">
      <label class="control-label col-sm-3" for="f-name">IMPAC II User First Name:</label>
      <div class="col-sm-9">
        <s:textfield name="searchVO.userFirstname" placeholder="Enter First Name" maxlength="192" cssClass="form-control" value="%{#session.searchVO.userFirstname}" id="f-name" style="width:590px;"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-3" for="l-name">IMPAC II User Last Name:</label>
      <div class="col-sm-9">          
        <s:textfield name="searchVO.userLastname" placeholder="Enter Last Name" maxlength="192" cssClass="form-control" value="%{#session.searchVO.userLastname}" id="l-name" style="width:590px;"/>
      </div>
</div>
<s:if test="category == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED">
  <div class="form-group">
      <label class="control-label col-sm-3" >Accounts Deleted By:</label>
      <div class="col-sm-9"> 
       <s:if test="role == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@USER_ROLE_SUPER_USER">    
      <s:select id="org" name="searchVO.organization" cssClass="form-control" value="%{#session.searchVO.organization}" onchange="onOrgChange(this.value);" list ="organizationList" listKey="optionKey" listValue="optionValue" headerKey="all" headerValue="All" style="width:590px;"/>
      </s:if>
      <s:else>
      <s:select id="org" name="searchVO.organization" cssClass="form-control" value="%{#session.searchVO.organization}" list ="organizationList" listKey="optionKey" listValue="optionValue" headerKey="all" headerValue="All" style="width:590px;" />
      </s:else>
      </div>
 </div>     
</s:if>
<s:else>
 <div class="form-group">
      <label class="control-label col-sm-3" for="org">NCI Organization:</label>
      <div class="col-sm-9"> 
       <s:if test="role == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@USER_ROLE_SUPER_USER">    
      <s:select id="org" name="searchVO.organization" cssClass="form-control" value="%{#session.searchVO.organization}" onchange="onOrgChange(this.value);" list ="organizationList" listKey="optionKey" listValue="optionValue" headerKey="all" headerValue="All" style="width:590px;"/>
      </s:if>
      <s:else>
      <s:select id="org" name="searchVO.organization" cssClass="form-control" value="%{#session.searchVO.organization}" list ="organizationList" listKey="optionKey" listValue="optionValue" headerKey="all" headerValue="All" style="width:590px;" />
      </s:else>
      </div>
 </div>     
 </s:else>
 <s:if test="role == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@USER_ROLE_SUPER_USER">   
	 <div class="form-group" style="margin-top: -10px;">
	 <label class="control-label col-sm-3" for="excludeNciCheck"> </label>
	 <div class="col-sm-9">
	    <s:if test="#session.currentPage == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED">
      	<s:checkbox name="searchVO.excludeNCIOrgs" cssStyle="valign:bottom" id="excludeNciCheck" disabled="true"/><label style="valign:bottom; font-weight: normal; font-size: 0.9em;">Exclude NCI Orgs with IC Coordinators</label>
      	</s:if>
      	<s:elseif test="#session.dashboard == @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@DASHBOARD">
	      	<s:checkbox name="searchVO.excludeNCIOrgs" cssStyle="valign:bottom" id="excludeNciCheck" disabled="true"/><label style="valign:bottom; font-weight: normal; font-size: 0.9em;">Exclude NCI Orgs with IC Coordinators</label>
      	</s:elseif>
      	<s:else>
      	<s:checkbox name="searchVO.excludeNCIOrgs" cssStyle="valign:bottom" id="excludeNciCheck"/><label style="valign:bottom; font-weight: normal; font-size: 0.9em;">Exclude NCI Orgs with IC Coordinators</label>
      	</s:else>
     </div>
     </div>
 </s:if>
 <div class="form-group">
   <!--  Audit Period selection -->
    <label class="control-label col-sm-3" >Audit Period:</label>
      <div class="col-sm-9"> 
      <s:select name="searchVO.auditId" cssClass="form-control" value="%{#session.searchVO.auditId}" list ="auditPeriodList" listKey="optionKey" listValue="optionValue" style="width:590px;" />
     </div>
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
<div id="anchor"></div>

<div style="padding-right: 10px;float:right;">
	<span style="font-size: 0.9em">
		<a href="#" onclick="window.open('<s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@KEY_ROLES_DOC_LINK)}"/>')">
		IMPAC II User Roles (.pdf) 
		</a>
	</span>
</div>
<div style="width: 80%; padding-right: 10px; padding-bottom: 30px; padding-left: 20px;">
	<s:if test="%{activeAuditAccounts.list.size > 0 && (#act == 'searchActiveAuditAccounts' || #act == 'searchNewAuditAccounts')}">
		<span style="font-size: 0.9em; font-weight:bold;">
			<b>NOTE: Below is a snapshot of IMPAC II accounts between <s:date name="emAuditsVO.impaciiFromDate" format="MM/dd/yyyy" />
			 and <s:date name="emAuditsVO.impaciiToDate" format="MM/dd/yyyy" />. 
    		Please note, the data for "NED Org Path" and "Current IMPAC II Account Status" are real-time, 
    		meaning their current NED organization and IMPAC II account status are being displayed.</b>
		</span>
	</s:if>
</div>



<s:if test="showResult">
<div class="panel panel-default">
  <div class="panel-heading">
  <s:if test="%{#act == 'searchActiveAuditAccounts'}">
  <h3  class="panel-title">Results - <s:property value='%{getDescriptionByCode(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@APP_LOOKUP_CATEGORY_LIST, @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_ACTIVE)}' /></h3>
  </s:if>
  <s:if test="%{#act == 'searchNewAuditAccounts'}">
  <h3  class="panel-title">Results - <s:property value='%{getDescriptionByCode(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@APP_LOOKUP_CATEGORY_LIST, @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_NEW)}' /></h3>
  </s:if>
  <s:if test="%{#act == 'searchDeletedAuditAccounts'}">
  <h3  class="panel-title">Results - <s:property value='%{getDescriptionByCode(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@APP_LOOKUP_CATEGORY_LIST, @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_DELETED)}' /></h3>
  </s:if>
  <s:if test="%{#act == 'searchInactiveAuditAccounts'}">
  <h3  class="panel-title">Results - <s:property value='%{getDescriptionByCode(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@APP_LOOKUP_CATEGORY_LIST, @gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@CATEGORY_INACTIVE)}' /></h3>
  </s:if>
  
  </div>
 <s:if test="%{activeAuditAccounts.list.size > 0}">
<div align="center" style="overflow:auto;">
	<s:include value="/jsp/content/accountSearchResult.jsp?act=%{formAction}"/>
</div>
</s:if>
<s:else>
	<div style="text-align:left; width: 100%; padding-left: 10px; padding-top: 10px; padding-bottom:10px;"><s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@NOTHING_DISPLAY)}"/></div>
	<s:if test="anchorDash">
		<body onload="moveToDash();"></body>
	</s:if>
	<s:else>
		<body onload="moveToAnchor();"></body>
	</s:else>
</s:else>
</div>
</s:if> 

<div id="submitAction" style="display: none;" title="Complete Review">
	<s:include value="/jsp/helper/submitActionContent.jsp" />
</div>
<div id="errorDialog" style="display: none;" title="Submit Action">
	<br/>
	<div align="center">
	<font color='red'><s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@ERROR_SAVE_TO_DATABASE)}"/></font>
	</div>
</div>
<div id="loading" align="center" style="display:none;"><img src="../images/loading.gif" alt="Loading" /></div>
<div id="unsubmitAction" style="display: none;" title="Undo Action">
	<br/>
	<div align="center">
	<s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@UNDO_COMFIRMATION)}"/>
	</div>
	<input type="hidden" id="unsubmitName"/>
	<input type="hidden" id="unsubmitCellId"/>
</div>  
<div id="eraua_na" align="center" style="display:none;" title="Information">
<br/>
	<div align="center">
	<s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@ERAUA_INFO)}"/>
	</div>
</div>
<script>
	$( window ).ready(function() { 
		onOrgChange($('#org option:selected').val());
	});
</script>