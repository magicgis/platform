package com.junl.wpwx.service.vaccinate;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.junl.wpwx.model.*;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junl.frame.tools.Ehcache;
import com.junl.frame.tools.date.DateUtils;
import com.junl.frame.tools.net.WebUtils;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.common.OrderUtil;
import com.junl.wpwx.common.WxMap;
import com.junl.wpwx.mapper.VacRabiesTempMapper;
import com.junl.wpwx.service.weixin.IWeixinjsSDK;
import com.junl.wpwx.vo.TemplateMsgVo;

/**
 * 微信自助建档Service
 * @author fuxin
 * @version 2017-03-02
 */
@Service
@Transactional(readOnly = true)
public class VacRabiesTempService{

	@Autowired
	private VacRabiesTempMapper mapper;
	@Autowired
	private VacUserService userService;
	@Autowired
	private IWeixinjsSDK weixinjsSDK;
	@Autowired
	private ConfigProperty conf;
	@Resource
	private Ehcache cache;

	protected static final Logger logger = LoggerFactory.getLogger(VacRabiesTempService.class);


	public VacRabiesTemp get(String id) {
		return mapper.get(id);
	}

	/**
	 * 保持自助建档并微信推动
	 * @author fuxin
	 * @date 2017年4月7日 下午6:59:28
	 * @description
	 *		TODO
	 * @param info
	 *
	 */
	public void save(VacRabiesTemp info){

		//生成临时编号
		info.setId(DateUtils.getShortDate() + OrderUtil.generateNum(4));

		mapper.insert(info);
		//发生模板消息通知用户
		VacUser user = userService.get((String) WebUtils.getRequest().getSession().getAttribute("wpwx.userid")); //http://cx.chinavacc.cn/
		TemplateMsgVo temp = new TemplateMsgVo(user.getOpenid(), conf.getTemp_selfhelp_reg_dog(), conf.getTemp_url()+"rabies/detail.do?id="+info.getId());//http://cx.chinavacc.cn/wpwx/
		@SuppressWarnings("unchecked")
		Map<String, LinkedMap> data = new LinkedMap();
		data.put("first", WxMap.getWxTempMsgMap("恭喜，您的的自助建档已完成。"));
		data.put("keyword1", WxMap.getWxTempMsgMap(info.getId()));
		data.put("keyword2", WxMap.getWxTempMsgMap(info.getUsername()));
		data.put("remark", WxMap.getWxTempMsgMap("有效期三天，请尽快前往接种单位完成建档"));
		temp.setData(data);
		try {
			weixinjsSDK.sendTemplateMessage(temp);
		} catch (Exception e) {
			logger.error("模板消息发送失败",e);
		}
	}

	public List<VacRabiesTemp> findList(VacRabiesTemp temp) {
		return mapper.findList(temp);
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
	public void update(VacRabiesTemp baseinfo) {
		mapper.update(baseinfo);

	}

	/**
	 * 删除记录(真删)
	 * @author fuxin
	 * @date 2017年4月18日 下午8:25:07
	 * @description
	 *		TODO
	 * @param code
	 *
	 */
	public void deleteReal(String code) {
		mapper.deleteReal(code);

	}


	/**
	 * 乙肝自助建档
	 * @author fuxin
	 * @date 2017年8月11日 下午4:11:54
	 * @description
	 *		TODO
	 * @param temp
	 *
	 */
	public void save(VacHepbTemp info) {
		//生成临时编号
		info.setId(DateUtils.getShortDate() + OrderUtil.generateNum(4));

		mapper.insertHepb(info);
		//发生模板消息通知用户
		VacUser user = userService.get((String) WebUtils.getRequest().getSession().getAttribute("wpwx.userid"));
		TemplateMsgVo temp = new TemplateMsgVo(user.getOpenid(), conf.getTemp_selfhelp_reg_dog(), conf.getTemp_url()+"rabies/hepbDetail.do?id="+info.getId());//http://cx.chinavacc.cn/wpwx/
		@SuppressWarnings("unchecked")
		Map<String, LinkedMap> data = new LinkedMap();
		data.put("first", WxMap.getWxTempMsgMap("恭喜，您的的自助建档已完成。"));
		data.put("keyword1", WxMap.getWxTempMsgMap(info.getId()));
		data.put("keyword2", WxMap.getWxTempMsgMap(info.getUsername()));
		data.put("remark", WxMap.getWxTempMsgMap("有效期三天，请尽快前往接种单位完成建档"));
		temp.setData(data);
		try {
			weixinjsSDK.sendTemplateMessage(temp);
		} catch (Exception e) {
			logger.error("模板消息发送失败",e);
		}

	}


	public VacHepbTemp getHepb(String id) {
		return mapper.getHepb(id);
	}

	/**
	 * 提示信息列表
	 * @author zhouqj
	 * @param temp
	 * @date 2017年10月16日 下午4:08:47
	 * @description
	 *		TODO
	 * @return
	 *
	 */
	public List<VacRemind> findVacRemindList(VacRabiesTemp temp){
		return mapper.findVacRemindList(temp);
	}

	/**
	 * 查询单条提示信息
	 * @author zhouqj
	 * @date 2017年10月17日 上午9:28:37
	 * @description
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	public VacRemind findVacRemind(VacRemind temp) {
		return mapper.findVacRemind(temp);
	}

	/**
	 * 查询告知书
	 * @author zhouqj
	 * @date 2017年10月17日 上午10:59:14
	 * @description
	 *		TODO
	 * @param vaccineid
	 * @return
	 *
	 */
	public CmsDisclosure getCmsDisclosureByVacid(String vaccineid) {
		try {
			Object obj = cache.getCache(Ehcache.KEY_DISCLOSURE_VACID + vaccineid, "areaCache");
			CmsDisclosure bsp = new CmsDisclosure();
			if(null == obj){
				bsp = mapper.getCmsDisclosureByVacid(vaccineid);
				cache.putCache(Ehcache.KEY_DISCLOSURE_VACID + vaccineid, bsp, "areaCache");
			}else{
				bsp = (CmsDisclosure) obj;
			}
			return bsp;
		} catch (Exception e) {
			return new CmsDisclosure();
		}
	}

	/**
	 * 新增签字
	 * @author zhouqj
	 * @date 2017年10月17日 下午3:11:41
	 * @description
	 *		TODO
	 * @param vr
	 *
	 */
	public void insertSignature(VacRemind vr) {
		mapper.insertSignature(vr);
	}

	/**
	 * 更新签字状态
	 * @author zhouqj
	 * @date 2017年10月17日 下午3:11:51
	 * @description
	 *		TODO
	 * @param vr
	 *
	 */
	public void updateSign(VacRemind vr) {
		mapper.updateSign(vr);
	}

	/**
	 * 查询签字记录
	 * @author zhouqj
	 * @date 2017年10月18日 下午4:18:26
	 * @description
	 *		TODO
	 * @param id
	 * @param office
	 * @return
	 *
	 */
	public VacRemind findVacRemindById(String id, String office) {
		return mapper.findVacRemindById(id,office);
	}

	/**
	 * 用户相同单位相同时间的接种记录
	 * @author zhouqj
	 * @date 2017年10月20日 上午9:07:31
	 * @description
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	public List<VacRemind> findVacRemindSignList(VacRemind vr) {
		return mapper.findVacRemindSignList(vr);
	}


	public List<BsHepb> getHepbByTempid(String tempid) {
		return mapper.getHepbByTempid(tempid);
	}


}