package com.skybee.tracker.network;

import android.util.Log;

import com.skybee.tracker.Factory;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.constants.ParserConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestHandler {

    public static final String TAG = "RequestHandler";

    public static String makeRequestAndValidate(Request request) throws JSONException, IOException, Exception {
        return doRequest(request, true);
    }

    private static String doRequest(Request request, boolean doValidate) throws IOException, JSONException, Exception {
        Log.i(TAG, request.method() + " : " + request.url());
        OkHttpClient httpClient = Factory.getOkHttpClient();
        Response response;
        String body = new String();
        response = httpClient.newCall(request).execute();
        if (response == null) {
            return null;
        }
        body = response.body().string();
        Log.i(TAG, response.code() + " : " + body);
        if (!response.isSuccessful()) {
            Log.d(TAG, response.toString());
            if (response.code() == 400) {
            } else if (response.code() == 401 || response.code() == 1028) {
            } else if (response.code() == 404) {
            } else if (response.code() == 403) {
            } else if (response.code() == 415) {
            } else if (response.code() >= 500) {
            } else {
            }
        } else {
            if (doValidate) {
                validateResponse(body);
            }
        }
        return body;
    }

    private static void validateResponse(String body) throws JSONException {
        JSONObject response;
        try {
            response = new JSONObject(body);
            if (response.isNull(ParserConstants.STATUS)) {
                return;
            }
            String status = response.getString(ParserConstants.STATUS);
            if (!status.equals(ParserConstants.SUCCESS)) {
                if (response.isNull(ParserConstants.ERRORS)) {
                    return;
                } else {
                    JSONArray errors = response.getJSONObject(ParserConstants.ERRORS).getJSONArray(ParserConstants.ERRS);
                    StringBuilder error = new StringBuilder(2);
                    for (int i = 0; i < errors.length(); i++) {
                        error.append(errors.getJSONObject(i).getString(ParserConstants.MSG));
                        error.append(". ");
                    }
                }
            }
        } catch (JSONException ignored) {
            Log.d(TAG, Constants.Exception.JSON_EXCEPTION);
            throw new JSONException(body);
        } catch (Exception e) {
            Log.d(TAG, Constants.Exception.EXCEPTION);
        }
    }
}
