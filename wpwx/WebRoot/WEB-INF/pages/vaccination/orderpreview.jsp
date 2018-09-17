<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	<link rel="stylesheet" href="${ctxStatic}css/style.css">
	<title>订单</title>
</head>
<body style="background: #ebebeb">
	<div class="container">
		<form action="${ctx}order/reserve.do" method="post">
			<input type="hidden" name="nid" value="${form.nid}">
			<input type="hidden" name="pid" value="${form.pid}">
			<input type="hidden" name="childcode" value="${form.childcode}">
			<input type="hidden" name="insurance" value="${form.insurance}">
			
			<div class="order-wrap">
				<div class="order-cell">
					<label for="">儿童编号：</label>
					<input type="text" value="${info.childcode}">
				</div>
				<div class="order-cell">
					<label for="">儿童姓名：</label>
					<input type="text" value="${info.childname}">
				</div>
				<div class="order-cell">
					<label for="">性别：</label>
					<input type="text" value="${info.gender=='1'?'男':'女'}">
				</div>
				<div class="order-cell">
					<label for="">生日：</label>
					<input type="text" value="<fmt:formatDate value="${info.birthday}" pattern="yyyy-MM-dd" />">
				</div>
				<div class="order-cell">
					<label for="">民族：</label>
					<input type="text" value="${info.nationName}">
				</div>
				<div class="order-cell">
					<label for="">监护人姓名：</label>
					<input type="text" value="${info.guardianname}">
				</div>
			</div>
			<div class="order-wrap">
				<div class="order-cell">
					<label for="">疫苗名称：</label>
					<input type="text" value="${pname}">
				</div>
				<div class="order-cell">
					<label for="">是否保险：</label>
					<input type="text" value="${not empty form.insurance ? '已购买':'未购买'}">
				</div>
				<div class="order-cell">
					<label for="">订单价格：</label>
					<input type="text" value="${form.price*0.01}">
				</div>
			</div>
			<div class="order-submit">
				<button class="fl" type="submit">确定</button>
				<button class="fr" type="button" onclick="history.go(-1)">取消</button>
			</div>
		</form>
	</div>
</body>
</html>