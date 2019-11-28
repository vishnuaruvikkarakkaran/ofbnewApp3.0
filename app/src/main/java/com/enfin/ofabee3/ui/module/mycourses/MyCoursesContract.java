package com.enfin.ofabee3.ui.module.mycourses;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;

//todo create BaseContract and import to this class
public interface MyCoursesContract {

    interface View extends BaseContract.View<Presenter> {
        <T> void onSuccees(T type);

        <T> void onFailure(T type);

        void logoutAction(String message);
    }

    interface Presenter extends BaseContract.Presenter {
        void getcourses(Context context);
    }
}
