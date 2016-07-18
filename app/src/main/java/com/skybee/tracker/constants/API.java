package com.skybee.tracker.constants;

public class API {
    public static final String SERVER_BASE = "http://www.impulse.webbzer.com/app/webroot/servicesApi/indextrackmzapp.php/";

    public class Headers {
        public static final String ACCEPT_KEY = "Accept";
        public static final String ACCEPT_VALUE = "application/vnd.tracking.v0+json";
        public static final String ACCEPT_JSON = "application/json";
        public static final String AUTHORIZATION_KEY = "Authorization";
        public static final String CAN_RENDER_HTML = "X-HTML-CanRender";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String SKYBEE_PLATFORM = "SKYBEE_PLATFORM";
        public static final String SKYBEE_PLATFORM_ANDROID = "android";
    }

    public static final String LOGIN_URL = SERVER_BASE + "login";
    public static final String REGISTER_URL = SERVER_BASE + "clientsignup";
    public static String ADMIN_REGISTER_URL = SERVER_BASE + "signup";
    public static String INVITE_URL = SERVER_BASE + "invitesignup";
    public static String GET_USER_URL = SERVER_BASE + "invitelist";
    public static String HMDATA_URL = "http://www.helpforcomfort.com/HFC/rest_api/rest/hmdata/%s/%s";
    public static String HMDATA_ALL_URL = "http://www.helpforcomfort.com/HFC/rest_api/rest/alldata/%s/%s";
    public static String UPLOAD_ALL_DATA = "http://www.helpforcomfort.com/HFC/rest_api/rest/submitdata";
    public static String UPLOAD_LOCATION_DATA = SERVER_BASE + "addlocation";
    public static String GET_LOCATION_DATA = SERVER_BASE + "memberlocationlist?limitval=%s";
    public static String GET_USER_LOCATION_DATA = SERVER_BASE + "userlocation";

}
