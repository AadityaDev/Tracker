package com.skybee.tracker.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    private TextView userImageText;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        userName = (TextView) view.findViewById(R.id.user_name);
        userEmail = (TextView) view.findViewById(R.id.user_email);
        userImageText = (TextView) view.findViewById(R.id.user_image_text);
        logoutButton = (RelativeLayout) view.findViewById(R.id.logout_section);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        user = getLocalUser();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getUserName())) {
                userName.setText(user.getUserName());
                userImageText.setText(String.valueOf(user.getUserName().charAt(0)));
            }
            if (user.getUserEmail() != null)
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
        ((Activity)getContext()).finish();
    }

}
