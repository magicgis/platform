package com.junl.wpwx.task;

import com.junl.wpwx.service.jpush.JpushMessageService;
import com.junl.wpwx.service.jpush.JpushMessageServiceImpl;
import org.apache.log4j.Logger;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * 定时
 */

public class TShowScheduleTimer extends TimerTask {
    @Resource
    private JpushMessageService jpushMessageService;
    private static final Logger logger = Logger.getLogger(TShowScheduleTimer.class);

    @Override
    public void run() {
        delayTshow();
    }

    public void delayTshow() {
        logger.info("");
        //业务逻辑处理
        String obdId = "160a3797c80100e1125";//registrationID
        String message = "后台推送测试";//消息内容
        List<String> obdIds = new ArrayList<String>();
        obdIds.add(obdId);
        //Boolean bo= jpushMessageService.sendToRegistrationId(obdIds,"","","");


    }

    public static void main(String[] args){
        //业务逻辑处理
        String obdId = "160a3797c80100e1125";//registrationID
        String message = "后台推送测试后台推送测试后台试";//消息内容
        List<String> obdIds = new ArrayList<String>();
        obdIds.add(obdId);
        //obdIds.add("190e35f7e040f4ad418");
        JpushMessageServiceImpl Jpu=new JpushMessageServiceImpl();
        //Jpu.sendToRegistrationId(obdIds,"测试",message,"");
    }
}