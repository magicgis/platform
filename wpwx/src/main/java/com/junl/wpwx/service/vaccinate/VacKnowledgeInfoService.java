package com.junl.wpwx.service.vaccinate;


/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junl.wpwx.mapper.VacKnowledgeInfoMapper;
import com.junl.wpwx.model.VacKnowledgeInfo;

/**
 * 微信疾病知识Service
 * @author fuxin
 * @version 2017-03-02
 */
@Service
@Transactional(readOnly = true)
public class VacKnowledgeInfoService {

	@Autowired
	VacKnowledgeInfoMapper vacKnowledgeInfoMapper;
	
	
	/**
	 * 根据id获取一条记录
	 * @author fuxin
	 * @date 2017年3月3日 下午3:15:31
	 * @description 
	 *		TODO
	 * @param id
	 * @return
	 *
	 */
	public VacKnowledgeInfo get(String id) {
		return vacKnowledgeInfoMapper.get(id);
	}

	
	
}