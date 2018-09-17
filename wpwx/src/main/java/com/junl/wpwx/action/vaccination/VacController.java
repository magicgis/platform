/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.action.vaccination;

import com.junl.frame.tools.net.WebUtils;
import com.junl.frame.tools.render.JSONMessage;
import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.form.OrderForm;
import com.junl.wpwx.model.*;
import com.junl.wpwx.service.SysHolidayService;
import com.junl.wpwx.service.vaccinate.VacChildInfoService;
import com.junl.wpwx.service.vaccinate.VacOrderService;
import com.junl.wpwx.service.vaccinate.VacService;
import com.junl.wpwx.service.vaccinate.WorkingHoursService;
import com.junl.wpwx.utils.DateTools;
import com.junl.wpwx.utils.FuxinUtil;
import com.junl.wpwx.vo.VaccGroupVo;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 微信关注宝宝表Controller
 * @author fuxin
 * @version 2017-03-02
 */
@Controller
@RequestMapping(value = "/vac")
public class VacController extends BaseAction {

	@Autowired
	private VacChildInfoService childInfoService;
	@Autowired
	private VacService vacServicd;
	@Autowired
	private VacOrderService vacOrderService;
	@Autowired
	private SysHolidayService sysService;
	@Autowired
	private WorkingHoursService workService;

	@RequestMapping(value="/index")
	public String index(Model model, @RequestParam(required=false) String code) {
		
		VacChildInfo info = new VacChildInfo();
		String userid = (String) WebUtils.getRequest().getSession().getAttribute("wpwx.userid");
		List<VacChildInfo> infos = new ArrayList<>();
		if(StringUtils.isNotEmpty(userid)){
			info.setUserid(userid);
			infos = childInfoService.findList(info);
		}
		model.addAttribute("infos", infos);
		if(infos.size() == 0){
            model.addAttribute("error", "你还没有关注宝宝，请您关注宝宝！");
			return "/vaccination/attention";
		}
		if(StringUtils.isNotEmpty(code)){
			for(int i = 0; i < infos.size(); i ++){
				if(code.equals(infos.get(i).getChildcode())){
					VacChildInfo temp = infos.get(i);
					infos.remove(i);
					infos.add(0, temp);
				}
			}
		}
		return "/vaccination/dangan";
		
	}
	
