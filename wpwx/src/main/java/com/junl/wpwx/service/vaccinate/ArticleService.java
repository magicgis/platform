/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.service.vaccinate;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.mapper.ArticleDataMapper;
import com.junl.wpwx.mapper.ArticleMapper;
import com.junl.wpwx.mapper.CategoryMapper;
import com.junl.wpwx.model.Article;
import com.junl.wpwx.model.ArticleData;

/**
 * 
 * @author chenmaolong
 * @date 2017年3月3日 上午11:43:25
 * @description 
 *		TODO
 */
@Service
@Transactional(readOnly = true)
public class ArticleService {

	/**
	 * 预防接种
	 */
	private static final String category01 = "83a8ef43056b49f8a0070f0535bb9717";
	/**
	 * 办事指南
	 */
	private static final String category02 = "12a3792b45774299ba37d94af2cf779d";
	/**
	 * 育儿知识
	 */
	private static final String category03 = "8995e14e0777486bb0c564815f126c1f";
	/**
	 * 行业资讯
	 */
	private static final String category04 = "33ab8ddc84d74f5a84c1f76cc9f6a1b2";
	
	@Autowired
	private ArticleMapper articleMapper;
	@Autowired
	private ArticleDataMapper articleDataMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	
	
	public Article get(String id) {
		Article entity = articleMapper.get(id);
		ArticleData articleData = articleDataMapper.get(id);
		entity.setArticleData(articleData);
		return entity;
	}
	
	public List<Article> findList(Article article) {
		if(StringUtils.isNotEmpty(article.getCategoryId())){
			if("1".equals(article.getCategoryId())){
				article.setCategoryId(category01);
			}else if("2".equals(article.getCategoryId())){
				article.setCategoryId(category02);
			}else if("3".equals(article.getCategoryId())){
				article.setCategoryId(category03);
			}else if("4".equals(article.getCategoryId())){
				article.setCategoryId(category04);
			}
		}
		return articleMapper.findList(article);
	}

	public List<Article> findByidList(Article article) {
		return articleMapper.findList(article);
	}
	
	@Transactional(readOnly = false)
	public void save(Article article) {
		articleMapper.insert(article);
	}
	
	@Transactional(readOnly = false)
	public void delete(Article article) {
		articleMapper.delete(article);
	}

	//妈咪课堂-预防接种，行业资讯 edit by wn 2018-2-8
	public List<HashMap<String,String>> findByCategoryID(String categoryID) {
		return articleMapper.findByCategoryID(categoryID);
	}
	
	
	
	//妈咪课堂-预防接种详情 edit by wn 2018-2-9
	public String getInocDetailByID(String ID) {
		return articleMapper.getInocDetailByID(ID);
	}
	
}