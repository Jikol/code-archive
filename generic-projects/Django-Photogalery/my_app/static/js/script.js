$('.login-popover').popover({
    html: true,
    animation: true,
    sanitize: false,
    content: function () {
        return $("#login-form-body").html();
    }
});

$(document).on('click', function(e) {
  $('.login-popover').each(function() {
    if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
      $(this).popover('hide').data('bs.popover').inState.click = false
    }
  });
});

$('.toast').toast('show');

function image_crop() {
    let imgs = $(".table-image");
    for (let item of imgs) {
        let img = document.getElementById(item.id);
        if (img.width >= img.height){
            let id = img.id;
            $('#' + id).css({
                "height": "100%",
                "width:": "auto"
            });
            $('#' + id).mouseenter(function () {
                $('#' + id).css({
                    "transform": "translate(-55%,-60%)",
                    "left": "60%",
                    "top": "60%",
                    "height": "110%"
                });
            });
            $('#' + id).mouseleave(function () {
                $('#' + id).css({
                    "transform": "translate(-50%,-50%)",
                    "left": "50%",
                    "top": "50%",
                    "height": "100%"
                });
            });
        } else {
            let id = img.id;
            console.log(id);
            $('#' + id).css({
                "height": "auto",
                "width": "100%"
            });
            $('#' + id).mouseenter(function () {
                $('#' + id).css({
                    "transform": "translate(-60%,-60%)",
                    "left": "60%",
                    "top": "60%",
                    "width": "110%"
                });
            });
            $('#' + id).mouseleave(function () {
                $('#' + id).css({
                    "transform": "translate(-50%,-50%)",
                    "left": "50%",
                    "top": "50%",
                    "width": "100%"
                });
            });
        }
    }
}

image_crop();

$('.register-login-a').click(function() {
    $('.login-popover').click();
    return false;
});

function on_load() {
    if (!$('.account-email').val()) {
        $('.account-change-card .id_email').css({"bottom": "38px", "left": "10px"});
    } else {
        $('.account-change-card .id_email').css({"bottom": "72px", "left": "5px"});
    }

    if (!$('.account-name').val()) {
        $('.account-change-card .id_name').css({"bottom": "38px", "left": "10px"});
    } else {
        $('.account-change-card .id_name').css({"bottom": "72px", "left": "5px"});
    }

    if (!$('.account-surname').val()) {
        $('.account-change-card .id_surname').css({"bottom": "38px", "left": "10px"});
    } else {
        $('.account-change-card .id_surname').css({"bottom": "72px", "left": "5px"});
    }
}

on_load();

$('.old-password').blur(function() {
    if (!$(this).val()) {
        $('.id_old_password').css({"bottom": "31px", "left": "10px"});
    } else {
        $('.id_old_password').css({"bottom": "65px", "left": "5px"});
    }
});

$('.old-password').focusin(function() {
    $('.id_old_password').css({"bottom": "65px", "left": "5px"});
});

$('.new-password').blur(function() {
    if (!$(this).val()) {
        $('.id_new_password1').css({"bottom": "31px", "left": "10px"});
    } else {
        $('.id_new_password1').css({"bottom": "65px", "left": "5px"});
    }
});

$('.new-password').focusin(function() {
    $('.id_new_password1').css({"bottom": "65px", "left": "5px"});
});


$('.new-password2').blur(function() {
    if (!$(this).val()) {
        $('.id_new_password2').css({"bottom": "31px", "left": "10px"});
    } else {
        $('.id_new_password2').css({"bottom": "65px", "left": "5px"});
    }
});

$('.new-password2').focusin(function() {
    $('.id_new_password2').css({"bottom": "65px", "left": "5px"});
});

$('.account-email').blur(function() {
    if (!$(this).val()) {
        $('.id_email').css({"bottom": "38px", "left": "10px"});
    } else {
        $('.id_email').css({"bottom": "72px", "left": "5px"});
    }
});

$('.account-email').focusin(function() {
    $('.id_email').css({"bottom": "72px", "left": "5px"});
});

$('.account-name').blur(function() {
    if (!$(this).val()) {
        $('.id_name').css({"bottom": "38px", "left": "10px"});
    } else {
        $('.id_name').css({"bottom": "72px", "left": "5px"});
    }
});

$('.account-name').focusin(function() {
    $('.id_name').css({"bottom": "72px", "left": "5px"});
});

$('.account-surname').blur(function() {
    if (!$(this).val()) {
        $('.id_surname').css({"bottom": "38px", "left": "10px"});
    } else {
        $('.id_surname').css({"bottom": "72px", "left": "5px"});
    }
});

$('.account-surname').focusin(function() {
    $('.id_surname').css({"bottom": "72px", "left": "5px"});
});

$('.register-email').blur(function() {
    if (!$(this).val()) {
        $('.id_email').css({"bottom": "31px", "left": "10px"});
    } else {
        $('.id_email').css({"bottom": "65px", "left": "5px"});
    }
});

$('.register-email').focusin(function() {
    $('.id_email').css({"bottom": "65px", "left": "5px"});
});

$('.register-password1').blur(function() {
    if (!$(this).val()) {
        $('.id_password1').css({"bottom": "31px", "left": "10px"});
    } else {
        $('.id_pasword1').css({"bottom": "65px", "left": "5px"});
    }
});

$('.register-password1').focusin(function() {
    $('.id_password1').css({"bottom": "65px", "left": "5px"});
});

$('.register-password2').blur(function() {
    if (!$(this).val()) {
        $('.id_password2').css({"bottom": "31px", "left": "10px"});
    } else {
        $('.id_pasword2').css({"bottom": "65px", "left": "5px"});
    }
});

$('.register-password2').focusin(function() {
    $('.id_password2').css({"bottom": "65px", "left": "5px"});
});

$('.register-name').blur(function() {
    if (!$(this).val()) {
        $('.id_name').css({"bottom": "31px", "left": "10px"});
    } else {
        $('.id_name').css({"bottom": "65px", "left": "5px"});
    }
});

$('.register-name').focusin(function() {
    $('.id_name').css({"bottom": "65px", "left": "5px"});
});

$('.register-surname').blur(function() {
    if (!$(this).val()) {
        $('.id_surname').css({"bottom": "31px", "left": "10px"});
    } else {
        $('.id_surname').css({"bottom": "65px", "left": "5px"});
    }
});

$('.register-surname').focusin(function() {
    $('.id_surname').css({"bottom": "65px", "left": "5px"});
});

