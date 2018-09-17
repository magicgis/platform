package com.junl.wpwx.form;

import com.junl.frame.tools.string.StringUtils;
import com.junl.wpwx.model.VacOrder;

public class OrderForm {

	String nid;  //计划表id
	String pid;	 //产品表id
	String childcode;	//儿童编号
	String insurance;	//保险费
	
	int price;

	
	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getChildcode() {
		return childcode;
	}

	public void setChildcode(String childcode) {
		this.childcode = childcode;
	}

	public String getInsurance() {
		return insurance;
	}
	
	/**
	 * 判断是否购买保险
	 * @author fuxin
	 * @date 2017年4月8日 上午11:28:12
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public String isInsurance() {
		if(StringUtils.isNotEmpty(insurance)){
			return VacOrder.INSURANCE_YES;
		}
		return VacOrder.INSURANCE_NO;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	
	
	
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * 数据验证 <br> true-->无异常  false-->数据异常
	 * @author fuxin
	 * @date 2017年3月30日 下午2:51:26
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public boolean valid() {
		if (StringUtils.isEmpty(childcode) || StringUtils.isEmpty(nid)
				|| StringUtils.isEmpty(pid)) {
			return false;
		} else {
			return true;
		}
	}
	

}
