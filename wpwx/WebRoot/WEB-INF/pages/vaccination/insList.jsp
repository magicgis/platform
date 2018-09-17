<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	<link rel="stylesheet" href="${ctxStatic}css/style.css">
	<title>选择投保</title>
	
	<script type="text/javascript">

	</script>

</head>
<body>
	<div class="banner">

		<img src="${ctxStatic}images/banner_select.jpg" alt="">

	</div>
	<div class="container">
		<%--<form action="${ctx}api/testIns.do" method="post">--%>
		<form action="${ctx}vac/bylist.do" method="post">

			<input type="hidden" name="childcode" value="152">
			<input type="hidden" name="nid" value="121">
			<input type="hidden" name="openid" value="000">

			<div class="box-vaccination p2">
				 <label for="5"><input type="radio" id="5" class="s" checked=""><i></i></label>
				<span>￥5元<a href="#">《华夏保险公司有关管理条例》</a></span>
			</div>
			
			<div class="box-vaccination">
				<div class="total">总计：<span id="money"> 5元</span></div>
			</div>
			<div class="box-wrap">
				<button type="submit" ></button>
				<a id="12" ></a>
			</div>
		</form>
	</div>
</body>
</html>