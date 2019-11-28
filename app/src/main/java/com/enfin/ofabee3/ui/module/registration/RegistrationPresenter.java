package com.enfin.ofabee3.ui.module.registration;

import android.content.Context;
import android.util.Log;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.Request.RegistrationRequestModel;
import com.enfin.ofabee3.data.remote.model.Response.RegistrationResponseModel;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NoInternetDialog;
import com.enfin.ofabee3.data.remote.WebApiListener;
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
 * Responsible for handling actions from the view and updating the UI
 * as required.
 */

public class RegistrationPresenter implements RegistrationContact.Presenter, NoInternetDialog.RetryListener {

    private RegistrationContact.MvpView mvpView;

    public RegistrationPresenter(RegistrationContact.MvpView view) {
        this.mvpView = view;
    }

    /////      Presenter Methods      /////

    @Override
    public boolean validateEmailFormat(String email) {
        return AppUtils.isValidEmail(email);
    }

    @Override
    public void showNoConnectivityDialog(Context context) {
        NoInternetDialog dialog = new NoInternetDialog(context);
        dialog.setRetryListener(this);
        dialog.show();
    }

    @Override
    public void validateRegisterFields(Context mContext, String name, String countryCode, String number, String email, String password, boolean isValidEmail) {
        if (name.length() == 0) {
            mvpView.onShowNameError("Enter Name");
        } else if (!AppUtils.isValidName(name)) {
            mvpView.onShowNameError("invalid Name");
        } else if (number.length() == 0)
            mvpView.onShowNumberError("Enter Number");
        else if (email.length() == 0)
            mvpView.onShowEmailError("Enter Email");
        else if (!isValidEmail)
            mvpView.onShowEmailError("Invalid Email");
        else if (password.length() == 0)
            mvpView.onShowPasswordError("Enter Password");
        else if (password.length() < 6)
            mvpView.onShowPasswordError("Password should contain minimum 6 characters");
        else
            callRegistrationApi(mContext, name, countryCode, number, email, password);
    }

    private void callRegistrationApi(Context mContext, String name, String countryCode, String number, String email, String password) {
        /*Create handle for the RetrofitInstance interface*/
        mvpView.onShowProgress();
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);

        RegistrationRequestModel requestModel = new RegistrationRequestModel();
        requestModel.setName(name);
        requestModel.setCountry_code(countryCode);
        requestModel.setPhone(number);
        requestModel.setEmail(email);
        requestModel.setPassword(password);
        requestModel.setHash_key(Constants.SMS_HASH_KEY);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestModel));
        Call<String> call = service.registerUser(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<RegistrationResponseModel>() {
                            }.getType();
                            RegistrationResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);

                            if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                mvpView.onShowToast(responseModel.getMetadata().getMessage());
                                mvpView.onStartOtpVerificationPage(name, number, email, password);
                            } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                mvpView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                            } else
                                mvpView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                        } else
                            mvpView.onShowToast(mContext.getString(R.string.something_went_wrong_text));

                    } else
                        mvpView.onShowToast(mContext.getString(R.string.something_went_wrong_text));

                    mvpView.onHideProgress();
                } catch (JsonSyntaxException e) {
                    mvpView.onHideProgress();
                    Log.e("Register exception : ", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mvpView.onHideProgress();
                if (t instanceof UnknownHostException)
                    mvpView.onShowAlertDialog("No Internet");
                else
                    mvpView.onShowAlertDialog(t.getMessage());
            }

        });


    }

    //No internet dialog retry button click call back method.
    @Override
    public void onRetry() {
        mvpView.onRetry();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
