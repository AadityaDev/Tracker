package com.skybee.tracker.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.skybee.tracker.core.BaseFragment;
import com.skybee.tracker.model.RosterPojo;
import com.skybee.tracker.model.User;
import com.skybee.tracker.network.ExecutorUtils;
import com.skybee.tracker.ui.adapters.RosterAdapter;
import com.skybee.tracker.ui.adapters.UserCardAdapter;
import com.skybee.tracker.ui.dialog.ErrorDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminFeed extends BaseFragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_feed, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.shareRegistrationCode);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.shareRegistrationCode(getContext(),getLocalUser().getRegistrationCode());
            }
        });
        employeeCards = (RecyclerView) view.findViewById(R.id.employee_list);
        employeeCards.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        employeeCards.setLayoutManager(linearLayoutManager);
        employeeCardList = new ArrayList<User>();
        employeeCardAdapter = new UserCardAdapter(employeeCardList);
        employeeCards.setAdapter(employeeCardAdapter);
        getEmployeeList();
        return view;
    }


    public void getEmployeeList() {
        ListenableFuture<JSONObject> getEmployees = Factory.getUserService().employeeList(API.EMPLOYEE_LIST, getLocalUser());
        Futures.addCallback(getEmployees, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d(TAG, result.toString());
                try {
                    if (result.has(Constants.JsonConstants.ERROR)) {
                        if (result.has(Constants.JsonConstants.MESSAGE) && !result.isNull(Constants.JsonConstants.MESSAGE)) {
                            message = result.getString(Constants.JsonConstants.MESSAGE);
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
                    progressDialog.dismiss();
                    message = Constants.ERROR_OCCURRED;
                    Utility.showErrorDialog(getContext(), message);
                }
                employeeCardAdapter.notifyItemInserted(employeeCardList.size() - 1);
                progressDialog.dismiss();
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
                Utility.showErrorDialog(getContext(), message);
            }
        }, ExecutorUtils.getUIThread());
    }

    public void getRoasterList() {
        ListenableFuture<JSONObject> getRoaster = Factory.getUserService().roasterList(API.ROSTER_LIST, getLocalUser());
        Futures.addCallback(getRoaster, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (result.has(Constants.JsonConstants.DATA)) {
                        JSONArray resultRosterList = new JSONArray();
                        resultRosterList = result.getJSONArray(Constants.JsonConstants.DATA);
                        for (int i = 0; i < resultRosterList.length(); i++) {
                            JSONObject roasterJsonObject = resultRosterList.getJSONObject(i);
                            if (roasterJsonObject != null) {
                                Gson gson=new Gson();
                                final RosterPojo rosterPojo =gson.fromJson(roasterJsonObject.toString(),RosterPojo.class);
                                if(rosterPojo !=null){
                                    roasterCardList.add(rosterPojo);
                                }
                            }
                        }
                        if(roasterCardList.size()>=1)
                            rosterAdapter.notifyItemInserted(roasterCardList.size()-1);
                    }
                } catch (JSONException jsonException) {
                    Log.d(getTAG(), Constants.Exception.JSON_EXCEPTION);
                } catch (Exception exception) {
                    Log.d(getTAG(), Constants.Exception.EXCEPTION);
                }

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
                Utility.showErrorDialog(getContext(), message);
            }
        }, ExecutorUtils.getUIThread());
    }
}
