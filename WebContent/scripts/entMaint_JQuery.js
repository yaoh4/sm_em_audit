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
});
function openHelp(id){
	$('#helpDiv').html($('#' + id).val());
	$('#help').dialog("open");
}
	