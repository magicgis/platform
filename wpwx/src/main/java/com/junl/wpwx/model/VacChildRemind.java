package com.junl.wpwx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 儿童接种提醒
 * 
 * @author fuxin
 * @date 2017年8月28日 下午10:06:32
 * @description TODO
 */
@Alias("VacChildRemind")
public class VacChildRemind extends BaseEntity<VacChildRemind> {
	private static final long serialVersionUID = 1L;

	public static final String TEMP_CHILD_REMIND = "C_0004";
	public static final String TEMP_CHILD_SIGN = "1";
	public static final String TEMP_CHILD_STYPE = "2";

	private String childcode;	//儿童编号
	private Date remindDate;	//提醒时间
	private String insuranceId="";		//保险id，没有保险null
	private String localcode;		//接种单位编号
	private String localname;		//接种单位名称
	private String sign;		//签字状态   默认0
	private String vaccId;	//疫苗编号
    private Date selectDate;//自选时段-日期
    private String selectTime;  //自选时段-时间段
	private String code;	//预约码
	private String childname;	//儿童姓名
	private String vaccname; //疫苗名称
    private String peopleNumber; //接种人数
	private String tempNo;	//模板编号
	private String remindVacc;	//下一针疫苗信息
	private String remindGroup;	//下一针疫苗大类id
	private String signatureData;    //签字内容
	private byte[] signature;    //签字内容
	private String stype;	//签字来源  2微信
	private String vid;	//记录id
	private String sid;	//签字id
	private String openid;	//微信id
	private  String nid;
	private int leng = 0; // 记录接口使用长度参数

	private String payStatus;//是否支付 0:未支付 1：已支付
	private String payPrice;//疫苗价格（元）

	private String insuranceName="华夏儿童疫苗无忧险";//保险名称
	private int insurancePrice=0;//保险价格

	private String registrationid;//极光推送ID
	private String phone;//手机号


	public String getChildcode() {
		return childcode;
	}

	public String getTempNo() {
		return tempNo;
	}

	public String getRemindVacc() {
		return remindVacc;
	}

	public String getRemindGroup() {
		return remindGroup;
	}


	public void setChildcode(String childcode) {
		this.childcode = childcode;
	}

	public void setTempNo(String tempNo) {
		this.tempNo = tempNo;
	}

	public void setRemindVacc(String remindVacc) {
		this.remindVacc = remindVacc;
	}

	public void setRemindGroup(String remindGroup) {
		this.remindGroup = remindGroup;
	}

	public String getChildname() {
		return childname;
	}

	public void setChildname(String childname) {
		this.childname = childname;
	}

	public int getLeng() {
		return leng;
	}

	public void setLeng(int leng) {
		this.leng = leng;
	}


	public String getVaccname() {
		return vaccname;
	}

	public void setVaccname(String vaccname) {
		this.vaccname = vaccname;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSignatureData() {
		return signatureData;
	}

	public void setSignatureData(String signatureData) {
		this.signatureData = signatureData;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getLocalcode() {
		return localcode;
	}

	public void setLocalcode(String localcode) {
		this.localcode = localcode;
	}

	public void setRemindDate(Date remindDate) {
		this.remindDate = remindDate;
	}

	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	public Date getRemindDate() {
		return remindDate;
	}

	public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	public Date getSelectDate() {
		return selectDate;
	}

	public void setSelectDate(Date selectDate) {
		this.selectDate = selectDate;
	}

	public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }

    public String getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(String peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getVaccId() {
		return vaccId;
	}

	public void setVaccId(String vaccId) {
		this.vaccId = vaccId;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getLocalname() {
		return localname;
	}

	public void setLocalname(String localname) {
		this.localname = localname;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public int getInsurancePrice() {
		return insurancePrice;
	}

	public void setInsurancePrice(int insurancePrice) {
		this.insurancePrice = insurancePrice;
	}

	public String getRegistrationid() {
		return registrationid;
	}

	public void setRegistrationid(String registrationid) {
		this.registrationid = registrationid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
