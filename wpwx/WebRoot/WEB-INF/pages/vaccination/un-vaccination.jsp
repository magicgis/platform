<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html >
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<link rel="stylesheet" href="${ctxStatic}css/style.css">
	<title>未种疫苗</title>
</head>
	<style>
		.select-title {
			height: .3rem;
		    line-height: .3rem;
		    background: #a7d25e;
		    color: #fff;
		    font-size: .15rem;
		}
		.select-ul {
			text-align: left;
    		padding: .1rem .2rem;
		}
		
		.select-ul li {
			line-height:.3rem;
			background: url(/wpwx/static/images/select-icon.png) no-repeat right;
    		background-size: .2rem;
		}
		
		.select-ul li span {
			display: inline-block;
		    background: #f7ab00;
		    padding: 0;
		    margin: 0;
		    height: .2rem;
		    line-height: .2rem;
		    padding: 0 .05rem;
		    color: #fff;
		    border-radius: 3px;
		    margin-right: .05rem;
		}
		#contentBox {
			padding:.15rem;
		}
	</style>
<body>
	<div class="banner">
		<img src="${ctxStatic}images/banner3.jpg" alt="">
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
					<td class="vaccinatio-header">应中月龄</td>
					<td class="vaccinatio-header">说明</td>
				</tr>
				</thead>
				<tbody>
				{{each data.list as value i}}
					{{each value.pinList as values j}}
					<tr>
						{{if value.typeFlag==1}}
							{{if j==0}}
								<td rowspan="{{value.pinList.length}}" class="vaccination-name">{{value.vaccineName}}</td>
							{{/if}}
							<td>第{{values.name}}剂</td>
							<td>{{values.time}}</td>
							<td>{{values.desp}}</td>

						{{else}}
							{{if j==0}}
								<td rowspan="{{value.pinList.length+1}}" class="vaccination-name">{{value.vaccineName}}</td>
							{{/if}}
							<td>第{{values.name}}剂</td>
							<td>{{values.time}}</td>
							{{if values.type==2}}
							<td style="color:#f9c46b">{{values.desp}}</td>
							{{else}}
							<td>{{values.desp}}</td>
							{{/if}}
						{{/if}}
					</tr>
					{{/each}}
					{{if value.typeFlag==2}}
						<tr>
							<td colspan='3'>
								<div class="select-title">可选疫苗</div>
								<ul class="select-ul">
									{{each value.vaccineType as val j}}
									<li>
										<p>{{val.vaccineTypeName}}</p>
										{{each val.desps as vals j}}
										<span>{{vals.intru}}</span>
										{{/each}}
									</li>
									{{/each}}
								</ul>
							</td>
						</tr>
					{{/if}}
				{{/each}}
				</tbody>
				</table>	
			</script>
			</div>
		</div>
	</div>
</body>
<script>
$.ajax({
	url:ctx+"static/js/data2.json",
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