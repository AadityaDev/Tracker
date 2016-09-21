package com.skybee.tracker.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.skybee.tracker.Factory;
import com.skybee.tracker.FileUtils;
import com.skybee.tracker.GPSTracker;
import com.skybee.tracker.R;
import com.skybee.tracker.Utility;
import com.skybee.tracker.activities.LoginActivity;
import com.skybee.tracker.constants.API;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.core.BaseFragment;
import com.skybee.tracker.model.AttendancePojo;
import com.skybee.tracker.model.User;
import com.skybee.tracker.network.ExecutorUtils;
import com.skybee.tracker.preferences.UserStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static android.app.Activity.RESULT_OK;

public class Profile extends BaseFragment {

    private final String TAG=this.getClass().getSimpleName();
    private static final int SELECT_PICTURE = 1;
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
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureChooser();
            }
        });
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

    private void showPictureChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            Log.d(TAG, "File Uri: " + uri.toString());
            // Get the path+
            String path = null;
            try {
                path = FileUtils.getPath(getContext(), uri);
                UserStore sessionStore = new UserStore(getContext());
                final File file = new File(new URI(uri.toString()));
                ListenableFuture<JSONObject> uploadResume = Factory.getUserService().updateUserDetails(sessionStore.getUserDetails(), API.UPDATE_PROFILE, file);
                Futures.addCallback(uploadResume, new FutureCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.d(TAG, "success");
                        try {
                                Toast.makeText(getContext(), "Picture updated", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i(TAG, "Error");
                    }
                }, ExecutorUtils.getUIThread());
            } catch (URISyntaxException e) {
                Log.d(TAG, "URI Syntax error");
            } catch (IllegalArgumentException e) {
                Log.d(TAG, "Illegal Argument");
                Toast.makeText(getContext(), "Wrong resume format", Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "File Path: " + path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
