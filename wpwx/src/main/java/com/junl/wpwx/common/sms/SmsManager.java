package com.junl.wpwx.common.sms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @class GeoManager
 * @author LEON
 * @date 2015年8月27日 上午11:32:23
 * @description
 *		启动项目读取Geocoding配置
 *
 */
public class SmsManager {

	public final Log logger = LogFactory.getLog(SmsManager.class);
	
	private static final SmsManager smsManager = new SmsManager();
	
	private Map<String, String> container = null;
	
	private SmsManager() {
		this.container = new HashMap<String, String>();
	}
	
	public static SmsManager getInstance() {
		return smsManager;
	}
	
	/**
	 * 
	 * @method loadPropertie
	 * @author LEON
	 * @date 2015年8月27日 上午11:36:29
	 * @description
	 *		加载配置文件
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void loadPropertie() {
		logger.info("正在加载短信配置");
		Properties properties = new Properties();
		InputStream input = SmsManager.class.getResourceAsStream("/sms.properties");
		try {
			properties.load(input);
			container = (Map) properties;
			input.close();
		} catch (IOException e) {
			logger.error("读取短信配置失败.");
			try {
				input.close();
			} catch (IOException e1) {
				logger.error("关闭短信文件流失败.");
			}
		}
	}
	
	/**
	 * 
	 * @method get
	 * @author LEON
	 * @date 2015年8月27日 下午2:09:50
	 * @description
	 *		TODO
	 *
	 * @param key
	 * @return
	 */
	public String get(String key) {
		
		return this.container.get(key);
	}
}
