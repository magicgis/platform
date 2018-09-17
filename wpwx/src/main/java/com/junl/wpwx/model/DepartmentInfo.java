package com.junl.wpwx.model;



import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.Alias;

import java.util.Date;
import java.util.Map;

@Alias("DepartmentInfo")
public class DepartmentInfo {

    private String id;
    private String localcode; //接种单位编号
    private String localname; //接种单位名称
    private String jobstime; //接种单位工作时间
    private String phonenumber; //接种单位联系电话
    private String remarks; //备注
    private String address; //接种单位详细地址


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalcode() {
        return localcode;
    }

    public void setLocalcode(String localcode) {
        this.localcode = localcode;
    }

    public String getLocalname() {
        return localname;
    }

    public void setLocalname(String localname) {
        this.localname = localname;
    }

    public String getJobstime() {
        return jobstime;
    }

    public void setJobstime(String jobstime) {
        this.jobstime = jobstime;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
