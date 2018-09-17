package com.junl.wpwx.model;



import org.apache.ibatis.type.Alias;

@Alias("WorkingHours")
public class WorkingHours {

    private String localcode;  //接种单位编号
    private String week;  //星期
    private String dateDay;  //1-7
    private String timeSlice;  //时间段
    private int maximum;  //人数

    public String getLocalcode() {
        return localcode;
    }

    public void setLocalcode(String localcode) {
        this.localcode = localcode;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDateDay() {
        return dateDay;
    }

    public void setDateDay(String dateDay) {
        this.dateDay = dateDay;
    }

    public String getTimeSlice() {
        return timeSlice;
    }

    public void setTimeSlice(String timeSlice) {
        this.timeSlice = timeSlice;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }
}
