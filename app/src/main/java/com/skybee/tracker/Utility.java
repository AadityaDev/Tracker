package com.skybee.tracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.skybee.tracker.activities.HomeScreenActivity;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.TimeCard;
import com.skybee.tracker.model.User;
import com.skybee.tracker.model.UserServer;
import com.skybee.tracker.network.ExecutorUtils;
import com.skybee.tracker.preferences.UserStore;
import com.skybee.tracker.ui.dialog.ErrorDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    private static final String TAG = "Utility";
    private static ErrorDialog errorDialog;
    private static ErrorDialog getErrorDialog;
    private static boolean error;
    private static String errorMessage = null;

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
        ListenableFuture<JSONObject> authenticateUser = Factory.getUserService().authenticateUser(user, url);
        Futures.addCallback(authenticateUser, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONObject data = new JSONObject();
                    Gson gson = new Gson();
                    if (result.has(Constants.JsonConstants.DATA)) {
                        data = Utility.getUserResultObject(result.toString());
                        final UserServer userServer = gson.fromJson(data.toString(), UserServer.class);
                        User userDetail = new User();
                        if (userServer != null) {
                            if (userServer.getId() != 0)
                                userDetail.setId(userServer.getId());
                            if (userServer.getDevice_id() != null)
                                userDetail.setDevice_id(userServer.getDevice_id());
                            if (userServer.getRegistration_key() != null)
                                userDetail.setRegistrationCode(userServer.getRegistration_key());
                            if (userServer.getPhone() != null)
                                userDetail.setUserMobileNumber(userServer.getPhone());
                            if(userServer.getMobile()!=null)
                                userDetail.setUserMobileNumber(userServer.getMobile());
                            if (userServer.getAuthToken() != null)
                                userDetail.setAuthToken(userServer.getAuthToken());
                            if (userServer.getName() != null)
                                userDetail.setUserName(userServer.getName());
                            if(userServer.getUserEmail()!=null)
                                userDetail.setUserEmail(userServer.getUserEmail());
                            if(userServer.getUserType().equals("Admin"))
                                userDetail.setAdmin(true);
                            saveUserDetailsPreference(context, userDetail);
                            progressDialog.dismiss();
                            startActivity(context);
                        }
                    } else if (result.has(Constants.JsonConstants.MESSAGE)) {
                        progressDialog.dismiss();
                        errorMessage = result.getString(Constants.JsonConstants.MESSAGE);
                        showErrorDialog(context, errorMessage);
                    } else {
                        progressDialog.dismiss();
                        errorMessage = Constants.ERROR_OCCURRED;
                        showErrorDialog(context, errorMessage);
                    }
                } catch (JSONException jsonException) {
                    progressDialog.dismiss();
                    errorMessage = Constants.ERROR_OCCURRED;
                    showErrorDialog(context, errorMessage);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    errorMessage = Constants.ERROR_OCCURRED;
                    showErrorDialog(context, errorMessage);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null) {
                    if (t.getMessage() != null) {
                        errorMessage = t.getMessage();
                    } else {
                        errorMessage = Constants.ERROR_OCCURRED;
                    }
                } else {
                    errorMessage = Constants.ERROR_OCCURRED;
                }
                progressDialog.dismiss();
                showErrorDialog(context, errorMessage);
            }
        }, ExecutorUtils.getUIThread());
    }

    public static JSONObject getUserResultObject(String body) throws JSONException {
        JSONObject jsonObject = new JSONObject(body);
        JSONObject result = jsonObject.getJSONObject(Constants.JsonConstants.DATA);
        return result;
    }

    public static void showErrorDialog(@NonNull Context context, @NonNull String messsage) {
        errorDialog = new ErrorDialog(context, messsage);
        errorDialog.show();
    }

    public static void saveUserDetailsPreference(@NonNull Context context, @NonNull User user) {
        UserStore userStore = new UserStore(context);
        userStore.saveUserName(user.getUserName());
        userStore.saveIsAdmin(user.isAdmin());
        userStore.saveAuthToken(user.getAuthToken());
        userStore.saveUserEmail(user.getUserEmail());
//                    userStore.saveUserEmail(result.getUserImage());
        userStore.saveUserMobileNumber(user.getUserMobileNumber());
//                    userStore.saveId(result.getId());
        userStore.saveRegistrationCode(user.getRegistrationCode());
    }

    public static void shareRegistrationCode(@NonNull Context context,@NonNull String registrationCode){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "\nDownload app from this url."
                +"\nhttps://play.google.com/store/apps/details?id=com.recharge.myrecharge&hl=en"
                +"\nRegistration Code: "+registrationCode);
        context.startActivity(Intent.createChooser(sharingIntent,"Share registration code with employee."));
    }
}
