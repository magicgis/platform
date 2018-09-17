<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!doctype html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>已种疫苗</title>
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
				width: 26%;
				/*background: yellow;*/
				/*position: absolute;*/
			}
			.item-left p{
				color: black;
			}
			.item-right{
				/*float: right;*/
				display: inline-block;
				width: 74%;
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
			}
			 .title-right{
				/*background: blue;*/
				display: inline-block;
				width: 45%;
				text-align: left;
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
		<c:if test="${fun:length(list) <= 0}">
			暂无数据
		</c:if>
		<c:if test="${fun:length(list) > 0}">
			<c:forEach items="${list }" var="l">
				<div id="record">
				<div class="item-left">
					<p>${l[0].allname}</p>
				</div>
				<div class="item-right">
					<ul>
						<li class="item-title">
							<div class="title-left ">
								<span>针次</span>
							</div>
							<div class="title-center">
								<span>接种时间</span>
							</div>
							<div class=" title-right">
								<span>名称</span>
							</div>
						</li>
						<c:forEach items="${l}" var="ii">
							<li class="item-title">
								<div class="title-left">
									<span>第${ii.dosage}剂</span>
								</div>
								<div class="title-center">
									<span><fmt:formatDate value="${ii.vaccinatedate}" pattern="yyyy-MM-dd" /></span>
								</div>
								<div class="title-right">
									<span>第${ii.vaccname}剂</span>
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			</c:forEach>
		</c:if>
		
		
		
	</body>

</html>