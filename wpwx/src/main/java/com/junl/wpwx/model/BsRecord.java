package com.junl.wpwx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("BsRecord")
public class BsRecord extends BaseEntity<BsRecord> {
	private static final long serialVersionUID = 1L;

	/** 记录状态-未接种 */
	public static final String STATUS_WAIT = "0";
	/** 记录状态-已接种 */
	public static final String STATUS_FINISH = "1";
	
	private String childid;
	private String vaccineid;
	private String nid;
	private String status;
	private String source;
	private String pid;
	private String orderby;
	private String allname;  //大类名称
	private String vaccname; //小类名称 
	private String dosage; //剂次
	private Date vaccinatedate; //接种时间
	
	private int leng; //树形结构记录共有几条数据
	
	//记录儿童编号，用于vo适配器传值
	private String childcode;

	private String replyStatus ; //回复状态 0：没提交 1：已提交 2已回复

	public BsRecord() {
		super();
	}

	public BsRecord(String childid, String vaccineid, String nid,
			String status, String pid) {
		super();
		this.childid = childid;
		this.vaccineid = vaccineid;
		this.nid = nid;
		this.status = status;
		this.pid = pid;
	}


	public String getChildid() {
		return childid;
	}

	public void setChildid(String childid) {
		this.childid = childid;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getVaccineid() {
		return vaccineid;
	}

	public void setVaccineid(String vaccineid) {
		this.vaccineid = vaccineid;
	}

	public String getChildcode() {
		return childcode;
	}

	public void setChildcode(String childcode) {
		this.childcode = childcode;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getAllname() {
		return allname;
	}

	public void setAllname(String allname) {
		this.allname = allname;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	@JsonFormat(pattern="yyyy-MM-dd ",locale = "zh",timezone="GMT+8")
	public Date getVaccinatedate() {
		return vaccinatedate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	public Date getVaccinatedateLong() {
		return vaccinatedate;
	}

	public void setVaccinatedate(Date vaccinatedate) {
		this.vaccinatedate = vaccinatedate;
	}

	public String getVaccname() {
		return vaccname;
	}

	public void setVaccname(String vaccname) {
		this.vaccname = vaccname;
	}

	public int getLeng() {
		return leng;
	}

	public void setLeng(int leng) {
		this.leng = leng;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}


	public String getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(String replyStatus) {
		this.replyStatus = replyStatus;
	}
}
