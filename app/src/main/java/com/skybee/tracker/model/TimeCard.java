package com.skybee.tracker.model;

import java.util.Date;

public class TimeCard {

    private Date time;
    private String event;

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
