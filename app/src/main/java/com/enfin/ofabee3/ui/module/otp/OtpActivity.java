package com.enfin.ofabee3.ui.module.otp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.sharedpreferences.OBPreferencesHelper;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.module.coursecategories.MyCourseCategoryListActivity;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.login.LoginActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.passwordreset.ResetPasswordActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;

import butterknife.BindView;
import butterknife.OnClick;
import hk.ids.gws.android.sclick.SClick;

/**
 * Displays the OTP screen.
 */


public class OtpActivity extends BaseActivity implements OtpContract.MvpView {

    @BindView(R.id.container)
    ConstraintLayout parentView;
    @BindView(R.id.otpEditText)
    EditText otpEditText;
    @BindView(R.id.otpDetailTextView)
    TextView otpDetailText;
    @BindView(R.id.reSendTextView)
    TextView resendTextView;
    @BindView(R.id.countDownTextView)
    TextView countDownTextView;


    private Context mContext;
    private String userName, userPassword, countryCode, userNumber, userEmail, userSource;
    private OtpPresenter otpPresenter;
    private Dialog progressDialog;
    private OBPreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = OtpActivity.this;
        otpPresenter = new OtpPresenter(this);
        resendTextView.setText(Html.fromHtml("Didn't recieve OTP? <font color=#2bb1ff>Resend Now</font>"));
        preferencesHelper = new OBPreferencesHelper(this);
        initialize();
    }

    private void initialize() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString(Constants.USER_NAME);
            userNumber = extras.getString(Constants.USER_NUMBER);
            countryCode = extras.getString(Constants.USER_COUNTRY_CODE);
            userEmail = extras.getString(Constants.USER_EMAIL);
            userPassword = extras.getString(Constants.USER_PASSWORD);
            userSource = extras.getString(Constants.USER_SOURCE);
            if (userEmail == null) {
                onShowSnackBar(getString(R.string.something_went_wrong_text));
                this.finish();
            } else {
                if (!TextUtils.isEmpty(userSource) && userSource.equalsIgnoreCase("forgot_password"))
                    otpDetailText.setText(mContext.getString(R.string.otp_detail_text).concat(" " + userEmail + "."));
                if (!TextUtils.isEmpty(userSource) && userSource.equalsIgnoreCase(Constants.REGISTRATION_SOURCE))
                    otpDetailText.setText(mContext.getString(R.string.otp_detail_text).concat(" " + countryCode + "" + userNumber + "."));
            }
        }

        //Progress Dialog initialization
        progressDialog = AppUtils.showProgressDialog(mContext);

        otpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 5) {
                    otpPresenter.onValidateOtp(mContext, userName, countryCode, userNumber, userEmail, userPassword, s.toString().trim(), userSource);
                }
            }
        });

        //Calling internet connectivity checking method.
        checkConnectivity();
    }

    private void checkConnectivity() {
        if (NetworkUtil.isConnected(this)) {
            otpPresenter.onStartSmsRetrieverClient(mContext);
            onCountDownStarted();
            otpPresenter.onStartCountDown();
        } else
            otpPresenter.showNoConnectivityDialog(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_otp;
    }

    @Override
    public void onOtpReceived(String otp) {
        otpEditText.setText(otp);
    }

    @Override
    public void otpVerificationSuccess(String userSource, LoginResponseModel userModel) {
        if (userSource.equals(Constants.REGISTRATION_SOURCE)) {
            if (!userModel.getMetadata().isError()) {
                OBLogger.d("Name " + userModel.getData().getUser().getUs_name());
                //save login status here
                preferencesHelper.setCurrentUserLoggedInStatus(true);
                otpPresenter.insertUserData(this, userModel.getData());
                //databasePresenter.insertUserData(userModel);
            } else
                onShowAlertDialog(userModel.getMetadata().getMessage());
            /*Toast.makeText(getApplicationContext(), "User registration success", Toast.LENGTH_SHORT).show();
            Intent courseCategory = new Intent(OtpActivity.this, MyCourseCategoryListActivity.class);
            courseCategory.putExtra(Constants.USER_SOURCE, "REGISTRATION");
            startActivity(courseCategory);*/
        }
    }

    @Override
    public void isSuccessfullyInserted() {
        //Toast.makeText(mContext, "Logged in successfully", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "User registration success", Toast.LENGTH_SHORT).show();
        Intent courseCategory = new Intent(OtpActivity.this, MyCourseCategoryListActivity.class);
        courseCategory.putExtra(Constants.USER_SOURCE, "REGISTRATION");
        courseCategory.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(courseCategory);
        finish();
        /*Intent intent = new Intent(LoginActivity.this, MyCourseCategoryListActivity.class);
        intent.putExtra(Constants.USER_SOURCE, "LOGIN");
        startActivity(intent);
        finish();*/
        //Intent homeIntent = new Intent(OtpActivity.this, HomeActivity.class);
        //homeIntent.putStringArrayListExtra(Constants.CATEGORY_ID_LIST, categoryIDList);
        //startActivity(homeIntent);
        //finish();
    }

    @Override
    public void insertionFailed() {
        OBLogger.e("DB Operation failed");
        onShowAlertDialog(getString(R.string.something_went_wrong_text));
    }

    @Override
    public void onCountDownStarted() {
        otpPresenter.onStartCountDown();
        resendTextView.setVisibility(View.GONE);
        //resendTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable-mdpi.ic_sms_gray, 0, 0, 0);
        //resendTextView.setTextColor(getResources().getColor(R.color.colorDarkGrey));
        //resendTextView.setClickable(false);
        countDownTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCountDownFinished() {
        resendTextView.setVisibility(View.VISIBLE);
        //resendTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable-mdpi.ic_sms_green, 0, 0, 0);
        //resendTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        resendTextView.setClickable(true);
        countDownTextView.setVisibility(View.GONE);
    }

    @Override
    public void onUpdateCountdownTimer(String time) {
        countDownTextView.setText(time);
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

    }

    @Override
    public void onRetry() {
        checkConnectivity();
    }

    @OnClick({R.id.reSendTextView})
    public void onItemClick(View view) {
        if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
        if (NetworkUtil.isConnected(mContext)) {
            switch (view.getId()) {
                case R.id.reSendTextView:
                    Toast.makeText(mContext, "Resend OTP", Toast.LENGTH_SHORT).show();
                    otpPresenter.onResendOtp(mContext, userNumber, userEmail);
                    break;
            }
        } else
            onShowSnackBar(mContext.getString(R.string.check_internet_text));
    }
}
