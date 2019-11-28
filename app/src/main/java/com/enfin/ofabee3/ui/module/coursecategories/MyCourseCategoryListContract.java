package com.enfin.ofabee3.ui.module.coursecategories;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;

//todo create BaseContract and import to this class
public interface MyCourseCategoryListContract {

    interface View extends BaseContract.View<Presenter> {
        <T> void onSuccees(T type);
        <T> void onFailure(T type);
    }

    interface Presenter extends BaseContract.Presenter {
        void getCoursesCategory(Context context);
        void saveCoursesCategory(Context context, String categoryIDs);
        void getCoursesCategoryWithoutHeader(Context context);
    }
}
