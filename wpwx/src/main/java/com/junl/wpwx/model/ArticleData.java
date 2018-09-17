/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;

/**
 * 文章Entity
 * @author ThinkGem
 * @version 2013-01-15
 */
@Alias("ArticleData")
public class ArticleData extends BaseEntity<ArticleData> {

	private static final long serialVersionUID = 1L;
	private String id;		// 编号
	private String content;	// 内容
	private String copyfrom;// 来源
	private String relation;// 相关文章
	private String allowComment;// 是否允许评论

	private Article article;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCopyfrom() {
		return copyfrom;
	}

	public void setCopyfrom(String copyfrom) {
		this.copyfrom = copyfrom;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
	


}