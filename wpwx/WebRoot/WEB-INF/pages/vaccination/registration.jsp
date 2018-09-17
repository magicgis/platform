<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<link rel="stylesheet" href="${ctxStatic}css/style.css">
	<title>预约登记</title>
	<style type="text/css">
		.refund{
			text-align: right;
			font-size: 14px !important;
		    float: right;
		    position: relative;
		    top: -33px;
		    height: 30px;
		    padding-left: 5px;
		    padding-right: 5px;
		}
		.refundBtn {
			background: #00AFEF;
			 border: 0; 
			color: #fff;
			border-radius: 2px;
		}
	</style>
</head>
<body>
	<div class="banner">
		<img src="${ctxStatic}images/banner01.png" alt="">
	</div>
	<div class="content">
		<div class="container">
			<h1 class="reg-title">预约记录</h1>
				<c:forEach items="${list}" var="l">
					<div class="reg-record">
						<p class="p1">${l.productname }</p>
						<p class="p2">￥${l.payprice*0.01}</p>
						<p class="p3"><fmt:formatDate value="${l.ordertime}" pattern="yyyy-MM-dd HH:mm" /></p>
						<c:choose>
							<c:when test="${l.status == '0' && empty l.orderStatus}">
								<button type="button" class="refund refundBtn"  data-id="${l.id}">关闭订单</button>
							</c:when>
							<c:when test="${l.status == '1' && empty l.orderStatus }">
								<button type="button" class="refund refundBtn" data-id="${l.id}">取消并退款</button>
							</c:when>
							<c:when test="${l.status == '2' }">
								<p class="refund">预约已取消</p>
							</c:when>
							<c:when test="${l.status == '3' }">
								<p class="refund">预约已关闭</p>
							</c:when>
							<c:when test="${l.orderStatus == '1' }">
								<p class="refund">接种完成</p>
							</c:when>
							<c:when test="${l.orderStatus == '0' }">
								<p class="refund">已出票</p>
							</c:when>
						</c:choose>
					</div>
				</c:forEach>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function(){
			$(".refundBtn").click(function(){
				var oid = $(this).attr("data-id");
				var winHeight = window.innerHeight/2-70;
				layer.confirm('确认取消?', {icon: 3, title:'取消订单', offset: winHeight}, function(index){		 
					var index = layer.load();
					$.ajax({
						url:"${ctx}order/refund.do",
						type:"POST",
						data:{'childcode':'${childcode}','id':oid},
						success:function(data){
							if(data){
								layer.close(index);
								if(data.code == 201){
									layer.msg(data.msg,{icon:1, time: 1500});
								}else if(data.code == 200){
									layer.msg(data.msg,{icon:1, time: 1500});
								}else{
									layer.msg(data.msg,{icon:0, time: 1500});
								}
								
								setTimeout(function reflush(){
											window.location.href="${ctx}order/list.do?code=${childcode}";
										},1500);
							}
						},
						error:function(){
							layer.msg("请求失败，请稍后再试",{icon:0, time: 2000});
						},
					});
				  
				  layer.close(index);
				});
			});
		})
	</script>
</body>
</html>