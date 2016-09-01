package com.skybee.tracker.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
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
import com.skybee.tracker.ui.adapters.UserCardAdapter;
import com.skybee.tracker.ui.dialog.ErrorDialog;
import com.skybee.tracker.ui.fragments.Map;
import com.skybee.tracker.ui.fragments.Profile;
import com.skybee.tracker.ui.fragments.Setting;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private UserStore userStore;

    private final String TAG = this.getClass().getSimpleName();
    private ErrorDialog errorDialog;
    private ProgressDialog progressDialog;
    private String message;
    private RecyclerView employeeCards;
    private FloatingActionButton floatingActionButton;
    private LinearLayoutManager linearLayoutManager;
    private List<User> employeeCardList;
    private UserCardAdapter employeeCardAdapter;
    private List<RosterPojo> roasterCardList;
    private RosterAdapter rosterAdapter;
    private User user;
    private TextView userName;
    private TextView userEmail;
    private TextView noResultFound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        noResultFound = (TextView) findViewById(R.id.no_result_text);
        noResultFound.setVisibility(View.INVISIBLE);

        userStore = new UserStore(getApplicationContext());
        user=userStore.getUserDetails();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.setSelected(true);

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

        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        floatingActionButton=(FloatingActionButton)findViewById(R.id.shareRegistrationCode);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.shareRegistrationCode(getContext(),user.getRegistrationCode());
            }
        });
        employeeCards = (RecyclerView) findViewById(R.id.employee_list);
        employeeCards.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        employeeCards.setLayoutManager(linearLayoutManager);
        employeeCardList = new ArrayList<User>();
        employeeCardAdapter = new UserCardAdapter(employeeCardList);
        employeeCards.setAdapter(employeeCardAdapter);
        getEmployeeList();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            getContext().startActivities(new Intent[]{new Intent(getContext(), MainActivity.class)});
            ((Activity)getContext()).finish();
        } else if (id == R.id.nav_map) {
            fragment=new Map();
            openFragment(fragment);
        } else if (id == R.id.nav_profile) {
            fragment=new Profile();
            openFragment(fragment);
        } else if (id == R.id.nav_settings) {
            fragment=new Setting();
            openFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openFragment(@NonNull final Fragment fragment) {
        fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void getEmployeeList() {
        ListenableFuture<JSONObject> getEmployees = Factory.getUserService().employeeList(API.EMPLOYEE_LIST, user);
        Futures.addCallback(getEmployees, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d(TAG, result.toString());
                try {
                    if (result.has(Constants.JsonConstants.ERROR)) {
                        if (result.has(Constants.JsonConstants.MESSAGE) && !result.isNull(Constants.JsonConstants.MESSAGE)) {
                            message = result.getString(Constants.JsonConstants.MESSAGE);
                            Utility.checkProgressDialog(progressDialog);
                            errorDialog = new ErrorDialog(getContext(), message);
                            errorDialog.show();
                        }
                    }
                    if (result.has(Constants.JsonConstants.DATA)) {
                        JSONArray resultEmployeeList = new JSONArray();
                        resultEmployeeList = result.getJSONArray(Constants.JsonConstants.DATA);
                        for (int i = 0; i < resultEmployeeList.length(); i++) {
                            JSONObject employeeJsonObject = resultEmployeeList.getJSONObject(i);
                            if (employeeJsonObject != null) {
                                User user = new User();
                                if (employeeJsonObject.has(Constants.JsonConstants.EMAIL))
                                    user.setUserEmail(employeeJsonObject.getString(Constants.JsonConstants.EMAIL));
                                if (employeeJsonObject.has(Constants.JsonConstants.API_TOKEN))
                                    user.setAuthToken(employeeJsonObject.getString(Constants.JsonConstants.API_TOKEN));
                                if (employeeJsonObject.has(Constants.JsonConstants.NAME))
                                    user.setUserName(employeeJsonObject.getString(Constants.JsonConstants.NAME));
                                if (employeeJsonObject.has(Constants.JsonConstants.EMPLOYEE_ID))
                                    user.setId(employeeJsonObject.getLong(Constants.JsonConstants.EMPLOYEE_ID));
                                if (employeeJsonObject.has(Constants.JsonConstants.MOBILE))
                                    user.setUserMobileNumber(employeeJsonObject.getString(Constants.JsonConstants.MOBILE));
                                if (employeeJsonObject.has(Constants.JsonConstants.BRANCH_ID))
                                    user.setBranchId(employeeJsonObject.getLong(Constants.JsonConstants.BRANCH_ID));
                                employeeCardList.add(user);
                            }
                        }
                    }
                } catch (Exception e) {
                    Utility.checkProgressDialog(progressDialog);
                    message = Constants.ERROR_OCCURRED;
                    Utility.showErrorDialog(getContext(), message);
                }
                finally {
                    if (employeeCardList.size()<=0)
                        noResultFound.setVisibility(View.VISIBLE);
                }
                employeeCardAdapter.notifyItemInserted(employeeCardList.size() - 1);
                Utility.checkProgressDialog(progressDialog);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Error!");
                progressDialog.dismiss();
                if (t != null) {
                    if (t.getMessage() != null) {
                        message = t.getMessage();
                    } else {
                        message = Constants.ERROR_OCCURRED;
                    }
                } else {
                    message = Constants.ERROR_OCCURRED;
                }
                Utility.checkProgressDialog(progressDialog);
                Utility.showErrorDialog(getContext(), message);
            }
        }, ExecutorUtils.getUIThread());
    }

}
