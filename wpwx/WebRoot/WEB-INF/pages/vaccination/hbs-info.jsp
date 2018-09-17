<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>  
    <head>  
        <meta charset="UTF-8">  
        <title>个人信息</title>  
        <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">  
        <meta name="format-detection" content="telephone=no">  
        <meta name="apple-mobile-web-app-capable" content="yes">  
        <link href="${ctxStatic}css/reset.css" rel="stylesheet" />
        <link href="${ctxStatic}css/mui.min.css" rel="stylesheet" />
        <link href="${ctxStatic}css/mui.picker.min.css" rel="stylesheet" />
    </head>  
    <body>  
    	<ul class="progressbar progressbar-hbs">
			<li class="active">个人信息</li>
			<li>地址信息</li>
		</ul>
  		<div class="wrap babyInfo">
  			<div class="box">
  				<p class="fl">
  					<span></span><label>姓名</label>
  				</p>
  				<input type="text" placeholder="" id="username" name="babyName" class="inputBg">
  			</div>
  			<div class="box sex-group">
				<div style="display: inline-block;line-height:30px;">
					<label class="sex-radio" data-id="sex-f">男<i data-sex="1"></i></label> 
				</div>
				<div style="display: inline-block;line-height:30px;margin-left:15px;">
					<label class="sex-radio" data-id="sex-m">女<i data-sex="2"></i></label> 
				</div>
				<input id="sex-f" class="hide" type="radio" name="sex" value="F" required checked="checked">
				<input id="sex-m" class="hide" type="radio" name="sex" value="M" required>
			</div>


			<div class="box mt10">
  				<p class="fl">
  					<label>身份证</label>
  				</p>
  				<input type="text" placeholder="" id="idcardNo" name="" class="inputBg"/>
  			</div>
			

			<div class="box">
  				<p class="fl">
  					<label>生日</label>
  				</p>
  				<input type="text" data-options='{"type":"date"}' readonly class="date-box1 dateNotNull inputBg" name="birthday" id='birthday' placeholder="请选择出生日期" >
  			</div>

  			<div class="box">
  				<p class="fl">
  					<span></span><label>年龄</label>
  				</p>
  				<input type="number" onkeyup="value=value.replace(/[^\d\.]/g,'')" onblur="value=value.replace(/[^\d\.]/g,'')" placeholder="" id="age" class="inputBg"/>
  			</div>

  			<div class="box">
  				<p class="fl">
  					<span></span><label>联系电话</label>
  				</p>
  				<input type="text" placeholder="" id="linkPhone" class="inputBg"/>
  			</div>

  			<div class="box">
  				<button class="info-submit hbs-next">下一步</button>
  			</div>
  		</div>

  		<div class="wrap selfInfo hide">
  			<div class="box">
				<p class="">
  					<span></span><label>居住住址</label>
  				</p>
  				<div class="address-box">
					<select class="one one1" name="province" id="province">
						<option value="">请选择省份</option>
					</select>
					<select class="two two1" name="city" id="city" style="margin-left: 2%">
						<option value="city">请选择城市</option>
					</select>
					<select class="three three1" name="county" id="county" style="margin-left: 2%">
						<option value="">请选择区域</option>
					</select>
				</div>
				<input class="group2 zzjd-input inputBg" id="address" type="text" placeholder="详细居住地址" style="font-size:.14rem;">
			</div>

			<div class="box">
  				<p class="fl">
  					<label>既往病史</label>
  				</p>
  				<textarea name="reaction" id="history" rows="4" class="inputBg"></textarea>
  			</div>

			<div class="box" style="color:#6ac4c8;">
  				注:自主建档有效期为三天，不可重复使用，请尽快完成登记
  			</div>

  			<div class="box">
  				<button class="backcustodianInfo backcustodianInfoHbs fl">上一步</button>
  				<button class="info-next hbs-submit fr">提交</button>
  			</div>
  		</div>
  		<script src="${ctxStatic}js/mui.min.js"></script>
  		<script src="${ctxStatic}js/mui.picker.min.js"></script>
        <script src="${ctxStatic}js/common.js"></script>
        <script src="http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js"></script>
        <script src="${ctxStatic}js/info.js"></script>
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
					    beginDate: new Date(1900, 01, 01),
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
    </body>  
</html>  