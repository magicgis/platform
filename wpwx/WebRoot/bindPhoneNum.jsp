<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()
			+ "://" + request.getServerName() + ":" 
			+ request.getServerPort()+path;
request.setAttribute("path", path);
request.setAttribute("basePath", basePath);
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="format-detection" content="telephone=no">
    <meta name="msapplication-tap-highlight" content="no">
    <meta name="msapplication-tap-highlight" content="no">
    <meta name="viewport" content="user-scalable=no, initial-scale=1, width=device-width">
    <title>绑定手机号</title>
    <link rel="stylesheet" href="./css/weui.css" charset="utf-8">
    <link rel="stylesheet" href="./css/bindNum.css" charset="utf-8">
</head>

<body>
    <div id="wrap" style="padding-top:44px;">
        <div class="topNav">
            <div id="goIndex" class="nav-btn-back"></div>
            绑定手机号
        </div>
        <div class="img"><img alt="" src="./img/bindNum.png"></div>
        <div class="weui_cells weui_cells_form">
            <div class="weui_cell">
                <div class="weui_cell_hd"><label class="weui_label">手机号</label></div>
                <div class="weui_cell_bd weui_cell_primary">
                    <input class="weui_input" name="mobile" type="number" pattern="[0-9]*" placeholder="请输入手机号">
                </div>
            </div>
            <div class="weui_cell">
                <div class="weui_cell_hd"><label class="weui_label">验证码</label></div>
                <div class="weui_cell_bd weui_cell_primary">
                    <input class="weui_input" name="password" type="text" placeholder="请输入验证码" />
                </div>
                <div class="getCord">获取验证码</div>
                <div class="getCord2">获取验证码</div>
            </div>
        </div>
        <div class="weui_dialog_alert" style="display: none;">
            <div class="weui_mask"></div>
            <div class="weui_dialog">
                <div class="weui_dialog_hd">
                    <strong class="weui_dialog_title">信息</strong>
                </div>
                <div class="weui_dialog_bd"></div>
                <div class="weui_dialog_ft">
                    <a href="javascript:;" class="weui_btn_dialog primary">确定</a>
                </div>
            </div>
        </div>
        <div class="weui_btn_area">
            <a id="submit" class="weui_btn weui_btn_primary" href="javascript:void(0);">绑定手机号</a>
        </div>

        <div class="info">
            为了您能更好的使用《一块过》的服务，我们需要对您的身份进行验证。
        </div>

        <div class="info" style="color:#04be02;">
            记得扫描下面的二维码关注我们哦~
        </div>

        <div style="text-align:center;">
            <img src="<%=basePath%>/img/wx_qrcode.jpg" alt="">
        </div>
    </div>
    <script src="<%=basePath%>/libs/zepto.js"></script>
    <script src="<%=basePath%>/libs/RegExps.js"></script>
    <script>
        <%
            String onlineUser = (String) request.getSession().getAttribute("wpwx.onlineUser");
            String marketType = (String) request.getSession().getAttribute("wpwx.marketType");
            String portalId = (String) request.getSession().getAttribute("wpwx.portalId");
        %>
        var rootPath = "<%=basePath%>";
        var onlineUser = "<%=onlineUser%>";
        var marketType = "<%=marketType%>";
        var portalId = "<%=portalId%>";
    </script>
    <script src="<%=basePath%>/bindPhoneNum.js"></script>
</body>

</html>