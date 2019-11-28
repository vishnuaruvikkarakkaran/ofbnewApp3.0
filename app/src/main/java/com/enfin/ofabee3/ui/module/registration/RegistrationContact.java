package com.enfin.ofabee3.ui.module.registration;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;
import com.enfin.ofabee3.ui.base.mvp.MvpBase;

/**
 * Created By Athul on 14-07-2019.
 * Defines the contract between the view {@link RegistrationActivity} and the Presenter
 * {@link RegistrationPresenter}.
 */
public interface RegistrationContact {
    interface MvpView extends MvpBase {

        void onRetry();
        void onShowNameError(String message);
        void onShowNumberError(String message);
        void onShowEmailError(String message);
        void onShowPasswordError(String message);
        void onStartOtpVerificationPage(String name, String number, String email, String password);

    }

    interface Presenter extends BaseContract.Presenter {

        boolean validateEmailFormat(String email);
        void validateRegisterFields(Context context, String name, String countryCode, String number,String email, String password, boolean isValidEmail);
        void showNoConnectivityDialog(Context context);
    }
}
