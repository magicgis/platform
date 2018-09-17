package com.junl.wpwx.service.vaccinate;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.junl.wpwx.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junl.frame.tools.Ehcache;
import com.junl.frame.tools.date.DateUtils;
import com.junl.frame.tools.net.WebUtils;
import com.junl.wpwx.common.ConfigProperty;
import com.junl.wpwx.form.AttenForm;
import com.junl.wpwx.mapper.BsMapper;

/**
 * 微信关注宝宝表Service
 * @author fuxin
 * @version 2017-03-02
 */
@Service
@Transactional(readOnly = true)
public class VacService {

	@Autowired
	BsMapper bsMapper;
	@Autowired
	VacOrderService orderService;
	@Autowired
	ConfigProperty conf;
	@Resource
	private Ehcache cache;
	
	
	/**
	 * 计算儿童应该接种何种疫苗
	 * @author fuxin
	 * @date 2017年3月6日 下午2:03:11
	 * @description 
	 *		TODO
	 * @param code
	 * @return
	 *
	 */
	public Object getVaccList(String code, String type) throws RuntimeException{
		if(!StringUtils.isNotBlank(code)){
			return new ArrayList<>();
		}
		
		//查询儿童月龄
		BsChildBaseInfo info = bsMapper.getBsChildBaseInfoByCode(code);
			if(info == null){
				throw new RuntimeException("儿童信息不存在");
			}
			
			//获取儿童生日时期
			Date birth = info.getBirthday();
			Calendar calInfo = GregorianCalendar.getInstance();
			calInfo.setTime(birth);
			int y1 = calInfo.get(Calendar.YEAR);
			int m1 = calInfo.get(Calendar.MONTH) +1;
			int d1 = calInfo.get(Calendar.DATE);
			
			//获取系统时间
			calInfo.setTime(new Date());
			int y2 = calInfo.get(Calendar.YEAR);
			int m2 = calInfo.get(Calendar.MONTH) +1;
			int d2 = calInfo.get(Calendar.DATE);
			
			int age =(y2-y1)*12 + m2 - m1;
			if(d2 < d1){
				age --;
			}
		
			
		//获取儿童已有记录的计划id
		String vacccease = "";
		String vaccceaseOrder = "";
		List<String> nums = bsMapper.getFinishNum(info.getId());
		for(String num : nums ){
			vacccease += "'" + num + "'" + ",";
		}
		if(vacccease.endsWith(",")){
			vacccease = vacccease.substring(0, vacccease.length() -1);
		}
		
		//获取已完成订单的计划id
		/*VacOrder o = new VacOrder();
		o.setUserid((String) WebUtils.getSession().getAttribute("wpwx.userid"));
		o.setChildcode(code);
		o.setStatus(VacOrder.STATUS_FINISH);
		List<VacOrder> orders = orderService.findList(o);
		//预约成功对于nid
		for(VacOrder vo : orders){
			if(vo.getNumid().contains("_")){
				vaccceaseOrder += "'" + vo.getNumid().replace("_", "','") + "',";
			}else{
				vaccceaseOrder += "'" + vo.getNumid() + "',";
			}
		}*/
		//去掉最后的,
		if(vaccceaseOrder.endsWith(",")){
			vaccceaseOrder = vaccceaseOrder.substring(0, vaccceaseOrder.length() -1);
		}
		
		
		//使用模型计算需要接种何种疫苗
		Map<String, String> parm = new HashMap<String, String>();
		parm.put("monage", age+"");
		parm.put("localCode", info.getLocalCode());
		
		List<BsVaccNum> list = null;
		
		//判断是获取预约接种还是未接种记录
		if(BsVaccNum.TYPE_ORDER.equals(type)){
			//计算预约疫苗种类
			if(!"".equals(vacccease) && !"".equals(vaccceaseOrder)){
				vacccease += ",";
			}
			parm.put("vacccease", vacccease + vaccceaseOrder);
			list = bsMapper.getVaccList(parm);
//			for(BsVaccNum n : list){
//				if(n.getStock() > 0){
//					return n;
//				}
//			}
//			return null;
			List<BsVaccNum> lm = new ArrayList<>();
			list = bsMapper.getVaccList(parm);
			for(BsVaccNum n : list){
				if(n.getStock() > 10){
					lm.add(n);
				}
			}
			lm = distinctByName(lm);
			return lm;
		}else if(BsVaccNum.TYPE_ORDER_FREE.equals(type)){
			//计算预约疫苗种类 -所有免费疫苗
			if(!"".equals(vacccease) && !"".equals(vaccceaseOrder)){
				vacccease += ",";
			}
			parm.put("vacccease", vacccease + vaccceaseOrder);
			parm.put("isfree", "1");
			list = bsMapper.getVaccList(parm);
//			for(BsVaccNum n : list){
//				if(n.getStock() > 0){
//					return n;
//				}
//			}
//			return null;
			List<BsVaccNum> lm = new ArrayList<>();
			list = bsMapper.getVaccList(parm);
			for(BsVaccNum n : list){
				if(n.getStock() > 10){
					lm.add(n);
				}
			}
			lm = distinctByName(lm);
			return lm;
		}else{
			//获取儿童已接种的计划id
			nums = bsMapper.getFinishNumAble(info.getId());
			vacccease = "";
			for(String num : nums ){
				vacccease += "'" + num + "'" + ",";
			}
			if(vacccease.endsWith(",")){
				vacccease = vacccease.substring(0, vacccease.length() -1);
			}
			//查询未接种记录
			parm.put("vacccease", vacccease);
			list = updateStatu(bsMapper.getUnfinishedRecord(parm), vaccceaseOrder, age);
			
			//数组转树形
			String op = "first";
			List<BsVaccNum> templist = new ArrayList<>();
			List<List<BsVaccNum>> returnlist = new ArrayList<>();
			
			for(BsVaccNum l : list){
				if(op.equals("first")){
					op = l.getName();
				}
				if(!op.equals(l.getName())){
					templist.get(0).setLeng(templist.size()+1);
					returnlist.add(templist);
					templist = new ArrayList<>();
					op = l.getName();
				}
				templist.add(l);
			}
			if(templist.size() > 0){
				templist.get(0).setLeng(templist.size()+1);
				returnlist.add(templist);
			}
			//数组转树形 结束
			return returnlist;
		}
		
	}


