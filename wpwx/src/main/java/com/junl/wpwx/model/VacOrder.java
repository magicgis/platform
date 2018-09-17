package com.junl.wpwx.model;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * 微信订单管理Entity
 * @author fuxin
 * @version 2017-03-02
 */

@Alias("VacOrder")
public class VacOrder extends BaseEntity<VacOrder> {
	
	private static final long serialVersionUID = 1L;
	
	/**订单状态-等待付款 */
	public final static int STATUS_WAIT = 0;
	/**订单状态-已付款 */
	public final static int STATUS_FINISH = 1;
	/**订单状态-取消 */
	public final static int STATUS_CANCEL = 2;
	/**订单状态-关闭 */
	public final static int STATUS_CLOSE = 3;
	
	/** 支付方式-微信支付 */
	public final static int PAYTYPE_WEIXINPAY = 1;
	/** 支付方式-支付宝支付 */
	public final static int PAYTYPE_ALIPAY = 2;
	
	/** 订单来源-微信 */
	public final static String UTMSOURCE_WEIXIN = "wechat";
	/** 订单来源-一体机 */
	public final static String UTMSOURCE_SELFMACHINE = "selfmachine";
	/** 订单来源-一体机 */
	public final static String UTMSOURCE_WEIXIN_REFUND = "wechat_refund";
	
	/** 是否接种-否 */
	public final static int ISVACCINE_NO = 0;
	/** 是否接种-是 */
	public final static int ISVACCINE_YES = 1;
	/** 是否保险-否 */
	public final static String INSURANCE_NO = "0";
	/** 是否保险-是 */
	public final static String INSURANCE_YES = "1";
	
	private String orderNo;		// 订单编号
	private String productid;		// 产品id
	private String productname;		// 产品名称
	private String userid;		// 用户id
	private int payprice;		// 付款金额
	private Integer paytype;		// 支付方式 1、微信  2、支付宝
	private Integer status;		// 状态【1、待支付 2、已付款】
	private Date ordertime;		// 下单时间
	private Date paytime;		// 付款时间
	private String mobile;		// 联系电话
	private String transactionno;		// 交易流水号
	private Date callbacktime;		// 回调时间
	private String utmsource;		// 订单来源：wechat（微信）、selfmachine（一体机）
	private Integer isvaccine;		// 是否接种：0、否 1、是
	private String childcode;		//儿童编号
	private String numid;
	private String insurance;		//是否购买保险
	
	private BsProduct product;		//产品信息
	private String orderStatus;
	
	public VacOrder() {
		super();
	}

	public VacOrder(String id){
		super(id);
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}
	
	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	/**
	 * 订单金额 单位（分）
	 * @author fuxin
	 * @date 2017年3月9日 上午10:38:06
	 * @description 
	 *		TODO
	 * @param payprice
	 *
	 */
	public int getPayprice() {
		return payprice;
	}

	/**
	 * 订单金额 单位（分）
	 * @author fuxin
	 * @date 2017年3月9日 上午10:38:06
	 * @description 
	 *		TODO
	 * @param payprice
	 *
	 */
	public void setPayprice(int payprice) {
		this.payprice = payprice;
	}
	
	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Date getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	
	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getTransactionno() {
		return transactionno;
	}

	public void setTransactionno(String transactionno) {
		this.transactionno = transactionno;
	}
	
	public Date getCallbacktime() {
		return callbacktime;
	}

	public void setCallbacktime(Date callbacktime) {
		this.callbacktime = callbacktime;
	}
	
	public String getUtmsource() {
		return utmsource;
	}

	public void setUtmsource(String utmsource) {
		this.utmsource = utmsource;
	}
	
	public Integer getIsvaccine() {
		return isvaccine;
	}

	public void setIsvaccine(Integer isvaccine) {
		this.isvaccine = isvaccine;
	}

	public String getChildcode() {
		return childcode;
	}

	public void setChildcode(String childcode) {
		this.childcode = childcode;
	}

	public String getNumid() {
		return numid;
	}

	public void setNumid(String numid) {
		this.numid = numid;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public BsProduct getProduct() {
		return product;
	}

	public void setProduct(BsProduct product) {
		this.product = product;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
	
	
}