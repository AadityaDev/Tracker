package com.skybee.tracker.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

import com.skybee.tracker.preferences.LocationServiceStore;
import com.skybee.tracker.receiver.PeriodicTaskReceiver;

public class BackgroundService extends Service {

    private static final String TAG = "BackgroundService";

    PeriodicTaskReceiver mPeriodicTaskReceiver = new PeriodicTaskReceiver();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationServiceStore locationServiceStore = new LocationServiceStore(getApplicationContext());
        IntentFilter batteryStatusIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = registerReceiver(null, batteryStatusIntentFilter);
        Log.d(TAG, "Background Service Started");
        if (batteryStatusIntent != null) {
            int level = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPercentage = level / (float) scale;
            float lowBatteryPercentageLevel = 0.14f;
            try {
                int lowBatteryLevel = Resources.getSystem().getInteger(Resources.getSystem().getIdentifier("config_lowBatteryWarningLevel", "integer", "android"));
                lowBatteryPercentageLevel = lowBatteryLevel / (float) scale;
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Missing low battery threshold resource");
            }
            locationServiceStore.saveIsBatteryOk(batteryPercentage >= lowBatteryPercentageLevel);
        } else {
            locationServiceStore.saveIsBatteryOk(true);
        }
        mPeriodicTaskReceiver.restartPeriodicTaskHeartBeat(BackgroundService.this);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
