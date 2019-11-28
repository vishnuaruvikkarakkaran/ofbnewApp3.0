package com.enfin.ofabee3.ui.module.bundlesubject;

import android.app.Activity;
import android.content.Context;

import com.enfin.ofabee3.data.remote.model.selfenroll.response.SelfEnrollResponse;
import com.enfin.ofabee3.ui.base.mvp.BaseContract;
import com.enfin.ofabee3.ui.module.coursedetail.CourseDetailContract;

public class BundleSubjectContract {

    interface View extends BaseContract.View<BundleSubjectContract.Presenter> {
        <T> void onSuccess(T type);
        <T> void onFailure(T type);
    }

    interface Presenter extends BaseContract.Presenter {
        void getCourseDetail(Activity context, String courseID, String type);
    }

}
