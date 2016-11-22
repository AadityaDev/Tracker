package com.skybee.tracker.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.app.Activity.RESULT_OK;

public class Profile extends BaseFragment {

    private final String TAG = this.getClass().getSimpleName();
    private static final int SELECT_PICTURE = 1;
    protected static final int SELECT_FILE = 0;
    protected static final int REQUEST_CAMERA = 1;
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
    private ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userStore = new UserStore(context);
        gpsTracker = new GPSTracker(context);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        coordinatorLayout=(CoordinatorLayout)view.findViewById(R.id.coordinator_layout);
        Utility.showSnackBar(getContext(),coordinatorLayout);
        userImage = (ImageView) view.findViewById(R.id.user_image);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        userName = (TextView) view.findViewById(R.id.user_name);
        userCompany = (TextView) view.findViewById(R.id.user_company);
        progressDialog = ProgressDialog.show(getContext(), "", "Loading...", true);
        Utility.showProgressDialog(progressDialog);
        userEmail = (TextView) view.findViewById(R.id.email_text);
        userMobileNumber = (TextView) view.findViewById(R.id.mobile_num_text);
        userImageText = (TextView) view.findViewById(R.id.user_image_text);
        logoutButton = (RelativeLayout) view.findViewById(R.id.logout_section);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        user = getLocalUser();
        getUserProfile();
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

    private void showPictureChooser() {
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

    private void uploadFile(@NonNull String fileName) {
        File file = new File(fileName);
        Log.d(TAG, file.toString());
        UserStore sessionStore = new UserStore(getContext());
        Utility.showSnackBar(context,coordinatorLayout);
        ListenableFuture<JSONObject> uploadResume = Factory.getUserService().updateUserDetails(sessionStore.getUserDetails(), API.UPDATE_PROFILE, file);
        Futures.addCallback(uploadResume, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d(TAG, "success");
                try {
                    Toast.makeText(getContext(), "Picture uploaded", Toast.LENGTH_LONG).show();
                    getUserProfile();
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i(TAG, "Error");
            }
        }, ExecutorUtils.getUIThread());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(
                        Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, TextUtils.isEmpty(e.getMessage()) ? "File Not Found" : e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, TextUtils.isEmpty(e.getMessage()) ? "IO Exception" : e.getMessage());
                }
                uploadFile(destination.getAbsolutePath());
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = ((Activity) context).managedQuery(selectedImageUri, projection,
                        null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);
                if (selectedImagePath != null) {
                    Bitmap bm;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(selectedImagePath, options);
                    final int REQUIRED_SIZE = 200;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    bm = BitmapFactory.decodeFile(selectedImagePath, options);
                    if (bm != null) {
                        uploadFile(selectedImagePath);
//                        if (isUserImage) {
//                            userImage.setImageBitmap(bm);
//                        } else {
//                            userCoverImage.setImageBitmap(bm);
//                        }
                    }
                }
            }
        }
    }

    public void getUserProfile() {
        Utility.showSnackBar(context,coordinatorLayout);
        ListenableFuture<JSONObject> getUserProfile = Factory.getUserService().getUserProfile(API.PROFILE_URL, user);
        Futures.addCallback(getUserProfile, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (!TextUtils.isEmpty(user.getUserCompany()))
                        userCompany.setText(user.getUserCompany());
                    else
                        userCompany.setText("");

                    if (result.has("message") && result.getString("message").equals("Success")) {
                        JSONObject data = result.getJSONObject("data");
                        if ((data.has("name")) && !(TextUtils.isEmpty(data.getString("name")))) {
                            userName.setText(data.getString("name"));
                        }
                        if (data.has("email") && !(TextUtils.isEmpty(data.getString("email")))) {
                            userEmail.setText(data.getString("email"));
                        }
                        if (data.has("mobile") && !(TextUtils.isEmpty(data.getString("mobile")))) {
                            userMobileNumber.setText(data.getString("mobile"));
                        }
                        if (data.has("profile_pic") && !(TextUtils.isEmpty(data.getString("profile_pic")))) {
                            final AtomicBoolean loaded = new AtomicBoolean();
                            Picasso.with(context).load(Constants.IMAGE_PREFIX + data.getString("profile_pic"))
                                    .into(userImage, new Callback.EmptyCallback() {
                                        @Override
                                        public void onSuccess() {
                                            loaded.set(true);
                                            userImage.setVisibility(View.VISIBLE);
                                            userImageText.setVisibility(View.INVISIBLE);
                                        }

                                        @Override
                                        public void onError() {
                                            userImageText.setVisibility(View.VISIBLE);
                                        }
                                    });
                            if (!loaded.get()) {
                                userImageText.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (user != null) {
                                if (!TextUtils.isEmpty(user.getUserName())) {
                                    userName.setText(user.getUserName());
                                }
                                if (user.getUserEmail() != null)
                                    userEmail.setText(user.getUserEmail());
                                if (user.getUserMobileNumber() != null)
                                    userMobileNumber.setText(user.getUserMobileNumber());
                            }
                        }
                    }
                } catch (JSONException jsonException) {
                    Log.d(TAG, Constants.Exception.JSON_EXCEPTION);
                    Utility.checkProgressDialog(progressDialog);
                } catch (Exception exception) {
                    Log.d(TAG, Constants.Exception.EXCEPTION);
                    Utility.checkProgressDialog(progressDialog);
                } finally {
                    Log.d(TAG, Constants.Exception.EXCEPTION);
                    Utility.checkProgressDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utility.checkProgressDialog(progressDialog);
                Utility.showErrorDialog(context, Constants.ERROR_OCCURRED);
            }
        }, ExecutorUtils.getUIThread());

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
