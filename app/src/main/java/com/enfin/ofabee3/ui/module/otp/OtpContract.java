package com.enfin.ofabee3.ui.module.otp;

import android.content.Context;

import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.ui.base.mvp.BaseContract;
import com.enfin.ofabee3.ui.base.mvp.MvpBase;

/**
 * Created By Athul on 13-08-2019.
 * Defines the contract between the view {@link OtpActivity} and the Presenter
 * {@link OtpPresenter}.
 */
public interface OtpContract {

    interface MvpView extends MvpBase {
        void onOtpReceived(String otp);

        void onCountDownStarted();

        void onCountDownFinished();

        void onUpdateCountdownTimer(String time);

        void otpVerificationSuccess(String userSource, LoginResponseModel userModel);

        void isSuccessfullyInserted();

        void insertionFailed();
    }

    interface Presenter extends BaseContract.Presenter {
        void onStartSmsRetrieverClient(Context context);

        void onStartCountDown();

        void onResendOtp(Context context, String number, String email);

        void onValidateOtp(Context context, String name,String countryCode, String number, String email, String password, String otp, String source);

        void showNoConnectivityDialog(Context context);

        void insertUserData(Context context, LoginResponseModel.DataBean userModel);
    }
}