	/**
	 * BsVaccNum 去重
	 * @author fuxin
	 * @date 2017年4月20日 下午3:32:31
	 * @description 
	 *		TODO
	 * @param lm
	 * @return
	 *
	 */
	private List<BsVaccNum> distinctByName(List<BsVaccNum> lm) {
		List<BsVaccNum> llm = new ArrayList<>();
		if(lm.size() > 0){
			String first = "_";
			for(BsVaccNum n : lm){
				if(first.indexOf("_" + n.getName() + "_") < 0){
					llm.add(n);
					first = first + n.getName() + "_";
				}
			}
		}
		return llm;
	}


	/**
	 * 更新计划状态
	 * @author fuxin
	 * @date 2017年3月30日 上午11:43:58
	 * @description 
	 *		TODO
	 * @param unfinishedRecord
	 * @param orderString
	 * @param age 
	 * @return
	 *
	 */
	private List<BsVaccNum> updateStatu(List<BsVaccNum> unfinishedRecord, String orderString, int age) {
		if(null == unfinishedRecord ){
			return null;
		}
		for(BsVaccNum n : unfinishedRecord){
			if(age > n.getMouage()){
				n.setStut(BsVaccNum.STUT_ABLE);
			}
			if(("," + orderString + ",").indexOf(",'" + n.getId() + "',") > -1){
				n.setStut(BsVaccNum.STUT_RESERVE);
			}
		}
		return unfinishedRecord;
	}


	/**
	 * 获取产品信息
	 * @author fuxin
	 * @date 2017年3月11日 上午11:16:59
	 * @description 
	 *		TODO
	 * @param pId
	 * @return
	 *
	 */
	public BsProduct getBsProduct(String pId) {
		try {
			Object obj = cache.getCache(Ehcache.KEY_PRODUCT_ID + pId, "areaCache");
			BsProduct bsp = new BsProduct();
			if(null == obj){
				bsp = bsMapper.getBsProduct(pId);
				cache.putCache(Ehcache.KEY_PRODUCT_ID + pId, bsp, "areaCache");
			}else{
				bsp = (BsProduct) obj;
			}
			return bsp;
		} catch (Exception e) {
			return new BsProduct();
		}
	}
	
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
	@SuppressWarnings("unchecked")
	public List<Area> findAreaByPid(String pid){
		try {
			Object obj = cache.getCache(Ehcache.KEY_AREA_PID + pid, "areaCache");
			List<Area> list = new ArrayList<>();
			if(null == obj){
				list = bsMapper.findAreaByPid(pid);
				cache.putCache(Ehcache.KEY_AREA_PID + pid, list, "areaCache");
			}else{
				list = (List<Area>) obj;
			}
			return list;
		} catch (Exception e) {
			return new ArrayList<>();
		}
		
	}
	
	
	public Area findAreaByid(String id){
		try {
			Object obj = cache.getCache(Ehcache.KEY_AREA_ID + id, "areaCache");
			Area area = new Area();
			if(null == obj){
				area = bsMapper.findAreaById(id);
				cache.putCache(Ehcache.KEY_AREA_ID + id, area, "areaCache");
			}else{
				area = (Area) obj;
			}
			return area;
		} catch (Exception e) {
			return new Area();
		}
		
	}


