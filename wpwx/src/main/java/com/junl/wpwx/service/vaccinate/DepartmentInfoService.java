/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.junl.wpwx.service.vaccinate;


import com.junl.wpwx.mapper.DepartmentInfoMapper;
import com.junl.wpwx.model.DepartmentInfo;
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
public class DepartmentInfoService {

	@Autowired
	private DepartmentInfoMapper mapper;

	
	@Transactional(readOnly = false)
	public List<DepartmentInfo>  findByChildcode(String localcode ) {
		return mapper.findByLocalcode(localcode);
	}

	
	 //我的界面-接种单位  edit by wangnan 2018-2-27 
    public HashMap<String,String> findByChildId(String childId) {
        return mapper.findByChildId(childId);
	}
}