package com.skybee.tracker.model;

public class AttendancePojo {
    long customer_site_id;
    double lattitude;
    double longitude;
    String address;
    int LoginStatus;
    transient String company_name;

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
}
