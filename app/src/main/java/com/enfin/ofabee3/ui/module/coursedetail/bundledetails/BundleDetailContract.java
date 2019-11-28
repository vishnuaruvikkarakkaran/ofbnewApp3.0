package com.enfin.ofabee3.ui.module.coursedetail.bundledetails;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;

public class BundleDetailContract {

    interface View extends BaseContract.View<BundleDetailContract.Presenter> {
        <T> void onSuccess(T type);
        <T> void onFailure(T type);
    }

    interface Presenter extends BaseContract.Presenter {
        void getBundleDetail(Context context, String courseID, String type);
    }
}
