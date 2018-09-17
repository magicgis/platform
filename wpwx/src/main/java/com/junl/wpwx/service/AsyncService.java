package com.junl.wpwx.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.junl.frame.tools.Ehcache;
import com.junl.frame.tools.date.DateUtils;
import com.junl.frame.tools.http.HttpPost;
import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.common.OrderUtil;
import com.junl.wpwx.common.WxMap;
import com.junl.wpwx.common.sms.SMS;
import com.junl.wpwx.model.BsChildBaseInfo;
import com.junl.wpwx.model.BsVaccNum;
import com.junl.wpwx.model.VacInsurance;
import com.junl.wpwx.service.vaccinate.VacOrderService;
import com.junl.wpwx.service.vaccinate.VacService;
import com.junl.wpwx.service.weixin.IWeixinjsSDK;
import com.junl.wpwx.vo.TemplateMsgVo;

/**
 * 异步业务
 * 
 * @author ngh
 * @datetime [2016年8月8日 下午1:44:34]
 *
 */
@Service
@Async
public class AsyncService {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private Ehcache cache;
	@Resource
	private VacService vacService;
	@Resource
	private VacOrderService orderService;
	@Autowired
	private IWeixinjsSDK weixinjsSDK;
	
	/**
	 * 发送短信验证码
	 * @author fuxin
	 * @date 2017年3月10日 下午2:47:42
	 * @description 
	 *		TODO
	 * @param phone
	 *
	 */
	public boolean sendCheckSMS(String phone) {
		String result = null;
		try {
			//生成6位随机数
			String code = OrderUtil.generateNum(6);
			logger.info("发送短信验证码：phone->" + phone + "  code->" + code);
			//将短信及验证码放入缓存
			cache.putCache(phone, code, "messageCache");
			
			Map<String,Object> parm = new HashMap<String, Object>();
			parm.put("number", code);
			//发送短信验证码
			result = SMS.sendaliSMS(phone, parm, SMS.TEMP_CHILD_CHECK);
			JSONObject jsonObj = JSONObject.fromObject(result);
			//根据返回值判断是否发送成功
			boolean success = jsonObj.getJSONObject("alibaba_aliqin_fc_sms_num_send_response").getJSONObject("result").getBoolean("success");
			if(success){
				logger.info("短信验证码发生成功 phone->" + phone + "  code->" + code + "  info->" + result);
				return true;
			}
		} catch (Exception e) {
			logger.error("短信验证码发送失败phone->" + phone + "  info->" + result);
			return false;
		}
		return false;
	}
	
	
	/**
	 * 发生短信通知
	 * @author fuxin
	 * @date 2017年5月24日 下午2:30:09
	 * @description 
	 *		TODO
	 * @param phone
	 * @param parm
	 * @param templateCode
	 * @return
	 *
	 */
	public boolean sendSMS(String phone, Map<String,Object> parm, String templateCode) {
		String result = null;
		try {
			//生成6位随机数			
			logger.info("发生短信通知：phone->" + phone + "ctx->" + JSONObject.fromObject(parm));			
			
			//发送短信
			result = SMS.sendaliSMS(phone, parm, templateCode);
			JSONObject jsonObj = JSONObject.fromObject(result);
			//根据返回值判断是否发送成功
			boolean success = jsonObj.getJSONObject("alibaba_aliqin_fc_sms_num_send_response").getJSONObject("result").getBoolean("success");
			if(success){
				logger.info("发生短信通知成功 phone->" + phone + " info->" + result);
				return true;
			}
		} catch (Exception e) {
			logger.error("发生短信通知失败phone->" + phone + "  info->" + result);
			return false;
		}
		return false;
	}
	
	
	/**
	 * 购买保险
	 * @author fuxin
	 * @date 2017年4月27日 下午3:51:29
	 * @description 
	 *		TODO
	 * @param childcode 儿童编号
	 * @param nid	计划id
	 * @param rid	接种记录id
	 * @param type 来源方式	1-一体机/ 2-微信
	 * @return 返回保险单号，若保险失败则返回null
	 *
	 */
	public String buyInsurance(String childcode, String nid, ConfigProperty conf, String openid) {
	   String timestamp = System.currentTimeMillis() + "";
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
       String serial_no = conf.getIns_channel_detail()+timestamp;
	       
		logger.info("开始购买保险任务  timestamp-->" + sdf.format(new Date()) + " 儿童编号-->" + childcode + " 计划编号-->" + nid);
	   //儿童信息
	   BsChildBaseInfo baseinfo = vacService.getBsChildBaseInfoByCode(childcode);
	   BsVaccNum num = vacService.getNumByid(nid, baseinfo.getLocalCode());
      
       //拼接请求json
       String json_str="{"+
			    "'third_serial_no': '"+serial_no+"',"+							//交易流水号[渠道号+时间戳]
			    "'user_name': '" + conf.getIns_user_name() + "',"+				//出单用户[配置]
			    "'channel_detail': '" + conf.getIns_channel_detail() + "',"+	//渠道号[配置]
			    "'policy_card_id': '',"+			//--单证号
			    "'pol_apply_date':'"+ sdf.format(new Date())+"',"+				//保单投保日期，格式：yyyy-MM-dd HH:mm:ss，若为空，则默认为当前时间				
			    "'cvali_date': '"+ sdf.format(new Date()) +"',"+				//保单生效日期，格式：yyyy-MM-dd HH:mm:ss
			    "'end_date': '',"+												//--保单失效日期，格式：yyyy-MM-dd HH:mm:ss
			    "'amnt': '',"+													//--保额，单位：分
			    "'prem': '',"+													//--保费，单位：分 
			    "'flight_no': '',"+												//--航班号
			    "'remark': '',"+												//--备注字段
			    "'buss_sign': '',"+												//--业务标示字段。学平险、旅意险投保传batch，其他为空
			    "'plan': {"+													
			        "'plan_code': '" + conf.getIns_plan_code() + "',"+			//计划编码[配置]
			        "'mult': '1'"+												//计划份数
			    "},"+				
			    "'appnt': {"+				
			        "'appnt_name': '" + baseinfo.getGuardianname() + "',"+										//投保人姓名
			        "'appnt_sex': '',"+											//投保人性别 M-男 F-女
			        "'appnt_birthday': '',"+									//被保人出生日期，格式：yyyy-MM-dd，当证件类型是身份证时，可空				
			        "'appnt_id_type': '0',"+									//投保人证件类型，见码表
			        "'appnt_id_no': '" + baseinfo.getGuardianidentificationnumber() + "',"+	 //投保人证件号
			        "'occupation_code': '',"+									//--投保人职业编码，见码表
			        "'address': {"+												
			            "'province': '',"+										//--所在省
			            "'city': '',"+											//--所在市
			            "'county': '',"+										//--所在县
			            "'street_address': '',"+								//--所在街道
//			            "'mobile': '" + baseinfo.getGuardianmobile() + "',"+	//--投保人手机号码
			            "'mobile': '',"+										//--投保人手机号码
			            "'email': ''"+											//--投保人邮箱
			        "}"+
			    "},"+
			    "'insured': {"+													
			      "'relation_to_appnt': '39',"+									//与投保人的关系
			       " 'insured_name': '" + baseinfo.getChildname() + "',"+		//被保人姓名
			        "'insured_sex': '" + ("1".equals(baseinfo.getGender())?"M":"F") + "',"+		//被保人性别，当证件类型是身份证时，可空，见码表  M-男 F-女
			        "'insured_birthday': '" + sdf1.format(baseinfo.getBirthday()) + "',"+		//被保人出生日期，格式：yyyy-MM-dd，当证件类型是身份证时，可空
			        "'insured_id_type': '7',"+									//被保人证件类型 ，见码表
			        "'insured_id_no': '" + baseinfo.getChildcode() + "',"+		//被保人证件号
			        "'occupation_code': ''"+									//--被保人职业编码，见码表
			    "},"+				
			    "'bnfs': []"+													//空数组
	  "}";

       VacInsurance vins = new VacInsurance();
       vins.setInsCreateDate(new Date());
       vins.setInsChildcode(childcode);
       vins.setInsNid(nid);
       vins.setInsReq(json_str);
       vins.setInsType(VacInsurance.TYPE_NEW);
       vins.setInsStatus(VacInsurance.STATUS_WAIT);
       vacService.insertIns(vins);
       
	      try {
	    	   String uri = "http://st.hxlife.com/services/rest/policy/new";
	           logger.info("拼装请求成功 req-->" + json_str + "uri-->" + uri);
	           String result = HttpPost.post(uri, json_str);
	           logger.info("购买保险请求结束 rsv-->" + result);
	           
	           JSONObject op = JSONObject.fromObject(result);
	           if("succ".equals(op.getString("result"))){
	        	   vins.setInsSuccess(VacInsurance.SUCCESS_YES);
	        	   vins.setInsStatus(VacInsurance.STATUS_NORMAL);
	        	   vins.setInsRsv(result);
	        	   vins.setInsSerialNo(op.getString("third_serial_no"));
	        	   vins.setInsPolicyNo(op.getString("policy_no"));
	        	   vacService.updateIns(vins);
	        	   logger.info("购买保险任务成功 childcode-->" + childcode + "   nid-->" + nid);
	        	   //发送提示信息消息
	        	   if(StringUtils.isNotEmpty(openid)){
	        		   //发生微信模板消息
	        		   logger.info("投保成功发送微信模板消息 childcode-->" + childcode + "   nid-->" + nid);
	        	    	TemplateMsgVo temp = new TemplateMsgVo(openid, conf.getTemp_ins_finish(), "");
	        	    	@SuppressWarnings("unchecked")
	        			Map<String, LinkedMap> data = new LinkedMap();
	        	    	data.put("first", WxMap.getWxTempMsgMap("恭喜您投保成功"));
	        	    	data.put("keyword1", WxMap.getWxTempMsgMap(op.getString("policy_no")));
	        	    	data.put("keyword2", WxMap.getWxTempMsgMap(baseinfo.getChildname()));
	        	    	data.put("keyword3", WxMap.getWxTempMsgMap(DateUtils.getStringOfTodayDate()));
	        	    	data.put("remark", WxMap.getWxTempMsgMap("本保险一针一保，本次保险为"+num.getName() + "第" + num.getPin() + "针"));
	        	    	temp.setData(data);
	        	    	try {
	        				weixinjsSDK.sendTemplateMessage(temp);
	        			} catch (Exception e) {
	        				logger.error("模板消息发送失败",e);
	        			}
	        	   }else{
	        		   //发生短信提醒
	        		   logger.info("投保成功发生短信提醒 childcode-->" + childcode + "   nid-->" + nid);
	        		   Map<String, Object> parm = new HashMap<>();
	        		   parm.put("insNo", op.getString("policy_no"));
	        		   parm.put("insTime", DateUtils.getStringOfTodayDate());
	        		   parm.put("insName", baseinfo.getChildname());
	        		   parm.put("${insCtx}", num.getName() + "第" + num.getPin() + "针");
	        		   sendSMS(baseinfo.getGuardianmobile(), parm, SMS.TEMP_INS_FINISH);
	        	   }
	        	   return op.getString("policy_no");
	           }else{
	        	   vins.setInsSuccess(VacInsurance.SUCCESS_NO);
	        	   vins.setInsRsv(result);
	        	   vacService.updateIns(vins);
	        	   logger.error("购买保险任务失败 childcode-->" + childcode + "   nid-->" + nid);
	        	   return null;
	           }
		} catch (Exception e) {
			logger.error("投保失败",e);
			return null;
		}
      
	}
	
	
	/**
	 * 退保
	 * @author fuxin
	 * @date 2017年5月23日 上午11:48:42
	 * @description 
	 *		TODO
	 * @param childcode
	 * @param nid
	 * @param conf
	 * @return boolean
	 */
	public boolean refundInsurance(String childcode, String nid, ConfigProperty conf) {
		JSONObject op = refundInsReq(childcode, nid, conf);
        if("succ".equals(op.getString("result"))){
        	return true;
        }else{
        	return false;
        }
	}
	
	
	/**
	 * 请求退保接口
	 * @author fuxin
	 * @date 2017年5月23日 上午11:36:16
	 * @description 
	 *		TODO
	 * @param childcode
	 * @param nid
	 * @param conf
	 * @return JSONObject
	 */
	public JSONObject refundInsReq(String childcode, String nid, ConfigProperty conf) {
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      String pol_apply_date2 = formatter.format(new Date());
	          
	      Map<String, String> returnMap = new HashMap<>();
	      returnMap.put("result", "fail");
	      returnMap.put("third_serial_no", "");
	      returnMap.put("message", "退保失败,未查到保险订单");
	      
	      JSONObject op = null;
	      VacInsurance ins = new VacInsurance();
	      ins.setInsChildcode(childcode);
	      ins.setInsNid(nid);
	      ins.setInsSuccess(VacInsurance.SUCCESS_YES);
	      ins.setInsStatus(VacInsurance.STATUS_NORMAL);
	      ins.setInsType(VacInsurance.TYPE_NEW);
	      List<VacInsurance> listIns = vacService.findListVacInsurance(ins);
	      if(listIns.size() > 0){
	    	  VacInsurance vins = listIns.get(0);
	          String serial_no=vins.getInsSerialNo();
	          String policy_no=vins.getInsPolicyNo();
	   	
	          String json_str="{"+
	   		    "'third_serial_no': '"+serial_no+"',"+
	   		    "'user_name': '" + conf.getIns_user_name() + "',"+
	   		    "'policy_no': '"+policy_no+"',"+
	   		    "'cancel_date':'"+pol_apply_date2+"',"+
	   		    "'prem':''"+
	   		    "}";
	   		  
	          returnMap.put("third_serial_no", serial_no);
	          returnMap.put("message", "退保失败，请求失败");
	          
           try {
              String uri = "http://st.hxlife.com/services/rest/policy/cancel";
              logger.info("拼装请求成功 req-->" + json_str + "uri-->" + uri);
              String result = HttpPost.httpClientPostJsonGBK(uri, json_str);
              logger.info("退保请求结束 rsv-->" + result);
              op = JSONObject.fromObject(result);
	             
			} catch (Exception e) {
				logger.error("退保失败",e.getMessage());
				if(null == op){
			    	op = JSONObject.fromObject(returnMap);
			     }
				e.printStackTrace();
			}
           
           if("succ".equals(op.getString("result"))){
            	  vins.setInsRefundDate(new Date());
            	  vins.setInsStatus(VacInsurance.STATUS_CANCEL);
            	  vacService.updateIns(vins);
           	   	  logger.info("退保任务成功 childcode-->" + childcode + "   nid-->" + nid);
           	   	  return op;
              }else{
           	   logger.error("退保任务失败 childcode-->" + childcode + "   nid-->" + nid);
           	   return op;
              }
	      }
	        op = JSONObject.fromObject(returnMap);
			return op;
	}

}
