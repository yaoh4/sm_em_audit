
$( window ).load(function() {

    $( "#menu-changeUser" ).click(function() {
        $("#input-changeUser").val('');
    });

    $( "#input-changeUser" ).autocomplete({
        source: function( request, response ) {
            $.ajax({
                url: "searchPerson.action",
                dataType: "json",
                type: "post",
                data: {
                    'searchCriteria.term': request.term,
                },
                success: function( data ) {
                    response( data.result );
                },
                error: function(xhr, error, thrown) {
                    if (error == "parsererror") {
                        response([]);
                    }
                }
            });
        },
        minLength: 2,
        delay: 100,
        select: function( event, ui ) {
            event.preventDefault();
            var url = "changeUser.action" + "?user=" + ui.item.nihNetworkId;
            window.location.replace(url);
        },
        focus: function(event, ui) {
            event.preventDefault();
        }
    });
});
