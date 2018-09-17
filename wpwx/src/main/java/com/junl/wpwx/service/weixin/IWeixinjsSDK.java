package com.junl.wpwx.service.weixin;

import java.util.Map;

import com.junl.wpwx.form.WeixinForm;
import com.junl.wpwx.vo.TemplateMsgVo;

public interface IWeixinjsSDK {

	public Map<String, Object> generatConf(WeixinForm form) throws Exception;
	
	public String sendTemplateMessage(TemplateMsgVo msg) throws Exception;
	
	public void sendCustomerMessage(WeixinForm form) throws Exception;

	public  Map<String, String>  getSign(String url )throws Exception;

}
