package com.skybee.tracker.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
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
import com.skybee.tracker.service.GeofenceErrorMessages;
import com.skybee.tracker.service.GeofenceTransitionsIntentService;
import com.skybee.tracker.ui.adapters.RosterAdapter;
import com.skybee.tracker.ui.dialog.ErrorDialog;
import com.skybee.tracker.ui.fragments.Map;
import com.skybee.tracker.ui.fragments.Profile;
import com.skybee.tracker.ui.fragments.Roasters;
import com.skybee.tracker.ui.fragments.Setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private GPSTracker gpsTracker;

    private final String TAG = this.getClass().getSimpleName();
    private ErrorDialog errorDialog;
    private ProgressDialog progressDialog;
    private String message;
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

    public static final HashMap<String, LatLng> LAT_LNG_HASH_MAP = new HashMap<String, LatLng>();

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * The list of geofences used in this sample.
     */
    protected ArrayList<Geofence> mGeofenceList;

    /**
     * Used to keep track of whether geofences were added.
     */
    private boolean mGeofencesAdded;

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    /**
     * Used to persist application state about whether geofences were added.
     */
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userStore = new UserStore(getApplicationContext());
        user = userStore.getUserDetails();

        gpsTracker = new GPSTracker(context);
        if (gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0) {
            userStore.saveLatitude(gpsTracker.getLatitude());
            userStore.saveLongitude(gpsTracker.getLongitude());
        }
        // Def
        LAT_LNG_HASH_MAP.put("LOCATION", new LatLng(user.getCompanyLatitude(), user.getCompanyLongitude()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(R.string.app_name);
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
        progressDialog.show();
        timeCards = (RecyclerView) findViewById(R.id.time_list);
        timeCards.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        timeCards.setLayoutManager(linearLayoutManager);
        roasterCardList = new ArrayList<>();
        rosterAdapter = new RosterAdapter(getApplicationContext(), roasterCardList, Constants.isSiteCard);
        timeCards.setAdapter(rosterAdapter);
        getCustomerSite();

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        // Initially set the PendingIntent used in addGeofences() and removeGeofences() to null.
        mGeofencePendingIntent = null;

        // Retrieve an instance of the SharedPreferences object.
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);

        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);

        // Get the geofences used. Geofence data is hard coded in this sample.
        populateGeofenceList();

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();

        Intent startServiceIntent = new Intent(context, BackgroundService.class);
        startService(startServiceIntent);
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            getContext().startActivities(new Intent[]{new Intent(getContext(), HomeActivity.class)});
            ((Activity) getContext()).finish();
        } else if (id == R.id.nav_map) {
            fragment = new Map();
            openFragment(fragment);
        } else if (id == R.id.nav_date) {
            fragment = new Roasters();
            openFragment(fragment);
        } else if (id == R.id.nav_profile) {
            fragment = new Profile();
            openFragment(fragment);
        } else if (id == R.id.nav_settings) {
            fragment = new Setting();
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
                                if (rosterPojo != null) {
                                    roasterCardList.add(rosterPojo);
                                }
                            }
                        }
                        if (roasterCardList.size() >= 1)
                            rosterAdapter.notifyItemInserted(roasterCardList.size() - 1);
                        Utility.checkProgressDialog(progressDialog);
                    }
                    Utility.checkProgressDialog(progressDialog);
                } catch (JSONException jsonException) {
                    Log.d(TAG, Constants.Exception.JSON_EXCEPTION);
                    Utility.checkProgressDialog(progressDialog);
                } catch (Exception exception) {
                    Log.d(TAG, Constants.Exception.EXCEPTION);
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

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by addGeofences().
     */
    public void addGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    /**
     * Removes geofences, which stops further notifications when the device enters or exits
     * previously registered geofences.
     */
    public void removeGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    /**
     * Runs when the result of calling addGeofences() and removeGeofences() becomes available.
     * Either method can complete successfully or with an error.
     * <p>
     * Since this activity implements the {@link ResultCallback} interface, we are required to
     * define this method.
     *
     * @param status The Status returned through a PendingIntent when addGeofences() or
     *               removeGeofences() get called.
     */
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.apply();

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
//            setButtonsEnabledState();

            Toast.makeText(
                    this,
                    getString(mGeofencesAdded ? R.string.geofences_added :
                            R.string.geofences_removed),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void populateGeofenceList() {
        for (java.util.Map.Entry<String, LatLng> entry : LAT_LNG_HASH_MAP.entrySet()) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            user.getCompanyLatitude(),
                            user.getCompanyLongitude(),
                            user.getCompanyRadius()
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    /**
     * Ensures that only one button is enabled at any time. The Add Geofences button is enabled
     * if the user hasn't yet added geofences. The Remove Geofences button is enabled if the
     * user has added geofences.
     */
//    private void setButtonsEnabledState() {
//        if (mGeofencesAdded) {
//            mAddGeofencesButton.setEnabled(false);
//            mRemoveGeofencesButton.setEnabled(true);
//        } else {
//            mAddGeofencesButton.setEnabled(true);
//            mRemoveGeofencesButton.setEnabled(false);
//        }
//    }
}
