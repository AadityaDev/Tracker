package com.skybee.tracker.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.skybee.tracker.constants.Constants;

public class LocationServiceStore {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public LocationServiceStore(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.LOCATION_SERVICE_STORE, Constants.UserStore.PRIVATE_MODE);
    }

    public void saveIsBatteryOk(@NonNull boolean isBatteryOk) {
        editor = sharedPreferences.edit();
        editor.putBoolean(Constants.BACKGROUND_SERVICE_BATTERY_CONTROL, isBatteryOk);
        editor.apply();
    }

    public boolean getBatteryStatus() {
        return sharedPreferences.getBoolean(Constants.BACKGROUND_SERVICE_BATTERY_CONTROL, true);
    }

    public void isUserOutsideRange(@NonNull boolean isUserOutside) {
        editor = sharedPreferences.edit();
        editor.putBoolean(Constants.IS_BOUNDARY_CROSSED, isUserOutside);
        editor.apply();
    }

    public boolean getIsUserOutsideRange() {
        return sharedPreferences.getBoolean(Constants.IS_BOUNDARY_CROSSED, false);
    }
}
