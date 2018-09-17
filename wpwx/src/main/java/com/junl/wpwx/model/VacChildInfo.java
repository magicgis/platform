package com.junl.wpwx.model;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import com.junl.frame.tools.date.DateUtils;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 微信关注宝宝表Entity
 * @author fuxin
 * @version 2017-03-02
 */
@Alias("VacChildInfo")
public class VacChildInfo extends BaseEntity<VacChildInfo> {
	
	private static final long serialVersionUID = 1L;
	private String childcode;		// 儿童编码
	private String cardcode;		// 身份证号
	private String birthcode;		// 出生证号
	private String childname;		// 儿童姓名
	private String guardianname;		// 监护人姓名
	private String guardianrelation;		// 儿童与监护人的关系
	private String guardianmobile;		// 监护人手机号
	private Date createdate;		// 创建日期
	private String userid;		// 用户ID
	private Date birthday;
	
	public VacChildInfo() {
		super();
	}

	public VacChildInfo(String id){
		super(id);
	}

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
	
	public String getGuardianmobile() {
		return guardianmobile;
	}

	public void setGuardianmobile(String guardianmobile) {
		this.guardianmobile = guardianmobile;
	}
	
	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public static VacChildInfo parse(BsChildBaseInfo bsInfo) {
		if(bsInfo == null){
			return null;
		}
		VacChildInfo info = new VacChildInfo();
		info.setChildcode(bsInfo.getChildcode());
		info.setCardcode(bsInfo.getCardcode());
		info.setChildname(bsInfo.getChildname());
		info.setCreatedate(new Date());
		info.setBirthday(bsInfo.getBirthday());
		info.setGuardianmobile(bsInfo.getGuardianmobile());
		info.setGuardianname(bsInfo.getGuardianname());
		return info;
	}
	
	public String getYear(){
		if(this.birthday != null){
			return DateUtils.ageYearMouDay(birthday);
		}
		return "";
	}
	
}