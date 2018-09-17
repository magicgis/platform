/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.action.knowledge;

import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.model.VacKnowledgeInfo;
import com.junl.wpwx.model.VacKnowledgeList;
import com.junl.wpwx.model.VacKnowledgeSickness;
import com.junl.wpwx.service.vaccinate.VacKnowledgeInfoService;
import com.junl.wpwx.service.vaccinate.VacKnowledgeListService;
import com.junl.wpwx.service.vaccinate.VacKnowledgeSicknessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信疫苗知识Controller
 * @author fuxin
 * @version 2017-03-02
 */
@Controller
@RequestMapping(value = "/know")
public class KnowledgeController extends BaseAction {

	@Autowired
	private VacKnowledgeListService listService;
	@Autowired
	private VacKnowledgeInfoService infoService;
	@Autowired
	private VacKnowledgeSicknessService sicknessService;
	

	/**
	 * 疫苗信息列表
	 * @author fuxin
	 * @date 2017年3月3日 下午3:13:06
	 * @description 
	 *		TODO
	 * @param model
	 * @return
	 * @throws SQLException
	 *
	 */
	@RequestMapping("/vaccinate/list")
	public String vaccinateList(Model model) throws SQLException{
		VacKnowledgeList knowlist = new VacKnowledgeList();
		knowlist.setOrderBy("a.ID ASC");
		List<VacKnowledgeList> list = listService.findList(knowlist);
		//数组转树形
		String op = "first";
		List<VacKnowledgeList> templist = new ArrayList<>();
		List<List<VacKnowledgeList>> returnlist = new ArrayList<>();
		for(VacKnowledgeList l : list){
			if(op.equals("first")){
				op = l.getTime();
			}
			if(!op.equals(l.getTime())){
				returnlist.add(templist);
				templist = new ArrayList<>();
				op = l.getTime();
			}
			templist.add(l);
		}
		returnlist.add(templist);
		//数组转树形 结束
		
		model.addAttribute("list", returnlist);
		return "knowledge/knowledge-list-of-the-vaccine";
	}
	
	/**
	 * 疫苗详细信息
	 * @author fuxin
	 * @date 2017年3月3日 下午3:13:22
	 * @description 
	 *		TODO
	 * @param model
	 * @param id
	 * @return
	 * @throws SQLException
	 *
	 */
	@RequestMapping("/vaccinate/info/{id}")
	public String vaccinateInfo(Model model, @PathVariable("id")String id) throws SQLException{
		VacKnowledgeInfo info = infoService.get(id);
		model.addAttribute("info", info);
		return "knowledge/knowledge-of-the-vaccine";
	}
	
	/**
	 * 疾病列表
	 * @author fuxin
	 * @date 2017年3月3日 下午3:13:43
	 * @description 
	 *		TODO
	 * @param model
	 * @return
	 * @throws SQLException
	 *
	 */
	@RequestMapping("/sickness/list")
	public String sicknessList(Model model) throws SQLException{
		VacKnowledgeSickness sickness = new VacKnowledgeSickness();
		List<VacKnowledgeSickness> list = sicknessService.findList(sickness);
		model.addAttribute("list", list);
		return "knowledge/knowledge-list-of-diseases";
	}
	
	/**
	 * 疾病详细信息
	 * @author fuxin
	 * @date 2017年3月3日 下午3:13:53
	 * @description 
	 *		TODO
	 * @param model
	 * @param id
	 * @return
	 * @throws SQLException
	 *
	 */
	@RequestMapping("/sickness/info/{id}")
	public String sicknessInfo(Model model, @PathVariable("id")String id) throws SQLException{
		VacKnowledgeSickness info = sicknessService.get(id);
		model.addAttribute("info", info);
		return "knowledge/knowledge-of-the-diseases";
	}


}