<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!doctype html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>预约记录</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link href="${ctxStatic}css/mui.min.css" rel="stylesheet" />
		<style type="text/css">
			* {
				padding: 0px;
				margin: 0px;
			}
			body{
				background: white;
			}
			#banner {
				background: yellow;
			}
			
			#titleName {
				text-align: center;
				font-size: 15px;
				border-top: 1px solid lightgrey;
				background: white;
			}
			
			#record {
				padding: 5px;
				border-top: 1px solid lightgrey;
				border-bottom: 1px solid lightgrey;
				background: white;
				display: flex;
			}
			.item-left{
				display: inline-block;
				width: 40%;
				/*background: yellow;*/
				/*position: absolute;*/
			}
			.item-left p{
				color: black;
			}
			.item-right{
				/*float: right;*/
				display: inline-block;
				width: 50%;
				position: relative;
				/*background: green;*/
			}
			.item-right ul{
				list-style: none;
				/*text-align: center;*/
			}
			 .title-left{
				/*background: yellow;*/
				display: inline-block;
				width: 40%;
			}
			 .title-right{
				/*background: blue;*/
				display: inline-block;
				width: 50%;
				text-align: right;
			}		
			
		</style>
	</head>

	<body>

		<div id="banner">
			<img src="${ctxStatic}image/aaaa.png"  style="height: 100px; width: 100%;">
		</div>
		<div id="titleName">
			预约记录
		</div>
		<c:forEach items="${list}" var="l">
			<div id="record">
				${l.orderno }<br>
				${l.productname }<br/>
				${l.payprice}<br/>
				<fmt:formatDate value="${l.ordertime}" pattern="yyyy-MM-dd HH:mm:ss" />
			</div>
		</c:forEach>
	</body>

</html>














