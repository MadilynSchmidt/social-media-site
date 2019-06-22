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

    $(".edit-button").click(function(){
        $(".edit-post-input").removeClass("hidden");
        $(".edit-button").hide();
        $(".post-save-button").removeClass("hidden");
        $(".back-button").removeClass("hidden");
        $(".edit-button").hide();
        $(".content").hide();

    });

    $(".back-button").click(function(){
        $(".content").show();
        $(".edit-button").show();
        $(".edit-post-input").addClass("hidden");
        $(".post-save-button").addClass("hidden");
        $(".back-button").addClass("hidden");
        $(".edit-post-input").val($(".edit-post-input").attr("data-original-value"));

    });

    $(".post-save-button").click(function(){
        if(!$(".post-save-button").hasClass("disabled")){
            $(".post-save-button").addClass("disabled");
            var postId = $(".post-save-button").parent().attr("data-post-id");
            var content = $(".edit-post-input").val();
            var payload = {
                content: content,
                postId: postId
            };

            $.ajax({
            url: "/posts/" + postId,
            type: "PATCH",
            data: JSON.stringify(payload),
            contentType: "application/json"
            })

            .done(function(){
                  $(".content").show();
                  $(".edit-button").show();
                  $(".edit-post-input").addClass("hidden");
                  $(".post-save-button").addClass("hidden");
                  $(".back-button").addClass("hidden");
                  $(".content").text(content);
                  $(".edit-post-input").val(content);
                  $(".edit-post-input").attr("data-original-value", content);
            })
            .fail(function(){

            })

            .always(function(){
                $(".post-save-button").removeClass("disabled");
            });







        }

    });

});