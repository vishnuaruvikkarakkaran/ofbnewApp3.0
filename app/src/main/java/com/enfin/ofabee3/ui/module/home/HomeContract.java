package com.enfin.ofabee3.ui.module.home;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;

//todo create BaseContract and import to this class
public interface HomeContract {

    interface View extends BaseContract.View<Presenter> {
        <T> void onSuccees(T type);

        <T> void onFailure(T type);

        void noDataFound();

        void logoutAction(String message);
    }

    interface Presenter extends BaseContract.Presenter {
        void getHomeData(Context context);
        void getmycourses(Context context);
        void clearLocalDB(Context context);    }
}
