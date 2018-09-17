/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.action.vaccination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.junl.frame.tools.net.WebUtils;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.form.OrderForm;
import com.junl.wpwx.model.BsChildBaseInfo;
import com.junl.wpwx.model.BsProduct;
import com.junl.wpwx.model.BsRecord;
import com.junl.wpwx.model.VacOrder;
import com.junl.wpwx.service.AsyncService;
import com.junl.wpwx.service.vaccinate.VacOrderService;
import com.junl.wpwx.service.vaccinate.VacService;

/**
 * 微信订单管理Controller
 * @author fuxin
 * @version 2017-03-02
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseAction {

	@Autowired
	private VacOrderService vacOrderService;
	@Autowired
	private VacService vacService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private ConfigProperty conf;
	
	@RequestMapping("/form")
	public String form(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "vaccination/select-vaccination";
	}
	
	
	/**
	 * 列表显示
	 * @author fuxin
	 * @date 2017年5月17日 下午1:48:20
	 * @description 
	 *		TODO
	 * @param request
	 * @param response
	 * @param model
	 * @param code
	 * @return
	 *
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(required=false)String code ) {
		if(!StringUtils.isNotBlank(code)){
			return "forward:/vac/index.do";
		}
		VacOrder order = new VacOrder();
		order.setUserid((String) WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
//		order.setStatus(VacOrder.STATUS_FINISH);
		order.setOrderBy("a.ordertime DESC");
		if(StringUtils.isNotBlank(code)){
			order.setChildcode(code);
		}
		BsRecord temprecord = new BsRecord();
		temprecord.setChildcode(code);
		temprecord.setStatus("1");
		List<BsRecord> records = vacService.findRecordList(temprecord);
		String r0 =  "_";
		String r1 =  "_";
		for( BsRecord r :records){
			if(BsRecord.STATUS_WAIT.equals(r.getStatus())){
				r0+=r.getNid() + "_";
			}
			if(BsRecord.STATUS_FINISH.equals(r.getStatus())){
				r1+=r.getNid() + "_";
			}
		}
		
		List<VacOrder> orderlist = vacOrderService.findList(order);
		for(VacOrder oo:orderlist){
			if(oo.getNumid().contains("_")){
				for(String nni:oo.getNumid().split("_")){
					if(r1.indexOf("_" + nni + "_")>0){
						oo.setOrderStatus("1");
						break;
					}
					if(r0.indexOf("_" + nni + "_")>0){
						oo.setOrderStatus("0");
						break;
					}
				}
			}else{
				if(r1.indexOf("_" + oo.getNumid() + "_")>0){
					oo.setOrderStatus("1");
				}
				if(r0.indexOf("_" + oo.getNumid() + "_")>0){
					oo.setOrderStatus("0");
				}
			}
		}
		model.addAttribute("list", orderlist);
		model.addAttribute("childcode", code);
		return "vaccination/registration";
	}
	
	
	/**
	 * 订单预览
	 * @author fuxin
	 * @date 2017年5月17日 下午1:48:28
	 * @description 
	 *		TODO
	 * @param request
	 * @param model
	 * @param form
	 * @return
	 *
	 */
	@RequestMapping(value = "/orderpreview")
	public String orderPreView(HttpServletRequest request, Model model, OrderForm form) {
		if(!form.valid()){
			model.addAttribute("msg", "提交失败，请稍后重试");
			return "forward:/vac/index.do";
		}
		
		//只选一个的情况
		if(form.getPid().contains("_")){
			String[] pids = form.getPid().split("_");
			List<BsProduct> ps = new ArrayList<>();
			int sum = 0;
			String pname = "";
			BsProduct pp;
			for(String pid :pids){
				pp = vacService.getBsProduct(pid);
				ps.add(pp);
				sum += pp.getSellprice();
				String ppname = pp.getVaccName();
				if(ppname.indexOf("(") > 0){
					ppname = ppname.substring(0,ppname.indexOf("("));
				}
				if(ppname.indexOf("（") > 0){
					ppname = ppname.substring(0,ppname.indexOf("（"));
				}
				pname +=  "+" + ppname;
			}
			if(pname.startsWith("+"))
				pname = pname.replaceFirst("[+]", "");
			
			if(StringUtils.isNotBlank(form.getInsurance())){
				form.setPrice(sum + 200*ps.size());
			}else{
				form.setPrice(sum);
			}
			model.addAttribute("pname", pname);
		}else{
			BsProduct product = vacService.getBsProduct(form.getPid());
			if(StringUtils.isNotBlank(form.getInsurance())){
				form.setPrice(product.getSellprice() + 200);
			}else{
				form.setPrice(product.getSellprice());
			}
			model.addAttribute("pname", product.getVaccName());
		}
		
		BsChildBaseInfo info = vacService.getBsChildBaseInfoByCode(form.getChildcode());
		model.addAttribute("info", info);
		model.addAttribute("form", form);
		
		

		return "vaccination/orderpreview";
	}
	
	
	/**
	 * 下单+支付
	 * @author fuxin
	 * @date 2017年5月17日 下午1:48:41
	 * @description 
	 *		TODO
	 * @param request
	 * @param model
	 * @param form
	 * @return
	 *
	 */
	@RequestMapping(value = "/reserve")
	public String reserve(HttpServletRequest request, Model model, OrderForm form) {
		if(!form.valid()){
			return "redirect:/vac/index.do";
		}
		
		String pname = "";
		//只选一个的情况
		if(form.getPid().contains("_")){
			String[] pids = form.getPid().split("_");
			List<BsProduct> ps = new ArrayList<>();
			int sum = 0;
			BsProduct pp;
			for(String pid :pids){
				pp = vacService.getBsProduct(pid);
				ps.add(pp);
				sum += pp.getSellprice();
				String ppname = pp.getVaccName();
				if(ppname.indexOf("(") > 0){
					ppname = ppname.substring(0,ppname.indexOf("("));
				}
				if(ppname.indexOf("（") > 0){
					ppname = ppname.substring(0,ppname.indexOf("（"));
				}
				pname +=  "+" + ppname;
			}
			if(pname.startsWith("+"))
				pname = pname.replaceFirst("[+]", "");
			
			if(StringUtils.isNotBlank(form.getInsurance())){
				form.setPrice(sum + 200*ps.size());
			}else{
				form.setPrice(sum);
			}
		}else{
			BsProduct product = vacService.getBsProduct(form.getPid());
			if(StringUtils.isNotBlank(form.getInsurance())){
				form.setPrice(product.getSellprice() + 200);
			}else{
				form.setPrice(product.getSellprice());
			}
			pname = product.getVaccName();
		}
		if(form.getPrice() == 0){
			vacOrderService.insertOrder(form, pname);
			model.addAttribute("msg", "预约成功");
			return "forward:/vac/loading.do";
		}
		
		// 调用beeCloud接口支付
		Map<String, String> map = vacOrderService.doBeeCloud(form, pname);
		if (map.size() > 0) {
			model.addAttribute("jsapiAppid", map.get("appId").toString());
			model.addAttribute("timeStamp", map.get("timeStamp").toString());
			model.addAttribute("nonceStr", map.get("nonceStr").toString());
			model.addAttribute("jsapipackage", map.get("package").toString());
			model.addAttribute("signType", map.get("signType").toString());
			model.addAttribute("paySign", map.get("paySign").toString());
			
			model.addAttribute("ctx", request.getContextPath());
			return "beePay";
		} else {
			model.addAttribute("error", "支付失败,请尝试重新提交");
			return "vaccination/loading";
		}
	}
	
	
	/**
	 * 退款
	 * @author fuxin
	 * @date 2017年5月17日 下午1:48:54
	 * @description 
	 *		TODO
	 * @param model
	 * @param id
	 * @param childcode
	 * @return
	 *
	 */
	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public @ResponseBody Map<String, Object> refund(Model model, String id, String childcode){
		logger.info("[" + childcode + "-" + id + "]微信退款流程开始");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", 500);
		
		//是否已锁定[当接种记录为空或者逻辑删除的时候才可退款]
		//判断订单是否被锁定
		
		if(StringUtils.isNoneBlank(id) && StringUtils.isNotBlank(childcode)){
			VacOrder order = vacOrderService.get(id);
			//验证订单信息
			if(null != order && StringUtils.isNotBlank(order.getNumid())){
				logger.info("[" + childcode + "-" + id + "]微信退款-订单信息检查成功" + JSONObject.fromObject(order).toString());
				//未付款订单，直接关闭
				if(VacOrder.STATUS_WAIT == order.getStatus()){
					order.setStatus(VacOrder.STATUS_CLOSE);
					vacOrderService.save(order);
					returnMap.put("code", 201);
					returnMap.put("msg", "订单关闭成功");
					logger.info("[" + childcode + "-" + id + "]微信退款结束-订单关闭成功" + JSONObject.fromObject(returnMap).toString());
					return returnMap;
				}else if(VacOrder.STATUS_CANCEL == order.getStatus() || VacOrder.STATUS_CLOSE == order.getStatus()){
					returnMap.put("msg", "订单已结束，无法取消");
					logger.info("[" + childcode + "-" + id + "]微信退款失败-订单状态已完成" + JSONObject.fromObject(returnMap).toString());
					return returnMap;
				}
				String[] nids;
				BsRecord temprec = null;
				String sb = "";
				if(order.getNumid().indexOf("_") > -1){
					nids = order.getNumid().split("_");	
					for(String n : nids){
						sb += "'" + n + "',";
					}
					if(sb.endsWith(",")){
						sb = sb.substring(0, sb.length() - 1);
					}
				}else{
					nids = new String[]{order.getNumid()};
					sb = "'" + order.getNumid() + "'";
				}
				//验证订单状态
				temprec = new BsRecord();
				temprec.setNid(sb);
				temprec.setChildcode(childcode);
				if(vacService.checkRefundOrder(temprec)){
					returnMap.put("msg", "订单已被锁定,请先去接种台取消排号再取消订单");
					logger.info("[" + childcode + "-" + id + "]微信退款失败-订单已被锁定" + JSONObject.fromObject(returnMap).toString());
					return returnMap;
				}
				logger.info("[" + childcode + "-" + id + "]微信退款-检查订单未被锁定");
				
				//订单金额大于0
				if(order.getPayprice() > 0){
					if(nids.length > 0){
						//只有一个订单时
						if(nids.length == 1){
							if(VacOrder.INSURANCE_YES.equals(order.getInsurance())){
								//TODO:退保								
								if(asyncService.refundInsurance(childcode, nids[0], conf)){
									logger.info("[" + childcode + "-" + id + "]微信退款-保险退保成功");
								}else{
									logger.error("[" + childcode + "-" + id + "]微信退款-保险退保失败");
								}
							}
							//TODO:退款
							//退款失败
							if(!vacOrderService.doBeeRefund(order)){
								returnMap.put("msg", "退款失败，请联系客服或稍后再试。");
								logger.info("[" + childcode + "-" + id + "]微信退款失败-BeeCloud接口调用失败" + JSONObject.fromObject(returnMap).toString());
								return returnMap;
							};
							logger.error("[" + childcode + "-" + id + "]微信退款-BeeCloud接口调用成功");
						//多个订单时
						}else{
							String insSuccess = "";
							for(String n : nids){
								if(VacOrder.INSURANCE_YES.equals(order.getInsurance())){
									//TODO:退保
									if(asyncService.refundInsurance(childcode, n, conf)){
										insSuccess += "[" + childcode + "-" + n +  "]" + "退保成功   ";
									}else{
										insSuccess += "[" + childcode + "-" + n +  "]" + "退保失败   ";
									}
									logger.info("[" + childcode + "-" + id + "]微信退款-保险退保结果" + insSuccess);
								}
							}
							//TODO:退款
							//退款失败
							if(!vacOrderService.doBeeRefund(order)){
								returnMap.put("msg", "退款失败，请联系客服或稍后再试。");
								logger.info("[" + childcode + "-" + id + "]微信退款失败-BeeCloud接口调用失败" + JSONObject.fromObject(returnMap).toString());
								return returnMap;
							};
							logger.error("[" + childcode + "-" + id + "]微信退款-BeeCloud接口调用成功");
						}
					}else{
						returnMap.put("msg", "订单数据错误，请联系客服或稍后再试。");
						logger.info("[" + childcode + "-" + id + "]微信退款失败-订单数据异常" + JSONObject.fromObject(order).toString());
						return returnMap;
					}
				}
				//TODO:取消订单
				//订单没钱
				logger.info("[" + childcode + "-" + id + "]微信退款-订单没有金额，直接取消");
				order.setStatus(VacOrder.STATUS_CANCEL);
	    		vacOrderService.save(order);							
				returnMap.put("code", 200);
				returnMap.put("msg", "取消订单交成功");
			}
		}else{
			logger.error("[" + childcode + "-" + id + "]微信退款失败，参数异常");
			returnMap.put("msg", "系统内部错误，请联系客服或稍后再试。");
		}
		logger.info("[" + childcode + "-" + id + "]微信退款申请成功" + JSONObject.fromObject(returnMap).toString());
		return returnMap;
	}

	
}