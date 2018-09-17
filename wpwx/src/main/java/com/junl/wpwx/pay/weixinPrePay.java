package com.junl.wpwx.pay;

import com.alibaba.fastjson.JSONObject;
import com.junl.wpwx.common.ConfigProperty;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class weixinPrePay {


    public static String REQUEST_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";//微信统一下单接口地址


    /*参数说明  sn订单号  totalAmount支付金额   description产品描述*/
    // tradeType  JSAPI 公众号支付 NATIVE 扫码支付 APP APP支付

    public static  Map<String, String> weixinPrePay(String sn, String totalAmount, String description, String WeixinPayUrl, String tradeType, HttpServletRequest request) {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", PayCommonUtil.APPID);
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));
        parameterMap.put("body", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600)); //商品描述
        parameterMap.put("out_trade_no", sn);//订单号
        parameterMap.put("fee_type", "CNY");
        BigDecimal total = new BigDecimal(totalAmount).multiply(new BigDecimal(100));
        DecimalFormat df=new DecimalFormat("0");
        parameterMap.put("total_fee", df.format(total));//金额
        parameterMap.put("spbill_create_ip", request.getRemoteAddr());//ip
        parameterMap.put("notify_url", WeixinPayUrl); //回调地址
        parameterMap.put("trade_type", tradeType);
        parameterMap.put("product_id",PayCommonUtil.getRandomInt(18) ); //trade_type=NATIVE时（即扫码支付）
        String sign = PayCommonUtil.createSign("UTF-8", parameterMap); //第一次签名
        parameterMap.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        System.out.println(requestXML);
        String result = PayCommonUtil.httpsRequest(REQUEST_URL, "POST", requestXML);
        System.out.println(result);
        Map<String, String> map = null;
        try {
            map = PayCommonUtil.doXMLParse(result);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }

    public static JSONObject createSignAgain(String description, String sn, String totalAmount, String WeixinPayUrl , String tradeType, HttpServletRequest request){
        Map<String, String> map = weixinPrePay(sn, totalAmount,description,WeixinPayUrl,tradeType,request);
        JSONObject jsonObject = new JSONObject();
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", PayCommonUtil.APPID);
        parameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        parameterMap.put("prepayid", map.get("prepay_id"));
        parameterMap.put("package", "Sign=WXPay");
        parameterMap.put("noncestr", PayCommonUtil.getRandomString(32));
        parameterMap.put("timestamp", System.currentTimeMillis()/1000+""); //(int) (System.currentTimeMillis() / 1000)
        String sign = PayCommonUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);
        jsonObject.put("parameterMap",parameterMap);
        return jsonObject;
    }
    public static String createNative(String description, String sn, String totalAmount, String WeixinPayUrl , String tradeType, HttpServletRequest request){
        Map map=weixinPrePay(sn, totalAmount,description,WeixinPayUrl,tradeType,request);
        return  map.get("code_url").toString();
    }



}
