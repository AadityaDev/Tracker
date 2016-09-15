package com.skybee.tracker.model;

public class RosterPojo {
    private long roster_id;
    private long customer_id;
    private long customer_site_id;
    private long site_id;
    private long employee_id;
    private int login_status;
    private String date;
    private String date_to;
    private String day;
    private String day_to;
    private String time_from;
    private String time_to;
    private String status;
    private String CustomerName;
    private String company_name;
    private String designation;
    private String ShortName;
    private String CUSTOMERNAME;
    private String designation1;
    private double latitude;
    private double longitude;
    private int radius;
    private String mobile;
    private String EmployeeName;
    private String address;
    private long site_task_id;
    private long taskId;
    private String TaskName;
    private String total_hours;
    private transient boolean isSelected;
    private transient boolean isAttendanceCard;

    public long getRoster_id() {
        return this.roster_id;
    }

    public void setRoster_id(long roster_id) {
        this.roster_id = roster_id;
    }

    public long getCustomer_id() {
        return this.customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public long getCustomer_site_id() {
        return this.customer_site_id;
    }

    public void setCustomer_site_id(long customer_site_id) {
        this.customer_site_id = customer_site_id;
    }

    public long getSite_id() {
        return this.site_id;
    }

    public void setSite_id(long site_id) {
        this.site_id = site_id;
    }

    public long getEmployee_id() {
        return this.employee_id;
    }

    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
    }

    public int getLogin_status() {
        return login_status;
    }

    public void setLogin_status(int login_status) {
        this.login_status = login_status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay_to() {
        return day_to;
    }

    public void setDay_to(String day_to) {
        this.day_to = day_to;
    }

    public String getTime_from() {
        return time_from;
    }

    public void setTime_from(String time_from) {
        this.time_from = time_from;
    }

    public String getTime_to() {
        return time_to;
    }

    public void setTime_to(String time_to) {
        this.time_to = time_to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        this.CustomerName = customerName;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        this.ShortName = shortName;
    }

    public String getCUSTOMERNAME() {
        return CUSTOMERNAME;
    }

    public void setCUSTOMERNAME(String CUSTOMERNAME) {
        this.CUSTOMERNAME = CUSTOMERNAME;
    }

    public String getDesignation1() {
        return designation1;
    }

    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.EmployeeName = employeeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getSite_task_id() {
        return this.site_task_id;
    }

    public void setSite_task_id(long site_task_id) {
        this.site_task_id = site_task_id;
    }

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        this.TaskName = taskName;
    }

    public String getTotal_hours() {
        return total_hours;
    }

    public void setTotal_hours(String total_hours) {
        this.total_hours = total_hours;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isAttendanceCard() {
        return isAttendanceCard;
    }

    public void setAttendanceCard(boolean attendanceCard) {
        isAttendanceCard = attendanceCard;
    }
}
