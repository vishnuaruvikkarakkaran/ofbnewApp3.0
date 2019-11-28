package com.enfin.ofabee3.ui.module.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.DatabaseContract;
import com.enfin.ofabee3.data.local.database.DatabasePresenter;
import com.enfin.ofabee3.data.local.sharedpreferences.OBPreferencesHelper;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.module.coursecategories.MyCourseCategoryListActivity;
import com.enfin.ofabee3.ui.module.coursedetail.TabsHeaderActivity;
import com.enfin.ofabee3.ui.module.forgotpassword.ForgotPasswordActivity;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.home.featuredcourses.FeaturedCourse;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.registration.RegistrationActivity;
import com.enfin.ofabee3.utils.AppSignatureHelper;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.AvenirEditText;
import com.enfin.ofabee3.utils.AvenirTextView;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.MultiTextWatcher;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.enfin.ofabee3.data.local.sharedpreferences.SharedPreferenceData;
import com.enfin.ofabee3.utils.OpenSansTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import hk.ids.gws.android.sclick.SClick;

/**
 * Displays the splash screen.
 */

public class LoginActivity extends BaseActivity implements LoginContract.MvpView, AppUtils.DialogActionListener {

    @BindView(R.id.container)
    ConstraintLayout parentView;
    @BindView(R.id.optionSelector)
    RadioGroup selectionGroup;
    @BindView(R.id.phoneNumberSelector)
    RadioButton phoneSelector;
    @BindView(R.id.emailSelector)
    RadioButton emailSelector;
    @BindView(R.id.emailLayout)
    LinearLayout emailLoginLayout;
    @BindView(R.id.mobileNumberLayout)
    LinearLayout numberLoginLayout;
    @BindView(R.id.emailEditText)
    AvenirEditText userEmail;
    @BindView(R.id.passwordEditText)
    AvenirEditText password;
    @BindView(R.id.numberEditText)
    AvenirEditText userPhoneNumber;
    @BindView(R.id.passwordMobileEditText)
    AvenirEditText numberPassword;
    @BindView(R.id.continueButton)
    Button continueBtn;
    @BindView(R.id.forgotPasswordTextView)
    AvenirTextView forgotPasswordBtn;
    @BindView(R.id.createNowTextView)
    AvenirTextView registerBtn;
    @BindView(R.id.skipTextView)
    AvenirTextView skipBtn;
    @BindView(R.id.createNowLayout)
    LinearLayout createNowLayout;
    @BindView(R.id.numberSelectionLayout)
    LinearLayout numberSelectionLayout;
    @BindView(R.id.phone_number_error_tv)
    OpenSansTextView phoneNumberError;
    @BindView(R.id.phone_password_error_tv)
    OpenSansTextView phonePasswordError;
    @BindView(R.id.email_error_tv)
    OpenSansTextView emailError;
    @BindView(R.id.email_password_error_tv)
    OpenSansTextView emailPasswordError;
    @BindView(R.id.emailEditText_ti)
    TextInputLayout emailSelectionLayout;

    private static final String TAG = "LoginActivity ";

