package com.junl.frame.tools.weixin;

import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.junl.frame.tools.net.HttpUtils;

/**
 * 
 * @author xus
 * @date 2016年6月23日 下午4:06:07
 * @description 
 *		微信公众账号工具类
 */
public class WeixinPublic {

	/**
	 * 
	 * @author chenmaolong
	 * @date 2016年7月15日 上午9:55:00
	 * @description 
	 *		生成签名的时间戳
	 * @return
	 *
	 */
	public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
	
	/**
	 * 
	 * @author chenmaolong
	 * @date 2016年7月15日 上午9:55:00
	 * @description 
	 *		生成签名的随机串
	 * @return
	 *
	 */
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
	public static String doPost(String url) throws Exception {
		
		String jsonString = HttpUtils.httpPost(url);
		JSONObject json = new JSONObject();
		json.parse(jsonString);
		
		return "";
	}
}
