package com.junl.wpwx.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("BsRabies")
public class BsRabies extends BaseEntity<BsRabies> {
	private static final long serialVersionUID = 1L;

	private String username; // 姓名
	private String sex; // 性别
	private String card; // 身份证号
	private Date birthday; // 出生日期
	private Integer age; // 年龄
	private String homeaddress; // 详细地址
	private String linkphone; // 联系电话
	private Date bitedate; // 咬伤时间
	private String bitepart; // 咬伤部位
	private String animal; // 动物名称
	private Date dealdate; // 处理时间
	private String dealaddress; // 处理地点
	private String exposebefore; // 暴露前免疫
	private String exposeafter; // 暴露后免疫
	private String exposelevel; // 暴露级别1,2,3,4,5
	private String vaccinatename; // 疫苗名称
	private String standard; // 规格
	private String manufacturer; // 生产厂家（制造商）
	private String batchnum; // 批号
	private String isinoculate; // 是否接种狂犬病人免疫球蛋白：0，否 1，是
	private String weight; // 受种者体重
	private String dosage; // 接种剂次
	private String province; // 省
	private String city; // 市
	private String county; // 县
	private String address; // 详细地址
	private String expose; // 暴露前后免疫：0，暴露前 1，暴露后
	private String standardNo; // 规格 -- 免疫蛋白
	private String manufacturerNo; // 生产厂家（制造商） -- 免疫蛋白
	private String batchnumNo; // 批号 -- 免疫蛋白
	private String remarks; // 备注
	private String judgmentTimes; // 是否48小时：0，否 1，是
	private String history; // 既往病史
	private String payment; // 是否已付款：0 未付款、1 已付款
	private String vaccinatenameNo; // 疫苗名称

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

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
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

	public String getHomeaddress() {
		return homeaddress;
	}

	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}

	public String getLinkphone() {
		return linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}

	public Date getBitedate() {
		return bitedate;
	}

	public void setBitedate(Date bitedate) {
		this.bitedate = bitedate;
	}

	public String getBitepart() {
		return bitepart;
	}

	public void setBitepart(String bitepart) {
		this.bitepart = bitepart;
	}

	public String getAnimal() {
		return animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

	public Date getDealdate() {
		return dealdate;
	}

	public void setDealdate(Date dealdate) {
		this.dealdate = dealdate;
	}

	public String getDealaddress() {
		return dealaddress;
	}

	public void setDealaddress(String dealaddress) {
		this.dealaddress = dealaddress;
	}

	public String getExposebefore() {
		return exposebefore;
	}

	public void setExposebefore(String exposebefore) {
		this.exposebefore = exposebefore;
	}

	public String getExposeafter() {
		return exposeafter;
	}

	public void setExposeafter(String exposeafter) {
		this.exposeafter = exposeafter;
	}

	public String getExposelevel() {
		return exposelevel;
	}

	public void setExposelevel(String exposelevel) {
		this.exposelevel = exposelevel;
	}

	public String getVaccinatename() {
		return vaccinatename;
	}

	public void setVaccinatename(String vaccinatename) {
		this.vaccinatename = vaccinatename;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getBatchnum() {
		return batchnum;
	}

	public void setBatchnum(String batchnum) {
		this.batchnum = batchnum;
	}

	public String getIsinoculate() {
		return isinoculate;
	}

	public void setIsinoculate(String isinoculate) {
		this.isinoculate = isinoculate;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
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

	public String getExpose() {
		return expose;
	}

	public void setExpose(String expose) {
		this.expose = expose;
	}

	public String getStandardNo() {
		return standardNo;
	}

	public void setStandardNo(String standardNo) {
		this.standardNo = standardNo;
	}

	public String getManufacturerNo() {
		return manufacturerNo;
	}

	public void setManufacturerNo(String manufacturerNo) {
		this.manufacturerNo = manufacturerNo;
	}

	public String getBatchnumNo() {
		return batchnumNo;
	}

	public void setBatchnumNo(String batchnumNo) {
		this.batchnumNo = batchnumNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getJudgmentTimes() {
		return judgmentTimes;
	}

	public void setJudgmentTimes(String judgmentTimes) {
		this.judgmentTimes = judgmentTimes;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getVaccinatenameNo() {
		return vaccinatenameNo;
	}

	public void setVaccinatenameNo(String vaccinatenameNo) {
		this.vaccinatenameNo = vaccinatenameNo;
	}

}
