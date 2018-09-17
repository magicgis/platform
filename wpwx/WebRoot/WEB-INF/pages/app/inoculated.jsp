<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>已种疫苗</title>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	</head>
	<style>
		*{ margin: 0; padding: 0; }
		body{ font-family: "微软雅黑"; font-size: 10px; }
		.banner img { width: 100%; }
		.wrap{ padding: 15px; }
		.table{ width: 100%; font-size: 1.4em; text-align: center; border: 1px solid #e6e6e6; border-collapse: collapse; border-spacing: 0; }
		.table th{ color: #666666; line-height: 30px; background: #f3f3f4; }
		.table td{ color: #333333; padding: 4px 6px; }
		.min-w50{ min-width: 50px;}
		.min-w90{ min-width: 90px;}
	</style>
	<body>
		<div class="banner">
			<img src="../../../static/images/inoculated-banner.jpg" />
		</div>
		<div class="wrap">
			<table class="table" border="1">
				<thead>
					<tr>
						<th>名称</th>
						<th class="min-w50">针次</th>
						<th class="min-w90">接种时间</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>71型灭活疫苗</td>
						<td>第1剂</td>
						<td>2017-07-21</td>
					</tr>
					<tr>
						<td>乙肝疫苗(CHO)</td>
						<td>第1剂</td>
						<td>2017-07-21</td>
					</tr>
					<tr>
						<td>乙肝疫苗(酿酒酵母)</td>
						<td>第1剂</td>
						<td>2017-07-21</td>
					</tr>
					<tr>
						<td>乙肝疫苗(汉逊酵母)</td>
						<td>第1剂</td>
						<td>2017-07-21</td>
					</tr>
					<tr>
						<td rowspan="2">轮状病毒疫苗</td>
						<td>第1剂</td>
						<td>2017-07-21</td>
					</tr>
					<tr>
						<td>第2剂</td>
						<td>2017-07-21</td>
					</tr>
					<tr>
						<td rowspan="4">百白破IPV和HIB五联疫苗</td>
						<td>第1剂</td>
						<td>2017-07-21</td>
					</tr>
					<tr>
						<td>第2剂</td>
						<td>2017-07-21</td>
					</tr>
					<tr>
						<td>第3剂</td>
						<td>2017-07-21</td>
					</tr>
					<tr>
						<td>第4剂</td>
						<td>2017-07-21</td>
					</tr>
				</tbody>
			</table>
		</div>
	</body>
</html>
