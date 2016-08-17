package com.skybee.tracker.core;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.skybee.tracker.AndroidApplication;

public class BaseActivity<T> extends FragmentActivity implements View.OnClickListener{

    protected Context context;
    protected AndroidApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context=this;
        application=(AndroidApplication)getApplicationContext();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {

    }

    public Context getContext(){
        return context;
    }
}
