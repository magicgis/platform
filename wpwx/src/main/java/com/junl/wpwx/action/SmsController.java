package com.junl.wpwx.action;

import com.junl.frame.tools.Ehcache;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.service.AsyncService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/sms")
public class SmsController extends BaseAction {

	@Autowired
	AsyncService async;
	@Resource
	private Ehcache cache;
	
	/**
	 * 发送短信验证码
	 * @author fuxin
	 * @date 2017年3月10日 下午2:32:31
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	@RequestMapping("/smscheckcode/{phone}")
	public @ResponseBody String sendchecksms(@PathVariable("phone") String phone){
		if(async.sendCheckSMS(phone)){
			return "success";
		}else{
			return "";
		}
	}
	
	/**
	 * 
	 * @author fuxin
	 * @date 2017年3月10日 下午3:42:31
	 * @description 
	 *		TODO
	 * @param phone
	 * @return
	 *
	 */
	@RequestMapping("/checksms")
	public @ResponseBody String checksms(String phone, String code){
		if (StringUtils.isNoneBlank(phone) && StringUtils.isNotBlank(code)) {
			try {
				//获取缓存中的验证码
				String cc = (String) cache.getCache(phone, "messageCache");
				if (code.equals(cc)) {
					return "success";
				}
			} catch (Exception e) {
				//捕获异常，不处理
			}
		}
		return "";
	}





}
