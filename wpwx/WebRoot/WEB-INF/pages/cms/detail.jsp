<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
		<title>知识详情</title>
		<link rel="stylesheet" href="${ctxStatic}css/normalize.css" />
		<link rel="stylesheet" href="${ctxStatic}css/common.css" />
		<style>
			.title{
				font-size:20px;
				margin-top: 10px;
				padding: 0 10px;
			}
			.desc{
				padding: 10px;
			}
			.topbar{
				background: url(${ctxStatic}img/logo.png) no-repeat 20px center;
				background-size:86px;
				height: 35px;
				border-bottom: 1px solid #F1F1F1;
			}
		</style>
	</head>

	<body>
		<div id="root">
			<!-- <div class="topbar"></div> -->
			<div class="title">
				${article.title}
			</div>
		<div class="desc">
			${article.content}
		</div>
	</div>

	</body>

</html>