	/**
	 * 更新区域信息
	 * @author fuxin
	 * @date 2017年3月13日 下午2:41:47
	 * @description 
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	public VacChildTemp updateAddr(VacChildTemp temp) {
		if(temp == null){
			return null;
		}
		//更新区域信息
		StringBuilder homeaddress = new StringBuilder();
		if(StringUtils.isNotBlank(temp.getProvince())){
			homeaddress.append(findAreaByid(temp.getProvince()).getName());
		}
		if(StringUtils.isNotBlank(temp.getCity())){
			homeaddress.append(findAreaByid(temp.getCity()).getName());
		}
		if(StringUtils.isNotBlank(temp.getCounty())){
			homeaddress.append(findAreaByid(temp.getCounty()).getName());
		}
		homeaddress.append(temp.getAddress());
		temp.setHomeaddress(homeaddress.toString());
		
		//更新户籍地址
		StringBuilder regAddress = new StringBuilder();
		if(StringUtils.isNotBlank(temp.getPr())){
			regAddress.append(findAreaByid(temp.getPr()).getName());
		}
		if(StringUtils.isNotBlank(temp.getCi())){
			regAddress.append(findAreaByid(temp.getCi()).getName());
		}
		if(StringUtils.isNotBlank(temp.getCo())){
			regAddress.append(findAreaByid(temp.getCo()).getName());
		}
		regAddress.append(temp.getAdd());
		temp.setRegistryaddress(regAddress.toString());
		return temp;
	}
	
	/**
	 * 更新区域信息
	 * @author fuxin
	 * @date 2017年3月13日 下午2:41:47
	 * @description 
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	public BsChildBaseInfo updateAddr(BsChildBaseInfo temp) {
		if(temp == null){
			return null;
		}
		//更新区域信息
		StringBuilder homeaddress = new StringBuilder();
		if(StringUtils.isNotBlank(temp.getProvince())){
			homeaddress.append(findAreaByid(temp.getProvince()).getName());
		}
		if(StringUtils.isNotBlank(temp.getCity())){
			homeaddress.append(findAreaByid(temp.getCity()).getName());
		}
		if(StringUtils.isNotBlank(temp.getCounty())){
			homeaddress.append(findAreaByid(temp.getCounty()).getName());
		}
		if(StringUtils.isNotBlank(temp.getAddress())){
			homeaddress.append(temp.getAddress());
		}
		temp.setHomeaddress(homeaddress.toString());
		
		//更新户籍地址
		StringBuilder regAddress = new StringBuilder();
		if(StringUtils.isNotBlank(temp.getPr())){
			regAddress.append(findAreaByid(temp.getPr()).getName());
		}
		if(StringUtils.isNotBlank(temp.getCi())){
			regAddress.append(findAreaByid(temp.getCi()).getName());
		}
		if(StringUtils.isNotBlank(temp.getCo())){
			regAddress.append(findAreaByid(temp.getCo()).getName());
		}
		if(StringUtils.isNotBlank(temp.getAddress())){
			regAddress.append(temp.getAddress());
		}
		temp.setRegistryaddress(regAddress.toString());
		return temp;
	}

	
	/**
	 * 根据儿童编号查询是否有儿童信息
	 * @author fuxin
	 * @date 2017年3月13日 下午5:59:50
	 * @description 
	 *		TODO
	 * @param childcode
	 * @return
	 *
	 */
	public BsChildBaseInfo getBsChildBaseInfoByCode(String childcode) {
		return bsMapper.getBsChildBaseInfoByCode(childcode);
	}


	/**
	 * 查询儿童信息
	 * @author fuxin
	 * @date 2017年8月29日 上午12:23:54
	 * @description 
	 *		TODO
	 * @param childcode
	 * @return
	 *
	 */
	public BsChildBaseInfo getBsChildBaseInfoById(String id) {
		return bsMapper.getBsChildBaseInfoById(id);
	}


