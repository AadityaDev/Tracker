package com.skybee.tracker.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.SystemClock;
import android.util.Log;

import com.google.common.base.Strings;
import com.skybee.tracker.GPSTracker;
import com.skybee.tracker.Utility;
import com.skybee.tracker.activities.HomeActivity;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.model.AttendancePojo;
import com.skybee.tracker.preferences.LocationServiceStore;
import com.skybee.tracker.preferences.UserStore;

public class PeriodicTaskReceiver extends BroadcastReceiver {

    private static final String TAG = "PeriodicTaskReceiver";
    private GPSTracker gpsTracker;
    private UserStore userStore;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Strings.isNullOrEmpty(intent.getAction())) {
            LocationServiceStore sharedPreferences = new LocationServiceStore(context);
            HomeActivity homeActivity = new HomeActivity();
            if (intent.getAction().equals("android.intent.action.BATTERY_LOW")) {
                sharedPreferences.saveIsBatteryOk(false);
                stopPeriodicTaskHeartBeat(context);
            } else if (intent.getAction().equals("android.intent.action.BATTERY_OKAY")) {
                sharedPreferences.saveIsBatteryOk(true);
                restartPeriodicTaskHeartBeat(context);
            } else if (intent.getAction().equals(Constants.INTENT_ACTION)) {
                doPeriodicTask(context, homeActivity);
            }
        }
    }

    private void doPeriodicTask(Context context, HomeActivity myApplication) {
        Log.d(TAG, "Repeat period task");
        userStore = new UserStore(context);
        gpsTracker = new GPSTracker(context);
        if (gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0) {
            userStore.saveLatitude(gpsTracker.getLatitude());
            userStore.saveLongitude(gpsTracker.getLongitude());
        }
        if (userStore.getUserDetails().getCompanyLatitude() != 0 && userStore.getUserDetails().getCompanyLongitude() != 0 && userStore.getUserDetails() != null) {
            float[] results = new float[1];
            Location.distanceBetween(userStore.getUserDetails().getCompanyLatitude(), userStore.getUserDetails().getCompanyLongitude(),
                    userStore.getUserDetails().getCompanyLatitude(), userStore.getUserDetails().getCompanyLongitude(), results);
            float distanceInMeters = results[0];
            boolean isWithinRange = distanceInMeters < userStore.getUserDetails().getCompanyRadius();
            if (!isWithinRange) {
                AttendancePojo attendancePojo = new AttendancePojo();
                attendancePojo.setLongitude(userStore.getUserDetails().getUserLongitude());
                attendancePojo.setLattitude(userStore.getUserDetails().getCompanyLongitude());
                attendancePojo.setCustomer_site_id(userStore.getCompanyId());
                Utility.saveNotPresent(context, attendancePojo);
            }
        }
    }

    public void restartPeriodicTaskHeartBeat(Context context) {
        LocationServiceStore locationServiceStore = new LocationServiceStore(context);
        boolean isBatteryOk = locationServiceStore.getBatteryStatus();
        Intent alarmIntent = new Intent(context, PeriodicTaskReceiver.class);
        boolean isAlarmUp = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null;

        if (isBatteryOk && !isAlarmUp) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmIntent.setAction(Constants.INTENT_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1 * 60 * 1000, pendingIntent);
        }
    }

    public void stopPeriodicTaskHeartBeat(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, PeriodicTaskReceiver.class);
        alarmIntent.setAction(Constants.INTENT_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
