package com.junl.frame.tools.render;

/**
 * 
 * @author chenmaolong
 * @date 2016年3月29日 下午3:22:39
 * @description 
 *		响应json信息
 */
public class JSONMessage  {
	private boolean success;
    private String msg="";
    private Object data;
    public JSONMessage(boolean success,String msg) {
        this.msg = msg;
        this.success = success;
    }
    public JSONMessage( boolean success,Object data) {
        this.data = data;
        this.success = success;
    }
    
    //同时返回msg
    public JSONMessage( boolean success,String msg,Object data) {
        this.data = data;
        this.success = success;
        this.msg = msg;
    }
    
    public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

}
