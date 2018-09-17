package com.junl.frame.core.beans;

import java.util.Date;

/**
 * 
 * @author chenmaolong
 * @date 2016年3月21日 下午6:02:32
 * @description 
 *		用户model
 */
public class UserModel {

	/**
	 * ID
	 */
	private String id;
	
	/**
	 * 登录帐号
	 */
	private String loginName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 状态(1: 启用, 2: 禁用)
	 */
	private int status;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 组织ID
	 */
	private String orgId;
	
	/**
	 * 用户岗位ID
	 */
	private String positionId;
	
	/**
	 * 职务ID
	 */
	private String dutyId;
	
	/**
	 * 排序
	 */
	private int sort;
	
	/**
	 * 用户删除状态(1: 正常, 2: 删除)
	 */
	private int state;

	/**
	 * 关联sys_user_info的姓名
	 */
	private String NAME;
	
	/**
	 * 关联sys_organization的组织
	 */
	private String orgName;
	/**
	 * 用户个人在云盘中的表名
	 */
	private String diskPath;
	/**
	 * 用户上一次登录时间
	 */
	private Date lastLoginTime;
	/**
	 * 记录电脑登录的ip
	 */
	private String ip;
	
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the positionId
	 */
	public String getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId the positionId to set
	 */
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	/**
	 * @return the dutyId
	 */
	public String getDutyId() {
		return dutyId;
	}

	/**
	 * @param dutyId the dutyId to set
	 */
	public void setDutyId(String dutyId) {
		this.dutyId = dutyId;
	}

	/**
	 * @return the sort
	 */
	public int getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(int sort) {
		this.sort = sort;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDiskPath() {
		return diskPath;
	}

	public void setDiskPath(String diskPath) {
		this.diskPath = diskPath;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	
	
}
