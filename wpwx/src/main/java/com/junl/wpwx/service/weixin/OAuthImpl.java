package com.junl.wpwx.service.weixin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junl.frame.tools.http.HttpPost;
import com.junl.frame.tools.net.WebUtils;
import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.vo.WechatUserInfoVo;

/**
 * 
 * @author xus
 * @date 2016年6月23日 上午11:16:05  
 * @description 微信OAuth授权逻辑
 */
@Service
public class OAuthImpl implements IOAuth {

	protected static final Logger logger = LoggerFactory.getLogger(OAuthImpl.class);
	
	public static final String ACCESS_TOKEN_OAUTH = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

	@Autowired()
	private ConfigProperty configProperty;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.junl.wpwx.service.IOAuth#step1()
	 */
	public String getAccToken(String code) throws Exception {
		logger.info("code:" + code);
		// 封装请求地址
		String url = MessageFormat.format(ACCESS_TOKEN_OAUTH, configProperty.getAppId(), configProperty.getAppSecret(), code);

		URI uri = new URI(url);
		SimpleClientHttpRequestFactory schr = new SimpleClientHttpRequestFactory();
		ClientHttpRequest chr = schr.createRequest(uri, HttpMethod.POST);
		ClientHttpResponse res = chr.execute();

		BufferedReader breader = new BufferedReader(new InputStreamReader(res.getBody()));
		String reslutStr = breader.readLine().toString();
		logger.info(reslutStr);

		/**
		 * 正确时返回的JSON数据包如下： { "access_token":"ACCESS_TOKEN", "expires_in":7200,
		 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE" }
		 *
		 * 错误时微信会返回JSON数据包如下（示例为Code无效错误）: {"errcode":40029,"errmsg":
		 * "invalid code"}
		 */
		String returnString = "";
		
		int err = reslutStr.toString().indexOf("errcode");
		if (err > -1) {
			//JSONObject result = JSONObject.parseObject(reslutStr);
			//returnString = "errcode:" + result.getString("errcode");
		} else {
			JSONObject result = JSONObject.parseObject(reslutStr);
			returnString = result.getString("openid");
			WebUtils.getSession().setAttribute("wpwx.openId", returnString);
			WebUtils.getSession().setAttribute("wpwx.login_token", result.getString("access_token"));
		}
		return returnString;
		
	}

	@Override
	public WechatUserInfoVo getWechatUserInfo(String openId) throws Exception {

		String access_token = (String) WebUtils.getSession().getAttribute("wpwx.login_token");

			String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openId+"&lang=zh_CN";
			logger.info("用户基本信息URL:"+url);

			String userInfo = HttpPost.post(url, null);

			logger.info("获取用户基本信息：" + userInfo);
			if(StringUtils.isNotEmpty(userInfo)){
				WechatUserInfoVo  user = JSON.parseObject(userInfo, WechatUserInfoVo.class);
				return user;
		}
		return null;
	}

}
