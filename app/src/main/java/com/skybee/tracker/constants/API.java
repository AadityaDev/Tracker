package com.skybee.tracker.constants;

public class API {

    public static final String SERVER_BASE = "http://www.impulse.webbzer.com/app/webroot/servicesApi/indextrackmzapp.php/";
    public static final String ADMIN_SIGN_UP = "http://track.snoowa.com/v1/indextrackmzapp.php/adminsignup";
    public static final String EMPLOYEE_SIGN_UP = "http://track.snoowa.com/v1/indextrackmzapp.php/employeesignup";
    public static final String LOGIN = "http://track.snoowa.com/v1/indextrackmzapp.php/login";
    public static final String EMPLOYEE_LIST = "http://track.snoowa.com/v1/indextrackmzapp.php/employeelist";
    public static final String ROSTER_LIST = "http://track.snoowa.com/v1/indextrackmzapp.php/rosterlist";
    public static final String ACCEPTED_ROSTER_LIST = "http://track.snoowa.com/v1/indextrackmzapp.php/rosterconfirmed";
    public static final String REJECTED_ROSTER_LIST = "http://track.snoowa.com/v1/indextrackmzapp.php/rosterrejected";
    public static final String ACCEPTED_ROSTER_ACTION = "http://track.snoowa.com/v1/indextrackmzapp.php/acceptresectroster";
    public static final String CUSTOMER_SITES = "http://track.snoowa.com/v1/indextrackmzapp.php/customersites";
    public static final String SAVE_ATTENDANCE = "http://track.snoowa.com/v1/indextrackmzapp.php/addattendance";

    public class Headers {
        public static final String ACCEPT_KEY = "Accept";
        public static final String ACCEPT_VALUE = "application/vnd.tracking.v0+json";
        public static final String ACCEPT_JSON = "application/json";
        public static final String AUTHORIZATION_KEY = "Auth";
        public static final String AUTH_KEY = "Auth";
        public static final String CAN_RENDER_HTML = "X-HTML-CanRender";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String SKYBEE_PLATFORM = "SKYBEE_PLATFORM";
        public static final String SKYBEE_PLATFORM_ANDROID = "android";
    }

    public class Roster {
        public static final String CONFIRMED = "Confirmed";
        public static final String REJECTED = "Rejected";
    }

}
