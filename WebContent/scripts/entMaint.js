function onCategoryChage(category){
	if(category == '22' || category == '25'){
		$('#dateRangeStartDate').datepicker('disable'); 
		$('#dateRangeEndDate').datepicker('disable'); 
		$('#dateRangeStartDate').val(null);
		$('#dateRangeEndDate').val(null);
	}
	else{
		$('#dateRangeStartDate').datepicker('enable'); 
		$('#dateRangeEndDate').datepicker('enable'); 
	}		
}