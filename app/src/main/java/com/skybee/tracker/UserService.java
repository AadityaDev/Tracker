package com.skybee.tracker;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.AttendancePojo;
import com.skybee.tracker.model.RosterAction;
import com.skybee.tracker.model.User;
import com.skybee.tracker.model.UserServer;
import com.skybee.tracker.network.ExecutorUtils;
import com.skybee.tracker.network.RequestGenerator;
import com.skybee.tracker.network.RequestHandler;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.Callable;

import okhttp3.Request;

public class UserService {

    private final String TAG = this.getClass().getSimpleName();

    public ListenableFuture<JSONObject> authenticateUser(@NonNull final User user, @NonNull final String url) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                Gson gson = new Gson();
                String userString = gson.toJson(user);
                Request request = RequestGenerator.post(url, userString);
                String body = RequestHandler.makeRequestAndValidate(request);
                Log.d(TAG, body);
                JSONObject result = new JSONObject(body);
//                JSONObject data = new JSONObject();
//                if (body.contains(Constants.JsonConstants.DATA)) {
//                    data = Utility.getUserResultObject(body);
//                } else {
//
//                }
//                final UserServer userServer = gson.fromJson(data.toString(), UserServer.class);
//                User userDetail = new User();
//                if (userServer != null) {
//                    if (userServer.getId() != 0)
//                        userDetail.setId(userServer.getId());
//                    if (userServer.getDevice_id() != null)
//                        userDetail.setDevice_id(userServer.getDevice_id());
//                    if (userServer.getRegistration_key() != null)
//                        userDetail.setRegistrationCode(userServer.getRegistration_key());
//                    if (userServer.getPhone() != null)
//                        userDetail.setUserMobileNumber(userServer.getPhone());
//                    if (userServer.getApi_token() != null)
//                        userDetail.setAuthToken(userServer.getApi_token());
//                    if (userServer.getName() != null)
//                        userDetail.setUserName(userServer.getName());
//
//                }
                return result;
            }
        });
    }

    public ListenableFuture<JSONObject> employeeList(@NonNull final String url, @NonNull final User user) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                Log.d(TAG, user.getAuthToken());
                UserServer userServer = new UserServer();
                userServer.setApi_token(user.getAuthToken());
                Log.d(TAG, user.getAuthToken());
                Request request = RequestGenerator.get(url, user.getAuthToken());
                String body = RequestHandler.makeRequestAndValidate(request);
                JSONObject result = new JSONObject(body);
                return result;
            }
        });
    }

    public ListenableFuture<JSONObject> roasterList(@NonNull final String url, @NonNull final User user) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                Log.d(TAG, user.getAuthToken());
                UserServer userServer = new UserServer();
                userServer.setApi_token(user.getAuthToken());
                Log.d(TAG, user.getAuthToken());
                Request request = RequestGenerator.get(url, user.getAuthToken());
                String body = RequestHandler.makeRequestAndValidate(request);
                JSONObject result = new JSONObject(body);
                return result;
            }
        });
    }

    public ListenableFuture<JSONObject> acceptOrRejectRoster(@NonNull final String url, @NonNull final User user, @NonNull final String userAction, @NonNull final long ids) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                Log.d(TAG, user.getAuthToken());
                Gson gson = new Gson();
                RosterAction rosterAction = new RosterAction();
                rosterAction.setRoster_id(ids);
                rosterAction.setStatus(userAction);
                String rosterActionString = gson.toJson(rosterAction);
                Request request = RequestGenerator.postWithToken(url, rosterActionString, user.getAuthToken());
                String body = RequestHandler.makeRequestAndValidate(request);
                JSONObject result = new JSONObject(body);
                return result;
            }
        });
    }

    public ListenableFuture<JSONObject> customerSites(@NonNull final String url, @NonNull final User user) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                Log.d(TAG, user.getAuthToken());
                Request request = RequestGenerator.get(url, user.getAuthToken());
                String body = RequestHandler.makeRequestAndValidate(request);
                JSONObject result = new JSONObject(body);
                return result;
            }
        });
    }

    public ListenableFuture<JSONObject> markAttendance(@NonNull final String url, @NonNull final User user, @NonNull final AttendancePojo attendancePojo) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                Gson gson = new Gson();
                String params = gson.toJson(attendancePojo);
                Request request = RequestGenerator.postWithToken(url, params, user.getAuthToken());
                Log.v(TAG,request.toString());
                String body = RequestHandler.makeRequestAndValidate(request);
                Log.v(TAG,body);
                JSONObject result = new JSONObject(body);
                return result;
            }
        });
    }

    public ListenableFuture<JSONObject> getAttendance(@NonNull final String url, @NonNull final User user,
                                                      @NonNull final int pageNumber, @NonNull final int pageSize) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                Gson gson = new Gson();
                Request request = RequestGenerator.get(url + Constants.QUESTION_MARK + Constants.PAGE_NUMER_TEXT + pageNumber
                        + Constants.AND + Constants.PAGE_SIZE_TEXT + pageSize, user.getAuthToken());
                String body = RequestHandler.makeRequestAndValidate(request);
                JSONObject result = new JSONObject(body);
                return result;
            }
        });
    }

    public ListenableFuture<JSONObject> updateUserDetails(@NonNull final User user, @NonNull final String url, @NonNull final File file) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                Gson gson = new Gson();
                String userString = gson.toJson(user);
                Request request = RequestGenerator.multipartPost(url, file,user.getAuthToken());
                String body = RequestHandler.makeRequestAndValidate(request);
                Log.d(TAG, body);
                JSONObject result = new JSONObject(body);
                return result;
            }
        });
    }

    public ListenableFuture<JSONObject> getUserProfile(@NonNull final String url, @NonNull final User user) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                Gson gson = new Gson();
                Request request = RequestGenerator.get(url , user.getAuthToken());
                String body = RequestHandler.makeRequestAndValidate(request);
                JSONObject result = new JSONObject(body);
                return result;
            }
        });
    }
}
