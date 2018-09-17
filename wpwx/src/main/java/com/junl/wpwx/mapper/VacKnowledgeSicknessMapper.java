package com.junl.wpwx.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.junl.wpwx.model.VacKnowledgeSickness;



/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */


/**
 * 微信疾病管理DAO接口
 * @author fuxin
 * @version 2017-03-03
 */
public interface VacKnowledgeSicknessMapper extends CrudMapper<VacKnowledgeSickness> {
	
	//查询疾病列表 edit by wangnan 2018-2-9
	public List<HashMap<String,String>> findSickList();
	
	/**
	* 妈咪课堂-疾病知识详情 vac_knowledge_sickness 表
	* edit by wangnan 2018-2-9
	*/
	public String getSickDetail(@Param(value="ID")String ID);
	
	
}