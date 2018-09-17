package com.junl.wpwx.action.vaccination;

import com.junl.frame.tools.date.DateUtils;
import com.junl.frame.tools.net.WebUtils;
import com.junl.frame.tools.render.JSONMessage;
import com.junl.wpwx.action.weixin.BaseAction;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.common.JsonMapper;
import com.junl.wpwx.common.OrderUtil;
import com.junl.wpwx.common.WxMap;
import com.junl.wpwx.model.*;
import com.junl.wpwx.pay.Alipay;
import com.junl.wpwx.pay.PayCommonUtil;
import com.junl.wpwx.pay.weixinPrePay;
import com.junl.wpwx.service.AsyncService;
import com.junl.wpwx.service.app.AppChildInfoService;
import com.junl.wpwx.service.jpush.JpushMessageService;
import com.junl.wpwx.service.vaccinate.*;
import com.junl.wpwx.service.weixin.IWeixinjsSDK;
import com.junl.wpwx.vo.TemplateMsgVo;
import com.junl.wpwx.vo.VaccGroupVo;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/api")
public class ApiController extends BaseAction {
	
	@Autowired
	VacChildTempService tempService;
	//@Autowired
	//VacOrderService orderService;
	@Autowired
	VacService vacService;
	@Autowired
	VacUserService userService;
	@Autowired
	IWeixinjsSDK weixinjsSDK;
	@Autowired
	VacRabiesTempService rabiesService;
	@Autowired
	ConfigProperty conf;
	@Autowired
	AsyncService asyncService;
	@Autowired
	VacChildInfoService childInfoService;
	@Autowired
	private AppChildInfoService appChildService;
	@Autowired
	private JpushMessageService jpushMessageService;

    @Autowired
    private BsOrderService bsOrderService;
    @Autowired
    private NotificationService notificationService;

	/*@Autowired
	VacRabiesTempService rabiesTempService;*/
	
	/**
	 * 查询自主建档信息
	 * @author fuxin
	 * @date 2017年3月13日 上午10:29:57
	 * @description 
	 *		TODO
	 * @param code
	 * @return Map
	 *
	 */
	@RequestMapping("/childTemp/{code}")
	public @ResponseBody JSONMessage childTemp (@PathVariable(value="code") String code){
		try {
			logger.info("接到查询临时建档请求,临时编号为:" + code);
			if(StringUtils.isNotBlank(code)){
				VacChildTemp baseinfo = tempService.getByCode(code.toUpperCase());
				if(baseinfo != null && StringUtils.isNotBlank(baseinfo.getTempid())){
					//校验是否已建档成功
					BsChildBaseInfo info = new BsChildBaseInfo();
					info.setTempid(baseinfo.getTempid());
					List<BsChildBaseInfo> list = vacService.getBsChildBaseInfoList(info);
					if(list.size() > 0){
						tempService.deletereal(baseinfo);
					}else{
						baseinfo.setId("");
						baseinfo.setChildcode("");
						logger.info("查询成功[code=" + code + "]" + JSONObject.fromObject(baseinfo));
						return new JSONMessage(true, baseinfo);
					}
				}	
			}
			logger.info("查询失败[code=" + code + "]");
			return new JSONMessage(false, "未查询到此条记录或已被使用");
		} catch (Exception e) {
			logger.error("自助建档接口错误",e);
		}
		return  new JSONMessage(false, "系统内部错误,请联系管理员");
	}

	/**
	 * 查询自主建档信息[犬伤]
	 * @author fuxin
	 * @date 2017年4月18日 下午8:19:56
	 * @description 
	 *		TODO
	 * @param code
	 * @return
	 *
	 */
	@RequestMapping("/rabiesTemp/{code}")
	public @ResponseBody JSONMessage rabiesTemp(@PathVariable(value="code") String code) {
		JSONMessage jsonmsg;
		try {
			logger.info("接到查询临时建档[犬伤]请求,临时编号为:" + code);
			if(StringUtils.isNotBlank(code)){
				VacRabiesTemp temp = rabiesService.get(code);
				if(temp != null){
					List<BsRabies> list = vacService.getByTempid(temp.getTempid());
					if(list.size() > 0){
						rabiesService.deleteReal(code);
					}else{
						temp.setId("");
						jsonmsg = new JSONMessage(true, temp);
						logger.info("查询成功[code=" + code + "] -->" + JSONObject.fromObject(jsonmsg));
						return jsonmsg;
					}
				}	
			}
			jsonmsg = new JSONMessage(false, "未查询到此条记录或已被使用");
			logger.info("查询失败[code=" + code + "] -->" + JSONObject.fromObject(jsonmsg));
			return jsonmsg;
		} catch (Exception e) {
			logger.error("自助建档接口错误",e);
		}
		jsonmsg =  new JSONMessage(false, "系统内部错误,请联系管理员");
		logger.info("查询失败[code=" + code + "] -->" + JSONObject.fromObject(jsonmsg));
		return jsonmsg;
	}
	
	
	/**
	 * 查询儿童预约记录
	 * @author fuxin
	 * @date 2017年4月8日 下午5:27:59
	 * @description 
	 *		TODO
	 * @param args
	 * @return
	 *
	 */
	/*@RequestMapping(value="/getReserve", method = RequestMethod.POST)
	public @ResponseBody JSONMessage getReserve(@RequestBody(required = false) Map<String, Object> args){
		if(null != args && args.size() > 0){
			try {
				if(null != args.get("childcode")){
					String childcode = args.get("childcode").toString();
					logger.info("接到查询预约信息请求,儿童编号为:" + childcode);
					BsChildBaseInfo baseinfo = vacService.getBsChildBaseInfoByCode(childcode);
					if(StringUtils.isNotBlank(childcode)){
						List<VaccGroupVo> data = new ArrayList<>();
						
						//获取已完成订单的计划id
						VacOrder o = new VacOrder();
						o.setChildcode(childcode);
						o.setStatus(VacOrder.STATUS_FINISH);
						o.setUtmsource(VacOrder.UTMSOURCE_WEIXIN);
						o.setOrderBy("a.ORDERTIME");
						List<VacOrder> orders = orderService.findList(o);
						
						List<BsRecord> records = new ArrayList<>();
						if(orders.size() > 0){
							//查询儿童接种记录
							records = vacService.getRecordFinish(childcode);
						}
						for(VacOrder r : orders){
							if(isfinsh(r.getNumid(), records)){
								continue;
							}
							
							VaccGroupVo vo = VaccGroupVo.parseVo(r);
							String[] nids = vo.getNid().split("_");
							String ctx = "";
							for(String n : nids){
								BsVaccNum num = vacService.getNumByid(n, baseinfo.getLocalCode());
								if("2".equals(num.getType())){
									vo.setNumType("二类");
								}
								vo.addVaccInfo(vacService.getLastPin(num.getId().substring(0,2), baseinfo.getLocalCode()), num.getPin()+"");
								CmsDisclosure dis = vacService.getCmsDisclosureByGroupId(n.substring(0,2));
								if(dis != null){
									ctx += dis.getContext();
								}
							}
							vo.setContext(ctx);
							data.add(vo);
							break;
						}
						
						//返回结果
						if(data.size() > 0){
							JSONMessage json = new JSONMessage(true, data);
							logger.info("查询预约信息请求成功 INFO-->" + JSONObject.fromObject(json));
							return json;
						}
					}
				}
			} catch (Exception e) {
				logger.error("查询预约信息接口错误",e);
			}
		}
		
		//没有查询的结果
		JSONMessage json = new JSONMessage(false, "没有查询到相关记录");
		logger.info("查询预约信息请求失败 INFO-->" + JSONObject.fromObject(json));
		return json;
	}*/
	