	@RequestMapping(value="/select")
	public String select(Model model, @RequestParam(required=false) String code) {
		if(StringUtils.isEmpty(code)){
			return "forward:/vac/index.do";
		}
/*		try {
			BsVaccNum num =  (BsVaccNum) vacServicd.getVaccList(code, BsVaccNum.TYPE_ORDER);
			if(num != null){
				List<BsProduct> productslist = vacServicd.getProductListByGroup(num.getGroup());
				model.addAttribute("productslist", productslist);
				model.addAttribute("childcode", code);
				model.addAttribute("nid", num.getId());
				return "/vaccination/vaccination-choose";
			}
		} catch (Exception e) {
			model.addAttribute("msg", e.getMessage());
			return index(model,code);
		}*/
		
		try {
			model.addAttribute("childcode", code);
			return "/vaccination/vaccination-choose2";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//没有查到计划
		model.addAttribute("msg", "未查询到可预约记录");
		return index(model,code);
	}
	
	
	/**
	 * 查询所有一类疫苗
	 * @author fuxin
	 * @date 2017年4月11日 下午7:18:04
	 * @description 
	 *		TODO
	 * @param model
	 * @param code
	 * @param isfree
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/selectfree")
	public @ResponseBody Map<String, Object> selectfree(Model model, @RequestParam(value="code",required=false) String code, 
			@RequestParam(value="isfree",required=false) String isfree) {
		if(StringUtils.isEmpty(code)){
			return null;
		}
		BsChildBaseInfo baseinfo = vacServicd.getBsChildBaseInfoByCode(code);
		if(null == baseinfo){
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", "200");
		List<VaccGroupVo> vos = new ArrayList<>();
		try {
			List<BsVaccNum> listnum = new ArrayList<>();
			if("free".equals(isfree)){
				//免费疫苗
				listnum =  (List<BsVaccNum>) vacServicd.getVaccList(code, BsVaccNum.TYPE_ORDER_FREE);
			}else if("all".equals(isfree)){
				//全部显示
				listnum =  (List<BsVaccNum>) vacServicd.getVaccList(code, BsVaccNum.TYPE_ORDER);
			}
			if(listnum.size() > 0){
				VaccGroupVo vo = VaccGroupVo.parseVo(listnum.get(0)).addProduct(vacServicd.getProductByGroupExp(listnum.get(0).getGroup(),baseinfo.getLocalCode()));
				//一类苗可以再配对另外一支一类苗
				VaccGroupVo vot = null;
				if(listnum.get(0).getType() == 1){
					for(int i = 1; i < listnum.size(); i++){
						if(listnum.get(i).getType() == 1){
							vot = VaccGroupVo.parseVo(listnum.get(i)).addProduct(vacServicd.getProductByGroupExp(listnum.get(i).getGroup(),baseinfo.getLocalCode()));
							break;
						}
					}
					if(null != vot){
						vo = VaccGroupVo.mix(vo, vot);
					}
				}
				vos.add(vo);
			}
			if(vos.size() > 0){
				map.put("vos", vos);
				return map;
			}
			
		} catch (Exception e) {
			map.put("status", "500");
			map.put("error", "没有查询到结果");
			logger.error("查询所有一类疫苗错误",e);
		}
		map.put("status", "500");
		map.put("msg", "没有查询到结果");
		return map;
	}

	@RequestMapping("/loading")
	public String loading(Model model, @RequestParam(required=false) String paystatus){
		logger.info("等待支付结果" + paystatus);
		paystatus = HtmlUtils.htmlUnescape(paystatus);
		if(StringUtils.isNotEmpty(paystatus)){
			if(paystatus.indexOf("ok") > -1){
				model.addAttribute("msg", "预约成功");
			}else if(paystatus.indexOf("cancel") > -1){
				model.addAttribute("error", "付款已取消");
			}else{
				model.addAttribute("error", "付款失败");
			}
		}
		return "/vaccination/loading";
	}
	
	/**
	 * 提示签字预约信息列表
	 * @author zhouqj
	 * @date 2017年10月28日 上午10:58:46
	 * @description 
	 *		TODO
	 * @param temp
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping(value = "/vaccList")
	public String vaccList(String code, Model model) {
        VacChildInfo temp =new VacChildInfo();
		temp.setUserid((String)WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
		temp.setChildcode(code);
		//提示信息列表
		List<VacChildRemind> list = childInfoService.findVacChildRemindList(temp);
		// 数组转树形
		String opo = "first";
		List<VacChildRemind> templist = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> returnlist = new ArrayList<>();
		for (VacChildRemind v : list) {
			if (opo.equals("first")) {
				opo = v.getChildcode() + "-" + v.getChildname();
			}
			if (!opo.equals(v.getChildcode() + "-" + v.getChildname())) {
				templist.get(0).setLeng(templist.size());
				map.put("templist", templist);
				map.put("child", opo);
				returnlist.add(map);
				templist = new ArrayList<>();
				map = new HashMap<String, Object>();
				opo = v.getChildcode() + "-" + v.getChildname();
			}
			templist.add(v);
		}
		if (templist.size() > 0) {
			templist.get(0).setLeng(templist.size());
			map.put("templist", templist);
			map.put("child", opo);
			returnlist.add(map);
		}
		model.addAttribute("list", returnlist);
		return "/vaccination/vacc-remind";
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
	public String sign(VacChildRemind temp, Model model) {
		//查询单条提示信息
		VacChildRemind vr = childInfoService.findVacChildRemind(temp);
		String disContext = "";
	    if(vr != null){
			CmsDisclosure bsp =new CmsDisclosure();
			if(temp.getVaccId().length()==4){
				bsp= vacServicd.getCmsDisclosureByVacid(temp.getVaccId().substring(0,2));
			}else if(temp.getVaccId().length()==2){
				String viccid= vacServicd.getByModelId(temp.getVaccId());
				bsp= vacServicd.getCmsDisclosureByVacid(viccid.substring(0,2));
			}else {
				logger.error("预约记录错误！");
			}
			//查询告知书
			if(bsp != null && StringUtils.isNotEmpty(bsp.getContext())){
                disContext = bsp.getContext();//.replaceAll("\r\n", "<br>&emsp;&emsp;");
            }
			vr.setVaccId(temp.getVaccId());
			model.addAttribute("VacChildRemind", vr);
			model.addAttribute("disContext", disContext);
			if(vr.getSign().equals(VacChildRemind.TEMP_CHILD_SIGN)){
				return "/vaccination/vacc-sign-out";  // 查看签字页面
			}
		}
		return "/vaccination/vacc-sign"; //签字页面
	}


	/**
	 * 预约
	 */
	@RequestMapping(value = "/reservaTion")
	public String reservaTion(VacChildRemind temp, Model model) {//id
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		//此儿童的预约信息
		List<VacChildRemind> vr = childInfoService.findRemindById(temp);
		vr.get(0).setRemindVacc(sdf.format(vr.get(0).getSelectDate()));

		/*VacChildRemind vac=new VacChildRemind();
		vac.setSelectDate(vr.get(0).getRemindDate());//预约日期
		vac.setLocalcode(vr.get(0).getLocalcode());//接种单位编号
		List<SimpleModel>  remindList = childInfoService.findByTime(vac);
		List<SimpleModel> selectTime =vacServicd.getDictList("select_time"); //字典数据
		*/

		List<SimpleModel> selectDate =new ArrayList();
		Date  date = vr.get(0).getRemindDate();
        SimpleModel si=new SimpleModel();
        si.setStr1(String.valueOf(0));
        si.setStr2(sdf.format(date));
        selectDate.add(si);
		for(int i=0;i<5;i++){
			SimpleModel sim=new SimpleModel();
			Date next=sysService.nextWorkDay(date,vr.get(0).getLocalcode());
			sim.setStr1(String.valueOf(i+1));
			sim.setStr2(sdf.format(next));
			selectDate.add(sim);
			date=next;
		}
		//model.addAttribute("dateList",  JSONArray.fromObject(remindList)); //数据列表
		//model.addAttribute("selectTime", JSONArray.fromObject(selectTime));//自选时间段列表
		model.addAttribute("selectDate",  JSONArray.fromObject(selectDate)); //自选日期列表
        model.addAttribute("vr", JSONArray.fromObject(vr));//儿童预约记录
		return "/vaccination/vacc-reservation"; //预约页面
	}


	/**
	 * 预约列表
	 */
	@RequestMapping(value = "/remindTable" )
	public @ResponseBody JSONMessage remindTable(@RequestBody VacChildRemind tem) { //预约日期 //接种单位
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String selectDate = formatter.format(tem.getSelectDate());
		String localcode = tem.getLocalcode();
		String id = tem.getId();
		String week= DateTools.dateToWeek(selectDate);
		WorkingHours work=new WorkingHours();
		work.setLocalcode(localcode);
		work.setWeek(week);
		List<WorkingHours> workList=workService.findByLocalcode(work);
		List<SimpleModel> listSimp=new ArrayList<>();
		VacChildRemind  temp =new VacChildRemind();
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date currentTime_2 = formatter.parse(selectDate, pos);
		temp.setSelectDate(currentTime_2);
		temp.setLocalcode(localcode);
		VacChildRemind temp1 =new VacChildRemind();
		temp1.setId(id);
		temp1.setSelectDate(currentTime_2);
		List<VacChildRemind>  vr = childInfoService.findRemindById(temp1);//此id的预约信息
		List<VacChildRemind>  vr1=  childInfoService.findRemindById(temp);//接种单位、日期
		Map<Object, List<VacChildRemind>>  remindMap= FuxinUtil.getTreeDateByParam(VacChildRemind.class,vr1,"selectTime");
		//String selectTime=vr.getSelectTime();
		for(WorkingHours sh : workList){
			SimpleModel simpleModel=new SimpleModel();
			List<VacChildRemind> li = remindMap.get(sh.getTimeSlice());
			simpleModel.setStr1(sh.getTimeSlice());
			simpleModel.setInt1(sh.getMaximum());
			int num=0;
			if(li != null){
				num=li.size();
			}
			int int2=sh.getMaximum()-num;
			if(int2<0){
				int2=0;
			}
			simpleModel.setInt2(int2);
			simpleModel.setInt1(sh.getMaximum());
			listSimp.add(simpleModel);
		}
		Map<String,Object> map = new HashMap<>();
		map.put("listSimp",listSimp);
		if(vr.size()==0){
			map.put("selectTime","");
		}else {
			map.put("selectTime",vr.get(0).getSelectTime());
		}
		if(listSimp.size()>0){
			return  new JSONMessage(true, map);
		}else {
			return  new JSONMessage(false, "查询错误");
		}

		/*ParsePosition pos = new ParsePosition(0);
		Date currentTime_2 = formatter.parse(dateString, pos);
		temp.setSelectDate(currentTime_2);
		List<SimpleModel>  remindList = childInfoService.findByTime(temp);
		if(remindList.size()>0){*/
            /*int cnut=0;
            for(int i=0;i<remindList.size();i++){
                cnut=cnut+Integer.valueOf(remindList.get(i).getStr2()).intValue();
            }
            SimpleModel simpleModel=new SimpleModel();
            simpleModel.setStr1("总计");
            simpleModel.setStr2(cnut+"");
            remindList.add(simpleModel);*/
			/*return  new JSONMessage(true, remindList);
		}else {
			return  new JSONMessage(false, "查询错误");
		}*/

	}

    /**
     * 修改预约记录
     */
    @RequestMapping(value = "/updateByTime" , method= RequestMethod.POST)
    public @ResponseBody JSONMessage updateByTime(@RequestBody VacChildRemind temp) {
        if("a".equals(temp.getSelectTime())){
            temp.setSelectTime("");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(temp.getSelectDate());
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = formatter.parse(dateString, pos);
        temp.setSelectDate(currentTime_2);
        int i= childInfoService.updateByTime(temp);
        if(i==1){
            return  new JSONMessage(true, "1");
        }else {
            return  new JSONMessage(false, "预约的错误");
        }
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
	public void signimg(HttpServletResponse response, VacChildRemind tm, Model model) {
        VacChildRemind temp = childInfoService.findVacChildRemind(tm);
		ServletOutputStream os = null;
		try {
			if(null != temp && StringUtils.isNotEmpty(temp.getId())){
			    temp.setVaccId(tm.getVaccId());
				VacChildRemind vr = childInfoService.findVacChildRemindById(temp);
				if(vr!=null){
                    byte[] stgn = vr.getSignature();
                    response.getOutputStream().write(stgn);
                    response.getOutputStream().flush();
                }
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
	@RequestMapping(value = "/savesign")
	public @ResponseBody JSONMessage savesign(@RequestParam(required = false) String id,@RequestParam(required = false) String vaccId,
			@RequestParam(required = false) String localcode,@RequestParam(required = false) String signatureDataTo) {
		if(StringUtils.isNotEmpty(id)){
			VacChildRemind temp = new VacChildRemind();
			temp.setId(id);
			temp.setNid(vaccId);
			temp.setLocalcode(localcode);
			temp.setSignatureData(signatureDataTo);
			//提示签字预约信息
			VacChildRemind vr = childInfoService.findVacChildRemind(temp);
			//List<VacChildRemind> vList = childInfoService.findVacRemindSignList(vr);
			if(StringUtils.isNotEmpty(temp.getSignatureData())){
				String signatureData = temp.getSignatureData().substring(22);
				try {
					//转换签字内容
					BASE64Decoder decoder = new BASE64Decoder(); 
					byte[] sign = decoder.decodeBuffer(signatureData);
					//签字状态
					if(null != sign && sign.length > 0){
						vr.setSignature(sign);
						vr.setStype(VacChildRemind.TEMP_CHILD_STYPE);
						vr.setVid(vr.getId());
						vr.setSign(VacChildRemind.TEMP_CHILD_SIGN);
						vr.setVaccId(vaccId);
						//新增签字
						childInfoService.insertSignature(vr);
						//更改记录签字状态
						childInfoService.updateSign(vr);
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
	/**
	 * 买保险列表
	 */
	@RequestMapping(value = "/insList")
	public String insList(String childcode,String nid,String openid,Model model) {
		model.addAttribute("childcode", childcode);
		model.addAttribute("nid", nid);
		model.addAttribute("openid", openid);
		return"/vaccination/insList";
	}
	/**
	 * 支付页面
	 */
	@RequestMapping(value = "/bylist", method = RequestMethod.POST)
	public String bylist( Model model,String childcode,String nid) {  // 金额 儿童编码 模型id
		OrderForm form =new OrderForm();// 调用beeCloud接口支付
		form.setPrice(1);
		form.setChildcode("340603030120177585");
		form.setNid("5001");
		form.setPid("756e5478266644029043d2a9e9040da3");
		BsProduct product = vacServicd.getBsProduct(form.getPid());  // 产品ID
		String pname=product.getVaccName();
		Map<String, String> map = vacOrderService.doBeeCloud(form, pname);

		if (map.size() > 0) {
			model.addAttribute("jsapiAppid", map.get("appId").toString());
			model.addAttribute("timeStamp", map.get("timeStamp").toString());
			model.addAttribute("nonceStr", map.get("nonceStr").toString());
			model.addAttribute("jsapipackage", map.get("package").toString());
			model.addAttribute("signType", map.get("signType").toString());
			model.addAttribute("paySign", map.get("paySign").toString());

			//model.addAttribute("ctx", request.getContextPath());
			return "beePay";
		} else {
			model.addAttribute("error", "支付失败,请尝试重新提交");
			return "vaccination/loading";
		}
	}

}