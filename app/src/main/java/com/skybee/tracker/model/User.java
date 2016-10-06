package com.skybee.tracker.model;

public class User {

    private Long id;
    private String authToken;
    private Long branchId;
    private boolean isAdmin;
    private String registrationCode;
    private String userEmail;
    private String userImage;
    private double userLatitude;
    private double userLongitude;
    private String userMobileNumber;
    private String userName;
    private String userPassword;
    private String userCompany;
    private transient int login_type;
    private String device_id;
    private long roster_id;
    private transient double companyLatitude;
    private transient double companyLongitude;
    private transient long companyRadius;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Long getBranchId() {
        return this.branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImage() {
        return this.userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public double getUserLatitude() {
        return this.userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return this.userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public int getLogin_type() {
        return this.login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public long getRoster_id() {
        return roster_id;
    }

    public void setRoster_id(long roster_id) {
        this.roster_id = roster_id;
    }

    public double getCompanyLatitude() {
        return companyLatitude;
    }

    public void setCompanyLatitude(double companyLatitude) {
        this.companyLatitude = companyLatitude;
    }

    public double getCompanyLongitude() {
        return companyLongitude;
    }

    public void setCompanyLongitude(double companyLongitude) {
        this.companyLongitude = companyLongitude;
    }

    public long getCompanyRadius() {
        return companyRadius;
    }

    public void setCompanyRadius(long companyRadius) {
        this.companyRadius = companyRadius;
    }
}
