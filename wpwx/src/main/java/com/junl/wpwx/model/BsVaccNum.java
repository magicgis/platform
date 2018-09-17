package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;

@Alias("BsVaccNum")
public class BsVaccNum extends BaseEntity<BsVaccNum>{
	private static final long serialVersionUID = 1L;
	
	public static final String TYPE_ORDER = "ORDER";
	public static final String TYPE_ORDER_FREE = "ORDER_FREE";
	public static final String TYPE_RECORD = "RECORD";
	
	/** 计划状态-未到月龄 */
	public static final int STUT_NOTENOUGH = 1;
	/** 计划状态-预约成功 */
	public static final int STUT_RESERVE = 2;
	/** 计划状态-可预约 */
	public static final int STUT_ABLE = 3;
	
	private String weight; // 权重
	private String vaccineid; // 疫苗id
	private Long mouage; // 月龄
	private String code; // 疫苗代码
	private String name; // 疫苗名称
	private Long pin; // 针次
	private String status; // 状态
	private Long type; // 疫苗类型
	private Long pintime; // 距离上次针次的时间 间隔 单位（月 ）
	private Long lasttime; // 最晚 接种月龄
	private Integer excep; // 特殊疫苗，出生可选，只打一次
	private Long pentrep; // 是否五联替代
	private Long price; // 疫苗价格
	private String group; // 大类CODE
	private String clazz; // 小类CODE
	private String groupname; // 育苗大类
	private String allname; // 疫苗名称
	
	private int stock; // 库存
	private int leng;//记录树形数据中的长度
	private int stut = STUT_NOTENOUGH;//计划状态 1.未到月龄 2.预约成功 3.可预约
	
	private String orderby;

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getVaccineid() {
		return vaccineid;
	}

	public void setVaccineid(String vaccineid) {
		this.vaccineid = vaccineid;
	}

	public Long getMouage() {
		return mouage;
	}

	public void setMouage(Long mouage) {
		this.mouage = mouage;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPin() {
		return pin;
	}

	public void setPin(Long pin) {
		this.pin = pin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getPintime() {
		return pintime;
	}

	public void setPintime(Long pintime) {
		this.pintime = pintime;
	}

	public Long getLasttime() {
		return lasttime;
	}

	public void setLasttime(Long lasttime) {
		this.lasttime = lasttime;
	}

	public Integer getExcep() {
		return excep;
	}

	public void setExcep(Integer excep) {
		this.excep = excep;
	}

	public Long getPentrep() {
		return pentrep;
	}

	public void setPentrep(Long pentrep) {
		this.pentrep = pentrep;
	}

	public Long getPrice() {
		if(this.price == null){
			return 0l;
		}
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getAllname() {
		return allname;
	}

	public void setAllname(String allname) {
		this.allname = allname;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public int getLeng() {
		return leng;
	}

	public void setLeng(int leng) {
		this.leng = leng;
	}

	public int getStut() {
		return stut;
	}

	public void setStut(int stut) {
		this.stut = stut;
	}
	
	
	

}
