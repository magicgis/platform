<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/pages/include/head.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
    <title>疫苗知识</title>
    <style>
        *{  margin: 0; padding: 0;}
        body{ font-family: "微软雅黑"; font-size: 10px; background: #f9f9f8;}
        .wrap{ padding: 20px 15px 15px 15px;}
        .shadow-bg{
            box-shadow: 0px 12px 30px -8px #CCCCCC;
        }
        .title-box{
            height: 50px;
            background: #64d5d7;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
            border-bottom: 8px solid #3fbcbe;
        }
        .disease-name{
            font-size: 2.3em;
            color: #008689;
            line-height: 58px;
            padding-left: 15px;
        }
        .ctx-box{
            padding: 10px 15px;
            background: #ffffff;
            border-bottom-left-radius: 10px;
            border-bottom-right-radius: 10px;
        }
        .contxt{ color: #333333; font-size: 1.5em; line-height: 1.5em;}
    </style>
</head>
<body>
<div class="wrap">
    <div class="shadow-bg">
        <div class="title-box">
            <p class="disease-name">
                ${article.name}
            </p>
        </div>
        <div class="ctx-box">
            <p class="contxt">
                ${article.before}${article.after}
            </p>
        </div>
    </div>
</div>
</body>

</html>