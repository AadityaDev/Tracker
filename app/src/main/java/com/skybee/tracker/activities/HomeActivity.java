package com.skybee.tracker.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.skybee.tracker.Attendance;
import com.skybee.tracker.Factory;
import com.skybee.tracker.GPSTracker;
import com.skybee.tracker.R;
import com.skybee.tracker.Utility;
import com.skybee.tracker.constants.API;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.core.BaseActivity;
import com.skybee.tracker.model.RosterPojo;
import com.skybee.tracker.model.User;
import com.skybee.tracker.network.ExecutorUtils;
import com.skybee.tracker.preferences.UserStore;
import com.skybee.tracker.service.BackgroundService;
import com.skybee.tracker.ui.adapters.RosterAdapter;
import com.skybee.tracker.ui.dialog.ErrorDialog;
import com.skybee.tracker.ui.fragments.Map;
import com.skybee.tracker.ui.fragments.Profile;
import com.skybee.tracker.ui.fragments.Roasters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = this.getClass().getSimpleName();
    private GPSTracker gpsTracker;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private final int READ_LOCATION = 1;

    private ErrorDialog errorDialog;
    private ProgressDialog progressDialog;
    private String message;
    private final int REQUEST_IMEI = 0;
    private final int REQUEST_LOCATION = 1;
    private static User user;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private UserStore userStore;
    private String errorMessage;
    private RecyclerView timeCards;
    private List<RosterPojo> roasterCardList;
    private LinearLayoutManager linearLayoutManager;
    private RosterAdapter rosterAdapter;
    private TextView userName;
    private TextView userEmail;
    private TextView noResultFound;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_home);

        userStore = new UserStore(getApplicationContext());
        user = userStore.getUserDetails();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        gpsTracker=new GPSTracker(HomeActivity.this);
        Utility.showSnackBar(context, coordinatorLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Mark Attendance");
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        noResultFound = (TextView) findViewById(R.id.no_result_text);
        noResultFound.setVisibility(View.INVISIBLE);
        //drawer header
        View headerView = navigationView.getHeaderView(0);
        userEmail = (TextView) headerView.findViewById(R.id.user_email);
        userName = (TextView) headerView.findViewById(R.id.user_name);
        if (user != null) {
            if (!TextUtils.isEmpty(user.getUserName())) {
                userName.setText(user.getUserName());
            }
            if (!TextUtils.isEmpty(user.getUserEmail())) {
                userEmail.setText(user.getUserEmail());
            }
        }

        progressDialog = ProgressDialog.show(getContext(), "", "Loading...", true);
        Utility.showProgressDialog(progressDialog);
        timeCards = (RecyclerView) findViewById(R.id.time_list);
        timeCards.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        timeCards.setLayoutManager(linearLayoutManager);
        roasterCardList = new ArrayList<>();
        rosterAdapter = new RosterAdapter(getApplicationContext(), roasterCardList, Constants.isSiteCard);
        timeCards.setAdapter(rosterAdapter);
        getCustomerSite();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE
            }, REQUEST_LOCATION);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Provide permission to access location", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            if (TextUtils.isEmpty(user.getImeiNumber())) {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                }
            }
            Intent intent=new Intent(HomeActivity.this, BackgroundService.class);
            startService(intent);
        }

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_LOCATION:
                if (checkPermissionsAllowed(grantResults)) {
                    userStore.saveIMEINumber(Utility.getIMEINumberFromDevice(HomeActivity.this));
                    displayLocation();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Handle the camera action
            toolbar.setTitle("Mark Attendance");
            getContext().startActivities(new Intent[]{new Intent(getContext(), HomeActivity.class)});
            ((Activity) getContext()).finish();
        } else if (id == R.id.nav_map) {
            toolbar.setTitle("Map");
            fragment = new Map();
            openFragment(fragment);
        } else if (id == R.id.nav_date) {
            toolbar.setTitle("Rosters");
            fragment = new Roasters();
            openFragment(fragment);
        } else if (id == R.id.nav_profile) {
            toolbar.setTitle("Profile");
            fragment = new Profile();
            openFragment(fragment);
        }
