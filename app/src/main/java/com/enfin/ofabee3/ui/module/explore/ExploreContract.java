package com.enfin.ofabee3.ui.module.explore;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;


public class ExploreContract {
    interface View extends BaseContract.View<ExploreContract.Presenter> {
        <T> void onSuccess(T type);

        <T> void onFailure(T type);

        void logoutAction(String message);
    }

    interface Presenter extends BaseContract.Presenter {
        void getCourses(Context context, String categoryIDs, int offset);
        void getCoursesCategoryWithoutHeader(Context context);
        void getCoursesCategory(Context context);
    }

}
