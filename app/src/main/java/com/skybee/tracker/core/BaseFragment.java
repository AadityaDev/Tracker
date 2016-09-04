package com.skybee.tracker.core;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.skybee.tracker.AndroidApplication;
import com.skybee.tracker.R;
import com.skybee.tracker.model.User;
import com.skybee.tracker.preferences.UserStore;

import io.fabric.sdk.android.Fabric;

public class BaseFragment<T> extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    protected Context context;
    protected AndroidApplication application;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private User user;
    private TextView selectAll;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getActivity();
        UserStore userStore = new UserStore(context);
        user = new User();
        user = userStore.getUserDetails();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String getTAG() {
        return TAG;
    }

    public Context getContext() {
        return context;
    }

    public User getLocalUser() {
        return user;
    }

}
