package com.junl.wpwx.vo;

import com.junl.wpwx.model.BsProduct;
import com.junl.wpwx.model.VacOrder;

public class ReserveVo {

	private String id;			//产品id(pid)
	private String batchno;		//产品批号
	private String manufacturer;	//产品生产厂家
	private double sellprice;	//产品价格
	private String name;			//产品名称
	private String context;		//告知书内容
	private String nid;			//计划id

	private String insurance;	//是否购买保险
	private String paystatus;	//是否已付款

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public double getSellprice() {
		return sellprice;
	}

	public void setSellprice(double sellprice) {
		this.sellprice = sellprice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getContext() {
		if(context == null){
			return "";
		}
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * 适配器
	 * @author fuxin
	 * @date 2017年3月15日 上午10:50:22
	 * @description 
	 *		TODO
	 * @param o
	 * @return
	 *
	 */
	public static ReserveVo parseVo(VacOrder o){
		ReserveVo vo = new ReserveVo();
		 vo.setId(o.getProductid());
		 vo.setNid(o.getNumid());
		 vo.setPaystatus(o.getStatus()+"");
		 vo.setInsurance(o.getInsurance());
		return vo;
	}

	
	/**
	 * 添加产品信息
	 * @author fuxin
	 * @date 2017年3月31日 下午6:50:32
	 * @description 
	 *		TODO
	 * @param bsProduct
	 *
	 */
	public void addProductInfo(BsProduct bsProduct) {
		if(bsProduct != null){
			this.sellprice = bsProduct.getSellpriceNormal();
			this.name = bsProduct.getVaccName();
			this.batchno = bsProduct.getBatchno();
			this.manufacturer = bsProduct.getManufacturer();
		}
	}
	


}
