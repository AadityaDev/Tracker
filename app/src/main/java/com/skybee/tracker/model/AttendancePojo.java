package com.skybee.tracker.model;

public class AttendancePojo {
    private long roster_id;
    long customer_site_id;
    double lattitude;
    double longitude;
    String address;
    int LoginStatus;
    String company_name;
    private String imei_in;
    private String ip_in;

    public long getRoster_id() {
        return roster_id;
    }

    public void setRoster_id(long roster_id) {
        this.roster_id = roster_id;
    }

    public long getCustomer_site_id() {
        return this.customer_site_id;
    }

    public void setCustomer_site_id(long customer_site_id) {
        this.customer_site_id = customer_site_id;
    }

    public double getLattitude() {
        return this.lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLoginStatus() {
        return this.LoginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.LoginStatus = loginStatus;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getImei_in() {
        return imei_in;
    }

    public void setImei_in(String imei_in) {
        this.imei_in = imei_in;
    }

    public String getIp_in() {
        return ip_in;
    }

    public void setIp_in(String ip_in) {
        this.ip_in = ip_in;
    }
}