    private LoginPresenter loginPresenter;
    private Context mContext;
    private boolean isNumberSelected, isValidEmail, isValidNumber, isShowPassword;
    private String country;
    private CountryCodePicker countryCodePicker;
    private Dialog progressDialog;
    private OBPreferencesHelper preferencesHelper;
    private String countryCode;
    private boolean validInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = LoginActivity.this;
        loginPresenter = new LoginPresenter(this);
        preferencesHelper = new OBPreferencesHelper(this);
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();
        init();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    /**
     * Method for initializing all widgets and views.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void init() {

        //initializing country phone code selection spinner.
        countryCodePicker = findViewById(R.id.ccp);

        //Progress Dialog initialization
        progressDialog = AppUtils.showProgressDialog(mContext);

        //Set phone number selector as default.
        selectionGroup.check(R.id.phoneNumberSelector);

        countryCode = "+" + countryCodePicker.getSelectedCountryCode() + " ";

        //Email field edit text listener for paste event.
        userEmail.addListener(() -> validateEmail(Objects.requireNonNull(userEmail.getText()).toString().trim()));

        //password.addListener(() -> validateEmailPassword(Objects.requireNonNull(password.getText()).toString().trim()));

        //numberPassword.addListener(() -> validatePhonePassword(Objects.requireNonNull(numberPassword.getText()).toString().trim()));

        //Phonenumber field edit text listener for paste event.
        userPhoneNumber.addListener(() -> validatePhoneNumber(countryCode, Objects.requireNonNull(userPhoneNumber.getText()).toString().trim()));

        //Text watcher for validation.
        new MultiTextWatcher()
                .registerEditText(userPhoneNumber)
                .setCallback(new MultiTextWatcher.TextWatcherWithInstance() {
                    @Override
                    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(EditText editText, Editable editable) {
                        if (editText.getId() == R.id.numberEditText) {
                            validatePhoneNumber(countryCode, editable.toString().trim());
                        }
                    }
                });

        //Text watcher for validation.
        new MultiTextWatcher()
                .registerEditText(userEmail)
                .setCallback(new MultiTextWatcher.TextWatcherWithInstance() {
                    @Override
                    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(EditText editText, Editable editable) {
                        if (editText.getId() == R.id.emailEditText) {
                            validateEmail(editable.toString().trim());
                        }
                    }
                });

        //Text watcher for validation.
        new MultiTextWatcher()
                .registerEditText(password)
                .setCallback(new MultiTextWatcher.TextWatcherWithInstance() {
                    @Override
                    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(EditText editText, Editable editable) {
                        if (editText.getId() == R.id.passwordEditText) {
                            validateEmailPassword(editable.toString().trim());
                        }
                    }
                });

        //Text watcher for validation.
        new MultiTextWatcher()
                .registerEditText(numberPassword)
                .setCallback(new MultiTextWatcher.TextWatcherWithInstance() {
                    @Override
                    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(EditText editText, Editable editable) {
                        if (editText.getId() == R.id.passwordMobileEditText) {
                            validatePhonePassword(editable.toString().trim());
                        }
                    }
                });

        //Password edit text right drawable-mdpi touch listener for view and hide password.
        password.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isShowPassword) {
                        //hide password
                        isShowPassword = false;
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_grey_24dp, 0);
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        password.setSelection(password.getText().length());
                    } else {
                        //show password
                        isShowPassword = true;
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_grey_24dp, 0);
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        password.setSelection(password.getText().length());
                    }
                    return true;
                }
            }
            return false;

        });

        //Number Passw^[+]?[0-9]{10,13}$ord edit text right drawable-mdpi touch listener for view and hide password.
        numberPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (numberPassword.getRight() - numberPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isShowPassword) {
                        //hide password
                        isShowPassword = false;
                        numberPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_grey_24dp, 0);
                        numberPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        numberPassword.setSelection(numberPassword.getText().length());
                    } else {
                        //show password
                        isShowPassword = true;
                        numberPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_grey_24dp, 0);
                        numberPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        numberPassword.setSelection(numberPassword.getText().length());
                    }
                    return true;
                }
            }
            return false;

        });

        //Calling internet connectivity checking method.
        //checkConnectivity();
    }

    /**
     * Radio group checked change lister for login type selection.
     *
     * @param button  Radio button identifier
     * @param checked Radio button state
     */
    @OnCheckedChanged({R.id.phoneNumberSelector, R.id.emailSelector})
    public void onSelection(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.phoneNumberSelector:
                    validInit = false;
                    emailError.setVisibility(View.GONE);
                    emailPasswordError.setVisibility(View.GONE);
                    phonePasswordError.setVisibility(View.GONE);
                    phoneNumberError.setVisibility(View.GONE);
                    phoneSelector.setTextColor(getResources().getColor(R.color.colorAccent));
                    emailSelector.setTextColor(getResources().getColor(R.color.colorGrey));
                    isNumberSelected = true;
                    hideKeyboard(this);
                    setPhoneNumberView();
                    break;
                case R.id.emailSelector:
                    validInit = false;
                    emailError.setVisibility(View.GONE);
                    emailPasswordError.setVisibility(View.GONE);
                    phonePasswordError.setVisibility(View.GONE);
                    phoneNumberError.setVisibility(View.GONE);
                    emailSelector.setTextColor(getResources().getColor(R.color.colorAccent));
                    phoneSelector.setTextColor(getResources().getColor(R.color.colorGrey));
                    isNumberSelected = false;
                    hideKeyboard(this);
                    setEmailView();
                    break;
            }
        }
    }

    /**
     * Method for validating email format.
     *
     * @param email email id
     */
    private void validateEmail(String email) {
        //Checking the email format is proper or not
        //True - valid email. False - Invalid email.
        if (loginPresenter.validateEmailFormat(email)) {
            OBLogger.i(TAG + getString(R.string.email_valid_text));
            //onShowToast(getString(R.string.email_valid_text));
            isValidEmail = true;
            //Drawable dr = getResources().getDrawable(R.drawable-mdpi.ic_done_green_24dp);
            //add an error icon to yur drawable-mdpi files
            //dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
            //userEmail.setError(null);
            emailError.setVisibility(View.GONE);
            userEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_green_24dp, 0);
        } else {
            OBLogger.d(TAG + getString(R.string.email_invalid_text));
            //onShowToast(getString(R.string.email_invalid_text));
            isValidEmail = false;
            userEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }


    private void validateEmailPassword(String password) {
        if (validInit) {
            if (TextUtils.isEmpty(password) || password.length() < 6)
                emailPasswordError.setVisibility(View.VISIBLE);
            else
                emailPasswordError.setVisibility(View.GONE);
        }
    }


    private void validatePhonePassword(String password) {
        if (validInit) {
            if (TextUtils.isEmpty(password) || password.length() < 6)
                phonePasswordError.setVisibility(View.VISIBLE);
            else
                phonePasswordError.setVisibility(View.GONE);
        }
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
            phoneNumberError.setVisibility(View.GONE);
            userPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_green_24dp, 0);
        } else {
            OBLogger.e("INVALID " + internationalNumber);
            isValidNumber = false;
            userPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    /**
     * Method for setting thecontinueBtn mobile number login view
     */
    private void setPhoneNumberView() {
        emailLoginLayout.setVisibility(View.INVISIBLE);
        numberLoginLayout.setVisibility(View.VISIBLE);
        //Clearing previous data's
        userPhoneNumber.getText().clear();
        //userPhoneNumber.setError(null);
        phoneNumberError.setVisibility(View.GONE);
        userPhoneNumber.clearFocus();
        numberPassword.getText().clear();
        //numberPassword.setError(null);
        phonePasswordError.setVisibility(View.GONE);
        numberPassword.clearFocus();
        userPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        numberPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_grey_24dp, 0);
        isValidNumber = false;
        isShowPassword = false;
    }

    /**
     * Method for setting the email login view
     */
    private void setEmailView() {
        emailLoginLayout.setVisibility(View.VISIBLE);
        numberLoginLayout.setVisibility(View.INVISIBLE);
        //Clearing previous data's
        userEmail.getText().clear();
        userEmail.setError(null);
        emailError.setVisibility(View.GONE);
        userEmail.clearFocus();
        password.getText().clear();
        password.setError(null);
        emailPasswordError.setVisibility(View.GONE);
        password.clearFocus();
        userEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_grey_24dp, 0);
        isValidEmail = false;
        isShowPassword = false;
    }

    /**
     * Method for clear all edit text fields.
     */
    private void clearAllEditTextFields() {
        userEmail.getText().clear();
        password.getText().clear();
        userPhoneNumber.getText().clear();
        numberPassword.getText().clear();
    }

    /**
     * Method for checking the internet connectivity.
     * if True - Connection Available
     * False - No Connection
     */
    private void checkConnectivity() {
        if (NetworkUtil.isConnected(this))
            loginPresenter.checkUserCountry(this);
        else
            loginPresenter.showNoConnectivityDialog(this);
    }

    /**
     * Method will execute after getting user current country.
     *
     * @param country user country.
     */
    @Override
    public void onUserCountry(String country) {
        this.country = country;
        //Setting the country phone code based on user country.
        countryCodePicker.setCountryForNameCode(country);
    }

    @Override
    public void onShowErrorEmailAlert(String message) {
        //userEmail.setError(message);
        emailError.setVisibility(View.VISIBLE);
        userEmail.requestFocus();
    }

    @Override
    public void onShowErrorPasswordAlert(String message) {
        if (isNumberSelected) {
            //numberPassword.setError(message);
            phonePasswordError.setVisibility(View.VISIBLE);
            numberPassword.requestFocus();
        } else {
            //password.setError(message);
            emailPasswordError.setVisibility(View.VISIBLE);
            password.requestFocus();
        }
    }

    @Override
    public void onLoginSuccess(LoginResponseModel userModel) {
        if (!userModel.getMetadata().isError()) {
            OBLogger.d("Name " + userModel.getData().getUser().getUs_name());
            //save login status here
            preferencesHelper.setCurrentUserLoggedInStatus(true);
            loginPresenter.insertUserData(this, userModel.getData());
            //databasePresenter.insertUserData(userModel);
        } else
            onShowAlertDialog(userModel.getMetadata().getMessage());

    }

    @Override
    public void onShowErrorNumberAlert(String message) {
        //userPhoneNumber.setError(message);
        phoneNumberError.setVisibility(View.VISIBLE);
        userPhoneNumber.requestFocus();
    }

    @Override
    public void onRetry() {
        checkConnectivity();
    }

    @Override
    public void onLoginFailure(String message) {
        AppUtils.onShowAlertDialog(mContext, message);
    }

    /**
     * Onclick listener for login button
     */
    @OnClick(R.id.continueButton)
    public void continueBtn() {
        if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
        if (NetworkUtil.isConnected(mContext)) {
            validInit = true;
            if (isNumberSelected) {
                if (isValidNumber) {
                    loginPresenter.validateNumberAndPasswordForLogin(mContext, countryCode, userPhoneNumber.getText().toString().trim(), numberPassword.getText().toString().trim());
                } else {
                    //userPhoneNumber.setError(mContext.getString(R.string.number_invalid_text));
                    phoneNumberError.setVisibility(View.VISIBLE);
                }
                validatePhonePassword(numberPassword.getText().toString().trim());
            } else {
                String email = userEmail.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                loginPresenter.validateEmailAndPasswordForLogin(mContext, email, passwordText, isValidEmail);
                validateEmailPassword(password.getText().toString().trim());
            }
        } else
            onShowToast(mContext.getString(R.string.check_internet_text));
    }

    /**
     * Onclick listener for registration button
     */
    @OnClick(R.id.createNowTextView)
    public void createNew() {
        if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
        if (NetworkUtil.isConnected(mContext)) {
            //userPhoneNumber.setError(null);
            phoneNumberError.setVisibility(View.GONE);

            //password.setError(null);
            emailPasswordError.setVisibility(View.GONE);

            userEmail.setError(null);
            emailError.setVisibility(View.GONE);
            emailPasswordError.setVisibility(View.GONE);
            phonePasswordError.setVisibility(View.GONE);
            clearAllEditTextFields();
            loginPresenter.startActivity(mContext, country, RegistrationActivity.class);
        } else
            onShowToast(mContext.getString(R.string.check_internet_text));
    }

    /**
     * Onclick listener for skip button
     */
    @OnClick(R.id.skipTextView)
    public void skip() {
        if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
        if (NetworkUtil.isConnected(mContext)) {
            Intent courseCategory = new Intent(LoginActivity.this, MyCourseCategoryListActivity.class);
            courseCategory.putExtra(Constants.USER_SOURCE, "LOGIN");
            startActivity(courseCategory);
        } else
            onShowToast(mContext.getString(R.string.check_internet_text));
    }

    /**
     * Onclick listener for forgot password button
     */
    @OnClick(R.id.forgotPasswordTextView)
    public void forgotPassword() {
        if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
        //databasePresenter.deleteDatabaseValues();
       /* if (NetworkUtil.isConnected(mContext))
            loginPresenter.startActivity(mContext, country, ForgotPasswordActivity.class);
        else
            onShowSnackBar(mContext.getString(R.string.check_internet_text));*/
        if (NetworkUtil.isConnected(mContext)) {
            Intent forgotPwdIntent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(forgotPwdIntent);
            clearAllEditTextFields();
        } else
            onShowToast(mContext.getString(R.string.check_internet_text));
    }

    /**
     * Focus changed listener for phone number EditText.
     * Background drawable-mdpi will set on the EditText based on focus state change.
     *
     * @param view     view
     * @param hasFocus focus state
     */
    @OnFocusChange(R.id.numberEditText)
    public void onNumberFocusChanged(View view, boolean hasFocus) {
        if (hasFocus)
            numberSelectionLayout.setBackground(getDrawable(R.drawable.bottom_border_selected));
        else
            numberSelectionLayout.setBackground(getDrawable(R.drawable.bottom_border));
    }

    /**
     * Focus changed listener for password EditText.
     * Background drawable-mdpi will set on the EditText based on focus state change.
     *
     * @param view     view
     * @param hasFocus focus state
     *//*
    @OnFocusChange(R.id.passwordMobileEditText)
    public void onNumberPasswordFocusChanged(View view, boolean hasFocus) {
        if (hasFocus)
            numberPassword.setBackground(getDrawable(R.drawable-mdpi.bottom_border_selected));
        else
            numberPassword.setBackground(getDrawable(R.drawable-mdpi.bottom_border));
    }*/

    /**
     * Focus changed listener for phone number EditText.
     * Background drawable-mdpi will set on the EditText based on focus state change.
     *
     * @param view     view
     * @param hasFocus focus state
     */
    @OnFocusChange(R.id.emailEditText)
    public void onEmailFocusChanged(View view, boolean hasFocus) {
        if (hasFocus)
            emailSelectionLayout.setBackground(getDrawable(R.drawable.bottom_border_selected));
        else
            emailSelectionLayout.setBackground(getDrawable(R.drawable.bottom_border));
    }

    /**
     * Focus changed listener for password EditText.
     * Background drawable-mdpi will set on the EditText based on focus state change.
     * <p>
     * //* @param view     view
     * // * @param hasFocus focus state
     *//*
    @OnFocusChange(R.id.passwordEditText)
    public void onEmailPasswordFocusChanged(View view, boolean hasFocus) {
        if (hasFocus)
            numberPassword.setBackground(getDrawable(R.drawable-mdpi.bottom_border_selected));
        else
            numberPassword.setBackground(getDrawable(R.drawable-mdpi.bottom_border));
    }*/
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
        AppUtils.onShowSnackbar(parentView, message);
        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(parentView.getWindowToken(), 0);
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

    @Override
    public void isSuccessfullyInserted() {
        Toast.makeText(mContext, "Logged in successfully", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(LoginActivity.this, MyCourseCategoryListActivity.class);
        intent.putExtra(Constants.USER_SOURCE, "LOGIN");
        startActivity(intent);
        finish();*/
        //OBLogger.e("TAG " + getCallingActivity().getShortClassName());
        /*if (getCallingActivity().getClassName().equals("com.enfin.ofabee3.ui.module.coursedetail.TabsHeaderActivity")) {
            String courseID = getIntent().getStringExtra(Constants.COURSE_ID);
            String itemType = getIntent().getStringExtra(Constants.TYPE);
            Intent homeIntent = new Intent(LoginActivity.this, TabsHeaderActivity.class);
            homeIntent.putExtra(Constants.COURSE_ID, courseID);
            homeIntent.putExtra(Constants.TYPE, itemType);
            startActivity(homeIntent);
            finish();
        } else {*/
        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
        finish();
        //}
    }

    @Override
    public void insertionFailed() {
        OBLogger.e("DB Operation failed");
        onShowAlertDialog(getString(R.string.something_went_wrong_text));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        AppUtils.showExitDialog(this, "Do you want to exit application ?", this);
    }

    @Override
    public void onPositiveButtonClick() {
        super.onBackPressed();
    }

    @Override
    public void onNegativeButtonClick() {
    }
}
