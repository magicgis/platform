package com.junl.wpwx.action.vaccination;

import com.junl.frame.tools.CommonUtils;
import com.junl.frame.tools.net.WebUtils;
import com.junl.frame.tools.render.JSONMessage;
import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.model.*;
import com.junl.wpwx.service.vaccinate.VacRabiesTempService;
import com.junl.wpwx.service.vaccinate.VacService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import com.junl.frame.tools.CommonUtils;
import com.junl.frame.tools.net.WebUtils;
import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.common.OrderUtil;
import com.junl.wpwx.model.VacRabiesTemp;
import com.junl.wpwx.service.vaccinate.VacRabiesTempService;
import com.junl.wpwx.service.vaccinate.VacService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rabies")
public class RabiesController extends BaseAction {

	@Autowired
	private VacRabiesTempService rabiesTempService;
	@Autowired
	private VacService vacService;

	@ModelAttribute
	public VacRabiesTemp get(@RequestParam(required=false) String id) {
		VacRabiesTemp entity = null;
		if (StringUtils.isNotEmpty(id)){
			entity = rabiesTempService.get(id);
		}
		if (entity == null){
			entity = new VacRabiesTemp();
		}
		return entity;
	}

	//狂犬病建档
	@RequestMapping(value = "/form")
	public String form(VacRabiesTemp temp, Model model) {
		model.addAttribute("animals", vacService.getDictList(VacRabiesTemp.DICTTYPE_ANIMAL));
		model.addAttribute("bites", vacService.getDictList(VacRabiesTemp.DICTTYPE_BITEPART));
		model.addAttribute("disposal_sites", vacService.getDictList(VacRabiesTemp.DICTTYPE_DEALADDRESS));
		model.addAttribute("bitetypes", vacService.getDictList(VacRabiesTemp.DICTTYPE_BITETYPE));
		return "/vaccination/rabies-info";
	}

	//成人建档
	@RequestMapping(value = "/hepbForm")
	public String hepbForm(VacRabiesTemp temp, Model model) {
		return "/vaccination/hbs-info";
	}

	@RequestMapping("/detail")
	public String detail(VacRabiesTemp temp, Model model) {
		if(StringUtils.isEmpty(temp.getId())){
			model.addAttribute("msg", "您的编号已过期");
			return "forward:/vac/index.do";
		}
		temp.setAnimal(vacService.getDictLabel(temp.getAnimal(), VacRabiesTemp.DICTTYPE_ANIMAL, "其他"));
		temp.setBitepart(vacService.getDictLabel(temp.getBitepart(), VacRabiesTemp.DICTTYPE_BITEPART, "其他"));
		temp.setBitetype(vacService.getDictLabel(temp.getBitetype(), VacRabiesTemp.DICTTYPE_BITETYPE, "其他"));
		temp.setDealaddress(vacService.getDictLabel(temp.getDealaddress(), VacRabiesTemp.DICTTYPE_DEALADDRESS, "其他"));
		model.addAttribute("temp", temp);
		return "/vaccination/rabies-detail";
	}


