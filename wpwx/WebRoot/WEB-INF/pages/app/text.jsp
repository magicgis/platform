<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>测试</title>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	</head>
	<body>
		<div class="banner">
			<form action="${ctx}bcdata/save.do" method="post" enctype="multipart/form-data">
				<input type="file" name="file">
				<input type="submit" value="提交">
			</form>
		</div>
	</body>
</html>
