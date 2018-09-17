<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	<title>选择疫苗</title>
</head>
<body>
	<div class="container">
		<form action="${ctx}order/reserve.do" method="post">
		<input type="hidden" name="childcode" value="${childcode}">
		<input type="hidden" name="name" value="${pros[0].name}">
		<input type="hidden" name="nid" value="${pros[0].nid}">
		<c:forEach items="${pros }" var="pro">
			<c:if test="${not empty pro.id}">
			<div class=" ">
				<span>${pro.name }</span>
				<c:choose>
					<c:when test="${pro.isforeign == 'Y' }">一类疫苗</c:when>
					<c:when test="${pro.isforeign == 'N' }">二类疫苗</c:when>
				
				</c:choose>
				<span>￥${pro.sellprice }</span>
				<label for="1"><input type="radio" id="1" class="s" value="${pro.id }" name="pid"><i></i></label>			
			</div>
			</c:if>
		</c:forEach>
		<c:if test="${empty pros[0].id}">
			<div>
				<div class=" ">
					<span>${pros[0].name }</span>
					<span>免费</span>
					<label for="1"><input type="radio" id="1" class="s" value="" name="pid"><i></i></label>			
				</div>
			</div>
		</c:if>
			
			<div class="box-vaccination p2">
				<label for="5"><input type="radio" id="5" class="s" checked="checked"><i></i></label>
				<span>￥2元<a href="#">《疫苗病流和预防接种管理条例》</a></span>
			</div>
			<div class="box-vaccination">
				<div class="total">总计：<span>2元</span></div>
			</div>
			<div class="box-wrap">
				<button type="submit" class="p1"></button>
				<a href="${ctx}vac/index.do" class="p2"></a>
			</div>
		</form>
	</div>
</body>
</html>