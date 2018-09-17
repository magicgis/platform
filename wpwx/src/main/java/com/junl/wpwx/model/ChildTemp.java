package com.junl.wpwx.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 微信自助建档Entity
 * @author fuxin
 * @version 2017-03-02
 */
@Alias("ChildTemp")
public class ChildTemp extends BaseEntity<ChildTemp> {
	/**
	 * 状态-已使用
	 */
	public static final String STATUS_YES = "1";
	/**
	 * 状态-未使用
	 */
	public static final String STATUS_NO = "0";
	private static final long serialVersionUID = 1L;


	private String  userId;    //用户唯一标识
	private String  childName;    //儿童姓名
	private String  childSex;    //儿童性别 0：女 1：男
	private String  childBirthdayNo;    //儿童出生证号
	private String  childIdentity;    //儿童身份证
	private String  childMomName;    //母亲姓名
	private String  childMomIdentity;    //母亲身份证
	private String  childMomMobile;    //母亲电话
	private String  childFatherName;    //父亲姓名
	private String  childFatherIdentity;    //父亲身份证
	private String  childFatherMobile;    //父亲电话
	private String  parityNo;    //胎次
	private Date  childBirthday;    //宝宝生日
	private String  childNationNo;    //宝宝民族编号
	private String  childWeight;    //宝宝体重
	private String  registeredTypeNo;    //户口类别编号
	private String  familyAddress;    //家庭详细地址
	private String  registerAddress;    //户籍详细地址
	private String  liveId;    //居住类别ID
	private String  abnormal;    //异常反应
	private String  remark;    //备注
	private String  hospitalAreaId;    //出生医院区ID
	private String  hospitalId;    //出生医院ID

	private String  province;    //家庭地址省ID
	private String  city;    //家庭地址市ID
	private String  familyaId;    //家庭地址区ID
	private String  pr;    //户籍地址省ID
	private String  ci;    //户籍地址市ID
	private String  registeraId;    //户籍地址区ID

	private String  childcode;    //临时建档编号
	private String  fileorigin;    //
	private String  registrationID;    //激光ID
	
	private String tempid; //建档编号

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public String getChildSex() {
		return childSex;
	}

	public void setChildSex(String childSex) {
		this.childSex = childSex;
	}

	public String getChildBirthdayNo() {
		return childBirthdayNo;
	}

	public void setChildBirthdayNo(String childBirthdayNo) {
		this.childBirthdayNo = childBirthdayNo;
	}

	public String getChildIdentity() {
		return childIdentity;
	}

	public void setChildIdentity(String childIdentity) {
		this.childIdentity = childIdentity;
	}

	public String getChildMomName() {
		return childMomName;
	}

	public void setChildMomName(String childMomName) {
		this.childMomName = childMomName;
	}

	public String getChildMomIdentity() {
		return childMomIdentity;
	}

	public void setChildMomIdentity(String childMomIdentity) {
		this.childMomIdentity = childMomIdentity;
	}

	public String getChildMomMobile() {
		return childMomMobile;
	}

	public void setChildMomMobile(String childMomMobile) {
		this.childMomMobile = childMomMobile;
	}

	public String getChildFatherName() {
		return childFatherName;
	}

	public void setChildFatherName(String childFatherName) {
		this.childFatherName = childFatherName;
	}

	public String getChildFatherIdentity() {
		return childFatherIdentity;
	}

	public void setChildFatherIdentity(String childFatherIdentity) {
		this.childFatherIdentity = childFatherIdentity;
	}

	public String getChildFatherMobile() {
		return childFatherMobile;
	}

	public void setChildFatherMobile(String childFatherMobile) {
		this.childFatherMobile = childFatherMobile;
	}

	public String getParityNo() {
		return parityNo;
	}

	public void setParityNo(String parityNo) {
		this.parityNo = parityNo;
	}

	public Date getChildBirthday() {
		return childBirthday;
	}

	public void setChildBirthday(Date childBirthday) {
		this.childBirthday = childBirthday;
	}

	public String getChildNationNo() {
		return childNationNo;
	}

	public void setChildNationNo(String childNationNo) {
		this.childNationNo = childNationNo;
	}

	public String getChildWeight() {
		return childWeight;
	}

	public void setChildWeight(String childWeight) {
		this.childWeight = childWeight;
	}

	public String getRegisteredTypeNo() {
		return registeredTypeNo;
	}

	public void setRegisteredTypeNo(String registeredTypeNo) {
		this.registeredTypeNo = registeredTypeNo;
	}

	public String getFamilyaId() {
		return familyaId;
	}

	public void setFamilyaId(String familyaId) {
		this.familyaId = familyaId;
	}

	public String getFamilyAddress() {
		return familyAddress;
	}

	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}

	public String getRegisteraId() {
		return registeraId;
	}

	public void setRegisteraId(String registeraId) {
		this.registeraId = registeraId;
	}

	public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getLiveId() {
		return liveId;
	}

	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}

	public String getAbnormal() {
		return abnormal;
	}

	public void setAbnormal(String abnormal) {
		this.abnormal = abnormal;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getHospitalAreaId() {
		return hospitalAreaId;
	}

	public void setHospitalAreaId(String hospitalAreaId) {
		this.hospitalAreaId = hospitalAreaId;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getChildcode() {
		return childcode;
	}

	public void setChildcode(String childcode) {
		this.childcode = childcode;
	}

	public String getFileorigin() {
		return fileorigin;
	}

	public void setFileorigin(String fileorigin) {
		this.fileorigin = fileorigin;
	}

	public String getRegistrationID() {
		return registrationID;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPr() {
		return pr;
	}

	public void setPr(String pr) {
		this.pr = pr;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;


	}
	
	public String getTempid() {
		return tempid;
	}

	public void setTempid(String tempid) {
		this.tempid = tempid;
	}
}