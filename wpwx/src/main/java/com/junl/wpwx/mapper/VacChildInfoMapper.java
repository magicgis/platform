package com.junl.wpwx.mapper;

import com.junl.wpwx.model.ChildAppinfo;
import com.junl.wpwx.model.SimpleModel;
import com.junl.wpwx.model.VacChildInfo;
import com.junl.wpwx.model.VacChildRemind;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;



/**
 * 资金账户持久化层
 * @class AccountsMapper
 * @author zhengxiang
 * @date 2015年7月15日

 *
 */
public interface VacChildInfoMapper extends CrudMapper<VacChildInfo> {

	/**
	 * 提示签字预约信息列表
	 * @author zhouqj
	 * @date 2017年10月28日 下午12:44:45
	 * @description 
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	List<VacChildRemind> findVacChildRemindList(VacChildInfo temp);

	/**
	 * 查询疫苗信息
	 * @author zhouqj
	 * @date 2017年10月28日 下午2:31:00
	 * @description 
	 *		TODO
	 * @param vcr
	 * @return
	 *
	 */
	String findVacChildNid(VacChildRemind vcr);

	/**
	 * 提示签字预约信息
	 * @author zhouqj
	 * @date 2017年10月30日 上午9:02:50
	 * @description 
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	VacChildRemind findVacChildRemind(VacChildRemind temp);

	/**
	 * 新增签字
	 * @author zhouqj
	 * @date 2017年10月30日 上午10:59:48
	 * @description 
	 *		TODO
	 * @param vr
	 *
	 */
	void insertSignature(VacChildRemind vr);

	/**
	 * 更改记录签字状态
	 * @author zhouqj
	 * @date 2017年10月30日 上午11:03:41
	 * @description 
	 *		TODO
	 * @param vr
	 *
	 */
	void updateSign(VacChildRemind vr);

	/**
	 * 查询签字记录
	 * @author zhouqj
	 * @date 2017年10月30日 上午11:18:04
	 * @description 
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	VacChildRemind findVacChildRemindById(VacChildRemind temp);

	//通过ID查询预约记录
	List<VacChildRemind> findRemindById(VacChildRemind temp);

	//查询时间段接种人数
	List<SimpleModel>  findByTime(VacChildRemind temp);

	//查询时间段接种人数
	int  findByNum(VacChildRemind temp);

	/**
	 * 更新选择日期/更新时间段
	 * @param entity
	 * @return
	 */
	 int updateByTime(VacChildRemind entity);


	List<VacChildRemind> findChildAppList(ChildAppinfo temp);

	List<ChildAppinfo> findAppUserList(VacChildInfo temp);

	void insertAppUser(VacChildInfo vr);

	int updatePayStatus(VacChildRemind entity);

	//通过id查询儿童预约记录
	/*
	 * edit by wangnan 
	 * 2018-2-24
	 * 预约时间+接种单位
	 */
	public HashMap<String, String> findRemindInfoById(@Param(value = "id")String id);

}
