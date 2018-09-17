package com.junl.wpwx.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Alipay {
    public static String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK5AdzZf7iqRHM7B3YzCOkBwafWnCKN4aFpWZBNiEtfugy0/pLIF0oNF/FZC/cC6s68HHd0kbHa4x2uKxuyeSOvBIAW3JVV81YOkSY" +
            "9TkgMnCpkCZvn+unGggB9l8t3TXqNlBl0UwB/Ma96I8h7SfoBZMJDd69HHtE9r2EqMvkKvAgMBAAECgYA/X4y7xtncu1gJzjIgTkdXRksTH4OhyFnTeRX+gq2twL4Oh9YDNODiA2MJkGWvYXeWEXWIBwY3i25jq147kjmcgsfJSKsB" +
            "uEWEgKqClSp7A6VSUf75XYeZQsCu4eBdyfDKNUjd451b8/oBST0U+ih4zZGFlWJXd0hIzQKLvBYFYQJBANRlsSv1CzBO1KrB0J6DpwRujzwjIh0Q/GTjIHxqNuKZoBj7E5RQuhYVQ4u/ugAWqFYFnOtXOG8P8/8XXg9JxJ8CQQDSBhU" +
            "VevQ+NNqfRxLc09f7ja2xVB88a2xLAUimiuaFYTiiNCGjdQ/smYxWHRJVDGDIFKzr7tIlZthosKIYMjfxAkEAicDetNy8AfvOsGT9siE+zIAMQ/uhX2qp16D7a9XTfQoYLLuCylnFJxdpdJTHxNDtIsWHLZenEVkubc6xAYddjwJAI6" +
            "YMphy8oGJwSWDS/tCmdHhT5ymUM7k4JHMPVdV102XxKvcWTpxsG4jknSlKF02Gj++SemmLGfYe/YYdvWWowQJAGnx/yex9NikpKAMJ80rbzaGlylGBA3Ok7JSlTP4mgchKQqzD/sd1mWb82rmqpMjxuAS7BdKqVLjo1Ok2mj4vCg==";
    public static String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO" +
            "5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    public static String APP_ID = "2017122601233126";
    public static String SERVER_URL = "https://openapi.alipay.com/gateway.do";
    public static AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA");

    public static String AlipayClient(String subject, String outtradeno, String totalamount, String AliPayUrl) {
        //实例化客户端  切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。  privateKey '请填写开发者私钥去头去尾去回车，一行字符串';
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("购买疫苗产品");//对一笔交易的具体描述信息
        model.setTimeoutExpress("30m");// 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
        model.setProductCode("QUICK_MSECURITY_PAY"); //销售产品码
        model.setSubject(subject);//商品的标题/交易标题/订单标题/订单关键字等
        model.setOutTradeNo(outtradeno); //订单号
        model.setPassbackParams("callback params");
        model.setTotalAmount(totalamount); //订单总金额 单位为元---
        request.setBizModel(model); //业务请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
        request.setNotifyUrl(AliPayUrl); // 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody().toString();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "接口异常！";
    }


    public static String AlipayPrecreate(String subject, String outtradeno, String totalamount, String AliPayUrl) {
        //AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.precreate  AlipayTradePrecreate
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setBody("购买疫苗产品");//对一笔交易的具体描述信息
        model.setTimeoutExpress("30m");// 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
        model.setSubject(subject);//商品的标题/交易标题/订单标题/订单关键字等
        model.setOutTradeNo(outtradeno); //订单号
        model.setTotalAmount(totalamount); //订单总金额 单位为元---
        request.setBizModel(model); //业务请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
        request.setNotifyUrl(AliPayUrl); // 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https

        /*request.setBizContent("{" +
                "\"out_trade_no\":\"20150320010101001\"," +
                "\"seller_id\":\"2088102146225135\"," +
                "\"total_amount\":88.88," +
                "\"discountable_amount\":8.88," +
                "\"subject\":\"Iphone616G\"," +
                "\"goods_detail\":[{" +
                "\"goods_id\":\"apple-01\"," +
                "\"goods_name\":\"ipad\"," +
                "\"quantity\":1," +
                "\"price\":2000," +
                "\"goods_category\":\"34543238\"," +
                "\"body\":\"特价手机\"," +
                "\"show_url\":\"http://www.alipay.com/xxx.jpg\"" +
                "}]," +
                "\"body\":\"Iphone616G\"," +
                "\"operator_id\":\"yx_001\"," +
                "\"store_id\":\"NJ_001\"," +
                "\"disable_pay_channels\":\"pcredit,moneyFund,debitCardExpress\"," +
                "\"enable_pay_channels\":\"pcredit,moneyFund,debitCardExpress\"," +
                "\"terminal_id\":\"NJ_T_001\"," +
                "\"extend_params\":{" +
                "\"sys_service_provider_id\":\"2088511833207846\"" +
                "}," +
                "\"timeout_express\":\"90m\"," +
                "\"business_params\":\"{\\\"data\\\":\\\"123\\\"}\"" +
                "}");*/


        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            if(response.isSuccess()){
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
            return response.getQrCode();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "接口异常！";

    }


    //异步通知
    public boolean Alipay(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        boolean flag = false;
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        try {
            flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "utf-8", "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return flag;
    }


}
