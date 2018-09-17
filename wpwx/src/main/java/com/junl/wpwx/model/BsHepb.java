package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("BsHepb")
public class BsHepb {

	private String username; // 姓名
	private String sex; // 性别
	private Date birthday; // 出生日期
	private Integer age; // 年龄
	private String homeAddress; // 详细地址
	private String linkPhone; // 联系电话
	private String idcardNo; // 身份证号
	private String vaccineName; // 疫苗名称
	private String standard; // 规格
	private String batch; // 批号
	private String inoculationStatus; // 是否接种疫苗：0，否 1，是
	private String weight; // 体重
	private String dosage; // 接种剂量
	private String manufacturer; // 生产厂家（制造商）
	private String tempId; // 建档编号
	private String openId; // 微信建档openid
	private String payStatus; // 是否已付款;0 未付款、1 已付款
	private String history; // 既往病史
	private String address; // 详细地址
	private String province; // 省
	private String city; // 市
	private String county; // 区
	private String searchName; // 查询字段 searchName
	private Integer finishTimes; // 已完成针数
	private Integer totalTimes; // 所有的针数
	private String id;
	private String hepaBcode;

	public String getUsername() {
		return username;
	}

	public String getSex() {
		return sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public Integer getAge() {
		return age;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public String getIdcardNo() {
		return idcardNo;
	}

	public String getVaccineName() {
		return vaccineName;
	}

	public String getStandard() {
		return standard;
	}

	public String getBatch() {
		return batch;
	}

	public String getInoculationStatus() {
		return inoculationStatus;
	}

	public String getWeight() {
		return weight;
	}

	public String getDosage() {
		return dosage;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getTempId() {
		return tempId;
	}

	public String getOpenId() {
		return openId;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public String getHistory() {
		return history;
	}

	public String getAddress() {
		return address;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getCounty() {
		return county;
	}

	public String getSearchName() {
		return searchName;
	}

	public Integer getFinishTimes() {
		return finishTimes;
	}

	public Integer getTotalTimes() {
		return totalTimes;
	}

	public String getId() {
		return id;
	}

	public String getHepaBcode() {
		return hepaBcode;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}

	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public void setInoculationStatus(String inoculationStatus) {
		this.inoculationStatus = inoculationStatus;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public void setFinishTimes(Integer finishTimes) {
		this.finishTimes = finishTimes;
	}

	public void setTotalTimes(Integer totalTimes) {
		this.totalTimes = totalTimes;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setHepaBcode(String hepaBcode) {
		this.hepaBcode = hepaBcode;
	}

}
