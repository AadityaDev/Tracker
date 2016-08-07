package com.skybee.tracker;

import net.jcip.annotations.GuardedBy;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Factory {
    private final String TAG = this.getClass().getSimpleName();
    private static final Object LOCK = new Object();
    public static final int TIMEOUT_IN_SECONDS = 60;

    @GuardedBy("LOCK")
    private static OkHttpClient okHttpClient;
    @GuardedBy("LOCK")
    private static UserService userService;

    public static OkHttpClient getOkHttpClient() {
        synchronized (LOCK) {
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                        .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                        .build();
            }
        }
        return okHttpClient;
    }

    public static UserService getUserService() {
        synchronized (LOCK) {
            if (userService == null) {
                userService = new UserService();
            }
        }
        return userService;
    }

}
