package com.skybee.tracker.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skybee.tracker.GPSTracker;
import com.skybee.tracker.LocationUtil;
import com.skybee.tracker.R;
import com.skybee.tracker.core.BaseFragment;

public class Map extends BaseFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static View view;
    private Marker myLocation;
    private GPSTracker gpsTracker;
    private double latitude = 0;
    private double longitude = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        gpsTracker=new GPSTracker(context);
        return view;
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        } else {
//            LocationUtil locationUtil=new LocationUtil(getContext());
//            locationUtil.showCurrentLocation();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getMaxZoomLevel();
            mMap.getMinZoomLevel();
            LatLng latLng=new LatLng(latitude,longitude);
            myLocation=mMap.addMarker(new MarkerOptions().position(latLng).title("Me"));
        }
    }

}
