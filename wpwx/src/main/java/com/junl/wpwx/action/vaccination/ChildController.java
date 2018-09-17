/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.action.vaccination;

import com.junl.frame.tools.date.DateUtils;
import com.junl.frame.tools.net.WebUtils;
import com.junl.frame.tools.render.JSONMessage;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.common.Hash;
import com.junl.wpwx.form.AttenForm;
import com.junl.wpwx.model.*;
import com.junl.wpwx.service.vaccinate.VacChildInfoService;
import com.junl.wpwx.service.vaccinate.VacChildTempService;
import com.junl.wpwx.service.vaccinate.VacService;
import com.junl.wpwx.service.weixin.WeixinjsSDKImpl;
import com.junl.wpwx.vo.Select2Vo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;

/**
 * 微信关注宝宝表Controller
 * @author fuxin
 * @version 2017-03-02
 */
@Controller
@RequestMapping(value = "/child")
public class ChildController extends BaseAction {

	@Autowired
	private VacChildInfoService childInfoService;
	@Autowired
	private VacChildTempService childTempService;
	@Autowired
	private VacService vacService;
	@Autowired
	private WeixinjsSDKImpl weixinjsSDKImpl;
	
	/**
	 * 关注宝宝-form
	 * @author fuxin
	 * @date 2017年3月13日 上午10:14:17
	 * @description 
	 *		TODO
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping(value="/info/form")
	public String infoForm(Model model) {
		return "/vaccination/attention";
	}
	
	
	/**
	 * 关注宝宝-save
	 * @author fuxin
	 * @date 2017年3月13日 上午10:14:01
	 * @description 
	 *		TODO
	 * @param request
	 * @param response
	 * @param model
	 * @param info
	 * @return
	 *
	 */
	@RequestMapping(value="/info/save", method=RequestMethod.POST)
	public String infoSave(HttpServletRequest request, HttpServletResponse response, Model model ,VacChildInfo info){
		//表单数据验证
		if(!StringUtils.isNotBlank(info.getChildcode()) || !StringUtils.isNotBlank(info.getChildname())){
			model.addAttribute("info", info);
			return infoForm(model);
		}
		info.setUserid((String)WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
		childInfoService.save(info);
		return "forward:/vac/index.do";
	}
	
	@RequestMapping(value="/info/atten", method=RequestMethod.POST)
	public String infoatten(HttpServletRequest request, HttpServletResponse response, Model model ,AttenForm info){
		//表单数据验证 USERID
		if(info.getBirthday() == null || !StringUtils.isNotBlank(info.getName()) || !StringUtils.isNotBlank(info.getPhone())) {
			model.addAttribute("info", info);
			model.addAttribute("msg", "关注宝宝失败");
			return "/vaccination/attention";
		}else{
			BsChildBaseInfo bsInfo = vacService.chooseBaseInfo(info);
			VacChildInfo i = VacChildInfo.parse(bsInfo);
			if(i != null){
				//验证是否已被关注
				String userid = (String)WebUtils.getRequest().getSession().getAttribute("wpwx.userid");
				VacChildInfo tempinfo = new VacChildInfo();
				tempinfo.setUserid(userid);
				tempinfo.setChildcode(i.getChildcode());
				List<VacChildInfo> infos =childInfoService.findList(tempinfo);
				if(infos.size() > 0){
					model.addAttribute("msg", "宝宝已被关注");
					return "forward:/vac/index.do?code=" + i.getChildcode();
				}
				i.setUserid((String)WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
				childInfoService.save(i);
			}else{
				model.addAttribute("info", info);
				model.addAttribute("msg", "宝宝建档信息尚未同步");
			}
		}
		return "forward:/vac/index.do";
	}
	
	@RequestMapping("/info/cancelAtten")
	public @ResponseBody String cancelAtten(Model model, String childcode){
		try {
			//验证是否已被关注
			String userid = (String)WebUtils.getRequest().getSession().getAttribute("wpwx.userid");
			VacChildInfo tempinfo = new VacChildInfo();
			tempinfo.setUserid(userid);
			List<VacChildInfo> infos =childInfoService.findList(tempinfo);
			if(infos.size() > 0){
				for(VacChildInfo ii : infos){
					if(childcode.equals(ii.getChildcode())){
						childInfoService.delete(ii);
					}
				}
				return "200";			
			}
		} catch (Exception e) {
			logger.error("取消关注失败",e);
			return "500";
		}
		return "500";
	}
	
	
	/**
	 * 自助建档-form
	 * @author fuxin
	 * @date 2017年3月13日 上午10:14:31
	 * @description 
	 *		TODO
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping(value="/temp/form")
	public String tempForm(Model model) {
		model.addAttribute("nationlist", vacService.getNationList());
		model.addAttribute("hostipallist", vacService.getHostipallist());
		model.addAttribute("communitylist", vacService.getCommunitylist());
//		model.addAttribute("deptlist", vacService.getDeptlist());
		model.addAttribute("paradoxicalreaction", vacService.getDictList("paradoxicalreaction"));
		model.addAttribute("properties", vacService.getDictList("properties"));
		model.addAttribute("reside", vacService.getDictList("reside"));
		model.addAttribute("nation", vacService.getDictList("nation"));
		return "/vaccination/baby-info";
	}
	
	@RequestMapping(value="/listDept/{code}",method = RequestMethod.POST) //value="/baseinfo/{code}"
	public @ResponseBody List<Select2Vo> listDept(@PathVariable("code") String code ) throws UnsupportedEncodingException{
		List<Select2Vo> selList = new ArrayList<>();
		SysDept dept = new SysDept();
		dept.setCode(code);
		List<SysDept> list = vacService.findDeptList(dept);
		//转为select2的格式
		for(SysDept sd : list){
			selList.add(sd.getSelect2Vo());
		}
		return selList;
	}

	@RequestMapping(value="/offarea/{code}",method = RequestMethod.POST) //value="/baseinfo/{code}"
	public @ResponseBody List<SimpleModel> offarea(@PathVariable("code") String code ) throws UnsupportedEncodingException{
		List<SimpleModel> selList = new ArrayList<>();
		selList = vacService.getCommunity(code);//vacService.getCommunitylist()
		return selList;
	}

	/**
	 * 自助建档-save
	 * @author fuxin
	 * @date 2017年3月13日 上午10:14:31
	 * @description 
	 *		TODO
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping(value="/temp/save", method=RequestMethod.POST)
	public @ResponseBody JSONMessage tempSave(HttpServletRequest request, HttpServletResponse response, Model model ,VacChildTemp temp){
		//表单数据验证
		if(!StringUtils.isNotBlank(temp.getChildname())){
			//model.addAttribute("temp", temp);
			//return tempForm(model);
			return  new JSONMessage(false, "参数错误！");
		}
		temp= childTempService.save(temp);
		//temp.setOfficeInfoName(vacService.getDeptlistByCode(temp.getOfficeinfo()).getName());
	/*	temp = vacService.updateAddr(temp);
		temp = vacService.updateDetail(temp);*/
		//model.addAttribute("temp", temp);
		//return "/vaccination/registration-information";
		Map map =new HashMap();
		map.put("id", temp.getChildcode());
		return  new JSONMessage(true, map);
	}

	@RequestMapping(value="/getSign", method=RequestMethod.GET)
	public @ResponseBody Map getSign(){
		String url="http://qbcs.fuxinx5.xin/wpwx/child/temp/form.do";
		Map map= weixinjsSDKImpl.getSign(url);
		return  map;

	}

	
	/**
	 *档案详情
	 */
	@RequestMapping(value="/temp/detail")
	public String tempDetail(HttpServletRequest request, HttpServletResponse response, Model model ,VacChildTemp temp){
		if(null != temp && StringUtils.isNotBlank(temp.getChildcode())){
			VacChildTemp baseinfo = childTempService.getByCode(temp.getChildcode());
			if(null == baseinfo){
				model.addAttribute("msg", "您的编号已过期");
				return "forward:/vac/index.do";
			}
			baseinfo = vacService.updateAddr(baseinfo);
			baseinfo = vacService.updateDetail(baseinfo);
			model.addAttribute("temp", baseinfo);
			return "/vaccination/registration-information";
		}
		model.addAttribute("msg", "您的编号已过期");
		return "forward:/vac/index.do";
	}
	
	/**
	 * 查询儿童信息
	 * @author fuxin
	 * @date 2017年5月18日 下午3:49:44
	 * @description 
	 *		TODO
	 * @param request
	 * @param response
	 * @param model
	 * @param code
	 * @return
	 *
	 */
	@RequestMapping(value="/baseinfo/{code}")
	public String baseInfoDetail(HttpServletRequest request, HttpServletResponse response, Model model ,@PathVariable("code") String code){
			BsChildBaseInfo baseinfo = vacService.getBsChildBaseInfoByCode(code);
			if(null == baseinfo){
				model.addAttribute("error", "儿童信息不存在");
				return "forward:/vac/index.do";
			}
			baseinfo = vacService.updateAddr(baseinfo);
			baseinfo = vacService.updateDetail(baseinfo);
			
			model.addAttribute("temp", baseinfo);
			model.addAttribute("type", "baseinfo");
			return "/vaccination/registration-information";
	}
	
	
	/**
	 * 宝宝列表
	 * @author fuxin
	 * @date 2017年3月13日 上午10:15:23
	 * @description 
	 *		TODO
	 * @param model
	 * @return
	 * @throws SQLException
	 *
	 */
	
	@RequestMapping(value="/list")
	public String list(Model model) throws SQLException {
		VacChildInfo info = new VacChildInfo();
		info.setUserid((String) WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
//		info.setUserid("1");
		List<VacChildInfo> infos = childInfoService.findList(info);
		model.addAttribute("infos", infos);
		return "/vaccination/baby-record";
	}

	/**
	 * 根据pid获取区域信息
	 * @author fuxin
	 * @date 2017年3月13日 上午10:15:46
	 * @description 
	 *		TODO
	 * @param pid
	 * @return
	 *
	 */
	@RequestMapping("/area/{pid}")
	public @ResponseBody Object findAreaByPid(@PathVariable String pid){
		if(StringUtils.isNotBlank(pid)){
			return vacService.findAreaByPid(pid);
		}else{
			return "";
		}
	}
	
	/**
	 * 验证数据库中是否在儿童数据
	 * @author fuxin
	 * @date 2017年3月13日 下午6:20:12
	 * @description 
	 *		TODO
	 * @param childcode
	 * @return
	 *
	 */
	@RequestMapping("/checkchild/{childcode}")
	public @ResponseBody String checkchild(@PathVariable String childcode){
		if(StringUtils.isNotBlank(childcode)){
			BsChildBaseInfo baseinfo = vacService.getBsChildBaseInfoByCode(childcode);
			if(baseinfo != null && StringUtils.isNotBlank(baseinfo.getId())){
				return "success";
			}
		}
		return "";
	}

	/**
	 * 查询手机号码
	 * @author fuxin
	 * @date 2017年3月24日 下午2:29:57
	 * @description 
	 *		TODO
	 * @param birth
	 * @param name
	 * @return
	 *
	 */
	@RequestMapping(value="/choosephone")
	public @ResponseBody JSONMessage choosephone(@RequestParam(required = false) Date birth, @RequestParam(required = false) String name){
		if(null != birth || !StringUtils.isNotBlank(name)){
			List<String> data = vacService.choosephone(birth, name);
			if(data.size() == 0){
				return new JSONMessage(false, "未查询到相关手机号码");
			}else{
				return new JSONMessage(true, data);
			}
		}
		return new JSONMessage(false, "请输入宝宝信息");
		
	}
	
	/**
	 * 查询接种记录
	 * @author fuxin
	 * @date 2017年3月24日 下午2:29:39
	 * @description 
	 *		TODO
	 * @param model
	 * @param type
	 * @param code
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/record/{type}")
	public String record(Model model, @PathVariable String type, @RequestParam(required = false) String code){
		if(!StringUtils.isNotBlank(code)){
			return "forward:/vac/index.do";
		}
		
		List<List<?>> returnlist = new ArrayList<>();
		//未完成列表
		if(BsRecord.STATUS_WAIT.equals(type)){
			try {
				returnlist = (List<List<?>>) vacService.getVaccList(code, BsVaccNum.TYPE_RECORD);
			} catch (Exception e) {
				model.addAttribute("msg", e.getMessage());
			}
			model.addAttribute("list", returnlist);
			return "/vaccination/un-vaccination";
		}else{
			List<BsRecord> listTemp = vacService.getRecordFinish(code);
			//数组转树形
			String op = "first";
			List<BsRecord> templist = new ArrayList<>();
			
			for(BsRecord l : listTemp){
				if(op.equals("first")){
					op = l.getAllname();
				}
				if(!op.equals(l.getAllname())){
					templist.get(0).setLeng(templist.size()+1);
					returnlist.add(templist);
					templist = new ArrayList<>();
					op = l.getAllname();
				}
				templist.add(l);
			}
			if(templist.size() > 0){
				templist.get(0).setLeng(templist.size()+1);
				returnlist.add(templist);
			}
			//数组转树形 结束
			
			model.addAttribute("list", returnlist);
			return "/vaccination/vaccination-con";
		}
		
	}
	
	/**
	 * 线下扫码关注,回答问题
	 * @author fuxin
	 * @date 2017年8月29日 上午2:37:35
	 * @description 
	 *		TODO
	 * @param id
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping("/attenT")
	public String attenT(@RequestParam(required=false) String id, Model model){
		
		boolean isFollow = false;
		try {
			List<String> openids = weixinjsSDKImpl.getFollowList();
			if(openids.contains((String)WebUtils.getRequest().getSession().getAttribute("wpwx.openId"))){
				isFollow = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!isFollow){
			return "/vaccination/followme";
		}	
		BsChildBaseInfo baseinfo = vacService.getBsChildBaseInfoById(id);
		if(StringUtils.isBlank(id) || baseinfo == null){
			model.addAttribute("error", "关注失败");
			return "/vaccination/attention";
		}
		
		model.addAttribute("baseinfo", baseinfo);				
		return "/vaccination/followOffline";
	}
	
	/**
	 * 线下扫码关注
	 * @author fuxin
	 * @date 2017年8月29日 上午2:37:17
	 * @description 
	 *		TODO
	 * @param base
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping("/attenTickets")
	public @ResponseBody Map<String, String> attenTickets(BsChildBaseInfo base, Model model){
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("code", "500");
		if(base != null && StringUtils.isBlank(base.getId())){
			returnMap.put("msg", "参数异常，请重试");
			return returnMap;
		}
		BsChildBaseInfo baseinfo = vacService.getBsChildBaseInfoById(base.getId());
		if(baseinfo != null && StringUtils.isBlank(baseinfo.getId())){
			returnMap.put("msg", "宝宝信息未上传，请联系工作人员");
			return returnMap;
		}
		
		if(!DateUtils.dateParseShortString(baseinfo.getBirthday()).equals(DateUtils.dateParseShortString(base.getBirthday()))){
			returnMap.put("msg", "很抱歉，您输入的生日与建卡时录入的生日不匹配");
			return returnMap;
		}
		
		VacChildInfo i = VacChildInfo.parse(baseinfo);
		//验证是否已被关注
		String userid = (String)WebUtils.getRequest().getSession().getAttribute("wpwx.userid");
		VacChildInfo tempinfo = new VacChildInfo();
		tempinfo.setUserid(userid);
		tempinfo.setChildcode(baseinfo.getChildcode());
		List<VacChildInfo> infos =childInfoService.findList(tempinfo);
		if(infos.size() > 0){
			model.addAttribute("msg", "宝宝已被关注");
			returnMap.put("code", "201");
			returnMap.put("msg", "/wpwx/vac/index.do");
			return returnMap;
		}
		i.setUserid((String)WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
		childInfoService.save(i);
		model.addAttribute("msg", "关注成功");
		returnMap.put("code", "200");
		returnMap.put("msg", "/wpwx/vac/index.do");
		
		//发送微信模板消息
		List<VacChildRemind> reminds = vacService.findVacChildRemindList(baseinfo.getChildcode());
		
		return returnMap;
	}
	
}