package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */



/**
 * 微信疾病知识Entity
 * @author fuxin
 * @version 2017-03-02
 */
@Alias("VacKnowledgeInfo")
public class VacKnowledgeInfo extends BaseEntity<VacKnowledgeInfo> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 疫苗名称
	private String prevent;		// 预防疾病
	private String before;		// 接种前注意事项
	private String after;		// 接种后注意事项
	
	public VacKnowledgeInfo() {
		super();
	}

	public VacKnowledgeInfo(String id){
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPrevent() {
		return prevent;
	}

	public void setPrevent(String prevent) {
		this.prevent = prevent;
	}
	
	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}
	
	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}
	
}