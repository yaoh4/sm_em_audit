<%@ taglib prefix="s" uri="/struts-tags"%>
 <div id="errorMessage"></div>
 <br/>
 <div class="form-group">
      <label class="control-label col-sm-2" for="f-name"> Name:</label>     
       <span><div id="nameValue"></div></span>      
 </div>
 <div class="form-group">
      <label class="control-label col-sm-2" >Action:</label>
      <div class="col-sm-10">  
      <s:select id="selectActId" onchange="onActionChange(this.value,$('#orgId').val());" cssClass="form-control" value="" list="actionWithoutAllList"  listKey="optionKey" listValue="optionValue"/>
      </div>
</div>
 <div class="form-group" id="transferOrgDiv" style="display:none"  >
      <label class="control-label col-sm-2" style="padding-top: 25px; padding-bottom: 25px;">NCI Org:</label>
      <div class="col-sm-10" style="padding-top: 25px; padding-bottom: 25px;"> 		
 		<s:select id="transferOrg" cssClass="form-control" value="" list ="transferOrgList" listKey="optionKey" listValue="optionValue"/>
       </div>
</div>
<br/>
<br/>
<br/>
  <div class="form-group">
      <label class="control-label col-sm-2" >Notes:</label>
      <div class="col-sm-10">          
       <textarea id="noteText" style="width: 400px; height:100px;"></textarea>
   </div>
</div>
<input type="hidden" id="cellId"/>
<input type="hidden" id="nameId" />
<input type="hidden" id="userId"/>
<input type="hidden" id="networkId"/>
<input type="hidden" id="orgId" />
