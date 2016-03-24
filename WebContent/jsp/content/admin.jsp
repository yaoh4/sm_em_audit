<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>

<s:include value="/jsp/content/manageAccounts.jsp" />
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery-ui-1.11.3.css"/>
<link rel="stylesheet" type="text/css" href="../stylesheets/jquery.cleditor.css" />
<script src="../scripts/jquery.cleditor.min.js" type="text/javascript"></script>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

function sendNotice()
 {
	var result = "";
	
	if ($('#sendAuditNotice').val() == "true") {
		 email = document.getElementById('icEmails').value;
		
		 $.ajax({
				url: "composeEmailAction.action",
				type: "post",
				async:   false,
				success: function(msg){
					result = $.trim(msg);
				}, 
				error: function(){}		
			});
		 var elements = result.split("|");
		 var sMailto = "mailto:"+email+"?subject=" + elements[0] + "&cc=" + elements[1]; 
		
	     var iframeHack;
	     iframeHack = document.createElement("IFRAME");
	     iframeHack.src = sMailto;
	     document.body.appendChild(iframeHack);
	     document.body.removeChild(iframeHack);
	}
 }
 
function validateFormAlert()
{
	var ConfirmMessage = confirm("Are you sure you want to reset the audit ?");
	if (ConfirmMessage) {
		validateForm();

	} else {
		return false;
	}
}

function validateForm() {
   return true;
}

function submitReset(){
	$('#adminActionId').attr("action", "resetAudit");
	$('#adminActionId').submit();
}
function submitForm(){
	var url = "<%=contextPath%>" + "/admin/openEmail";
	var winName = "email";
	var features = "scrollbars=yes,resizable=yes,height="+screen.height+",width="+screen.width+",menubar=yes,toolbar=yes, status=yes";

	var newWin = window.open(url, winName, features);
	newWin.moveTo(0,0);
	return false;
}

</script>
   

<body onload="sendNotice();">
</body>



<div class="tab-content">

  
 
 
  <form class="form-horizontal" role="form" action="adminHome.action" id="adminActionId" namespace="/admin" method="post">
  
    <s:hidden id="sendAuditNotice" name="sendAuditNotice" />
    <s:hidden id="icEmails" name="icEmails" />
 
 <p style="text-align:left;"> 
<span style="float:right;"><img src="../images/mail.png" width="22" height="22" alt="EMAIL"/>
<a href="#" onclick="submitForm();">Open/Edit Audit Email</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
</p>
 
    <div class="form-group">
    	<label for="Audit" class="col-sm-3 control-label" style="padding-top:0px;">Audit:</label>
    	<div class="col-sm-4" style="padding-bottom:0px;">
			<s:if
	    		test="%{emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_RESET)}">
        		<s:radio name="emAuditsVO.i2eAuditFlag"
						list="#{'false':'IMPAC II Only','true':'IMPAC II and I2E'}"
						template="radiomap-nobr.ftl" />
			</s:if>
       		<s:else>
       			<s:radio name="emAuditsVO.i2eAuditFlag"
						list="#{'false':'IMPAC II Only','true':'IMPAC II and I2E'}" disabled="true"
						template="radiomap-nobr.ftl" /> 
       		</s:else>
   		</div>
    </div>
  
    <div class="form-group">
    	<label  for="category" class="col-sm-3 control-label" style="padding-top:0px;">Account Categories:</label>
    	<div class="col-sm-4">
			<s:if
	    		test="%{emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_RESET)}">
        		<s:checkboxlist  list="%{displayCategories}"  value="%{emAuditsVO.categoryList}" name="emAuditsVO.categories"/>
			</s:if>
       		<s:else>
       			<s:checkboxlist  list="%{displayCategories}" disabled="true" value="%{emAuditsVO.categoryList}" name="emAuditsVO.categories"/>
       		</s:else>
   		</div>
    </div>
   
	<div class="form-group">
		<label class="control-label col-sm-3" for="rate_range">Accounts Audited Date Range:</label>
      	<div class="col-sm-4">
			<s:if
	    		test="%{emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_RESET)}">
        		<sj:datepicker id="auditStartDate" size="15" placeholder="Select Start Date" name="emAuditsVO.impaciiFromDate" displayFormat="mm/dd/yy" buttonImage="../images/calendar_icon.gif" buttonImageOnly="true" buttonText="Select Start Date"  changeYear="true" yearRange="-10:+10" />
       			&nbsp;&nbsp;&nbsp;<label for="auditEndDate" style="width: auto;">to</label>&nbsp;&nbsp;&nbsp;
         		<sj:datepicker id="auditEndDate" size="15" name="emAuditsVO.impaciiToDate" displayFormat="mm/dd/yy" buttonImage="../images/calendar_icon.gif" buttonImageOnly="true" buttonText="Select End Date"  changeYear="true" yearRange="-10:+10" /> 
       		</s:if>
       		<s:else>
       			<div class="read_only">
       				<s:date name="emAuditsVO.impaciiFromDate" format="MM/dd/yyyy" />
		  			&nbsp;&nbsp;<label for="auditEndDate" style="width: auto;">to</label>&nbsp;&nbsp;
		 			<s:date name="emAuditsVO.impaciiToDate" format="MM/dd/yyyy" />
		 		</div>       		
       		</s:else>
      	</div> 
	</div>
  
	<div class="form-group">
    	<label class="control-label col-sm-3" for="comments">Comments:</label>
      	<div class="col-sm-9">          
        	<s:textarea cssClass="form-control" id="comments" name="emAuditsVO.comments" placeholder="Enter Your Comments" rows="4"></s:textarea>
    	</div>
	</div>
 
   
	<div class="form-group">        
    	<div class="col-sm-offset-3 col-sm-9">
        	<s:if test="%{emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_RESET)}">
              	<s:submit value="Start Audit" cssClass="btn btn-primary" action="startAudit" onClick="openLoading(); $('#sendAuditNotice').val('true');"/>      
          	</s:if>
      
          	<s:elseif test="%{emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_ENABLED)}">
            	<s:submit value="Disable Audit" cssClass="btn btn-primary" action="endAudit" onClick="return validateForm()"/>      
            </s:elseif>
      
          	<s:elseif test="%{emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_DISABLED)}">
              	<s:submit value="Enable Audit" cssClass="btn btn-primary" action="enableAudit" onClick="return validateForm()"/>      
           <!--  <s:submit value="Reset Audit" cssClass="btn btn-primary" action="resetAudit" onClick="return validateForm()"/> -->
           <input type="button" class="btn btn-primary" value="End Audit" onclick="openConfirmation();">
          	</s:elseif>
      	</div>
     </div>
      	<br><br>
      	<s:if test="%{!emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_RESET)}">
      		<div>
   					<h4>Audit History</h4>
   					<s:include value="auditHistory.jsp"/>
 			</div>
      	</s:if>
      	     	 
</form>
<div id="loading" align="center" style="display:none;"><img src="../images/loading.gif" alt="Loading" /></div>
<div id="confirmation" align="center" style="display:none;" title="End Audit Confirmation">
<br/>
You have attempted to end the audit. Are you sure?
</div>
<!--  tab-content -->
</div>
<!-- panel -->
</div>

 

