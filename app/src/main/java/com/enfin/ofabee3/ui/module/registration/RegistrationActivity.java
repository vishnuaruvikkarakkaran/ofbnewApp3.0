package com.enfin.ofabee3.ui.module.registration;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.module.home.guesthome.GuestHomeActivity;
import com.enfin.ofabee3.ui.module.login.LoginActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.otp.OtpActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.AvenirEditText;
import com.enfin.ofabee3.utils.AvenirTextView;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.MultiTextWatcher;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import hk.ids.gws.android.sclick.SClick;

/**
 * Displays the Registration screen.
 */

public class RegistrationActivity extends BaseActivity implements RegistrationContact.MvpView {


    @BindView(R.id.nameRegEditText)
    AvenirEditText regName;
    @BindView(R.id.numberSelectionLayoutReg)
    LinearLayout numberLayout;
    @BindView(R.id.numberRegEditText)
    AvenirEditText regPhoneNumber;
    @BindView(R.id.emailRegEditText)
    AvenirEditText regEmail;
    @BindView(R.id.passwordRegEditText)
    AvenirEditText regPassword;
    @BindView(R.id.joinNowButton)
    Button regJoinNowBtn;
    @BindView(R.id.signInTextView)
    AvenirTextView regSignInBtn;
    @BindView(R.id.skipRegTextView)
    AvenirTextView regSkipBtn;
    @BindView(R.id.name_error_tv)
    TextView nameErrorTV;
    @BindView(R.id.phone_number_error_tv)
    TextView phoneNumberErrorTV;
    @BindView(R.id.email_error_tv)
    TextView emailErrorTV;
    @BindView(R.id.password_error_tv)
    TextView passwordErrorTV;

