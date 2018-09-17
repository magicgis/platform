<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="${ctxStatic }css/style.css">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<title>结果详情</title>
</head>

<script type="text/javascript">
	$(document).ready(function() {
//  		var index = layer.load();
		setTimeout(function(){
			window.location.href="${ctx}/vac/index.do"
		}, 2000);
	});
</script>

<body style="background: #e8e8e8;">
	<div class="loading">
		<div class="loading-img">
			<img src="${ctxStatic }images/loading.png" alt="">
		</div>
		<p class="blue">订单处理中......</p>
		<p class="gray">订单已提交，请耐心等待结果</p>
	</div>
</body>
</html>