package com.junl.wpwx.service.jpush;

import java.util.List;

public interface JpushMessageService {

    public boolean senPushByRegesterId(List<String> regeSterIds, String msgContent);

    public boolean sendPushAll(String title);
    /**
     * 推送给设备标识参数的用户
     * @param regeSterIds 设备标识
     * @param msg_title 消息内容标题
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public  boolean sendToRegistrationId( List<String> regeSterIds,String msg_title, String msg_content, String extrasparam,String type) ;

}
