<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" 
			+ request.getServerPort()+path;
request.setAttribute("path", path);
request.setAttribute("basePath", basePath);
%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="format-detection" content="telephone=no">
	<meta name="msapplication-tap-highlight" content="no">
	<meta name="viewport" content="user-scalable=no, initial-scale=1, width=device-width">
	<title>智慧育苗</title>
	
	<link rel="stylesheet" href="<%=basePath%>/css/data-flex.min.css" charset="utf-8">
	<script src="<%=basePath%>/js/zepto.min.js" type="text/javascript"></script>
	<script type="text/javascript">
	    var rootPath = "<%=basePath%>";
	</script>
</head>
<body>
  <h1>智慧育苗接种站</h1>

</body>

</html>