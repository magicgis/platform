package com.junl.wpwx.service.vaccinate;


import java.util.HashMap;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.junl.frame.tools.render.JSONMessage;
import com.junl.wpwx.mapper.VacKnowledgeSicknessMapper;
import com.junl.wpwx.model.VacKnowledgeSickness;

/**
 * 微信疾病知识Service
 * @author fuxin
 * @version 2017-03-02
 */
@Service
@Transactional(readOnly = true)
public class VacKnowledgeSicknessService {

	@Autowired
	VacKnowledgeSicknessMapper mapper;
	
	
	/**
	 * 根据id获取一条记录
	 * @author fuxin
	 * @date 2017年3月3日 下午3:16:06
	 * @description 
	 *		TODO
	 * @param id
	 * @return
	 *
	 */
	public VacKnowledgeSickness get(String id) {
		return mapper.get(id);
	}

	/**
	 * 查询所有记录，传入实例化对象可进行筛选和排序
	 * @author fuxin
	 * @date 2017年3月3日 下午3:15:14
	 * @description 
	 *		TODO
	 * @param sickness
	 * @return
	 *
	 */
	public List<VacKnowledgeSickness> findList(VacKnowledgeSickness sickness) {
		return mapper.findList(sickness);
	}

	
	//查询疾病列表 edit by wangnan 2018-2-9
	public List<HashMap<String,String>> findSickList(){
		return mapper.findSickList();
		
	}
	
	/**
	* 妈咪课堂-疾病知识详情 vac_knowledge_sickness 表
	* edit by wangnan 2018-2-9
	*/
	public String getSickDetail(String ID){
		return mapper.getSickDetail(ID);
		
	}
	
}