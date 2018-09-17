package com.junl.wpwx.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.junl.wpwx.form.AttenForm;
import com.junl.wpwx.model.*;


/**
 * 
 * @author songqingfeng
 * @date 2016年8月4日 上午10:34:58
 * @description 
 *		TODO
 */
public interface BsMapper{
	
	/**
	 * 根据儿童编号获取 儿童信息
	 * @author fuxin
	 * @date 2017年3月6日 下午1:53:45
	 * @description 
	 *		TODO
	 * @param code
	 * @return
	 *
	 */
	public BsChildBaseInfo getBsChildBaseInfoByCode(String code);

	
	/**
	 * 根据bsChildInfo表的id查询已有的记录
	 * @author fuxin
	 * @date 2017年3月6日 下午2:00:55
	 * @description 
	 *		TODO
	 * @param id
	 * @return
	 *
	 */
	public List<String> getFinishNum(String id);

	/**
	 * 计算儿童根据模型应该接种何种疫苗
	 * @author fuxin
	 * @date 2017年3月6日 下午2:02:29
	 * @description 
	 *		TODO
	 * @param parm
	 * @return
	 *
	 */
	public List<BsVaccNum> getVaccList(Map<String, String> parm);

	/**
	 * 根据模型计算未接种疫苗
	 * @author fuxin
	 * @date 2017年3月23日 上午11:35:25
	 * @description 
	 *		TODO
	 * @param parm
	 * @return
	 *
	 */
	public List<BsVaccNum> getUnfinishedRecord(Map<String, String> parm);

	/**
	 * 根据productId 获取疫苗信息
	 * @author fuxin
	 * @date 2017年3月7日 上午9:27:29
	 * @description 
	 *		TODO
	 * @param pId
	 * @return
	 *
	 */
	public BsProduct getBsProduct(String pid);
	
	/**
	 * 根据父级id查询区域信息
	 * @author fuxin
	 * @date 2017年3月11日 上午11:16:17
	 * @description 
	 *		TODO
	 * @param pid
	 * @return
	 *
	 */
	public List<Area> findAreaByPid(String pid);

	/**
	 * 查询地区信息
	 * @author fuxin
	 * @date 2017年3月13日 下午2:28:46
	 * @description 
	 *		TODO
	 * @param id
	 * @return
	 *
	 */
	public Area findAreaById(String id);


	/**
	 * 查询未完成的接种记录
	 * @author fuxin
	 * @date 2017年3月15日 上午11:04:37
	 * @description 
	 *		TODO
	 * @param id
	 * @return
	 *
	 */
	public List<BsRecord> findRecordList(BsRecord bsRecord);


	/**
	 * 插入接种记录
	 * @author fuxin
	 * @date 2017年3月15日 下午2:17:42
	 * @description 
	 *		TODO
	 * @param record
	 * @return
	 *
	 */
	public void insertRecord(BsRecord record);


	/**
	 * 根据姓名和儿童生日查询
	 * @author fuxin
	 * @date 2017年3月21日 下午4:27:41
	 * @description 
	 *		TODO
	 * @param param
	 * @return
	 *
	 */
	public List<SimpleModel> choosephone(Map<String, String> param);


	/**
	 * 根据生日姓名电话查询儿童信息
	 * @author fuxin
	 * @date 2017年3月21日 下午6:35:22
	 * @description 
	 *		TODO
	 * @param info
	 * @return
	 *
	 */
	public BsChildBaseInfo chooseBaseInfo(AttenForm info);


	/**
	 * 查询儿童已完成的接种记录
	 * @author fuxin
	 * @date 2017年3月22日 下午8:15:36
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<BsRecord> getRecordFinish(String childcode);


	/**
	 * 根据nid和站点编号查询计划信息
	 * @author fuxin
	 * @date 2017年5月24日 下午2:21:18
	 * @description 
	 *		TODO
	 * @param nid
	 * @param localCode
	 * @return
	 *
	 */
	public BsVaccNum getNumByid(String nid, String localCode);


	/**
	 * 获取民族列表
	 * @author fuxin
	 * @date 2017年3月24日 下午2:31:30
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<BsNation> getNationList();


	/**
	 * 查询医院列表
	 * @author fuxin
	 * @date 2017年3月24日 下午4:26:33
	 * @description 
	 *		TODO
	 * @return str1-code str2-name
	 *
	 */
	public List<SimpleModel> getHostipallist();


