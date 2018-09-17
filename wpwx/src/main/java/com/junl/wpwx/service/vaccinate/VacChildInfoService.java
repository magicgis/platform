package com.junl.wpwx.service.vaccinate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

import java.util.List;

import com.junl.wpwx.model.ChildAppinfo;
import com.junl.wpwx.model.SimpleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junl.wpwx.mapper.VacChildInfoMapper;
import com.junl.wpwx.model.VacChildInfo;
import com.junl.wpwx.model.VacChildRemind;

/**
 * 微信关注宝宝表Service
 * @author fuxin
 * @version 2017-03-02
 */
@Service
@Transactional(readOnly = true)
public class VacChildInfoService {
	
	@Autowired()
	VacChildInfoMapper mapper;
	
	public VacChildInfo get(String id) {
		return mapper.get(id);
	}

	public void save(VacChildInfo info){
		mapper.insert(info);
	}

	public List<VacChildInfo> findList(VacChildInfo info) {
		return mapper.findList(info);
		
	}

	/**
	 * 真删
	 * @author fuxin
	 * @date 2017年5月18日 下午4:20:52
	 * @description 
	 *		TODO
	 * @param vacChildInfo
	 *
	 */
	public void delete(VacChildInfo vacChildInfo) {
		mapper.delete(vacChildInfo.getId());
		
	}

	/**
	 * 提示签字预约信息列表
	 * @author zhouqj
	 * @date 2017年10月28日 上午11:45:00
	 * @description 
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	public List<VacChildRemind> findVacChildRemindList(VacChildInfo temp) {
		//查询提示预约信息
/*		List<VacChildRemind> list = mapper.findVacChildRemindList(temp);
		List<VacChildRemind> templist = new ArrayList<>();
		//拆分大类code
		for(VacChildRemind vcr : list){
			List<String> nlist = new ArrayList<String>();
			if(vcr.getRemindGroup()!=null&& !"".equals(vcr.getRemindGroup())){
					nlist = Arrays.asList(vcr.getRemindGroup().split(","));
					for(String nid : nlist){
						VacChildRemind vr = new VacChildRemind();
						vr.setId(vcr.getId());
						vr.setLocalcode(vcr.getLocalcode());
						vr.setVaccId(nid);
						vr = findVacChildRemind(vr);
						vr.setVaccId(nid);
						templist.add(vr);
					}
            }else {
                templist.add(vcr);
            }
		}
		return templist;*/
		return mapper.findVacChildRemindList(temp);
	}

	/**
	 * 提示签字预约信息
	 * @author zhouqj
	 * @date 2017年10月30日 上午9:02:22
	 * @description 
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	public VacChildRemind findVacChildRemind(VacChildRemind temp) {
		return mapper.findVacChildRemind(temp);
	}

	/**
	 * 查询同类预约记录信息
	 * @author zhouqj
	 * @date 2017年10月30日 上午10:39:21
	 * @description 
	 *		TODO
	 * @param vr
	 * @return
	 *
	 */
/*
	public List<VacChildRemind> findVacRemindSignList(VacChildRemind vcr) {
		List<VacChildRemind> templist = new ArrayList<>();
		if(vcr.getRemindGroup()!=null&&!"".equals(vcr.getRemindGroup())){
            List<String> nlist = Arrays.asList(vcr.getRemindGroup().split(","));
            for(String nid : nlist){
                VacChildRemind vr = new VacChildRemind();
                vr.setId(vcr.getId());
                vr.setLocalcode(vcr.getLocalcode());
                vr.setVaccId(nid);
                vr = findVacChildRemind(vr);
                templist.add(vr);
            }
        }else {
            templist.add(vcr);
        }
		return templist;
	}
*/

	/**
	 * 新增签字
	 * @author zhouqj
	 * @date 2017年10月30日 上午10:59:01
	 * @description 
	 *		TODO
	 * @param vr
	 *
	 */
	public void insertSignature(VacChildRemind vr) {
		mapper.insertSignature(vr);
	}

	/**
	 * 更改记录签字状态
	 * @author zhouqj
	 * @date 2017年10月30日 上午11:00:31
	 * @description 
	 *		TODO
	 * @param vr
	 *
	 */
	public void updateSign(VacChildRemind vr) {
		mapper.updateSign(vr);
	}

	/**
	 * 查询签字记录
	 * @author zhouqj
	 * @date 2017年10月30日 上午11:17:17
	 * @description 
	 *		TODO
	 * @param temp
	 * @return
	 *
	 */
	public VacChildRemind findVacChildRemindById(VacChildRemind temp) {
		return mapper.findVacChildRemindById(temp);
	}


	//通过id查询儿童预约记录
	public List<VacChildRemind> findRemindById(VacChildRemind temp) {
		return mapper.findRemindById(temp);
	}
	//查询接种时间段 的人数
	public List<SimpleModel> findByTime(VacChildRemind temp) {
		return mapper.findByTime(temp);
	}
	//查询接种人数
	public int findByNum(VacChildRemind temp) {
		return mapper.findByNum(temp);
	}


	//修改预约时间段
	public int updateByTime(VacChildRemind entity){
		return mapper.updateByTime(entity);
	}

	public List<VacChildRemind> findChildAppList(ChildAppinfo temp) {
		/*//查询提示预约信息
		List<VacChildRemind> list = mapper.findChildAppList(temp); //findVacChildRemindList(temp);
		List<VacChildRemind> templist = new ArrayList<>();
		//拆分大类code
		for(VacChildRemind vcr : list){
			List<String> nlist = new ArrayList<String>();
			if(vcr.getRemindGroup()!=null&& !"".equals(vcr.getRemindGroup())){
				nlist = Arrays.asList(vcr.getRemindGroup().split(","));
				for(String nid : nlist){
					VacChildRemind vr = new VacChildRemind();
					vr.setId(vcr.getId());
					vr.setLocalcode(vcr.getLocalcode());
					vr.setVaccId(nid);
					vr = findVacChildRemind(vr);
					vr.setVaccId(nid);
					templist.add(vr);
				}
			}else {
				templist.add(vcr);
			}
		}
		return templist;*/
		return mapper.findChildAppList(temp);
	}


	public List<ChildAppinfo> findAppUserList(VacChildInfo info) {
		return mapper.findAppUserList(info);

	}

	public void insertAppUser(VacChildInfo info){
		mapper.insertAppUser(info);
	}


	public int updatePayStatus(VacChildRemind entity){
		return mapper.updatePayStatus(entity);
	}

	
	//通过id查询儿童预约记录
	/*
	 * edit by wangnan 
	 * 2018-2-24
	 * 预约时间+接种单位
	 */
	public HashMap<String, String> findRemindInfoById(String id) {
		return mapper.findRemindInfoById(id);
	}
	
	
}