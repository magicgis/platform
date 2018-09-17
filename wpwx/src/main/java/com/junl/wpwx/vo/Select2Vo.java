package com.junl.wpwx.vo;

import java.io.Serializable;

public class Select2Vo implements Serializable {
	private static final long serialVersionUID = 1L;

	String id;
	String text;
	
	public Select2Vo() {}

	public Select2Vo(String id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
