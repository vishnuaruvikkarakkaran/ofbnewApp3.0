package com.enfin.ofabee3.ui.module.passwordreset;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;

//todo create BaseActivity and import to this class
public class ResetPasswordActivity extends BaseActivity implements ResetPasswordFragment.OnResetPasswordFragmentInteractionListener, AppUtils.DialogActionListener {

    ResetPasswordContract.Presenter mPresenter;
    boolean doubleBackToExitPressedOnce = false;
    private String userNumber, userPassword, userEmail, userSource;
    private Bundle userBundle;
    private boolean userValid = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userBundle = new Bundle();
        getInitilValues();
        ResetPasswordFragment resetPasswordFragment = (ResetPasswordFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frame_layout_content);
        if (resetPasswordFragment == null) {
            resetPasswordFragment = ResetPasswordFragment.newInstance();
            resetPasswordFragment.setArguments(userBundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame_layout_content, resetPasswordFragment);
            transaction.commit();
        }
        mPresenter = new ResetPasswordPresenter(this, resetPasswordFragment);

    }

    private void getInitilValues() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userNumber = extras.getString(Constants.USER_NUMBER);
            userEmail = extras.getString(Constants.USER_EMAIL);
            userSource = extras.getString(Constants.USER_SOURCE);
            if (userEmail == null) {
                onShowError(getString(R.string.something_went_wrong_text));
                this.finish();
                userBundle.putString(Constants.USER_EMAIL, null);
                userBundle.putString(Constants.USER_PASSWORD, null);
                userBundle.putString(Constants.USER_OTP, null);
                userBundle.putBoolean(Constants.USER_VALID, false);
            } else {
                userBundle.putString(Constants.USER_EMAIL, userEmail);
                userBundle.putString(Constants.USER_PASSWORD, null);
                userBundle.putString(Constants.USER_OTP, null);
                userBundle.putBoolean(Constants.USER_VALID, true);
            }
        }
    }

    public void onShowError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_reset_password_layout;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onBackPressed() {
        AppUtils.showExitDialog(this, "All your progress will get lost. Do you want to go back?", this);
        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "All your progress will get lost.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);*/
    }

    @Override
    public void onResetPasswordFragmentInteraction() {
    }

    @Override
    public void onPositiveButtonClick() {
        super.onBackPressed();
    }

    @Override
    public void onNegativeButtonClick() {

    }
}
