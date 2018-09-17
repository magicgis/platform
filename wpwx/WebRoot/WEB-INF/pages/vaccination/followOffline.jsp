<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	<link rel="stylesheet" href="${ctxStatic}css/lCalendar.css">
	<link rel="stylesheet" href="${ctxStatic}css/mui.min.css">
	<link rel="stylesheet" href="${ctxStatic}css/mui.picker.min.css" />
	<title>快捷关注宝宝</title>
	<style type="text/css">
		.error{
			border-color: red !important;
			color: red;
		}
		input ,select{
			padding:0 15px;
		}
	</style>
</head>
<body>
	<div class="wrap">
		<form id="self-form" action="">
			<input type="hidden" id="id" value="${baseinfo.id}">
			<div class="box">
				<label for="">宝宝姓名<span class="red">*</span></label>
				<input type="text" id="childname" name="childname" readonly="readonly" value="${baseinfo.childname }">
			</div>
		
			<div class="box mt10">
				<label for="">宝宝生日<span class="red">*</span></label>
				<input type="text" data-options='{"type":"date"}' class="date-box1 dateNotNull" id="birthday" name="birthday" id='result1' placeholder="请选择宝宝生日" >
			</div>
			<div class="box">
				<button class="submit" type="button" id="sub">提 交</button>				
			</div>
		</form>
	</div>
</body>
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
				var myDate = new Date(); 
				options.endYear = myDate.getFullYear();
				options.beginYear = myDate.getFullYear() - 10;
				var id = this.getAttribute('id');
				var picker = new $.DtPicker(options);
				picker.show(function(rs) {
					btn.value = rs.value;
					picker.dispose();
				});
			}, false);
		});
	})(mui);
    $("#time").focus(function(){
	    document.activeElement.blur();
	});
</script>
<script>

	$(function(){
		$("#sub").click(function(){
			if($("#birthday").val()){
				$.ajax({
					url:"${ctx}child/attenTickets.do",
					data:{"id":$("#id").val(), "birthday":$("#birthday").val()},
					success:function(data){
						if(data.code=="200"){
							layer.msg("关注成功",{"icon":1,time:1500})
							setTimeout(function() {
								window.location.href=data.msg;
							}, 1500)
						}else if(data.code=="201"){
							layer.msg("您已关注该宝宝",{"icon":7})
							setTimeout(function() {
								window.location.href=data.msg;
							}, 1500)
						}else{
							layer.msg(data.msg,{"icon":7})
						}
					},
					error:function(){
						
					}
				});
			}else{
				layer.msg("请选择宝宝生日",{"icon":7});
				return false;
			}
		});
		
	})
</script>


</html>
