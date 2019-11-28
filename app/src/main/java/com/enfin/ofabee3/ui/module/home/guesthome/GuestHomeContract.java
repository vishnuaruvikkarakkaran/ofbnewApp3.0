package com.enfin.ofabee3.ui.module.home.guesthome;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;

//todo create BaseContract and import to this class
public interface GuestHomeContract {

    interface View extends BaseContract.View<Presenter> {
        <T> void onSuccees(T type);
        <T> void onFailure(T type);
        void noDataFound();
    }

    interface Presenter extends BaseContract.Presenter {
        void getHomeData(Context context);
    }
}
