package com.junl.wpwx.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.junl.wpwx.model.Article;



/**
 * 
 * @author chenmaolong
 * @date 2017年3月3日 上午11:24:41
 * @description 
 *		TODO
 */
public interface ArticleMapper extends CrudMapper<Article> {

	//妈咪课堂-预防接种，行业资讯 edit by wn 2018-2-8
	public List<HashMap<String,String>> findByCategoryID(@Param(value = "categoryID")String categoryID);
	

	//妈咪课堂-预防接种详情 edit by wn 2018-2-9
	public String getInocDetailByID(@Param(value = "ID")String ID);
		
		
}
