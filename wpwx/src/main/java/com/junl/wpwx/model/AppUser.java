package com.junl.wpwx.model;


import org.apache.ibatis.type.Alias;

@Alias("AppUser")
public class AppUser extends BaseEntity<AppUser> {

    private String nickname;	//昵称
   // private byte[] headpath;	//头像
    private String phone;	//绑定手机号
    private String registrationid;	//激光推送用户标识
    private String uid;	//第三方用户标识
    private String userpwd;	//用户密码
    private String realname;	//姓名
    private String headurl;	//头像url
    private String type;	//


/*    private Date createDate;	//创建时间
    private Date updateDate;	//更新时间
    private String remarks;	//备注信息*/

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /*public byte[] getHeadpath() {
        return headpath;
    }

    public void setHeadpath(byte[] headpath) {
        this.headpath = headpath;
    }*/

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegistrationid() {
        return registrationid;
    }

    public void setRegistrationid(String registrationid) {
        this.registrationid = registrationid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
