package com.junl.wpwx.mapper;

import com.junl.wpwx.model.Notification;
import com.junl.wpwx.model.PromotMessage;
import com.junl.wpwx.model.WorkingHours;

import java.util.List;

public interface WorkingHouesMapper extends CrudMapper<WorkingHours> {

    List<WorkingHours> findByLocalcode(WorkingHours work);

}
