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



function openRole(result){
	$('#roleHelpId').html(result);
	$('#role').dialog("open");
}