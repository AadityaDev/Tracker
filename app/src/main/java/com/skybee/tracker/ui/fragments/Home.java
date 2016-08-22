package com.skybee.tracker.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.skybee.tracker.model.TimeCard;
import com.skybee.tracker.model.User;
import com.skybee.tracker.network.ExecutorUtils;
import com.skybee.tracker.preferences.UserStore;
import com.skybee.tracker.ui.adapters.RosterAdapter;
import com.skybee.tracker.ui.adapters.TimeCardAdapter;
import com.skybee.tracker.ui.customview.ItemClickSupport;
import com.skybee.tracker.ui.customview.StepperIndicator;
import com.skybee.tracker.ui.dialog.ErrorDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Home extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ErrorDialog errorDialog;
    private String errorMessage;
    private ProgressDialog progressDialog;
    private StepperIndicator stepperIndicator;
    private TextView checkInText;
    private TextView lunchText;
    private TextView checkOutText;
    private RecyclerView timeCards;
    private List<RosterPojo> roasterCardList;
    private LinearLayoutManager linearLayoutManager;
    private RosterAdapter rosterAdapter;
    private int progress = 1;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UserStore userStore = new UserStore(getContext());
        user = new User();
        user = userStore.getUserDetails();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressDialog = ProgressDialog.show(getContext(), "", "Loading...", true);
        progressDialog.show();
        stepperIndicator = (StepperIndicator) view.findViewById(R.id.stepper_indicator);
        checkInText = (TextView) view.findViewById(R.id.check_in_heading);
        checkInText.setText(Constants.HeadingText.CHECK_IN);
        lunchText = (TextView) view.findViewById(R.id.lunch_heading);
        lunchText.setText(Constants.HeadingText.CHECK_LUNCH);
        checkOutText = (TextView) view.findViewById(R.id.check_out_heading);
        checkOutText.setText(Constants.HeadingText.CHECK_OUT);
        timeCards = (RecyclerView) view.findViewById(R.id.time_list);
        timeCards.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        timeCards.setLayoutManager(linearLayoutManager);
        roasterCardList = new ArrayList<>();
        rosterAdapter = new RosterAdapter(roasterCardList,Constants.isSiteCard);
        timeCards.setAdapter(rosterAdapter);
        getCustomerSite();
//        addTimeCard();
//        ItemClickSupport.addTo(timeCards).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                if (timeCardList.get(position).getEvent() == null || timeCardList.get(position).getEvent().equals(Constants.HeadingText.EMPTY_CARD)) {
//                    if (timeCardList.size() <= Constants.NUMBER_OF_CARDS) {
//                        Utility.setEventType(timeCardList.get(position), timeCardList.size());
//                        timeCardList.get(position).setTime(new Date());
//                        timeCardAdapter.notifyDataSetChanged();
//                        stepperIndicator.setCurrentStep(progress);
//                        progress++;
//                        if (timeCardList.size() < Constants.NUMBER_OF_CARDS) {
//                            addTimeCard();
//                        }
//                    }
//                }
//            }
//        });
        return view;
    }

//    public void addTimeCard() {
//        TimeCard timeCard = new TimeCard();
//        timeCard.setTime(new Date());
//        timeCardList.add(timeCard);
//    }

    public void getCustomerSite(){
        ListenableFuture<JSONObject> getCustomerSites = Factory.getUserService().customerSites(API.CUSTOMER_SITES, user);
        Futures.addCallback(getCustomerSites, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try{
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
