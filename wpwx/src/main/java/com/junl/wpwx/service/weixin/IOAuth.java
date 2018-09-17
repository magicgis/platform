package com.junl.wpwx.service.weixin;

import com.junl.wpwx.vo.WechatUserInfoVo;

/**
 * 
 * @author xus
 * @date 2016年6月23日 上午11:16:05
 * @description 
 *		微信OAuth授权逻辑
 */
public interface IOAuth {

	/**
	 * 
	 * @author xus
	 * @date 2016年6月23日 上午11:32:00
	 * @description 
	 *		通过code换取网页授权access_token
	 * @param code
	 * @throws Exception
	 *
	 */
	public String getAccToken(String code) throws Exception;
	

	public WechatUserInfoVo getWechatUserInfo(String openId) throws Exception;
}
