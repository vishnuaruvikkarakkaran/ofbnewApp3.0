package com.enfin.ofabee3.ui.module.forgotpassword;

import android.content.Context;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.Request.LoginRequestModel;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.data.remote.model.forgotpassword.request.ForgotPasswordOTPRequestModel;
import com.enfin.ofabee3.data.remote.model.forgotpassword.response.ForgotPasswordOTPResponseModel;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.NoInternetDialog;
import com.enfin.ofabee3.utils.OBLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created By Athul on 13-07-2019.
 */
public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter, NoInternetDialog.RetryListener {

    private ForgotPasswordContract.MvpView mView;

    ForgotPasswordPresenter(ForgotPasswordContract.MvpView view) {
        this.mView = view;
    }

    @Override
    public void showNoConnectivityDialog(Context context) {
        NoInternetDialog dialog = new NoInternetDialog(context);
        dialog.setRetryListener(this);
        dialog.show();
    }

    @Override
    public boolean validateEmailFormat(String email) {
        return AppUtils.isValidEmail(email);
    }

    @Override
    public void sendotp(Context context, String email) {
        if (NetworkUtil.isConnected(context)) {
            mView.onShowProgress();

            ForgotPasswordOTPRequestModel forgotPasswordOTPRequestModel = new ForgotPasswordOTPRequestModel();
            forgotPasswordOTPRequestModel.setEmail(email);
            forgotPasswordOTPRequestModel.setHash_key("");

            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(forgotPasswordOTPRequestModel));
            Call<String> call = service.forgotPasswordGetOTP(requestBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    mView.onHideProgress();
                    try {
                        if (response.code() == Constants.SUCCESS) {
                            if (response.body() != null) {
                                Type listType = new TypeToken<ForgotPasswordOTPResponseModel>() {
                                }.getType();
                                ForgotPasswordOTPResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);

                                if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                    mView.onSuccees(responseModel);
                                } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_404))) {
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                                } else
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                            }
                        } else
                            mView.onShowToast(context.getString(R.string.something_went_wrong_text));

                        /*else if (response.code() == Constants.ERROR_401) {
                            if (response.errorBody() != null) {
                                Type listType = new TypeToken<ForgotPasswordOTPResponseModel>() {
                                }.getType();
                                ForgotPasswordOTPResponseModel responseModel = new GsonBuilder().create().fromJson(response.errorBody().string(), listType);
                                mView.onShowAlertDialog(responseModel.getHeader().getMessage());
                            } else
                                mView.onShowToast(context.getString(R.string.something_went_wrong_text));
                        }*/
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
                        mView.onShowAlertDialog(t.getMessage());
                }
            });
        } else {
            mView.onShowSnackBar(context.getString(R.string.check_internet_text));
        }
    }

    @Override
    public void onRetry() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
