package com.junl.wpwx.service.jpush;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.junl.wpwx.utils.JPushUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jpushMessageService")
public class JpushMessageServiceImpl implements JpushMessageService {

    private final static String appKey = "e970628ca38727c5d37fd3ee";
    private final static String masterSecret = "d41ce2dd240b75ba40692c51";
    /**
     * 保存离线的时长。秒为单位。最多支持10天（864000秒）。 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
     * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒)。
     */
    private static long timeToLive = 60 * 60 * 24;
    private static JPushClient jPushClient = new JPushClient(masterSecret,appKey);
    private static final Logger logger = Logger.getLogger(JpushMessageServiceImpl.class);

    public boolean sendPushAll(String title) {
       // jPushClient = new JPushClient(masterSecret, appKey );
        boolean flag = false;
        try {
            //String title = "推送测试";
            PushPayload payload = JPushUtil.buildPushObject_all_all_alert(title);
            System.out.println("服务器返回数据：" + payload.toString());

            PushResult result = jPushClient.sendPush(payload);
            if (null != result) {
                logger.info("Get resul ---" + result);
                flag = true;
            }
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            flag = false;
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            flag = false;
        }
        return flag;

    }

    public boolean senPushByRegesterId(List<String> regeSterIds, String msgContent) {
        jPushClient = new JPushClient(masterSecret, appKey);
        boolean flag = false;
//      String content = "多个ID测试";
        try {
            PushPayload payload = JPushUtil.buildPushObject_all_all_regesterIds(regeSterIds, msgContent);
            System.out.println("服务器返回数据：" + payload.toString());
            PushResult result = jPushClient.sendPush(payload);
            if (null != result) {
                logger.info("Get result ----" + result);
                flag = true;
            }
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            flag = false;
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            flag = false;
        }

        return flag;
    }

    public  boolean sendToRegistrationId( List<String> regeSterIds,String msg_title, String msg_content, String extrasparam,String type) {

        String[] strs=extrasparam.split(",");
        boolean flag = false;
        try {
            PushPayload pushPayload= JPushUtil.registrationId(regeSterIds,msg_title,msg_content,strs,type);
            PushResult pushResult=jPushClient.sendPush(pushPayload);
            if (null != pushResult) {
                logger.info("Get result ----" + pushResult);
                flag = true;
            }
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            flag = false;
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            flag = false;
        }
        return flag;
    }




}
