package com.enfin.ofabee3.ui.module.orderpreview;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;


public class OrderPreviewContract {
    interface View extends BaseContract.View<OrderPreviewContract.Presenter> {
        <T> void onSuccess(T type);

        <T> void onFailure(T type);

        void paymentFailed();
    }

    interface Presenter extends BaseContract.Presenter {
        void applyCouponCode(Context context, String couponCode);

        void removeCouponCode(Context context);

        void createOrder(Context context, String courseID, String type);
    }
}