    private Context mContext;
    private RegistrationPresenter registrationPresenter;
    private CountryCodePicker countryCodePicker;
    private Dialog progressDialog;
    private String countryCode;
    private boolean isNumberSelected, isValidEmail, isValidNumber, isShowPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = RegistrationActivity.this;
        registrationPresenter = new RegistrationPresenter(this);
        checkConnectivity();
        regPhoneNumber.addListener(() -> validatePhoneNumber(countryCode, Objects.requireNonNull(regPhoneNumber.getText()).toString().trim()));
        phonenumberTextWatcher();
    }

    private void phonenumberTextWatcher() {
        //Text watcher for validation.
        new MultiTextWatcher()
                .registerEditText(regPhoneNumber)
                .setCallback(new MultiTextWatcher.TextWatcherWithInstance() {
                    @Override
                    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(EditText editText, Editable editable) {
                        if (editText.getId() == R.id.numberRegEditText) {
                            validatePhoneNumber(countryCode, editable.toString().trim());
                        }
                    }
                });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_registration;
    }

    private void checkConnectivity() {
        if (NetworkUtil.isConnected(this)) {
            init();
        } else {
            registrationPresenter.showNoConnectivityDialog(this);
        }
    }

    /**
     * Method for initializing all widgets and views.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        //initializing country phone code selection spinner.
        countryCodePicker = findViewById(R.id.ccpReg);
        //Progress Dialog initialization
        progressDialog = AppUtils.showProgressDialog(mContext);
        //Getting user country details from intent.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Toast.makeText(mContext, extras.get(Constants.COUNTRY).toString(), Toast.LENGTH_SHORT).show();
            countryCodePicker.setCountryForNameCode((String) extras.get(Constants.COUNTRY));
            countryCode = "+" + countryCodePicker.getSelectedCountryCode() + " ";
        }

        //Text watcher for validation.
        new MultiTextWatcher()
                .registerEditText(regEmail)
                .setCallback(new MultiTextWatcher.TextWatcherWithInstance() {
                    @Override
                    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(EditText editText, Editable editable) {
                        switch (editText.getId()) {
                            case R.id.emailRegEditText:
                                //Checking the email format is proper or not
                                if (registrationPresenter.validateEmailFormat(editable.toString().trim())) {
                                    isValidEmail = true;
                                    regEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_green_24dp, 0);
                                } else {
                                    isValidEmail = false;
                                    regEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                }
                        }
                    }
                });

        //Password edit text right drawable-mdpi touch listener for view and hide password.
        regPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (regPassword.getRight() - regPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    if (isShowPassword) {
                        //hide password
                        isShowPassword = false;
                        regPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_grey_24dp, 0);
                        regPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        regPassword.setSelection(regPassword.getText().length());
                    } else {
                        //show password
                        isShowPassword = true;
                        regPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_grey_24dp, 0);
                        regPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        regPassword.setSelection(regPassword.getText().length());
                    }
                    return true;
                }
            }
            return false;

        });

    }

    /////      Mvp Methods      /////

    @Override
    public void onRetry() {
        checkConnectivity();
    }

    @Override
    public void onShowNameError(String message) {
        //regName.setError(message);
        nameErrorTV.setText(message);
        nameErrorTV.setVisibility(View.VISIBLE);
        //regName.requestFocus();
    }

    @Override
    public void onShowNumberError(String message) {
        phoneNumberErrorTV.setText(message);
        phoneNumberErrorTV.setVisibility(View.VISIBLE);
        //regPhoneNumber.setError(message);
        //regPhoneNumber.requestFocus();
    }


    @Override
    public void onShowEmailError(String message) {
        emailErrorTV.setText(message);
        emailErrorTV.setVisibility(View.VISIBLE);
        //regEmail.requestFocus();
    }

    @Override
    public void onShowPasswordError(String message) {
        passwordErrorTV.setText(message);
        passwordErrorTV.setVisibility(View.VISIBLE);
        //regPassword.requestFocus();
    }

    @Override
    public void onStartOtpVerificationPage(String name, String number, String email, String password) {
        Intent intent = new Intent(RegistrationActivity.this, OtpActivity.class);
        intent.putExtra(Constants.USER_SOURCE, Constants.REGISTRATION_SOURCE);
        intent.putExtra(Constants.USER_NAME, name);
        intent.putExtra(Constants.USER_COUNTRY_CODE, "+" + countryCodePicker.getSelectedCountryCode() + " ");
        intent.putExtra(Constants.USER_NUMBER, number);
        intent.putExtra(Constants.USER_EMAIL, email);
        intent.putExtra(Constants.USER_PASSWORD, password);
        startActivity(intent);
    }

    @OnClick({R.id.joinNowButton, R.id.signInTextView, R.id.skipRegTextView})
    public void onItemClick(View view) {
        if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
        if (NetworkUtil.isConnected(mContext)) {
            switch (view.getId()) {
                case R.id.joinNowButton:
                    String countryCode = "+" + countryCodePicker.getSelectedCountryCode() + " ";
                    if (validateFields(regName.getText().toString().trim(), countryCode, regPhoneNumber.getText().toString().trim(),
                            regEmail.getText().toString().trim(), regPassword.getText().toString().trim(), isValidEmail)) {

                        registrationPresenter.validateRegisterFields(mContext, regName.getText().toString().trim(), countryCode, regPhoneNumber.getText().toString().trim(),
                                regEmail.getText().toString().trim(), regPassword.getText().toString().trim(), isValidEmail);
                    } else {

                    }

                    break;
                case R.id.signInTextView:
                    Intent signIn = new Intent(RegistrationActivity.this, LoginActivity.class);
                    signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(signIn);
                    finish();
                    break;
                case R.id.skipRegTextView:
                    Toast.makeText(mContext, "skip", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else
            onShowToast(mContext.getString(R.string.check_internet_text));
    }

    private boolean validateFields(String name, String countryCode,
                                   String number, String email, String password, boolean isValidEmail) {
        boolean validData = true;

        if (name.length() == 0) {
            validData = false;
            onShowNameError("Enter Name");
        } else if (!AppUtils.isValidName(name)) {
            validData = false;
            onShowNameError("invalid Name");
        } else
            nameErrorTV.setVisibility(View.GONE);

        if (number.length() == 0) {
            validData = false;
            onShowNumberError("Enter Number");
        } else if (!isValidNumber) {
            validData = false;
            onShowNumberError("Invalid number");
        } else
            phoneNumberErrorTV.setVisibility(View.GONE);

        if (email.length() == 0) {
            validData = false;
            onShowEmailError("Enter Email");
        } else if (!isValidEmail) {
            validData = false;
            onShowEmailError("Invalid Email");
        }
        else
            emailErrorTV.setVisibility(View.GONE);

        if (password.length() == 0) {
            validData = false;
            onShowPasswordError("Enter Password");
        } else if (password.length() < 6) {
            validData = false;
            onShowPasswordError("Password should contain minimum 6 characters");
        }
        else
            passwordErrorTV.setVisibility(View.GONE);

        return validData;

    }

    @Override
    public void onShowProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void onHideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowSnackBar(String message) {

    }

    @Override
    public void onShowAlertDialog(String message) {
        if (message.equalsIgnoreCase("No Internet")) {
            Intent noInternet = new Intent(this, NoInternetActivity.class);
            startActivity(noInternet);
        } else
            AppUtils.onShowAlertDialog(this, message);
    }

    @Override
    public void onConnectivityError() {
        AppUtils.showToast(mContext, getString(R.string.check_internet_text));
    }

    /**
     * Method for validating phone format.
     *
     * @param phonenumber phone number
     */
    private void validatePhoneNumber(String countryCode, String phonenumber) {
        //Checking the phonenumber format is proper or not
        //True - valid phonenumber. False - Invalid phonenumber.
        String internationalNumber = countryCode + phonenumber;
        String regex = "(([+][(]?[0-9]{1,3}[)]?)|([(]?[0-9]{4}[)]?))\\s*[)]?[-\\s\\.]?[(]?[0-9]{1,3}[)]?([-\\s\\.]?[0-9]{3})([-\\s\\.]?[0-9]{3,4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(internationalNumber);

        if (matcher.matches()) {
            OBLogger.e("VALID " + internationalNumber);
            isValidNumber = true;
            //userPhoneNumber.setError(null);
            //phoneNumberError.setVisibility(View.GONE);
            regPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_green_24dp, 0);
        } else {
            OBLogger.e("INVALID " + internationalNumber);
            isValidNumber = false;
            regPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

}
