<%@ taglib prefix="s" uri="/struts-tags"%>

 <br/>
 <div id="errorMessage"></div>
 <br/>
 <div class="form-group">
      <label class="control-label col-sm-2" for="f-name"> Name:</label>     
       <span><div id="nameValue"></div></span>      
 </div>
<br/>
<br/>
 <div class="form-group">
      <label class="control-label col-sm-2" >Action:</label>
      <div class="col-sm-10">  
      <s:select id="selectActId" cssClass="form-control" value="" list="actionWithoutAllList"  listKey="optionKey" listValue="optionValue"/>
      </div>
</div>
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