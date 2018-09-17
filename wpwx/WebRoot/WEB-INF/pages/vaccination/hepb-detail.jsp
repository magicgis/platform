<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	
	<title>成人建档信息</title>
</head>
	<style>
		
	</style>
<body class="wrap" style="background: #f9f8f8;margin:10px;">
	<div class="cell">
		<div class="strong" style="color: #f48093;border-right:1px solid #f48093;" >建档编码</div>
		<div><span class="strong" style="color: #f48093"> ${temp.id}</span></div>
	</div>
	<div class="cell">
		<div class="strong">身份证号</div>
		<div class="strong">${temp.idcardNo}</div>
	</div>
	<div class="cell">
		<div class="strong" >姓名</div>
		<div class="strong">${temp.username}</div>
	</div>
	<div class="cell">  
		<div class="strong">性别</div>
		<div class="strong"> <c:choose>
				<c:when test="${temp.sex == '1'}">男</c:when>
				<c:when test="${temp.sex == '2'}">女</c:when>
			</c:choose>
		</div>
	</div>
	<div class="cell">
		<div class="strong" >年龄</div>
		<div class="strong">${temp.age}岁</div>
	</div>
	<div class="cell">
		<div class="strong">联系电话</div>
		<div class="strong">${temp.linkPhone}</div>
	</div>
	
	<div class="cell">
		<div class="strong">详细地址</div>
		<div class="strong">${temp.address}</div>
	</div>
	<div class="cell">
		<div class="strong" >体重</div>
		<div class="strong">${temp.weight}千克</div>
	</div>
	<div class="cell">
		<div class="strong">既往病史</div>
		<div class="strong">${temp.history}</div>
	</div>
	
</body>
<script src="${ctxStatic}js/common.js"></script>
</html>