package com.junl.wpwx.mapper;

import com.junl.wpwx.model.Notification;
import com.junl.wpwx.model.PromotMessage;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NotificationMapper extends CrudMapper<Notification> {


    //通过儿童编号查询通知列表
    List<Notification> findByChildcode(String childcode);

    List<PromotMessage> findByMessage();

    PromotMessage findById(  String id);

    int insertNotification(Notification notification);
    
    /**
     * 消息中心-系统消息
     * * edit by wangnan
     * 2018-3-1
     */
	public List<HashMap<String, String>>  findMessage();
	
	/**
     * 消息中心-系统消息-详情
     * * edit by wangnan
     * 2018-3-1
     */
	public String getMessDetail(@Param(value="id")String id);
	
}
