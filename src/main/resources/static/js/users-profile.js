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
            var postId = $(".delete-button").parents(".post-display").attr("data-post-id");
            $.ajax({
            url: "/posts/" + postId,
            type: "DELETE"


            })
            .done(function(){
                //.show()
                $(".post-display[data-post-id=" + postId + "]").remove();
            })
            .fail(function(){
            })
            .always(function(){
                $(".delete-button").removeClass("disabled");
            });
        }

    });

    $(".open-dot-button").click(function(){
        var postDisplay = $(this).parents(".post-display");
        $(postDisplay).find(".edit-button").removeClass("hidden");
        $(postDisplay).find(".delete-button").removeClass("hidden");
        $(this).hide();
        $(postDisplay).find(".close-dot-button").removeClass("hidden");


    });

    $(".close-dot-button").click(function(){
            var postDisplay = $(this).parents(".post-display");
            $(postDisplay).find(".edit-button").addClass("hidden");
            $(postDisplay).find(".delete-button").addClass("hidden");
            $(this).addClass("hidden");
            $(postDisplay).find(".open-dot-button").show();

    });

    $(".edit-button").click(function(){
        var postDisplay = $(this).parents(".post-display");
        $(postDisplay).find(".edit-post-input").removeClass("hidden");
        $(postDisplay).find(".edit-button").hide();
        $(postDisplay).find(".post-save-button").removeClass("hidden");
        $(postDisplay).find(".back-button").removeClass("hidden");
        $(postDisplay).find(".edit-button").hide();
        $(postDisplay).find(".content").hide();
        $(postDisplay).find(".close-dot-button").addClass("hidden");

    });

    $(".back-button").click(function(){
        var postDisplay = $(this).parents(".post-display");
        $(postDisplay).find(".content").show();
        $(postDisplay).find(".edit-button").show();
        $(postDisplay).find(".edit-post-input").addClass("hidden");
        $(postDisplay).find(".post-save-button").addClass("hidden");
        $(postDisplay).find(".back-button").addClass("hidden");
        $(postDisplay).find(".close-dot-button").removeClass("hidden");
        $(".edit-post-input").each(function(){
            $(this).val($(this).attr("data-original-value"));

        });
    });

    $(".post-save-button").click(function(){
        var saveButton = $(this);
        var postDisplay = $(saveButton.parent());
        if(!saveButton.hasClass("disabled")){
            $(saveButton).addClass("disabled");
            var postId = $(postDisplay).attr("data-post-id");
            var content = $(postDisplay).find(".edit-post-input").val();
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
                //specific on postId
                  postDisplay.find(".content").show();
                  postDisplay.find(".edit-button").show();
                  postDisplay.find(".edit-post-input").addClass("hidden");
                  saveButton.addClass("hidden");
                  $(postDisplay).find(".close-dot-button").removeClass("hidden");
                  postDisplay.find(".back-button").addClass("hidden");
                  var timeStamp = postDisplay.find(".content").attr("data-post-timestamp");
                  postDisplay.find(".content").text(content + " " +timeStamp);
                  postDisplay.find(".edit-post-input").val(content);
                  postDisplay.find(".edit-post-input").attr("data-original-value", content);
            })
            .fail(function(){

            })

            .always(function(){
                saveButton.removeClass("disabled");
            });







        }

    });

});