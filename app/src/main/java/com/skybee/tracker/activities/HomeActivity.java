package com.skybee.tracker.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.skybee.tracker.Factory;
import com.skybee.tracker.R;
import com.skybee.tracker.Utility;
import com.skybee.tracker.constants.API;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.core.BaseActivity;
import com.skybee.tracker.model.RosterPojo;
import com.skybee.tracker.model.User;
import com.skybee.tracker.network.ExecutorUtils;
import com.skybee.tracker.preferences.UserStore;
import com.skybee.tracker.ui.adapters.RosterAdapter;
import com.skybee.tracker.ui.dialog.ErrorDialog;
import com.skybee.tracker.ui.fragments.AdminFeed;
import com.skybee.tracker.ui.fragments.Map;
import com.skybee.tracker.ui.fragments.Profile;
import com.skybee.tracker.ui.fragments.Roster;
import com.skybee.tracker.ui.fragments.Setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();
    private ErrorDialog errorDialog;
    private ProgressDialog progressDialog;
    private String message;
    private User user;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userStore = new UserStore(getApplicationContext());
        user = userStore.getUserDetails();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        //drawer header
        View headerView=navigationView.getHeaderView(0);
        userEmail=(TextView)headerView.findViewById(R.id.user_email);
        userName=(TextView)headerView.findViewById(R.id.user_name);
        if(user!=null){
            if(!TextUtils.isEmpty(user.getUserName())){
                userName.setText(user.getUserName());
            }
            if(!TextUtils.isEmpty(user.getUserEmail())){
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
            ((Activity)getContext()).finish();
        } else if (id == R.id.nav_map) {
            fragment = new Map();
            openFragment(fragment);
        } else if (id == R.id.nav_date) {
            fragment = new Roster();
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
                } catch (JSONException jsonException) {
                    Log.d(TAG, Constants.Exception.JSON_EXCEPTION);
                    Utility.checkProgressDialog(progressDialog);
                } catch (Exception exception) {
                    Log.d(TAG, Constants.Exception.EXCEPTION);
                    Utility.checkProgressDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utility.checkProgressDialog(progressDialog);
                errorMessage = Constants.ERROR_OCCURRED;
                Utility.showErrorDialog(getApplicationContext(), errorMessage);
            }
        }, ExecutorUtils.getUIThread());
    }

}
