 $().ready(function(){



 $("#save-button").click(function(){
        $("input").each(function(){
            $(this).val($(this).val().trim());
        });
    });

    });