package com.junl.wpwx.common;

import org.apache.commons.collections.map.LinkedMap;

public class WxMap{
	
	/**
	 * 获取微信模板消息map
	 * @author fuxin
	 * @date 2017年3月17日 上午11:39:58
	 * @description 
	 *		TODO
	 * @param value
	 * @return
	 *
	 */
	public static LinkedMap getWxTempMsgMap(String value){
		LinkedMap map = new LinkedMap();
		map.put("color", "#173177");
		map.put("value", value);
		return map;
	}
	
	/**
	 * 获取微信模板消息map
	 * @author fuxin
	 * @date 2017年3月17日 上午11:39:58
	 * @description 
	 *		TODO
	 * @param value
	 * @return
	 *
	 */
	public static LinkedMap getWxTempMsgRedMap(String value){
		LinkedMap map = new LinkedMap();
		map.put("color", "#ed5565");
		map.put("value", value);
		return map;
	}
}