<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	<title>宝宝档案</title>
</head>
<body>
	<div class="container">
		<c:forEach items="${infos }" var="info">
			<!-- <a class="lista" href="##"> -->
				<div class="baby-list c-flex">
					<div class="baby-img"><img src="${ctxStatic}img/content_01.png"alt=""></div>
					<div class="c-flex-1 baby-con">
						<h3>${info.childname}</h3>
						<p>${info.childcode }<br>${info.birthcode }</p>
					</div>
					<!-- <div class="baby-next"></div> -->
				</div>
			<!-- </a> -->
		</c:forEach>

	</div>
	<div class="container">
		<div class="baby-attention">
			<a href="${ctx}child/info/form.do">关注宝宝</a>
		</div>
	</div>
</body>
</html>