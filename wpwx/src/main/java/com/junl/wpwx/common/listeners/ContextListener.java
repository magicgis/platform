package com.junl.wpwx.common.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.beecloud.BeeCloud;

import com.junl.frame.tools.PropertiesUtil;
import com.junl.wpwx.common.sms.SmsManager;

/**
 * 
 * @class ContextListener
 * @author LEON
 * @date 2015年8月27日 上午11:27:11
 * @description
 *		TODO
 *
 */
public class ContextListener implements ServletContextListener {
	
	public final Log logger = LogFactory.getLog(ContextListener.class);
	
	
	private SmsManager smsManager;
	
	public void contextInitialized(ServletContextEvent event) {
		
		smsManager = SmsManager.getInstance();
		smsManager.loadPropertie();
		
		Map<String, String> conf = new HashMap<String, String>();
		conf = PropertiesUtil.getProperties(this.getClass().getClassLoader().getResourceAsStream("conf.properties"));
		//测试模式使用方法
		BeeCloud.registerApp(conf.get("bee_appId"), conf.get("bee_test_secret"), conf.get("bee_secret"), conf.get("bee_master"));  
		logger.info("beeCloud注册成功!!!");
		//设置sandbox属性为true，开启测试模式  
		//BeeCloud.setSandbox(true);
		
	}
	
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
