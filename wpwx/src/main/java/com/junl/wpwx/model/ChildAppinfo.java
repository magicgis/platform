package com.junl.wpwx.model;

import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("ChildAppinfo")
public class ChildAppinfo extends BaseEntity<ChildAppinfo> {

    private String childcode;	//儿童编码
    private String childname;	//儿童姓名
    private Date birthday;	//儿童生日
    private String guardianname;	//监护人姓名
    private String guardianrelation;	//儿童与监护人的关系
    private String phone;	//关联人手机号
    private String userid;	//app用户表的ID
/*    private Date createDate;	//创建时间
    private Date updateDate;	//更新时间
    private String remarks;	//备注信息*/

    public String getChildcode() {
        return childcode;
    }

    public void setChildcode(String childcode) {
        this.childcode = childcode;
    }

    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGuardianname() {
        return guardianname;
    }

    public void setGuardianname(String guardianname) {
        this.guardianname = guardianname;
    }

    public String getGuardianrelation() {
        return guardianrelation;
    }

    public void setGuardianrelation(String guardianrelation) {
        this.guardianrelation = guardianrelation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public static ChildAppinfo parse(BsChildBaseInfo bsInfo) {
        if(bsInfo == null){
            return null;
        }
        ChildAppinfo info = new ChildAppinfo();
        info.setChildcode(bsInfo.getChildcode());
        info.setChildname(bsInfo.getChildname());
        info.setBirthday(bsInfo.getBirthday());
        info.setGuardianname(bsInfo.getGuardianname());
        info.setGuardianrelation(bsInfo.getGuardianrelation());
        info.setPhone(bsInfo.getGuardianmobile());
        info.setCreateDate(new Date());
        return info;
    }
}
