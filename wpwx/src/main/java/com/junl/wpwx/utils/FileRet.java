package com.junl.wpwx.utils;
/**
 * Auto-generated: 2018-02-03 22:8:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class FileRet {

    private int code;
    private String message;
    private String request_id;
    private DataRet data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }
    public String getRequest_id() {
        return request_id;
    }

    public DataRet getData() {
        return data;
    }

    public void setData(DataRet data) {
        this.data = data;
    }
}