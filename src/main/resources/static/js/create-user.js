
$().ready(function(){
    $("#create-user-form").validate({
        rules:{

            password:{
                required: true,
                minlength: 8
            },
            confirm_password:{
                equalTo: "#password"
            }



        },
        messages:{

            password: {
                required: "Please enter a password",
                minlength: "Your password must consist of at least eight characters"
            },

            confirm_password:{
                equalTo: "Your passwords do not match"
            }



        }


    });

    $("#submit-button").click(function(){
        $("input").each(function(){
            $(this).val($(this).val().trim());
        });
    });

});
