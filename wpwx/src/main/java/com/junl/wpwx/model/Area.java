package com.junl.wpwx.model;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("Area")
public class Area implements Serializable {
	private static final long serialVersionUID = 1L;

	// id
	private String id;
	// 名称
	private String name;
	// 父级id
	private String pid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
