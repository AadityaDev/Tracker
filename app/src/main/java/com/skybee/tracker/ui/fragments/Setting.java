package com.skybee.tracker.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skybee.tracker.R;
import com.skybee.tracker.activities.LoginActivity;
import com.skybee.tracker.core.BaseFragment;
import com.skybee.tracker.model.User;
import com.skybee.tracker.preferences.UserStore;

public class Setting extends BaseFragment {

    private RelativeLayout logoutButton;
    private TextView userName;
    private TextView userEmail;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        userName = (TextView) view.findViewById(R.id.user_name);
        userEmail = (TextView) view.findViewById(R.id.user_email);
        logoutButton = (RelativeLayout) view.findViewById(R.id.logout_section);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        user=getLocalUser();
        if(user!=null){
            if(user.getUserName()!=null)
                userName.setText(user.getUserName());
            if(user.getUserEmail()!=null)
                userEmail.setText(user.getUserEmail());
        }
        return view;
    }

    public void logout() {
        UserStore userStore = new UserStore(getContext());
        userStore.logoutUser(getContext());
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