	/**
	 * 判断记录中是否包含 nid
	 * @author fuxin
	 * @date 2017年4月28日 下午2:15:29
	 * @description 
	 *		TODO
	 * @param numid
	 * @param records
	 * @return
	 *
	 */
	private boolean isfinsh(String numid, List<BsRecord> records) {
		boolean returnValue = false;
		for(BsRecord b : records){
			if(numid.contains(b.getNid())){
				returnValue = true;
				break;
			}
		}
		return returnValue;
	}


	/**
	 * 发生模板消息
	 * @author fuxin
	 * @date 2017年4月8日 下午5:27:50
	 * @description 
	 *		TODO
	 * @param args
	 * @return
	 *
	 */
	@RequestMapping(value="/sendWxTempMsg")
	public @ResponseBody JSONMessage sendWxTempMsg(@RequestBody(required = false) Map<String, Object> args){
		if(null != args && args.size() > 0 && null != args.get("childcode")){
			String childcode = args.get("childcode").toString();
			List<String> openid = userService.getOpenidByChildcode(childcode);
			if(openid.size() == 0){
				return new JSONMessage(false, "模板消息发送失败,宝宝未被关注");
			}
			BsChildBaseInfo info = vacService.getBsChildBaseInfoByCode(childcode);
			for(String oid : openid){
				if(StringUtils.isNotBlank(childcode) && StringUtils.isNotBlank(oid)){
					//模板消息通知用户
			    	TemplateMsgVo temp = new TemplateMsgVo(oid, conf.getTemp_quene_finish(), "");
			    	@SuppressWarnings("unchecked")
					Map<String, LinkedMap> data = new LinkedMap();
			    	data.put("first", WxMap.getWxTempMsgMap("本次疫苗接种已完成，请注意下次接种时间。"));
			    	data.put("keyword1", WxMap.getWxTempMsgMap(info.getChildname()));
			    	data.put("keyword2", WxMap.getWxTempMsgMap(DateUtils.dateParseShortString(DateUtils.calculateNextSettleDate(new Date(), 1))));
			    	data.put("remark", WxMap.getWxTempMsgMap("请及时带上您的宝宝前来接种。"));
			    	temp.setData(data);
			    	try {
						weixinjsSDK.sendTemplateMessage(temp);
					} catch (Exception e) {
						logger.error("模板消息发送失败",e);
						return new JSONMessage(false, "模板消息发送失败");
					}
				}
			}
			return new JSONMessage(true, "模板消息发送成功");
		}
		return new JSONMessage(false, "模板消息发送失败");
	}
	
	/**
	 * 发生模板消息
	 * @author fuxin
	 * @date 2017年4月8日 下午5:27:50
	 * @description 
	 *		TODO
	 * @param args
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sendWxTemplateMsg")
	public @ResponseBody JSONMessage sendWxTemplateMsg(@RequestBody(required = false) Map<String, Object> args){
		if(null != args && args.size() > 0 && null != args.get("childcode")){
			String childcode = args.get("childcode").toString();
			List<String> openid = userService.getOpenidByChildcode(childcode);
			if(openid.size() == 0){
				return new JSONMessage(false, "模板消息发送失败,宝宝未被关注");
			}
			List<String> params = (List<String>) args.get("params");
			for(String oid : openid){
				if(StringUtils.isNotBlank(childcode) && StringUtils.isNotBlank(oid)){
					//模板消息通知用户
					TemplateMsgVo temp = new TemplateMsgVo(oid, args.get("tempId").toString(), args.get("url").toString());
					Map<String, LinkedMap> data = new LinkedMap();
					for(int i = 0; i < params.size(); i ++){
						if(i == 0){
							data.put("first", WxMap.getWxTempMsgMap(params.get(i)));
							continue;
						}
						if(i == params.size() -1){
							data.put("remark", WxMap.getWxTempMsgMap(params.get(i)));
							continue;
						}
						data.put("keyword"+i, WxMap.getWxTempMsgMap(params.get(i)));
					}
					temp.setData(data);
					try {
						weixinjsSDK.sendTemplateMessage(temp);
					} catch (Exception e) {
						logger.error("模板消息发送失败",e);
						return new JSONMessage(false, "模板消息发送失败");
					}
				}
			}
			return new JSONMessage(true, "模板消息发送成功");
		}
		return new JSONMessage(false, "模板消息发送失败");
	}


	/**
	 * 发生预约模板消息
	 * @author lonny
	 * @date 2017年12月13日
	 * @description
	 *		TODO
	 * @param args
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sendChildRemind")
	public @ResponseBody JSONMessage sendChildRemind(@RequestBody(required = false) Map<String, Object> args){
		if(null != args && args.size() > 0 && null != args.get("childcode")){
			String childcode = args.get("childcode").toString();
			List<String> openid = userService.getOpenidByChildcode(childcode);
			if(openid.size() == 0){
				return new JSONMessage(false, "模板消息发送失败,宝宝未被关注");
			}
			List<String> params = (List<String>) args.get("params");
			for(String oid : openid){
				if(StringUtils.isNotBlank(childcode) && StringUtils.isNotBlank(oid)){
					//模板消息通知用户
					TemplateMsgVo temp = new TemplateMsgVo(oid, conf.getTemp_remind_child(), conf.getTemp_url()+"/vac/vaccList.do?code="+childcode);
					Map<String, LinkedMap> data = new LinkedMap();
					for(int i = 0; i < params.size(); i ++){
						if(i == 0){
							data.put("first", WxMap.getWxTempMsgMap(params.get(i)));
							continue;
						}
						if(i == params.size() -1){
							data.put("remark", WxMap.getWxTempMsgMap(params.get(i)));
							continue;
						}
						data.put("keyword"+i, WxMap.getWxTempMsgMap(params.get(i)));
					}
					temp.setData(data);
					try {
						weixinjsSDK.sendTemplateMessage(temp);
					} catch (Exception e) {
						logger.error("模板消息发送失败",e);
						return new JSONMessage(false, "模板消息发送失败");
					}
				}
			}
			return new JSONMessage(true, "模板消息发送成功");
		}
		return new JSONMessage(false, "模板消息发送失败");
	}



	/**
	 * 发生模板消息
	 * @author fuxin
	 * @date 2017年4月8日 下午5:27:50
	 * @description 
	 *		TODO
	 * @param args
	 * @return
	 *
	 */
	@RequestMapping(value="/sendWxTempMsgDog")
	public @ResponseBody JSONMessage sendWxTempMsgDog(@RequestBody(required = false) Map<String, Object> args){
		if(null != args && args.size() > 0 
				&& null != args.get("openid") 
				&& null != args.get("username") 
				&& null != args.get("nexttime")){
			String openid = (String)args.get("openid");
			String username = (String)args.get("username");
			String nexttime = (String)args.get("nexttime");
				if(StringUtils.isNotBlank(openid)){
					//模板消息通知用户
			    	TemplateMsgVo temp = new TemplateMsgVo(openid, conf.getTemp_quene_finish(), "");
			    	@SuppressWarnings("unchecked")
					Map<String, LinkedMap> data = new LinkedMap();
			    	data.put("first", WxMap.getWxTempMsgMap("本次疫苗接种已完成，请注意下次接种时间。"));
			    	data.put("keyword1", WxMap.getWxTempMsgMap(username));
			    	data.put("keyword2", WxMap.getWxTempMsgMap(nexttime));
			    	data.put("remark", WxMap.getWxTempMsgMap("请及时前来接种。"));
			    	temp.setData(data);
			    	try {
						weixinjsSDK.sendTemplateMessage(temp);
					} catch (Exception e) {
						logger.error("模板消息发送失败",e);
						return new JSONMessage(false, "模板消息发送失败");
					}
				}
				return new JSONMessage(false, "模板消息发送失败,用户没有关注信息");
		}
		return new JSONMessage(false, "模板消息发送失败");
	}
	
