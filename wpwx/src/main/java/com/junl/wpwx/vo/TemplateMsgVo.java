package com.junl.wpwx.vo;


/**
 * 模板消息
 * 
 * @author fuxin
 * @date 2017年3月17日 上午11:14:20
 * @description TODO
 */
public class TemplateMsgVo {

	/** 订单成功发送模板消息 */
	public static final String TEMP_ORDER_FINISH = "WSHDFWF6bOiQ-p_RtgjNB1Tfjymfar2p-b1h9WuWyMU";
	/** 接种台完成 */
	public static final String TEMP_QUENE_FINISH = "kGjKCYasuj1Lp9Mdr4oiiOegeUeQN5GverGtBuQEl8k";
	/** 自助建完成 */
	public static final String TEMP_SELFHELP_REG = "tUQLQD9-6RaHCYwAraD0i9yXz_sD1tcfXW1ntT_5cNU";
	/** 自助建完成-犬伤 */
	public static final String TEMP_SELFHELP_REG_DOG = "3ivbTK_elRJ-UB-Plq1-L1qUY1rU5v53PddbBU70HYY";
	
	// OPENID
	private String touser;
	// 模板编号
	private String template_id;
	// url
	private String url;
	// 数据
	private Object data;
	
	public TemplateMsgVo() {
		super();
	}
	
	public TemplateMsgVo(String touser, String template_id, String url) {
		super();
		this.touser = touser;
		this.template_id = template_id;
		this.url = url;
	}



	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
