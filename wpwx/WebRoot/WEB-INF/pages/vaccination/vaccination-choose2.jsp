<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<link rel="stylesheet" href="${ctxStatic}css/style.css">
	<title>选择疫苗</title>
	<style type="text/css">
		    .item{
			    font-size: 14px !important;
		    }
	</style>
	<script type="text/javascript">
		function link(url){
			window.location.href=url;
		};
	
		 $(function(){
		 	//选择疫苗
		 	$(".productlist").on( "click",".item", function(){
		 		var price = parseInt($(this).find("input").attr("data-price"));
		 		var nid = $(this).find("input").attr("data-nid");
		 		$("input[name=nid]").val(nid);
				$("#pp").val(price);
				if(nid){
					var countnid = nid.split("_").length;
					$("#p0").val(parseInt(countnid)*200);
					$("#p2").val(parseInt(countnid)*200);
				}
				sum();
		 	});
			
			//勾选保险
			$("#insurance").change(function(){
				if($(this).is(":checked")){
					$("#p2").val($("#p0").val());
				}else{
					$("#p2").val(0);
				}
				sum();
			});
			
			//计算价格
			function sum(){
				var total;
				if($("#insurance").is(":checked")){
					total = parseInt($("#pp").val()) + parseInt($("#p2").val());
				}else{
					total = parseInt($("#pp").val());
				}
				$(".sum").html(total / 100)
			}
			
			//初始化
			function restore(){
				$("#pp").val(0);
				$("#p2").val(200);
				$(".sum").html(2)
				$("#insurance").prop("checked",true);
			}
			
// 			$(".productlist :first").find("input").click();
			
			$("#subbtn").click(function(){
				if(!$("input[name=pid]:checked").val()){
					layer.msg("请选择一种疫苗", {icon:2});
				}else{
					$("#orderForm").submit();
				}
			});
			
			$("#showfree").change(function(){
				//直显示免费
				if($(this).is(":checked")){
					var index = layer.load();
					$.ajax({
						url:"${ctx}/vac/selectfree.do",
						data:{"code":$("input[name=childcode]").val(),"isfree":"free"},
						type:"post",
						timeout:1000*15,
						success:function(data){
							if(data){
								if(data.status == '200'){
									$("input[name=nid]").val(data.nid);
									restore();
									var html='';
									$.each(data.vos, function(i,t){
										html = html + '<div class="cell item">'+
											'<label for="i-' + t.id + '" class="s"><input type="radio" id="i-' + t.id + '"  class="i" name="pid" value="' + t.pid + '" data-nid="' + t.nid + '" data-price="' + t.sellprice + '"><i></i>'+
											'<span>' + t.name +'</span><span class="fr red" style="font-weight: bold;">￥ ' + (t.sellprice/100) + '</span></label>'+
										'</div>';
									})
									$(".productlist").html(html);
								}else if(data.status == '500'){
									layer.msg(data.msg, {icon:2});
								}
							}
							layer.close(index);  
						},
						error:function(){
							layer.close(index);  
							layer.msg("服务器异常，请重试", {icon:2});
						},
					})
				}else{
					var index = layer.load();
					//都显示
					$.ajax({
						url:"${ctx}/vac/selectfree.do",
						data:{"code":$("input[name=childcode]").val(),"isfree":"all"},
						type:"post",
						timeout:1000*15,
						success:function(data){
							if(data){
								if(data.status == '200'){
									$("input[name=nid]").val(data.nid);
									restore();
									var html='';
									$.each(data.vos, function(i,t){
										html = html + '<div class="cell item">'+
											'<label for="i-' + t.id + '" class="s"><input type="radio" id="i-' + t.id + '"  class="i" name="pid" value="' + t.pid + '" data-nid="' + t.nid + '" data-price="' + t.sellprice + '"><i></i>'+
											'<span>' + t.name +'</span><span class="fr red" style="font-weight: bold;">￥ ' + (t.sellprice/100) + '</span></label>'+
										'</div>';
									})
									$(".productlist").html(html);
								}else if(data.status == '500'){
									layer.msg(data.msg, {icon:2});
								}
							}
							layer.close(index);  
						},
						error:function(){
							layer.close(index);  
							layer.msg("服务器异常，请重试", {icon:2});
						},
					});
					};
					});
					
					$("#showfree").change();
			

			$("#insuranceDetail").click(function(){
				layer.open({
				  title: '保险说明',
				  content: '疫苗险是平安少儿保险新增加的一个针对疫苗安全的保险保障项目,被保险人因注射疫苗在48小时内住院的，本公司就其实际支出的床位费、手术费、药费、治疗费等合理医疗费用，给付“疫苗住院保险金”；疫苗险还提供因注射疫苗48小时内身故双倍赔付。疫苗保险相关产品:平安少儿保险、少儿综合保险是专为30天-18周岁少年儿童设计的综合保障险种，涵盖人身意外、意外医疗、疫苗意外保险、疾病身故和疾病住院15种儿童常见重大疾病保险，同时提供意外医疗救援和垫付、意外住院津贴等健康保险。'
				});  
			});
			
		}) 
	</script>
</head>
<body>
	<div class="banner">
		<img src="${ctxStatic}images/banner_select.jpg" alt="">
	</div>
	<form action="${ctx}order/orderpreview.do" method="post" id="orderForm">
		<input type="hidden" name="childcode" value="${childcode}">
		<input type="hidden" name="nid" value="">
		<input type="hidden" id="pp" value="0">
		<input type="hidden" id="p2" value="200">
		<input type="hidden" id="p0" value="200">
		<div class="productlist">
		<%-- <c:forEach items="${productslist }" var="pro">
			<div class="cell item">
				<label for="i-${pro.id}" class="s"><input type="radio" id="i-${pro.id}"  class="i" name="pid" value="${pro.id}" data-price="${pro.sellprice }"><i></i>
				<span>${pro.vaccName } (厂商：${pro.manufacturer})</span><span class="fr red" style="font-weight: bold;">￥ ${pro.sellprice/100}</span></label>
				
			</div>
		</c:forEach> --%>
		</div>
		<div class="cell">
			<label for="insurance" class="s c">
				<input type="checkbox" checked="checked" id="insurance" name="insurance" value="200">接种保险费用(2元/针)
				<i></i>
			</label>
			<span id="insuranceDetail" style="float: none; font-size:13px; font-weight: bold; color: blue;position: absolute;top: 0;right: 100px; ">保险说明</span>
		</div>
		<div class="cell">
			<label for="showfree" class="s c">
				<input type="checkbox" id="showfree" >只显示一类
				<i></i>
			</label>
		</div>
		<div class="cell">
			<span class="total">总计：￥<em class="sum" style="font-style:normal; font-weight: bold;" >2</em></span>
		</div>
		<div class="botton-wrap">
			<button type="button" class="fl" id="subbtn">预约</button>
			<button class="fr" type="button" onclick="link('${ctx}vac/index.do')">返回</button>
		</div>
	</form>
</body>

</html>