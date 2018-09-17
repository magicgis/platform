package com.junl.wpwx.action.weixin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.junl.wpwx.vo.WechatUserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junl.frame.tools.net.WebUtils;
import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.mapper.VacUserMapper;
import com.junl.wpwx.model.VacUser;
import com.junl.wpwx.service.weixin.IOAuth;

/**
 * 
 * @author xus
 * @date 2016年6月23日 上午10:28:05
 * @description 
 *		微信OAuth 2.0 网页授权 获取用户信息
 *		微信用户进入需手动授权
 */
@Controller
public class OAuthAction extends BaseAction{
	
	
	@Autowired
	private ConfigProperty property;
	@Autowired
	private IOAuth wxAuth;
	@Autowired
	private VacUserMapper userMapper;

	
	
	/**
	 * 
	 * @author xus
	 * @date 2016年6月23日 上午11:24:43
	 * @description 
	 *		获取配置文件，传入授权页面
	 *		页面根据配置文件进入微信获取用户授权页面
	 * @param model
	 * @return
	 * @throws Exception
	 *
	 */
	
	@RequestMapping("/oauthGrant")
	public String grant(HttpServletRequest request,ModelMap model) throws Exception {
		//记录来源页面
		if(StringUtils.isNotEmpty(request.getParameter("from"))){
			WebUtils.getRequest().getSession().setAttribute("wpwx.from", request.getParameter("from"));
		}
		model.put("property", property);
		return "grantAuth";
	}
	
	
	/**
	 * 
	 * @author xus
	 * @date 2016年6月23日 上午11:28:18
	 * @description 
	 *		通过code换取网页授权access_token
	 * @throws Exception
	 *
	 */
	@RequestMapping("/oauthGrantToken")
	public void grantToken(HttpServletRequest request,HttpServletResponse response,
			String code, String state) throws Exception {
		
		//获取openid
		String openId = wxAuth.getAccToken(code);
		WechatUserInfoVo userinfo=  wxAuth.getWechatUserInfo(openId);
		if(StringUtils.isNotEmpty(openId)){
			logger.info("获取微信用户openID:" + openId);
			//获取该openid是否注册过
			Map<String,Object> params =new HashMap<String,Object>();
			params.put("openId",openId);
			VacUser user = userMapper.queryObject(params);
			if(null == user){
				//未注册过则进行注册
				user = new VacUser();
				user.setOpenid(openId);
				user.setCreatedate(new Date());
				userMapper.insert(user);
			}
			//将用户放入session
			WebUtils.getRequest().getSession().setAttribute("wpwx.userid", user.getId());
			
			//跳转到来源页面
			String from = (String) WebUtils.getRequest().getSession().getAttribute("wpwx.from");
			if(StringUtils.isNotEmpty(from)){
				response.sendRedirect(from);
			}else{
				response.sendRedirect("vac/index.do");
			}	
		}
	}
}
