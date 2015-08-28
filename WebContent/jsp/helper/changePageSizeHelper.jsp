<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form action="reportSearch" cssClass="form-horizontal">
<div style="display:block; padding-left: 10px; padding-top: 10px; font-size:0.9em; text-align:left;">
	<label class="control-label col-sm-0" for="changePageSize">Change page size:</label>
	<s:select name="changePageSize" id="changePageSize"
			cssClass="form-control" list="session.pageSizeList" value="%{changePageSize}" 
			listKey="optionKey" listValue="optionValue" style="width:100px;" />
	<s:submit value="Go" cssClass="btn btn-primary"/>	
</div>
</s:form>