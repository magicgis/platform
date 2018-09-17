package com.junl.wpwx.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @Class WebUtil
 * @author LEON
 * @Date 2014-5-2 下午5:50:41
 * @Description TODO
 * 
 */
public class WebUtil {

	/**
	 * 
	 * @Method getRequest
	 * @author LEON
	 * @Date 2014-5-2 下午5:50:44
	 * @Description TODO
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return request;
	}

	/**
	 * 
	 * @Method getSession
	 * @author LEON
	 * @Date 2014-5-2 下午5:51:23
	 * @Description 获取SESSION
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 
	 * @Title: getIP
	 * @Author: sxu
	 * @Date: Sep 29, 201410:38:30 AM
	 * @Description: 获取IP
	 * 
	 * @Version: 1.0
	 * 
	 * @return
	 */
	public static String getIP() {

		HttpServletRequest request = getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if ((ip == null) || (ip.length() == 0)
				|| ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0)
				|| ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0)
				|| ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 
	 * @Title: getRealRootPath
	 * @Author: sxu
	 * @Date: Nov 6, 201411:54:00 AM
	 * @Description: 获取项目根目录绝对路径
	 * 
	 * @Version: 1.0
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getRealRootPath() {

		return getRequest().getRealPath("/");
	}

	/**
	 * 
	 * @method getWebPath
	 * @author LEON
	 * @date 2015年8月30日 上午10:19:33
	 * @description
	 *		获取当前web路径
	 *		例如:
	 *			http://localhost:8080/demo/
	 *
	 * @return
	 */
	public static String getWebPath() {

		HttpServletRequest request = getRequest();
		return request.getScheme() + "://"
				+ request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath()+"/";
	}
	
	/**
	 * 
	 * @method getServerPath
	 * @author chenmaolong
	 * @date 2015年9月1日 上午10:19:33
	 * @description
	 *		获取当前web路径
	 *		例如:
	 *			http://localhost:8080
	 *
	 * @return
	 */
	public static String getServerPath() {

		HttpServletRequest request = getRequest();
		return request.getScheme() + "://"
				+ WebUtil.getRequest().getServerName() + ":"
				+ WebUtil.getRequest().getServerPort();
	}
}
