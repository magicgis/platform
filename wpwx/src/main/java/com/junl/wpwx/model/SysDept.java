package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;

import com.junl.wpwx.vo.Select2Vo;

/**
 * 预防门诊编码Entity
 * @author fuxin
 * @version 2017-04-08
 */
@Alias("SysDept")
public class SysDept extends BaseEntity<SysDept> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 门诊编码
	private String name;		// 门诊简称
	private String allName;		// 门诊全称
	private String codeLevel;		// code级别
	private String fCode;		// 父节点code
	private String nCode;		// 新code
	
	public SysDept() {
		super();
	}

	public SysDept(String id){
		super(id);
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
	
	public String getAllName() {
		return allName;
	}

	public void setAllName(String allName) {
		this.allName = allName;
	}
	
	public String getCodeLevel() {
		return codeLevel;
	}

	public void setCodeLevel(String codeLevel) {
		this.codeLevel = codeLevel;
	}
	
	public String getFCode() {
		return fCode;
	}

	public void setFCode(String fCode) {
		this.fCode = fCode;
	}
	
	public String getNCode() {
		return nCode;
	}

	public void setNCode(String nCode) {
		this.nCode = nCode;
	}
	
	/**
	 * 包装成select2Vo
	 * @author fuxin
	 * @date 2017年5月19日 上午11:06:47
	 * @description 
	 *		TODO
	 * @return
	 *
	 */
	public Select2Vo getSelect2Vo(){
		Select2Vo vo = new Select2Vo();
		vo.setId(code);
		vo.setText(name);
		return vo;
	}
	
}