<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>


<s:include value="/jsp/content/manageAccounts.jsp" />

<script type="text/javascript">
function sendNotice()
 {
	var result = "";
	$('#sendAuditNotice').val(true);
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
		 var sMailto = "mailto:"+email+"?subject=" + elements[0] + "&body=" + elements[1].substring(0, 512); 
		
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
</script>
   

<body onload="">
</body>



<div class="tab-content">

  
 
 
  <form class="form-horizontal" role="form" action="adminHome.action" namespace="/admin" method="post">
  
    <s:hidden id="sendAuditNotice" name="sendAuditNotice" />
    <s:hidden id="icEmails" name="icEmails" />
 
 <h3>Administer Audit</h3>
    <div class="form-group">
    	<label for="Audit" class="col-sm-3 control-label" style="padding-top:0px;">Audit:</label>
    	<div class="col-sm-4" style="padding-bottom:0px;">
    		<s:checkbox name="emAuditsVO.impac2AuditFlag" disabled="%{disableInput}"/> 
        	<label style="padding-left:0px;">IMPAC II</label>
			&nbsp;&nbsp;&nbsp;
        	<s:checkbox name="emAuditsVO.i2eAuditFlag" style="padding-right:0px;" disabled="true"/> 
			<label style="padding-left:0px;">I2E</label>
   		</div>
    </div>
  
    
	<div class="form-group">
		<label class="control-label col-sm-3" for="rate_range">Accounts Audited Date Range:</label>
      	<div class="col-sm-4">
			<s:if
	    		test="%{emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_RESET)}">
        		<sj:datepicker id="auditStartDate" cssStyle="width:100px;" placeholder="Select" name="emAuditsVO.impaciiFromDate" displayFormat="mm/dd/yy" buttonImage="../images/calendar_icon.gif" buttonImageOnly="true" buttonText="Select start date."  changeYear="true" yearRange="-10:+10" />
       			&nbsp;&nbsp;&nbsp;<label for="auditEndDate" style="width: auto;">to</label>&nbsp;&nbsp;&nbsp;
         		<sj:datepicker id="auditEndDate" cssStyle="width:100px;" name="emAuditsVO.impaciiToDate" displayFormat="mm/dd/yy" buttonImage="../images/calendar_icon.gif" buttonImageOnly="true" buttonText="Select end date."  changeYear="true" yearRange="-10:+10" /> 
       		</s:if>
       		<s:else>
        		<s:property value="emAuditsVO.impaciiFromDate" />
		  		&nbsp;&nbsp;&nbsp;<label for="auditEndDate" style="width: auto;">to</label>&nbsp;&nbsp;&nbsp;
		 		<s:property value="emAuditsVO.impaciiToDate" />
       		</s:else>
      	</div> 
	</div>
  
	<div class="form-group">
    	<label class="control-label col-sm-3" for="comments">Comments:</label>
      	<div class="col-sm-6">          
        	<s:textarea cssClass="form-control" id="comments" name="emAuditsVO.comments" placeholder="Enter Your Comments" rows="4"></s:textarea>
    	</div>
	</div>
 
   
	<div class="form-group">        
    	<div class="col-sm-offset-3">     
        	<s:if test="%{emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_RESET)}">
              	<s:submit value="Start Audit" cssClass="btn btn-primary" action="startAudit" onClick="return validateForm()"/>      
          	</s:if>
      
          	<s:elseif test="%{emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_ENABLED)}">
            	<s:submit value="End Audit" cssClass="btn btn-primary" action="endAudit" onClick="return validateForm()"/>      
            </s:elseif>
      
          	<s:elseif test="%{emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_DISABLED)}">
              	<s:submit value="Enable Audit" cssClass="btn btn-primary" action="enableAudit" onClick="return validateForm()"/>      
              	<s:submit value="Reset Audit" cssClass="btn btn-primary" action="resetAudit" onClick="return validateForm()"/> 
          	</s:elseif>
      	</div>
      	
      	<br><br>
      	<s:if test="%{!emAuditsVO.auditState.equalsIgnoreCase(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@AUDIT_STATE_CODE_RESET)}">
      		<div class="col-sm-offset-3">
   					<h4>Audit History</h4>
   					<s:include value="auditHistory.jsp"/>
 			</div>
      	</s:if>
      	
      	
  	</div>
    <input type="button" value="Test" onclick="sendNotice();"/>
</form>

<!--  tab-content -->
</div>
<!-- panel -->
</div>

 

