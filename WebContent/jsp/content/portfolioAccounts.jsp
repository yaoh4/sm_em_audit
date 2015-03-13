<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<s:include value="/jsp/content/manageAccounts.jsp" />
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js"	type="text/javascript"></script>
<link rel="stylesheet" type="text/css"	href="../stylesheets/jquery-ui-1.11.3.css" />
<script>
	$(function() {
		$("#submitNotesAction").dialog({
			autoOpen : false,
			resizable : false,
			width : 600,
			height : 400,
			modal : true,
			show : {
				effect : "slide",
				duration : 1000
			},
			hide : {
				effect : "slide",
				duration : 1000
			},
			buttons : {
				OK : function() {
					var result = "";
					var ipac2Id = $('#cellId').val();
					var name = $('#nameId').val();
					var notes = $('#noteText').val();

					$.ajax({
						url : "saveNotes.action",
						type : "get",
						data : {
							ipac2Id : ipac2Id,
							name : name,
							notes : notes
						},
						async : false,
						success : function(msg) {
							result = $.trim(msg);
						},
						error : function() {
						}
					});

					if (result == 'fail') {
						$(this).dialog("close");
						openErrorDialog();
					} else {
						$("#notesDiv_" + ipac2Id).html(result);
						$(this).dialog("close");
					}
				},
				Cancel : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#errorDialog").dialog({
			autoOpen : false,
			resizable : false,
			width : 600,
			height : 200,
			modal : true,
			show : {
				effect : "slide",
				duration : 1000
			},
			hide : {
				effect : "slide",
				duration : 1000
			},
			buttons : {
				OK : function() {
					$(this).dialog("close");
				}
			}
		});
	});
	function submitNotes(name, cellId) {
		$('#nameId').val(name);
		$('#nameValue').html("<label>" + name + "</label>");
		$('#cellId').val(cellId);
		$('messageId').html("");
		$('#noteText').html($('#notesDiv_'+cellId).text());
		$("#submitNotesAction").dialog("open");
	}
	function openErrorDialog() {
		$('#errorDialog').dialog("open");
	}
</script>

<div class="tab-content">
	<div class="tab-pane fade active in" id="par1">
		<s:form action="%{formAction}" cssClass="form-horizontal">
			<fieldset style="padding: 15px 0;">
				<div class="form-group">
					<label class="control-label col-sm-3" for="f-name">IMPAC II
						User First Name:</label>
					<div class="col-sm-9">
						<s:textfield name="searchVO.userFirstname" cssClass="form-control"
							value="%{#session.searchVO.userFirstname}" id="f-name" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-3" for="l-name">IMPAC II
						User Last Name:</label>
					<div class="col-sm-9">
						<s:textfield name="searchVO.userLastname" cssClass="form-control"
							value="%{#session.searchVO.userLastname}" id="l-name" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-3">NCI Organization:</label>
					<div class="col-sm-9">
						<s:select name="searchVO.organization" cssClass="form-control"
							value="%{#session.searchVO.organization}" list="organizationList"
							listKey="optionKey" listValue="optionValue" headerKey="all"
							headerValue="All" style="width:460px;" />
					</div>
				</div>
				<div class="form-group" style="margin-top: -10px;">
					<label class="control-label col-sm-3"> </label>
					<div class="col-sm-9">
						<s:checkbox name="searchVO.excludeNCIOrgs" value="Y" />
						<label style="font-weight: normal; font-size: 0.9em;">Exclude
							NCI Orgs with IC Coordinators</label>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-3">Category:</label>
					<div class="col-sm-9">
						<s:select name="searchVO.category" id="portfolioCategory"
							onchange="onCategoryChage(this.value);" cssClass="form-control"
							value="%{#session.searchVO.category}" list="categoriesList"
							listKey="optionKey" listValue="optionValue" style="width:460px;" />
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-sm-3">Date Range:</label>
					<div class="col-sm-9">
						<sj:datepicker size="15" id="dateRangeStartDate"
							name="searchVO.dateRangeStartDate"
							value="%{#session.searchVO.dateRangeStartDate}"
							displayFormat="mm/dd/yy" changeYear="true" yearRange="-10:+10"
							placeholder="Start date"
							buttonImage="../images/calendar_icon.gif" buttonImageOnly="true"
							buttonText="Select start date." disabled="true" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<sj:datepicker size="15" id="dateRangeEndDate"
							name="searchVO.dateRangeEndDate"
							value="%{#session.searchVO.dateRangeEndDate}"
							displayFormat="mm/dd/yy" changeYear="true" yearRange="-10:+10"
							placeholder="End date" buttonImage="../images/calendar_icon.gif"
							buttonImageOnly="true" buttonText="Select end date."
							disabled="true" />
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9">
						<s:submit value="Search" cssClass="btn btn-primary" />
						<s:submit value="Clear"
							action="impac2/clearSearchPortfolioAccounts"
							cssClass="btn btn-default" />
					</div>
				</div>
			</fieldset>
		</s:form>
	</div>
	<br />
	<!--  For tab-content -->
</div>

<div style="text-align:right; width: 100%; padding-right: 10px; padding-bottom: 20px;">
    <span style="font-size: 0.9em;"><a href="http://inside.era.nih.gov/files/Roles_Description_Report.pdf" target="blank">IMPAC II User Roles (.pdf) </a></span>
</div> 

<s:if test="showResult">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Search Results</h3>
		</div>

		<div align="center" style="overflow: auto;">
			<s:include value="/jsp/content/portfolioAccountSearchResult.jsp?sortAction=%{formAction}"/>
		</div>

	</div>
</s:if>



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
	});
</script>
