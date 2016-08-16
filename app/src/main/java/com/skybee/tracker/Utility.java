package com.skybee.tracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.skybee.tracker.activities.HomeScreenActivity;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.TimeCard;
import com.skybee.tracker.model.User;
import com.skybee.tracker.network.UIThreadExecutor;
import com.skybee.tracker.preferences.UserStore;
import com.skybee.tracker.ui.dialog.ErrorDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    private static final String TAG = "Utility";
    private static ErrorDialog errorDialog;
    private static ErrorDialog getErrorDialog;
    private static boolean error;


    public static TimeCard setEventType(TimeCard timeCard, int size) {
        switch (size) {
            case 1:
                timeCard.setEvent(Constants.HeadingText.CHECK_IN);
                break;
            case 2:
                timeCard.setEvent(Constants.HeadingText.CHECK_LUNCH);
                break;
            case 3:
                timeCard.setEvent(Constants.HeadingText.CHECK_OUT);
                break;
            case 4:
                timeCard.setEvent(Constants.HeadingText.CHECK_OFF);
                break;
        }
        return timeCard;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomeScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void authenticate(@NonNull final Context context, @NonNull final ProgressDialog progressDialog, @NonNull String url, @NonNull User user) {
        ListenableFuture<User> authenticateUser = Factory.getUserService().authenticateUser(user, url);
        Futures.addCallback(authenticateUser, new FutureCallback<User>() {
            @Override
            public void onSuccess(User result) {
                if (result != null) {
                    Log.d(TAG, result.toString());
                    if (result.getAuthToken() != null) {
                        UserStore userStore = new UserStore(context);
                        userStore.saveIsAdmin(result.isAdmin());
                        userStore.saveAuthToken(result.getAuthToken());
                        userStore.saveUserEmail(result.getUserEmail());
//                    userStore.saveUserEmail(result.getUserImage());
                        userStore.saveUserMobileNumber(result.getUserMobileNumber());
//                    userStore.saveId(result.getId());
                        userStore.saveRegistrationCode(result.getRegistrationCode());
                        progressDialog.dismiss();
                        startActivity(context);
                    } else {
                        error=true;
                        progressDialog.dismiss();
                        errorDialog = new ErrorDialog(context, Constants.ERROR_OCCURRED);
                        errorDialog.show();
//                        Looper.prepare();
//                        getErrorDialog=new ErrorDialog(context,Constants.ERROR_OCCURRED);
//                        getErrorDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, Constants.ERROR);
                progressDialog.dismiss();
                error=true;
                if (t != null) {
                    if (t.getMessage() != null) {
                        errorDialog = new ErrorDialog(context, t.getMessage());
                    } else {
                        errorDialog = new ErrorDialog(context, Constants.ERROR_OCCURRED);
                    }
                } else {
                    errorDialog = new ErrorDialog(context, Constants.ERROR_OCCURRED);
                }
                errorDialog.show();
            }
        });
        if(authenticateUser.isDone()&&error){
            errorDialog = new ErrorDialog(context, Constants.ERROR_OCCURRED);
            errorDialog.show();
        }
    }

    public static JSONObject getUserResultObject(String body) throws JSONException {
        JSONObject jsonObject = new JSONObject(body);
        JSONObject result = jsonObject.getJSONObject(Constants.JsonConstants.DATA);
        return result;
    }
}
