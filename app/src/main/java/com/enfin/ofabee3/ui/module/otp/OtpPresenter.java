package com.enfin.ofabee3.ui.module.otp;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.model.Request.OtpRequestModel;
import com.enfin.ofabee3.data.remote.model.Request.OtpValidateRequestModel;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.utils.AppSignatureHelper;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.NoInternetDialog;
import com.enfin.ofabee3.utils.ObservableObject;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created By Athul on 13-07-2019.
 */
public class OtpPresenter implements OtpContract.Presenter, Observer, NoInternetDialog.RetryListener {

    private OtpContract.MvpView mView;

    OtpPresenter(OtpContract.MvpView view) {
        this.mView = view;
        ObservableObject.getInstance().addObserver(this);
    }

    /////      Presenter Methods      /////

    @Override
    public void onStartSmsRetrieverClient(Context context) {
        //appSignatureHelper.getAppSignatures();
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(context /* context */);

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
                Log.e("auto Sucess", "sucess");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                Log.e("auto excep : ", e.getMessage());
            }
        });

    }

    @Override
    public void onStartCountDown() {
        new CountDownTimer(100000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                String time = String.format(Locale.getDefault(), "%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                mView.onUpdateCountdownTimer(time);
            }

            public void onFinish() {
                mView.onCountDownFinished();
            }
        }.start();
    }

    @Override
    public void onResendOtp(Context context, String number, String email) {
        if (NetworkUtil.isConnected(context)) {
            mView.onShowProgress();
            OtpRequestModel otpRequestModel = new OtpRequestModel();
            otpRequestModel.setPhone(number);
            otpRequestModel.setEmail(email);
            otpRequestModel.setMode("1");
            otpRequestModel.setHash_key(Constants.SMS_HASH_KEY);
            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(otpRequestModel));
            Call<String> call = service.reSendOtp(requestBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    mView.onHideProgress();
                    try {
                        if (response.code() == Constants.SUCCESS) {
                            if (response.body() != null) {
                                Type listType = new TypeToken<LoginResponseModel>() {
                                }.getType();
                                LoginResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                    Toast.makeText(context, "OTP successfully sent", Toast.LENGTH_SHORT).show();
                                    mView.onCountDownStarted();
                                } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                                } else
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                            } else
                                mView.onShowToast(context.getString(R.string.something_went_wrong_text));
                        } else
                            mView.onShowToast(context.getString(R.string.something_went_wrong_text));

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


        } else
            mView.onShowSnackBar(context.getString(R.string.check_internet_text));
    }

    @Override
    public void onValidateOtp(Context context, String name, String countryCode, String number, String email, String password, String otp, String source) {
        if (NetworkUtil.isConnected(context)) {
            mView.onShowProgress();

            OtpValidateRequestModel otpValidateRequestModel = new OtpValidateRequestModel();
            otpValidateRequestModel.setName(name);
            otpValidateRequestModel.setCountry_code(countryCode);
            otpValidateRequestModel.setPhone(number);
            otpValidateRequestModel.setEmail(email);
            otpValidateRequestModel.setPassword(password);
            otpValidateRequestModel.setOtp(otp);
            otpValidateRequestModel.setIdentifier("1");
            otpValidateRequestModel.setMode("1");

            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(otpValidateRequestModel));
            Call<String> call = service.validateOtp(requestBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    mView.onHideProgress();
                    try {
                        if (response.code() == Constants.SUCCESS) {
                            if (response.body() != null) {
                                Type listType = new TypeToken<LoginResponseModel>() {
                                }.getType();
                                LoginResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                    mView.otpVerificationSuccess(source,responseModel);
                                } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                                } else
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                            } else
                                mView.onShowToast(context.getString(R.string.something_went_wrong_text));
                            /*if (response.body() != null) {
                                Type listType = new TypeToken<LoginResponseModel>() {
                                }.getType();
                                LoginResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                //mvpView.onLoginSuccess(responseModel.getBody());
                            } else
                                mView.onShowToast(context.getString(R.string.something_went_wrong_text));*/
                        } else
                            mView.onShowToast(context.getString(R.string.something_went_wrong_text));
                            /*else if (response.code() == Constants.ERROR_404) {
                            if (response.errorBody() != null) {
                                Type listType = new TypeToken<LoginResponseModel>() {
                                }.getType();
                                LoginResponseModel responseModel = new GsonBuilder().create().fromJson(response.errorBody().string(), listType);
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

    public void insertUserData(Context context, LoginResponseModel.DataBean userModel) {
        OBDBHelper dbHelper = new OBDBHelper(context);
        if (dbHelper.insertUserData(userModel)) {
            mView.isSuccessfullyInserted();
        } else {
            mView.insertionFailed();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        mView.onOtpReceived(data.toString());
    }

    @Override
    public void onRetry() {
        mView.onRetry();
    }

    @Override
    public void showNoConnectivityDialog(Context context) {
        NoInternetDialog dialog = new NoInternetDialog(context);
        dialog.setRetryListener(this);
        dialog.show();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
