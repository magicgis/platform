<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!doctype html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>未种疫苗</title>
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
			
			.wrap{
				padding-left: 10px;
				padding-right: 10px;
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
				width: 60%;
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
				width: 20%;
			}
			
			 .title-center{
				/*background: yellow;*/
				display: inline-block;
				width: 30%;
				text-align: center;
			}
			 .title-right{
				/*background: blue;*/
				display: inline-block;
				width: 45%;
				text-align: center;
				
			}
			ul li:nth-child(1) span{
				color: rgb(94, 184, 1);
				font-size: 14px;
			}
			ul li span{
				font-size: 12px;
				color: #666;
			}
		</style>
	</head>

	<body>

		<div id="banner">
			<img src="${ctxStatic}image/aaaa.png" style="height: 100px; width: 100%;">
		</div>
		<div id="titleName">
			接种内容
		</div>
		<div class="warp">
		<c:if test="${fun:length(list) <= 0}">
			暂无数据
		</c:if>
		<c:if test="${fun:length(list) > 0}">
			<c:forEach items="${list}" var="l">
				<div id="record">
				<div class="item-left">
					<p>${l[0].name}</p>
				</div>
				<div class="item-right">
					<ul>
						<li class="item-title">
							<div class="title-left ">
								<span>针次</span>
							</div>
							<div class="title-center">
								<span>应种月龄</span>
							</div>
							<div class="title-right">
								<span>说明</span>
							</div>
						</li>
						<c:forEach items="${l}" var="ii">
							<li class="item-title">
								<div class="title-left">
									<span>第${ii.pin}剂</span>
								</div>
								<div class="title-center">
									<span>${ii.mouage}</span>
								</div>
								<div class="title-right">
									<span>${monage > ii.mouage ? "可预约":"未到月龄"}</span>
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			</c:forEach>
		</c:if>
		</div>
	</body>

</html>