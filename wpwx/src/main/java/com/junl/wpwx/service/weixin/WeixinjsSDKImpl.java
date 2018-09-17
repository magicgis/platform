package com.junl.wpwx.service.weixin;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junl.frame.tools.Ehcache;
import com.junl.frame.tools.http.HttpPost;
import com.junl.frame.tools.string.StringUtils;
import com.junl.frame.tools.weixin.WeixinjsSDK;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.common.JsonBuild;
import com.junl.wpwx.form.WeixinForm;
import com.junl.wpwx.vo.TemplateMsgVo;

/**
 * 
 * @author chenmaolong
 * @date 2016年7月15日 上午10:47:16
 * @description 
 *		微信js-SDK接口对接
 */
@Service
public class WeixinjsSDKImpl implements IWeixinjsSDK{

	protected static final Logger logger = LoggerFactory.getLogger(WeixinjsSDKImpl.class);
	
	public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";
	public static final String FOLLOWS_LIST = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={0}&next_openid={1}";

	@Autowired
	private ConfigProperty property;

	@Autowired
	private Ehcache ehcache;
	
	/**
	 * 
	 * @author chenmaolong
	 * @date 2016年7月15日 上午10:49:39
	 * @description 
	 *		获取access token--接口调用凭据
	 *	API地址： http://mp.weixin.qq.com/wiki/15/54ce45d8d30b6bf6758f68d2e95bc627.html
	 * @return
	 *
	 */
	private String getAccess_Token(){
		
		String access_token = "";
		try {

			access_token = (String) ehcache.getCache("access_token", "tokenCache");
			if(StringUtils.isEmpty(access_token)){
				String appid = property.getAppId();
				String appsecret = property.getAppSecret();
				
				//获取access token
				String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
						+ "&appid="+appid+"&secret="+appsecret;
				String tokenInfo = HttpPost.post(tokenUrl, null);
				
				logger.info("获取access token返回：" + tokenInfo);
				
				access_token = JSON.parseObject(tokenInfo).getString("access_token");
				
				//公众号调用接口并不是无限制的。为了防止公众号的程序错误而引发微信服务器负载异常，默认情况下，每个公众号调用接口都不能超过一定限制
				ehcache.putCache("access_token",access_token, "tokenCache");
			}
			
		} catch (Exception e) {
			logger.info("获取接口调用凭据access token异常：" + e);
		}
		return access_token;
		
	}


	/***
	 * 获取jsapiTicket
	 * 来源 www.vxzsk.com
	 * @return
	 */
	public  String getJSApiTicket(){

		String urlStr = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+this.getAccess_Token()+"&type=jsapi";
		String jsapi_ticket = "";
		try {
            jsapi_ticket = (String) ehcache.getCache("jsapi_ticket", "tokenCache");
            if(StringUtils.isEmpty(jsapi_ticket)){
                String JSApiTicket = HttpPost.post(urlStr, null);
                logger.info("获取access token返回：" + JSApiTicket);
                jsapi_ticket = JSON.parseObject(JSApiTicket).getString("ticket");
                //公众号调用接口并不是无限制的。为了防止公众号的程序错误而引发微信服务器负载异常，默认情况下，每个公众号调用接口都不能超过一定限制
                ehcache.putCache("jsapi_ticket",jsapi_ticket, "tokenCache");
            }
		}catch (Exception e) {
			logger.info("获取接口调用凭据access token异常：" + e);
		}
		return  jsapi_ticket;

	}



	public  Map<String, String>  getSign(String url ){
		String appid = property.getAppId();
		Map<String, String> ret = WeixinjsSDK.sign(this.getJSApiTicket(), url);
		ret.put("appid",appid);
		return  ret;
	}
	
	/**
	 * 
	 * @author chenmaolong
	 * @date 2016年7月15日 上午10:49:39
	 * @description 
	 *		生成config接口注入权限验证配置
	 *	API地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
	 * @return
	 *
	 */
	@Override
	public Map<String, Object> generatConf(WeixinForm form) throws Exception {
		String ticktUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ this.getAccess_Token()
				+ "&type=jsapi";
		
		String jsapiTicketJson = HttpPost.post(ticktUrl, null);
		//jsapi_ticket是公众号用于调用微信JS接口的临时票据
		if(jsapiTicketJson.indexOf("ticket") > -1){
			String jsapi_ticket = JSON.parseObject(jsapiTicketJson).getString("ticket");
			
			Map<String, String> signMap = WeixinjsSDK.sign(jsapi_ticket, form.getUrl());
			
			//config接口注入权限验证配置
			String[] jsApiList = {"onMenuShareTimeline","onMenuShareAppMessage",
					"onMenuShareQQ","onMenuShareWeibo","onMenuShareQZone"};
			Map<String, Object> reMap = new HashMap<String, Object>();
			reMap.put("appId",property.getAppId());
			reMap.put("timestamp",signMap.get("timestamp"));
			reMap.put("nonceStr",signMap.get("nonceStr"));
			reMap.put("signature", signMap.get("signature"));
			reMap.put("jsApiList",jsApiList);
			
			return JsonBuild.build(true, "js-sdk接口","", reMap);
		}else{
			logger.info("获取jsapi_ticket临时票据异常：" + jsapiTicketJson);
		}
		
		return JsonBuild.build(false, "js-sdk接口异常","", null);
	}

	/**
	 * 发送客服消息
	 */
	public void sendCustomerMessage(WeixinForm form) throws Exception {
		String reqUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+ this.getAccess_Token();
		
		String openId = "o3JF8s8XrxObJoPo4KM3N1_f52r8";
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("touser",openId);
		reMap.put("msgtype","text");

        Map<String, Object> textObj = new HashMap<String, Object>();
        textObj.put("content", "快接受我的消息");

		reMap.put("text",textObj);
		
		String responseJson = HttpPost.post(reqUrl, JSONObject.toJSONString(reMap));
		
		logger.info("发送客服消息响应:"+responseJson);
	}
	
	/**
	 * 发送模板消息
	 */
	public String sendTemplateMessage(TemplateMsgVo msg) throws Exception {
		String reqUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ this.getAccess_Token();
		
		logger.info("发送模板消息:"+JSONObject.toJSONString(msg));
		String responseJson = HttpPost.post(reqUrl, JSONObject.toJSONString(msg));
		logger.info("发送模板消息响应:"+responseJson);
		return responseJson;
	}

	@SuppressWarnings("unchecked")
	public List<String> getFollowList() throws Exception {
		String accessToken = getAccess_Token();
		List<String> openIds = new ArrayList<String>();
		String url = MessageFormat.format(FOLLOWS_LIST, accessToken, "");
		logger.info("用户基本信息URL:"+url);
		
		String token = HttpPost.post(url, null);
		
		logger.info("获取关注列表：" + token);
		if(StringUtils.isNotEmpty(token)){
			JSONObject json = JSONObject.parseObject(token);
			if(json.getInteger("total") > 0){
				openIds = JSON.parseObject(json.getJSONObject("data").getString("openid"), List.class);
//				if(json.getInteger("total") > json.getInteger("count")){
			}
		}
		return openIds;
	}

}
