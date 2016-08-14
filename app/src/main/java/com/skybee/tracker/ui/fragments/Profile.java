package com.skybee.tracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skybee.tracker.R;
import com.skybee.tracker.core.BaseFragment;

public class Profile extends BaseFragment {

    private ImageView userImage;
    private TextView userName;
    private TextView userCompany;
    private TextView userLocation;
    private TextView userEmail;
    private TextView userMobileNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userImage = (ImageView) view.findViewById(R.id.user_image);
        userName = (TextView) view.findViewById(R.id.user_name);
        userCompany = (TextView) view.findViewById(R.id.user_company);
        userLocation = (TextView) view.findViewById(R.id.location_text);
        userEmail = (TextView) view.findViewById(R.id.email_text);
        userMobileNumber = (TextView) view.findViewById(R.id.mobile_num_text);
        return view;
    }

}