	/**
	 * 查询儿童接种记录
	 * @author fuxin
	 * @date 2017年3月15日 上午11:05:47
	 * @description 
	 *		TODO
	 * @param bsRecord
	 * @return
	 *
	 */
	public List<BsRecord> findRecordList(BsRecord bsRecord) {
		return bsMapper.findRecordList(bsRecord);
	}
	
	public void insertBsRecord(BsRecord record){
		bsMapper.insertRecord(record);
	}

	/**
	 * 根据生日，姓名查询手机号码
	 * @author fuxin
	 * @date 2017年3月21日 下午4:33:16
	 * @description 
	 *		TODO
	 * @param birth
	 * @param name
	 * @return
	 *
	 */
	public List<String> choosephone(Date birth, String name) {
		Map<String, String> param = new HashMap<>();
		param.put("name", name);
		param.put("birth", DateUtils.dateParseShortString(birth));
		List<SimpleModel> simples = bsMapper.choosephone(param);
		List<String> phones = new ArrayList<>();
		for(SimpleModel sm : simples){
			phones.add(sm.getStr1());
			if(StringUtils.isNotBlank(sm.getStr2())){
				phones.add(sm.getStr2());
			}
		}
		return phones;
	}


	/**
	 * 根据手机号码，姓名查询儿童记录
	 * @author fuxin
	 * @date 2017年3月22日 下午8:14:34
	 * @description 
	 *		TODO
	 * @param info
	 * @return
	 *
	 */
	public BsChildBaseInfo chooseBaseInfo(AttenForm info) {
		return bsMapper.chooseBaseInfo(info);
	}
	
	/**
	 * 获取儿童接种记录
	 * @author fuxin
	 * @date 2017年3月22日 下午8:17:22
	 * @description 
	 *		TODO
	 * @param childcode
	 * @param type
	 * @return
	 *
	 */
	public List<BsRecord> getRecordFinish(String childcode){
		return bsMapper.getRecordFinish(childcode);
	}


	/**
	 * 根据id和站点编码查询计划信息
	 * @author fuxin
	 * @date 2017年5月24日 下午2:18:40
	 * @description 
	 *		TODO
	 * @param openid
	 * @param localCode
	 * @return
	 *
	 */
	public BsVaccNum getNumByid(String nid, String localCode) {
		return bsMapper.getNumByid(nid, localCode);
	}
	
	/**
	 * 查询儿童月龄
	 * @author fuxin
	 * @date 2017年3月23日 下午4:40:59
	 * @description 
	 *		TODO
	 * @param code
	 * @return
	 *
	 */
	public int getMonAge(String code) {
		// 查询儿童月龄
		BsChildBaseInfo info = bsMapper.getBsChildBaseInfoByCode(code);
		if (info == null) {
			throw new RuntimeException("儿童信息不存在");
		}

		// 获取儿童生日时期
		Date birth = info.getBirthday();
		Calendar calInfo = GregorianCalendar.getInstance();
		calInfo.setTime(birth);
		int y1 = calInfo.get(Calendar.YEAR);
		int m1 = calInfo.get(Calendar.MONTH) + 1;
		int d1 = calInfo.get(Calendar.DATE);

		// 获取系统时间
		calInfo.setTime(new Date());
		int y2 = calInfo.get(Calendar.YEAR);
		int m2 = calInfo.get(Calendar.MONTH) + 1;
		int d2 = calInfo.get(Calendar.DATE);

		int age = (y2 - y1) * 12 + m2 - m1;
		if (d2 < d1) {
			age--;
		}
		return age;
	}
	
