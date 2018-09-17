package com.junl.wpwx.mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.junl.wpwx.model.VacKnowledgeList;



/**
 * 
 * @class MemberMapper
 * @author LEON
 * @date 2015年8月24日 下午7:15:28
 * @description
 *		TODO
 *
 */
public interface VacKnowledgeListMapper extends CrudMapper<VacKnowledgeList> {
	
	//查询疫苗列表 edit by wangnan 2018-2-9
	public List<HashMap<String,String>> findKnowList();
	
	//查询疫苗知识详情 edit by wangnan 2018-2-9
	public String getKnowDetail(@Param(value="ID")String ID);
		
}
