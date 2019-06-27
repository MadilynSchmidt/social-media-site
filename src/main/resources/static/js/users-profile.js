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
                $(".post-display[data-post-id=" + postId + "]").remove();
            })
            .fail(function(){
            })
            .always(function(){
                $(".delete-button").removeClass("disabled");
            });
        }

    });

    $(".edit-button").click(function(){
        var postDisplay = $(this).parent(".post-display");
        $(postDisplay).children(".edit-post-input").removeClass("hidden");
        $(postDisplay).children(".edit-button").hide();
        $(postDisplay).children(".post-save-button").removeClass("hidden");
        $(postDisplay).children(".back-button").removeClass("hidden");
        $(postDisplay).children(".edit-button").hide();
        $(postDisplay).children(".content").hide();

    });

    $(".back-button").click(function(){
        //var postDisplay = $(this).parent(".post-display");
        $(".content").show();
        $(".edit-button").show();
        $(".edit-post-input").addClass("hidden");
        $(".post-save-button").addClass("hidden");
        $(".back-button").addClass("hidden");
        //$(postDisplay).children(".edit-post-input").val($(".edit-post-input").attr("data-original-value"));
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
            var content = $(postDisplay).children(".edit-post-input").val();
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
                  postDisplay.children(".content").show();
                  postDisplay.children(".edit-button").show();
                  postDisplay.children(".edit-post-input").addClass("hidden");
                  saveButton.addClass("hidden");
                  postDisplay.children(".back-button").addClass("hidden");
                  var timeStamp = postDisplay.children(".content").attr("data-post-timestamp");
                  postDisplay.children(".content").text(content + timestamp);
                  postDisplay.children(".edit-post-input").val(content);
                  postDisplay.children(".edit-post-input").attr("data-original-value", content);
            })
            .fail(function(){

            })

            .always(function(){
                saveButton.removeClass("disabled");
            });







        }

    });

});