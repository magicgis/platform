package com.junl.wpwx.common;

/**
 * 
 * @class ResponseStatus
 * @author LEON
 * @date 2015年8月29日 下午3:16:59
 * @description
 *		返回状态
 *
 */
public class ResponseStatus {
	
	//--------------------------------
	// 系统状态码0-99
	//--------------------------------
	/**
	 * 系统异常
	 */
	public static final String SYSTEM_ERROR = "0";
	
	
	/**
	 * 手机号为空，未发送短信
	 */
	public static final String SMS_MOBILE_EMPTY = "9";
	
	/**
	 * 发送短信失败
	 */
	public static final String SMS_MOBILE_ERROR = "10";
	
	/**
	 * 短信验证码发送成功
	 */
	public static final String SMS_MOBILE_SENDED = "11";
	
	/**
	 * 短信验证码匹配成功
	 */
	public static final String SMS_MOBILE_CHECK_SUCCESS = "12";
	
	/**
	 * 短信验证码匹配失败
	 */
	public static final String SMS_MOBILE_CHECK_ERROR = "13";
	
	/**
	 * 短信验证码失效
	 */
	public static final String SMS_MOBILE_REMOVE = "14";
	
	/**
	 * 服务器没有该手机号的验证码信息
	 */
	public static final String SMS_MOBILE_NOCODE = "15";
	
	
	// 会员相关状态由100-199
	//--------------------------------
	/**
	 * 注册成功
	 */
	public static final String MEMBER_REGIST_SUCCESS = "100";
	
	/**
	 * 注册失败，账号已存在
	 */
	public static final String MEMBER_REGIST_EXIST = "101";
	
	/**
	 * 会员登录成功
	 */
	public static final String MEMBER_LOGIN_SUCCESS = "102";
	
	/**
	 * 会员登录失败，用户名或密码错误
	 */
	public static final String MEMBER_LOGIN_ERROR = "103";
	
	/**
	 * 会员没有绑定手机号码
	 */
	public static final String MEMBER_NO_BINGDMOBILE = "104";
	
	/**
	 * 会员信息不存在
	 */
	public static final String MEMBER_NO_EXSIT = "105";
	

	/**
	 * 您已预定过该题包
	 */
	public static final String PACKAGE_ORDER01 = "150";
	
	//*****************分销***********************************8
	/**
	 * 记录分销信息成功
	 */
	public static final String RECORD_SUCCESS = "801";
	
	
	/**
	 * 记录分销信息失败
	 */
	public static final String RECORD_FAIL = "802";
	
	/**
	 * 资金账户成功
	 */
	public static final String ACCOUNTS_SUCCESS = "803";
	
	/**
	 * 资金账户成功
	 */
	public static final String ACCOUNTS_FAIL = "804";
	
	
	/**
	 * 流水记录表信息失败
	 */
	public static final String FUNDSERIAL_SUCCESS = "805";
	
	/**
	 * 流水记录表信息失败
	 */
	public static final String FUNDSERIAL_FAIL = "806";
	
	/**
	 * 查看被分享人会员信息成功
	 */
	public static final String FUNDSRELATEUSER_SUCCESS = "807";
	
	/**
	 * 查看被分享人会员信息失败
	 */
	public static final String FUNDSRELATEUSER_FALT = "807";
	
	
	//*****************转账记录***********************************8
	
	/**
	 * 查询转账记录成功 ,转账申请成功
	 */
	public static final String  OUTAPPLY_SUCCESS = "901";
	
	/**\
	 * 查询转账记录失败 ,转账申请失败
	 */
	public static final String  OUTAPPLY_ERROR = "902";
	
	
	
	//*****************资金账户***********************************8
	
	/**
	 * 资金账户表(成功)
	 */
	public static final String ACCOUNTS_SUCESS="701";
	/**
	 * 资金账户表(失败)
	 */
	public static final String ACCOUNTS_ERROR="702";
	
	/**
	 * 取款大于可用余额
	 */
	public static final String FOUNLITTLE="703";
	
}
