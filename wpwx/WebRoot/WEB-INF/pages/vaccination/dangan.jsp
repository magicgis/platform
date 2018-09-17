<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!doctype html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>儿童智慧接种</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link href="${ctxStatic }css/mui.min.css" rel="stylesheet" />
		<style type="text/css">
			*{
				padding: 0px;
				margin: 0px;
			}
			#banner{
				background: yellow;
			}
			span.name{
				font-size: .15rem;
    			padding-right: .05rem;
    			padding-left: .2rem;
    			color:#000;
			}
			span.age{
				float: right;
				color:#999;
				margin-right: .05rem;
				font-size: .14rem;
			}
			.people-info{
				margin-top: 0px;
			    background: lightgray;
			    /* padding: .1rem .2rem; */
			    /* height: 43px; */
			    height: .4rem;
			    line-height: .4rem;
			}
			#item-list{
				display: -webkit-flex;
				display: flex;
				justify-content: flex-start;
				flex-wrap: wrap;
				margin-top: 30px;
				margin-left: 30px;
				
			}
			.wrap {
				height:.4rem;
				border-bottom:1px solid #cac7c7;
				box-sizing:border-box;
			}
			.item{
				display: -webkit-flex;
				display: flex;
				flex-direction: column;
				align-items: center;
				/*background: red;*/
				min-width: 100px;
				margin: 15px 0px;
			}
			.item img{
				height: 50px;
			}
			.item span{
				color: gray;
				font-size: 13px;
			}
			.triangle {
				position: absolute;
			    top: 50%;
			    transform: translateY(-50%);
			}
			
			.triangle-up {    
				width:0;    
				height:0;      
				border-left:.08rem solid transparent;   
				border-right:.08rem solid transparent;   
				border-bottom:.08rem solid #64d5d7;
				}
				
			.triangle-down {   
				width:0;   
				height:0;   
				border-left:.08rem solid transparent;   
				border-right:.08rem solid transparent;   
				border-top:.08rem solid #64d5d7;

				}
				
			.childinfo{
				position: relative;
				display: inline-block; 
				float: left; 
				width: 85%
				}
			
			.childinfo-detail{
				display: inline-block; 
				float: left; 
				/* border-top:1px #808080 solid;
				border-bottom:1px #808080 solid; */
				width: 15%;
				height:.4rem;
				
				text-align: center;
				background:url(${ctxStatic}img/child-icon.png) no-repeat center;
				background-color: #64d5d7;
				background-size: .3rem;
				}
			.child-list {
				overflow:hidden;
				list-style-type:none;
				padding: .3rem .2rem;
			}
			.child-list li {
				float:left;
				text-align:center;
			}
			.child-list li img {
				width:.8rem;
				height:.8rem;
			}
			.child-list li span {
				display:block;
				font-size: .14rem;
			}
			.col-xs-4 {
			    width: 33.33333333%;
			}
		</style>
	</head>

	<body>
		<%-- <div id="banner">
			<img src="${ctxStatic }image/aaaa.png" style="height: 120px; width: 100%;">
		</div> --%>
		<div class="banner">
			<img src="${ctxStatic}images/banner_home2.jpg" alt="">
		</div>
		<div class="childinfowrap">
		<c:forEach items="${infos}" var="info">
			<div class="wrap clearfix">
				<div class="people-info childinfo" data-code="${info.childcode }">
					<img src=""/>
					<span class="name">
						${info.childname}
					</span>
					<span class="triangle-down triangle" ></span>
					<span class="age">${info.year}</span>
				</div>
				<div class="childinfo-detail">
					<%-- <img alt="儿童信息" src="${ctxStatic}img/icon-index-detail-2-1.jpg"> --%>
				</div>
			</div>
		</c:forEach>
		</div>

		<input id="childcode" type="hidden" value="">
		<ul class="child-list">
			<li class="col-xs-4" data-url="${ctx}child/record/1.do">
				<img src="${ctxStatic }image/show1.png"/>
				<span>已种疫苗</span>
			</li>
			<li class="col-xs-4" data-url="${ctx}child/record/0.do">
				<img src="${ctxStatic }image/show2.png"/>
				<span>未种疫苗</span>
			</li>
			<li class="col-xs-4" data-url="${ctx}vac/vaccList.do">
				<img src="${ctxStatic }image/show3.png"/>
				<span>预约记录</span>
			</li>
		</ul>
		<%-- <div id="item-list">
			 <div class="item" data-url="${ctx}vac/select.do">
				<img src="${ctxStatic }image/reserve.png"/>
				<span>预约</span>
			</div>
			<div class="item" data-url="${ctx}child/record/1.do">
				<img src="${ctxStatic }image/record1.png"/>
				<span>已种疫苗</span>
			</div>
			 <div class="item" data-url="${ctx}child/record/0.do">
				<img src="${ctxStatic }image/record0.png"/>
				<span>未种疫苗</span>
			</div>
 			 <div class="item" data-url="${ctx}vac/vaccList.do">
				<img src="${ctxStatic }image/orderlist.png"/>
				<span>预约记录</span>
			</div>
			<div class="item" data-url="${ctx}child/info/form.do">
				<img src="${ctxStatic }image/attention.png"/>
				<span>关注宝宝</span>
			</div> 
			<div class="item" data-url="${ctx}child/temp/form.do">
				<img src="${ctxStatic }image/self.png"/>
				<span>自助建档</span>
			</div> 
		</div>  --%>
	</body>
	<script src="${ctxStatic}js/common.js"></script>
	<script type="text/javascript">
	
	$(function(){
	    var str="${infos}";
		var isopen = true;
		
		//设置默认儿童
		var cookiecode = getCookie('code');
		/* $(".childinfowrap :first").siblings().hide(); */
		
		if(cookiecode){
			 $(".childinfo").each(function(){
			 	if(cookiecode == $(this).attr("data-code")){
			 		$(this).parent().show();
			 		$(this).parent().siblings().hide();
			 		$("#childcode").val(cookiecode);
			 		isopen = false;
			 		/* $(this).click(); */
			 	}
			 });
		}else{
			$("#childcode").val($(".childinfowrap div :first").attr("data-code"));
		}
		
		//点击切换儿童
		$(".childinfo").click(function(){
			if(!isopen){
				$(this).parent().siblings().stop(true, false).slideDown();
				isopen = true;
				//去除小三角
				$(".childinfo").each(function(){
					$(".triangle").removeClass("triangle-down");
					$(".triangle").addClass("triangle-up");
				});
			}else{
				$(this).parent().siblings().stop(true, false).slideUp();
				isopen = false;
				$("#childcode").val($(this).attr("data-code"));
				$(".triangle").addClass("triangle-down");
				$(".triangle").removeClass("triangle-up");
				//保存cookies
				setCookie("code",$(this).attr("data-code"),"d7");
			}
		});
		
		$(".child-list li").click(function(){
			//如果有data-url属性就跳转
			if($(this).attr("data-url")){
				window.location.href=$(this).attr("data-url") + "?code=" + $("#childcode").val();
			}
		});
		
		$(".childinfo-detail").click(function(){
			var childcode = $(this).prev().attr("data-code"); 
			/* alert(childcode); */
			window.location.href="${ctx}child/baseinfo/" + childcode + ".do";
		});
		

	})
	</script>
</html>