	/**
	 * 获取民族信息
	 * @author fuxin
	 * @date 2017年3月24日 下午2:32:56
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<BsNation> getNationList(){
		try {
			Object obj = cache.getCache(Ehcache.KEY_NATION_ALL, "areaCache");
			List<BsNation> list = new ArrayList<>();
			if(null == obj){
				list = bsMapper.getNationList();
				cache.putCache(Ehcache.KEY_NATION_ALL, list ,"areaCache");
			}else{
				list = (List<BsNation>) obj;
			}
			return list;
		} catch (Exception e) {
			return new ArrayList<>();
		}
		
	}


	/**
	 * 获取医院信息
	 * @author fuxin
	 * @date 2017年3月24日 下午4:17:41
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<SimpleModel> getHostipallist() {
		try {
			Object obj = cache.getCache(Ehcache.KEY_HOSTIPAL_ALL, "areaCache");
			List<SimpleModel> list = new ArrayList<>();
			if(null == obj){
				list = bsMapper.getHostipallist();
				cache.putCache(Ehcache.KEY_HOSTIPAL_ALL, list ,"areaCache");
			}else{
				list = (List<SimpleModel>) obj;
			}
			return list;
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}


	/**
	 * 获取社区信息
	 * @author fuxin
	 * @date 2017年3月24日 下午4:40:02
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<SimpleModel> getCommunitylist( ) {
		try {
			Object obj = cache.getCache(Ehcache.KEY_COMMUNITYLIST_ALL, "areaCache");
			List<SimpleModel> list = new ArrayList<>();
			if(null == obj){
				list = bsMapper.getCommunitylist();
				cache.putCache(Ehcache.KEY_COMMUNITYLIST_ALL, list ,"areaCache");
			}else{
				list = (List<SimpleModel>) obj;
			}
			return list;
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}


	/**
	 * 根据疫苗大类获取疫苗产品
	 * @author fuxin
	 * @date 2017年3月27日 下午3:51:11
	 * @description 
	 *		TODO
	 * @param group
	 * @return
	 *
	 */
	@Deprecated
	public List<BsProduct> getProductListByGroup(String group) {
		return bsMapper.getProductListByGroup(group);
	}


	/**
	 * 根据小类id获取风险告知书
	 * @author fuxin
	 * @date 2017年4月1日 下午2:48:54
	 * @description 
	 *		TODO
	 * @param vaccineid
	 * @return
	 *
	 */
	public CmsDisclosure getCmsDisclosureByVacid(String vaccineid) {
		try {
			Object obj = cache.getCache(Ehcache.KEY_DISCLOSURE_VACID + vaccineid, "areaCache");
			CmsDisclosure bsp = new CmsDisclosure();
			if(null == obj){
				bsp = bsMapper.getCmsDisclosureByVacid(vaccineid);
				cache.putCache(Ehcache.KEY_DISCLOSURE_VACID + vaccineid, bsp, "areaCache");
			}else{
				bsp = (CmsDisclosure) obj;
			}
			return bsp;
		} catch (Exception e) {
			return new CmsDisclosure();
		}
	}

	@Deprecated
	public List<BsOffice> getOfficelist() {
		return bsMapper.getOfficelist();
	}


	/**
	 * 获取接种单位列表
	 * @author fuxin
	 * @date 2017年4月10日 上午9:38:47
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<SysDept> getDeptlist() {
		return bsMapper.getDeptlist();
	}
	
	/**
	 * 获取接种单位列表
	 * @author fuxin
	 * @date 2017年4月10日 上午9:38:47
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public SysDept getDeptlistByCode(String code) {
		return bsMapper.getDeptlistByCode(code);
	}


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
	@SuppressWarnings("unchecked")
	public List<SimpleModel> getDictList(String type) {
		try {
			Object obj = cache.getCache(Ehcache.KEY_DICT + type, "areaCache");
			List<SimpleModel> list = new ArrayList<>();
			if(null == obj){
				list = bsMapper.getDictList(type);
				cache.putCache(Ehcache.KEY_DICT + type, list ,"areaCache");
			}else{
				list = (List<SimpleModel>) obj;
			}
			return list;
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}
	
	/**
	 * 获取字典标签
	 * @author fuxin
	 * @date 2017年4月18日 下午7:26:23
	 * @description 
	 *		TODO
	 * @param string
	 * @return
	 *
	 */
	public String getDictLabel(String value, String type, String defValue) {
		Map<String, String> map = new HashMap<>();
		map.put("value", value);
		map.put("type", type);
		String returnValue = bsMapper.getDictLabel(map);
		if(StringUtils.isNotBlank(returnValue)){
			return returnValue;
		}
		return defValue;
	}


	/**
	 * 通过产品大类获取有库存不为空的最贵的产品
	 * @author fuxin
	 * @date 2017年4月20日 上午11:32:40
	 * @description 
	 *		TODO
	 * @param group
	 * @param code 
	 * @return
	 *
	 */
	public BsProduct getProductByGroupExp(String group, String code) {
		Map<String , String> map = new HashMap<>();
		map.put("group", group);
		map.put("code", code);
		return bsMapper.getProductByGroupExp(map);
	}


