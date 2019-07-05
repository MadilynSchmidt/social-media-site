 $().ready(function(){

    var token = $("meta[name='_csrf']").attr("content");
     var header = $("meta[name='_csrf_header']").attr("content");
     $(document).ajaxSend(function(e, xhr, options){
         xhr.setRequestHeader(header, token);
     });



    $("#save-button").click(function(){
        $("input").each(function(){
            $(this).val($(this).val().trim());
        });
    });

    $(".delete-button").click(function(){
        if(!$(".delete-button").hasClass("disabled")){
            $(".delete-button").addClass("disabled");
            var postId = $(".delete-button").parent().attr("data-post-id");
            $.ajax({
            url: "/posts/" + postId,
            type: "DELETE"


            })
            .done(function(){
                //.show()
                $(".delete-button").parent().hide();
            })
            .fail(function(){
            })
            .always(function(){
                $(".delete-button").removeClass("disabled");
            });
        }

    });

});