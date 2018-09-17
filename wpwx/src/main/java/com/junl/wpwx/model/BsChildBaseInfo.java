package com.junl.wpwx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.junl.frame.tools.date.DateUtils;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("BsChildBaseInfo")
public class BsChildBaseInfo extends BaseEntity<BsChildBaseInfo> {
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
	private String guardianrelation; // 儿童与监护人的关系
	private String guardianidentificationnumber; // 监护人的身份证号
	private String homeaddress; // 家庭住址详细地址
	private String registryaddress; // 户籍地址
	private String paradoxicalreaction; // 是否异常反应
	private String officeinfo; // 接种单位名称和联系方式
	private Date createdate; // 创建日期
	private String creater; // 创建者
	private String guardianmobile; //
	private String nation; // 民族
	private String nationName; // 民族
	private String childorder; // 孩次
	private String situation; // 在册情况
	private String properties; // 户口属性
	private String reside; // 居住属性监护人手机号码
	private String area; // 区域划分
	private String father; // 父亲
	private String fatherphone; // 父亲电话
	private String fathercard; // 父亲身份证号
	private String mailingaddress; // 通讯地址

	private String province; // 省
	private String city; // 市
	private String county; // 县
	private String address; // 详细地址
	private String pr; // 省
	private String ci; // 市
	private String co; // 县
	private String add; // 详细地址
	
	private String localCode;
	private String tempid;

	private String age; //年龄
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
		return createdate;
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
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public String getNationName() {
		return nationName;
	}

	public void setNationName(String nationName) {
		this.nationName = nationName;
	}

	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	public String getTempid() {
		return tempid;
	}

	public void setTempid(String tempid) {
		this.tempid = tempid;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getAge() {
		if(this.birthday != null){
			return DateUtils.ageYearMouDay(birthday);
		}
		return "";
	}

	public void setAge(String age) {
		this.age = age;
	}
	
}
