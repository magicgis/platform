package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;


/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */


/**
 * 微信疾病管理Entity
 * @author fuxin
 * @version 2017-03-03
 */
@Alias("VacKnowledgeSickness")
public class VacKnowledgeSickness extends BaseEntity<VacKnowledgeSickness> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 疾病名称
	private String description;		// 疾病描述
	private String infoid;		// 疫苗id
	private  String img;			//图片
	
	public VacKnowledgeSickness() {
		super();
	}

	public VacKnowledgeSickness(String id){
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}