	/**
	 * 查询社区列表
	 * @author fuxin
	 * @date 2017年3月24日 下午4:40:36
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<SimpleModel> getCommunitylist();


	/**
	 * 根据疫苗大类获取疫苗产品 
	 * @author fuxin
	 * @date 2017年3月27日 下午3:52:38
	 * @description 
	 *		TODO
	 * @param group
	 * @return
	 *
	 */
	@Deprecated
	public List<BsProduct> getProductListByGroup(String group);


	/**
	 * 根据小类id获取风险告知书
	 * @author fuxin
	 * @date 2017年4月1日 下午2:49:46
	 * @description 
	 *		TODO
	 * @param vaccineid
	 * @return 
	 *
	 */
	public CmsDisclosure getCmsDisclosureByVacid(String vaccineid);


	/**
	 * 获取所有接种单位
	 * @author fuxin
	 * @date 2017年4月7日 下午8:20:36
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	@Deprecated()
	public List<BsOffice> getOfficelist();


	/**
	 * 获取所有接种单位信息
	 * @author fuxin
	 * @date 2017年4月10日 上午9:36:34
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<SysDept> getDeptlist();


	/**
	 * 获取字典数组 str1-label str2-value
	 * @author fuxin
	 * @date 2017年4月18日 下午7:26:23
	 * @description 
	 *		TODO
	 * @param string
	 * @return
	 *
	 */
	public List<SimpleModel> getDictList(String type);


	/**
	 * 获取字典标签
	 * @author fuxin
	 * @date 2017年4月19日 上午10:20:54
	 * @description 
	 *		TODO
	 * @param map
	 * @return
	 *
	 */
	public String getDictLabel(Map<String, String> map);

	/**
	 * 通过产品大类获取有库存不为空的最贵的产品
	 * @author fuxin
	 * @date 2017年4月20日 上午11:35:27
	 * @description 
	 *		TODO
	 * @param map
	 * @return
	 *
	 */
	public BsProduct getProductByGroupExp(Map<String, String> map);


	/**
	 * 获取所有接种单位信息 通过编号
	 * @author fuxin
	 * @date 2017年4月21日 下午4:36:12
	 * @description 
	 *		TODO
	 * @param code
	 * @return
	 *
	 */
	public SysDept getDeptlistByCode(String code);


	/**
	 * 查询儿童档案列表
	 * @author fuxin
	 * @date 2017年4月22日 下午12:09:03
	 * @description 
	 *		TODO
	 * @param info
	 * @return
	 *
	 */
	public List<BsChildBaseInfo> getBsChildBaseInfoList(BsChildBaseInfo info);


	/**
	 * 根据身份证查询三天内是建档成功
	 * @author fuxin
	 * @date 2017年4月22日 下午1:20:12
	 * @description 
	 *		TODO
	 * @param card
	 * @return
	 *
	 */
	public List<BsRabies> findRabiesList(VacRabiesTemp temp);


	/**
	 * 获取提示列表
	 * @author fuxin
	 * @date 2017年4月24日 下午5:41:31
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<VacRemind> findVacRemindList();


	/**
	 * 根据建档编号获取犬伤建档信息
	 * @author fuxin
	 * @date 2017年4月27日 上午11:45:40
	 * @description 
	 *		TODO
	 * @param tempid
	 * @return
	 *
	 */
	public List<BsRabies> getByTempid(String tempid);


	/**
	 * 根据大类查询风险告知书
	 * @author fuxin
	 * @date 2017年4月27日 上午11:45:17
	 * @description 
	 *		TODO
	 * @param gr
	 * @return
	 *
	 */
	public CmsDisclosure getCmsDisclosureByGroupId(Object gr);


	/**
	 * 插入保险信息
	 * @author fuxin
	 * @date 2017年4月27日 下午6:51:24
	 * @description 
	 *		TODO
	 * @param vins
	 *
	 */
	public void insertIns(VacInsurance vins);


	/**
	 * 更新保险信息
	 * @author fuxin
	 * @date 2017年4月27日 下午6:59:37
	 * @description 
	 *		TODO
	 * @param vins
	 *
	 */
	public void updateIns(VacInsurance vins);


