/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.service.vaccinate;


import com.junl.wpwx.mapper.NotificationMapper;
import com.junl.wpwx.mapper.WorkingHouesMapper;
import com.junl.wpwx.model.Notification;
import com.junl.wpwx.model.PromotMessage;
import com.junl.wpwx.model.WorkingHours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class WorkingHoursService {

	@Autowired
	private WorkingHouesMapper mapper;

	@Transactional(readOnly = false)
	public List<WorkingHours> findByLocalcode (WorkingHours work ) {
		return mapper.findByLocalcode(work);
	}


}