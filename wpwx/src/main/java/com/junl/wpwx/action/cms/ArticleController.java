/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.action.cms;

import com.alibaba.fastjson.JSONObject;
import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.model.Article;
import com.junl.wpwx.service.vaccinate.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 
 * @author chenmaolong
 * @date 2017年3月3日 上午11:50:21
 * @description 
 *		TODO
 */
@Controller
@RequestMapping(value = "/cms/article")
public class ArticleController extends BaseAction {

	@Autowired
	private ArticleService articleService;
	
	/**
	 * 
	 * @author chenmaolong
	 * @date 2017年3月3日 上午11:55:19
	 * @description 
	 *		TODO 每个方法执行前被执行
	 * @param id
	 * @return
	 *
	 */
	@ModelAttribute
	public Article get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotEmpty(id)){
			return articleService.get(id);
		}else{
			return new Article();
		}
	}
	
	/**
	 * 
	 * @author chenmaolong
	 * @date 2017年3月3日 上午11:54:46
	 * @description 
	 *		TODO 列表
	 * @param article   PROMOTEMESSAGE
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping(value = {"list"})
	public String list(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
        List<Article> findList = articleService.findList(article);
        logger.info(JSONObject.toJSONString(findList));
        model.addAttribute("list", findList);
        
        //行业资讯页面
        if("4".equals(article.getCategoryId())){
        	return "cms/informationList";
		}
		return "cms/classList";
	}

	/**
	 * 
	 * @author chenmaolong
	 * @date 2017年3月3日 上午11:54:34
	 * @description 
	 *		TODO 详情
	 * @param article
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping(value = "detail")
	public String form(Article article, Model model ,String id) {
		// 如果当前传参有子节点，则选择取消传参选择
		article=articleService.get(id);
		model.addAttribute("article", article);
		return "cms/articleDetail";
	}
}
