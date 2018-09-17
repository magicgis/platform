<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
		<title>疫苗知识列表</title>
		<link rel="stylesheet" href="${ctxStatic}css/normalize.css" />
		<link rel="stylesheet" href="${ctxStatic}css/knowledge-list-of-the-vaccine.css" />
	</head>

	<body>
		<div id="root">
			<div class="banner">
				<img src="${ctxStatic}img/banner_demo.jpg" alt="" />
			</div>
			<div class="list">
			<div>
				<c:forEach items="${list}" var="item">
					<div class="list-title">${item[0].time}</div>
					<c:forEach items="${item}" var="i">
					<a class="lista" href="${ctx}know/vaccinate/info/${i.infoid}.do">	
					<div class="list-item">
						<img src="${ctxStatic}img/icon_list_01.png" alt="" class="list-head-img">
						<div class="list-infos">
							<div class="title">
								${i.title } <span class="fr color-grey">${i.dose}</span>
							</div>
							<div class="desc color-grey">${i.desc}</div>
						</div>
					</div>
					</a>
					</c:forEach>
				</c:forEach>
				</div>
			</div>
		</div>
	</body>

</html>