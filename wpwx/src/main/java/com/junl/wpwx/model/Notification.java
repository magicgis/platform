package com.junl.wpwx.model;



import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.Alias;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Alias("Notification")
public class Notification /*extends BaseEntity<Notification>*/ {

    private String id;
    private String childcode;  //儿童编号 JOBSTIME PHONENUMBER
    private String notifyname;  //通知名称
    private Date  notifytime;  //通知时间
    private String notifycontent;  //通知内容  date = (List<String>) args.get("date");
    private String type;  //通知类型
    private Map<String,String> content ;  //内容
   private String status;  //状态1已发送2：未发送
    private String recipient;  //消息接受者
    private String createBy;  //创建人 DepartmentInfo
    private String updateBy;  //修改人*/
    private Date createDate;  //创建时间
    private Date updateDate;  //修改时间

    private String childid;  //儿童ID


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

    public String getNotifyname() {
        return notifyname;
    }

    public void setNotifyname(String notifyname) {
        this.notifyname = notifyname;
    }

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    public Date getNotifytime() {
        return notifytime;
    }

    public void setNotifytime(Date notifytime) {
        this.notifytime = notifytime;
    }

    public String getNotifycontent() {
        return notifycontent;
    }

    public void setNotifycontent(String notifycontent) {
        this.notifycontent = notifycontent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getChildid() {
        return childid;
    }

    public void setChildid(String childid) {
        this.childid = childid;
    }

    public Notification(String childcode, String notifyname, Date notifytime, String notifycontent, String type) {
        this.childcode = childcode;
        this.notifyname = notifyname;
        this.notifytime = notifytime;
        this.notifycontent = notifycontent;
        this.type = type;
    }
    public Notification() {
        super();
    }

}
