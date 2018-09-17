package com.junl.wpwx.model;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 微信自助建档Entity
 * @author fuxin
 * @version 2017-03-02
 */
@Alias("VacChildTemp")
public class VacChildTemp extends BaseEntity<VacChildTemp> {
	
	/**
	 * 状态-已使用
	 */
	public static final String STATUS_YES = "1";
	/**
	 * 状态-未使用
	 */
	public static final String STATUS_NO = "0";
	
	private static final long serialVersionUID = 1L;
	private String childcode; // 儿童编码
	private String cardcode; // 身份证号
	private String birthcode; // 出生证号
	private String childname; // 儿童姓名
	private String gender; // 性别
	private Date birthday; // 出生日期
	private String birthhostipal; // 出生医院名称
	private String birthweight; // 出生体重 kg
	private String guardianname; // 监护人姓名
	private String guardianrelation; // 儿童与监护人的关系--
	private String guardianidentificationnumber; // 监护人的身份证号
	private String homeaddress; // 家庭住址详细地址--
	private String registryaddress; // 户籍地址--
	private String paradoxicalreaction; // 是否异常反应
	private String officeinfo; // 接种单位名称和联系方式
	private Date createdate; // 创建日期--
	private String creater; // 创建者--
	private String guardianmobile; // 监护人手机号码
	private String nation; // 民族
	private String childorder; // 孩次
	private String situation; // 在册情况--
	private String properties; // 户口属性
	private String reside; // 居住属性
	private String area; // 区域划分
	private String father; // 父亲
	private String fatherphone; // 父亲电话
	private String fathercard; // 父亲身份证号
	private String mailingaddress; // 通讯地址--

	private String province; // 省
	private String city; // 市
	private String county; // 县
	private String address; // 详细地址
	private String pr; // 省
	private String ci; // 市
	private String co; // 县
	private String add; // 详细地址
	private String officecode; //接种单位编码
	private String status;  //状态是否使用过 0：未使用 1已使用
	private String officeInfoName; //接种单位名称
	private String tempid; //建档编号
	private String userId; //用户标识
	private String remarks; //备注

	private String type; //标识 0：微信  1：APP
	private String statusType="0"; // 0：儿童免疫 1：狂犬病免疫 2：非常规免疫
	private String fileorigin;    //档案来源 1:本地 2：微信 3：APP

	public String getChildcode() {
		return childcode;
	}

	public void setChildcode(String childcode) {
		this.childcode = childcode;
	}

	public String getCardcode() {
		return cardcode;
	}

	public void setCardcode(String cardcode) {
		this.cardcode = cardcode;
	}

	public String getBirthcode() {
		return birthcode;
	}

	public void setBirthcode(String birthcode) {
		this.birthcode = birthcode;
	}

	public String getChildname() {
		return childname;
	}

	public void setChildname(String childname) {
		this.childname = childname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	public Date getBirthday() {
		return birthday;
	}

	//@JsonDeserialize(using = CustomJsonDateDeserializer.class)  
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getBirthhostipal() {
		return birthhostipal;
	}

	public void setBirthhostipal(String birthhostipal) {
		this.birthhostipal = birthhostipal;
	}

	public String getBirthweight() {
		return birthweight;
	}

	public void setBirthweight(String birthweight) {
		this.birthweight = birthweight;
	}

	public String getGuardianname() {
		return guardianname;
	}

	public void setGuardianname(String guardianname) {
		this.guardianname = guardianname;
	}

	public String getGuardianrelation() {
		return guardianrelation;
	}

	public void setGuardianrelation(String guardianrelation) {
		this.guardianrelation = guardianrelation;
	}

	public String getGuardianidentificationnumber() {
		return guardianidentificationnumber;
	}

	public void setGuardianidentificationnumber(
			String guardianidentificationnumber) {
		this.guardianidentificationnumber = guardianidentificationnumber;
	}

	public String getHomeaddress() {
		return homeaddress;
	}

	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}

	public String getRegistryaddress() {
		return registryaddress;
	}

	public void setRegistryaddress(String registryaddress) {
		this.registryaddress = registryaddress;
	}

	public String getParadoxicalreaction() {
		return paradoxicalreaction;
	}

	public void setParadoxicalreaction(String paradoxicalreaction) {
		this.paradoxicalreaction = paradoxicalreaction;
	}

	public String getOfficeinfo() {
		return officeinfo;
	}

	public void setOfficeinfo(String officeinfo) {
		this.officeinfo = officeinfo;
	}

	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	public Date getCreatedate() {
		return super.createDate;
	}
//	@JsonDeserialize(using = CustomJsonDateDeserializer.class)  
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getGuardianmobile() {
		return guardianmobile;
	}

	public void setGuardianmobile(String guardianmobile) {
		this.guardianmobile = guardianmobile;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getChildorder() {
		return childorder;
	}

	public void setChildorder(String childorder) {
		this.childorder = childorder;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getReside() {
		return reside;
	}

	public void setReside(String reside) {
		this.reside = reside;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getFatherphone() {
		return fatherphone;
	}

	public void setFatherphone(String fatherphone) {
		this.fatherphone = fatherphone;
	}

	public String getFathercard() {
		return fathercard;
	}

	public void setFathercard(String fathercard) {
		this.fathercard = fathercard;
	}

	public String getMailingaddress() {
		return mailingaddress;
	}

	public void setMailingaddress(String mailingaddress) {
		this.mailingaddress = mailingaddress;
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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		if(null == address){
			return "";
		}
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getCo() {
		return co;
	}

	public void setCo(String co) {
		this.co = co;
	}

	public String getAdd() {
		if(null == add){
			return "";
		}
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public String getOfficecode() {
		return officecode;
	}

	public void setOfficecode(String officecode) {
		this.officecode = officecode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOfficeInfoName() {
		return officeInfoName;
	}

	public void setOfficeInfoName(String officeInfoName) {
		this.officeInfoName = officeInfoName;
	}

	public String getTempid() {
		return tempid;
	}

	public void setTempid(String tempid) {
		this.tempid = tempid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}


	public String getFileorigin() {
		return fileorigin;
	}

	public void setFileorigin(String fileorigin) {
		this.fileorigin = fileorigin;
	}
}