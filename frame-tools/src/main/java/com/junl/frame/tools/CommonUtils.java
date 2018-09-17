package com.junl.frame.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @Class CommonUtils
 * @author LEON
 * @Date 2014-3-21 下午4:32:30
 * @Description
 * 		一些工具类
 *
 */
public class CommonUtils {
	
	
	public static final Log logger = LogFactory.getLog(CommonUtils.class);
	
	/**
	 * 
	 * @Method UUIDGenerator
	 * @author LEON
	 * @Date 2014-3-21 下午4:33:58
	 * @Description
	 * 		uuid 生成器
	 *
	 * @return
	 */
	public static String UUIDGenerator() {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		return id.replace("-", "");
	}
	
	/** 
	* Html转换为TextArea文本:编辑时拿来做转换 
	* @author zhengxingmiao 
	* @param str 
	* @return 
	*/  
	public static String html2Text(String str) {  
		if (str == null) {  
		return "";  
		}else if (str.length() == 0) {  
		return "";  
		}  
		str = str.replaceAll("<br />", "\n");  
		str = str.replaceAll("<br />", "\r");  
		return str;  
	}  
	  
	/** 
	* TextArea文本转换为Html:写入数据库时使用 
	* @author zhengxingmiao 
	* @param str 
	* @return 
	*/  
	public static String text2Html(String str) {  
		if (str == null) {  
		return "";  
		}else if (str.length() == 0) {  
		return "";  
		}  
		str = str.replaceAll("\n", "<br />");  
		str = str.replaceAll("\r", "<br />");  
		return str;  
	}
	
	
	/**
	 * 
	 * @method getProperties
	 * @author LEON
	 * @date 2015年8月31日 下午1:36:15
	 * @description
	 *		 获取配置文件
	 *
	 * @param inputstream
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> getProperties(InputStream inputstream) {
		
		Map<String, String> propertyMap = null;
		try {
			Properties properties = new Properties();
			properties.load(inputstream);
			propertyMap = (Map) properties;
			
			inputstream.close();
			return propertyMap;
		} catch (Exception e) {
			logger.error("加载配置文件出错");
			e.printStackTrace();
			try {
				inputstream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				inputstream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return propertyMap;

	}
	
	/**
	 * 
	 * @Method randomGenerator
	 * @author chenmaolong
	 * @date 2015年9月9日 下午6:14:53
	 * @Description
	 * 		random 生成器
	 *
	 * @return
	 */
	public static String randomGenerator() {
	    double random = Math.random()*89999999+10000000;
		
		return String.valueOf(Math.round(random));
	}
	
	public static String randomData(){
		int max = 9999;
		int min = 1000;
		Random ran = new Random();
		int re = ran.nextInt(max)%(max - min + 1) + min;
		
		return String.valueOf(re);
	}

	
	
	public static void main(String[] args) {
		System.out.println(UUIDGenerator());
		System.out.println(randomData());
	}
}
