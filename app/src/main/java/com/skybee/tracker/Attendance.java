package com.skybee.tracker;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.skybee.tracker.constants.API;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.core.BaseFragment;
import com.skybee.tracker.model.RosterPojo;
import com.skybee.tracker.model.User;
import com.skybee.tracker.network.ExecutorUtils;
import com.skybee.tracker.preferences.UserStore;
import com.skybee.tracker.ui.EndlessRecyclerViewScrollListener;
import com.skybee.tracker.ui.adapters.RosterAdapter;
import com.skybee.tracker.ui.dialog.ErrorDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Attendance extends BaseFragment {
    private ProgressDialog progressDialog;
    private ErrorDialog errorDialog;
    private String errorMessage;
    private RecyclerView roasterCards;
    private LinearLayoutManager linearLayoutManager;
    private List<RosterPojo> roasterCardList;
    private RosterAdapter rosterAdapter;
    private User user;
    private ActionBar actionBar;

    public Attendance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        UserStore userStore = new UserStore(getContext());
        user = new User();
        user = userStore.getUserDetails();
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        View toolbarView = (View) inflater.inflate(R.layout.app_bar_home, container, false);
        progressDialog = ProgressDialog.show(getContext(), "", "Loading...", true);
        progressDialog.show();
        roasterCards = (RecyclerView) view.findViewById(R.id.accepted_roaster_list);
        roasterCards.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        roasterCards.setLayoutManager(linearLayoutManager);
        roasterCardList = new ArrayList<>();
        rosterAdapter = new RosterAdapter(roasterCardList, false, true);
        roasterCards.setAdapter(rosterAdapter);
        getAcceptedRoasterList(Constants.PAGE_NUMBER, Constants.PAGE_SIZE);
        roasterCards.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                progressDialog.show();
                getAcceptedRoasterList(page, Constants.PAGE_SIZE);
            }
        });
        return view;
    }

    public void getAcceptedRoasterList(@NonNull int pageNumber, @NonNull int pageSize) {
        ListenableFuture<JSONObject> getRoaster = Factory.getUserService().roasterList(API.ATTENDANCE_LIST
                + Constants.QUESTION_MARK + Constants.PAGE_NUMER_TEXT + pageNumber
                + Constants.AND + Constants.PAGE_SIZE_TEXT + pageSize, user);
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
                    Log.d(getTAG(), Constants.Exception.JSON_EXCEPTION);
                    Utility.checkProgressDialog(progressDialog);
                } catch (Exception exception) {
                    Log.d(getTAG(), Constants.Exception.EXCEPTION);
                    Utility.checkProgressDialog(progressDialog);
                }
                Utility.checkProgressDialog(progressDialog);
            }

            @Override
            public void onFailure(Throwable t) {
                Utility.checkProgressDialog(progressDialog);
                errorMessage = Constants.ERROR_OCCURRED;
                Utility.showErrorDialog(context, errorMessage);
            }
        }, ExecutorUtils.getUIThread());
    }
}
