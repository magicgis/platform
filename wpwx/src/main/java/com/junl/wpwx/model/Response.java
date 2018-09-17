package com.junl.wpwx.model;


import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("Response")
public class Response  {

    private String id;  // BS_CHILD_Response
    private String childcode;  //儿童编号
    private String nid;  //计划id
    private Date submittime;  //提交时间
    private String submitmage;  //提交图片
    private String submittext;  //提交文字
    private Date responsetime;  //回复时间
    private String replycontent="";  //回复内容

    private String vaccinId; //已接种记录id


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChildcode() {
        return childcode;
    }

    public void setChildcode(String childcode) {
        this.childcode = childcode;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public Date getSubmittime() {
        return submittime;
    }

    public void setSubmittime(Date submittime) {
        this.submittime = submittime;
    }

    public String getSubmitmage() {
        return submitmage;
    }

    public void setSubmitmage(String submitmage) {
        this.submitmage = submitmage;
    }

    public String getSubmittext() {
        return submittext;
    }

    public void setSubmittext(String submittext) {
        this.submittext = submittext;
    }

    public Date getResponsetime() {
        return responsetime;
    }

    public void setResponsetime(Date responsetime) {
        this.responsetime = responsetime;
    }

    public String getReplycontent() {
        return replycontent;
    }

    public void setReplycontent(String replycontent) {
        this.replycontent = replycontent;
    }

    public String getVaccinId() {
        return vaccinId;
    }

    public void setVaccinId(String vaccinId) {
        this.vaccinId = vaccinId;
    }
}
