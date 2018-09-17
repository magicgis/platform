package com.junl.wpwx.service;

import com.junl.frame.tools.Ehcache;
import com.junl.wpwx.mapper.SysHolidayMapper;
import com.junl.wpwx.model.BsNation;
import com.junl.wpwx.model.SysHoliday;
import org.apache.http.impl.cookie.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class SysHolidayService {

    @Autowired
    SysHolidayMapper mapper;
    @Resource
    private Ehcache cache;

    /**
     * 计算下一个工作日
     * @author fuxin
     * @date 2017年9月22日 下午2:31:23
     * @description
     *		TODO
     * @param date
     * @return Date
     * @return null 节假日不可用
     *
     */
    public Date nextWorkDay(Date date,String localCode){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime();

        SysHoliday  holi= new SysHoliday();
        holi.setLocalCode(localCode);
        List<SysHoliday> holidays =findList(holi); //mapper.findList(holi);
        if(!isAvailable(holidays)){
            return null;
        }
        while(isHoliday(date, holidays)){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);//把日期往后增加一年.整数往后推,负数往前移动
            date = calendar.getTime();
        }
        return date;
    }

    /**
     * 获取节假日时间
     */
    public List<SysHoliday>  findList(SysHoliday  holi){
        try {
            Object obj = cache.getCache("Holiday"+holi.getLocalCode(), "areaCache");
            List<SysHoliday> list = new ArrayList<>();
            if(null == obj){
                list =mapper.findList(holi);
                cache.putCache("Holiday"+holi.getLocalCode(), list ,"areaCache");
            }else{
                list = (List<SysHoliday>) obj;
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }


    /**
     * 判断是否为工作日
     * @author fuxin
     * @date 2017年9月22日 下午2:31:59
     * @description
     *		TODO
     * @param date
     * @return
     *
     */
    public boolean isHoliday(Date date, List<SysHoliday> holidays){
        if(holidays == null){
            holidays = mapper.findList(new SysHoliday());
        }
        //将时间包装成Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //循环匹配节假日
        for(SysHoliday h : holidays){
            if(SysHoliday.DATETYPE_WEEK.equals(h.getDateType())){
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                //星期六或星期天。星期天是1，类推
                if(day == h.getDateDayInt()){
                    return true;
                }
            }
            if(SysHoliday.DATETYPE_DAY.equals(h.getDateType())){
                //将日期格式化比较
                if(DateUtils.formatDate(date).equals(DateUtils.formatDate(h.getDateTime()))){
                    return true;
                }
            }
        }
        return false;
    }



    /**
     * 检查节假日是否可用，即周一到周日是否都包含
     * @author fuxin
     * @date 2017年9月23日 上午10:59:47
     * @description
     *		TODO
     * @return
     *
     */
    private boolean isAvailable(List<SysHoliday> holidays){
        //将一周的放到set中
        Set<Integer> weekSet = new HashSet<Integer>();
        for(SysHoliday h:holidays){
            if(SysHoliday.DATETYPE_WEEK.equals(h.getDateType())){
                weekSet.add(h.getDateDayInt());
            }
        }
        //若有7个都有则不可用
        if(weekSet.size() >= 7){
            return false;
        }
        return true;
    }


}
