package com.junl.wpwx.model;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 微信用户管理Entity
 * @author fuxin
 * @version 2017-03-02
 */
@Alias("VacUser")
public class VacUser extends BaseEntity<VacUser> {
	
	private static final long serialVersionUID = 1L;
	private String nickname;		// 昵称
	private String headpath;		// 头像
	private String bindmobile;		// 绑定手机号
	private String openid;		// 微信授权openID
	private String realname;		// 姓名
	private Date createdate;		// 创建时间

	private String userpwd;	//用户密码
	private Date updatedate;	//更新时间
	private String remarks;	//备注信息


	
	public VacUser() {
		super();
	}

	public VacUser(String id){
		super(id);
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getHeadpath() {
		return headpath;
	}

	public void setHeadpath(String headpath) {
		this.headpath = headpath;
	}
	
	public String getBindmobile() {
		return bindmobile;
	}

	public void setBindmobile(String bindmobile) {
		this.bindmobile = bindmobile;
	}
	
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}


	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	@Override
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}