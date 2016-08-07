package com.skybee.tracker;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.skybee.tracker.constants.API;
import com.skybee.tracker.model.User;
import com.skybee.tracker.network.ExecutorUtils;
import com.skybee.tracker.network.RequestGenerator;
import com.skybee.tracker.network.RequestHandler;

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

    public ListenableFuture<User> authenticateUser(@NonNull final User user) {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<User>() {
            @Override
            public User call() throws Exception {
                Request request = RequestGenerator.post(API.ADMIN_SIGN_UP, user.toString());
                String body = RequestHandler.makeRequestAndValidate(request);
                Log.d(TAG, body);
                Gson gson = new Gson();
                final User user = gson.fromJson(body, User.class);
                return user;
            }
        });
    }

}
