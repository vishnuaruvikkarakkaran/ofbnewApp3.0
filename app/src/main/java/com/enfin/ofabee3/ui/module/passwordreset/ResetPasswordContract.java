package com.enfin.ofabee3.ui.module.passwordreset;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;

//todo create BaseContract and import to this class
public interface ResetPasswordContract {

    interface View extends BaseContract.View<Presenter> {
        <T> void onSuccees(Context context, T type);

        <T> void onFailure(Context context, T type);

        void onShowError(String message);
    }

    interface Presenter extends BaseContract.Presenter {
        //void updatePassword(Context context, String email, String password, String otp);
        void updatePassword(Context context, String name,String countryCode, String number, String email, String password, String otp, String source);

    }
}
