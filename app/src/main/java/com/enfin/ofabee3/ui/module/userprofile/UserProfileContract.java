package com.enfin.ofabee3.ui.module.userprofile;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;

public interface UserProfileContract {

    interface View extends BaseContract.View<Presenter> {
        <T> void onSuccess(T type);

        <T> void onFailure(T type);

        void logoutAction(String message);

    }

    interface Presenter extends BaseContract.Presenter {
        void getUserDetails(Context context);

        void getProfileDeatails(Context context);

        void logout(Context context);

        void clearLocalDB(Context context);
    }

}
