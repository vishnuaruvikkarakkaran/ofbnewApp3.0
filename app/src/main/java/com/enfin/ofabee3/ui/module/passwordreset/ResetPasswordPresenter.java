package com.enfin.ofabee3.ui.module.passwordreset;

import android.content.Context;

import androidx.annotation.NonNull;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.Request.OtpValidateRequestModel;
import com.enfin.ofabee3.data.remote.model.forgotpassword.request.ResetPasswordRequestModel;
import com.enfin.ofabee3.data.remote.model.forgotpassword.response.ResetPasswordResponse;
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

public class ResetPasswordPresenter extends BasePresenter implements ResetPasswordContract.Presenter {

    private ResetPasswordContract.View mView;

    private Context mContext;

    public ResetPasswordPresenter(@NonNull Context context, @NonNull ResetPasswordContract.View view) {
        this.mView = view;
        this.mContext = context;
        this.mView.setPresenter(this);
    }

    @Override
    public void updatePassword(Context context, String name,String countryCode, String number, String email, String password, String otp, String source) {
        mView.onShowProgress();

        OtpValidateRequestModel requestModel = new OtpValidateRequestModel();
        requestModel.setCountry_code(countryCode);
        requestModel.setPhone(number);
        requestModel.setEmail(email);
        requestModel.setPassword(password);
        requestModel.setOtp(otp);
        requestModel.setIdentifier("2");
        requestModel.setMode("2");

        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestModel));
        Call<String> call = service.validateOtp(requestBody);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mView.onShowProgress();
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<ResetPasswordResponse>() {
                            }.getType();
                            ResetPasswordResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            mView.onSuccees(context, responseModel);
                        } else {
                            mView.onFailure(context, context.getString(R.string.something_went_wrong_text));
                        }
                    } else if (response.code() == Constants.ERROR_404) {
                    }
                    mView.onHideProgress();
                } catch (JsonSyntaxException e) {
                    mView.onHideProgress();
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

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
