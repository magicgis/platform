<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付跳转中...</title>
</head>
<body>
<script type="text/javascript">
    //debugger;
    callpay();
    function jsApiCall() {
        var data = {
            //以下参数的值由BCPayByChannel方法返回来的数据填入即可
            "appId": "${jsapiAppid}",
            "timeStamp": "${timeStamp}",
            "nonceStr": "${nonceStr}",
            "package": "${jsapipackage}",
            "signType": "${signType}",
            "paySign": "${paySign}"
        };
         //alert(JSON.stringify(data));
        WeixinJSBridge.invoke('getBrandWCPayRequest',  data,
                function (res) {
                    //alert(res.err_msg);
                    //alert(JSON.stringify(res));
                   	window.location.href="${ctx}/wpwx/vac/loading.do?paystatus="+JSON.stringify(res);
                    WeixinJSBridge.log(res.err_msg);
                }
        );
    }
    function callpay() {
        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', jsApiCall);
                document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
            }
        } else {
            jsApiCall();
        }
    }
</script>
	


</body>
</html>