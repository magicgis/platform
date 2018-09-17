<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<title>妈咪课堂</title>
	<link rel="stylesheet" href="${ctxStatic}css/normalize.css" />
	<link rel="stylesheet" href="${ctxStatic}css/common.css" />
	<style>
		.list-item{
			background: url(${ctxStatic}img/next.png) no-repeat right 10px center;
			background-size:10px 20px;
			height: 46px;
			position: relative;
			border-bottom: 1px solid #CBCACC;
		}
		.list-img{
			height: 40px;
			position: absolute;
			left: 10px;
			top: 3px;
		}
		.list-item .title{
			margin-left: 62px;
			height: 100%;
			line-height: 46px;
		}
	</style>
</head>
<body>
	<div id="root">
		<div class="banner">
			<img src="${ctxStatic}img/banner_demo.jpg" alt="" />
		</div>
		<c:forEach items="${list}" var="item">
			<a class="lista" href="${ctx}cms/article/detail.do?id=${item.id}">
			<div class="list">
				<div class="list-item">
					<img class="list-img" src="${ctxStatic}img/iconnew.png" alt="" />
					<div class="title">
						${item.title }
					</div>
				</div>
			</div>
			</a>
		</c:forEach>
		
	</div>
</body>
</html>
		