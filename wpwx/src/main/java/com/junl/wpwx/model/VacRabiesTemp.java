package com.junl.wpwx.model;

import com.junl.frame.tools.date.DateUtils;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("VacRabiesTemp")
public class VacRabiesTemp extends BaseEntity<VacRabiesTemp> {
	private static final long serialVersionUID = 1L;
	/** 字典-动物名称 */
	public static final String DICTTYPE_ANIMAL = "animal";
	/** 字典-咬伤部位 */
	public static final String DICTTYPE_BITEPART = "bite";
	/** 字典-处理地点 */
	public static final String DICTTYPE_DEALADDRESS = "disposal_sites";
	/** 字典-咬伤方式 */
	public static final String DICTTYPE_BITETYPE = "biteType";

	private String username; // 姓名
	private String sex; // 性别 1:男性,2:女性
	private String linkphone; // 联系电话
	private Date bitedate; // 咬伤时间
	private String bitepart; // 咬伤部位
	private String animal; // 动物名称
	private Date dealdate; // 处理时间
	private String dealaddress; // 处理地点
	private String card; // 身份证号
	private String weight; // 受种者体重
	private String province; // 省
	private String city; // 市
	private String county; // 区
	private String address; // 区
	private String status; // 状态0正常
	private String openid; //OPENID 用于pc建档后的微信推送
	private String tempid;
	private String bitetype;
	
	private Integer age;
	
	private String rtype;
	
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

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getBitedateStr(){
		if(null != this.bitedate){
			return DateUtils.dateToString(this.bitedate);
		}else{
			return "";
		}
	}
	
	public String getDealdateStr(){
		if(null != this.dealdate){
			return DateUtils.dateToString(this.dealdate);
		}else{
			return "";
		}
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getTempid() {
		return tempid;
	}

	public void setTempid(String tempid) {
		this.tempid = tempid;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getBitetype() {
		return bitetype;
	}

	public void setBitetype(String bitetype) {
		this.bitetype = bitetype;
	}

	public String getRtype() {
		return rtype;
	}

	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	

	
	

}
