package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("Order")
public class Order {

    private static final long serialVersionUID = 1L;
    private String id;
    private String orderNo;  // 订单号
    private String source;  // 订单来源[1:APP、2：微信、3小程序]
    private String batch;  // 批号
    private String payPrice;  // 付款金额
    private String payType;  // 支付方式 1、微信  2、支付宝
    private String status;  // 状态【1、待支付 2、已付款】
    private Date orderTime;  // 下单时间
    private String transactionNo;  // 交易流水号
    private Date callbackTime;  // 回调时间
    private String userId;  //  用户标识
    private String vaccineType;  // 疫苗类型(1-儿童，2-成人)
    private String vaccId;  // 疫苗id
    private String childCode;  // 儿童编号
    private String nid;  // 计划id
    private String vaccName;  // 疫苗名称
    private String localcode;  // 接种单位编号
    private String vaccPrice;  // 疫苗价格

    private String vid;  // 預約ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public Date getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(Date callbackTime) {
        this.callbackTime = callbackTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(String vaccineType) {
        this.vaccineType = vaccineType;
    }

    public String getVaccId() {
        return vaccId;
    }

    public void setVaccId(String vaccId) {
        this.vaccId = vaccId;
    }

    public String getChildCode() {
        return childCode;
    }

    public void setChildCode(String childCode) {
        this.childCode = childCode;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getVaccName() {
        return vaccName;
    }

    public void setVaccName(String vaccName) {
        this.vaccName = vaccName;
    }

    public String getLocalcode() {
        return localcode;
    }

    public void setLocalcode(String localcode) {
        this.localcode = localcode;
    }

    public String getVaccPrice() {
        return vaccPrice;
    }

    public void setVaccPrice(String vaccPrice) {
        this.vaccPrice = vaccPrice;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
