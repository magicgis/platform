package com.junl.wpwx.task;


import com.junl.frame.tools.date.DateUtils;
import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.common.WxMap;
import com.junl.wpwx.model.Notification;
import com.junl.wpwx.model.VacChildRemind;
import com.junl.wpwx.service.jpush.JpushMessageService;
import com.junl.wpwx.service.vaccinate.NotificationService;
import com.junl.wpwx.service.vaccinate.VacService;
import com.junl.wpwx.service.weixin.IWeixinjsSDK;
import com.junl.wpwx.vo.TemplateMsgVo;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时提醒任务
 * @author zhouqj
 * @date 2017年10月30日 下午2:32:25
 * @description 
 *		TODO
 */

public class MarketDealTask {
	protected static Logger logger = LoggerFactory.getLogger(MarketDealTask.class);
	
	@Autowired
	VacService vacService;
	@Autowired
	ConfigProperty conf;
	@Autowired
	IWeixinjsSDK weixinjsSDK;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private JpushMessageService jpushMessageService;
	
	public MarketDealTask() {}
	
	public void execute(){
		logger.info("=============开始执行定时提醒任务===================");
		int count = 0;
		int countF = 0;
		List<VacChildRemind> jobs = vacService.findJobRemind();
		for(VacChildRemind job : jobs){
			String url = "";
			if(StringUtils.isNotEmpty(job.getId())){
				url = conf.getTemp_url()+"vac/vaccList.do?code="+job.getChildcode();
			}

			//模板消息通知用户
	    	TemplateMsgVo temp = new TemplateMsgVo(job.getOpenid(), conf.getTemp_remind_child(), url);
	    	@SuppressWarnings("unchecked")
			Map<String, LinkedMap> data = new LinkedMap();
	    	data.put("first", WxMap.getWxTempMsgMap("您好,明天您孩子需要接种疫苗哟！"));
	    	data.put("keyword1", WxMap.getWxTempMsgMap(job.getChildname()));
	    	data.put("keyword2", WxMap.getWxTempMsgRedMap(DateUtils.dateParseShortString(job.getRemindDate()))); //DateUtils.dateParseShortString
	    	data.put("keyword3", WxMap.getWxTempMsgMap(job.getRemindVacc()));
	    	data.put("remark", WxMap.getWxTempMsgMap("点击该消息可查看预约信息列表并提前签字，请及时前往接种门诊接种疫苗，感谢使用。"));
	    	temp.setData(data);
	    	try {
				weixinjsSDK.sendTemplateMessage(temp);
				count ++;
			} catch (Exception e) {
				logger.error("模板消息发送失败",e);
				countF ++;
			}
		}
		List<VacChildRemind> apps = vacService.findAppRemind();

		//贺大大,2018-02-12,水痘疫苗（￥123）,淮海路接种门诊,￥123
		for(VacChildRemind app : apps){
			if(app.getRegistrationid()!=null){
				StringBuffer inputString = new StringBuffer();
				StringBuffer appStr = new StringBuffer();
				inputString.append(app.getChildname()+",");
				inputString.append(DateUtils.dateParseShortString(app.getRemindDate())+",");
				inputString.append(app.getRemindVacc()+",");
				inputString.append(app.getLocalname()+",");//接种单位名称
				inputString.append(app.getPayPrice());//价格
				appStr.append(app.getChildcode()); //儿童code
				Notification notification=new Notification();
				notification.setChildcode(app.getChildcode());
				notification.setNotifyname("疫苗接种提醒");
				notification.setNotifytime(new Date());
				notification.setNotifycontent(inputString.toString());
				notification.setType("03");
				notification.setChildid(app.getId());
				notificationService.insertNotification(notification);
				List<String> list=new ArrayList<>();
				list.add(app.getRegistrationid());
				jpushMessageService.sendToRegistrationId(list,"疫苗接种提醒","点击该消息可查看预约信息列表并提前签字，请及时前往接种门诊接种疫苗，感谢使用。",appStr.toString(),"03");
			}
		}
		logger.info("=============定时提醒任务结束 [成功:" + count + " 失败:" + countF + "]===================");
		logger.info("=============清理过时提醒开始===================");
		int countD = vacService.clearJobRemind();
		logger.info("=============清理过时提醒结束[清理:" + countD + "]===================");
	}

}
