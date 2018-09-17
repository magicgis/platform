package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */



/**
 * 微信疫苗知识Entity
 * @author fuxin
 * @version 2017-03-02
 */
@Alias("VacKnowledgeList")
public class VacKnowledgeList extends BaseEntity<VacKnowledgeList> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 疫苗名称
	private String dose;		// 剂次
	private String desc;		// 预防疾病
	private Long infoid;		// 疫苗id
	private String time;		// 接种时间
	private String img;		//图片
	
	public VacKnowledgeList() {
		super();
	}

	public VacKnowledgeList(String id){
		super(id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Long getInfoid() {
		return infoid;
	}

	public void setInfoid(Long infoid) {
		this.infoid = infoid;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}