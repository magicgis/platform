package com.junl.wpwx.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;


@Alias("BsProduct")
public class BsProduct implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String vaccineid; // 疫苗id
	private String batchno; // 批次编号
	private Long dosage; // 每盒剂次
	private String manufacturer; // 疫苗的制造厂商
	private String code; // 疫苗的制造厂商
	private String isforeign; // 是否进口 Y国产 N进口
	private Long storenum; // 库存
	private double sellprice; // 出售价格
	private String isshow; // 是否显示 Y显示 N不显示
	private String name; // 疫苗名称
	
	private String vaccName;		// 疫苗小类名称
	private Date vaccExpDate;		// 有效期
	private String vacsiteno;    //疫苗站点编号
	private String codeall;		// 疫苗英文名称
	
	public String nid;
	private int num;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVaccineid() {
		return vaccineid;
	}

	public void setVaccineid(String vaccineid) {
		this.vaccineid = vaccineid;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public Long getDosage() {
		return dosage;
	}

	public void setDosage(Long dosage) {
		this.dosage = dosage;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getIsforeign() {
		return isforeign;
	}

	public void setIsforeign(String isforeign) {
		this.isforeign = isforeign;
	}

	public Long getStorenum() {
		return storenum;
	}

	public void setStorenum(Long storenum) {
		this.storenum = storenum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 已完成*100操作
	 * @author fuxin
	 * @date 2017年3月30日 下午2:36:43
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public int getSellprice() {
		return (int) (sellprice*100);
	}
	
	/**
	 * 获取价格不*100
	 * @author fuxin
	 * @date 2017年4月12日 上午9:36:21
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public double getSellpriceNormal() {
		return sellprice;
	}

	public void setSellprice(double sellprice) {
		this.sellprice = sellprice;
	}

	public String getIsshow() {
		return isshow;
	}

	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getVaccName() {
		return vaccName;
	}

	public void setVaccName(String vaccName) {
		this.vaccName = vaccName;
	}

	public Date getVaccExpDate() {
		return vaccExpDate;
	}

	public void setVaccExpDate(Date vaccExpDate) {
		this.vaccExpDate = vaccExpDate;
	}

	public String getVacsiteno() {
		return vacsiteno;
	}

	public void setVacsiteno(String vacsiteno) {
		this.vacsiteno = vacsiteno;
	}

	public String getCodeall() {
		return codeall;
	}

	public void setCodeall(String codeall) {
		this.codeall = codeall;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	
	
	

}
