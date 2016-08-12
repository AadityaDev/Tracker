package com.skybee.tracker.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.skybee.tracker.constants.API;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestGenerator {
    public static String TAG = "RequestGenerator";
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");

    private static void addDefaultHeaders(@NonNull Request.Builder builder) {

    }

    public static Request get(@NonNull String url) {
        Request.Builder builder = new Request.Builder().url(url);
        addDefaultHeaders(builder);
        Log.i(TAG, builder.build().toString());
        return builder.build();
    }

    public static Request get(@NonNull String url, @NonNull String token) {
        Log.i(TAG, token);
        Request.Builder builder = new Request.Builder().url(url);
        addDefaultHeaders(builder);
        builder.addHeader(API.Headers.AUTH_KEY, token);
        builder.addHeader(API.Headers.CONTENT_TYPE, API.Headers.ACCEPT_JSON);
        return builder.build();
    }

    public static Request post(@NonNull String url, @NonNull String params) {
        Request.Builder builder = new Request.Builder().url(url);
        builder.addHeader(API.Headers.CONTENT_TYPE, API.Headers.ACCEPT_JSON);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, params);
        Gson gson = new Gson();
        String s = gson.toJson(builder.post(body));
        return builder.post(body).build();
    }

    public static Request postWithToken(@NonNull String url, @NonNull String params, @NonNull String authToken) {
        Request.Builder builder = new Request.Builder().url(url);
        addDefaultHeaders(builder);
        builder.addHeader(API.Headers.AUTHORIZATION_KEY, authToken);
        builder.addHeader(API.Headers.CONTENT_TYPE, API.Headers.ACCEPT_JSON);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, params.toString());
        Gson gson = new Gson();
        String s = gson.toJson(body);
        return builder.post(body).build();
    }

    public static Request putWithToken(@NonNull String url, @NonNull String params, @NonNull String authToken) {
        Request.Builder builder = new Request.Builder().url(url);
        addDefaultHeaders(builder);
        builder.addHeader(API.Headers.AUTHORIZATION_KEY, authToken);
        builder.addHeader(API.Headers.CONTENT_TYPE, API.Headers.ACCEPT_JSON);
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, params.toString());
        Gson gson = new Gson();
        String s = gson.toJson(builder.put(body));
        return builder.put(body).build();
    }
}
