/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.service.vaccinate;


import com.junl.wpwx.mapper.NotificationMapper;
import com.junl.wpwx.model.Notification;
import com.junl.wpwx.model.PromotMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author chenmaolong
 * @date 2017年3月3日 上午11:43:25
 * @description 
 *		TODO
 */
@Service
@Transactional(readOnly = true)
public class NotificationService {

	@Autowired
	private NotificationMapper mapper;

	
	@Transactional(readOnly = false)
	public List<Notification>  findByChildcode(String childcode) {
		return mapper.findByChildcode(childcode);
	}

	@Transactional(readOnly = false)
	public List<PromotMessage>  findByMessage( ) {
		return mapper.findByMessage();
	}

	@Transactional(readOnly = false)
	public PromotMessage findById(String id ) {
		return mapper.findById(id);
	}

	public int insertNotification(Notification notification){return mapper.insertNotification(notification);}

	/**
     * 消息中心-系统消息
     * * edit by wangnan
     * 2018-3-1
     */
	public List<HashMap<String, String>>  findMessage() {
		return mapper.findMessage();
	}

	/**
     * 消息中心-系统消息-详情
     * * edit by wangnan
     * 2018-3-1
     */
	public String getMessDetail(String id) {
		return mapper.getMessDetail(id);
	}
		
}