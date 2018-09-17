package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("VacHepbTemp")
public class VacHepbTemp extends BaseEntity<VacHepbTemp> {
	
	private String username; // 姓名
	private String sex; // 性别
	private Date birthday; // 出生日期
	private Integer age; // 年龄
	private String linkPhone; // 联系电话
	private String idcardNo; // 身份证号
	private String weight; // 体重
	private String address; // 详细地址
	private String province; // 省
	private String city; // 市
	private String county; // 区
	private String history; // 既往病史
	
	private String openId; //OPENID 用于pc建档后的微信推送
	private String tempId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getIdcardNo() {
		return idcardNo;
	}

	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getOpenId() {
		return openId;
	}

	public String getTempId() {
		return tempId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

}
