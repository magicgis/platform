<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!doctype html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>关注宝宝</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link href="${ctxStatic}css/mui.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="${ctxStatic}css/mui.min.css">
		<link rel="stylesheet" href="${ctxStatic}css/mui.picker.min.css" />
		<style type="text/css">
			* {
				padding: 0px;
				margin: 0px;
			}
			body{ 
				background: #fff; 
				padding-top: 10px;	
			}
			.step {
				background: rgb(241, 241, 241);
				color: rgb(67, 67, 67);
				height: 0;
				line-height: 0;
				padding:0 10px;
			}
			
			.personInfo,.phone-number {
				padding: 0px 10px;
			}
			
			.list-item {
				display: flex;
				align-items: center;
				height: 70px;
			}
			
			.list-align-right {
				line-height: 70px;
			}
			
			.list-align-right button {
			    height: 34px;
			    margin: 10px;
			    background: transparent;
			    color: #bbbbbb;
			    border: 1px solid #bbbbbb;
			    border-radius: 6px;
			    position: absolute;
			    top: 8px;
			    right: 10px;
			    font-size: 12px;
			}
			
			
			/*输入框*/
			input.list-input {
				font-size: 14px;
			    width: 100%;
			    height: 50px;
			    margin: 10px 0px;
			    border: 0px solid transparent;
			    border-radius: 6px;
			    background: #f7f7f7;
			}
			
			div span {
				font-size: 14px;
			}
			#binding{ padding: 15px 10px; }
			
			#binding button {
				background: rgba(68, 181, 73, 0.74);
			    color: white;
			    width: 100%;
			    height: 50px;
			    font-size: 20px;
			    border: 0;
			    border-radius: 6px;
			}
			
			#alertInfo {
				padding: 10px;
				font-size: 14px;
				color: gray;
			}
			.input-right-btn{
				position: relative;
			}
		</style>
	</head>

	<body>
		<form id="infoform" method="post">
			<div class="step">
				<img src="" />
			</div>
	
			<div class="personInfo">
				<div class="list-item">
					<span></span> 
					<%-- <!--<input id="birthday" class="list-input" type="text" placeholder="点击选择宝宝生日" onfocus="(this.type='date')" onblur="(this.type='text')"/>-->
					<input type="text" class="list-input" id="birthday" readonly="" name="birthday" placeholder="点击选择宝宝生日" data-lcalendar="1900-01-01,2088-01-29" value="${info.birth }" /> --%>
					<input type="text" data-options='{"type":"date"}' class="list-input date-box1" name="birthday" id="birthday" placeholder="点击选择宝宝生日" required="required" value="${info.birthday }">
				</div>
				<div class="list-item">
					<span></span> <input class="list-input" type="text" name="name" id="name" value="" placeholder="输入母亲或父亲或儿童姓名" value="${info.name} }"/>
				</div>
			</div>
			<div class="step">
				<img src="" />
			</div>
			<div class="phone-number">
				<div class="list-align-right input-right-btn">
					<span></span><!-- <span style="font-size: 13px; color: gray;">选择手机号码</span> -->
					<select name="phone" id="phone" class="list-input" style="margin:10px 0; width: 100%; height: 50px; background-color: #f7f7f7;"></select>
					<button type="button" id="selectphone">选择手机号</button>
				</div>
				<div class="list-align-right input-right-btn">
					<span></span> <input class="list-input code" id="code" type="text" placeholder="输入验证码" />
					<button type="button" id="sendsms">获取验证码</button>
				</div>
			</div>
			<div id="binding">
				<button type="button" class="submit">完成绑定</button>
			</div>
		</form>
		<div id="alertInfo">
			温馨提示:<br />1、您可以重复以上操作绑定多个宝宝；<br /> 2、若选择的号码已停用或不正确，请尽快联系所属接种单位进行更改。<br/>
			<span style="color: red">注：关注宝宝必须预先在当地疫苗接种地点领取接种证。</span>
			
		</div>
		
		<script src="${ctxStatic}js/zepto.min.js"></script>
		<script src="${ctxStatic}js/mui.min.js"></script>
		<script src="${ctxStatic}js/mui.picker.min.js"></script>
		<script>
		//日期选择
	    (function($) {
			$.init();
			var btns1 = $('.date-box1');
			btns1.each(function(i, btn) {
				btn.addEventListener('tap', function() {
					var optionsJson = this.getAttribute('data-options') || '{}';
					var options = JSON.parse(optionsJson);
					var id = this.getAttribute('id');
					var picker = new $.DtPicker(options);
					picker.show(function(rs) {
						$('#birthday')[0].value = rs.value;
						picker.dispose();
					});
				}, false);
			});
		})(mui);
		</script>
	</body>

<script type="text/javascript">
	$(function(){
		
        //防止页面后退
        history.pushState(null, null, document.URL);
        window.addEventListener('popstate', function () {
            history.pushState(null, null, document.URL);
        });
        
		
	    $("#time").focus(function(){
		    document.activeElement.blur();
		});
		
		$("#selectphone").click(function(){
			if(!$("#birthday").val() || !$("#name").val() ){
				layer.msg("请填写宝宝生日和姓名",{icon:2,time: 800});
				return ;
			}
			$.ajax({
				url:'${ctx}child/choosephone.do',
				data:{'birth':$("#birthday").val(),'name':$("#name").val()},
				type:"post",
				success:function(data){
					if(data.success){
						var html = "";
						for(var i =0;  i <  data.data.length; i ++ ){
							html += '<option>' + data.data[i] + '</option>';
						}
						$("#phone").html(html);
					}else{
						layer.msg(data.msg,{icon: 2,time: 800});
					}
				}
				
			});		
		});
		
		
		 //短信验证码
	    	$("#sendsms").click(function(){
	    		var phone =  $("#phone").val();
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
			var count = 30;
			function disSend() {
				count = 60;
				$("#sendsms").attr("disabled", true);
				$("#sendsms").html(count + "秒后重试");
				countdown = setInterval(CountDown, 1000);
			}

			function CountDown() {
				count--;
				$("#sendsms").html(count + "秒后重试");
				if (count == 0) {
					$("#sendsms").removeAttr("disabled");
					$("#sendsms").html("重新发送");
					clearInterval(countdown);
				}
			}

			//提交表单，验证短信验证码
			$(".submit").click(function() {
				var code = $("#code").val();
				var phone = $("#phone").val();
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
									var act = "${ctx}child/info/atten.do";
									$("#infoform").attr("action", act);
									$("#infoform").submit();
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
			
	});
</script>
</html>