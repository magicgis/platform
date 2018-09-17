package com.junl.wpwx.mapper;

import com.junl.wpwx.model.DepartmentInfo;
import com.junl.wpwx.model.Notification;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DepartmentInfoMapper extends CrudMapper<DepartmentInfo> {


    //通过接种单位编号取详情
    List<DepartmentInfo> findByLocalcode(String localcode);

    //我的界面-接种单位  edit by wangnan 2018-2-27 
    public HashMap<String,String> findByChildId(@Param(value="childId")String childId);
    
}
