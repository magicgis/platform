<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<style type="text/css">
		.error{
			border-color: red !important;
		}
	</style>
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	<link rel="stylesheet" href="${ctxStatic}css/lCalendar.css">
	<title>关注宝宝</title>
</head>
<body>
	<div class="wrap">
		<form method="post" id="infoform">
			<div class="box">
				<label for="childcode">儿童编码</label>
				<input type="text" placeholder="请填写儿童编码" name="childcode" required="required">
			</div>
			<div class="box">
				<label for="childname">儿童姓名</label>
				<input type="text" placeholder="请填写儿童姓名" name="childname" required="required">
			</div>
<!-- 			<div class="box">
				<label for="" class="fl mr10">性别</label>
				<div class="sex fl">
					<label for="man" class="fl">
						<input type="radio" name="sex" id="man" checked="checked" class="s">
						<i></i>
					</label>
					<span class="fl ml10">男</span>
				</div>
				<div class="sex fl">
					<label for="wom" class="fl">
						<input type="radio" name="sex" id="wom" class="s">
						<i></i>
					</label>
					<span class="fl ml10">女</span>
				</div>
			</div> -->
			<!-- <div class="box mt10">
				<label for="">出生日期</label>
				<input id="time" type="text" readonly="" name="input_date" placeholder="请选择出生日期"/>
			</div> -->
			<div class="box">
				<label for="guardianname">监护人姓名</label>
				<input type="text" name="guardianname" placeholder="请填写监护人姓名" required="required">
			</div>
			<div class="box">
				<label for="guardianmobile">监护人手机号码</label>
				<div>
					<input type="text" name="guardianmobile" placeholder="请填写监护人手机号码" >
					<input type="text" name="smscode" style="width: 60%" placeholder="验证码">
					<button class="sendsms" type="button" >短信验证</button>
				</div>
				
			</div>
			<div class="box">
				<button class="submit" type="button">保 存</button>
				<button type="submit" id="sub" style="display: none"></button>
			</div>
		</form>
	</div>
</body>
<script src="${ctxStatic}js/lCalendar.min.js"></script>
<script>
	//日期选择
/*     var calendar = new lCalendar();
    calendar.init({
        'trigger': '#time',
        'type': 'date'
    }); */
    
    //短信验证码
    $(function(){
    	$(".sendsms").click(function(){
    		var phone =  $("input[name=guardianmobile]").val();
    		if(phone){
     			if (phone.match(/^((1[0-9]{2})+\d{8})$/)) {
     				disSend();
     				$.ajax({
	    				url:"${ctx}sms/smscheckcode/" + phone + ".do",
	    				success:function(data){
	    					if(data){
               					layer.msg("发送成功",{icon: 1,time: 800});
	    					}else{
	    						layer.msg("发送失败,请重新发送",{icon:5,time: 800});
	    					}
	    				},
	    			}); 
    			}else{
    				layer.msg("手机号码格式不正确！请重新输入！",{icon: 0,time: 800});
    			} 
    			
    			
    		}else{
    			layer.msg("手机号码不能为空",{icon: 2,time: 800});
    		}
    	});
    	
    	

		//按钮倒计时
		var countdown;
		var count;
		function disSend() {
			count = 89;
			countdown = setInterval(CountDown, 1000);
		}

		function CountDown() {
			$(".sendsms").attr("disabled", true);
			count--;
			$(".sendsms").html(count + "秒后重试");
			if (count == 0) {
				$(".sendsms").removeAttr("disabled");
				$(".sendsms").html("重新发送");
				clearInterval(countdown);
				
			}
		}

		//提交表单，验证短信验证码
		$(".submit").click(function() {
			var code = $("input[name=smscode]").val();
			var phone = $("input[name=guardianmobile]").val();
			if (phone) {
				if (code) {
					//验证短信
					$.ajax({
						url : "${ctx}sms/checksms.do",
						data : {
							"phone" : phone,
							"code" : code
						},
						success : function(data) {
							if (data) {
								//layer.msg("验证成功",{icon: 1,time: 800});
								//提交表单
								//验证儿童编号是否合法
								if($("input[name=childcode]").hasClass("error")){
									layer.msg("该儿童编号还未成功建档", {icon : 2,time : 800});
									return;
								}
								if(!$("input[name=childcode]").val().match(/^[0-9]{18}$/)){
									layer.msg("输入儿童编号不正确", {icon : 2,time : 800});
									return;
								}
								var act = "${ctx}child/info/save.do";
								$("#infoform").attr("action", act);
								//$("#sub").click();
								$("#infoform").submit();
								//layer.load();
							} else {
								layer.msg("验证码错误", {icon : 2,time : 800});
								$("input[name=smscode]").val("");
								return;
							}

						},
						error : function() {

						}
					});

				} else {
					layer.msg("请输入短信验证码", {icon : 2,time : 800});
				}
			} else {
				layer.msg("手机号码不能为空", {icon : 2,time : 800});
			}
		});
		
		
		//验证code 是否存在
		$("input[name=childcode]").keyup(function(){
			var co = $(this).val(); 
			if(co.match(/^[0-9]{18}$/)){
				$.ajax({
					url:"${ctx}child/checkchild/" + co + ".do",
					success:function(data){
						if(!data){
							layer.msg("该儿童编号还未成功建档", {icon : 2,time : 800});
							$("input[name=childcode]").addClass("error");
						}else{
							$("input[name=childcode]").removeClass("error");
						}
					}
				});
			}
		});
	})
</script>
</html>
