package com.skybee.tracker;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.User;
import com.skybee.tracker.network.ExecutorUtils;
import com.skybee.tracker.network.RequestGenerator;
import com.skybee.tracker.network.RequestHandler;

import org.json.JSONObject;

import java.util.concurrent.Callable;

import okhttp3.Request;

public class UserService {

    private final String TAG = this.getClass().getSimpleName();

//    public FutureTask<User> authenticateUser(Callable callable, @NonNull final User user) {
//        FutureTask<User> futureTask = new FutureTask<User>(callable);
//        ExecutorUtils.getUIThread().submit(new Callable<User>() {
//            @Override
//            public User call() throws Exception {
//                Request request = RequestGenerator.post(API.ADMIN_REGISTER_URL, user.toString());
//                String body = RequestHandler.makeRequestAndValidate(request);
//                Log.d(TAG, body);
//                Gson gson = new Gson();
//                final User user = gson.fromJson(body, User.class);
//                return user;
//            }
//        });
//        return futureTask;
//    }

    public ListenableFuture<User> authenticateUser(@NonNull final User user, @NonNull final String url) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<User>() {
            @Override
            public User call() throws Exception {
                Gson gson = new Gson();
                String userString = gson.toJson(user);
                Request request = RequestGenerator.post(url, userString);
                String body = RequestHandler.makeRequestAndValidate(request);
                Log.d(TAG, body);
                JSONObject result = new JSONObject(body);
                JSONObject data = new JSONObject();
                if (body.contains(Constants.JsonConstants.DATA)) {
                    data = Utility.getUserResultObject(body);
                } else {

                }
                final User user = gson.fromJson(data.toString(), User.class);
                return user;
            }
        });
    }

}
