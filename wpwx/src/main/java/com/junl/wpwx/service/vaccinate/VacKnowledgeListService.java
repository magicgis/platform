package com.junl.wpwx.service.vaccinate;


/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junl.wpwx.mapper.VacKnowledgeListMapper;
import com.junl.wpwx.model.VacKnowledgeList;

/**
 * 微信疫苗知识Service
 * @author fuxin
 * @version 2017-03-02
 */
@Service
@Transactional(readOnly = true)
public class VacKnowledgeListService {

	
	@Autowired
	VacKnowledgeListMapper mapper;
	
	/**
	 * 根据id获取一条记录
	 * @author fuxin
	 * @date 2017年3月3日 下午3:15:58
	 * @description 
	 *		TODO
	 * @param id
	 * @return
	 * @throws SQLException
	 *
	 */
	public VacKnowledgeList get(String id) throws SQLException{
		return mapper.get(id);
		
	}
	
	/**
	 * 查询所有记录，传入实例化对象可进行筛选和排序
	 * @author fuxin
	 * @date 2017年3月3日 下午3:14:25
	 * @description 
	 *		TODO
	 * @param vacKnowledgeList
	 * @return
	 * @throws SQLException
	 *
	 */
	public List<VacKnowledgeList> findList(VacKnowledgeList vacKnowledgeList) throws SQLException{
		return mapper.findList(vacKnowledgeList);
		
	}
	
	//查询疫苗列表 edit by wangnan 2018-2-9
	public List<HashMap<String,String>> findKnowList(){
		return mapper.findKnowList();
		
	}
	
	
	//查询疫苗知识详情 edit by wangnan 2018-2-9
	public String getKnowDetail(String ID){
		return mapper.getKnowDetail(ID);
		
	}
	
}