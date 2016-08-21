package com.skybee.tracker.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.skybee.tracker.R;
import com.skybee.tracker.Utility;
import com.skybee.tracker.constants.API;
import com.skybee.tracker.constants.Constants;
import com.skybee.tracker.core.BaseActivity;
import com.skybee.tracker.model.User;
import com.skybee.tracker.preferences.UserStore;

public class RegisterActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();
    // UI references.
    private AutoCompleteTextView emailView;
    private EditText nameView;
    private EditText mobileView;
    private EditText passwordView;
    private EditText registrationKeyView;
    private ProgressDialog progressDialog;
    private View progressView;
    private View loginFormView;
    private RadioGroup radioGroup;
    private RadioButton userType;
    private TextView selectAll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        selectAll=(TextView)findViewById(R.id.select_all_text);
        selectAll.setVisibility(View.INVISIBLE);
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        nameView = (EditText) findViewById(R.id.user_name);
        mobileView = (EditText) findViewById(R.id.user_mobile_number);
        registrationKeyView = (EditText) findViewById(R.id.user_registration_key);
        radioGroup = (RadioGroup) findViewById(R.id.user_type);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UserStore userStore = new UserStore(getApplicationContext());
                userStore.saveIsAdmin(true);
                attemptLogin();
            }
        });
        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);
        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String name = nameView.getText().toString();
        String mobile = nameView.getText().toString();
        String registrationKey = nameView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }
        //Check for valid name
        if (TextUtils.isEmpty(name)) {
            nameView.setError(getString(R.string.error_invalid_name));
            focusView = nameView;
            cancel = true;
        }
        //Check for valid mobile number
        if (TextUtils.isEmpty(mobile) && mobile.length() < Constants.MOBILE_NUMBER_LENGTH) {
            mobileView.setError(getString(R.string.error_invalid_mobile));
            focusView = mobileView;
            cancel = true;
        }
        //Check for registration name
        if (TextUtils.isEmpty(registrationKey)) {
            registrationKeyView.setError(getString(R.string.error_invalid_registration_key));
            focusView = registrationKeyView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            int selectedId = radioGroup.getCheckedRadioButtonId();
            userType = (RadioButton) findViewById(selectedId);
            progressDialog = ProgressDialog.show(this, "", "Loading...", true);
            User user = new User();
            user.setUserName(nameView.getText().toString());
            user.setUserEmail(emailView.getText().toString());
            user.setUserMobileNumber(mobileView.getText().toString());
            user.setUserPassword(passwordView.getText().toString());
            user.setRegistrationCode(registrationKeyView.getText().toString());
            user.setLogin_type(1);
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.getDeviceId();
            user.setDevice_id(telephonyManager.getDeviceId());
            if (userType.getText() == getResources().getString(R.string.admin_text)) {
                user.setAdmin(true);
                Utility.authenticate(getApplicationContext(), progressDialog, API.ADMIN_SIGN_UP, user);
            } else {
                user.setAdmin(false);
                Utility.authenticate(getApplicationContext(), progressDialog, API.EMPLOYEE_SIGN_UP, user);
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}

