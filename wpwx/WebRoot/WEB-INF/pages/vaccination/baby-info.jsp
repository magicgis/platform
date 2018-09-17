<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>  
    <head>  
        <meta charset="UTF-8">  
        <title>宝宝信息</title>  
        <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">  
        <meta name="format-detection" content="telephone=no">  
        <meta name="apple-mobile-web-app-capable" content="yes">
        <link href="${ctxStatic}css/reset.css" rel="stylesheet" />
        <link href="${ctxStatic}css/mui.min.css" rel="stylesheet" />
        <link href="${ctxStatic}css/mui.picker.min.css" rel="stylesheet" />
    </head>  
    <body>  
    	<ul class="progressbar">
			<li class="active">宝宝信息</li>
			<li>监护人信息</li>
			<li>地址信息</li>
		</ul>
    	
  		<div class="wrap babyInfo">
  			<div class="box">
  				<p class="fl">
  					<span></span><label>宝宝姓名</label>
  				</p>
  				<input type="text" placeholder="" id="babyName" name="babyName" class="inputBg" >
  			</div>
  			<div class="box sex-group">
				<div style="display: inline-block;line-height:30px;">
					<label class="sex-radio" data-id="sex-f">男<i  data-sex="1"></i></label> 
				</div>
				<div style="display: inline-block;line-height:30px;margin-left:15px;">
					<label class="sex-radio" data-id="sex-m">女<i data-sex="2"></i></label> 
				</div>
				<input id="sex-f" class="hide" type="radio" name="sex" value="F" required checked="checked">
				<input id="sex-m" class="hide" type="radio" name="sex" value="M" required>
			</div>

			<div class="box mt10">
				<div class="birthday fl">
					<p class="fl">
	  					<span></span><label>宝宝生日</label>
	  				</p>
	  				<input type="text" id="birthday" class="group zzjd-input date-box1 inputBg" readonly value="请选择..." data-options='{"type":"date"}'style="font-size:.14rem;">
				</div>
				<div class="gravida fr">
					<p class="fl">
	  					<span></span><label>宝宝胎次</label>
	  				</p>
	  				<input type="number" onkeyup="value=value.replace(/[^\d\.]/g,'')" onblur="value=value.replace(/[^\d\.]/g,'')" id="gravida" class="group zzjd-input inputClass inputBg" style="font-size:.14rem;">
				</div>
			</div>
			<div class="box">
				<div class="birthday fl">
					<p class="fl">
	  					<span></span><label>出身体重(g)</label>
	  				</p>
	  				<input type="number" onkeyup="value=value.replace(/[^\d\.]/g,'')" onblur="value=value.replace(/[^\d\.]/g,'')" id="weight" class="group zzjd-input inputClass inputBg" style="font-size:.14rem;">
				</div>
				<div class="gravida fr">
					<p class="fl">
	  					<label>民族</label>
	  				</p>
	  				<select name="nation" id="nation">
						<c:forEach items="${nation}" var="nati">
							<option value="${nati.str2}">${nati.str1}</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div class="box">
  				<p class="fl">
  					<label class="ml0">宝宝身份证号</label>
  				</p>
  				<input type="text" placeholder="" name="childIdCard" id="childIdCard" class="inputBg">
  			</div>

			<div class="box">
  				<p class="fl">
  					<label class="ml0">宝宝出生证号码</label>
  				</p>
  				<input type="text" oninput="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'');"  id="babyCardId" name="babyCardId" placeholder="非必填" class="inputBg" style="font-size:.14rem;" />
  			</div>

  			<div class="box">
  				<p class="">
  					<label class="ml0">宝宝出生医院</label>
  				</p>
  				<div class="address-boxs">
	  				<select class="one one1" id="babyprovince" name="pr" style="width:48%">
						<option value="">请选择...</option>
					</select>
					<select class="two two1" id="babycity" name="ci" style="margin-left: 2%;width:48%">
						<option value="">请选择...</option>
					</select>
					
				</div>
  				<select name="birthhostipal" id="hospital">
					<option value="">请选择...</option>
				</select>
  			</div>

  			<div class="box">
  				<p class="fl">
  					<label class="ml0">异常反应</label>
  				</p>
  				<textarea name="reaction" id="reaction" rows="4" class="inputBg"></textarea>
  			</div>
  			
  			<div class="box">
  				<button class="info-submit baby-next">下一步</button>
  			</div>
  		</div>
  		<div class="wrap custodianInfo hide">
  			<div class="box">
  				<p class="fl">
  					<span></span><label>母亲姓名</label>
  				</p>
  				<input type="text" placeholder="" name="motherName" id="motherName" class="inputBg">
  			</div>
  			<div class="box">
  				<p class="fl">
  					<span></span><label>母亲身份证号</label>
  				</p>
  				<input type="text" placeholder="" name="motherIdCard" id="motherIdCard" class="inputBg">
  			</div>
  			<div class="box">
  				<p class="fl">
  					<span></span><label>母亲电话</label>
  				</p>
  				<input type="text" placeholder="" name="motherPhoneNum" id="motherPhoneNum" class="inputBg">
  			</div>

  			<div class="box">
  				<p class="fl">
  					<span></span><label>父亲姓名</label>
  				</p>
  				<input type="text" placeholder="" name="fatherName" id="fatherName" class="inputBg">
  			</div>
  			<div class="box">
  				<p class="fl">
  					<span></span><label>父亲身份证号</label>
  				</p>
  				<input type="text" placeholder="" name="fatherIdCard" id="fatherIdCard" class="inputBg">
  			</div>
  			<div class="box">
  				<p class="fl">
  					<span></span><label>父亲电话</label>
  				</p>
  				<input type="text" placeholder="" name="fatherPhoneNum" id="fatherPhoneNum" class="inputBg">
  			</div>
  			<div class="box">
  				<button class="backChildInfo fl">上一步</button>
  				<button class="info-next detail-next fr">下一步</button>
  			</div>
  		</div> 	
  		<div class="wrap addressInfo hide">
  			<div class="box">
				<div class="birthday fl">
					<p class="fl">
	  					<span></span><label>户口类别</label>
	  				</p>
	  				<select  name="properties" id="htlb">
						<c:forEach items="${properties }" var="prop">
							<option value="${prop.str2}">${prop.str1}</option>
						</c:forEach>
					</select>	
				</div>
				<div class="gravida fr">
					<p class="fl">
	  					<label class="ml0">居住类别</label>
	  				</p>
	  				<select  name="reside" id="jzlb">
						<c:forEach items="${reside }" var="resi">
							<option value="${resi.str2 }">${resi.str1}</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div class="box">
				<p class="">
  					<span></span><label>家庭住址</label>
  				</p>
  				<div class="address-box">
					<select class="one one2" id="province" name="pr">
						
					</select>
					<select class="two two2" id="city" name="ci" style="margin-left: 2%;">
						<option value="">淮北市</option>
					</select>
					<select class="three three2" id="county" name="co" style="margin-left: 2%;">
						<option value="">相山区</option>
					</select>
				</div>
				<input class="group2 zzjd-input inputBg" type="text" id="address" placeholder="请填写家庭详细地址" style="font-size:.14rem;" />
			</div>

			<div class="box">
				<p class="">
  					<label class="ml0">户籍住址</label>
  				</p>
  				<div class="address-box">
					<select class="one one3" id="pr" name="province">
						<option value="">请选择...</option>
					</select>
					<select class="two two3" id="ci" name="city" style="margin-left: 2%;">
						<option value="">请选择...</option>
					</select>
					<select class="three three3" id="co" name="county" style="margin-left: 2%;">
						<option value="">请选择...</option>
					</select>
				</div>
				<input class="group2 zzjd-input inputBg" type="text" id="add" placeholder="详细户籍地址"  style="font-size:.14rem;"/>
			</div>

			<div class="box">
  				<p class="fl">
  					<label class="ml0">备注</label>
  				</p>
  				<textarea name="reaction" id="remarks" rows="4" class="inputBg"></textarea>
  			</div>

  			<div class="box" style="color:#6ac4c8;">
  				注:自主建档后需在当地疫苗接种地点完成建档并领取接种证
  			</div>

  			<div class="box">
  				<button class="backcustodianInfo backcustodianInfoBaby  fl">上一步</button>
  				<button class="info-next detail-submit fr">提交</button>
  			</div>
  		</div>
  		
  		
  		
  		<script src="${ctxStatic}js/mui.min.js"></script>
  		<script src="${ctxStatic}js/mui.picker.min.js"></script>
        <script src="${ctxStatic}js/common.js"></script>
        <script src="http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js"></script>
        <script src="${ctxStatic}js/info.js"></script>
        <script src="${ctxStatic}js/jquery.easing.min.js"></script>
        <script src="${ctxStatic}js/script.js"></script>
       
        <%-- <script src="${ctxStatic}js/vconsole.min.js"></script> --%>
         
  		<script>   
		//日期选择
	    (function($) {
			$.init();
			var btns1 = $('.date-box1');
			btns1.each(function(i, btn) {
				btn.addEventListener('tap', function() {
					/* var optionsJson = this.getAttribute('data-options') || '{}';
					var options = JSON.parse(optionsJson);
					var id = this.getAttribute('id');
					var picker = new $.DtPicker(options); */
					var picker = new mui.DtPicker({
					    type: "date",//设置日历初始视图模式 
					    beginDate: new Date(1990, 01, 01),
					    endDate: new Date(),
					    labels: ['年', '月', '日']
					   
					}) 
					picker.show(function(rs) {
						$('#birthday')[0].value = rs.value;
						picker.dispose();
					});
				}, false);
			});
		})(mui);
		</script>
		<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
		<!-- <script type="text/javascript" src="http://api.map.baidu.com/api?type=quick&ak=5ZCba4kItMwxnnt3aqqWrEuLzPBuQ6G1&v=1.0"></script> -->

<script type="text/javascript" >

	
	$(function(){
		//getLocation();
});  

</script>
        
    </body>  
</html>  