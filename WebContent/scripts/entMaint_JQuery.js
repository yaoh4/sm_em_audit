$(function() {
	$("#help").dialog({
		 autoOpen: false,
		 resizable: false,
		 width: 400,
		 height:300,
		 modal: true,
		 show: { effect: "slide", duration: 1000 },
		 hide: { effect: "slide", duration: 1000 },
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
		 show: { effect: "slide", duration: 1000 },
		 hide: { effect: "slide", duration: 1000 },
		 buttons: {
		 		OK: function() {
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
	
	$.ajax({
			url: "roleDescriptionAction.action",
			type: "get",
			data: {rId: id},
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