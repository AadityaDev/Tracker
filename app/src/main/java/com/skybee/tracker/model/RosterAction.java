package com.skybee.tracker.model;

public class RosterAction {
    private long[] roster_id;
    private String status;

    public long[] getRoster_id() {
        return this.roster_id;
    }

    public void setRoster_id(long[] roster_id) {
        this.roster_id = roster_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