	@RequestMapping("/hepbDetail")
	public String hepbDetail(VacHepbTemp temp, Model model) {
		if(StringUtils.isEmpty(temp.getId())){
			model.addAttribute("msg", "您的编号已过期");
			return "forward:/vac/index.do";
		}
		temp = rabiesTempService.getHepb(temp.getId());
		model.addAttribute("temp", temp);
		return "/vaccination/hepb-detail";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public  @ResponseBody JSONMessage save(VacRabiesTemp temp) {
		temp.setOpenid((String)WebUtils.getRequest().getSession().getAttribute("wpwx.openId"));
		temp.setTempid(CommonUtils.UUIDGenerator());
		rabiesTempService.save(temp);

		return  new JSONMessage(true, "rabies/detail.do?id="+temp.getId());
		//return "forward:/rabies/detail.do?id="+temp.getId();
	}

	@RequestMapping(value = "/hepbSave", method = RequestMethod.POST)
	public @ResponseBody JSONMessage hepbSave(VacHepbTemp temp) {
		temp.setOpenId((String)WebUtils.getRequest().getSession().getAttribute("wpwx.openId"));
		temp.setTempId(CommonUtils.UUIDGenerator());
		rabiesTempService.save(temp);
		return  new JSONMessage(true, "rabies/hepbDetail.do?id="+temp.getId());
		//return "forward:/rabies/hepbDetail.do?id="+temp.getId();
	}

	/**
	 * 提示信息列表
	 * @author zhouqj
	 * @date 2017年10月17日 上午9:28:45
	 * @description
	 *		TODO
	 * @param temp
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping(value = "/vaccList")
	public String vaccList(VacRabiesTemp temp, Model model) {
		temp.setOpenid((String)WebUtils.getRequest().getSession().getAttribute("wpwx.openId"));
		//提示信息列表
		List<VacRemind> list = rabiesTempService.findVacRemindList(temp);
		// 数组转树形
		String opo = "first";
		String code="";
		List<VacRemind> templist = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> returnlist = new ArrayList<>();
		for (VacRemind v : list) {
			if (opo.equals("first")) {
				opo = v.getCtxvaccname() + "-" + v.getCtxusername();
				code= v.getNcode();
			}
			if (!opo.equals(v.getCtxvaccname() + "-" + v.getCtxusername())) {
				templist.get(0).setLeng(templist.size());
				map.put("templist", templist);
				map.put("ctxvaccname", opo);
				map.put("code", code);
				returnlist.add(map);
				templist = new ArrayList<>();
				map = new HashMap<String, Object>();
				opo = v.getCtxvaccname() + "-" + v.getCtxusername();
				code= v.getNcode();
			}
			templist.add(v);
		}
		if (templist.size() > 0) {
			templist.get(0).setLeng(templist.size());
			map.put("templist", templist);
			map.put("ctxvaccname", opo);
			map.put("code",code );
			returnlist.add(map);
		}
		model.addAttribute("list", returnlist);
		return "/vaccination/vacc-remind-doge";
	}

	/**
	 * 签字页面
	 * @author zhouqj
	 * @date 2017年10月17日 上午9:28:50
	 * @description
	 *		TODO
	 * @param temp
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping(value = "/sign")
	public String sign(VacRemind temp, Model model) {
		//查询单条提示信息
		VacRemind vr = rabiesTempService.findVacRemind(temp);
		String disContext = "";
		if(vr != null){
			//查询告知书
			CmsDisclosure bsp = rabiesTempService.getCmsDisclosureByVacid(vr.getCode());
			if(bsp != null && StringUtils.isNotEmpty(bsp.getContext())){
				disContext = bsp.getContext();//.replaceAll("\r\n", "<br>&emsp;&emsp;");
			}
			model.addAttribute("vacRemind", vr);
			model.addAttribute("disContext", disContext);
			if(vr.getSign().equals(VacRemind.TEMP_SIGN)){
				return "/vaccination/vacc-sign-out-doge";
			}
		}
		return "/vaccination/vacc-sign-doge";
	}

	/**
	 * 签字页面显示
	 * @author zhouqj
	 * @date 2017年10月19日 下午8:07:56
	 * @description
	 *		TODO
	 * @param response
	 * @param temp
	 * @param model
	 *
	 */
	@RequestMapping(value = "signimg")
	public void signimg(HttpServletResponse response, VacRemind temp, Model model) {
		temp = rabiesTempService.findVacRemind(temp);
		ServletOutputStream os = null;
		try {
			if(null != temp && StringUtils.isNotEmpty(temp.getId())){
				VacRemind vr = rabiesTempService.findVacRemindById(temp.getId(),temp.getOffice());
				byte[] stgn = vr.getSignature();
				response.getOutputStream().write(stgn);
				response.getOutputStream().flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != os){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 保存签字内容
	 * @author zhouqj
	 * @date 2017年10月17日 下午2:49:22
	 * @description
	 *		TODO
	 * @param id
	 * @param signatureData
	 * @return
	 *
	 */
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/savesign")
	public @ResponseBody JSONMessage savesign(@RequestParam(required = false) String id, @RequestParam(required = false) String signatureData) {
		if(StringUtils.isNotEmpty(id)){
			//查询单条提示信息
			VacRemind temp = new VacRemind();
			temp.setId(id);
			VacRemind vr = rabiesTempService.findVacRemind(temp);
			List<VacRemind> vList = rabiesTempService.findVacRemindSignList(vr);
			if(StringUtils.isNotEmpty(signatureData)){
				signatureData = signatureData.substring(22);
				try {
					//转换签字内容
					BASE64Decoder decoder = new BASE64Decoder();
					byte[] sign = decoder.decodeBuffer(signatureData);
					//签字状态
					if(null != sign && sign.length > 0){
						//判断是否需要多条签字
						if(vList.size() != 0){
							for(VacRemind v : vList){
								v.setVid(v.getId());
								v.setSignature(sign);
								v.setSign(VacRemind.TEMP_SIGN);
								v.setStype(VacRemind.TEMP_STYPE);
								//新增签字
								rabiesTempService.insertSignature(v);
								//更改记录签字状态
								rabiesTempService.updateSign(v);
							}
						}
						return new JSONMessage(true, "签字成功");
					}else{
						return new JSONMessage(false, "签字内容为空2");
					}
				} catch (IOException e) {
					e.printStackTrace();
					return new JSONMessage(false, "签字存储异常");
				}
			}else{
				return new JSONMessage(false, "签字内容为空1");
			}
		}
		return new JSONMessage(false, "记录id为空");
	}

}
