package com.junl.wpwx.service.vaccinate;

import java.util.ArrayList;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.junl.wpwx.model.ChildTemp;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junl.frame.tools.CommonUtils;
import com.junl.frame.tools.Ehcache;
import com.junl.frame.tools.net.WebUtils;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.common.WxMap;
import com.junl.wpwx.common.sms.SMS;
import com.junl.wpwx.mapper.VacChildTempMapper;
import com.junl.wpwx.model.SimpleModel;
import com.junl.wpwx.model.VacChildTemp;
import com.junl.wpwx.model.VacUser;
import com.junl.wpwx.service.AsyncService;
import com.junl.wpwx.service.weixin.IWeixinjsSDK;
import com.junl.wpwx.vo.TemplateMsgVo;

/**
 * 微信自助建档Service
 * @author fuxin
 * @version 2017-03-02
 */
@Service
@Transactional(readOnly = true)
public class VacChildTempService{

	@Autowired
	private VacChildTempMapper mapper;
	@Autowired
	private VacUserService userService;
	@Autowired
	private IWeixinjsSDK weixinjsSDK;
	@Autowired
	private VacService vacService;
	@Autowired
	private ConfigProperty conf;
	@Autowired
	private AsyncService asyncService;
	
	protected static final Logger logger = LoggerFactory.getLogger(VacChildTempService.class);
	
	
	/**
	 * 保持自助建档并微信推动
	 * @author fuxin
	 * @date 2017年4月7日 下午6:59:28
	 * @description 
	 *		TODO
	 * @param info
	 *
	 */
	public synchronized VacChildTemp  save(VacChildTemp info){
		
		//更新住址信息
		info = vacService.updateAddr(info);
		//添加建档id
		info.setTempid(CommonUtils.UUIDGenerator());
		
		//生成临时编号
		//String vacccode = info.getOfficeinfo();
		info.setChildcode( getLastCode());
		info.setUserId(WebUtils.getRequest().getSession().getAttribute("wpwx.userid").toString());
		info.setFileorigin("2");
		mapper.insert(info);
		//发生模板消息通知用户
    	VacUser user = userService.get((String) WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
    	TemplateMsgVo temp = new TemplateMsgVo(user.getOpenid(), conf.getTemp_selfhelp_reg(), conf.getTemp_url()+"/child/temp/detail.do?childcode="+info.getChildcode());
    	@SuppressWarnings("unchecked")
		Map<String, LinkedMap> data = new LinkedMap();
    	data.put("first", WxMap.getWxTempMsgMap("恭喜，您的的自助建档已完成。"));
    	data.put("keyword1", WxMap.getWxTempMsgMap(info.getChildcode()));
    	data.put("keyword2", WxMap.getWxTempMsgMap(info.getChildname()));
    	data.put("remark", WxMap.getWxTempMsgMap("有效期三天，请尽快前往接种单位完成建档并领取接种证"));
    	temp.setData(data);
    	try {
			String succ = weixinjsSDK.sendTemplateMessage(temp);
			JSONObject op = JSONObject.fromObject(succ);
			if(0 != (int)op.get("errcode")){
				throw new RuntimeException("模板消息发送失败");
			}
		} catch (Exception e) {
			logger.error("模板消息发送失败,发生短信提醒",e);
			Map<String, Object> parm = new HashMap<String, Object>();
			parm.put("no", info.getChildcode());
			parm.put("name", info.getChildname());
			asyncService.sendSMS(info.getGuardianmobile(), parm, SMS.TEMP_SELF_SUCCESS);
		}
		return  info;
	}

	public List<VacChildTemp> findList(VacChildTemp temp) {
		return mapper.findList(temp);
	}

	
	/**
	 * 根据查询自助建档信息
	 * @author fuxin
	 * @date 2017年4月7日 下午6:19:16
	 * @description 
	 *		TODO
	 * @param code
	 * @return
	 *
	 */
	public VacChildTemp getByCode(String code) {
		return mapper.getByCode(code);
	}

	public List<VacChildTemp> getByPhone(String phone) {
		return mapper.getByPhone(phone);
	}

	
	/**
	 * 使用过一次的临时编码会失效
	 * @author fuxin
	 * @date 2017年4月7日 下午6:26:51
	 * @description 
	 *		TODO
	 * @param baseinfo
	 *
	 */
	public void update(VacChildTemp baseinfo) {
		mapper.update(baseinfo);
		
	}

	/**
	 * 获取改组织机构代码最新排号
	 * @author fuxin
	 * @date 2017年4月7日 下午9:03:44
	 * @description 
	 *		TODO
	 * @param pre
	 * @return
	 *
	 */
	public synchronized String getLastCode( ){
		List<String> allcode = mapper.getlastcode();
		if(allcode.size() > 0){
			String endcode = allcode.get(0);
			try {
				int endint = Integer.parseInt(endcode);
				endint ++;
				endcode = endint + "";
				for(int i = endcode.length(); i < 4; i ++){
					endcode = "0" + endcode;
				}
//				return pre + " " + endcode;
				return endcode;
			} catch (Exception e) {
				logger.error("生成临时建档编码失败"+e);
			}
		}
//		return pre + " " + "0001";
		return "0001";
	}

	/**
	 * 删除记录-真删
	 * @author fuxin
	 * @date 2017年4月22日 下午12:10:42
	 * @description 
	 *		TODO
	 * @param baseinfo
	 *
	 */
	public void deletereal(VacChildTemp baseinfo) {
		mapper.deletereal(baseinfo);
	}



	public VacChildTemp save1(VacChildTemp info){
		//添加建档id
		info.setTempid(CommonUtils.UUIDGenerator());
		//生成临时编号
		//String vacccode = info.getOfficeinfo();
		info.setChildcode( getLastCode());
		logger.info(info.toString());
		int i= mapper.insert(info);
		return  info;
	}
	
	//edit by wangnan 2018-2-8 建档记录APP
	public List<HashMap<String,String>> getRecordByUserID(String userId) {
		return mapper.getRecordByUserID(userId);
	}
	
	//edit by wangnan 2018-2-9 建档记录详情APP
	public HashMap<String,String> getRecordByID(String childId) {
		return mapper.getRecordByID(childId);
	}
	
	//edit by wangnan 2018-3-2建档记录详情APP 通过childcode
	public HashMap<String,String> getRecordByCode(String childcode) {
		return mapper.getRecordByCode(childcode);
	}
		
	

	//edit by wangnan 2018-2-23 预约记录APP
	public List<HashMap<String,String>> getRemidRecordByID(String childId) {
		return mapper.getRemidRecordByID(childId);
	}
	
	//edit by wangnan 2018-3-2 预约记录APP
	public List<HashMap<String,String>> getRemidRecordByCode(String childcode) {
		return mapper.getRemidRecordByCode(childcode);
	}
	
	
	/**
	 * 获取医院信息
	 * @author wangnan
	 * @date 2018年3月23日 下午4:17:41
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<HashMap<String,String>> getHostipallist(String code) {
		return mapper.getHostipallist(code);
	}
	
	/**
	 * 查询记录是否存在
	 * @author wangnan
	 * @date 2018年3月23日 下午4:17:41
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public  Integer findChildRemind(String id) {
		return mapper.findChildRemind(id);
	}


	public ChildTemp childSava(ChildTemp info){
		//添加建档id
		info.setId(CommonUtils.UUIDGenerator());
		//生成临时编号
		info.setChildcode( getLastCode());
		info.setTempid(CommonUtils.UUIDGenerator());//建档编号
		info.setProvince(info.getFamilyaId().substring(0,2)+"0000");
		info.setCity(info.getFamilyaId().substring(0,4)+"00");
		info.setPr(info.getRegisteraId().substring(0,2)+"0000");
		info.setCi(info.getRegisteraId().substring(0,4)+"00");
		logger.info(info.toString());
		int i= mapper.inserChildTemp(info);
		return  info;
	}
}