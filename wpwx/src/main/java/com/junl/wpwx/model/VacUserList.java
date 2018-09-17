/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

/**
 * 移动端-用户表Entity
 * @author wangnan
 * @version 2018-02-01
 */
@Alias("VacUserList")
public class VacUserList extends BaseEntity<VacUserList> {
	
	private static final long serialVersionUID = 1L;
	private String openid;		// openid
	private String uid;		// uid
	private String mark;		// mark
	private String registrationid;		// registrationid
	private Long phone;		// phone
	private String password;		// password
	private String nickname;		// nickname
	private String image;		// image
	private String imagepath;		// imagepath
	
	public VacUserList() {
		super();
	}

	public VacUserList(String id){
		super(id);
	}

	@Length(min=0, max=255, message="openid长度必须介于 0 和 255 之间")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	@Length(min=0, max=255, message="uid长度必须介于 0 和 255 之间")
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@Length(min=0, max=255, message="mark长度必须介于 0 和 255 之间")
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	@Length(min=0, max=255, message="registrationid长度必须介于 0 和 255 之间")
	public String getRegistrationid() {
		return registrationid;
	}

	public void setRegistrationid(String registrationid) {
		this.registrationid = registrationid;
	}
	
	@NotNull(message="phone不能为空")
	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=255, message="password长度必须介于 0 和 255 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Length(min=0, max=255, message="nickname长度必须介于 0 和 255 之间")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Length(min=0, max=255, message="imagepath长度必须介于 0 和 255 之间")
	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	
}