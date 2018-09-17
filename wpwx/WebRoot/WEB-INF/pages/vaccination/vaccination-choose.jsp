<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<link rel="stylesheet" href="${ctxStatic}css/style.css">
	<title>选择接种</title>
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
				$("#pp").val(price);
				sum();
		 	});
			
			//勾选保险
			$("#insurance").change(function(){
				if($(this).is(":checked")){
					$("#p2").val(200);
				}else{
					$("#p2").val(0);
				}
				sum();
			});
			
			//计算价格
			function sum(){
				var total = parseInt($("#pp").val()) + parseInt($("#p2").val());
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
									$.each(data.productslist, function(i,t){
										html = html + '<div class="cell item">'+
											'<label for="i-' + t.id + '" class="s"><input type="radio" id="i-' + t.id + '"  class="i" name="pid" value="' + t.id + '" data-price="' + t.sellprice + '"><i></i>'+
											'<span>' + t.vaccName + '(厂商：' + t.manufacturer + ')</span><span class="fr red" style="font-weight: bold;">￥ ' + (t.sellprice/100) + '</span></label>'+
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
									$.each(data.productslist, function(i,t){
										html = html + '<div class="cell item">'+
											'<label for="i-' + t.id + '" class="s"><input type="radio" id="i-' + t.id + '"  class="i" name="pid" value="' + t.id + '" data-price="' + t.sellprice + '"><i></i>'+
											'<span>' + t.vaccName + '(厂商：' + t.manufacturer + ')</span><span class="fr red" style="font-weight: bold;">￥ ' + (t.sellprice/100) + '</span></label>'+
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
			

			
		}) 
	</script>
</head>
<body>
	<div class="banner">
		<img src="${ctxStatic}images/banner_select.jpg" alt="">
	</div>
	<form action="${ctx}order/orderpreview.do" method="post" id="orderForm">
		<input type="hidden" name="childcode" value="${childcode}">
		<input type="hidden" name="nid" value="${nid}">
		<input type="hidden" id="pp" value="0">
		<input type="hidden" id="p2" value="200">
		<div class="productlist">
		<c:forEach items="${productslist }" var="pro">
			<div class="cell item">
				<label for="i-${pro.id}" class="s"><input type="radio" id="i-${pro.id}"  class="i" name="pid" value="${pro.id}" data-price="${pro.sellprice }"><i></i>
				<span>${pro.vaccName } (厂商：${pro.manufacturer})</span><span class="fr red" style="font-weight: bold;">￥ ${pro.sellprice/100}</span></label>
				
			</div>
		</c:forEach>
		</div>
		<div class="cell">
			<label for="insurance" ><input type="checkbox" checked="checked" id="insurance" name="insurance" value="200">&nbsp;&nbsp;2元接种保险费用</label>
		</div>
		<div class="cell">
			<label for="showfree" ><input type="checkbox" id="showfree" >&nbsp;&nbsp;只显示免费</label>
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