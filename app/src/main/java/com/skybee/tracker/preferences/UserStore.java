package com.skybee.tracker.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.User;

import static com.skybee.tracker.constants.Constants.EMPTY;
import static com.skybee.tracker.constants.Constants.EMPTY_BOOLEAN;
import static com.skybee.tracker.constants.Constants.EMPTY_DOUBLE;
import static com.skybee.tracker.constants.Constants.EMPTY_LONG;

public class UserStore {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserStore(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.UserStore.PREF_NAME, Constants.UserStore.PRIVATE_MODE);
    }

    public User getUserDetails() {
        User user = new User();
        user.setId(sharedPreferences.getLong(Constants.UserStore.USER_ID, EMPTY_LONG));
        user.setAuthToken(sharedPreferences.getString(Constants.UserStore.AUTH_TOKEN, EMPTY));
        user.setAdmin(sharedPreferences.getBoolean(Constants.UserStore.IS_ADMIN, EMPTY_BOOLEAN));
        user.setRegistrationCode(sharedPreferences.getString(Constants.UserStore.USER_REGISTRATION_KEY, EMPTY));
        user.setUserEmail(sharedPreferences.getString(Constants.UserStore.USER_EMAIL, EMPTY));
        user.setUserLatitude(getDouble(sharedPreferences, Constants.UserStore.LATITUDE, EMPTY_DOUBLE));
        user.setUserLongitude(getDouble(sharedPreferences, Constants.UserStore.LONGITUDE, EMPTY_DOUBLE));
        user.setUserCompany(sharedPreferences.getString(Constants.UserStore.USER_COMPANY, EMPTY));
        user.setUserMobileNumber(sharedPreferences.getString(Constants.UserStore.USER_MOBILE_NUMBER, EMPTY));
        user.setUserName(sharedPreferences.getString(Constants.UserStore.USER_NAME, EMPTY));
        user.setUserPassword(sharedPreferences.getString(Constants.UserStore.USER_PASSWORD, EMPTY));
        user.setCompanyLatitude(getDouble(sharedPreferences, Constants.UserStore.COMPANY_LATITUDE, EMPTY_DOUBLE));
        user.setCompanyLongitude(getDouble(sharedPreferences, Constants.UserStore.COMPANY_LONGITUDE, EMPTY_DOUBLE));
        user.setCompanyRadius(getDouble(sharedPreferences,Constants.UserStore.COMPANY_RADIUS, Constants.DEFAULT_RADIUS));
        user.setRoster_id(sharedPreferences.getLong(Constants.UserStore.ROSTER_ID, Constants.EMPTY_LONG));
        user.setImeiNumber(sharedPreferences.getString(Constants.UserStore.IMEI_NUMBER, Constants.EMPTY));
        return user;
    }

    public void saveCompanyId(@NonNull Long id) {
        editor = sharedPreferences.edit();
        editor.putLong(Constants.UserStore.COMPANY_ID, id);
        editor.apply();
    }

    public void saveId(@NonNull Long id) {
        editor = sharedPreferences.edit();
        editor.putLong(Constants.UserStore.USER_ID, id);
        editor.apply();
    }

    public void saveAuthToken(@NonNull String authToken) {
        editor = sharedPreferences.edit();
        editor.putString(Constants.UserStore.AUTH_TOKEN, authToken);
        editor.apply();
    }

    public void saveIsAdmin(@NonNull boolean isAdmin) {
        editor = sharedPreferences.edit();
        editor.putBoolean(Constants.UserStore.IS_ADMIN, isAdmin);
        editor.apply();
    }

    public void saveRegistrationCode(@NonNull String registrationKey) {
        editor = sharedPreferences.edit();
        editor.putString(Constants.UserStore.USER_REGISTRATION_KEY, registrationKey);
        editor.apply();
    }

    public void saveUserEmail(@NonNull String userEmail) {
        editor = sharedPreferences.edit();
        editor.putString(Constants.UserStore.USER_EMAIL, userEmail);
        editor.apply();
    }

    public void saveLatitude(@NonNull Double latitude) {
        editor = sharedPreferences.edit();
        putDouble(editor, Constants.UserStore.LATITUDE, latitude);
        editor.apply();
    }

    public void saveLongitude(@NonNull Double longitude) {
        editor = sharedPreferences.edit();
        putDouble(editor, Constants.UserStore.LONGITUDE, longitude);
        editor.apply();
    }

    public void saveUserMobileNumber(@NonNull String mobileNumber) {
        editor = sharedPreferences.edit();
        editor.putString(Constants.UserStore.USER_MOBILE_NUMBER, mobileNumber);
        editor.apply();
    }

    public void saveUserName(@NonNull String userName) {
        editor = sharedPreferences.edit();
        editor.putString(Constants.UserStore.USER_NAME, userName);
        editor.apply();
    }

    public void saveUserCompany(@NonNull String userCompany) {
        editor = sharedPreferences.edit();
        editor.putString(Constants.UserStore.USER_COMPANY, userCompany);
        editor.apply();
    }

    public void saveCompanyLatitude(@NonNull Double latitude) {
        editor = sharedPreferences.edit();
        putDouble(editor, Constants.UserStore.COMPANY_LATITUDE, latitude);
        editor.apply();
    }

    public void saveCompanyLongitude(@NonNull Double longitude) {
        editor = sharedPreferences.edit();
        putDouble(editor, Constants.UserStore.COMPANY_LONGITUDE, longitude);
        editor.apply();
    }

    public void saveCompanyRadius(@NonNull double companyRadius) {
        editor = sharedPreferences.edit();
        putDouble(editor, Constants.UserStore.COMPANY_RADIUS, companyRadius);
        editor.apply();
    }

    public void saveRosterId(@NonNull long rosterId) {
        editor = sharedPreferences.edit();
        editor.putLong(Constants.UserStore.ROSTER_ID, rosterId);
        editor.apply();
    }

    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    public long getCompanyId() {
        return sharedPreferences.getLong(Constants.UserStore.COMPANY_ID, 0);
    }

    public void saveIMEINumber(@NonNull String imei) {
        editor = sharedPreferences.edit();
        editor.putString(Constants.UserStore.IMEI_NUMBER, imei);
        editor.apply();
    }

    public String getIMEINumber() {
        return sharedPreferences.getString(Constants.UserStore.IMEI_NUMBER, Constants.EMPTY);
    }

    public void logoutUser(@NonNull Context context) {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(context, "You've been signed out", Toast.LENGTH_SHORT).show();
    }
}
