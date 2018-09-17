package com.junl.wpwx.common.sms;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.junl.frame.tools.string.StringUtils;


/**
 * 
 * @class SmsClient
 * @author LEON
 * @date 2015年9月5日 下午4:13:29
 * @description
 *		短信接口
 *
 */
public class SmsClient {
	
	public static final Log logger = LogFactory.getLog(SmsClient.class);
	
	/**
	 * 
	 * @method getSix
	 * @author LEON
	 * @date 2015年9月5日 下午4:19:02
	 * @description
	 *		产生6位随机数，解决不足6位的bug
	 *
	 * @return
	 */
	private static String getSix() {
		
		Random rad = new Random();  
		String result  = rad.nextInt(1000000) + "";  
		if(result.length() != 6){  
			return getSix();  
		}  
		return result;  
	}
	
	/**
	 * 
	 * @author chenmaolong
	 * @date 2015年12月7日 下午3:34:20
	 * @description 生成12位的时间戳
	 * @return
	 *
	 */
	private static long getTimeStamp() {
		return System.currentTimeMillis() /10;
	}

	/**
	 * 
	 * @method getParams
	 * @author LEON
	 * @date 2015年9月5日 下午4:51:07
	 * @description
	 *		获取配置并封装
	 *
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private static Map<String, String> getParamsV1(String mobile, String randomNum,String tempid) {
		
		String userName = SmsManager.getInstance().get("sms.api.username");
		String scode = SmsManager.getInstance().get("sms.api.scode");
		String template = SmsManager.getInstance().get("sms.api.temp");
		
		Gson gson = new Gson();
		Map<String, String> temp = gson.fromJson(template, HashMap.class);
		
		Map<String, String> configMap = new HashMap<String, String>();
		configMap.put("username", userName);	// 此处填写用户账号
		configMap.put("scode", scode);			// 此处填写用户密码
		configMap.put("mobile", mobile);
		//短信模板
		if(StringUtils.isNotEmpty(tempid)){
			configMap.put("tempid", (String) temp.get(tempid));
		}else{
			configMap.put("tempid", (String) temp.get("organizationregister"));
		}
		
		
		String [] content = randomNum.split(",");
		String msm = "";
		for (int i = 0; i < content.length; i++) {
			msm +="@"+(i+1)+"@="+content[i]+",";
		}
		msm = msm.substring(0, msm.length()-1);
		configMap.put("content", msm);// 此处填写模板短信内容
		
		return configMap;
	}
	/**
	 * 
	 * @method getParamsV2
	 * @author chenmaolong
	 * @date 2015年12月30日 下午4:51:07
	 * @description
	 *		获取配置并封装
	 *
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> getParamsV2(String mobile, String randomNum,String tempid) {

		String method = SmsManager.getInstance().get("sms.api.method");
		String userName = SmsManager.getInstance().get("sms.api.username");
		String password = SmsManager.getInstance().get("sms.api.password");
		String template = SmsManager.getInstance().get("sms.api.temp");
		String veryCode = SmsManager.getInstance().get("sms.api.veryCode");
		String msgtype = SmsManager.getInstance().get("sms.api.msgtype");
		
		Gson gson = new Gson();
		Map<String, String> temp = gson.fromJson(template, HashMap.class);
		
		Map<String, String> configMap = new HashMap<String, String>();
		
		configMap.put("method", method);
		configMap.put("username", userName);	// 此处填写用户账号
		configMap.put("password", password);			// 此处填写用户密码
		configMap.put("veryCode", veryCode);
		configMap.put("mobile", mobile);
		configMap.put("msgtype", msgtype);
		
		//短信模板
		if(StringUtils.isNotEmpty(tempid)){
			configMap.put("tempid", (String) temp.get(tempid));
		}else{
			configMap.put("tempid", (String) temp.get("organizationregister"));
		}
		
		String [] content = randomNum.split(",");
		String msm = "";
		for (int i = 0; i < content.length; i++) {
			msm +="@"+(i+1)+"@="+content[i]+",";
		}
		msm = msm.substring(0, msm.length()-1);
		configMap.put("content", msm);// 此处填写模板短信内容
		
		return configMap;
	}
	

	/**
	 * 
	 * @method post
	 * @author LEON
	 * @date 2015年9月5日 下午5:06:01
	 * @description
	 *		调用接口发送短信
	 *
	 * @param mobile
	 * @param tempid短信模板
	 * @param timestamp 是否使用时间戳
	 * @return
	 */
	public static String post(String mobile,String tempid,boolean timestamp) {
		
		// step1: 生成6位随机数
		String randomNum = "";
		
		if(timestamp){
			randomNum = String.valueOf(SmsClient.getTimeStamp());
		}else{
			randomNum = getSix();
		}
		
		
		// step2: 获取配置
		Map<String, String> configMap = getParamsV2(mobile, randomNum,tempid);
		
		// step4: 调用接口
		String requrl = SmsManager.getInstance().get("sms.api.requrl");
		HttpURLConnection url_con = null;
        String responseContent = null;
        try {
        	StringBuffer params = new StringBuffer();
            for (Iterator<?> iter = configMap.entrySet().iterator(); iter
                    .hasNext();) {
            	Entry<?, ?> element = (Entry<?, ?>) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), "GBK"));
                params.append("&");
            }

            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(requrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            url_con.setConnectTimeout(5000);
            url_con.setReadTimeout(10000);
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            byte[] echo = new byte[10 * 1024];
            int len = in.read(echo);
            responseContent = (new String(echo, 0, len)).trim();
            int code = url_con.getResponseCode();
            if (code != 200) {
                responseContent = "ERROR" + code;
            }
            logger.info("短信平台====响应内容："+responseContent);
        } catch(IOException e) {
        	logger.error("网络故障:"+ e.toString());
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
		
		return randomNum;
	}
	
	/**
	 * 
	 * @author qijianwen
	 * @date 2016年5月26日 下午6:07:28
	 * @description 
	 *		获取验证码
	 * @param timestamp
	 * @return
	 *
	 */
	public static String postVoice(boolean timestamp) {
		
		// step1: 生成6位随机数
		String randomNum = "";
		
		if(timestamp){
			randomNum = String.valueOf(SmsClient.getTimeStamp());
		}else{
			randomNum = getSix();
		}
		
		return randomNum;
	}
	public static void main(String[] args) {
		SmsClient.post("13856053570", null,true);
		long str = SmsClient.getTimeStamp();
		System.out.println(str);
	}
}
