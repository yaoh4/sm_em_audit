<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{!fieldErrors.isEmpty || !actionMessages.isEmpty || !actionErrors.isEmpty }">
 <div id="errorBox" >
  
   <s:if test="%{ !actionMessages.isEmpty  }">
    <fieldset style="width: auto; font-size: 12px">
    <legend>Message</legend>
    <strong><s:actionmessage/></strong>
	</fieldset>
   </s:if>
   <s:if test="%{ !actionErrors.isEmpty  }">
    <fieldset style="width: auto">
    <legend>Error</legend>
    <strong><s:actionerror/></strong>
	</fieldset>
   </s:if>
   <s:if test="%{ !fieldErrors.isEmpty  }">
    <fieldset style="width: auto">
    <legend>Field Error</legend>
    <strong><s:fielderror/></strong>
	</fieldset>
   </s:if>
  
 </div>
</s:if>