package com.skybee.tracker;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import butterknife.ButterKnife;

public class AndroidApplication extends Application{

    private SharedPreferences userStore;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
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
