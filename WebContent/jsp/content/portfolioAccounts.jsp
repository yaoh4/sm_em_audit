<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<s:include value="/jsp/content/manageAccounts.jsp" />
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js"	type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css"	href="../stylesheets/jquery-ui-1.11.3.css" />
<script>
	$(function() {
		$("#submitNotesAction").dialog({
			autoOpen : false,resizable : false,	width : 600, height : 400, modal : true,
			show : {effect : "slide",duration : 250},
			hide : {effect : "slide",duration : 250},
			buttons : {
				Done : function() {
					var result = "";
					var impac2Id = $('#cellId').val();
					var name = $('#nameId').val();
					var notes = $('#noteText').val();
					if($.trim(notes).length > 0){					
						$.ajax({
							url : "saveNotes.action", type : "get",	data : {impac2Id : impac2Id,name : name,notes : notes},async : false,
							success : function(msg) {result = $.trim(msg);},
							error : function() {}
						});	
						if (result == 'fail') {
							$(this).dialog("close");
							openErrorDialog();
						} else {
							$("#notesDiv_" + impac2Id).html(notes);
							$("#lastUpdateDiv_" + impac2Id).html(result);
							$(this).dialog("close");
						}
					}
					else{$('#missingNotesMessage').html("<font color='red'>Please enter Notes.</font>");}
				},
				Cancel : function() {$(this).dialog("close");}
			}
		});		
	});
	function submitNotes(name, cellId) { 
		$('#missingNotesMessage').html("");
		$('#nameId').val(name);
		$('#nameValue').html("<label>" + name + "</label>");
		$('#cellId').val(cellId);
		$('#noteText').val($('#notesDiv_'+cellId).text());
		$("#submitNotesAction").dialog("open");
	}	
</script>

<div class="tab-content">
	<div class="tab-pane fade active in" id="portfolioSearchCriteria">
		<s:form action="%{formAction}" cssClass="form-horizontal">
			<fieldset style="padding: 15px 0;">
			
				<div class="form-group">
					<label class="control-label col-sm-3" for="f-name">IMPAC II
						User First Name:</label>
					<div class="col-sm-9">
						<s:textfield name="searchVO.userFirstname" cssClass="form-control"
							value="%{#session.portfolioSearchVO.userFirstname}" id="f-name" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-sm-3" for="l-name">IMPAC II
						User Last Name:</label>
					<div class="col-sm-9">
						<s:textfield name="searchVO.userLastname" cssClass="form-control"
							value="%{#session.portfolioSearchVO.userLastname}" id="l-name" />
					</div>
				</div>	
				
				<s:if test="isSuperUser()">				
					<div class="form-group">
						<label class="control-label col-sm-3" for="portfolioOrg">NCI Organization:</label>
						<div class="col-sm-9">
							<s:select name="searchVO.organization" onchange="onOrgChange(this.value);" 
								id="portfolioOrg" cssClass="form-control"
								value="%{#session.portfolioSearchVO.organization}" list="session.orgList"
								listKey="optionKey" listValue="optionValue" headerKey="all"
								headerValue="All" style="width:590px;" />
						</div>
					</div>       
					
					<div class="form-group" style="margin-top: -10px;">
						<label class="control-label col-sm-3" for="excludeNciCheck"> </label>
						<div class="col-sm-9">
							<s:checkbox name="searchVO.excludeNCIOrgs" id="excludeNciCheck" />
							<label style="font-weight: normal; font-size: 0.9em;">Exclude
								NCI Orgs with IC Coordinators</label>
						</div>
					</div>
				</s:if>
				
				<s:else>
					<div class="form-group">
						<label class="control-label col-sm-3" for="portfolioOrg">NCI Organization:</label>
						<div class="col-sm-9">
							<s:select name="searchVO.organization" id="portfolioOrg" cssClass="form-control"
								value="%{#session.portfolioSearchVO.organization}" list="session.orgList"
								listKey="optionKey" listValue="optionValue" headerKey="all"
								headerValue="All" style="width:590px;" />
						</div>
					</div> 
				</s:else>
				
				<div class="form-group">
					<label class="control-label col-sm-3" for="portfolioCategory">Category:</label>
					<div class="col-sm-9">
						<s:select name="searchVO.category" id="portfolioCategory"
							onchange="onCategoryChage(this.value);" cssClass="form-control"
							value="%{#session.portfolioSearchVO.category}" list="session.categoryList"
							listKey="optionKey" listValue="optionValue" style="width:590px;" />
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-sm-3" for="dateRange">Date Range:</label>
					<div class="col-sm-9" id="dateRange">
						<sj:datepicker size="15" id="dateRangeStartDate"
							name="searchVO.dateRangeStartDate"							
							displayFormat="mm/dd/yy" changeYear="true" yearRange="-10:+10"
							placeholder="Start date"
							buttonImage="../images/calendar_icon.gif" buttonImageOnly="true"
							buttonText="Select start date." />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<sj:datepicker size="15" id="dateRangeEndDate"
							name="searchVO.dateRangeEndDate"							
							displayFormat="mm/dd/yy" changeYear="true" yearRange="-10:+10"
							placeholder="End date" buttonImage="../images/calendar_icon.gif"
							buttonImageOnly="true" buttonText="Select end date." />
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9">
						<s:submit value="Search" cssClass="btn btn-primary" onclick="openLoading();"  />
						<s:submit value="Clear"	action="impac2/clearSearchPortfolioAccounts" cssClass="btn btn-default" />
					</div>
				</div>
			</fieldset>
		</s:form>
	</div>
	<br />
	<!--  For tab-content -->
</div>

<div style="text-align:right; width: 100%; padding-right: 10px; padding-bottom: 20px;">
	<span style="font-size: 0.9em;">
		<a href="#" onclick="window.open('<s:property value="%{getPropertyValue(@gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants@KEY_ROLES_DOC_LINK)}"/>')">
		IMPAC II User Roles (.pdf) 
		</a>
	</span>
</div>

<s:if test="showResult">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Search Results</h3>
		</div>
		<s:if test="%{portfolioAccounts.list.size > 0}">
			<div align="center" style="overflow: auto;">
				<s:include value="/jsp/content/portfolioAccountSearchResult.jsp?sortAction=%{formAction}" />
			</div>
		</s:if>
		<s:else>
			<div style="text-align:left; width: 100%; padding-left: 10px; padding-top: 10px; padding-bottom:10px;">Nothing found to display.</div>
		</s:else>
	</div>
</s:if>

<div id="loading" align="center" style="display:none;"><img src="../images/loading.gif" alt="Loading" /></div>

<div id="errorDialog" style="display: none;" title="Submit Action">
	<div align="center">
		<br /> <font color='red'>Failed to save data into database,
			please contact system administrator.</font>
	</div>
</div>

<div id="submitNotesAction" style="display: none;" title="Submit Notes">
	<s:include value="/jsp/helper/submitNotesContent.jsp" />
</div>

<script>
	$( window ).ready(function() { 
		onCategoryChage($('#portfolioCategory option:selected').val());
		onOrgChange($('#portfolioOrg option:selected').val());
	});
</script>
