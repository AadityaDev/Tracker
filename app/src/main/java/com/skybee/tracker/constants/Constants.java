package com.skybee.tracker.constants;

public class Constants {

    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 10;
    public static final int NUMBER_OF_CARDS = 3;

    public class UserDetails {
        public static final String IS_LOGGED_IN = "IsLoggedIn";
        public static final String AUTH_TOKEN = "authToken";
        public static final String USER_ID = "userId";
        public static final String USER_NAME = "userName";
        public static final String USER_EMAIL = "userEmail";
        public static final String USER_MOBILE_NUMBER = "userMobileNo";
        public static final String USER_PASSWORD = "userPassword";
        public static final String USER_REGISTERATION_KEY = "registerationKey";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String IS_ADMIN = "isAdmin";
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
}
