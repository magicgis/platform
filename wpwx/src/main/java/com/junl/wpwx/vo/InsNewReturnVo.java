package com.junl.wpwx.vo;

import java.io.Serializable;

public class InsNewReturnVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String policy_no;

	private String third_serial_no;

	private String prem;

	private String message;

	private String result;

	public String getPolicy_no() {
		return this.policy_no;
	}

	public void setPolicy_no(String policy_no) {
		this.policy_no = policy_no;
	}

	public String getThird_serial_no() {
		return this.third_serial_no;
	}

	public void setThird_serial_no(String third_serial_no) {
		this.third_serial_no = third_serial_no;
	}

	public String getPrem() {
		return this.prem;
	}

	public void setPrem(String prem) {
		this.prem = prem;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
