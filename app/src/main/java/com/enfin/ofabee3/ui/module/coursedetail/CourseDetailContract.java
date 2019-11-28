package com.enfin.ofabee3.ui.module.coursedetail;

import android.content.Context;

import com.enfin.ofabee3.data.remote.model.selfenroll.response.SelfEnrollResponse;
import com.enfin.ofabee3.ui.base.mvp.BaseContract;
import com.enfin.ofabee3.ui.module.explore.ExploreContract;

public class CourseDetailContract {

    interface View extends BaseContract.View<CourseDetailContract.Presenter> {
        <T> void onSuccess(T type);
        <T> void onFailure(T type);
        void onSuccessSelfEnroll(SelfEnrollResponse response);
        void invokeLogin();
    }

    interface Presenter extends BaseContract.Presenter {
        void getCourseDetail(Context context,String courseID, String type);
        void getSharedCourseDetail(Context context,String courseID, String type);
        void createOrder(Context context,String courseID, String type);
        void selfEnroll(Context context,String courseID, String type);
    }
}
