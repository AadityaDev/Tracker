package com.skybee.tracker.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skybee.tracker.AndroidApplication;
import com.skybee.tracker.GPSTracker;
import com.skybee.tracker.R;
import com.skybee.tracker.model.User;
import com.skybee.tracker.preferences.UserStore;

public class BaseFragment<T> extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    protected Context context;
    protected AndroidApplication application;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private User user;
    private GPSTracker gpsTracker;
    private double latitude = 0;
    private double longitude = 0;
    private TextView selectAll;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context=getActivity();
        UserStore userStore=new UserStore(context);
        user=new User();
        user=userStore.getUserDetails();
        super.onCreate(savedInstanceState);
        gpsTracker=new GPSTracker(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        if(gpsTracker.canGetLocation()){
            latitude=gpsTracker.getLatitude();
            longitude=gpsTracker.getLongitude();
        }else {
            gpsTracker.showSettingsAlert();
        }
        Log.d(TAG,"Location :"+latitude+" Long: "+longitude);
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

    public String getTAG(){
        return TAG;
    }

    public Context getContext(){
        return context;
    }

    public User getLocalUser(){return user;}



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
