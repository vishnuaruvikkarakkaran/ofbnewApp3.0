package com.enfin.ofabee3.ui.module.forgotpassword;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;
import com.enfin.ofabee3.ui.base.mvp.MvpBase;

public interface ForgotPasswordContract {

    interface MvpView extends MvpBase{
        <T> void onSuccees(T type);

        <T> void onFailure(T type);
    }

    interface Presenter extends BaseContract.Presenter{

        void showNoConnectivityDialog(Context context);
        boolean validateEmailFormat(String email);
        void sendotp(Context context, String email);
    }

}