//        else if (id == R.id.nav_settings) {
//            fragment = new Setting();
//            openFragment(fragment);
//        }
        else if (id == R.id.nav_attendance) {
            toolbar.setTitle("Attendance");
            fragment = new Attendance();
            openFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openFragment(@NonNull final Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void getCustomerSite() {
        Utility.showSnackBar(context, coordinatorLayout);
        ListenableFuture<JSONObject> getCustomerSites = Factory.getUserService().customerSites(API.CUSTOMER_SITES, user);
        Futures.addCallback(getCustomerSites, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (result.has(Constants.JsonConstants.DATA)) {
                        JSONArray resultRosterList = new JSONArray();
                        resultRosterList = result.getJSONArray(Constants.JsonConstants.DATA);
                        for (int i = 0; i < resultRosterList.length(); i++) {
                            JSONObject roasterJsonObject = resultRosterList.getJSONObject(i);
                            if (roasterJsonObject != null) {
                                Gson gson = new Gson();
                                final RosterPojo rosterPojo = gson.fromJson(roasterJsonObject.toString(), RosterPojo.class);
                                if (roasterJsonObject.has(Constants.JsonConstants.MARK_BUTTON_STATUS) && roasterJsonObject.getInt(Constants.JsonConstants.MARK_BUTTON_STATUS) == 1) {
                                    rosterPojo.setMark_btn_status(true);
                                }
                                if (roasterJsonObject.has(Constants.JsonConstants.OFF_BUTTON_STATUS) && roasterJsonObject.getInt(Constants.JsonConstants.OFF_BUTTON_STATUS) == 1) {
                                    rosterPojo.setOff_btn_status(true);
                                }
                                if (rosterPojo != null) {
                                    roasterCardList.add(rosterPojo);
                                }
                            }
                        }
                        rosterAdapter.notifyDataSetChanged();
                        Utility.checkProgressDialog(progressDialog);
                    }
                    Utility.checkProgressDialog(progressDialog);
                } catch (@NonNull JSONException jsonException) {
                    Log.d(TAG, Constants.Exception.JSON_EXCEPTION);
                    Utility.checkProgressDialog(progressDialog);
                } catch (@NonNull Exception exception) {
                    Log.d(TAG, Utility.isNullOrEmpty(exception.getMessage()) ? Constants.Exception.EXCEPTION : exception.getMessage());
                    Utility.checkProgressDialog(progressDialog);
                } finally {
                    Utility.checkProgressDialog(progressDialog);
                    if (roasterCardList.size() == 0) {
                        noResultFound.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utility.checkProgressDialog(progressDialog);
                errorMessage = Constants.ERROR_OCCURRED;
                Utility.showErrorDialog(HomeActivity.this, errorMessage);
            }
        }, ExecutorUtils.getUIThread());
    }

    private void displayLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                userStore.saveLatitude(latitude);
                userStore.saveLongitude(longitude);
            }
        } catch (SecurityException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    /**
     * Method to toggle periodic location updates
     */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
            mRequestingLocationUpdates = true;
            // Starting the location updates
            startLocationUpdates();
            Log.d(TAG, "Periodic location updates started!");
        } else {
            // Changing the button text
            mRequestingLocationUpdates = false;
            // Stopping the location updates
            stopLocationUpdates();
            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnected(@NonNull Bundle arg0) {
        displayLocation();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    public static boolean checkPermissionsAllowed(int[] request) {
        int numofPermissions = 0;
        if (request != null) {
            for (int i = 0; i < request.length; i++) {
                if (request[i] == PackageManager.PERMISSION_GRANTED) {
                    numofPermissions = numofPermissions + 1;
                }
            }
            if (numofPermissions == request.length) {
                return true;
            }
        }
        return false;
    }

}
