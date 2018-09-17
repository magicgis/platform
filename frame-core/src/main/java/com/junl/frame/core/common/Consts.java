package com.junl.frame.core.common;

public class Consts {


	public static final int PAGE_SIZE = 10;
	
	/**
	 * 菜单启用状态标记
	 */
	public static final String SYS_MENU_ENABLE = "0";
	
	/**
	 * 菜单禁用状态标记
	 */
	public static final String SYS_MENU_DISABLE = "1";
	
	
	/**
	 * 登录用户
	 */
	public static final String SESSION_USER = "SESSION_USER";
	
	/**
	 * 登录用户信息
	 */
	public static final String SESSION_USER_INFO = "SESSION_USER_INFO";
	
	/**
	 * 发送短信验证码
	 */
	public static final String SMS_TYPE_SEND = "0";
	
	/**
	 * 获取短信验证码
	 */
	public static final String SMS_TYPE_CHECK = "1";
	
	
	/**
	 * 注册发送
	 */
	public static final String SMS_OPERATE_TYPE01 = "1";
	
	/**
	 * 绑定手机号
	 */
	public static final String SMS_BIND_TYPE02 = "2";
	
	
	/**
	 * 注册用户用户来源
	 * return : wechat
	 */
	public static final String UTM_SOURCE = "wechat";
	public static final String UTM_SOURCE_SHARE = "share";
	
	/**
	 * 来源类型：1、题包
	 */
	public static final String MARKET_TYPE01 = "1";
	/**
	 * 来源类型： 2、扫码
	 */
	public static final String MARKET_TYPE02 = "2";
	/**
	 * 来源类型：3、机构码
	 */
	public static final String MARKET_TYPE03 = "3";
	/**
	 * 用户类型：1、下线用户 
	 */
	public static final String MARKET_USERTYPE01 = "1";
	/**
	 * 用户类型： 2、非下线用户
	 */
	public static final String MARKET_USERTYPE02 = "2";
	
	
	/**
	 * 账户类型  1、会员账户
	 */
	public static final String USERTYPE_ACCUOUNTS = "1";
	
	/**
	 * 1、题包
	 */
	public static final String SOURCE_TYPE01 = "1";

	/**
	 * 2、提现
	 */
	public static final String SOURCE_TYPE02 = "2";
	
	/**
	 * 是否账户被冻结
	 */
	public static final String BISFROZEN = "0";
	
	/**
	 * 是否已停用
	 */
	public static final String BISSTOP = "0";
	
	
	//******************分销记录处理状态******************
	/**
	 * 未激活
	 */
	public static final String MARKET_STATUS00 = "0";
	
	/**
	 * 已激活
	 */
	public static final String MARKET_STATUS01 = "1";
	
	/**
	 * 已结算
	 */
	public static final String MARKET_STATUS02 = "2";
	
	/**
	 * 提现：0待激活
	 */
	public static final String ISTATUS00= "0";

	/**
	 * 提现：1：已激活-审核成功
	 */
	public static final String ISTATUS01= "1";

	/**
	 * 提现：异常-审核不通过
	 */
	public static final String ISTATUS02= "2";
	
	/**
	 *  版本
	 */
	public static final String IVERSION = "1";
	
	
	//******************下载题包记录处理状态******************
	/**
	 * 0、老记录
	 */
	public static final String DOWN_PACK_STATUS00 ="0";
	/**
	 * 1、待处理
	 */
	public static final String DOWN_PACK_STATUS01 ="1";
	/**
	 *  2、已结算
	 */
	public static final String DOWN_PACK_STATUS02 ="2";
	/**
	 *  3、异常
	 */
	public static final String DOWN_PACK_STATUS03 ="3";
	/**
	 *  4、无上线
	 */
	public static final String DOWN_PACK_STATUS04 ="4";
	
	
	//******************下线级别******************
	/**
	 *  2下线
	 */
	public static final String LINE_LEVEL02 ="2";
	/**
	 *  3下线
	 */
	public static final String LINE_LEVEL03 ="3";
	
	
}
