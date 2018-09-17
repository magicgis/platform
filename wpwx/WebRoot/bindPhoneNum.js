
function showMsg(text) {
    $('.weui_dialog_alert .weui_dialog_bd').text(text);
    $('.weui_dialog_alert').show();
}
$(".weui_btn_dialog").on('click', function () {
    $('.weui_dialog_alert').hide();
});
$("#goIndex").on("click", function () {
    location.href = rootPath;
});

/* 获取验证码 */
$(".getCord").on("click", function () {
    var mobile = $('input[name=mobile]').val();
    if (!mobile) {
        showMsg("请填写手机号");
        return;
    } else if (!RegExps.Mobile.test(mobile)) {
        showMsg("手机号格式有误");
        return;
    }

    var data = { smsType: 0, mobile: mobile, operateType: 2 };

    //变灰, 准备倒计时, 防止重新点击发送重复的请求
    var sec = 120;
    var $getCode = $('.getCord2');
    $('.getCord').css("display", "none");
    $getCode.css("display", "block");

    Zepto.post(rootPath + "/home/sms.do", { smsType: 0, mobile: mobile, operateType: 2 }, function (data) {
        console.log(data);
        if (data.success) {
            if (data.status == 9) {
                showMsg("手机号为空，未发送短信");
            } else if (data.status == 10) {
                showMsg("发送短信失败");
            } else if (data.status == 11) {
                //开始倒计时
                var itv = setInterval(function () {
                    $getCode.text(sec);
                    if (sec == 0) {
                        clearInterval(itv);
                        $getCode.css("display", "none");
                        $(".getCord").css("display", "block");
                    } else {
                        sec--;
                    }
                }, 1000);
            }
        } else {
            showMsg(data.msg);
        }
    }, 'json');



});

/*绑定手机号 */
var isSubmitting = false;
$("#submit").on("click", function () {
    submitIt();
});

function submitIt() {
    if (isSubmitting == true) {
        showMsg("正在提交中, 请不要重复提交");
        return;
    }

    var mobile = $('input[name=mobile]').val();
    var password = $('input[name=password]').val();

    if (!mobile) {
        showMsg("请填写手机号");
        return;
    } else if (!RegExps.Mobile.test(mobile)) {
        showMsg("手机号格式有误");
        return;
    } else if (!password) {
        showMsg("请填写验证码");
        return;
    }

    Zepto.post(rootPath + "/home/sms.do", { smsType: 1, mobile: mobile, code: password, operateType: 2 }, function (data) {
        console.log(data);
        isSubmitting = false;
        if (data.success) {
            if (data.status == 9) {
                showMsg("手机号为空，未发送短信");
            } else if (data.status == 12) {
                console.log("匹配成功");
                isSubmitting = true;
                Zepto.post(rootPath + "/member/bindMobile.do", {
                    marketType: marketType,
                    mobile: mobile,
                    onlineUser: onlineUser,
                    portalId: portalId
                }, function (data) {
                    isSubmitting = false;
                    if (data.success) {
                        if (data.status == 100) {
                            window.location.href = rootPath;
                        }
                        if (data.status == 101) {
                            showMsg("绑定手机号已存在");
                        }
                    } else {
                        showMsg(data.msg);
                    }
                }, 'json');
            } else if (data.status == 13) {
                showMsg("短信验证码匹配失败");
            } else if (data.status == 14) {
                showMsg("短信验证码失效");
            } else if (data.status == 15) {
                showMsg("服务器没有该手机号的验证码信息");
            }
        } else {
            showMsg(data.msg);
        }
    }, 'json');
}