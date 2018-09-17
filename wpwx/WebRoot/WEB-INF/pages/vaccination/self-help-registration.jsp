<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	<link rel="stylesheet" href="${ctxStatic}css/lCalendar.css">
	<link rel="stylesheet" href="${ctxStatic}css/select1.css">
	<link rel="stylesheet" href="${ctxStatic}css/mui.min.css">
	<link rel="stylesheet" href="${ctxStatic}css/mui.picker.min.css" />
	<%--<script type="text/javascript" src="${ctxStatic}js/select2.js"></script>--%>
    <%--<script src="${ctxStatic}/distjs/jquery.1.11.2.min.js"></script>--%>
	<title>自助登记</title>
	<style type="text/css">
		.error{
			border-color: red !important;
			color: red;
		}
		.end{
			border-color: rgba(84, 91, 73, 0.35) !important;
			color: rgba(84, 91, 73, 0.35);
		}
		input ,select{
			padding:0 15px;
		}
	</style>
</head>
<body>
	<div class="wrap">
		<form id="self-form" action="${ctx}child/temp/save.do" method="post">
			<div class="box">
				<label for="">儿童姓名<span class="red">*</span></label>
				<input type="text" placeholder="请填写儿童姓名" name="childname" required required="required">
			</div>
			<div class="box">
				<label for="" class="fl mr10">儿童性别<span class="red">*</span></label>
				<div class="sex fl">
					<label for="man" class="fl"> <input type="radio" name="gender" id="man" required checked="checked" value="1" class="s"> <i></i>
					</label> <span class="fl ml10">男性</span>
				</div>
				<div class="sex fl">
					<label for="wom" class="fl"> <input type="radio" name="gender" id="wom" required value="2" class="s"> <i></i>
					</label> <span class="fl ml10">女性</span>
				</div>
			</div>
			<div class="box">
				<label for="">出生证号</label>
				<input type="text" placeholder="请填写出生证号" name="birthcode" >
			</div>
			<div class="box">
				<label for="">儿童身份证</label>
				<input type="text" placeholder="请填写宝宝身份证号" name="cardcode" class="idCard">
			</div>
			<div class="box">
				<label for="">母亲姓名<span class="red">*</span></label>
				<input type="text" name="guardianname" placeholder="请填写母亲姓名" required required="required">
			</div>
			<div class="box">
				<label for="">母亲身份证<span class="red">*</span></label>
				<input type="text" name="guardianidentificationnumber" placeholder="请填母亲身份证号" required  class="idCard" required="required">
			</div>
			<div class="box">
				<label for="">母亲电话<span class="red">*</span></label>
				<input type="text" name="guardianmobile" class="mobile" data-msg-mobile="号码格式错误" placeholder="请填写母亲电话" required required="required">
			</div>
			<div class="box">
				<label for="">父亲姓名<span class="red">*</span></label>
				<input type="text" name="father" placeholder="请填写父亲姓名">
			</div>
			<div class="box">
				<label for="">父亲身份证<span class="red">*</span></label>
				<input type="text" name="fathercard" placeholder="请填父亲身份证号" class="idCard">
			</div>
			<div class="box">
				<label for="">父亲电话<span class="red">*</span></label>
				<input type="text" name="fatherphone" class="mobile" data-msg-mobile="号码格式错误" placeholder="请填写父亲电话">
			</div>		
			<div class="box">
				<label for="">胎次<span class="red">*</span></label>
				<input type="text" name="childorder" placeholder="请填写胎次(如:1)" data-msg-number="胎次请填写数字(如:1)" class="number" required required= required">
			</div>
			<div class="box select1">
				<label class="col-sm-2 control-label">出生医院</label>
				<select name="birthhostipal">
					<c:forEach items="${hostipallist}" var="birth">
						<option value="${birth.str1 }">${birth.str2 }</option>
					</c:forEach>
				</select>
			</div>
			<div class="box mt10">
				<label for="">宝宝生日<span class="red">*</span></label>
				<input type="text" data-options='{"type":"date"}' class="date-box1 dateNotNull" name="birthday" id='result1' placeholder="请选择宝宝生日" >
			</div>
			<div class="box select1">
				<label class="col-sm-2 control-label">民族：</label>
				<select name="nation">
					<c:forEach items="${nation}" var="nati">
						<option value="${nati.str2}">${nati.str1}</option>
					</c:forEach>
				</select>
			</div>
			<div class="box">
				<label for="">出生体重(g)<span class="red">*</span></label>
				<input type="text" name="birthweight" class="number required" data-msg-number="体重请填写数字(单位:克,如:1)" placeholder="出生体重(单位:克)">
			</div>
			<div class="box">
				<label for="" class="fl mr10 ">户口类别<span class="red">*</span></label>
				<select  name="properties">
				<c:forEach items="${properties }" var="prop">
					<option value="${prop.str2}">${prop.str1}</option>
				</c:forEach>
				</select>				
			</div>
			<div class="box" id="homeaddress">
				<label for="">家庭地址<span class="red">*</span></label>
				<div style="display: block;">
					<select class="one" name="province" style="width: 32%; float: left">
						<option value="">请选择省份</option>
					</select>
					<select class="two" name="city" style="width: 32%; float: left; margin-left: 2%">
						<option value="county">请选择城市</option>
					</select>
					<select class="three" name="county" style="width: 32%; float: left; margin-left: 2%">
						<option value="">请选择区域</option>
					</select>
				</div>
				<input type="text" name="address" class="required" placeholder="请填写家庭详细地址">
			</div>
			<div class="box" id="registryaddress">
				<label for="">户籍地址</label>
				<div style="display: block;">
					<select class="one" name="pr" style="width: 32%; float: left">
						<option value="">请选择省份</option>
					</select>
					<select class="two" name="ci" style="width: 32%; float: left; margin-left: 2%">
						<option value="">请选择城市</option>
					</select>
					<select class="three" name="co" style="width: 32%; float: left; margin-left: 2%">
						<option value="">请选择区域</option>
					</select>
				</div>
				<input type="text" name="add" placeholder="请填写户籍详细地址">				
			</div>

			<div class="box">
				<label for="" class="fl mr10">居住类别</label>
				<select  name="reside">
					<c:forEach items="${reside }" var="resi">
						<option value="${resi.str2 }">${resi.str1}</option>
					</c:forEach>
				</select>
			</div>

			<div class="box">
 				<label for="" class="fl mr10">异常反应</label>
 				<textarea  name="paradoxicalreaction" placeholder="请输入接种异常反应史、接种禁忌和疫苗针对传染病" maxlength="200" rows="2" required>无</textarea>
			</div>
			<div class="box">
				<label for="">备注</label>
				<textarea name="remarks"  placeholder="请填写备注" rows="4"></textarea>
			</div>
			
			<div class="box">
				<button class="submit" type="submit" id="sub">提 交</button>				
			</div>
			<div class="box" style="margin-top: 20px; color: red; font-size: 16px;">
			注：自助建档后需在当地疫苗接种地点完成建档并领取接种证。</div>
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
		//表单验证
		$("#self-form").validate({
			submitHandler: function(form) {
				var $birth = $(".dateNotNull");
				if(!$birth.val()){
					layer.msg("请选择宝宝生日",{"icon":7,"offset": window.innerHeight/2-70});
					return ;
				}
				layer.load();
                $("#sub").attr('disabled',true);
                $(".submit").addClass("end");
                form.submit();
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
                        var html = "<option value='340600'>淮北市</option>";
						for(var i = 0; i < data.length; i ++){
                            var html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
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
                        var html = "<option value='340603'>相山区</option>";
						for(var i = 0; i < data.length; i ++){
                            var html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
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

<script type="text/javascript">

    //通过省市区编码查询接种单位
   /* var  getInoculation = function (provinceCity) {
        //var provinceCity = "340603";
        //var provinceCity = $('#provinceCity').val();
        $.ajax({
            url : "${ctx}child/listDept/"+provinceCity+".do",
            type:"POST",
            success:function(data){
                if(data.length>0){
                    $("#officeinfo").empty();
                    $("#officeinfo").select2({
                        data: data,
                        placeholder:'请选择接种单位'
                    })
                }else {
                    $("#officeinfo").empty();
                    $("#officeinfo").append('<option values="">此地区的接种单位暂未开放</option>');
                }
            },
            error:function(){
                layer.msg("请求失败，请稍后再试",{icon:0, time: 2000});
            },
        });
    }*/

    //通过接种单位查询社区
/*    var getCompany = function (off) {
        // var officeinfo = $('#officeinfo').val();
        $.ajax({
            type:"POST",
            url:"${ctx}child/offarea/" +off + ".do",
            async: false,
            success:function(data){
                if (data.length>0) {
                    var str = "" ;
                    for(var i = 0;i<data.length;i++){
                        str += '<option value="'+data[i].str1+'">'+data[i].str2+'</option>';
                    }
                    $("#regionaliza").html("");
                    $('#regionaliza').html(str);
                } else {
                    $("#regionaliza").html("");
                    $("#regionaliza").append('<option values="">此接种单位的区域未维护</option>');
                }
            }, error: function(){

            }
        });
    }*/

/*    //请求省级数据
    $.ajax({
        url:"${ctx}child/area/0.do",
        success:function(data){
            if(data){
                var html = "";//<option value='340000'>安徽省</option>";   $(".selector").find("option[text='pxx']").attr("selected",true);
                for(var i = 0; i < data.length; i ++){
                    if(data[i].id=="340000"){
                        html="<option value='" + data[i].id + "'>" + data[i].name + "</option>";
                    }
                }
                for(var i = 0; i < data.length; i ++){
                    if(data[i].id!="340000"){
                        html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
                    }
                }
                $(".one1").each(function(){
                    $(this).html(html);
                    $(this).change();
                });
            }
        }
    });
    //修改省级行政单位刷新市级行政单位数据
    var $nex2;
    var $nex;
    $(".one1").change(function(){
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
                    var html = "";//<option value='340600'>淮北市</option>";
                    for(var i = 0; i < data.length; i ++){
                        if(data[i].id=="340600"){
                            html="<option value='" + data[i].id + "'>" + data[i].name + "</option>";
                        }
                    }
                    for(var i = 0; i < data.length; i ++){
                        if(data[i].id!="340600"){
                            var html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
                        }
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
                    /!*var provinceCity = $('#provinceCity').val();
                    getInoculation(provinceCity);
                    var officeinfo = $('#officeinfo').val();
                    getCompany(officeinfo);*!/
                }
            }
        });
    });

    var $next2;
    var $next;
    //修改市级行政单位数据刷新区级行政单位
    $(".two1").change(function(){
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
                    var html = "";//<option value='340603'>相山区</option>";
                    for(var i = 0; i < data.length; i ++){
                        if(data[i].id=="340603"){
                            html="<option value='" + data[i].id + "'>" + data[i].name + "</option>";
                        }
                    }
                    for(var i = 0; i < data.length; i ++){
                        if(data[i].id!="340603"){
                            var html = html + "<option value='" + data[i].id + "'>" + data[i].name + "</option>";
                        }
                    }
                    if($next){
                        $next.html(html);
                        $next = "";
                    }else{
                        $next2.html(html);
                        $next2 = "";
                    }
                    var provinceCity = $('#provinceCity').val();
                    getInoculation(provinceCity);
                    var off = $('#officeinfo').val();
                    getCompany(off);
                }
            }
        });
    });

    $(".three1").change(function(){
        var provinceCity = $('#provinceCity').val();
        getInoculation(provinceCity);
	});

    $("#officeinfo").change(function(){
        var off = $('#officeinfo').val();
        getCompany(off);
	});
    getInoculation("340603");
    getCompany("3406030301");*/


</script>
</html>
