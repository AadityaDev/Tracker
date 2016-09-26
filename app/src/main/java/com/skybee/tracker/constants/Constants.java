package com.skybee.tracker.constants;

public class Constants {

    public static final int NOTIFICATION_ID = 9999;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 10;
    public static final int NUMBER_OF_CARDS = 3;
    public static final int MOBILE_NUMBER_LENGTH = 10;
    public static final String EMPTY = "";
    public static final Long EMPTY_LONG = 0l;
    public static final boolean EMPTY_BOOLEAN = false;
    public static final double EMPTY_DOUBLE = 0;
    public static final long DEFAULT_RADIUS = 500;
    public static final String ERROR = "Error";
    public static final String ERROR_OCCURRED = "Error Occured!";
    public static final String AND = "&";
    public static final String QUESTION_MARK = "?";
    public static final String PAGE_NUMER_TEXT = "pageNumber=";
    public static final String PAGE_SIZE_TEXT = "pageSize=";

    public class AuthType {
        public static final String ADMIN_SIGN_UP = "AdminSignUp";
        public static final String EMPLOYEE_SIGN_UP = "EmployeeSignUp";
        public static final String LOGIN = "LogIn";
    }

    public class UserStore {
        public static final String PREF_NAME = "userStore";
        public static final String IS_LOGGED_IN = "isLoggedIn";
        public static final String AUTH_TOKEN = "authToken";
        public static final String COMPANY_ID = "companyId";
        public static final String USER_ID = "userId";
        public static final String USER_NAME = "userName";
        public static final String USER_EMAIL = "userEmail";
        public static final String USER_MOBILE_NUMBER = "userMobileNo";
        public static final String USER_PASSWORD = "userPassword";
        public static final String USER_REGISTRATION_KEY = "registrationKey";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String USER_COMPANY = "userCompany";
        public static final String COMPANY_LATITUDE = "companyLatitude";
        public static final String COMPANY_LONGITUDE = "companyLongitude";
        public static final String COMPANY_RADIUS = "companyRadius";
        public static final String IS_ADMIN = "isAdmin";
        public static final int PRIVATE_MODE = 0;
    }

    public class HeadingText {
        public static final String EMPTY_CARD = "EmptyCard";
        public static final String CHECK_IN = "CheckIn";
        public static final String CHECK_LUNCH = "Lunch";
        public static final String CHECK_OUT = "CheckOut";
        public static final String CHECK_OFF = "CheckOff";
    }

    public class Event {
        public static final int EMPTY_CARD = 0;
        public static final int CHECK_IN = 1;
        public static final int CHECK_LUNCH = 2;
        public static final int CHECK_OUT = 3;
        public static final int CHECK_OFF = 4;
    }

    public class EmployeeStatus {
        public static final String AT_WORK = "AtWork";
        public static final String ABSENT = "Absent";
        public static final String ON_LEAVE = "OnLeave";
    }

    public class Exception {
        public static final String EXCEPTION = "Exception";
        public static final String JSON_EXCEPTION = "JsonException";
        public static final String HTTP_EXCEPTION = "HttpException";
    }

    public class JsonConstants {
        public static final String BRANCH_ID = "branch_id";
        public static final String CODE = "code";
        public static final String DATA = "data";
        public static final String MESSAGE = "message";
        public static final String ERROR = "error";
        public static final String ERROR_CODE = "error_code";
        public static final String EMPLOYEE_ID = "employee_id";
        public static final String EMAIL = "email";
        public static final String NAME = "name";
        public static final String MOBILE = "mobile";
        public static final String API_TOKEN = "api_token";
        public static final String SUCCESS = "Success";
        public static final String MARK_BUTTON_STATUS="mark_btn_status";
        public static final String OFF_BUTTON_STATUS="off_btn_status";
    }

    public static final int rosterCard = 0;
    public static final int isSiteCard = 1;

    public static class Geometry {
        public static double MinLatitude = -90.0;
        public static double MaxLatitude = 90.0;
        public static double MinLongitude = -180.0;
        public static double MaxLongitude = 180.0;
        public static double MinRadius = 0.01; // kilometers
        public static double MaxRadius = 20.0; // kilometers
    }

    public static class SharedPrefs {
        public static String Geofences = "SHARED_PREFS_GEOFENCES";
    }

    public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";
    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";
    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";
    public static final String GEOFENCES_LOCATION = "LOCATION";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 8;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 1209; // 1 mile, 1.6 km

    public static final String INTENT_ACTION = "com.skybee.tracker.PERIODIC_LOCATION_UPDATE";
    public static final String LOCATION_SERVICE_STORE = "LocationServiceStore";
    public static final String BACKGROUND_SERVICE_BATTERY_CONTROL = "BatteryControl";
    public static final String IS_BOUNDARY_CROSSED = "BoundaryCrossed";

    public static final class LOGIN_STATUS {
        public static final int ABSENT = 0;
        public static final int PRESENT = 1;
        public static final int LOGOUT = 2;
        public static final int OFF_DUTY = 3;
    }
}
