<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	<link rel="stylesheet" href="${ctxStatic}css/lCalendar.css">
	<link rel="stylesheet" href="${ctxStatic}css/select1.css">
	<link rel="stylesheet" href="${ctxStatic}css/mui.min.css">
	<link rel="stylesheet" href="${ctxStatic}css/mui.picker.min.css" />
	<script type="text/javascript" src="${ctxStatic}js/select1.js"></script>
	<title>成人自助登记</title>
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
		<form id="self-form" action="${ctx}rabies/hepbSave.do" method="post">
		
<%-- 			<div class="box">
				<label for="" class="fl">接种单位<span class="red">*</span></label>
				<select name="officeinfo">
					<c:forEach items="${deptlist }" var="o">
						<option value="${o.code }">${o.name }</option>
					</c:forEach>
				</select>				
			</div> --%>
			
			
			
			<div class="box">
				<label for="">姓名<span class="red">*</span></label>
				<input type="text" placeholder="请您的姓名" name="username" required required="required">
			</div>
			
			<div class="box">
				<label for="" class="fl mr10">性别<span class="red">*</span></label>
				<div class="sex fl">
					<label for="man" class="fl"> <input type="radio" name="sex" id="man" checked="checked" value="1" class="s"> <i></i>
					</label> <span class="fl ml10">男</span>
				</div>
				<div class="sex fl">
					<label for="wom" class="fl"> <input type="radio" name="sex" id="wom" value="2" class="s"> <i></i>
					</label> <span class="fl ml10">女</span>
				</div>
			</div>
			
			<div class="box">
				<label for="">生日</label>
				<input type="text" data-options='{"type":"date"}' class="date-box1 dateNotNull" name="birthday" id='birthday' placeholder="请选择出生日期" >
			</div>
			
			<div class="box">
				<label for="">年龄<span class="red">*</span></label>
				<input type="text" name="age" class="number age" data-msg-number="年龄请填写数字(如:18)" placeholder="请填写年龄(如:18)" required="required">
			</div>
			
			<div class="box">
				<label for="">联系电话<span class="red">*</span></label>
				<input type="text" name="linkPhone" class="mobile" data-msg-mobile="号码格式错误" placeholder="请填写联系电话" required="required">
			</div>
			
			<div class="box" id="homeaddress">
				<label for="">居住地址</label>
				<div style="display: block;">
					<select class="one" name="province" style="width: 32%; float: left">
						<option value="">请选择省份</option>
					</select>
					<select class="two" name="city" style="width: 32%; float: left; margin-left: 2%">
						<option value="city">请选择城市</option>
					</select>
					<select class="three" name="county" style="width: 32%; float: left; margin-left: 2%">
						<option value="">请选择区域</option>
					</select>
				</div>
				<label for="">详细地址</label>
				<input type="text" name="address" placeholder="请填写详细地址" >
			</div>
			
<!-- 			<div class="box">
				<label for="">体重(kg)<span class="red">*</span></label>
				<input type="text" placeholder="请填体重(单位:kg)" name="weight" class="number required" data-msg-number="体重请填写数字(单位:kg,如:1)">
			</div>	 -->
			
			<div class="box">
				<label for="">身份证</label>
				<input type="text" name="idcardNo" placeholder="请填身份证号" class="idCard">
			</div>
			<div class="box">
				<label for="">既往病史</label>
				<textarea name="history"  placeholder="请填写既往病史" rows="4">无</textarea>
			</div>
			
			
			<div class="box">
				<button class="submit" type="submit" id="sub">提 交</button>
<!-- 				<button style="display: none;" type="submit" id="sub2"></button> -->
			</div>
			<div class="box" style="margin-top: 20px; color: red; font-size: 16px;">
			注：自助建档有效期为三天,不可重复使用,请尽快完成登记。</div>
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
				var id = this.getAttribute('id');
				var now = new Date();
				options.endYear = now.getFullYear();
				options.beginYear = now.getFullYear() - 100;
				var picker = new $.DtPicker(options);
				picker.show(function(rs) {
					btn.value = rs.value;
					getAge(rs.value); //根据生日计算年龄
					picker.dispose();
				});
			}, false);
		});
	})(mui);
    $("#time").focus(function(){
	    document.activeElement.blur();
	});

	//根据生日计算年龄
	function getAge(date){
 		/*console.info(date);
		console.info(jsGetAge(date));*/
		$("input[name=age]").val(jsGetAge(date));
	};
	
</script>
<script>
	$(function(){
		
		
		//表单验证
		$("#self-form").validate({
			submitHandler: function(form) {
				
				layer.load(); //加载弹框
				form.submit();//表单提交时验证
			}
		})
	
		//请求省级数据
		$.ajax({
			url:"${ctx}child/area/0.do",
			success:function(data){
				if(data){
					var html = "<option value='340000'>安徽省</option>";
					for(var i = 0; i < data.length; i ++){
						html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
					}
					$(".one").each(function(){
						$(this).html(html);
						$(this).change();
					});
				}
			}
		});
		
		//修改省级行政单位刷新市级行政单位数据
		var $nex2;
		var $nex;
		$(".one").change(function(){
			$(this).next().html("<option value=''>请选择城市</option>");
			$(this).next().next().html("<option value=''>请选择区域</option>");
			if($nex){
				$nex2 = $(this).next();
			}else{
				$nex = $(this).next();
			}
			$.ajax({
				url:"${ctx}child/area/" + $(this).val() + ".do",
				success:function(data){
					if(data){
						var html = "";
						for(var i = 0; i < data.length; i ++){
							html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
					}
					//这里解决重复加载第二次赋值吧第一次赋值覆盖
						if($nex){
							$nex.html(html);
							$nex.change();
							$nex = "";
						}else{
							$nex2.html(html);
							$nex2.change();
							$nex2 = "";
						}
						
					}
				}
			});
		});
		
		
		var $next2;
		var $next;
		//修改市级行政单位数据刷新区级行政单位
		$(".two").change(function(){
			$(this).next().html("<option value=''>请选择区域</option>");
			if($next){
				$next2 = $(this).next();
			}else{
				$next = $(this).next();
			}
			$.ajax({
				url:"${ctx}child/area/" + $(this).val() + ".do",
				success:function(data){
					if(data){
						var html = "";
						for(var i = 0; i < data.length; i ++){
							html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
						}
						if($next){
							$next.html(html);
							$next = "";
						}else{
							$next2.html(html);
							$next2 = "";
						}
						}
				}
			});
		});
	})
</script>

</html>
