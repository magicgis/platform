<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<title>疫苗接种列表</title>
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
			padding: 15px;
		}
		.vacc-title{
			font-size: 1.3em;
			margin-top:0;
			margin-bottom: 8px;
			color:#565555;
		}
		.pin-tips{
			height: 60px;
			padding: 5px;
			position: relative;
		}
		.pin-tips:hover{
			background: #F8FAFC;
			border-radius: 4px;
		}
		.vacc-name , .pin-date{
			position: absolute;
			top: 5px;
			line-height: 25px;
			color: #93999F;
		}
		.vacc-name{
			left: 5px;
			font-size: 1.1em;
		}
		.pin-date{
			right: 5px;
		}
		.sign-status , .reserve-status{
			position: absolute;
			top: 32px;
			line-height: 28px;
			padding: 0 6px;
			border: none;
			border-radius: 16px;
			color: #fff;
			background: #64d5d7;
		}
		.sign-status{
			right: 5px;
		}
		.reserve-status{
			right: 80px;
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
       /* history.pushState(null, null, document.URL);
        window.addEventListener('popstate', function () {
            history.pushState(null, null, document.URL);
        });*/
        //签字
        function stockVal(val,vaccId,localcode) {
	        window.location.href="${ctx}vac/sign.do?id="+val+"&vaccId="+vaccId+"&localcode="+localcode;

		}//
        function bespeak(val,nid) {
            window.location.href="${ctx}vac/insList.do?id="+ ${l.child }+"&nid="+nid;
        }
		//预约
        function reservaTion(val,childcode,localcode) {
            window.location.href="${ctx}vac/reservaTion.do?id="+val+"&childcode="+childcode+"&localcode="+localcode;

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
					<p class="vacc-title">${l.child }</p>
					<c:forEach items="${l.templist }" var="ll">
						<div class="pin-tips" >
							<span class="vacc-name">${ll.vaccname}</span>
							<span class="pin-date"><fmt:formatDate value="${ll.remindDate}" pattern="yyyy-MM-dd" /></span>
                            <c:if test="${ll.sign == 0 }">
                                <span class="sign-status"  onclick="stockVal('${ll.id}','${ll.vaccId}','${ll.localcode}')">点击签字</span>
                            </c:if>
                            <c:if test="${ll.sign == 1 }">
                                <span class="sign-status" style="background: #ccc;" onclick="stockVal('${ll.id}','${ll.vaccId}','${ll.localcode}')">查看签字</span>
                            </c:if>
                            <c:if test="${ll.selectTime == null }">
								<span class="reserve-status" onclick="reservaTion('${ll.id}','${ll.childcode}','${ll.localcode}')">在线预约</span>
                            </c:if>
                            <c:if test="${ll.selectTime != ''&& ll.selectTime !=null}">
								<span class="reserve-status" style="background: #ccc;" onclick="reservaTion('${ll.id}','${ll.childcode}','${ll.localcode}')">修改预约</span>
                            </c:if>
								<%--
                                <c:if test="${ll.insuranceId == null }">
                                   <span style="position:absolute;right: 90px; top: 16px; line-height: 32px;padding: 0 10px;border: none;border-radius: 16px;color: #fff;background: #64d5d7;"
                                         onclick="bespeak('${ll.id}','${ll.vaccId}','${ll.localcode}')">购买保险</span>
                                </c:if>
                                <c:if test="${ll.insuranceId != ''&& ll.insuranceId !=null}">
                                    <span style="position:absolute;right: 90px; top: 16px; line-height: 32px;padding: 0 10px;border: none;border-radius: 16px;color: #fff;background:  #64d5d7;"
                                          onclick="bespeak('${ll.id}','${ll.vaccId}','${ll.localcode}')">查看保险</span>
                                </c:if>--%>

						</div>

					</c:forEach>
				</div>
			</c:forEach>
		</c:if>
	</div>
</body>
</html>