	/**
	 * 一体机查询交易是否回掉成功
	 * @author fuxin
	 * @date 2017年4月8日 下午5:27:26
	 * @description 
	 *		TODO
	 * @param args
	 * @return
	 *
	 */
	/*@RequestMapping(value="/billStatus" , method = RequestMethod.POST)
	public @ResponseBody JSONMessage billStatus(@RequestBody(required = false) Map<String, String> args){
		JSONMessage jsonMsg = null;
		if(null != args && args.size() > 0 && null != args.get("orderNo")){
			try {
				String orderNo = args.get("orderNo");
				VacOrder o = new VacOrder();
				o.setOrderNo(orderNo);
				o.setUtmsource(VacOrder.UTMSOURCE_WEIXIN);
				o.setUtmsource(VacOrder.UTMSOURCE_SELFMACHINE);
				o.setStatus(VacOrder.STATUS_FINISH);				
				List<VacOrder> list = orderService.findList(o);
				if(list.size() > 0){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("result", true);
					jsonMsg = new JSONMessage(true, map);
					logger.error(JSONObject.fromObject(jsonMsg).toString());
					return jsonMsg; 
				}
			} catch (Exception e) {
				jsonMsg = new JSONMessage(false, "参数错误:" + JSONObject.fromObject(args).toString());
				logger.error(JSONObject.fromObject(jsonMsg).toString());
				return  jsonMsg;
			}
		}else{
			return new JSONMessage(false, "参数错误:" + JSONObject.fromObject(args).toString()); 
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", false);
		jsonMsg = new JSONMessage(true, map);
		logger.error(JSONObject.fromObject(jsonMsg).toString());
		return jsonMsg; 
	}*/
	
