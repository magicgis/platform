<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" href="${ctxStatic}css/vaccination.css">
	<title>宝宝档案信息</title>
	
	<style type="text/css">
		.cancalAtten{
			height: 35px;
			line-height: 35px;
			width: 100%;
			background: red;
			border-radius: 5px;
			border: 0;
			color: #fff;
			font-size: 14px;
			margin-top: 10px;
			margin-bottom: 10px;
		}
	</style>
</head>
<body>
	<div class="wrap" style="background: #f9f8f8;margin:10px;">
		<c:choose>
			<c:when test="${not empty type}">
				<div class="cell">
					<div class="strong">儿童编号</div>
					<div><span class="strong"> ${temp.childcode}</span></div>
				</div>
				
			</c:when>
			<c:otherwise>
				<div class="cell">
					<div class="strong" style="color: #f48093;border-right:1px solid #f48093;" >建档编码</div>
					<div><span class="strong" style="color: #f48093"> ${temp.childcode}</span></div>
				</div>
			</c:otherwise>
		</c:choose>
		
		<div class="cell">
			<div class="strong">儿童姓名11</div>
			<div class="strong">${temp.childname}</div>
		</div>
		<div class="cell" style="border-bottom: 1px solid #dddddd;">
			<div class="strong">儿童身份证</div>
			<div class="strong">${temp.cardcode}</div>
		</div>
		<div class="cell">
			<div class="strong">出生证号</div>
			<div class="strong">${temp.birthcode}</div>
		</div>
		<div class="cell">
			<div class="strong">儿童性别</div>
			<div class="strong">
				<c:choose>
					<c:when test="${temp.gender == '1'}">男性</c:when>
					<c:when test="${temp.gender == '2'}">女性</c:when>
				</c:choose>
			</div>
		</div>
		<div class="cell">
			<div class="strong">母亲姓名</div>
			<div class="strong">${temp.guardianname}</div>
		</div>
		<div class="cell">
			<div class="strong">母亲身份证</div>
			<div class="strong">${temp.guardianidentificationnumber}</div>
		</div>
		<div class="cell"style="border-bottom: 1px solid #dddddd;">
			<div class="strong">母亲电话</div>
			<div class="strong">${temp.guardianmobile}</div>
		</div>
		
		<div class="cell">
			<div class="strong">父亲姓名</div>
			<div class="strong">${temp.father}</div>
		</div>
		<div class="cell">
			<div class="strong">父亲身份证</div>
			<div class="strong">${temp.fathercard}</div>
		</div>
		<div class="cell" style="border-bottom: 1px solid #dddddd;">
			<div class="strong">父亲电话</div>
			<div class="strong">${temp.fatherphone}</div>
		</div>
		<div class="cell">
			<div class="strong">胎次</div>
			<div class="strong">${temp.childorder}</div>
		</div>
		<div class="cell">
			<div class="strong">出生医院名称</div>
			<div class="strong">${temp.birthhostipal}</div>
		</div>
		<div class="cell">
			<div class="strong">出生日期</div>
			<div class="strong"><fmt:formatDate value="${temp.birthday}" pattern="yyyy-MM-dd" /></div>
		</div>
		<div class="cell" style="border-bottom: 1px solid #dddddd;">
			<div class="strong">民族</div>
			<div class="strong">${temp.nation}</div>
		</div>
		<div class="cell">
			<div class="strong">体重(克)</div>
			<div class="strong">${temp.birthweight}</div>
		</div>
	<%--    <div class="cell">
			<p class="strong">区域划分</div>
			<div>${temp.area}</div>
		</div>--%>
		<div class="cell">
			<div class="strong">户口类别</div>
			<div class="strong">${temp.properties}</div>
		</div>
			<div class="cell">
			<div class="strong">居住类别</div>
			<div class="strong">${temp.reside}</div>
		</div>
	
		<div class="cell">
			<div class="strong">家庭地址</div>
			<div class="strong">${temp.homeaddress}</div>
		</div>
		<div class="cell">
			<div class="strong">户籍地址</div>
			<div class="strong">${temp.registryaddress}</div>
		</div>
		
		<c:if test="${not empty type}">
			<div class="cell">
				<button type="button" class="cancalAtten">取消关注</button>
			</div>
		</c:if>
	
	</div>
	<script src="${ctxStatic}js/common.js"></script>
	<script type="text/javascript">
		$(function(){
			$(".cancalAtten").click(function(){
				var winHeight = window.innerHeight/2-70;
				layer.confirm('确认取消关注?', {icon: 3, title:'取消关注', offset: winHeight}, function(index){	
					$.ajax({
						url:"${ctx}child/info/cancelAtten.do?",
						data:{"childcode":"${temp.childcode}"},
						success:function(data){
							if(data == '200'){
								layer.msg("取消关注成功",{icon:1, time: 1500});
							}else{
								layer.msg("取消关注失败",{icon:0, time: 1500});
							}
							setTimeout(function reflush(){
											window.location.href="${ctx}vac/index.do";
										},1600);
						}
					});	 
// 					window.location.href = "${ctx}child/info/cancelAtten.do?childcode=${temp.childcode}"
			});
		})
		});
	</script>
</body>
</html>