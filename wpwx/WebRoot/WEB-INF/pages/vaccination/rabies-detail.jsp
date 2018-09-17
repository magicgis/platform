<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	
	<style type="text/css">
		#qrcode{
			width: .7rem;
			height: .7rem;
			margin: .2rem auto;
			display: block;
			position: absolute;
			top: 0;
			right: .2rem;
		}
		#qrcode canvas {
			width:100%;
			height:100%;
		}
	</style>
	<title>犬伤建档信息</title>
	<script type="text/javascript" src="${ctxStatic}js/jquery.qrcode.min.js"></script>
	<script type="text/javascript">
        $(function(){
		$("#qrcode").qrcode("${temp.id}",{"width":40});
            /* $("#qrcode").qrcode({
                render: "canvas",
                text: "${temp.id}",
                width: 140,
                height: 140
            }); */
        }) 
	</script>
</head>
<body class="wrap" style="background: #f9f8f8;margin:10px;">
<div id="qrcode" style="border:0!important"></div>
<div class="cell">
	<div class="strong" style="color: #f48093;border-right:1px solid #f48093;" >建档编码</div>
	<div><span class="strong" style="color: #f48093"> ${temp.id}</span></div>
</div>
<div class="cell">
	<div class="strong">姓名</div>
	<div class="strong">${temp.username}</div>
</div>
<div class="cell">
	<div class="strong">身份证号</div>
	<div class="strong">340122199401147672</div>
</div>

<div class="cell">
	<div class="strong">性别</div>
	<div class="strong"> <c:choose>
		<c:when test="${temp.sex == '1'}">男</c:when>
		<c:when test="${temp.sex == '2'}">女</c:when>
	</c:choose>
	</div>
</div>
<div class="cell">
	<div class="strong">联系电话</div>
	<div class="strong">${temp.linkphone}</div>
</div>

<div class="cell">
	<div class="strong">咬伤时间</div>
	<div class="strong"><fmt:formatDate value="${temp.bitedate}" pattern="yyyy-MM-dd HH:mm" /></div>
</div>
<div class="cell">
	<div class="strong">动物名称</div>
	<div class="strong">${temp.animal}</div>
</div>
<div class="cell">
	<div class="strong">受伤方式</div>
	<div class="strong">${temp.bitetype}</div>
</div>
<div class="cell">
	<div class="strong">咬伤部位</div>
	<div class="strong">${temp.bitepart}</div>
</div>

<div class="cell">
	<div class="strong">处理时间</div>
	<div class="strong"><fmt:formatDate value="${temp.dealdate}" pattern="yyyy-MM-dd HH:mm" /></div>
</div>

<div class="cell">
	<div class="strong">处理地点</div>
	<div class="strong">${temp.dealaddress}</div>
</div>
<div class="cell">
	<div class="strong">体重</div>
	<div class="strong">${temp.weight}kg</div>
</div>

<%-- <div class="cell">
    <p class="strong">居住地址</p>
    <p>${temp.homeaddress}</p>
</div> --%>



</body>
<script src="${ctxStatic}js/common.js"></script>
</html>