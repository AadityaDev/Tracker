package com.skybee.tracker.model;

public class UserServer {
    private long id;
    private long s_id;
    private int login_type;
    private String authToken;
    private String name;
    private String userEmail;
    private String phone;
    private String MobileNo;
    private String password;
    private String device_id;
    private String registration_key;
    private int used_limit;
    private int max_limit;
    private int status;
    private String api_token;
    private String UserType;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getS_id() {
        return this.s_id;
    }

    public void setS_id(long s_id) {
        this.s_id = s_id;
    }

    public int getLogin_type() {
        return this.login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobileNo() {
        return this.MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.MobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getRegistration_key() {
        return registration_key;
    }

    public void setRegistration_key(String registration_key) {
        this.registration_key = registration_key;
    }

    public int getUsed_limit() {
        return this.used_limit;
    }

    public void setUsed_limit(int used_limit) {
        this.used_limit = used_limit;
    }

    public int getMax_limit() {
        return this.max_limit;
    }

    public void setMax_limit(int max_limit) {
        this.max_limit = max_limit;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        this.UserType = userType;
    }
}
