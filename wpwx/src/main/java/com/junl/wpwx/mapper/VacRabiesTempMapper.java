package com.junl.wpwx.mapper;

import java.util.List;

import com.junl.wpwx.model.*;


/**
 *
 * @class MemberMapper
 * @author LEON
 * @date 2015年8月24日 下午7:15:28
 * @description
 *		TODO
 *
 */
public interface VacRabiesTempMapper extends CrudMapper<VacRabiesTemp> {

	/**
	 * 删除记录（真删）
	 * @author fuxin
	 * @date 2017年4月18日 下午8:25:40
	 * @description
	 *		TODO
	 * @param code
	 *
	 */
	void deleteReal(String code);


	/**
	 * 乙肝自助建档-保存
	 * @author fuxin
	 * @date 2017年8月11日 下午4:14:01
	 * @description
	 *		TODO
	 * @param info
	 *
	 */
	void insertHepb(VacHepbTemp info);


	/**
	 * 获取乙肝自助建档
	 * @author fuxin
	 * @date 2017年8月11日 下午5:18:56
	 * @description
	 *		TODO
	 * @param id
	 * @return
	 *
	 */
	VacHepbTemp getHepb(String id);


	/**
	 * 提示信息列表
	 * @author zhouqj
	 * @date 2017年10月16日 下午4:09:59
	 * @description
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	List<VacRemind> findVacRemindList(VacRabiesTemp temp);


	/**
	 * 查询单条提示信息
	 * @author zhouqj
	 * @date 2017年10月17日 上午9:28:00
	 * @description
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	VacRemind findVacRemind(VacRemind temp);


	/**
	 * 查询告知书
	 * @author zhouqj
	 * @date 2017年10月17日 上午11:00:31
	 * @description
	 *		TODO
	 * @param vaccineid
	 * @return
	 *
	 */
	CmsDisclosure getCmsDisclosureByVacid(String vaccineid);


	/**
	 * 新增签字
	 * @author zhouqj
	 * @date 2017年10月17日 下午3:12:22
	 * @description
	 *		TODO
	 * @param vr
	 *
	 */
	void insertSignature(VacRemind vr);


	/**
	 * 更新签字状态
	 * @author zhouqj
	 * @date 2017年10月17日 下午3:12:29
	 * @description
	 *		TODO
	 * @param vr
	 *
	 */
	void updateSign(VacRemind vr);


	/**
	 * 查询签字记录
	 * @author zhouqj
	 * @date 2017年10月18日 下午4:29:01
	 * @description
	 *		TODO
	 * @param id
	 * @param office
	 * @return
	 *
	 */
	VacRemind findVacRemindById(String id, String office);


	/**
	 * 用户相同单位相同时间的接种记录
	 * @author zhouqj
	 * @date 2017年10月20日 上午9:10:30
	 * @description
	 *		TODO
	 * @param vr
	 * @return
	 *
	 */
	List<VacRemind> findVacRemindSignList(VacRemind vr);


	/**
	 * 根据建档编号获取犬伤建档信息
	 * @author fuxin
	 * @date 2017年8月11日 下午8:18:57
	 * @description
	 *		TODO
	 * @param tempid
	 * @return
	 *
	 */
	public List<BsHepb> getHepbByTempid(String tempid);

}
