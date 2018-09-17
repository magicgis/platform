<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
		<title>疫苗知识</title>
		<link rel="stylesheet" href="${ctxStatic}css/normalize.css" />
		<style>
			.title{
				font-size:20px;
				padding: 10px;
				border-bottom: 1px solid #F1F1F1;
			}
			.panel{
				border-bottom: 1px solid #F1F1F1;
				padding: 10px;
			}
			.panel-title{
				line-height: 30px;
			}
			.panel-title>img{
				vertical-align: top;
				height: 30px;
				width: 30px;
			}
			.panel-desc{
				margin-top: 10px;
			}
		</style>
	</head>

	<body>
		<div id="root">
			<div class="banner">
				<img src="${ctxStatic}img/banner_demo.jpg" alt="" />
			</div>
			<div class="title">${info.name}</div>
			<div class="panel">
				<div class="panel-title">
					<img src="${ctxStatic}img/list_img06.png" alt="" />
					预防疾病
				</div>
				<div class="panel-desc color-grey">
					${info.prevent }
				</div>
			</div>
			<div class="panel">
				<div class="panel-title">
					<img src="${ctxStatic}img/list_img07.png" alt="" />
					接种前注意事项
				</div>
				<div class="panel-desc color-grey">
					${info.before }
				</div>
			</div>
			<div class="panel">
				<div class="panel-title">
					<img src="${ctxStatic}img/list_img08.png" alt="" />
					接种后注意事项
				</div>
				<div class="panel-desc color-grey">
					${info.after}
				</div>
			</div>
		</div>

	</body>

</html>