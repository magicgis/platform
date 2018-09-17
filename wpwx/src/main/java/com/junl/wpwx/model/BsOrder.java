package com.junl.wpwx.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("BsOrder")
public class BsOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id; //
	private String orderNo; // 订单号
	private String serialNo; // 流水号
	private String channel; // 渠道号
	private String source; // 来源
	private Date orderTime; // 订单时间
	private Date payTime; // 支付时间
	private Date callTime; // 回调时间
	private String userId; // 用户id
	private String childCode; // 用户/儿童编号
	private String openid; // openID
	private String vaccineId; // 疫苗编号
	private String vaccineType; // 疫苗类型 1-儿童 2-成人
	private String manufacturer; // 生产厂商
	private String batch; // 批号
	private Integer price; // 价格
	private Integer num; // 价格
	private String remarks; // 备注
	private String beneficiary; // 收款方
	private Integer total; // 合计

	public String getId() {
		return id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public String getChannel() {
		return channel;
	}

	public String getSource() {
		return source;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public Date getCallTime() {
		return callTime;
	}

	public String getUserId() {
		return userId;
	}

	public String getChildCode() {
		return childCode;
	}

	public String getOpenid() {
		return openid;
	}

	public String getVaccineId() {
		return vaccineId;
	}

	public String getVaccineType() {
		return vaccineType;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getBatch() {
		return batch;
	}

	public Integer getPrice() {
		return price;
	}

	public Integer getNum() {
		return num;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getBeneficiary() {
		return beneficiary;
	}

	public Integer getTotal() {
		return total;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setChildCode(String childCode) {
		this.childCode = childCode;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public void setVaccineId(String vaccineId) {
		this.vaccineId = vaccineId;
	}

	public void setVaccineType(String vaccineType) {
		this.vaccineType = vaccineType;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
