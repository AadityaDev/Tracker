package com.skybee.tracker.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skybee.tracker.GPSTracker;
import com.skybee.tracker.R;
import com.skybee.tracker.Utility;
import com.skybee.tracker.activities.LoginActivity;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.core.BaseFragment;
import com.skybee.tracker.model.AttendancePojo;
import com.skybee.tracker.model.User;
import com.skybee.tracker.preferences.UserStore;

public class Profile extends BaseFragment {

    private RelativeLayout logoutButton;
    private ImageView userImage;
    private TextView userName;
    private TextView userCompany;
    private TextView userLocation;
    private TextView userEmail;
    private TextView userMobileNumber;
    private TextView userImageText;
    private ImageView editName;
    private User user;
    private UserStore userStore;
    private GPSTracker gpsTracker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userStore = new UserStore(context);
        gpsTracker = new GPSTracker(context);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userImage = (ImageView) view.findViewById(R.id.user_image);
        userName = (TextView) view.findViewById(R.id.user_name);
        userCompany = (TextView) view.findViewById(R.id.user_company);
//        userLocation = (TextView) view.findViewById(R.id.location_text);
        userEmail = (TextView) view.findViewById(R.id.email_text);
        userMobileNumber = (TextView) view.findViewById(R.id.mobile_num_text);
        userImageText=(TextView)view.findViewById(R.id.user_image_text);
        logoutButton = (RelativeLayout) view.findViewById(R.id.logout_section);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
//        editName=(ImageView)view.findViewById(R.id.edit_name);
//        editName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userName.setCursorVisible(true);
//                userName.setFocusableInTouchMode(true);
//                userName.setInputType(InputType.TYPE_CLASS_TEXT);
//                userName.requestFocus();
//            }
//        });
        user=getLocalUser();
        if(user!=null){
            if(!TextUtils.isEmpty(user.getUserName())) {
                userName.setText(user.getUserName());
                userImageText.setText(String.valueOf(user.getUserName().charAt(0)));
            }
            if(user.getUserEmail()!=null)
                userEmail.setText(user.getUserEmail());
            if(user.getUserMobileNumber()!=null)
                userMobileNumber.setText(user.getUserMobileNumber());
            if(!TextUtils.isEmpty(user.getUserCompany()))
                userCompany.setText(user.getUserCompany());
            else
                userCompany.setText("");
        }
        return view;
    }


    public void logout() {
        AttendancePojo attendancePojo = new AttendancePojo();
        attendancePojo.setLongitude(userStore.getUserDetails().getUserLongitude());
        attendancePojo.setLattitude(userStore.getUserDetails().getUserLatitude());
        attendancePojo.setCustomer_site_id(userStore.getCompanyId());
        attendancePojo.setLoginStatus(Constants.LOGIN_STATUS.LOGOUT);
        Utility.saveNotPresent(context, attendancePojo);
        userStore.logoutUser(getContext());
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ((Activity) getContext()).finish();
    }
}