	/**
	 * 一体机查询交易是否回掉成功
	 * @author fuxin
	 * @date 2017年10月19日 下午2:21:40
	 * @description 
	 *		TODO
	 * @param args
	 * @return
	 *
	 */
	/*@RequestMapping(value="/payStatus" , method = RequestMethod.POST)
	public @ResponseBody JSONMessage payStatus(@RequestBody(required = false) Map<String, String> args){
		JSONMessage jsonMsg = null;
		if(null != args && args.size() > 0 && null != args.get("orderNo")){
			try {
				String orderNo = args.get("orderNo");
				BsOrder o = new BsOrder();
				o.setOrderNo(orderNo);
				o.setTotal(Integer.parseInt(args.get("total")));
				o.setBeneficiary(args.get("officeCode"));
				o.setSource(args.get("source"));
				o.setChildCode(args.get("childCode"));
				List<BsOrder> list = orderService.findBsOrderList(o);
				if(list.size() > 0){
					jsonMsg = new JSONMessage(true, null);
					logger.error(JSONObject.fromObject(jsonMsg).toString());
					return jsonMsg; 
				}
			} catch (Exception e) {
				jsonMsg = new JSONMessage(false, "参数错误:" + JSONObject.fromObject(args).toString());
				logger.error(JSONObject.fromObject(jsonMsg).toString());
				return  jsonMsg;
			}
		}else{
			return new JSONMessage(false, "参数错误:" + JSONObject.fromObject(args).toString()); 
		}
		jsonMsg = new JSONMessage(true, null);
		//jsonMsg.setSuccess(false);
		logger.error(JSONObject.fromObject(jsonMsg).toString());
		return jsonMsg; 
	}
	*/
	/**
	 * 退保接口
	 * @author fuxin
	 * @date 2017年5月23日 下午2:32:33
	 * @description 
	 *		TODO
	 * @param args
	 * @return
	 *
	 */
	@RequestMapping(value = "/refundIns", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject refundIns(@RequestBody(required = false) Map<String, Object> args) {
		Map<String, String> returnMap = new HashMap<>();
		returnMap.put("result", "fail");
		returnMap.put("third_serial_no", "");
		returnMap.put("message", "退保失败,参数错误 -->" + JSONObject.fromObject(args));
		if (null != args && args.size() > 0) {
			try {
				if (null != args.get("childcode") && null != args.get("nid")) {
					String childcode = args.get("childcode").toString();
					String nid = args.get("nid").toString();
					logger.info("接到退保请求,儿童编号为:" + childcode + "计划编号:" + nid);
					if (StringUtils.isNotBlank(childcode) && StringUtils.isNotBlank(nid)) {
						JSONObject op = asyncService.refundInsReq(childcode, nid, conf);
						logger.info("退保请求成功 INFO-->" + op);
						return op;
					}
				}
			} catch (Exception e) {
				returnMap.put("message", "退保失败,系统内部错误 + " + e.getMessage());
				logger.error("查询预约信息接口错误", e);
			}
		}
		// 没有查询的结果
		
		JSONObject op = JSONObject.fromObject(returnMap);
		logger.error("退保请求失败 INFO-->" + op);
		return JSONObject.fromObject(op);
	}
	
	
	/**
	 * 登记台小票关注，发生提醒
	 * @author fuxin
	 * @date 2017年8月28日 下午5:37:19
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	@RequestMapping("/FormTickets")
	public @ResponseBody JSONMessage FormTickets(@RequestBody(required = false) Map<String, Object> args){
		if(null != args && args.size() > 0 && null != args.get("childcode")){
			String childcode = args.get("childcode").toString();
			List<String> openid = userService.getOpenidByChildcode(childcode);
			if(openid.size() == 0){
				return new JSONMessage(false, "模板消息发送失败,宝宝未被关注");
			}
			List<String> params = (List<String>) args.get("params");
			for(String oid : openid){
				if(StringUtils.isNotBlank(childcode) && StringUtils.isNotBlank(oid)){
					//模板消息通知用户
					TemplateMsgVo temp = new TemplateMsgVo(oid, args.get("tempId").toString(), args.get("url").toString());
					Map<String, LinkedMap> data = new LinkedMap();
					for(int i = 0; i < params.size(); i ++){
						if(i == 0){
							data.put("first", WxMap.getWxTempMsgMap(params.get(i)));
							continue;
						}
						if(i == params.size() -1){
							data.put("remark", WxMap.getWxTempMsgMap(params.get(i)));
							continue;
						}
						data.put("keyword"+i, WxMap.getWxTempMsgMap(params.get(i)));
					}
					temp.setData(data);
					try {
						weixinjsSDK.sendTemplateMessage(temp);
					} catch (Exception e) {
						logger.error("模板消息发送失败",e);
						return new JSONMessage(false, "模板消息发送失败");
					}
				}
			}
			return new JSONMessage(true, "模板消息发送成功");
		}
		return new JSONMessage(false, "模板消息发送失败");
	}
	
	
	/**
	 * 测试买保险接口
	 * @author fuxin
	 * @date 2017年5月24日 下午3:18:42
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	//@Autowired或@Resource

	@RequestMapping(value = "/testIns", method = RequestMethod.POST)
	public @ResponseBody String testIns() {
		String openId = (String) WebUtils.getRequest().getSession().getAttribute("wpwx.openId"); //openId
		String str=asyncService.buyInsurance("340603030120177585","5001",conf,openId);
		return str;
	}



	/**
	 * 获取微信用户签字记录-儿童预约
	 * @author zhouqj
	 * @date 2017年10月30日 下午7:12:16
	 * @description 
	 *		TODO
	 * @param map
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/vacTempSign" , method = RequestMethod.POST)
	public @ResponseBody JSONMessage vacTempSign(@RequestBody(required = false) Map<String, String> map) {
		JSONMessage jsonmsg;
		try {
			logger.info("接到查询签字信息请求,接收参数为:" + JsonMapper.toJsonString(map));  //{"rids":["0ae33ed7ca62497bbe3fc8905f0d97d2"]}
			//记录id
			List<String> vList = (List<String>) JsonMapper.fromJsonString(JsonMapper.toJsonString(map.get("rids")), List.class);
			//查询签字记录
			List<VacChildRemind> vr = new ArrayList<VacChildRemind>();
			VacChildRemind vacRemind = null;
			for(String id : vList){
				vacRemind = new VacChildRemind();
				vacRemind.setId(id);
				vacRemind = childInfoService.findVacChildRemindById(vacRemind);
				if(vacRemind != null){
					vr.add(vacRemind);
				}
			}
			if(vr.size() > 0){
				jsonmsg = new JSONMessage(true,vr);
				logger.info("查询成功 -->" + JSONObject.fromObject(jsonmsg));
			}else{
				jsonmsg = new JSONMessage(true,vr);
				logger.info("查询成功 -->" + JSONObject.fromObject(jsonmsg));
			}
			logger.info("接到查询签字信息请求,接收参数为:" + JsonMapper.toJsonString(vr));
			
			return jsonmsg;
		} catch (Exception e) {
		}
		jsonmsg =  new JSONMessage(false, "系统内部错误,请联系管理员");
		logger.info("查询失败 -->" + JSONObject.fromObject(jsonmsg));
		return jsonmsg;
	}
	
	/**
	 * 建档成功自动关注宝宝
	 * @author Lonny
	 * @date 2017年11月20日 
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	@RequestMapping(value="/getConcerBaby1", method = RequestMethod.POST)
	public @ResponseBody JSONMessage getConcerBaby1(@RequestBody(required = false) Map<String, String> baby){
		if(null != baby && baby.size() > 0){
			try {
				if(null != baby.get("childCode")){
					String childCode = baby.get("childCode").toString();
					String userId = baby.get("userId").toString();
					String fileorigin = baby.get("fileorigin").toString();
					logger.info("建档成功自动关注宝宝接口入参儿童编号:" + childCode+"用户ID"+userId);

					BsChildBaseInfo bsInfo = vacService.getBsChildBaseInfoByCode(childCode);
					VacChildInfo i = VacChildInfo.parse(bsInfo);
					if(i != null){
						if(!"".equals(fileorigin)&&fileorigin!=null){
							if("2".equals(fileorigin)){
								//验证是否已被关注
								VacChildInfo tempinfo = new VacChildInfo();
								tempinfo.setUserid(userId);
								tempinfo.setChildcode(i.getChildcode());
								List<VacChildInfo> infos =childInfoService.findList(tempinfo);
								if(infos.size() > 0){
									return new JSONMessage(false, "宝宝已被关注");
								}else{
									i.setUserid(userId);
									childInfoService.save(i);
									return new JSONMessage(true, "宝宝关注成功！");
								}
							}else if("3".equals(fileorigin)) {
								//验证是否已被关注
								VacChildInfo tempinfo = new VacChildInfo();
								if(bsInfo.getGuardianmobile()!=null&&!"".equals(bsInfo.getGuardianmobile())){
									tempinfo.setGuardianmobile(bsInfo.getGuardianmobile());
									tempinfo.setChildcode(i.getChildcode());
									List<ChildAppinfo> infos =childInfoService.findAppUserList(tempinfo); //查询是否已被关注
									if(infos.size() > 0){
										logger.error("该手机号已关联-->"+bsInfo.getGuardianmobile());
										//return new JSONMessage(false, "该手机号已关联");
									}else{
										i.setUserid(userId);
										childInfoService.insertAppUser(i); //插入APP关注表
										logger.error("关联成功-->"+bsInfo.getGuardianmobile());
										//return new JSONMessage(true, "关联成功");
									}
								}
								if(bsInfo.getFatherphone()!=null&&!"".equals(bsInfo.getFatherphone())){
								tempinfo.setGuardianmobile(bsInfo.getGuardianmobile());
								tempinfo.setChildcode(i.getChildcode());
								List<ChildAppinfo> infos =childInfoService.findAppUserList(tempinfo); //查询是否已被关注
								if(infos.size() > 0){
									logger.error("该手机号已关联-->"+bsInfo.getFatherphone());
									//return new JSONMessage(false, "该手机号已关联");
								}else{
									i.setUserid(userId);
									childInfoService.insertAppUser(i); //插入APP关注表
									logger.error("关联成功-->"+bsInfo.getFatherphone());
									//return new JSONMessage(true, "关联成功");
								}
							}
								return new JSONMessage(true, "执行完成！");
							}
						}
					}else{
						return new JSONMessage(false, "宝宝建档信息尚未同步");
					}
				}
			} catch (Exception e) {
				logger.error("关注宝宝接口错误",e);
			}
		}
		//没有查询的结果
		JSONMessage json = new JSONMessage(false, "关注宝宝失败");
		logger.info("关注宝宝失败 INFO-->" + JSONObject.fromObject(json));
		return json;
	}

	 /**
  	 * 建档成功自动关注宝宝
  	 * 可去掉改成触发器方式
  	 * @author wangnan
  	 * @date 2018年3月27日 
  	 * AppDataController
  	 *
  	 */
  	@RequestMapping(value="/getConcerBaby")
  	public @ResponseBody JSONMessage getConcerBaby(@RequestBody(required = false) Map<String, String> baby){
// 	public @ResponseBody JSONMessage getConcerBaby(String childCode,String userId,String fileorigin){
  		if(null != baby && baby.size() > 0){
//  		if(1==1){//测试使用
  			try {
  				
  				String childCode = baby.get("childCode").toString();
 				String userId = baby.get("userId").toString();
 				String fileorigin = baby.get("fileorigin").toString();
 				//参数不能为空
 				if(!StringUtils.isNoneBlank(childCode,fileorigin)){
 		             return new JSONMessage(false, "参数不能为空");
 		        }
 				logger.info("建档成功自动关注宝宝接口入参儿童编号:" + childCode+"用户ID"+userId);
 				
 				//根据宝宝编号查询宝宝信息
 				BsChildBaseInfo bsInfo = vacService.getBsChildBaseInfoByCode(childCode);
 				VacChildInfo childinfo = VacChildInfo.parse(bsInfo);
 				if(childinfo==null){
 					//根据宝宝编号查询不到信息
 					return new JSONMessage(false, "宝宝建档信息尚未同步");
 				}
 				
 				//微信
 				if("2".equals(fileorigin)){
 					//验证是否已被关注
 					VacChildInfo tempinfo = new VacChildInfo();
 					tempinfo.setUserid(userId);
 					tempinfo.setChildcode(childinfo.getChildcode());
 					List<VacChildInfo> infos =childInfoService.findList(tempinfo);
 					if(infos.size() > 0){
 						return new JSONMessage(false, "宝宝已被关注");
 					}else{
 						childinfo.setUserid(userId);
 						childInfoService.save(childinfo);
 						return new JSONMessage(true, "宝宝关注成功！");
 					}
 				} 										
 				
 				//APP数据
 				if("3".equals(fileorigin)) {
 					//验证是否已被关注
 					String Guardianmobile = bsInfo.getGuardianmobile();//父亲手机号
 					//验证母亲手机号
 					if(!StringUtils.isNoneBlank(Guardianmobile)){
 						//母亲手机号不为空
 						return new JSONMessage(false, "建档记录有误，母亲号码未填");
 					}
 					
 					//查询是否已被关注
 					Integer mcount = appChildService.getIsAttenByPhone(Guardianmobile, childCode);
 	            	   
 					if(mcount>0){
             		 //已关注，提示宝宝已被关注
 						logger.error("宝宝已被关注-->"+bsInfo.getGuardianmobile());
 					}else{
             		
 						SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );						
 						String CHILDCODE = bsInfo.getChildcode(); 
 						String CHILDNAME = bsInfo.getChildname(); 
 						String CARDCODE = bsInfo.getCardcode(); 
 						String BIRTHCODE = bsInfo.getBirthcode(); 
 						String BIRTHDAY = sdf.format(bsInfo.getBirthday()); 
 						String GUARDIANNAME = bsInfo.getGuardianname(); 
 						String ID = UUID.randomUUID().toString();//随机生成
 						Date createtime = new Date();
 	        			//插入关联表
 	                    appChildService.insertChildInfo(Guardianmobile,ID,createtime,CHILDCODE,CHILDNAME,CARDCODE,BIRTHCODE,BIRTHDAY,GUARDIANNAME);//插入app用户表
 	                    
 						logger.error("关联成功-->"+Guardianmobile);
 						//return new JSONMessage(true, "关联成功");
 						}
 					
 					String Fatherphone = bsInfo.getFatherphone();
 					
 					//验证父亲手机号
 					if(StringUtils.isNoneBlank(Fatherphone)){
 						//父亲手机号不为空
 						//爸爸手机号和妈妈手机号不一致
 						if(!Fatherphone.equals(Guardianmobile))
 						{
							
							Integer fcount = appChildService.getIsAttenByPhone(Fatherphone, childCode);
			            	   
							if(fcount>0){
		            		 //已关注，提示宝宝已被关注
								logger.error("宝宝已被关注-->"+bsInfo.getGuardianmobile());
							}else{
								SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );						
								String CHILDCODE = bsInfo.getChildcode(); 
								String CHILDNAME = bsInfo.getChildname(); 
								String CARDCODE = bsInfo.getCardcode(); 
								String BIRTHCODE = bsInfo.getBirthcode(); 
								String BIRTHDAY = sdf.format(bsInfo.getBirthday()); 
								String GUARDIANNAME = bsInfo.getGuardianname(); 
								String ID = UUID.randomUUID().toString();//随机生成
								Date createtime = new Date();
			        			
			        			//插入关联表
			                    appChildService.insertChildInfo(Fatherphone,ID,createtime,CHILDCODE,CHILDNAME,CARDCODE,BIRTHCODE,BIRTHDAY,GUARDIANNAME);//插入app用户表
			                   
								logger.error("关联成功-->"+Fatherphone);
								//return new JSONMessage(true, "关联成功");
							}
 						}else{
 							logger.error("父母手机号一致-->"+childCode);
 						}
 					} 
 					
 					return new JSONMessage(true, "执行完成！");
 					
 				}
  				
  			} catch (Exception e) {
  				logger.error("关注宝宝接口错误",e);
  			}
  		}
  		//没有查询的结果
  		return new JSONMessage(false, "关注宝宝失败");
  	}
  	
  	
	/**
	 * 获取微信用户签字记录
	 * @author zhouqj
	 * @date 2017年10月18日 下午2:57:29
	 * @description
	 *		TODO
	 * @param json
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/rabiesTempSign" , method = RequestMethod.POST)
	public @ResponseBody JSONMessage rabiesTempSign(@RequestBody(required = false) Map<String, String> map) {
		JSONMessage jsonmsg;
		try {
			logger.info("接到查询签字信息请求,接收参数为:" + JsonMapper.toJsonString(map));
			/*logger.info("接到查询签字信息请求,接收参数为:" + json);
			Map<String, String> map = (Map<String, String>) JsonMapper.fromJsonString(json, Map.class);*/
			//接种单位
			String office = map.get("office");
			//记录id
			List<String> vList = (List<String>) JsonMapper.fromJsonString(JsonMapper.toJsonString(map.get("vList")), List.class);
			//查询签字记录
			List<VacRemind> vr = new ArrayList<VacRemind>();
			VacRemind vacRemind = null;
			for(String id : vList){
				vacRemind = new VacRemind();
				vacRemind = rabiesService.findVacRemindById(id,office);
				if(vacRemind != null){
					vr.add(vacRemind);
				}
			}
			if(vr.size() > 0){
				jsonmsg = new JSONMessage(true,vr);
				logger.info("查询成功 -->" + JSONObject.fromObject(jsonmsg));
			}else{
				jsonmsg = new JSONMessage(true,vr);
				logger.info("查询成功 -->" + JSONObject.fromObject(jsonmsg));
			}
			logger.info("接到查询签字信息请求,接收参数为:" + JsonMapper.toJsonString(vr));

			return jsonmsg;
		} catch (Exception e) {
		}
		jsonmsg =  new JSONMessage(false, "系统内部错误,请联系管理员");
		logger.info("查询失败 -->" + JSONObject.fromObject(jsonmsg));
		return jsonmsg;
	}

	/**
	 * 查询自主建档信息[成人]
	 * @author fuxin
	 * @date 2017年4月18日 下午8:19:56
	 * @description
	 *		TODO
	 * @param code
	 * @return
	 *
	 */
	@RequestMapping("/hepbTemp/{code}")
	public @ResponseBody JSONMessage hepbTemp(@PathVariable(value="code") String code) {
		JSONMessage jsonmsg;
		try {
			logger.info("接到查询临时建档[犬伤]请求,临时编号为:" + code);
			if(StringUtils.isNotBlank(code)){
				VacHepbTemp temp = rabiesService.getHepb(code);
				if(temp != null){
					List<BsHepb> list = rabiesService.getHepbByTempid(temp.getTempId());
					if(list.size() > 0){
//						rabiesService.deleteReal(code);
					}else{
						temp.setId("");
						jsonmsg = new JSONMessage(true, temp);
						logger.info("查询成功[code=" + code + "] -->" + JSONObject.fromObject(jsonmsg));
						return jsonmsg;
					}
				}
			}
			jsonmsg = new JSONMessage(false, "未查询到此条记录或已被使用");
			logger.info("查询失败[code=" + code + "] -->" + JSONObject.fromObject(jsonmsg));
			return jsonmsg;
		} catch (Exception e) {
			logger.error("自助建档接口错误",e);
		}
		jsonmsg =  new JSONMessage(false, "系统内部错误,请联系管理员");
		logger.info("查询失败[code=" + code + "] -->" + JSONObject.fromObject(jsonmsg));
		return jsonmsg;
	}

	/**
	 * 接口
	 * @author lonny
	 * @date 2018年01月14日
	 * @description
	 *		TODO
	 * @param
	 * @returncode
	 *
	 */
	/*@RequestMapping("/hepbTemp/{code}")
	public @ResponseBody JSONMessage xxx(@PathVariable(value="code") String code) {

		JSONMessage jsonmsg;
		jsonmsg =  new JSONMessage(false, "系统内部错误,请联系管理员");
		logger.info("查询失败[code=" + code + "] -->" + JSONObject.fromObject(jsonmsg));
		return jsonmsg;
	}*/

	/**
	 * 发生取消预约的模板消息
	 * @author lonny
	 * @date 2018年01月14日
	 * @description
	 *		TODO
	 * @param args temp_canceled_child
	 * @return
	 *
	 */
	@RequestMapping(value="/sendWxTempReservation")
	public @ResponseBody JSONMessage sendWxTempReservation(@RequestBody(required = false) Map<String, Object> args){
		if(null != args && args.size() > 0 && null != args.get("openid") && null != args.get("username")
		&&null != args.get("time")&&null != args.get("vaccineName")
				&& null != args.get("clinicName")){
			String openid = (String)args.get("openid");
			String username = (String)args.get("username");
			String time = (String)args.get("time");
			String vaccineName = (String)args.get("vaccineName");
			String clinicName = (String)args.get("clinicName");
			if(StringUtils.isNotBlank(openid)){
				//模板消息通知用户
				TemplateMsgVo temp = new TemplateMsgVo(openid, conf.getTemp_canceled_child(), "");
				@SuppressWarnings("unchecked")
				Map<String, LinkedMap> data = new LinkedMap();
				data.put("first", WxMap.getWxTempMsgMap("您好，您的预约已取消成功，取消预约信息如下："));
				data.put("keyword1", WxMap.getWxTempMsgMap(username));// 姓名：张三
				data.put("keyword2", WxMap.getWxTempMsgMap(time));// 接种时间：2018年08月07日
				data.put("keyword3", WxMap.getWxTempMsgMap(vaccineName));// 接种疫苗：戊肝疫苗益可宁第二针
				data.put("keyword4", WxMap.getWxTempMsgMap(clinicName));// 接种单位：xxx市接种单位
				data.put("remark", WxMap.getWxTempMsgMap("感谢你的使用。"));
				temp.setData(data);
				try {
					weixinjsSDK.sendTemplateMessage(temp);
				} catch (Exception e) {
					logger.error("模板消息发送失败",e);
					return new JSONMessage(false, "模板消息发送失败");
				}
			}
			return new JSONMessage(false, "模板消息发送失败,用户没有关注信息");
		}
		return new JSONMessage(false, "模板消息发送失败");
	}


