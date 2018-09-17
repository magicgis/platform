package com.junl.wpwx.service.vaccinate;


/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junl.wpwx.mapper.VacUserMapper;
import com.junl.wpwx.model.VacUser;

/**
 * 微信用户管理Service
 * @author fuxin
 * @version 2017-03-02
 */
@Service
@Transactional(readOnly = true)
public class VacUserService {

	@Autowired
	VacUserMapper mapper;
	
	public VacUser get(String id) {
		return mapper.get(id);
	}

	/**
	 * 根据宝宝编号获取openid
	 * @author fuxin
	 * @date 2017年3月25日 下午4:27:43
	 * @description 
	 *		TODO
	 * @param childcode
	 * @return
	 *
	 */
	public List<String> getOpenidByChildcode(String childcode) {
		return mapper.getOpenidByChildcode(childcode);
	}



	/**
	 * 根据手机号码获取用户信息
	 * @description
	 *		TODO
	 * @param phone
	 * @return
	 *
	 */
	public List<VacUser> findByPhone(String phone) {
		return mapper.findByPhone(phone);
	}


	/**
	 * 根据宝宝编号获取APP的极光ID
	 * @author Lonny
	 * @date 2018年01月14日
	 * @description
	 *		TODO
	 * @param childcode
	 * @return
	 *
	 */
	public List<String> getIdByChildcode(String childcode) {
		return mapper.getIdByChildcode(childcode);
	}
	
}