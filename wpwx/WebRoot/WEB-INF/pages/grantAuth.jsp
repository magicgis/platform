<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>加载中...</title>
</head>
<body>
	
	
	
	<script type="text/javascript">
		// https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
		var appid = '${property.appId }',
			 redirect_uri = '${property.redirectUri }' ;
		var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' + appid
				+ '&redirect_uri=' + redirect_uri
				+ '&response_type=code&scope=snsapi_base&state=' + Math.random() + '#wechat_redirect';
		
		window.location.href = url;
	</script>
</body>
</html>