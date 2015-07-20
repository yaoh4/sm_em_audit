<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<% String content = (String)request.getAttribute("emailContent");%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<script language="JavaScript" src="<s:url value="/scripts/jquery-2.1.3.min.js" />" type="text/javascript"></script>
<script language="JavaScript" src="<s:url value="/scripts/jquery-ui-1.11.3.js" />" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<s:url value="/stylesheets/jquery-ui-1.11.3.css" />"/>
<link rel="stylesheet" type="text/css" href="<s:url value="/stylesheets/jquery.cleditor.css"/>" />
<script src="<s:url value="/scripts/jquery.cleditor.min.js" />" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<s:url value="/stylesheets/entmaint.css" />" />
<script>
      $(document).ready(function () { 
    	$("#input").cleditor({height:700});
    	$("#input").val("<%= content %>").blur(); 
    	
    	$("#message").dialog({
   			 autoOpen: false,
   			 resizable: false,
   			 width: 400,
   			 height:200,
   			 modal: true,
   			 show: { effect: "slide", duration: 250 },
   			 hide: { effect: "slide", duration: 250 },
   			 buttons: {
   			 		OK: function() {
   			 		$( this ).dialog( "close" );
   			 		}
   			 }
   		});
      });
      
      function saveEmail(){
    	  var content = $('#input').val();
    	  $.ajax({
    		 	url: "saveEmailAction.action",
				type: "post",
				data: { emailBody: content},
				async:   false,
				success: function(msg){
					openMessage(true);
				}, 
				error: function(){
					openMessage(false);
				}		
			});
      }
      
      function openMessage(flag){
    	  $('#message').dialog("open");
    	  if(flag){
    		  $('#msgLabel').html("<font color='green'>The Email has been saved successfully.</font>")
    	  }else{
    		  $('#msgLabel').html("<font color='red'>The Email has not been saved successfully.</font>")
    	  }
    	
      }
      
      function CloseWindow() 
      {
          window.open('', '_self', '');
          window.close();
      }
</script>      
</head>
<body>


<center><h3> Audit Email</h3></center>
<br/>
   <textarea id="input" name="input"></textarea>
   <br/>
   <span style="float:right;"><input type="button" value="Save Email" onclick="saveEmail();"/></input> &nbsp;&nbsp;&nbsp;<input type="button" onclick="CloseWindow();" value="Close"/></span>
</body>
<div id="message" title="Message">
	<br/>
	<center><label id="msgLabel"/></center>
</div>
</html>
