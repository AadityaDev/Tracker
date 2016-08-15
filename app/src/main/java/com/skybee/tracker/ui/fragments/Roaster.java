package com.skybee.tracker.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skybee.tracker.R;
import com.skybee.tracker.core.BaseFragment;
import com.skybee.tracker.model.RoasterPojo;
import com.skybee.tracker.model.User;
import com.skybee.tracker.preferences.UserStore;
import com.skybee.tracker.ui.adapters.RoasterAdapter;
import com.skybee.tracker.ui.adapters.UserCardAdapter;

import java.util.List;

public class Roaster extends BaseFragment {

    private RecyclerView roasterCards;
    private LinearLayoutManager linearLayoutManager;
    private List<RoasterPojo> roasterCardList;
    private RoasterAdapter roasterAdapter;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UserStore userStore = new UserStore(getContext());
        user = new User();
        user = userStore.getUserDetails();
        View view = inflater.inflate(R.layout.fragment_roaster, container, false);
        return view;
    }
}
