package com.junl.wpwx.mapper;

import com.junl.wpwx.model.VacUser;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author songqingfeng
 * @date 2016年8月4日 上午10:34:58
 * @description 
 *		TODO
 */
public interface VacUserMapper extends CrudMapper<VacUser>{

	VacUser queryObject(Map<String, Object> params);

	/**
	 * 根据宝宝编号获取openid
	 * @author fuxin
	 * @date 2017年3月25日 下午4:28:25
	 * @description 
	 *		TODO
	 * @param childcode
	 * @return
	 *
	 */
	List<String> getOpenidByChildcode(String childcode);



	/**
	 * 根据手机号码获取用户信息
	 * @author fuxin
	 * @date 2017年3月25日 下午4:28:25
	 * @description
	 *		TODO
	 * @param phone
	 * @return
	 *
	 */
	List<VacUser> findByPhone(String phone);


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
	List<String> getIdByChildcode(String childcode);

}
