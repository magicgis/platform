package com.junl.wpwx.mapper;

import com.junl.wpwx.model.ChildTemp;
import com.junl.wpwx.model.VacChildTemp;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;



/**
 * 
 * @class MemberMapper
 * @author LEON
 * @date 2015年8月24日 下午7:15:28
 * @description
 *		TODO
 *
 */
public interface VacChildTempMapper extends CrudMapper<VacChildTemp> {

	
	/**
	 * 根据查询自助建档信息
	 * @author fuxin
	 * @date 2017年4月7日 下午6:19:40
	 * @description 
	 *		TODO
	 * @param code
	 * @return
	 *
	 */
	VacChildTemp getByCode(String code);



	/**
	 * 根据手机号查询自助建档信息

	 * @description
	 *		TODO
	 * @param phone
	 * @return
	 *
	 */
	List<VacChildTemp> getByPhone(String phone);

	/**
	 * 获取最新代码
	 * @author fuxin
	 * @date 2017年4月7日 下午8:30:59
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	List<String> getlastcode();

	/**
	 * 删除记录-真删
	 * @author fuxin
	 * @date 2017年4月22日 下午12:11:17
	 * @description 
	 *		TODO
	 * @param baseinfo
	 * @return
	 *
	 */
	void deletereal(VacChildTemp baseinfo);


	//edit by wangnan 2018-2-8 建档记录APP
	public List<HashMap<String,String>> getRecordByUserID(@Param(value = "userId")String userId);


		
	//edit by wangnan 2018-2-23 预约记录APP
	public List<HashMap<String,String>> getRemidRecordByID(@Param(value = "childId")String childId);	

	/**
	 * 获取医院信息
	 * @author wangnan
	 * @date 2018年3月23日 下午4:17:41
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<HashMap<String,String>> getHostipallist(@Param(value = "code")String code);
	
	/**
	 * 查询记录是否存在
	 * @author wangnan
	 * @date 2018年3月23日 下午4:17:41
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public Integer findChildRemind(@Param(value = "id")String id);
	
	
	//edit by wangnan 2018-2-9 建档记录详情APP
	public HashMap<String,String> getRecordByID(@Param(value = "childId")String childId) ;


	public  Integer inserChildTemp(ChildTemp childTemp);


	//edit by wangnan 2018-3-2建档记录详情APP 通过childcode
	public HashMap<String,String> getRecordByCode(@Param(value = "childcode")String childcode);

	//edit by wangnan 2018-3-2 预约记录APP
	public List<HashMap<String,String>> getRemidRecordByCode(@Param(value = "childcode")String childcode);
}
