/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * 栏目Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
@Alias("Category")
public class Category extends BaseEntity<Category> {


	private static final long serialVersionUID = 1L;
//	private Category parent;// 父级菜单
//	private String parentIds;// 所有父级编号
	private String module; 	// 栏目模型（article：文章；picture：图片；download：下载；link：链接；special：专题）
//	private String name; 	// 栏目名称
	private String image; 	// 栏目图片
	private String href; 	// 链接
	private String target; 	// 目标（ _blank、_self、_parent、_top）
	private String description; 	// 描述，填写有助于搜索引擎优化
	private String keywords; 	// 关键字，填写有助于搜索引擎优化
//	private Integer sort; 		// 排序（升序）
	private String inMenu; 		// 是否在导航中显示（1：显示；0：不显示）
	private String inList; 		// 是否在分类页中显示列表（1：显示；0：不显示）
	private String showModes; 	// 展现方式（0:有子栏目显示栏目列表，无子栏目显示内容列表;1：首栏目内容列表；2：栏目第一条内容）
	private String allowComment;// 是否允许评论
	private String isAudit;	// 是否需要审核
	private String customListView;		// 自定义列表视图
	private String customContentView;	// 自定义内容视图
    private String viewConfig;	// 视图参数
    
    private Date beginDate;	// 开始时间
    private Date endDate;	// 结束时间
    private String cnt;//信息量
    private String hits;//点击量
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getInMenu() {
		return inMenu;
	}
	public void setInMenu(String inMenu) {
		this.inMenu = inMenu;
	}
	public String getInList() {
		return inList;
	}
	public void setInList(String inList) {
		this.inList = inList;
	}
	public String getShowModes() {
		return showModes;
	}
	public void setShowModes(String showModes) {
		this.showModes = showModes;
	}
	public String getAllowComment() {
		return allowComment;
	}
	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}
	public String getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}
	public String getCustomListView() {
		return customListView;
	}
	public void setCustomListView(String customListView) {
		this.customListView = customListView;
	}
	public String getCustomContentView() {
		return customContentView;
	}
	public void setCustomContentView(String customContentView) {
		this.customContentView = customContentView;
	}
	public String getViewConfig() {
		return viewConfig;
	}
	public void setViewConfig(String viewConfig) {
		this.viewConfig = viewConfig;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	public String getHits() {
		return hits;
	}
	public void setHits(String hits) {
		this.hits = hits;
	}
	
}