/*
{
    "childCode": "6515511564815", //儿童编码
    "teyp": "01", //模板编码 01:建档成功通知模板 02:预约通知模板 03:接种通知模板 04:接种完成通知模板 05:留观完成通知模板
    "date": {
        "username": "张三.",//儿童姓名
        "vaccineName": "水痘疫苗", //疫苗名称
        "clinicName": "淮海路门诊"//接种单位名称
    }
}
* */

	@RequestMapping(value="/sendWxTempResation")
	public @ResponseBody JSONMessage sendWxTempResation(@RequestBody(required = false) Map<String, Object> args){
		if(null != args && args.size() > 0 && null != args.get("childCode") && null != args.get("type") &&null != args.get("data")){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			logger.info("收到模板消息的数据"+args.toString());
			String childcode = args.get("childCode").toString();
			String childid =  appChildService.getIDByChildcode(childcode); //根据childcode 获取childId
			String type = args.get("type").toString();
			Map<String,String> date1 = (Map<String,String>) args.get("data");
            StringBuffer inputString = new StringBuffer();
			StringBuffer extrasparam = new StringBuffer();
			List<String> date = new ArrayList<>();
			String first;//头
			String remark;//尾
			String template_id; //模板编号
			String url; //微信跳转地址
			String title; //APP标题
			String content;//APP内容
			String notifyname; //通知名称
			if ("01".equals(type)){ //建档成功通知
				extrasparam.append(childid);
                inputString.append(date1.get("childCode")+",");
                inputString.append(date1.get("username"));
                date.add(date1.get("childCode"));
                date.add(date1.get("username"));
				notifyname="建档成功通知";
				first="恭喜，您的的宝宝档案已建成功。";
				remark="点击该消息可查看宝宝信息,核对信息准确无误,如有错误请及时到接种单位修改。";
				template_id=conf.getTemp_selfhelp_reg();
				url=conf.getTemp_url()+"child/baseinfo/"+childcode+".do";
				title="建档成功通知";
				content="恭喜，您的的自助建档已完成。";
			}else if("02".equals(type)){//预约成功
				extrasparam.append(date1.get("username")+",");
				extrasparam.append(date1.get("clinicName")+",");
				extrasparam.append(date1.get("vatTime")+",");
				extrasparam.append(date1.get("vaccineName")+",");
				extrasparam.append(date1.get("price"));
                inputString.append(date1.get("username")+",");
                inputString.append(date1.get("vatTime")+",");
                inputString.append(date1.get("vaccineName")+",");
                inputString.append(date1.get("clinicName")+",");
                inputString.append(date1.get("price"));
                date.add(date1.get("vaccineName"));
                date.add(date1.get("vatTime"));
                date.add(date1.get("clinicName"));
                date.add(date1.get("username"));
                date.add(date1.get("price"));
				notifyname="预约成功通知";
				first="您好，已为您的宝宝【"+date1.get("username")+"】预约下次接种时间";
				remark="请你于【"+date1.get("vatTime")+"】带上您的宝宝和接种证准时前往接种。";
				template_id=conf.getTemp_reserve_success();
                url=conf.getTemp_url()+"vac/vaccList.do?code="+childcode;
				title="预约成功通知";
				content="您好，已为您的宝宝预约下次接种时间";
			}/*else if("03".equals(type)){ //定时前一天 接种通知
				notifyname="接种通知";
				first="";
				remark="";
				template_id="";
				url="";
				title="接种通知";
				content="";
			}*/else if("04".equals(type)){ //接种完成通知
				extrasparam.append(childid+",");
                extrasparam.append(childcode);
				inputString.append(date1.get("username"));
				date.add(date1.get("username"));
				date.add(date1.get("nextVatTime"));
				notifyname="接种完成通知";
				first="本次疫苗接种已完成，请注意下次接种时间。";
				remark="请等待留观完成。";
				template_id=conf.getTemp_quene_finish();
				url="";
				title="接种完成通知";
				content="本次疫苗接种已完成，请注意下次接种时间。";
			}else if("05".equals(type)){ //留观完成通知
                extrasparam.append(childid+",");
				extrasparam.append(childcode);
				inputString.append(date1.get("username"));
				date.add(date1.get("username"));
				date.add("xxxxxx");
				notifyname="留观完成通知";
				first="您好，现已到达留观要求时间，可带您的宝宝离开。";
				remark="如遇特殊状况，请及时联系社区医生。";
				template_id=conf.getTemp_stay_see();
				url="";
				title="留观完成通知";
				content="您好，现已到达留观要求时间，可带您的宝宝离开。";
			}/*else if("07".equals(type)){//预约取消通知
				extrasparam.append(date1.get("childCode"));
				inputString.append(date1.get("username")+",");
				inputString.append(date1.get("vatTime")+",");
				inputString.append(date1.get("vaccineName")+",");
				inputString.append(date1.get("clinicName")+",");
				inputString.append(date1.get("price"));
				date.add(date1.get("username"));
				date.add(date1.get("vatTime"));
				date.add(date1.get("vaccineName"));
				date.add(date1.get("clinicName"));
				date.add(date1.get("price"));
				notifyname="预约取消通知";
				first="您好，您的预约已取消成功，取消信息如下：";
				remark="感谢你的使用。";
				template_id=conf.getTemp_canceled_child();
				url="";
				title="预约取消通知";
				content="您好，您的预约已取消成功";
			}*/else {
				return new JSONMessage(false, "入参错误");
			}
            logger.info("发送模板消息的数据"+date+"/"+childcode+"/"+first+"/"
                    +remark+"/"+template_id+"/"+url+"/"+title+"/"+content+"/"+type+"/"+extrasparam.toString());
            Notification notification=new Notification();
            notification.setChildcode(childcode);
            notification.setNotifyname(title);
            notification.setNotifytime(new Date());
            notification.setNotifycontent(inputString.toString());
            notification.setType(type);
			notification.setCreateDate(new Date());
			notification.setChildid(childid);
            notificationService.insertNotification(notification);
		    sendWx( date, childcode, first, remark, template_id,  url, title, content,type,extrasparam.toString());
			return new JSONMessage(true, "发送模板消息成功！");
		}else {
			return new JSONMessage(false, "入参错误");
		}
	}

	public  void   sendWx(List<String> date,String childcode,String first,String remark,String template_id, String url,String title,String content,String type,String extrasparam){
		List<String> id = userService.getIdByChildcode(childcode); //查询APP的极光id
        logger.info("ID列表"+id);
		List<String> openid = userService.getOpenidByChildcode(childcode);//查询微信openid
        logger.info("openid列表"+openid);
        if(openid.size() > 0) {
			for(String oid : openid){
				if(StringUtils.isNotBlank(childcode) && StringUtils.isNotBlank(oid)){
                    logger.info("openid"+oid);
					//模板消息通知用户   
					TemplateMsgVo temp = new TemplateMsgVo(oid, template_id, url);
					Map<String, LinkedMap> data = new LinkedMap();
                    data.put("first", WxMap.getWxTempMsgMap(first));
                    for(int i = 0; i < date.size(); i ++){
                       int m=i+1;
                       data.put("keyword"+m, WxMap.getWxTempMsgMap(date.get(i)));
                    }
					data.put("remark", WxMap.getWxTempMsgMap(remark));
					temp.setData(data);
                    logger.info("微信模板消息发送值",temp);
					try {
						String str=weixinjsSDK.sendTemplateMessage(temp);
						logger.info("微信模板消息返回值",str);
					} catch (Exception e) {
						logger.error("模板消息发送失败",e);
					}
				}
			}
		}
		if(id.size() > 0) {
			Boolean bo= jpushMessageService.sendToRegistrationId(id,title,content,extrasparam,type);
			logger.info("APP极光推送返回值",bo);
		}
	}



    /**
     * 微信支付回调
     * @param request
     * @param resposne
     */
    @RequestMapping(value="/notifyUrlWeixin")
    public @ResponseBody String notifyWeixinPayment(HttpServletRequest request,HttpServletResponse response){
        try{
            BufferedReader reader = request.getReader();
            String line = "";
            StringBuffer inputString = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            logger.info(inputString.toString());
            if(!StringUtils.isEmpty(inputString.toString())){
                Map map = PayCommonUtil.doXMLParse(inputString.toString());
                if("SUCCESS".equals(map.get("return_code"))){
                    System.out.println("微信回调成功----------------------" );
                /*  SortedMap<String,Object> parameters = new TreeMap<String,Object>();
                    parameters.put("appid", map.get("appid")); //wxPayResult.getAppid()
                    parameters.put("bank_type", map.get("bank_type"));//bank_type
                    parameters.put("cash_fee", map.get("cash_fee"));//cash_fee
                    parameters.put("fee_type", map.get("fee_type"));//fee_type
                    parameters.put("is_subscribe", map.get("is_subscribe"));//is_subscribe
                    parameters.put("mch_id", map.get("mch_id"));
                    parameters.put("nonce_str", map.get("nonce_str"));
                    parameters.put("openid", map.get("openid"));//
                    parameters.put("out_trade_no", map.get("out_trade_no"));//订单号
                    parameters.put("result_code", map.get("result_code"));
                    parameters.put("time_end", map.get("time_end")); //支付完成时间
                    parameters.put("total_fee", map.get("total_fee"));//总金额
                    parameters.put("trade_type", map.get("trade_type"));
                    parameters.put("transaction_id",map.get("transaction_id")); //微信支付订单号
                    String sign = PayCommonUtil.createSign("UTF-8", parameters); //反校验签名
                    System.out.println("----获取的参数---"+parameters.toString());
                    System.out.println("---- 签名参数---原sign"+map.get("sign") +"-----现sign"+sign.toString());
                    if(sign.equals(map.get("sign"))){
                        //orderService.alipayNotifyPayment(wxPayResult.getOut_trade_no(), wxPayResult.getTransaction_id(),2);//修改订单的状态
                        writer.write(backWeixin("SUCCESS","OK"));
                    }else{
                        writer.write(backWeixin("FAIL","签名失败"));
                    }*/
                    String out_trade_no=  map.get("out_trade_no").toString();//商户订单号
                    String time_end=  map.get("time_end").toString();//支付完成时间
                    String total_fee=  map.get("total_fee").toString();//总金额
                    String transaction_id=  map.get("transaction_id").toString();//微信支付订单号
                    Order order=new Order();
                    order.setOrderNo(out_trade_no);
                    List<Order> list= bsOrderService.findOrderList(order);
                    if(list.size()>0){
                        Order or=new Order();
                        or.setId(list.get(0).getId());
                        or.setStatus("2"); // TransactionNo
                        or.setCallbackTime(new Date());
                        bsOrderService.updateOrderStuatus(or);
                        VacChildRemind entity=new VacChildRemind();
                        entity.setId(list.get(0).getVid());
                        entity.setPayStatus("1");
                        childInfoService.updatePayStatus(entity);
                    }
                    return "<xml> <return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg> </xml>";
                }else{
                    return "<xml> <return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg> </xml>";
                }
            }else{
                return "<xml> <return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg> </xml>";
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return "<xml> <return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg> </xml>";
        }
    }

    /**
     * 支付宝异步请求通知
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/notifyUrlAlipay", method = RequestMethod.POST)
    public @ResponseBody String notifyUrlAlipay( HttpServletRequest request,HttpServletResponse response) {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        String out_trade_no = request.getParameter("out_trade_no"); //商户订单号
        String tradeStatus = request.getParameter("trade_status");
        System.out.println("支付宝回调成功----------------------" );
        logger.info(params.toString());
        if (true/*AlipayNotify.verify(params)*/) {//验证成功
            if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                Order order=new Order();
                order.setOrderNo(out_trade_no);
                List<Order> list= bsOrderService.findOrderList(order);
                if(list.size()>0){
                    for (int i=0;i<list.size();i++){
                        Order or=new Order();
                        or.setId(list.get(i).getId());
                        or.setStatus("2");
                        or.setCallbackTime(new Date());
                        bsOrderService.updateOrderStuatus(or);
                        VacChildRemind entity=new VacChildRemind();
                        entity.setId(list.get(i).getVid());
                        entity.setPayStatus("1");
                        childInfoService.updatePayStatus(entity);
                    }
                }
            }
        } /*else {//验证失败
            System.out.println(">>>>>验签失败" + tradeNo);
            System.out.println(">>>>>交易被关闭了");
            return "fail";
        }*/
        return "SUCCESS";
    }


	@RequestMapping(value = "/precreatePay" , method= RequestMethod.POST) //payType 1:支付宝 2:微信
	public @ResponseBody JSONMessage payInfo( String id ,String totalPrice ,String VaccId ,String childCode,  HttpServletRequest request ) {
		Order order=new Order();
        order.setChildCode(childCode);
		String orderNo= OrderUtil.getOrderId();
        String[] strs=id.split(",");
        String[] vaccs=VaccId.split(",");
        for (int i=0;i<strs.length;i++){
            order.setOrderNo(orderNo);
            order.setSource("2");
            order.setPayPrice(totalPrice);
            order.setStatus("1");
            order.setOrderTime(new Date()); // ORDERSOURCE
            order.setVid(strs[i]);
            order.setVaccId(vaccs[i]);
            bsOrderService.insertVacOrder(order);
        }
		String str = Alipay.AlipayPrecreate("疫苗名称",orderNo,"0.01",conf.getAliPayUrl());
		String str1= weixinPrePay.createNative("疫苗名称",orderNo,"0.01",conf.getWeixinPayUrl(),"NATIVE",request);
		Map map = new HashMap();
		map.put("aliPay",str);
		map.put("weiXinPay",str1);
		map.put("orderNo",orderNo);
		return  new JSONMessage(true, map);
	}


    @RequestMapping(value = "/pollingPay" , method= RequestMethod.POST)
    public @ResponseBody JSONMessage payInfo( String orderNo ) {
        Order order =new Order();
        order.setOrderNo(orderNo);
        List<Order> list= bsOrderService.findOrderStatus(order);
        if (list.size()>0){
            if(list.get(0).getStatus().equals("2")){
                return  new JSONMessage(true, "支付成功");
            }else {
                return  new JSONMessage(false, "支付未完成");
            }
        }else {
            return  new JSONMessage(false, "订单号错误");
        }

    }




}
