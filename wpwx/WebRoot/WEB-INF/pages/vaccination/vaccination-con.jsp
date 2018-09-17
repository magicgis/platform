<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<link rel="stylesheet" href="${ctxStatic}css/style.css">
	<title>已种疫苗</title>
</head>
	<style>
		#contentBox {
			 padding: .15rem;
		}
	</style>
<body>
	<div class="banner">
		<img src="${ctxStatic}images/banner2.png" alt="">
	</div>
	<div class="content">
		<div class="container">
			<div id="contentBox"></div>
			<script id="modInformCharge" type="text/html">
				<table>
				<thead style="background: #f3f3f4;">
				<tr>
					<td class="vaccinatio-header">名称</td>
					<td class="vaccinatio-header">针次</td>
					<td class="vaccinatio-header">接种时间</td>
				</tr>
				</thead>
				<tbody>
				{{each data.list as value i}}
					{{each value.pinList as values j}}
					<tr>
						{{if j==0}}
							<td rowspan="{{value.pinList.length}}" class="vaccination-name">{{value.vaccineName}}</td>
						{{/if}}
							<td>第{{values.name}}剂</td>
							<td>{{values.time}}</td>
					</tr>
					{{/each}}
				{{/each}}
				</tbody>
				</table>	
			</script>
		</div>
	</div>
</body>
<script>
$.ajax({
	url:ctx+"static/js/data.json",
    type: 'POST',
    dataType: 'json',
    success:function(data) {
    	var data={
    			"data":data
    	}
    	var html = template('modInformCharge', data);
   		
        document.getElementById('contentBox').innerHTML = html;
    }
})
</script>
<script src="${ctxStatic}js/common.js"></script>
<script src="${ctxStatic}js/template.js"></script>
</html>