package com.skybee.tracker;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class AndroidApplication extends Application{

    private SharedPreferences userStore;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getApplicationContext());
        Fabric.with(getApplicationContext(), new Crashlytics());
    }

    public SharedPreferences getUserStore(){
        if (userStore == null)
        {
            setPreferences();
        }
        return userStore;
    }

    public void setPreferences(){
        userStore=getSharedPreferences(this.getClass().getName(),MODE_PRIVATE);
    }
}
