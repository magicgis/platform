package com.junl.wpwx.service.vaccinate;



/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.beecloud.BCEumeration.PAY_CHANNEL;
import cn.beecloud.BCPay;
import cn.beecloud.bean.BCException;
import cn.beecloud.bean.BCOrder;
import cn.beecloud.bean.BCRefund;

import com.alibaba.fastjson.JSON;
import com.junl.frame.tools.date.DateUtils;
import com.junl.frame.tools.net.WebUtils;
import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.common.OrderUtil;
import com.junl.wpwx.common.WxMap;
import com.junl.wpwx.form.OrderForm;
import com.junl.wpwx.mapper.BsMapper;
import com.junl.wpwx.mapper.VacOrderMapper;
import com.junl.wpwx.model.BsChildBaseInfo;
import com.junl.wpwx.model.BsOrder;
import com.junl.wpwx.model.BsVaccNum;
import com.junl.wpwx.model.VacOrder;
import com.junl.wpwx.model.VacUser;
import com.junl.wpwx.service.AsyncService;
import com.junl.wpwx.service.weixin.IWeixinjsSDK;
import com.junl.wpwx.vo.TemplateMsgVo;

/**
 * 微信订单管理Service
 * @author fuxin
 * @version 2017-03-02
 */
@Service
@Transactional(readOnly = true)
public class VacOrderService {

	protected static final Logger logger = LoggerFactory.getLogger(VacOrderService.class);
	
	@Autowired
	private VacOrderMapper mapper;
	@Autowired
	private ConfigProperty conf;
	@Autowired
	private VacService vacService;
	@Autowired
	private IWeixinjsSDK weixinjsSDK;
	@Autowired
	private VacUserService userService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private BsMapper bsmapper;
	
	
	public VacOrder get(String id){
		return mapper.get(id);
	}
	
	public void save(VacOrder order) {
		if(StringUtils.isNotEmpty(order.getId())){
			mapper.update(order);
		}else{
			mapper.insert(order);
		}
	}


