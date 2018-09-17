package com.junl.wpwx.vo;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.junl.wpwx.model.BsProduct;
import com.junl.wpwx.model.BsVaccNum;
import com.junl.wpwx.model.SimpleModel;
import com.junl.wpwx.model.VacOrder;


public class VaccGroupVo {

	private String name; // 大类名称
	private double sellprice; // 产品价格
	private String pid; // 产品id(pid or pid_pid)
	private String nid; // 计划id
	
	private String context;		//告知书内容
	private String insurance;	//是否购买保险
	private String paystatus;	//是否已付款
	private int num;			//针数
	private String type;		//数据类型
	
	private String pin;			//剂次
	private String pathema;		//预防疾病类型
	private String numType = "一类";		//标识一类还是二类

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSellprice() {
		return sellprice;
	}

	public void setSellprice(double sellprice) {
		this.sellprice = sellprice;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}
	
	public String getPathema() {
		return pathema;
	}

	public void setPathema(String pathema) {
		this.pathema = pathema;
	}

	public String getNumType() {
		return numType;
	}

	public void setNumType(String numType) {
		this.numType = numType;
	}
	
	

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * 将计划信息转换为vo
	 * @author fuxin
	 * @date 2017年4月20日 下午1:51:43
	 * @description 
	 *		TODO
	 * @param n
	 * @return
	 *
	 */
	public static VaccGroupVo parseVo(BsVaccNum n){
		VaccGroupVo vo = new VaccGroupVo();
		if(null != n){
			vo.setName(n.getName());
			vo.setNid(n.getId());
			vo.setInsurance(VacOrder.INSURANCE_NO);
			vo.setPaystatus(VacOrder.STATUS_WAIT+"");
			vo.setNum(1);
		}
		return vo;
	}
	
	/**
	 * 向vo中添加产品信息
	 * @author fuxin
	 * @date 2017年4月20日 下午1:52:08
	 * @description 
	 *		TODO
	 * @param p
	 * @return
	 *
	 */
	public VaccGroupVo addProduct(BsProduct p){
		if(null != p){
			this.pid = p.getId();
			this.sellprice = p.getSellprice();
		}
		return this;
	}

	/**
	 * 将两个一类苗进行拼装
	 * @author fuxin
	 * @date 2017年4月20日 下午1:52:22
	 * @description 
	 *		TODO
	 * @param vo
	 * @param vot
	 * @return
	 *
	 */
	public static VaccGroupVo mix(VaccGroupVo v1, VaccGroupVo v2) {
		VaccGroupVo vo = new VaccGroupVo();
		vo.setContext(v1.getContext() + "\r\n" + v2.getContext());
		if(VacOrder.INSURANCE_YES.equals(v1.getInsurance()) || VacOrder.INSURANCE_YES.equals(v2.getInsurance())){
			vo.setInsurance(VacOrder.INSURANCE_YES);
		}else{
			vo.setInsurance(VacOrder.INSURANCE_NO);
		}
		vo.setName(v1.getName() + "+" + v2.getName());
		vo.setNid(v1.getNid() + "_" + v2.getNid());
		vo.setPid(v1.getPid() + "_" + v2.getPid());
		vo.setNum(v1.getNum() + v2.getNum());
		vo.setPaystatus(v1.getPaystatus());
		vo.setSellprice(v1.getSellprice() + v2.getSellprice());
		return vo;
	}
	

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public static VaccGroupVo parseVo(VacOrder r) {
		VaccGroupVo vo = null;
		if(r != null){
			vo = new VaccGroupVo();
			vo.setInsurance(r.getInsurance());
			vo.setName(r.getProductname().replace("+", "_"));
			vo.setNid(r.getNumid());
			vo.setNum(r.getProductid().split("_").length);
			vo.setPaystatus(VacOrder.STATUS_FINISH+"");
			vo.setPid(r.getProductid());
			BigDecimal bd = new BigDecimal(0.01);
			if(VacOrder.INSURANCE_YES.equals(r.getInsurance())){
				vo.setSellprice(bd.multiply(new BigDecimal(r.getPayprice()-200*vo.getNum())).doubleValue());
			}else{
				vo.setSellprice(bd.multiply(new BigDecimal(r.getPayprice())).doubleValue());
			}
			vo.setType("4");
			
		}
		return vo;
	}
	
	public void addVaccInfo(SimpleModel vac, String pinNum) {
		if(StringUtils.isNoneBlank(this.pin)){
			this.pin += "_第"+ pinNum + "针/共" + vac.getStr2() + "针";
		}else{
			this.pin = "第"+ pinNum + "针/共" + vac.getStr2() + "针";
		}
		if(StringUtils.isNoneBlank(this.pathema)){
			this.pathema += "_" + vac.getStr1();
		}else{
			this.pathema = vac.getStr1();
		}
	}

}
