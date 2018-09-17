package com.junl.wpwx.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.junl.frame.tools.net.WebUtils;

/**
 * 微信授权拦截器
 * @author fuxin
 * @date 2017年3月15日 下午5:37:13
 * @description 
 *		TODO
 */
public class AuthorInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURI();
		//自助建档查询
		if(url.indexOf("/api/") > -1 ||url.indexOf("/child/temp/detail.do") > -1 || url.indexOf("/rabies/detail.do") > -1){
			return true;
		}
		if((url.indexOf("/child/") > -1 || url.indexOf("/order/") > -1 || url.indexOf("/vac/") > -1 || url.indexOf("/rabies/") > -1) 
				&& WebUtils.getRequest().getSession().getAttribute("wpwx.userid") == null){
			response.sendRedirect("/wpwx/oauthGrant.do?from=" + url);
			return  false;
		}		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		
	}

}
