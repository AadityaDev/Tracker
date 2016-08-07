package com.skybee.tracker.model;

public class UserServer {
    private String userEmail;
    private String userName;
    private String userPassword;
    private String registrationCode;
    private int login_type;
    private String userMobileNumber;
    private String device_id;

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRegistrationCode() {
        return this.registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public int getLogin_type() {
        return this.login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }

    public String getUserMobileNumber() {
        return this.userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
