package com.example.appointmentscheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppointmentModel {
    private int id;
    private String userId;
    private String time;
    private String day;
    private String isTaken;

    public AppointmentModel(int id, String userId, String start, String day, String taken) {
        this.id = id;
        this.userId = userId;
        this.time = start;
        this.day = day;
        this.isTaken = taken;
    }

    public AppointmentModel() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String t) {
        this.time = t;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String d) {
        this.day = d;
    }
    public String getIsTaken() {
        return isTaken;
    }
    public void setIsTaken(String t) {
        this.isTaken = t;
    }
    public String getDate(String startTimestamp) {
        long timestampMillis=Long.parseLong(startTimestamp);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date date=new Date(timestampMillis);
        return sdf.format(date);
    }
    /*public String getTime(String startTimestamp) {
        long timestampMillis=Long.parseLong(startTimestamp);

        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        Date date=new Date(timestampMillis);
        return sdf.format(date);
    }*/
}
