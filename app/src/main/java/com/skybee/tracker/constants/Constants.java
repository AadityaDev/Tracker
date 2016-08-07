package com.skybee.tracker.constants;

public class Constants {

    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 10;
    public static final int NUMBER_OF_CARDS = 3;
    public static final int MOBILE_NUMBER_LENGTH = 10;
    public static final String EMPTY = "";
    public static final Long EMPTY_LONG = 0l;
    public static final boolean EMPTY_BOOLEAN = false;
    public static final double EMPTY_DOUBLE = 0;
    public static final String ERROR = "Error";

    public class AuthType {
        public static final String ADMIN_SIGN_UP = "AdminSignUp";
        public static final String EMPLOYEE_SIGN_UP = "EmployeeSignUp";
        public static final String LOGIN = "LogIn";
    }

    public class UserStore {
        public static final String PREF_NAME = "userStore";
        public static final String IS_LOGGED_IN = "isLoggedIn";
        public static final String AUTH_TOKEN = "authToken";
        public static final String USER_ID = "userId";
        public static final String USER_NAME = "userName";
        public static final String USER_EMAIL = "userEmail";
        public static final String USER_MOBILE_NUMBER = "userMobileNo";
        public static final String USER_PASSWORD = "userPassword";
        public static final String USER_REGISTRATION_KEY = "registrationKey";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
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
        public static final String CODE = "code";
        public static final String DATA = "data";
        public static final String MESSAGE = "message";
    }
}
