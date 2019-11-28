package com.enfin.ofabee3.ui.module.Payment;

import android.content.Context;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.coursedetail.request.CourseDetailRequestModel;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.BundleDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.CourseDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.createorder.request.CreateOrderRequestModel;
import com.enfin.ofabee3.data.remote.model.createorder.response.CreateOrderResponse;
import com.enfin.ofabee3.data.remote.model.payment.request.PaymentRequestModel;
import com.enfin.ofabee3.data.remote.model.payment.response.PaymentResponseModel;
import com.enfin.ofabee3.data.remote.model.removecoupon.response.CouponRemoveResponse;
import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentPresenter extends BasePresenter implements PaymentContract.Presenter {

    private PaymentContract.View mView;
    private Context mContext;
    private String token;

    public PaymentPresenter(PaymentContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void completePayment(Context context, String courseID, String type, String orderID, String transactioID, String signature) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        token = obdbHelper.getAccessToken();
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        PaymentRequestModel request = new PaymentRequestModel();
        request.setItem_id(courseID);
        request.setItem_type(type);
        request.setRazorpay_order_id(orderID);
        request.setRazorpay_payment_id(transactioID);
        request.setRazorpay_signature(signature);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(request));
        Call<String> call = service.completePayment(Constants.BEARER + token, requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mView.onHideProgress();
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<PaymentResponseModel>() {
                            }.getType();
                            PaymentResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                mView.onSuccess(responseModel);
                            } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                            } else
                                mView.onShowAlertDialog(responseModel.getMetadata().getMessage());

                        } else
                            mView.onShowAlertDialog(context.getString(R.string.something_went_wrong_text));

                    } else
                        mView.onShowAlertDialog(context.getString(R.string.something_went_wrong_text));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mView.onHideProgress();
                if (t instanceof UnknownHostException)
                    mView.onShowAlertDialog("No Internet");
                else
                    mView.onServerError(t.getMessage());
            }
        });
    }

    @Override
    public void removeCouponCode(Context context) {
        //mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        token = obdbHelper.getAccessToken();
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        Call<String> call = service.removecoupon(Constants.BEARER + token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //mView.onHideProgress();
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<CouponRemoveResponse>() {
                            }.getType();
                            CouponRemoveResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                {
                                    //mView.onSuccess(responseModel);
                                }
                            } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_204))) {
                                {
                                    //mView.onFailure(responseModel.getMetadata().getMessage());
                                }
                            } else
                            {
                                //mView.onFailure(responseModel.getMetadata().getMessage());
                            }
                        } else
                        {
                            //mView.onShowAlertDialog(context.getString(R.string.something_went_wrong_text));
                        }

                    } else
                    {
                        //mView.onShowAlertDialog(context.getString(R.string.something_went_wrong_text));
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //mView.onHideProgress();
                if (t instanceof UnknownHostException)
                    mView.onShowAlertDialog("No Internet");
                else
                    mView.onShowAlertDialog(t.getMessage());
            }
        });
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

}
