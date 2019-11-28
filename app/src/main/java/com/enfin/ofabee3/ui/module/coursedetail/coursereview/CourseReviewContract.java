package com.enfin.ofabee3.ui.module.coursedetail.coursereview;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;


public class CourseReviewContract {
    interface View extends BaseContract.View<CourseReviewContract.Presenter> {
        <T> void onSuccess(T type);
        <T> void onFailure(T type);
    }

    interface Presenter extends BaseContract.Presenter {

    }

}
