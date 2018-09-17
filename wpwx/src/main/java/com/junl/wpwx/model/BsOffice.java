/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;


/**
 * 民族Entity
 * @author wang
 * @version 2017-03-22
 */
@Alias("BsOffice")
public class BsOffice extends BaseEntity<BsOffice> {
	private static final long serialVersionUID = 1L;
	
	private String code;		// code
	private String name;		// 民族名称	
	
	public BsOffice() {
		super();
	}

	public BsOffice(String id){
		super(id);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}