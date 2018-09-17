<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<title>疫苗接种列表</title>
	<link rel="stylesheet" href="${ctxStatic}css/style.css">
	<style>
		*{font-family: "微软雅黑"!important;}
		body{
			width: 100%;
			font-size: 14px;
			background: #fafafa;
			margin: 0;
			padding: 0;
		}
		.bodybg{
			padding: 15px;
		}
		.formbg{
			background: #FFFFFF;
			border-radius: 6px;
			box-shadow: 0 4px 6px #eaeaea;
			margin-bottom: 20px;
			padding: 16px;
		}
		.vacc-title{
			font-size: 1.4em;
			margin-top:0;
			margin-bottom: 10px;
		}
		.pin-tips{
			height: 52px;
			padding: 8px 10px;
			position: relative;
		}
		.pin-tips:hover{
			background: #F8FAFC;
   			border-radius: 4px;
		}
		.pin-date{
			position: absolute;
			left: 62px;
			top: 10px;
			line-height: 32px;
			font-size: 1.2em;
			color: #93999F;
		}
		.sign-status{
			position: absolute;
			right: 10px;
			top: 8px;
		    line-height: 32px;
		    padding: 0 10px;
		    border: none;
		    border-radius: 16px;
		    color: #fff;
		    background: #64d5d7;
		}
		.pin-tips img{
			height: 32px;
			width: 32px;
		}
		.nothing-box{
			text-align: center;
			padding-top: 100px;
		}
		.nothing-box p{
			font-size: 1.4em;
			color: #93999F;
			margin-top:20px;
		}
	</style>
	<script type="text/javascript">
		//防止页面后退
        history.pushState(null, null, document.URL);
        window.addEventListener('popstate', function () {
            history.pushState(null, null, document.URL);
        });
        
        function stockVal(val,sign) {
	        window.location.href="${ctx}rabies/sign.do?id=" + val;
		}
		
	</script>
</head>
<body>
	<div class="bodybg">
		<c:if test="${fun:length(list) <= 0}">
			<div class="nothing-box">
				<img src="${ctxStatic}images/nothing.png" />
				<p>暂时还没有疫苗信息哦！</p>
			</div>
		</c:if>
		
		<c:if test="${fun:length(list) > 0}">
			<c:forEach items="${list }" var="l">
				<div class="formbg">
					<p class="vacc-title">${l.ctxvaccname }</p>
					<p>${l.code }</p>
					<c:forEach items="${l.templist }" var="ll">
						<div class="pin-tips" onclick="stockVal('${ll.id}','${ll.sign}')">
							<img src="${ctxStatic}images/pin.png" />
							<span class="pin-date"><fmt:formatDate value="${ll.ctxdate}" pattern="yyyy-MM-dd" /></span>
							<c:if test="${ll.sign == 0 }">
								<span class="sign-status">点击签字</span>
							</c:if>
							<c:if test="${ll.sign == 1 }">
								<span class="sign-status" style="background: #ccc;">查看签字</span>
							</c:if>
						</div>
					</c:forEach>
				</div>
			</c:forEach>
		</c:if>
	</div>
</body>
</html>