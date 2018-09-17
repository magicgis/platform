package com.junl.wpwx.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBuild {

	
	/**
	 * 
	 * @method build
	 * @author LEON
	 * @date 2015年8月24日 下午7:57:45
	 * @description
	 *		封装标准返回对象
	 *
	 * @param success
	 * @param msg
	 * @param list
	 * @return
	 */
	public static Map<String, Object> build(boolean success, String msg, String status, List<?> list) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", success);
		resultMap.put("msg", msg);
		resultMap.put("status", status);
		resultMap.put("list", list);
		resultMap.put("serverPath", WebUtil.getServerPath());
		
		return resultMap;
	}
	
	/**
	 * 
	 * @method build
	 * @author LEON
	 * @date 2015年8月25日 上午11:24:40
	 * @description
	 *		TODO
	 *
	 * @param success
	 * @param msg
	 * @param t
	 * @return
	 */
	public static <T> Map<String, Object> build(boolean success, String msg, String status, T t) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", success);
		resultMap.put("msg", msg);
		resultMap.put("status", status);
		resultMap.put("list", t);
		resultMap.put("serverPath", WebUtil.getServerPath());
		
		return resultMap;
	}
	
	/**
	 * 
	 * @method buildSystemError
	 * @author LEON
	 * @date 2015年8月29日 下午3:57:48
	 * @description
	 *		系统异常状态均为 success: false   status: 0
	 *
	 * @param map
	 */
	public static Map<String, Object> buildSystemError(Map<String, Object> map) {
		
		if(map == null) {
			map = new HashMap<String, Object>();
		}
		map.put("success", false);
		map.put("status", ResponseStatus.SYSTEM_ERROR);
		map.put("msg", null);
		map.put("list", null);
		map.put("serverPath", WebUtil.getServerPath());
		
		return map;
	}
}
