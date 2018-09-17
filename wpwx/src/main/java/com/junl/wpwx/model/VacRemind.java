package com.junl.wpwx.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("VacRemind")
public class VacRemind extends BaseEntity<VacRemind> {
	private static final long serialVersionUID = 1L;

	public static final String TEMP_SIGN = "1";
	public static final String TEMP_STYPE = "2";

	private String openid;	//用户openid
	private String ctxusername;	//用户姓名
	private String rtype;	//提示类型 1犬伤 2成人
	private Date ctxdate;	//程序接种时间
	private Date createdate;	//创建时间
	private String ctxvaccname;	//疫苗名称
	private String rstatus;		//提示状态
	private String code;		//小类code
	private String office;		//接种单位编号
	private String sign;		//签字状态   默认0

	private byte[] signature;    //签字内容
	private String stype;	//签字来源  2微信
	private String vid;	//记录id
	private String sid;	//签字id

	private String ncode;		//用户编号

	private int leng = 0; // 接种记录接口使用长度参数

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getCtxusername() {
		return ctxusername;
	}

	public void setCtxusername(String ctxusername) {
		this.ctxusername = ctxusername;
	}

	public String getRtype() {
		return rtype;
	}

	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	public Date getCtxdate() {
		return ctxdate;
	}

	public void setCtxdate(Date ctxdate) {
		this.ctxdate = ctxdate;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getCtxvaccname() {
		return ctxvaccname;
	}

	public void setCtxvaccname(String ctxvaccname) {
		this.ctxvaccname = ctxvaccname;
	}

	public String getRstatus() {
		return rstatus;
	}

	public void setRstatus(String rstatus) {
		this.rstatus = rstatus;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public int getLeng() {
		return leng;
	}

	public void setLeng(int leng) {
		this.leng = leng;
	}

	public String getNcode() {
		return ncode;
	}

	public void setNcode(String ncode) {
		this.ncode = ncode;
	}

}
