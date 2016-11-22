package com.skybee.tracker.constants;

public class API {

   public static final String SERVER_BASE = "http://wapinnovation.net/roster/v1/";
  //  public static final String SERVER_BASE = "http://www.eventsbyideation.com/v1/";
    public static final String ADMIN_SIGN_UP = SERVER_BASE + "indextrackmzapp.php/adminsignup";
    public static final String EMPLOYEE_SIGN_UP = SERVER_BASE + "indextrackmzapp.php/employeesignup";
    public static final String LOGIN = SERVER_BASE + "indextrackmzapp.php/login";
    public static final String EMPLOYEE_LIST = SERVER_BASE + "indextrackmzapp.php/employeelist";
    public static final String ROSTER_LIST = SERVER_BASE + "indextrackmzapp.php/rosterlist";
    public static final String ACCEPTED_ROSTER_LIST = SERVER_BASE + "indextrackmzapp.php/rosterconfirmed";
    public static final String REJECTED_ROSTER_LIST = SERVER_BASE + "indextrackmzapp.php/rosterrejected";
    public static final String ACCEPTED_ROSTER_ACTION = SERVER_BASE + "indextrackmzapp.php/acceptresectroster";
    public static final String CUSTOMER_SITES = SERVER_BASE + "indextrackmzapp.php/customersites";
    public static final String SAVE_ATTENDANCE = SERVER_BASE + "indextrackmzapp.php/addattendance";
//    public static final String ATTENDANCE_LIST = SERVER_BASE + "indextrackmzapp.php/myattendance";
    public static final String ATTENDANCE_LIST = "http://wapinnovation.net/roster/v1/indextrackmzapp.php/attendancelist";
  //  public static final String ATTENDANCE_LIST = "http://www.eventsbyideation.com/v1/indextrackmzapp.php/attendancelist";

    public static final String OFF_DUTY = SERVER_BASE + "indextrackmzapp.php/addattendanceclickout";
    public static final String UPDATE_PROFILE = SERVER_BASE + "indextrackmzapp.php/userprofileupdate";
    public static final String PROFILE_URL = SERVER_BASE + "indextrackmzapp.php/profiledetails";

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