	/**
	 * 查询儿童档案列表
	 * @author fuxin
	 * @date 2017年4月22日 下午12:08:16
	 * @description 
	 *		TODO
	 * @param info
	 * @return
	 *
	 */
	public List<BsChildBaseInfo> getBsChildBaseInfoList(BsChildBaseInfo info) {
		return bsMapper.getBsChildBaseInfoList(info);
	}


	/**
	 * 根据身份证查询三天内是建档成功
	 * @author fuxin
	 * @date 2017年4月22日 下午1:19:28
	 * @description 
	 *		TODO
	 * @param card
	 * @return
	 *
	 */
	public List<BsRabies> findRabiesList(VacRabiesTemp temp) {
		return bsMapper.findRabiesList(temp);
	}
	
	/**
	 * 获取提示列表
	 * @author fuxin
	 * @date 2017年4月24日 下午5:41:11
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<VacRemind> findVacRemindList(){
		return bsMapper.findVacRemindList();
	}


	public List<BsRabies> getByTempid(String tempid) {
		return bsMapper.getByTempid(tempid);
	}
	
	/**
	 * 根据pid判断产品是否可用(存在并库存>0)
	 * @author fuxin
	 * @date 2017年4月26日 下午6:54:23
	 * @description 
	 *		TODO
	 * @param pid
	 * @return
	 *
	 */
	public boolean hasProduct(String pid){
		BsProduct product = getBsProduct(pid);
		if(null != product && product.getStorenum() != 0){
			return true;
		}
		return false;
	}


	/**
	 * 根据大类查询风险告知书
	 * @author fuxin
	 * @date 2017年4月27日 上午11:44:26
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public CmsDisclosure getCmsDisclosureByGroupId(String group) {
		return bsMapper.getCmsDisclosureByGroupId(group);
	}
	
	
	/**
	 * 插入保险信息
	 * @author fuxin
	 * @date 2017年4月27日 下午8:36:21
	 * @description 
	 *		TODO
	 * @param vins
	 *
	 */
	public void insertIns(VacInsurance vins) {
		bsMapper.insertIns(vins);
		
	}


	/**
	 * 更新保险信息
	 * @author fuxin
	 * @date 2017年4月27日 下午8:37:16
	 * @description 
	 *		TODO
	 * @param vins
	 *
	 */
	public void updateIns(VacInsurance vins) {
		bsMapper.updateIns(vins);
		
	}


	/**
	 * 检查订单是否被锁定，[登记台出票后会生成一条未完成订单，此时订单会被锁定]
	 * @author fuxin
	 * @date 2017年5月17日 上午11:19:01
	 * @description 
	 *		TODO
	 * @param temprec
	 * @return
	 *
	 */
	public boolean checkRefundOrder(BsRecord temprec) {
		List<BsRecord> list = bsMapper.checkRefundOrder(temprec);
		return (null != list && list.size() > 0) ? true : false;
	}


	/**
	 * 查询保险列表
	 * @author fuxin
	 * @date 2017年5月17日 下午3:04:19
	 * @description 
	 *		TODO
	 * @param ins
	 * @return
	 *
	 */
	public List<VacInsurance> findListVacInsurance(VacInsurance ins) {
		return bsMapper.findListVacInsurance(ins);
	}


	/**
	 *
	 * @author fuxin
	 * @date 2017年5月19日 上午10:59:00
	 * @description
	 *		TODO
	 * @param dept
	 * @return
	 *
	 */
	public List<SysDept> findDeptList(SysDept dept) {
		return bsMapper.findDeptList(dept);
	}


	public SimpleModel getLastPin(String group, String localCode) {
		return bsMapper.getLastPin(group, localCode);
	}


