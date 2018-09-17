package com.junl.wpwx.common.sms;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import net.sf.json.JSONObject;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 发送短信
 * 
 * @author ngh
 * @datetime [2016年7月26日 下午3:12:11]
 *
 */
public final class SMS {
	
	public static final int PLAN_NOTICE = 3029011;
	/** 微信-关注宝宝-短信验证码 */
	public static final String TEMP_CHILD_CHECK = "SMS_56705067";
	/** 投保成功-短信通知 */
	public static final String TEMP_INS_FINISH = "SMS_68105333";
	/** 自助建档-成功 */
	public static final String TEMP_SELF_SUCCESS = "SMS_67280487";
	
	private SMS() {
		
	}

	
	
	
	/**
	 * 
	 * @author fuxin
	 * @date 2017年2月23日 上午11:46:38
	 * @description 
	 *		TODO
	 * @param phone(电话号码，多个用,分格)  args(参数{name:'data1',time:'data2'})
	 * @return
	 * @throws ApiException
	 * @throws UnsupportedEncodingException 
	 *
	 */
	public static String sendaliSMS(String phone, Map<String, Object> args, String TemplateCode) throws ApiException, UnsupportedEncodingException{
		if(!"open".equals(SmsManager.getInstance().get("sms.option"))){
			return  "短信发送功能已关闭,若要打开此功能,请将配置文件 sms.option修改为open";
		}
		TaobaoClient client = new DefaultTaobaoClient(SmsManager.getInstance().get("sms.templateUri"), SmsManager.getInstance().get("sms.appKey"), SmsManager.getInstance().get("sms.appSecret"));
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend( "" );
		req.setSmsType( "normal" );
		req.setSmsFreeSignName(SmsManager.getInstance().get("sms.freeSignName"));
		req.setSmsParamString(JSONObject.fromObject(args).toString());
		req.setRecNum(phone);
		req.setSmsTemplateCode(TemplateCode);
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		return rsp.getBody();
	}
	
	
	
}