package com.junl.wpwx.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;


/**
 * 保险
 * @author fuxin
 * @date 2017年4月27日 下午6:25:26
 * @description 
 *		TODO
 */
@Alias("VacInsurance")
public class VacInsurance extends BaseEntity<VacInsurance> {
	private static final long serialVersionUID = 1L;

	/** 保险订单类型 - 投保 */
	public static final String TYPE_NEW = "1";
	/** 保险订单类型 - 退保 */
	public static final String TYPE_CANCEL = "2";
	/** 保险订单请求状态 - 请求成功 */
	public static final String SUCCESS_YES = "1";
	/** 保险订单请求状态 - 请求失败 */
	public static final String SUCCESS_NO = "0";
	
	/** 保险状态 - 未完成 */
	public static final String STATUS_WAIT = "0";
	/** 保险状态 - 正常 */
	public static final String STATUS_NORMAL = "1";
	/** 保险状态 - 过期 */
	public static final String STATUS_PAST = "2";
	/** 保险状态 - 取消 */
	public static final String STATUS_CANCEL = "3";

	private String insReq; // 请求
	private String insRsv; // 返回
	private String insType; // 类型1-投保 2-退保
	private Date insCreateDate; // 创建时间
	private String insChildcode; // 儿童编号
	private String insRid; // 记录
	private String insNid; // 计划
	private String insSuccess;	//是否成功
	
	private String insPolicyNo;		//保单号
	private String insSerialNo;		//交易流水号
	private String insStatus;		//保险状态
	private Date insRefundDate;		//退保时间
	
	public String getInsReq() {
		return insReq;
	}

	public void setInsReq(String insReq) {
		this.insReq = insReq;
	}

	public String getInsRsv() {
		return insRsv;
	}

	public void setInsRsv(String insRsv) {
		this.insRsv = insRsv;
	}

	public String getInsType() {
		return insType;
	}

	public void setInsType(String insType) {
		this.insType = insType;
	}

	public Date getInsCreateDate() {
		return insCreateDate;
	}

	public void setInsCreateDate(Date insCreateDate) {
		this.insCreateDate = insCreateDate;
	}

	public String getInsChildcode() {
		return insChildcode;
	}

	public void setInsChildcode(String insChildcode) {
		this.insChildcode = insChildcode;
	}

	public String getInsRid() {
		return insRid;
	}

	public void setInsRid(String insRid) {
		this.insRid = insRid;
	}

	public String getInsNid() {
		return insNid;
	}

	public void setInsNid(String insNid) {
		this.insNid = insNid;
	}

	public String getInsSuccess() {
		return insSuccess;
	}

	public void setInsSuccess(String insSuccess) {
		this.insSuccess = insSuccess;
	}

	public String getInsPolicyNo() {
		return insPolicyNo;
	}

	public void setInsPolicyNo(String insPolicyNo) {
		this.insPolicyNo = insPolicyNo;
	}

	public String getInsSerialNo() {
		return insSerialNo;
	}

	public void setInsSerialNo(String insSerialNo) {
		this.insSerialNo = insSerialNo;
	}

	public String getInsStatus() {
		return insStatus;
	}

	public void setInsStatus(String insStatus) {
		this.insStatus = insStatus;
	}

	public Date getInsRefundDate() {
		return insRefundDate;
	}

	public void setInsRefundDate(Date insRefundDate) {
		this.insRefundDate = insRefundDate;
	}
	
	
	
	
	

}
