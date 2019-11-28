package com.enfin.ofabee3.ui.module.popularcourseslist;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;

public class PopularCoursesListContract {

    interface View extends BaseContract.View<Presenter> {
        <T> void onSuccees(T type);
        <T> void onFailure(T type);
    }

    interface Presenter extends BaseContract.Presenter {
        void getpopularcourses(Context context, String categoryIDs, int offset);
    }

}
