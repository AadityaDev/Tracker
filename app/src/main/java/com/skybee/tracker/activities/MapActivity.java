package com.skybee.tracker.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skybee.tracker.GPSTracker;
import com.skybee.tracker.R;
import com.skybee.tracker.Utility;
import com.skybee.tracker.core.BaseActivity;

public class MapActivity extends BaseActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private String title;
    private static View view;
    private Marker myLocation;
    private GPSTracker gpsTracker;
    private double latitude = 0;
    private double longitude = 0;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bundle=getIntent().getExtras();
        if(bundle!=null){
            latitude=bundle.getDouble("Lat");
            longitude=bundle.getDouble("Long");
            title=bundle.getString("company");
        }
        if((getSupportActionBar()!=null)&&(!Utility.isStringNullOrEmpty(title))){
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        } else {
            mMap=googleMap;
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            LatLng latLng=new LatLng(latitude,longitude);
            myLocation=mMap.addMarker(new MarkerOptions().position(latLng).title(title));
            myLocation.setTag(0);
            CameraUpdate myLocation= CameraUpdateFactory.newLatLngZoom(latLng,14);
            mMap.setOnMarkerClickListener(this);
            mMap.animateCamera(myLocation);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
