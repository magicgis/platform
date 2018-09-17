package com.junl.wpwx.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperty {

	/**
	 * appid
	 */
	@Value("#{conf.AppID}")
	private String appId;
	
	/**
	 * appSecret
	 */
	@Value("#{conf.AppSecret}")
	private String appSecret;
	
	/**
	 * redirectUri
	 */
	@Value("#{conf.redirect_uri}")
	private String redirectUri;
	
	/** 订单成功 模板id */
	@Value("#{conf.temp_order_finish}")
	private String temp_order_finish;
	/** 购买保险成功 模板id */
	@Value("#{conf.temp_ins_finish}")
	private String temp_ins_finish;
	/** 自助建档犬伤成功 模板id */
	@Value("#{conf.temp_selfhelp_reg_dog}")
	private String temp_selfhelp_reg_dog;


	/** 自助建档成功 模板id */
	@Value("#{conf.temp_selfhelp_reg}")
	private String temp_selfhelp_reg;
	/** 预约成功 模板id */
	@Value("#{conf.temp_reserve_success}")
	private String temp_reserve_success;
	/** 接种提醒 模板id */
	@Value("#{conf.temp_remind_child}")
	private String temp_remind_child;
	/** 接种完成 模板id */
	@Value("#{conf.temp_quene_finish}")
	private String temp_quene_finish;
	/** 留观完成 模板id */
	@Value("#{conf.temp_stay_see}")
	private String temp_stay_see;
	/** 预约取消 模板id */
	@Value("#{conf.temp_canceled_child}")
	private String temp_canceled_child;

	@Value("#{conf.packTake}")
	private String packTake;
	
	@Value("#{conf.erWeiMaTake}")
	private String erWeiMaTake;
	
//	=保险=======================================
	/** 保险-出单用户 */
	@Value("#{conf.ins_user_name}")
	private String ins_user_name;
	/** 保险-渠道号*/
	@Value("#{conf.ins_channel_detail}")
	private String ins_channel_detail;
	/** 保险-计划编码 */
	@Value("#{conf.ins_plan_code}")
	private String ins_plan_code;
//	=保险end====================================

	/** 模板消息跳转头_地址 */
	@Value("#{conf.temp_url}")
	private String temp_url;

	/** 支付宝回调URL */
	@Value("#{conf.aliPayUrl}")
	private String aliPayUrl;
	/** 微信回调URL */
	@Value("#{conf.weixinPayUrl}")
	private String weixinPayUrl;


	@Value("#{conf.imgPath}")
	private String imgPath;

	@Value("#{conf.imgurl}")
	private String imgurl;

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	@Value("#{conf.AppID}")
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the appSecret
	 */
	public String getAppSecret() {
		return appSecret;
	}

	/**
	 * @param appSecret the appSecret to set
	 */
	@Value("#{conf.AppSecret}")
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	/**
	 * @return the redirectUri
	 */
	public String getRedirectUri() {
		return redirectUri;
	}

	/**
	 * @param redirectUri the redirectUri to set
	 */
	@Value("#{conf.redirect_uri}")
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	

	public String getTemp_order_finish() {
		return temp_order_finish;
	}

	public void setTemp_order_finish(String temp_order_finish) {
		this.temp_order_finish = temp_order_finish;
	}

	public String getTemp_quene_finish() {
		return temp_quene_finish;
	}

	public void setTemp_quene_finish(String temp_quene_finish) {
		this.temp_quene_finish = temp_quene_finish;
	}

	public String getTemp_selfhelp_reg() {
		return temp_selfhelp_reg;
	}

	public void setTemp_selfhelp_reg(String temp_selfhelp_reg) {
		this.temp_selfhelp_reg = temp_selfhelp_reg;
	}

	public String getTemp_selfhelp_reg_dog() {
		return temp_selfhelp_reg_dog;
	}

	public void setTemp_selfhelp_reg_dog(String temp_selfhelp_reg_dog) {
		this.temp_selfhelp_reg_dog = temp_selfhelp_reg_dog;
	}
	
	public String getTemp_ins_finish() {
		return temp_ins_finish;
	}

	public void setTemp_ins_finish(String temp_ins_finish) {
		this.temp_ins_finish = temp_ins_finish;
	}

	public String getPackTake() {
		return packTake;
	}

	public void setPackTake(String packTake) {
		this.packTake = packTake;
	}

	public String getErWeiMaTake() {
		return erWeiMaTake;
	}

	public void setErWeiMaTake(String erWeiMaTake) {
		this.erWeiMaTake = erWeiMaTake;
	}
	
	public String getTemp_remind_child() {
		return temp_remind_child;
	}

	public void setTemp_remind_child(String temp_remind_child) {
		this.temp_remind_child = temp_remind_child;
	}



	//=====beeCloud==============================
	/** APP ID */
	private String beeAppId;
	/** APP Secret */
	private String beeSecret;
	/** APP Test Secret */
	private String beeTestSecret;
	/** Master Secret */
	private String beeMaster;

	public String getBeeAppId() {
		return beeAppId;
	}

	@Value("#{conf.bee_appId}")
	public void setBeeAppId(String beeAppId) {
		this.beeAppId = beeAppId;
	}

	public String getBeeSecret() {
		return beeSecret;
	}

	@Value("#{conf.bee_secret}")
	public void setBeeSecret(String beeSecret) {
		this.beeSecret = beeSecret;
	}
	
	
	public String getBeeTestSecret() {
		return beeTestSecret;
	}

	@Value("#{conf.bee_test_secret}")
	public void setBeeTestSecret(String beeTestSecret) {
		this.beeTestSecret = beeTestSecret;
	}

	public String getBeeMaster() {
		return beeMaster;
	}

	@Value("#{conf.bee_master}")
	public void setBeeMaster(String beeMaster) {
		this.beeMaster = beeMaster;
	}

	
	//=====beeCloud END==========================
	
	
	// =保险=======================================
	/**保险-出单用户*/
	public String getIns_user_name() {
		return ins_user_name;
	}

	public void setIns_user_name(String ins_user_name) {
		this.ins_user_name = ins_user_name;
	}

	/**保险-渠道号*/
	public String getIns_channel_detail() {
		return ins_channel_detail;
	}

	public void setIns_channel_detail(String ins_channel_detail) {
		this.ins_channel_detail = ins_channel_detail;
	}

	/**保险-计划编码*/
	public String getIns_plan_code() {
		return ins_plan_code;
	}

	public void setIns_plan_code(String ins_plan_code) {
		this.ins_plan_code = ins_plan_code;
	}
	// =保险end====================================


	public String getTemp_url() {
		return temp_url;
	}

	public void setTemp_url(String temp_url) {
		this.temp_url = temp_url;
	}


	public String getTemp_canceled_child() {
		return temp_canceled_child;
	}

	public void setTemp_canceled_child(String temp_canceled_child) {
		this.temp_canceled_child = temp_canceled_child;
	}

	public String getTemp_reserve_success() {
		return temp_reserve_success;
	}

	public void setTemp_reserve_success(String temp_reserve_success) {
		this.temp_reserve_success = temp_reserve_success;
	}

	public String getTemp_stay_see() {
		return temp_stay_see;
	}

	public void setTemp_stay_see(String temp_stay_see) {
		this.temp_stay_see = temp_stay_see;
	}

	public String getAliPayUrl() {
		return aliPayUrl;
	}

	public void setAliPayUrl(String aliPayUrl) {
		this.aliPayUrl = aliPayUrl;
	}

	public String getWeixinPayUrl() {
		return weixinPayUrl;
	}

	public void setWeixinPayUrl(String weixinPayUrl) {
		this.weixinPayUrl = weixinPayUrl;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
}
