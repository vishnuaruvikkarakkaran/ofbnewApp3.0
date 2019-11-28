package com.enfin.ofabee3.ui.module.forgotpassword;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.forgotpassword.response.ForgotPasswordOTPResponseModel;
import com.enfin.ofabee3.data.remote.model.mycourses.response.MyCoursesResponseModel;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.otp.OtpActivity;
import com.enfin.ofabee3.ui.module.passwordreset.ResetPasswordActivity;
import com.enfin.ofabee3.ui.module.registration.RegistrationActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.AvenirEditText;
import com.enfin.ofabee3.utils.AvenirTextView;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.MultiTextWatcher;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import hk.ids.gws.android.sclick.SClick;

public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordContract.MvpView {

    @BindView(R.id.emailEditText)
    AvenirEditText userEmail;
    @BindView(R.id.createNowTextView)
    AvenirTextView register;
    @BindView(R.id.email_error_tv)
    TextView emailError;

    private Context mContext;
    private Dialog progressDialog;
    private ForgotPasswordPresenter forgotPasswordPresenter;
    private boolean isValidEmail;
    private boolean doValidation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ForgotPasswordActivity.this;
        forgotPasswordPresenter = new ForgotPasswordPresenter(this);
        init();
        emailValidation();
    }

    private void emailValidation() {
        //Email field edit text listener for paste event.
        userEmail.addListener(() -> validateEmail(Objects.requireNonNull(userEmail.getText()).toString().trim()));

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
    }

    private void init() {
        //Progress Dialog initialization
        progressDialog = AppUtils.showProgressDialog(mContext);
        checkConnectivity();
    }

    private void checkConnectivity() {
       /* if (NetworkUtil.isConnected(this))
            loginPresenter.checkUserCountry(this);
        else
            forgotPasswordPresenter.showNoConnectivityDialog(this);*/
    }

    @Override
    public int getLayout() {
        return R.layout.activity_forgot_password;
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
       /* AppUtils.onShowSnackbar(parentView, message);
        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(parentView.getWindowToken(), 0);*/
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
    public void onRetry() {
        checkConnectivity();
    }

    @OnClick(R.id.createNowTextView)
    public void registerUser() {
        if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
        if (NetworkUtil.isConnected(this)) {
            this.finish();
            Intent registerIntent = new Intent(ForgotPasswordActivity.this, RegistrationActivity.class);
            startActivity(registerIntent);
        } else
            onShowToast(mContext.getString(R.string.check_internet_text));
    }

    @OnClick(R.id.continueButton)
    public void continueBtn() {
        emailError.setVisibility(View.VISIBLE);
        if (NetworkUtil.isConnected(mContext)) {
            doValidation = true;
            String email = userEmail.getText().toString().trim();
            validateEmail(email);
            if (forgotPasswordPresenter.validateEmailFormat(email)) {
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                forgotPasswordPresenter.sendotp(this, email);
            } else {
                //emailError.setVisibility(View.VISIBLE);
                //emailError.setText("Invalid email");
                /*//isValidEmail = false;
                if (email.length() > 0) {
                    userEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    userEmail.setError(getString(R.string.email_invalid_text));
                    userEmail.requestFocus();
                } else {
                    userEmail.setError(getString(R.string.email_empty_text));
                    userEmail.requestFocus();
                }*/
            }

        } else
            onShowToast(mContext.getString(R.string.check_internet_text));
    }

    /**
     * Method for validating email format.
     *
     * @param email email id
     */
    private void validateEmail(String email) {
        if (email.length() > 0) {
            if (forgotPasswordPresenter.validateEmailFormat(email)) {
                emailError.setVisibility(View.GONE);
                isValidEmail = true;
                userEmail.setError(null);
                userEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_green_24dp, 0);
            } else {
                if (doValidation) {
                    emailError.setVisibility(View.VISIBLE);
                    emailError.setText("Invalid email");
                    isValidEmail = false;
                }
                //userEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        } else {
            emailError.setVisibility(View.VISIBLE);
            emailError.setText("Enter your email");
        }
    }

    @Override
    public <T> void onSuccees(T type) {
        if (type instanceof ForgotPasswordOTPResponseModel) {
            ForgotPasswordOTPResponseModel responseModel = (ForgotPasswordOTPResponseModel) type;
            Intent otpActivity = new Intent(getApplicationContext(), ResetPasswordActivity.class);
            otpActivity.putExtra(Constants.USER_SOURCE, "forgot_password");
            otpActivity.putExtra(Constants.USER_NUMBER, "");
            otpActivity.putExtra(Constants.USER_EMAIL, Objects.requireNonNull(userEmail.getText()).toString().trim());
            startActivity(otpActivity);
        }
    }

    @Override
    public <T> void onFailure(T type) {
        if (type instanceof String) {
            String responseError = (String) type;
        }
    }
}
