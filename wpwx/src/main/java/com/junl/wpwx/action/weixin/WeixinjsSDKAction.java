package com.junl.wpwx.action.weixin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.junl.wpwx.common.JsonBuild;
import com.junl.wpwx.form.WeixinForm;
import com.junl.wpwx.service.weixin.IWeixinjsSDK;

@Controller
@RequestMapping("/weixin")
public class WeixinjsSDKAction extends BaseAction {

	@Autowired
	private IWeixinjsSDK service;
	
	/**
	 * 
	 * @author Songqingfeng
	 * @date 2016年7月5日 下午2:58:15
	 * @description 
	 *		js-sdk接口注入权限验证配置
	 *		http://localhost:8080/wpwx/weixin/generatConf.do
	 *				接口参数：
	 *					url:请求页面URL
	 * @param form
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(value="generatConf" ,method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> generatConf(@ModelAttribute WeixinForm form) throws Exception {
	
		Map<String,Object> result = null;
		try {
			result=service.generatConf(form);
		} catch (Exception e) {
			result = JsonBuild.buildSystemError(result);
			logger.error("登录接口异常："+e);
			e.printStackTrace();
		}
		return result;
	}
	
}
