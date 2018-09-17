package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias("SimpleModel")
public class SimpleModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String str1;	//label
	private String str2="";	//value
	private int int1;
	private int int2;

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}

	public int getInt1() {
		return int1;
	}

	public void setInt1(int int1) {
		this.int1 = int1;
	}

	public int getInt2() {
		return int2;
	}

	public void setInt2(int int2) {
		this.int2 = int2;
	}

}