	/**
	 * 保存订单信息，并调用bee cloud支付
	 * @author fuxin
	 * @param pname 
	 * @param childcode2 
	 * @date 2017年3月7日 上午10:38:17
	 * @description 
	 *		TODO
	 * @return
	 * @throws Exception
	 *
	 */
	@Transactional(readOnly = false)
	public Map<String, String> doBeeCloud(OrderForm form, String pname){
//		BsVaccNum num = vacService.getNumByid(form.getNid());
//		String name = num.getName();
		//生成订单信息
		VacOrder order = new VacOrder();
		order.setPayprice(form.getPrice()); //金额
		order.setProductname(pname); //产品名称
		order.setProductid(form.getPid());// 产品ID //金额 //儿童编码 //  模型id
		order.setUserid((String) WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
		order.setChildcode(form.getChildcode());//儿童编码
		order.setStatus(VacOrder.STATUS_WAIT);
		order.setOrderNo(OrderUtil.getOrderNo());
		order.setOrdertime(new Date());
		order.setNumid(form.getNid());//  模型id
//		order.setMobile(mobile);
//		order.setTransactionno(transactionno);
		order.setUtmsource(VacOrder.UTMSOURCE_WEIXIN);
		order.setIsvaccine(VacOrder.ISVACCINE_NO);
		order.setInsurance(form.isInsurance());// 保险费
		//保存订单信息
		
		//============================================
//		order.setPayprice(1);
		//============================================
		
		mapper.insert(order);
		
		
		// 调用bee cloud支付
		Map<String, String> map = new HashMap<String, String>();
		try {
			String openId = (String) WebUtils.getRequest().getSession().getAttribute("wpwx.openId"); //openId
			BCOrder bcOrder = new BCOrder(PAY_CHANNEL.WX_JSAPI, order.getPayprice(), order.getOrderNo(), order.getProductname()); // "", 金额 ,订单号，名称
			//放入自定义属性
			Map<String, Object> args = new HashMap<>();
			args.put("oid", order.getId());
			args.put("orderno", order.getOrderNo()); //订单号
			args.put("orderFee", order.getPayprice()); //金额/分
			args.put("source", VacOrder.UTMSOURCE_WEIXIN);
			bcOrder.setOptional(args);
			bcOrder.setOpenId(openId);
			logger.info("发起beecloud支付请求: " + JSON.toJSONString(bcOrder));
			bcOrder = BCPay.startBCPay(bcOrder);
			logger.info("beecloud支付请求成功:"+ bcOrder.getObjectId() + "INFO-->" + JSON.toJSONString(bcOrder));
			map = bcOrder.getWxJSAPIMap();
		} catch (Exception e) {
			logger.error("调用beecloud失败",e);
			return map;
		}
		
		return map;
	}


	/**
	 * 根据条件筛选订单
	 * @author fuxin
	 * @date 2017年3月9日 上午10:41:38
	 * @description 
	 *		TODO
	 * @param order
	 * @return
	 *
	 */
	public List<VacOrder> findList(VacOrder order) {
		return mapper.findList(order);
	}


	/**
	 * 接到beeCloud响应数据，更新对应订单状态
	 * @author fuxin
	 * @date 2017年3月9日 上午10:49:37
	 * @description 
	 *		TODO
	 * @param op
	 *
	 */
	@Transactional(readOnly = false)
	public void finishOrder(JSONObject jsonObj) {
//		String optional = "{'oid':'C390CE1545FC4508AB7A8811A48C84E6','orderno':'1170309145622068','orderFee': '200'}";
//	    JSONObject op = JSONObject.fromObject(optional);
		JSONObject op = JSONObject.fromObject(jsonObj.get("optional"));
		//获取来源
		String source = op.getString("source");
		//微信预约处理
		if(VacOrder.UTMSOURCE_WEIXIN.equals(source)){
			String oid = op.getString("oid");
	        String orderno = op.getString("orderno");
	        int orderFee = op.getInt("orderFee");
	        
	        VacOrder order = new VacOrder();
	        order.setId(oid);
	        order.setOrderNo(orderno);
	        order.setPayprice(orderFee);
	        List<VacOrder> orders = mapper.findList(order);
	        if(orders.size() == 1 ){
	        	//正常状态
	        	VacOrder o = orders.get(0);
	        	o.setStatus(VacOrder.STATUS_FINISH);
	        	o.setCallbacktime(new Date());
	        	//解析微信付款方式
		        //获取支付方式
		  		String channel_type = jsonObj.getString("channel_type");
	        	if("WX".equals(channel_type)){
	        		o.setPaytype(VacOrder.PAYTYPE_WEIXINPAY);
	        	}
	        	mapper.update(o);
	        	
	        	//获取用户信息
	        	VacUser user = userService.get(o.getUserid());
	        	if(VacOrder.INSURANCE_YES.equals(o.getInsurance())){
	        		if(o.getNumid().contains("_")){
						for(String n : o.getNumid().split("_")){
							asyncService.buyInsurance(o.getChildcode(), n, conf, user.getOpenid());
						}
					}else{
						asyncService.buyInsurance(o.getChildcode(), o.getNumid(), conf, user.getOpenid());
					}
	        	}
	        	
	        	//增加接种记录
	        	BsChildBaseInfo baseinfo = vacService.getBsChildBaseInfoByCode(o.getChildcode());
	        	
	        	//模板消息通知用户预约成功
	        	TemplateMsgVo temp = new TemplateMsgVo(user.getOpenid(), conf.getTemp_order_finish(), "");
	        	@SuppressWarnings("unchecked")
				Map<String, LinkedMap> data = new LinkedMap();
	        	data.put("first", WxMap.getWxTempMsgMap("恭喜你预约成功"));
	        	data.put("keyword1", WxMap.getWxTempMsgMap(baseinfo.getChildname()));
	        	data.put("keyword2", WxMap.getWxTempMsgMap(DateUtils.getStringOfTodayDate()));
	        	data.put("keyword3", WxMap.getWxTempMsgMap(o.getProductname()));
	        	data.put("remark", WxMap.getWxTempMsgMap("请到接种点接种"));
	        	temp.setData(data);
	        	try {
					weixinjsSDK.sendTemplateMessage(temp);
				} catch (Exception e) {
					logger.error("模板消息发送失败",e);
				}
	        }
		}else if(VacOrder.UTMSOURCE_SELFMACHINE.equals(source)){
	        //来自一体机
			String orderNo = op.getString("orderNo");
			String insurance = op.getString("insurance");
			VacOrder order = new VacOrder();
			order.setOrderNo(orderNo);
			order.setUtmsource(VacOrder.UTMSOURCE_SELFMACHINE);
			order.setStatus(VacOrder.STATUS_FINISH);
			order.setIsvaccine(VacOrder.ISVACCINE_NO);
			order.setInsurance(insurance);
			String channel_type = jsonObj.getString("channel_type");
			if("ALI".equals(channel_type)){
				order.setPaytype(VacOrder.PAYTYPE_ALIPAY);
			}else if("WX".equals(channel_type)){
				order.setPaytype(VacOrder.PAYTYPE_WEIXINPAY);
			}
			order.setCreateDate(new Date());
			order.setCallbacktime(new Date());
			order.setProductid("0");
			
			//保存订单信息
			mapper.insert(order);
			
			String childcode = op.getString("childcode");
			String nid = op.getString("nid");
			
			if("1".equals(insurance)){
				if(nid.contains("_")){
					for(String n : nid.split("_")){
						asyncService.buyInsurance(childcode, n, conf, null);
					}
				}else{
					asyncService.buyInsurance(childcode, nid, conf, null);
				}
			}
	    }else if(VacOrder.UTMSOURCE_WEIXIN_REFUND.equals(source)){
	    	//来自一体机退款
	    	String orderId = op.getString("orderId");
	    	VacOrder order = mapper.get(orderId);
	    	if(null != order && StringUtils.isNotEmpty(order.getId())){
	    		order.setStatus(VacOrder.STATUS_CANCEL);
	    		save(order);
	    	}
	    }
		
	}

	/**
	 * 插入订单记录（免费）
	 * @author fuxin
	 * @date 2017年3月31日 下午6:46:48
	 * @description 
	 *		TODO
	 * @param form
	 *
	 */
	public void insertOrder(OrderForm form, String name){
//		BsVaccNum num = vacService.getNumByid(form.getNid());
		//生成订单信息
		VacOrder order = new VacOrder();
		order.setProductname(name);
		order.setProductid(form.getPid());
		order.setUserid((String) WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
		order.setChildcode(form.getChildcode());
		order.setStatus(VacOrder.STATUS_FINISH);
		order.setOrderNo(OrderUtil.getOrderNo());
		order.setOrdertime(new Date());
		order.setNumid(form.getNid());
		order.setUtmsource(VacOrder.UTMSOURCE_WEIXIN);
		order.setIsvaccine(VacOrder.ISVACCINE_NO);
		order.setPayprice(form.getPrice());
		order.setInsurance(VacOrder.INSURANCE_NO);
		//保存订单信息
		mapper.insert(order);

		
		BsChildBaseInfo baseinfo = vacService.getBsChildBaseInfoByCode(form.getChildcode());
		
    	//短信通知用户
    	VacUser user = userService.get((String) WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
    	TemplateMsgVo temp = new TemplateMsgVo(user.getOpenid(), conf.getTemp_order_finish(), "");
    	@SuppressWarnings("unchecked")
		Map<String, LinkedMap> data = new LinkedMap();
    	data.put("first", WxMap.getWxTempMsgMap("恭喜你预约成功"));
    	data.put("keyword1", WxMap.getWxTempMsgMap(baseinfo.getChildname()));
    	data.put("keyword2", WxMap.getWxTempMsgMap(DateUtils.getStringOfTodayDate()));
    	data.put("keyword3", WxMap.getWxTempMsgMap(name));
    	data.put("remark", WxMap.getWxTempMsgMap("请到接种点接种"));
    	temp.setData(data);
    	try {
			weixinjsSDK.sendTemplateMessage(temp);
		} catch (Exception e) {
			logger.error("模板消息发送失败",e);
		}
	}

	/**
	 * beeCloud微信退款
	 * @author fuxin
	 * @date 2017年5月16日 下午4:03:48
	 * @description 
	 *		TODO
	 * @param orderNo
	 *
	 */
	public boolean doBeeRefund(VacOrder order) {
		logger.info("发起beecloud退款请求");
		BCRefund param = new BCRefund(order.getOrderNo(), getRefundOrderNo(), order.getPayprice());
		Map<String, Object> options = new HashMap<>();
		options.put("orderId", order.getId());
		options.put("source", VacOrder.UTMSOURCE_WEIXIN_REFUND);
		param.setOptional(options);//optional 可选业务参数
		try {
		   @SuppressWarnings("unused")
		   BCRefund refund = BCPay.startBCRefund(param);
		   return true;
		} catch (BCException e) {
			logger.error("微信退款失败", e.getMessage());
		    e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取退款订单号
	 * @author fuxin
	 * @date 2017年5月17日 下午4:52:30
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public static String getRefundOrderNo(){
		String orderNo = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		orderNo = orderNo + sdf.format(new Date()) + OrderUtil.generateNum(6);
		return orderNo;
	}

	/**
	 * 江南银行支付完成
	 * @author fuxin
	 * @date 2017年10月11日 下午3:49:54
	 * @description 
	 *		TODO
	 * @param jsonStr
	 *	OFFJN12017101813233792|1_1|3406030301|123456789012345678901234567890|66600#1703_201608026_47_9500_0#1703_201409037_47_9500_0#
	 */
	@Transactional(readOnly=false)
	public void finishJnBank(String optional) {
		logger.info("江南银行支付回调校验成功,保存订单数据" + optional);
		Date now = new Date();
		String[] params = optional.split("#");
		String[] orderParams = params[0].split("[|]");
		BsOrder order = new BsOrder();
		order.setOrderNo(orderParams[0]);
		order.setSource(orderParams[1]);
		order.setBeneficiary(orderParams[2]);
		order.setChildCode(orderParams[3]);
		order.setTotal(Integer.valueOf(orderParams[4]));
		order.setVaccineType(order.getOrderNo().substring(5,6));
		order.setChannel(order.getOrderNo().substring(3,5));
		order.setOrderTime(DateUtils.parseDateByFormat(order.getOrderNo().substring(6,20),"yyyyMMddHHmmss"));
		order.setPayTime(now);
		order.setCallTime(now);
		
		for(int i = 1; i < params.length; i ++){
			String[] tempParam = params[i].split("_");
			BsOrder tempOrder = new BsOrder();
			BeanUtils.copyProperties(order, tempOrder);
			tempOrder.setVaccineId(tempParam[0]);
			tempOrder.setBatch(tempParam[1]);
			tempOrder.setManufacturer(tempParam[2]);
			tempOrder.setPrice(Integer.parseInt(tempParam[3]));
			tempOrder.setNum(Integer.parseInt(tempParam[4]));
			mapper.insertBsOrder(tempOrder);
		
		}
		logger.info("江南银行支付回调,保存订单数据成功[" + order.getOrderNo() + "]");
	}

	/**
	 * 订单查询
	 * @author fuxin
	 * @date 2017年10月19日 上午11:01:23
	 * @description 
	 *		TODO
	 * @param bsOrder
	 * @return
	 *
	 */
	public List<BsOrder> findBsOrderList(BsOrder bsOrder) {
		return mapper.findBsOrderList(bsOrder);
	}

	
}

