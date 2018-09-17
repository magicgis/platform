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
@Alias("BsNation")
public class BsNation extends BaseEntity<BsNation> {
	private static final long serialVersionUID = 1L;
	
	private String code;		// code
	private String name;		// 民族名称
	private String sort;		// 排序
	
	public BsNation() {
		super();
	}

	public BsNation(String id){
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
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
}