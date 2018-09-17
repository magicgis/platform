<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	<link rel="stylesheet" href="${ctxStatic}css/style.css">
	<title>选择疫苗</title>
	
	<script type="text/javascript">
	/* 	$(function(){
			var money = ${not empty price ? price:0};
			$("#money").html("￥" + (money + 2) + "元");
		}) */
		
		$(function(){
			$(".list :first").attr("checked","checked");
		})
	</script>
	
<%-- 	<c:if test="${not empty success}">
		<script type="text/javascript">
			window.location.href="${ctx}vac/index.do";
		</script>
	</c:if> --%>
</head>
<body>
	<div class="banner">
		<img src="${ctxStatic}images/banner_select.jpg" alt="">
	</div>
	<div class="container">
		<form action="${ctx}order/reserve.do" method="post">
		<input type="hidden" name="childcode" value="${childcode}">
		<input type="hidden" name="nid" value="${nid}">
			
			<div class="list">
				<c:forEach items="productslist" var="pro">
				<div >
					<span>${pro.vaccName }</span>				
					<span>￥${pro.sellprice }</span>
					<span>￥${pro.manufacturer }</span>
					<label for="1"><input type="radio" name="pid" class="s" checked="checked"><i></i></label>			
				</div> 
				</c:forEach>
			</div>
			
			<div class="box-vaccination p2">
				<label for="5"><input type="radio" id="5" class="s" checked="checked"><i></i></label>
				<span>￥2元<a href="#">《疫苗病流和预防接种管理条例》</a></span>
			</div>
			
			<div class="box-vaccination">
				<div class="total">总计：<span id="money">元</span></div>
			</div>
			<div class="box-wrap">
				<button type="submit" class="p1"></button>
				<a href="${ctx}vac/index.do" class="p2"></a>
			</div>
		</form>
	</div>
</body>
</html>