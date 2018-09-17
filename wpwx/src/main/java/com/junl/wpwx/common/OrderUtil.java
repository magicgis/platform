package com.junl.wpwx.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderUtil {

	/** 订单类型-微信疫苗付款 */
	public static String ORDERTYPE_VAC = "1";
	/** 订单类型-微信退款 */
	public static String ORDERTYPE_WX_REFUND = "2";
	/** 微信刷卡支付 */
	public static String CHANNEL_WX_SCAN = "WX_SCAN";
	/** 微信扫码支付 */
	public static String CHANNEL_WX_NATIVE = "WX_NATIVE";
	/** 微信公众号支付 */
	public static String CHANNEL_WX_JSAPI = "BC_WX_JSAPI";
	
	
//	private static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";   
    private static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";   
    private static final String numberChar = "0123456789"; 
	
	/**
	 * 根据类型生成订单号
	 * @author fuxin
	 * @date 2017年3月6日 下午6:33:19
	 * @description 
	 *		TODO
	 * @param type
	 * @return
	 *
	 */
	public static String getOrderNo(String type){
		String orderNo = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		orderNo = orderNo + type + sdf.format(new Date()) + generateNum(3);
		return orderNo;
	}



	public static String getOrderId(){
		String orderNo = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHH");
		orderNo = orderNo  + sdf.format(new Date()) + generateNum(5);
		return orderNo;
	}


	/**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)  
     *  
     * @param length 随机字符串长度  
     * @return 随机字符串  
     */   
    public static String generateNum(int length) {   
            StringBuffer sb = new StringBuffer();   
            Random random = new Random();   
            for (int i = 0; i < length; i++) {   
                    sb.append(numberChar.charAt(random.nextInt(numberChar.length())));   
            }   
            return sb.toString();   
    }
	
    /**
     * 生成订单号
     * @author fuxin
     * @date 2017年3月6日 下午6:41:50
     * @description 
     *		TODO
     * @return
     *
     */
	public static String getOrderNo(){
		return getOrderNo(ORDERTYPE_VAC);
	}
	
	
    /**  
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)  
     *  
     * @param length 随机字符串长度  
     * @return 随机字符串  
     */   
    public static String generateNumChar(int length) {   
            StringBuffer sb = new StringBuffer();   
            Random random = new Random();   
            for (int i = 0; i < length; i++) {   
                    sb.append(letterChar.charAt(random.nextInt(letterChar.length())));   
            }   
            return sb.toString();   
    }
	
	
	public static void main(String[] args) {
		System.out.println(getOrderNo());
		System.out.println(getOrderNo());
		System.out.println(getOrderNo());
		System.out.println(getOrderNo());
		
	}
}