	/**
	 * 根据id获取医院名称
	 * @author fuxin
	 * @date 2017年5月31日 上午10:00:40
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public String getHostipal(String id) {
		return bsMapper.getHostipal(id);
	}


	/**
	 * 更新详细页面区域划分、户口类型、居住类型、出生医院信息
	 * @author fuxin
	 * @date 2017年5月31日 下午5:27:01
	 * @description 
	 *		TODO
	 * @param baseinfo
	 * @return
	 *
	 */
	public BsChildBaseInfo updateDetail(BsChildBaseInfo baseinfo) {
		//区域划分
		if(StringUtils.isNotBlank(baseinfo.getArea())){
			 List<SimpleModel> areas = getCommunity(baseinfo.getLocalCode());
			 for(SimpleModel sm : areas){
				 if(baseinfo.getArea().equals(sm.getStr1())){
					 baseinfo.setArea(sm.getStr2());
					 break;
				 }
			 }
		}
		//户口类型
		if(StringUtils.isNotBlank(baseinfo.getProperties())){
			 List<SimpleModel> properties = getDictList("properties");
			 for(SimpleModel dic : properties){
				 if(baseinfo.getProperties().equals(dic.getStr2())){
					 baseinfo.setProperties(dic.getStr1());
					 break;
				 }
			 }
		}
		//居住类型
		if(StringUtils.isNotBlank(baseinfo.getReside())){
			 List<SimpleModel> resides = getDictList("reside");
			 for(SimpleModel dic : resides){
				 if(baseinfo.getReside().equals(dic.getStr2())){
					 baseinfo.setReside(dic.getStr1());
					 break;
				 }
			 }
		}
		//出生医院
		if(StringUtils.isNotBlank(baseinfo.getBirthhostipal())){
			List<SimpleModel> birthhostipals = getHostipallist();
			for(SimpleModel dic : birthhostipals){
				if(baseinfo.getBirthhostipal().equals(dic.getStr1())){
					baseinfo.setBirthhostipal(dic.getStr2());
					break;
				}
			}
		}
		//名族
		if(StringUtils.isNotBlank(baseinfo.getNation())){
			List<SimpleModel> nations = getDictList("nation");
			for(SimpleModel dic : nations){
				if(baseinfo.getNation().equals(dic.getStr2())){
					baseinfo.setNation(dic.getStr1());
					break;
				}
			}
		}
		return baseinfo;
	}


	/**
	 * 更新详细页面区域划分、户口类型、居住类型、出生医院信息
	 * @author fuxin
	 * @date 2017年5月31日 下午5:27:01
	 * @description 
	 *		TODO
	 * @param baseinfo
	 * @return
	 *
	 */
	public VacChildTemp updateDetail(VacChildTemp baseinfo) {
		//区域划分
				if(StringUtils.isNotBlank(baseinfo.getArea())){
					 List<SimpleModel> areas = getCommunity(baseinfo.getOfficeinfo());
					 for(SimpleModel sm : areas){
						 if(baseinfo.getArea().equals(sm.getStr1())){
							 baseinfo.setArea(sm.getStr2());
							 break;
						 }
					 }
				}
				//户口类型
				if(StringUtils.isNotBlank(baseinfo.getProperties())){
					 List<SimpleModel> properties = getDictList("properties");
					 for(SimpleModel dic : properties){
						 if(baseinfo.getProperties().equals(dic.getStr2())){
							 baseinfo.setProperties(dic.getStr1());
							 break;
						 }
					 }
				}
				//居住类型
				if(StringUtils.isNotBlank(baseinfo.getReside())){
					 List<SimpleModel> resides = getDictList("reside");
					 for(SimpleModel dic : resides){
						 if(baseinfo.getReside().equals(dic.getStr2())){
							 baseinfo.setReside(dic.getStr1());
							 break;
						 }
					 }
				}
				//出生医院
				if(StringUtils.isNotBlank(baseinfo.getBirthhostipal())){
					List<SimpleModel> birthhostipals = getHostipallist();
					for(SimpleModel dic : birthhostipals){
						if(baseinfo.getBirthhostipal().equals(dic.getStr1())){
							baseinfo.setBirthhostipal(dic.getStr2());
							break;
						}
					}
				}
				//名族
				if(StringUtils.isNotBlank(baseinfo.getNation())){
					List<SimpleModel> nations = getDictList("nation");
					for(SimpleModel dic : nations){
						if(baseinfo.getNation().equals(dic.getStr2())){
							baseinfo.setNation(dic.getStr1());
							break;
						}
					}
				}
				return baseinfo;
	}

