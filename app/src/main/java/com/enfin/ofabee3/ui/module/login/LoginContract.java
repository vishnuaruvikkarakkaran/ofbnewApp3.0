package com.enfin.ofabee3.ui.module.login;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;
import com.enfin.ofabee3.ui.base.mvp.MvpBase;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;

/**
 * Created By Athul on 14-07-2019.
 * Defines the contract between the view {@link LoginActivity} and the Presenter
 * {@link LoginPresenter}.
 */
public interface LoginContract {

    interface MvpView extends MvpBase {

        void onUserCountry(String country);

        void onShowErrorEmailAlert(String message);

        void onShowErrorPasswordAlert(String message);

        void onLoginSuccess(LoginResponseModel userModel);

        void onLoginFailure(String message);

        void onShowErrorNumberAlert(String message);

        void onRetry();

        void isSuccessfullyInserted();

        void insertionFailed();
    }

    interface Presenter extends BaseContract.Presenter {

        void showNoConnectivityDialog(Context context);

        void checkUserCountry(Context context);

        boolean validateEmailFormat(String email);

        void validateEmailAndPasswordForLogin(Context context, String email, String password, boolean isValidEmail);

        void validateNumberAndPasswordForLogin(Context context, String countryCode, String number, String password);

        void startActivity(Context context, String country, Class<?> intentClass);

        void insertUserData(Context context, LoginResponseModel.DataBean userModel);

    }
}