	/**
	 * 根据条件查询接种记录
	 * @author fuxin
	 * @date 2017年5月17日 上午11:36:35
	 * @description 
	 *		TODO
	 * @param temprec
	 * @return
	 *
	 */
	public List<BsRecord> checkRefundOrder(BsRecord temprec);


	/**
	 * 查询保险列表
	 * @author fuxin
	 * @date 2017年5月17日 下午3:05:57
	 * @description 
	 *		TODO
	 * @param ins
	 * @return
	 *
	 */
	public List<VacInsurance> findListVacInsurance(VacInsurance ins);


	/**
	 * 根据条件搜索接种医院
	 * @author fuxin
	 * @date 2017年5月19日 上午11:01:17
	 * @description 
	 *		TODO
	 * @param dept
	 * @return
	 *
	 */
	public List<SysDept> findDeptList(SysDept dept);


	/**
	 * 获取已经完成的接种记录
	 * @author fuxin
	 * @date 2017年5月24日 下午5:00:13
	 * @description 
	 *		TODO
	 * @param id
	 * @return
	 *
	 */
	public List<String> getFinishNumAble(String id);


	public SimpleModel getLastPin(String group, String localCode);


	/**
	 * 根据id获取医院名称
	 * @author fuxin
	 * @date 2017年5月31日 上午10:01:28
	 * @description 
	 *		TODO
	 * @param id
	 * @return
	 *
	 */
	public String getHostipal(String id);


	/**
	 * 查询儿童信息
	 * @author fuxin
	 * @date 2017年8月29日 上午12:24:32
	 * @description 
	 *		TODO
	 * @param id
	 * @return
	 *
	 */
	public BsChildBaseInfo getBsChildBaseInfoById(String id);


	/**
	 * 根据儿童编号获取接种提醒
	 * @author fuxin
	 * @date 2017年8月29日 下午9:28:10
	 * @description 
	 *		TODO
	 * @param childcode
	 * @return
	 *
	 */
	public List<VacChildRemind> findVacChildRemindList(String childcode);


	/**
	 * 提醒消息列表
	 * @author zhouqj
	 * @date 2017年10月30日 下午2:42:36
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<VacChildRemind> findJobRemind();

	public List<VacChildRemind> findAppRemind();

	/**
	 * 删除过时提醒
	 * @author zhouqj
	 * @date 2017年10月30日 下午4:42:02
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public int clearJobRemind();

    /**
     * 根据手机号查询关注列表
     * */
	public List<BsChildBaseInfo> getBsChildBaseInfoByPhone(String phone);



	/**
	 * 查询社区列表
	 * @author fuxin
	 * @date 2017年3月24日 下午4:40:36
	 * @description
	 *		TODO
	 * @return
	 *
	 */
	public List<SimpleModel> getCommunity(String code);

	public List<Response> getByChildcode(Map<String,String> map);


	public int insertResponse(Response response);

	public String getByModelId( String vaccineid);

	/**
	 * 获取儿童接种记录
	 * @author wangnan
	 * @date 2018年2月27日 下午3:17:22
	 * @description 
	 *		TODO
	 * @param childcode
	 * @param type
	 * @return
	 *
	 */
	public List<HashMap<String,String>>  getvaccRecordFinish(@Param(value="childcode")String childcode);
	
	/**
	 * 获取不良反应表中的回复时间
	 * @author wangnan
	 * @date 2018年2月28日 下午3:57:22
	 * @param childcode
	 * nid：计划id
	 * 暂时未用
	 * @return
	 *
	 */
	public String getAFEI( @Param(value="childcode")String childcode,@Param(value="NID")String NID);
	
	/**
	 * 获取不良反应回复详情
	 * @author wangnan
	 * @date 2018年3月01日 下午3:17:22
	 * @param childcode
	 * @param nid
	 * @return
	 *
	 */
	public List<HashMap<String,String>>  getResponseDetail( @Param(value="childcode")String childcode,@Param(value="nid")String nid);
	
	/**
	 * 获取最新版本信息
	 * @author wangnan
	 * @date 2018年3月01日 下午5:17:22
	sys_app_version
	 * @return
	 *
	 */
	public HashMap<String,String>  getAPPVersion();
	
	
}
