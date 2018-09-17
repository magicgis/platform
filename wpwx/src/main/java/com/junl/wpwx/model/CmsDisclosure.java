package com.junl.wpwx.model;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 风险告知书
 * 
 * @author fuxin
 * @date 2017年4月1日 下午2:35:42
 * @description TODO
 */
@Alias("CmsDisclosure")
public class CmsDisclosure implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String vid;
	private String context;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

}
