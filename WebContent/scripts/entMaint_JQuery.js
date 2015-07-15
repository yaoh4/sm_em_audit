$(function() {
	$("#help").dialog({
		 autoOpen: false,
		 resizable: false,
		 width: 400,
		 height:300,
		 modal: true,
		 show: { effect: "slide", duration: 250 },
		 hide: { effect: "slide", duration: 250 },
		 buttons: {
		 		OK: function() {
		 			$( this ).dialog( "close" ); 
		 		}
		 }
	});
	
	$("#role").dialog({
		 autoOpen: false,
		 resizable: false,
		 width: 400,
		 height:300,
		 modal: true,
		 show: { effect: "slide", duration: 250 },
		 hide: { effect: "slide", duration: 250 },
		 buttons: {
		 		OK: function() {
		 			$( this ).dialog( "close" ); 
		 		}
		 }
	});
	
	$("#dateRangeHelp").dialog({
		 autoOpen: false,
		 resizable: false,
		 width: 400,
		 height:300,
		 modal: true,
		 show: { effect: "slide", duration: 250 },
		 hide: { effect: "slide", duration: 250 },
		 buttons: {
			 OK: function() {
	 			$( this ).dialog( "close" ); 
	 		 }}
	});
	$("#eraua_na").dialog({
		 autoOpen: false,
		 resizable: false,
		 width: 400,
		 height:300,
		 modal: true,
		 show: { effect: "slide", duration: 250 },
		 hide: { effect: "slide", duration: 250 },
		 buttons: {
		 		OK: function() {
		 			$( this ).dialog( "close" ); 
		 		}
		 }
	});
	$("#auditHistory").dialog({
		 autoOpen: false,
		 resizable: false,
		 width: 900,
		 height:500,
		 modal: true,
		 show: { effect: "slide", duration: 250 },
		 hide: { effect: "slide", duration: 250 },
		 buttons: {
		 		OK: function() {
		 			$( this ).dialog( "close" ); 
		 		}
		 }
	});
	$("#note").dialog({
		 autoOpen: false,
		 resizable: false,
		 width: 400,
		 height:300,
		 modal: true,
		 show: { effect: "slide", duration: 250 },
		 hide: { effect: "slide", duration: 250 },
		 buttons: {
		 		OK: function() {
		 			$( this ).dialog( "close" ); 
		 		}
		 }
	});
	
	$("#confirmation").dialog({
		 autoOpen: false,
		 resizable: false,
		 width: 400,
		 height:200,
		 modal: true,
		 show: { effect: "slide", duration: 250 },
		 hide: { effect: "slide", duration: 250 },
		 buttons: {
		 		OK: function() {
		 			submitReset();
		 			$( this ).dialog( "close" ); 
		 		},
		 		Cancel: function(){
		 			$( this ).dialog( "close" );
		 		}
		 }
	});
	
	$("#errorDialog").dialog({
		 autoOpen: false,
		 resizable: false,
		 width: 600,
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
	
	$("#loading").dialog({
		 autoOpen: false,
		 resizable: false,
		 width: 300,
		 height:150,
		 modal: true,
		 show: { effect: "", duration: 1 },
		 hide: { effect: "", duration: 1 },
	}).siblings('div.ui-dialog-titlebar').remove();
	
   });


function openHelp(id){
	$('#helpDiv').html($('#' + id).val());
	$('#help').dialog("open");
}

function getRoleDescription(id){
	var result = "";
	var category = $('#categoryId').val();
	
	$.ajax({
			url: "roleDescriptionAction.action",
			type: "post",
			data: {rId: id, cate:category},
			async:   false,
			success: function(msg){
				result = $.trim(msg);
			}, 
			error: function(){}		
		});
	openRole(result);
}	

function getI2eRoleDescription(id){
	var result = "";
	var category = $('#categoryId').val();
	
	$.ajax({
			url: "i2eRoleDescriptionAction.action",
			type: "post",
			data: {rId: id, cate:category},
			async:   false,
			success: function(msg){
				result = $.trim(msg);
			}, 
			error: function(){}		
		});
	openRole(result);
}	

function openErrorDialog(){
	$('#errorDialog').dialog("open");
}

function openLoading(){
	
	$('#loading').dialog("open");
}

function openConfirmation(){
	
	$('#confirmation').dialog("open");
}

function openRole(result){
	$('#roleHelpId').html(result);
	$('#role').dialog("open");
}

function onOrgChange(org){
	if(org == 'all'){
		$("#excludeNciCheck").removeAttr("disabled");
	}
	else{
		$("#excludeNciCheck").removeAttr('checked');
		$("#excludeNciCheck").attr("disabled","disabled");
	}		
	
}

function onCategoryChage(category){
	if(category == '22' || category == '25'){
		$('#dateRangeStartDate').datepicker('disable'); 		
		$('#dateRangeEndDate').datepicker('disable'); 
		$('#dateRangeStartDate').val(''); 
		$('#dateRangeEndDate').val(''); 		
	}
	else{		
		$('#dateRangeStartDate').datepicker('enable'); 
		$('#dateRangeEndDate').datepicker('enable'); 
		if($('#dateRangeEndDate').val == null) {
			$('#dateRangeEndDate').datepicker('setDate', new Date());
		}
	}
	$.ajax({
		url: "orgOptionAction.action",
		type: "post",
		data: {cate: category},
		async:   false,
		success: function(msg){
			result = $.trim(msg);
			$('#orgListId').html(result);
		}, 
		error: function(){}		
	});
}

function getNote(id, category){
	var result = "";
	$.ajax({
		url: "getNoteAction.action",
		type: "post",
		data: {pId: id, cate:category},
		async:   false,
		success: function(msg){
			result = $.trim(msg);
			
		}, 
		error: function(){}		
	});
	
	return result;
}

function fetchAuditNote(id, category){
	$.ajax({
			url: "getNoteAction.action",
			type: "post",
			data: {pId: id, cate:category},
			async:   false,
			success: function(msg){
				result = $.trim(msg);
				$('#noteId').html(result);
				openNote();
			}, 
			error: function(){}		
		});
}
function getPortfolioNote(id){
	var result = "";
	$.ajax({
		url: "getPortFolioNoteAction.action",
		type: "post",
		data: {pId: id},
		async:   false,
		success: function(msg){
			result = $.trim(msg);
			
		}, 
		error: function(){}		
	});
	
	return result;
}

function fetchPortfolioNote(id){
	$.ajax({
			url: "getPortFolioNoteAction.action",
			type: "post",
			data: {pId: id},
			async:   false,
			success: function(msg){
				result = $.trim(msg);
				//$('#noteId').html(result);
				openPortfolioNote();
			}, 
			error: function(){}		
		});
}
function openNote(){
	$('#note').dialog("open");
}

function moveToAnchor(){
	$(document).scrollTop( $("#anchor").offset().top );  
}

function openHistory(){
	$('#auditHistory').dialog("open");
}

function openEraua(){
	$('#eraua_na').dialog("open");
}
function getDateRangeHelp(){	
	$('#dateRangeHelp').dialog("open");
}

function toggle(thisname) {
   tr=document.getElementsByTagName('tr');
   for (var i=0;i<tr.length;i++){
	    if (tr[i].getAttribute(thisname)){
	    if ( tr[i].style.display=='none' ){
	       tr[i].style.display = '';
	    }else {
	       tr[i].style.display = 'none';
	    }
   }
  }
}

function toggleOther(thisname){
		tr=document.getElementsByTagName('tr')
		for (var i=0;i<tr.length;i++){
		  if (tr[i].getAttribute(thisname)){
		   if ( tr[i].style.display=='none' ){
		     tr[i].style.display = '';
		     $('#otherAnchor').html("<strong><a href=\"javascript:toggleOther('nameit');\"><img src='../images/CriteriaOpen.gif' alt='Plus'></a>OTHER</strong>");
		   } else {
		     tr[i].style.display = 'none';
		     $('#otherAnchor').html("<strong><a href=\"javascript:toggleOther('nameit');\"><img src='../images/CriteriaClosed.gif' alt='Minus'></a>OTHER</strong>");	
		   }
		  }
		}
}

function searchAuditByCategory(cate, org){
	var url = "searchAudit?cate="+cate+"&orgName=" + org + "&dashboard=Y";
	var winName = "searchAudit";
	var features = "menubar=yes,scrollbars=yes,resizable=yes,fullscreen=yes";

	var newWin = window.open(url, winName, features);
}
function refresh(){
	$('#dashboardFormId').attr("action", "gotoDashboard");
	$('#dashboardFormId').submit();
}