	/**
	 * 更新详细页面区域划分、户口类型、居住类型、出生医院信息
	 * @author fuxin
	 * @date 2017年5月31日 下午5:27:01
	 * @description
	 *		TODO
	 * @param baseinfo
	 * @return
	 *
	 */
	public ChildTemp updateChildTemp(ChildTemp baseinfo) {
		//户口类型
		if(StringUtils.isNotBlank(baseinfo.getRegisteredTypeNo())){
			List<SimpleModel> properties = getDictList("properties");
			for(SimpleModel dic : properties){
				if(baseinfo.getRegisteredTypeNo().equals(dic.getStr2())){
					baseinfo.setRegisteredTypeNo(dic.getStr1());
					break;
				}
			}
		}
		//居住类型
		if(StringUtils.isNotBlank(baseinfo.getLiveId())){
			List<SimpleModel> resides = getDictList("reside");
			for(SimpleModel dic : resides){
				if(baseinfo.getLiveId().equals(dic.getStr2())){
					baseinfo.setLiveId(dic.getStr1());
					break;
				}
			}
		}
		//出生医院
		if(StringUtils.isNotBlank(baseinfo.getHospitalId())){
			List<SimpleModel> birthhostipals = getHostipallist();
			for(SimpleModel dic : birthhostipals){
				if(baseinfo.getHospitalId().equals(dic.getStr1())){
					baseinfo.setHospitalId(dic.getStr2());
					break;
				}
			}
		}
		//民族
		if(StringUtils.isNotBlank(baseinfo.getChildNationNo())){
			List<SimpleModel> nations = getDictList("nation");
			for(SimpleModel dic : nations){
				if(baseinfo.getChildNationNo().equals(dic.getStr2())){
					baseinfo.setChildNationNo(dic.getStr1());
					break;
				}
			}
		}
		return baseinfo;
	}
	/**
	 * 根据儿童编号获取接种提醒
	 * @author fuxin
	 * @date 2017年8月29日 下午9:27:18
	 * @description 
	 *		TODO
	 * @param childcode
	 * @return
	 *
	 */
	public List<VacChildRemind> findVacChildRemindList(String childcode) {
		return bsMapper.findVacChildRemindList(childcode);
	}


	/**
	 * 提醒消息列表
	 * @author zhouqj
	 * @date 2017年10月30日 下午2:41:53
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public List<VacChildRemind> findJobRemind() {
		return bsMapper.findJobRemind();
	}

	public List<VacChildRemind> findAppRemind() {
		return bsMapper.findAppRemind();
	}

	/**
	 * 删除过时提醒
	 * @author zhouqj
	 * @date 2017年10月30日 下午4:41:28
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public int clearJobRemind() {
		return bsMapper.clearJobRemind();
	}

	/**
	 * 根据手机号查询关注列表
	 * */
	public List<BsChildBaseInfo> getBsChildBaseInfoByPhone(String phone){
		return bsMapper.getBsChildBaseInfoByPhone(phone);
	}

	/**
	 * 根据接种门诊获取社区信息
	 * @author
	 * @date 2018年01月07日
	 * @description
	 *		TODO
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<SimpleModel> getCommunity( String code) {
		try {
			Object obj = cache.getCache(code, "areaCache");
			List<SimpleModel> list = new ArrayList<>();
			if(null == obj){
				list = bsMapper.getCommunity(code);
				cache.putCache(code, list ,"areaCache");
			}else{
				list = (List<SimpleModel>) obj;
			}
			/*List<SimpleModel> list = new ArrayList<>();
			list = bsMapper.getCommunity(code);*/
			return list;
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<Response> getByChildcode(Map<String,String> map){
		return bsMapper.getByChildcode(map);
	}

	public int insertResponse(Response response){
		return bsMapper.insertResponse(response);
	}

	public String getByModelId(String vaccineid){
		return bsMapper.getByModelId(vaccineid);
	}


	/**
	 * 获取儿童接种记录
	 * @author wangnan
	 * @date 2018年2月27日 下午3:17:22
	 * @param childcode
	 * @param type
	 * @return
	 *
	 */
	public List<HashMap<String,String>>  getvaccRecordFinish(String childcode){
		return bsMapper.getvaccRecordFinish(childcode);
	}
	
	/**
	 * 获取不良反应表中的回复时间
	 * @author wangnan
	 * @date 2018年2月28日 下午3:57:22
	 * @param childcode
	 * nid：计划id
	 * @return
	 *
	 */
	public String getAFEI( String childcode,String NID){
		return bsMapper.getAFEI(childcode,NID);
	}
	
	/**
	 * 获取不良反应回复详情
	 * @author wangnan
	 * @date 2018年3月01日 下午3:17:22
	 * @param childcode
	 * @param nid
	 * @return
	 *
	 */
	public List<HashMap<String,String>>  getResponseDetail(String childcode,String nid){
		return bsMapper.getResponseDetail(childcode,nid);
	}
	
	/**
	 * 获取最新版本信息
	 * @author wangnan
	 * @date 2018年3月01日 下午5:17:22
	sys_app_version
	 * @return
	 *
	 */
	public HashMap<String,String>  getAPPVersion(){
		return bsMapper.getAPPVersion();
	}
	
	
}