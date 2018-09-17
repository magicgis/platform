package com.junl.wpwx.form;

import com.junl.frame.tools.date.DateUtils;

import java.util.Date;

public class AttenForm {

	private Date birthday;
	private String name;
	private String phone;
	private String code;


	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getBirth(){
		return DateUtils.dateParseShortString(birthday);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
