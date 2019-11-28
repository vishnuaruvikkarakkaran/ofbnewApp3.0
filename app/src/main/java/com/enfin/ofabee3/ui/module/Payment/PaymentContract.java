package com.enfin.ofabee3.ui.module.Payment;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;

public class PaymentContract {

    interface View extends BaseContract.View<PaymentContract.Presenter> {
        <T> void onSuccess(T type);

        <T> void onFailure(T type);
    }

    interface Presenter extends BaseContract.Presenter {
        void completePayment(Context context, String courseID, String type, String orderID, String transactioID, String signature);
        void removeCouponCode(Context context);
    }
}
